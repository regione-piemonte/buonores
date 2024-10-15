/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbatch;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.ws.WebServiceException;

import org.apache.commons.lang3.exception.ExceptionUtils;

import org.codehaus.jackson.map.ObjectMapper;

import it.csi.buonores.buonoresbatch.dao.BuonoresBatchDAO;
import it.csi.buonores.buonoresbatch.dao.BuonoresBatchLogDAO;
import it.csi.buonores.buonoresbatch.dao.DBConnectionManager;
import it.csi.buonores.buonoresbatch.dto.ModelRicezioneSegnalazione;
import it.csi.buonores.buonoresbatch.dto.RichiestaDto;
import it.csi.buonores.buonoresbatch.exception.ConnectionException;
import it.csi.buonores.buonoresbatch.exception.DatabaseConnectionException;
import it.csi.buonores.buonoresbatch.external.anagraficaservice.InterrogaMefEsenredd1Soap;
import it.csi.buonores.buonoresbatch.external.anagraficaservice.InterrogaMefEsenreddRes;
import it.csi.buonores.buonoresbatch.external.anagraficaservice.ServiziEsterniClient;
import it.csi.buonores.buonoresbatch.integration.rest.RestBaseService;
import it.csi.buonores.buonoresbatch.logger.BatchLoggerFactory;
import it.csi.buonores.buonoresbatch.util.Checker;
import it.csi.buonores.buonoresbatch.util.Constants;
import it.csi.buonores.buonoresbatch.util.Converter;
import it.csi.buonores.buonoresbatch.util.rest.ResponseRest;

public class Main {
	private Connection conn;
	private BuonoresBatchDAO dao = null;
	private ServiziEsterniClient serviziEsterni = null;
	private RestBaseService restbase = null;
	private BuonoresBatchLogDAO batchDao = null;
	private int systemReturn = 0;

	public static void main(String[] args) {
		new Main().run();
	}

	/**
	 * Crea la connessione a db
	 * 
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private void init() throws DatabaseConnectionException {
		try {
			BatchLoggerFactory.getLogger(this).info("--- CONNESSIONE AL DATABASE IN CORSO ---");
			this.conn = DBConnectionManager.getConnection();
			this.conn.setAutoCommit(false);
			dao = new BuonoresBatchDAO(this.conn);
			batchDao = new BuonoresBatchLogDAO(this.conn);
			serviziEsterni = ServiziEsterniClient.getInstance(dao);
			restbase = RestBaseService.getInstance(dao);
			BatchLoggerFactory.getLogger(this).info("--- CONNESSIONE EFFETTUATA ---");
		} catch (Exception e) {
			throw new DatabaseConnectionException("Database connection fail: " + e.getMessage(), e);
		}

	}

	private void run() {
		try {
			BatchLoggerFactory.getLogger(this).info("--- AVVIO BATCH ---");
			// Stabilisco la connessione con il Database
			init();
			BatchLoggerFactory.getLogger(this).info("--- PRENDO ELENCO CF CON RICHIESTE APERTE ---");
			ResponseRest response = new ResponseRest();
			Integer contadeceduti = 0;
			Integer contacambioresidenza = 0;
			List<RichiestaDto> richieste = new ArrayList<RichiestaDto>();
			boolean eseguoresidenza = dao.getParametro(Constants.BATCH_CONTROLLO_DECESSO_RESIDENZA, Constants.GENERICO)
					.toLowerCase().equalsIgnoreCase("true") ? true : false;
			// prendo il parametro della pausa per interrogamef
			Integer pausa = Converter.getInt(dao.getParametro(Constants.PAUSA, Constants.TIPO_PARAMETRO_GIORNI));
			if (eseguoresidenza) {
				richieste = dao.getRichieste();
				InterrogaMefEsenredd1Soap port = serviziEsterni.creaServizio();
				dao.insertAudit("Inizio Batch", "Conteggio pratiche revocate decesso o cambio residenza", 0);
				ModelRicezioneSegnalazione segnalazione = new ModelRicezioneSegnalazione();
				for (RichiestaDto rich : richieste) {
					try {
						// verifico se lo stato della domanda proviene da un pagamento emesso
						// metto una pausa prima di chiamare interrogamef
						Thread.sleep(pausa);
						InterrogaMefEsenreddRes dati = serviziEsterni.chiamaGetInterrogaMef(rich.getCf(), port);
						if (dati != null && dati.getEsitomef() != null && dati.getEsitomef().equalsIgnoreCase("OK")) {
							if (dati.getBody() != null) {
								if (Checker.isValorizzato(dati.getBody().getDatadecesso())) {
									BatchLoggerFactory.getLogger(this).error("--- Cf " + rich.getCf() + " --- NumeroDomanda "
											+ rich.getDomandaNumero() + " --- deceduto in data " + dati.getBody().getDatadecesso());
									// aggiorno la colonna data decesso
									dao.updateDataDecessoRes(rich.getDomandaDetId(),
											new java.sql.Date(
													Converter.getData(dati.getBody().getDatadecesso()).getTime()),
											Constants.DEC);
									// per quelle in pagamento mando solo la segnalazione a bandi
									if (rich.getStato().equalsIgnoreCase(Constants.IN_PAGAMENTO)) {
										boolean esisteBuono = dao.esisteBuono(rich.getSportelloId(),
												rich.getDomandaId(), Constants.ATTIVO);
										if (esisteBuono) {
											// aggiungo la parte di invio a bandi della segnalazione di decesso
											segnalazione = new ModelRicezioneSegnalazione();
											segnalazione.setCodiceNotifica(Constants.DEC);
											segnalazione.setData(Converter.getDataString(
													Converter.getData(dati.getBody().getDatadecesso()),
													Constants.DATE_FORMAT));
											segnalazione.setNumeroDomanda(rich.getDomandaNumero());
											segnalazione.setDescrizioneNotifica(dao.selectDescMotivo(Constants.DEC));
											ObjectMapper mapper = new ObjectMapper();
											String jsonSegnalazione = mapper.writeValueAsString(segnalazione);
											response = new ResponseRest();
											response = setSegnalazioneBandi(rich.getCf(), jsonSegnalazione,
													rich.getDomandaNumero(), "DECESSO", "SEGNALAZIONE_DECESSO",
													"SEGNALAZIONE_DECESSO");
											BatchLoggerFactory.getLogger(this)
											.info("Chiamata di segnalazione a Bandi per decesso per domanda "
													+ rich.getDomandaNumero() + " - " + response.toString());
										}
									} else {
										// revoco la richiesta per decesso
										// se la domanda ammessa o ammessa riserva e la graduatoria provvisoria devo
										// aggiornarla
											if (rich.getStato().equalsIgnoreCase(Constants.AMMESSA)
													|| rich.getStato().equalsIgnoreCase(Constants.AMMESSA_RISERVA)) {
												long idGraduatoria = dao.selectGraduatoriaIdByDomandaDettaglioId(
														rich.getDomandaDetId());
												if (idGraduatoria != 0) {
													if (dao.checkStatoGraduatoria(idGraduatoria,
															Constants.PROVVISORIA)) {
														dao.updateRGraduatoriaStato(idGraduatoria,
																Constants.DA_AGGIORNARE, Constants.XCODICESERVIZIO);
													}
												}
											}
											dao.executeCambioStatoDomanda(rich.getDomandaDetId(), Constants.DINIEGO,
													"decesso del destinatario", rich.getDomandaNumero());
											response = new ResponseRest();
											response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(),
													"DECESSO", "DECESSO");
											BatchLoggerFactory.getLogger(this)
													.info("Chiamata a Crea Domanda per domanda "
															+ rich.getDomandaNumero() + " - " + response.toString());
											response = new ResponseRest();
											response = getCreaLettera(rich.getDomandaNumero(), rich.getRichiedenteCf(),
													Constants.DINIEGO_SCADENZA_REQUISITI_DECESSO, "DECESSO", "DECESSO");
											BatchLoggerFactory.getLogger(this)
													.info("Chiamata a Crea Lettera per domanda "
															+ rich.getDomandaNumero() + "- " + response.toString());
											response = new ResponseRest();
											response = getStartDasPartenza(rich.getDomandaNumero(),
													rich.getRichiedenteCf(), Constants.LETTERA_DINIEGO, "DECESSO",
													"DECESSO");
											BatchLoggerFactory.getLogger(this)
													.info("Chiamata a Stardas Partenza per domanda "
															+ rich.getDomandaNumero() + " - " + response.toString());
											response = new ResponseRest();
											response = getInviaNotifica(rich.getDomandaNumero(),
													Constants.BO_DINIEGO_SCADENZA_REQUISITI_DECESSO,
													rich.getRichiedenteCf(), "DECESSO", "DECESSO");
											BatchLoggerFactory.getLogger(this)
													.info("Chiamata a Invia Notifica per domanda "
															+ rich.getDomandaNumero() + " - " + response.toString());
										
									}
									contadeceduti = contadeceduti + 1;
									dao.insertAudit(
											rich.getCf() + " deceduto in data " + dati.getBody().getDatadecesso(),
											dati.toString(), 200);

								} else { // controllo residenza
									BatchLoggerFactory.getLogger(this)
											.info("--- Cf " + rich.getCf() + " --- NumeroDomanda "
													+ rich.getDomandaNumero() + " --- non deceduto - controllo residenza ");
									if (dati.getBody().getProvinciaResidenza() != null) {
										if (!dao.IsPiemontese(dati.getBody().getProvinciaResidenza())) {
											// aggiorno la colonna data residenza
											dao.updateDataDecessoRes(rich.getDomandaDetId(),
													new java.sql.Date(new Date().getTime()), Constants.CR);
											BatchLoggerFactory.getLogger(this)
													.info("--- Cf " + rich.getCf() + " --- NumeroDomanda " + rich.getDomandaNumero() + 
															" --- cambio residenza");
											// per quelle in pagamento mando solo la segnalazione a bandi
											if (rich.getStato().equalsIgnoreCase(Constants.IN_PAGAMENTO)) {
												boolean esisteBuono = dao.esisteBuono(rich.getSportelloId(),
														rich.getDomandaId(), Constants.ATTIVO);
												if (esisteBuono) {
													// aggiungo la parte di invio a bandi della segnalazione di decesso
													segnalazione = new ModelRicezioneSegnalazione();
													segnalazione.setCodiceNotifica(Constants.CR);
													segnalazione.setData(
															Converter.getDataString(new Date(), Constants.DATE_FORMAT));
													segnalazione.setNumeroDomanda(rich.getDomandaNumero());
													segnalazione
															.setDescrizioneNotifica(dao.selectDescMotivo(Constants.CR));
													ObjectMapper mapper = new ObjectMapper();
													String jsonSegnalazione = "";
													jsonSegnalazione = mapper.writeValueAsString(segnalazione);
													response = new ResponseRest();
													response = setSegnalazioneBandi(rich.getCf(), jsonSegnalazione,
															rich.getDomandaNumero(), "CAMBIO_RESIDENZA",
															"SEGNALAZIONE_CAMBIO_RESIDENZA",
															"SEGNALAZIONE_CAMBIO_RESIDENZA");
													BatchLoggerFactory.getLogger(this)
													.info("Chiamata di segnalazione a Bandi per cambio residenza per domanda "
															+ rich.getDomandaNumero() + " - " + response.toString());
												}
											} else {
												// revoco la richiesta per cambio residenza
												// se la domanda ammessa o ammessa riserva e la graduatoria provvisoria
												// devo aggiornarla
													if (rich.getStato().equalsIgnoreCase(Constants.AMMESSA) || rich
															.getStato().equalsIgnoreCase(Constants.AMMESSA_RISERVA)) {
														long idGraduatoria = dao
																.selectGraduatoriaIdByDomandaDettaglioId(
																		rich.getDomandaDetId());
														if (idGraduatoria != 0) {
															if (dao.checkStatoGraduatoria(idGraduatoria,
																	Constants.PROVVISORIA)) {
																dao.updateRGraduatoriaStato(idGraduatoria,
																		Constants.DA_AGGIORNARE,
																		Constants.XCODICESERVIZIO);
															}
														}
													}
													dao.executeCambioStatoDomanda(rich.getDomandaDetId(),
															Constants.DINIEGO,
															"cambio di residenza fuori Piemonte del destinatario",
															rich.getDomandaNumero());
													response = new ResponseRest();
													response = getCreaDomanda(rich.getDomandaNumero(),
															rich.getRichiedenteCf(), "CAMBIO_RESIDENZA",
															"CAMBIO_RESIDENZA");
													BatchLoggerFactory.getLogger(this)
															.info("Chiamata a Crea Domanda per domanda "
																	+ rich.getDomandaNumero() + " - "
																	+ response.toString());
													response = new ResponseRest();
													response = getCreaLettera(rich.getDomandaNumero(),
															rich.getRichiedenteCf(),
															Constants.DINIEGO_SCADENZA_REQUISITI_CAMBIO_RESIDENZA,
															"CAMBIO_RESIDENZA", "CAMBIO_RESIDENZA");
													BatchLoggerFactory.getLogger(this)
															.info("Chiamata a Crea Lettera per domanda "
																	+ rich.getDomandaNumero() + " - "
																	+ response.toString());
													response = new ResponseRest();
													response = getStartDasPartenza(rich.getDomandaNumero(),
															rich.getRichiedenteCf(), Constants.LETTERA_DINIEGO,
															"CAMBIO_RESIDENZA", "CAMBIO_RESIDENZA");
													BatchLoggerFactory.getLogger(this)
															.info("Chiamata a Stardas Partenza per domanda "
																	+ rich.getDomandaNumero() + "- "
																	+ response.toString());
													response = new ResponseRest();
													response = getInviaNotifica(rich.getDomandaNumero(),
															Constants.BO_DINIEGO_SCADENZA_REQUISITI_CAMBIO_RESIDENZA,
															rich.getRichiedenteCf(), "CAMBIO_RESIDENZA",
															"CAMBIO_RESIDENZA");
													BatchLoggerFactory.getLogger(this)
															.info("Chiamata a Invia Notifica per domanda "
																	+ rich.getDomandaNumero() + " - "
																	+ response.toString());
												
											}
											contacambioresidenza = contacambioresidenza + 1;
											dao.insertAudit(
													rich.getCf() + " cambio residenza in "
															+ dati.getBody().getComuneResidenza() + " ("
															+ dati.getBody().getProvinciaResidenza() + ")",
													dati.toString(), 200);
										} else {
											// residenza non cambiata
											BatchLoggerFactory.getLogger(this)
													.info("--- Cf " + rich.getCf() + " --- NumeroDomanda " 
															+ rich.getDomandaNumero() + " --- residenza non cambiata "
															+ dati.getBody().getProvinciaResidenza());
										}
									}
								}
							}
						} else {
							BatchLoggerFactory.getLogger(this)
									.warn("--- Cf " + rich.getCf() + " --- NumeroDomanda " + rich.getDomandaNumero()
											+ " --- Non è stato possibile effettuare il controllo verso Mef");
						}

					} catch (Exception e) {
						BatchLoggerFactory.getLogger(this)
								.error("--- Cf " + rich.getCf() + " --- NumeroDomanda " + rich.getDomandaNumero() 
										+ " --- Errore nella gestione della domanda deceduto o residente - ", e);
						systemReturn = 4;
						continue;
					}
				}
				dao.insertAudit("Fine Batch", "Numero di deceduti " + contadeceduti.toString(), 200);
				dao.insertAudit("Fine Batch", "Numero di cambi residenza " + contacambioresidenza.toString(), 200);
			}
			// per la rettifica selezionare ultimo stato se rettifica conta i giorni se in
			// rettifica prendi lo stato da rettificare con data inzio validita max
			dao.insertAudit("Inizio Batch", "Conteggio pratiche per avvisi", 0);
//			//ciclo per le pratiche su cui mandare notifiche
			Integer avvisirettifica = 0;
			Integer avvisicontrodedotta = 0;
			Integer avvisiammessariserva = 0;
			Integer avvisiammessa = 0;
			Integer numerogiornirettifica = Converter
					.getInt(dao.getParametro(Constants.DA_RETTIFICARE_GIORNI, Constants.TIPO_PARAMETRO_GIORNI));
			Integer numerogiornicontrodedotta = Converter
					.getInt(dao.getParametro(Constants.CONTRODEDOTTA_GIORNI, Constants.TIPO_PARAMETRO_GIORNI));
			Integer avvisiammessaapreavviso = 0;
			Integer avvisipreavvisodiniego = 0;
			Integer numerogiorniammessaapreavviso = Converter.getInt(dao
					.getParametro(Constants.AMMESSA_RISERVA_PREAVVISO_DINIEGO_GIORNI, Constants.TIPO_PARAMETRO_GIORNI));
			Integer numerogiornipreavvisodiniego = Converter.getInt(
					dao.getParametro(Constants.PREAVVISO_DINIEGO_A_DINIEGO_GIORNI, Constants.TIPO_PARAMETRO_GIORNI));
			boolean notificaammessariservapagamento = dao
					.getParametro(Constants.NOTIFICA_AMMESSA_RISERVA_IN_PAGAMENTO, Constants.TIPO_PARAMETRO_GIORNI)
					.toLowerCase().equalsIgnoreCase("true") ? true : false;
			Long idsportellochiuso = dao.getUltimoSportelloChiuso();
			richieste = dao.getRichieste();
			for (RichiestaDto rich : richieste) {
				try {
					if ((rich.getStato().equalsIgnoreCase(Constants.DA_RETTIFICARE)
							&& rich.getGiorni() > numerogiornirettifica)
							|| (rich.getStato().equalsIgnoreCase(Constants.IN_RETTIFICA)
									&& dao.getGiorniStatoPrecedente(rich.getDomandaNumero(),
											Constants.DA_RETTIFICARE) > numerogiornirettifica)) {
						dao.executeCambioStatoDomanda(rich.getDomandaDetId(), Constants.NON_AMMISSIBILE,
								"Non ammessa per scadenza dei " + numerogiornirettifica + " giorni di rettifica",
								rich.getDomandaNumero());
						avvisirettifica = avvisirettifica + 1;
						response = new ResponseRest();
						response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(), "CAMBIO_STATO",
								"CAMBIO_STATO_DOMANDA_" + Constants.DA_RETTIFICARE);
						BatchLoggerFactory.getLogger(this).info("Chiamata a Crea Domanda per domanda "
								+ rich.getDomandaNumero() + " - " + response.toString());
						dao.insertAudit(
								"Domanda numero " + rich.getDomandaNumero() + " richiedente " + rich.getRichiedenteCf()
										+ " destinatario " + rich.getCf(),
								"Non ammessa per scadenza dei " + numerogiornirettifica + " giorni di rettifica", 200);
					} else if ((rich.getStato().equalsIgnoreCase(Constants.PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA)
							&& rich.getGiorni() > numerogiornicontrodedotta)) {
						dao.executeCambioStatoDomanda(
								rich.getDomandaDetId(), Constants.NON_AMMISSIBILE, "Non ammessa per scadenza dei "
										+ numerogiornicontrodedotta + " giorni consentiti per la controdeduzione",
								rich.getDomandaNumero());
						avvisicontrodedotta = avvisicontrodedotta + 1;
						response = new ResponseRest();
						response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(), "CAMBIO_STATO",
								"CAMBIO_STATO_DOMANDA_" + Constants.PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA);
						BatchLoggerFactory.getLogger(this).info("Chiamata a Crea Domanda per domanda "
								+ rich.getDomandaNumero() + " - " + response.toString());
						dao.insertAudit(
								"Domanda numero " + rich.getDomandaNumero() + " richiedente " + rich.getRichiedenteCf()
										+ " destinatario " + rich.getCf(),
								"Non ammessa per scadenza dei " + numerogiornicontrodedotta
										+ " giorni consentiti per la controdeduzione",
								200);
					} else if ((rich.getStato().equalsIgnoreCase(Constants.AMMESSA_RISERVA_IN_PAGAMENTO)
							&& rich.getGiorni() > numerogiorniammessaapreavviso)) {
						dao.executeCambioStatoDomanda(rich.getDomandaDetId(), Constants.PREAVVISO_DINIEGO_IN_PAGAMENTO,
								"Preavviso di diniego per scadenza dei " + avvisiammessaapreavviso
										+ " giorni consentiti per il perfezionamento",
								rich.getDomandaNumero());
						avvisiammessaapreavviso = avvisiammessaapreavviso + 1;
						response = new ResponseRest();
						response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(), "CAMBIO_STATO",
								"CAMBIO_STATO_DOMANDA_" + Constants.AMMESSA_RISERVA_IN_PAGAMENTO);
						BatchLoggerFactory.getLogger(this).info("Chiamata a Crea Domanda per domanda "
								+ rich.getDomandaNumero() + " - " + response.toString());
						response = new ResponseRest();
						response = getInviaNotifica(rich.getDomandaNumero(),
								Constants.BO_PREAVVISO_DI_DINIEGO_IN_PAGAMENTO, rich.getRichiedenteCf(), "CAMBIO_STATO",
								"CAMBIO_STATO_DOMANDA_" + Constants.AMMESSA_RISERVA_IN_PAGAMENTO);
						BatchLoggerFactory.getLogger(this).info("Chiamata a Invia Notifica per domanda "
								+ rich.getDomandaNumero() + " - " + response.toString());
						dao.insertAudit(
								"Domanda numero " + rich.getDomandaNumero() + " richiedente " + rich.getRichiedenteCf()
										+ " destinatario " + rich.getCf(),
								"Preavviso di diniego per scadenza dei " + avvisiammessaapreavviso
										+ " giorni consentiti per il perfezionamento",
								200);
					}
					// verifica caso ammessa con riserva in pagamento che proviene dalla
					// pubblicazione per partenza di notifica 30 giorni
					// passo che va fatto solo se non esiste esito positivo
					else if (rich.getStato().equalsIgnoreCase(Constants.AMMESSA_RISERVA_IN_PAGAMENTO)) {
						response = new ResponseRest();
						response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(), "CAMBIO_STATO",
								"CAMBIO_STATO_DOMANDA_" + Constants.AMMESSA_RISERVA);
						BatchLoggerFactory.getLogger(this)
								.info("Chiamata a Crea Domanda per domanda " + rich.getDomandaNumero() + " - "
										+ response.toString());
						response = new ResponseRest();
						response = getCreaLettera(rich.getDomandaNumero(), rich.getRichiedenteCf(),
								Constants.LETTERA_AMMISSIONE_FINANZIAMENTO, "CAMBIO_STATO",
								"CAMBIO_STATO_DOMANDA_" + Constants.AMMESSA_RISERVA);
						BatchLoggerFactory.getLogger(this).info("Chiamata a Crea Lettera per domanda "
								+ rich.getDomandaNumero() + " - " + response.toString());
						if (notificaammessariservapagamento) {
							response = new ResponseRest();
							response = getStartDasPartenza(rich.getDomandaNumero(), rich.getRichiedenteCf(),
									Constants.LETTERA_AMMISSIONE_FINANZIAMENTO, "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.AMMESSA_RISERVA);
							BatchLoggerFactory.getLogger(this).info("Chiamata a Stardas Partenza per domanda "
									+ rich.getDomandaNumero() + " - " + response.toString());
							response = new ResponseRest();
							response = getInviaNotifica(rich.getDomandaNumero(), Constants.BO_AMMESSA_RISERVA_CONTRATTO,
									rich.getRichiedenteCf(), "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.AMMESSA_RISERVA);
							BatchLoggerFactory.getLogger(this).info("Chiamata a Invia Notifica per domanda "
									+ rich.getDomandaNumero() + " - " + response.toString());
						}
						dao.insertAudit(
								"Domanda numero " + rich.getDomandaNumero() + " richiedente " + rich.getRichiedenteCf()
										+ " destinatario " + rich.getCf(),
								"Ammessa con riserva in pagamento 30 giorni consentiti per il perfezionamento", 200);
					} else if ((rich.getStato().equalsIgnoreCase(Constants.PREAVVISO_DINIEGO_IN_PAGAMENTO)
							&& rich.getGiorni() > numerogiornipreavvisodiniego)) {
						dao.executeCambioStatoDomanda(
								rich.getDomandaDetId(), Constants.DINIEGO, "Diniego per scadenza dei "
										+ numerogiornipreavvisodiniego + " giorni consentiti per il perfezionamento",
								rich.getDomandaNumero());
						avvisipreavvisodiniego = avvisipreavvisodiniego + 1;
						response = new ResponseRest();
						response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(), "CAMBIO_STATO",
								"CAMBIO_STATO_DOMANDA_" + Constants.PREAVVISO_DINIEGO_IN_PAGAMENTO);
						BatchLoggerFactory.getLogger(this).info("Chiamata a Crea Domanda per domanda "
								+ rich.getDomandaNumero() + " - " + response.toString());
						response = new ResponseRest();
						response = getCreaLettera(rich.getDomandaNumero(), rich.getRichiedenteCf(),
								Constants.LETTERA_DINIEGO, "CAMBIO_STATO",
								"CAMBIO_STATO_DOMANDA_" + Constants.PREAVVISO_DINIEGO_IN_PAGAMENTO);
						BatchLoggerFactory.getLogger(this).info("Chiamata a Crea Lettera per domanda "
								+ rich.getDomandaNumero() + " - " + response.toString());
						response = new ResponseRest();
						response = getStartDasPartenza(rich.getDomandaNumero(), rich.getRichiedenteCf(),
								Constants.LETTERA_DINIEGO, "CAMBIO_STATO",
								"CAMBIO_STATO_DOMANDA_" + Constants.PREAVVISO_DINIEGO_IN_PAGAMENTO);
						BatchLoggerFactory.getLogger(this).info("Chiamata a Stardas Partenza per domanda "
								+ rich.getDomandaNumero() + " - " + response.toString());
						response = new ResponseRest();
						response = getInviaNotifica(rich.getDomandaNumero(), Constants.BO_DINIEGO,
								rich.getRichiedenteCf(), "CAMBIO_STATO",
								"CAMBIO_STATO_DOMANDA_" + Constants.PREAVVISO_DINIEGO_IN_PAGAMENTO);
						BatchLoggerFactory.getLogger(this).info("Chiamata a Invia Notifica per domanda "
								+ rich.getDomandaNumero() + " - " + response.toString());
						dao.insertAudit(
								"Domanda numero " + rich.getDomandaNumero() + " richiedente " + rich.getRichiedenteCf()
										+ " destinatario " + rich.getCf(),
								"Diniego per scadenza dei " + numerogiornipreavvisodiniego
										+ " giorni consentiti per il perfezionamento",
								200);
					}
					// caso della graduatoria cambio stato da ammissibile ad ammessa e ammessa con
					// riserva
					else if (rich.getStato().equalsIgnoreCase(Constants.AMMISSIBILE)
							&& rich.getSportelloId().equals(idsportellochiuso)) {
						Boolean incompatibilita = dao.getIncompatibilita(rich.getDomandaDetId());
						// AMMESSA CON RISERVA
						if (incompatibilita) {
							dao.executeCambioStatoDomanda(rich.getDomandaDetId(), Constants.AMMESSA_RISERVA,
									"Batch passaggio ad Ammessa con riserva in graduatoria", rich.getDomandaNumero());
							avvisiammessariserva = avvisiammessariserva + 1;
							response = new ResponseRest();
							response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(), "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.AMMISSIBILE);
							BatchLoggerFactory.getLogger(this).info("Chiamata a Crea Domanda per domanda "
									+ rich.getDomandaNumero() + " - " + response.toString());
							dao.insertAudit(
									"Domanda numero " + rich.getDomandaNumero() + " richiedente "
											+ rich.getRichiedenteCf() + " destinatario " + rich.getCf(),
									"Passaggio ad ammessa con riserva in graduatoria", 200);
						}
						// AMMESSA
						else {
							dao.executeCambioStatoDomanda(rich.getDomandaDetId(), Constants.AMMESSA,
									"Batch passaggio ad Ammessa in graduatoria", rich.getDomandaNumero());
							avvisiammessa = avvisiammessa + 1;
							response = new ResponseRest();
							response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(), "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.AMMISSIBILE);
							BatchLoggerFactory.getLogger(this).info("Chiamata a Crea Domanda per domanda "
									+ rich.getDomandaNumero() + " - " + response.toString());
							dao.insertAudit(
									"Domanda numero " + rich.getDomandaNumero() + " richiedente "
											+ rich.getRichiedenteCf() + " destinatario " + rich.getCf(),
									"Passaggio ad ammessa in graduatoria", 200);
						}

						long idGraduatoria = dao.selectGraduatoriaIdByDomandaDettaglioId(rich.getDomandaDetId());
						if (idGraduatoria != 0) {
							if (dao.checkStatoGraduatoria(idGraduatoria, Constants.PROVVISORIA)) {
								dao.updateRGraduatoriaStato(idGraduatoria, Constants.DA_AGGIORNARE,
										Constants.XCODICESERVIZIO);
							}
						}
					}
				} catch (Exception e) {
					BatchLoggerFactory.getLogger(this)
							.error("--- Cf " + rich.getCf() + " --- NumeroDomanda " + rich.getDomandaNumero() 
									+ " --- Errore nella gestione della domanda");
					systemReturn = 3;
					continue;
				}
			}

			Integer avvisidinieghi = 0;
			
			boolean notificadiniego = dao.getParametro(Constants.NOTIFICA_DINIEGO, Constants.TIPO_PARAMETRO_GIORNI)
					.toLowerCase().equalsIgnoreCase("true") ? true : false;
			if (notificadiniego) {
				richieste = dao.getRichiesteDiniego();
				for (RichiestaDto rich : richieste) {
					try {
						String statoultimo = dao.selectStatoPrecedente(rich.getDomandaNumero());
						if (statoultimo.contains(Constants.AMMESSA)) {
							response = new ResponseRest();
							response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(), "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							BatchLoggerFactory.getLogger(this).info("Chiamata a Crea Domanda per domanda "
									+ rich.getDomandaNumero() + " - " + response.toString());
							// genero lettera
							response = new ResponseRest();
							response = getCreaLettera(rich.getDomandaNumero(), rich.getRichiedenteCf(),
									Constants.LETTERA_DINIEGO, "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							BatchLoggerFactory.getLogger(this).info("Chiamata a Crea Lettera per domanda "
									+ rich.getDomandaNumero() + " - " + response.toString());
							response = new ResponseRest();
							response = getStartDasPartenza(rich.getDomandaNumero(), rich.getRichiedenteCf(),
									Constants.LETTERA_DINIEGO, "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							BatchLoggerFactory.getLogger(this).info("Chiamata a Stardas Partenza per domanda "
									+ rich.getDomandaNumero() + " - " + response.toString());
							response = new ResponseRest();
							response = getInviaNotifica(rich.getDomandaNumero(), Constants.BO_DINIEGO,
									rich.getRichiedenteCf(), "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							BatchLoggerFactory.getLogger(this).info("Chiamata a Invia Notifica per domanda "
									+ rich.getDomandaNumero() + " - " + response.toString());
							avvisidinieghi = avvisidinieghi + 1;
						} else if (statoultimo.contains(Constants.CONTRODEDOTTA)) {
							response = new ResponseRest();
							response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(), "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							BatchLoggerFactory.getLogger(this).info("Chiamata a Crea Domanda per domanda "
									+ rich.getDomandaNumero() + " - " + response.toString());
							// genero lettera
							response = new ResponseRest();
							response = getCreaLettera(rich.getDomandaNumero(), rich.getRichiedenteCf(),
									Constants.LETTERA_DINIEGO, "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							BatchLoggerFactory.getLogger(this).info("Chiamata a Crea Lettera per domanda "
									+ rich.getDomandaNumero() + " - " + response.toString());
							// invio a stardas in partenza
							response = new ResponseRest();
							response = getStartDasPartenza(rich.getDomandaNumero(), rich.getRichiedenteCf(),
									Constants.LETTERA_DINIEGO, "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							BatchLoggerFactory.getLogger(this).info("Chiamata a Stardas Partenza per domanda "
									+ rich.getDomandaNumero() + " - " + response.toString());
							response = new ResponseRest();
							response = getInviaNotifica(rich.getDomandaNumero(), Constants.BO_DINIEGO,
									rich.getRichiedenteCf(), "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							BatchLoggerFactory.getLogger(this).info("Chiamata a Invia Notifica per domanda "
									+ rich.getDomandaNumero() + " - " + response.toString());
							avvisidinieghi = avvisidinieghi + 1;
						} else if (statoultimo.contains(Constants.NON_AMMISSIBILE)) {
							response = new ResponseRest();
							response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(), "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							BatchLoggerFactory.getLogger(this).info("Chiamata a Crea Domanda per domanda "
									+ rich.getDomandaNumero() + " - " + response.toString());
							// genero lettera
							response = new ResponseRest();
							response = getCreaLettera(rich.getDomandaNumero(), rich.getRichiedenteCf(),
									Constants.LETTERA_DINIEGO, "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							BatchLoggerFactory.getLogger(this).info("Chiamata a Crea Lettera per domanda "
									+ rich.getDomandaNumero() + " - " + response.toString());
							// invio a stardas in partenza
							response = new ResponseRest();
							response = getStartDasPartenza(rich.getDomandaNumero(), rich.getRichiedenteCf(),
									Constants.LETTERA_DINIEGO, "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							BatchLoggerFactory.getLogger(this).info("Chiamata a Stardas Partenza per domanda "
									+ rich.getDomandaNumero() + " - " + response.toString());
							response = new ResponseRest();
							response = getInviaNotifica(rich.getDomandaNumero(), Constants.BO_DINIEGO,
									rich.getRichiedenteCf(), "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							BatchLoggerFactory.getLogger(this).info("Chiamata a Invia Notifica per domanda "
									+ rich.getDomandaNumero() + " - " + response.toString());
							avvisidinieghi = avvisidinieghi + 1;
						} else if (statoultimo.contains(Constants.AMMESSA_RISERVA)) {
							response = new ResponseRest();
							response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(), "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							BatchLoggerFactory.getLogger(this).info("Chiamata a Crea Domanda per domanda "
									+ rich.getDomandaNumero() + " - " + response.toString());
							// genero lettera
							response = new ResponseRest();
							response = getCreaLettera(rich.getDomandaNumero(), rich.getRichiedenteCf(),
									Constants.LETTERA_DINIEGO, "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							BatchLoggerFactory.getLogger(this).info("Chiamata a Crea Lettera per domanda "
									+ rich.getDomandaNumero() + " - " + response.toString());
							// invio a stardas in partenza
							response = new ResponseRest();
							response = getStartDasPartenza(rich.getDomandaNumero(), rich.getRichiedenteCf(),
									Constants.LETTERA_DINIEGO, "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							BatchLoggerFactory.getLogger(this).info("Chiamata a Stardas Partenza per domanda "
									+ rich.getDomandaNumero() + " - " + response.toString());
							response = new ResponseRest();
							response = getInviaNotifica(rich.getDomandaNumero(), Constants.BO_DINIEGO,
									rich.getRichiedenteCf(), "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							BatchLoggerFactory.getLogger(this).info("Chiamata a Invia Notifica per domanda "
									+ rich.getDomandaNumero() + " - " + response.toString());
							avvisidinieghi = avvisidinieghi + 1;
						}
					} catch (Exception e) {
						BatchLoggerFactory.getLogger(this)
								.error("--- Cf " + rich.getCf() + " --- NumeroDomanda " + rich.getDomandaNumero()
										+ " --- Errore nella gestione della domanda");
						systemReturn = 3;
						continue;
					}
				}
			}

			dao.insertAudit("Fine Batch",
					"Numero notifiche per stato " + Constants.DA_RETTIFICARE + " " + avvisirettifica.toString(), 200);
			dao.insertAudit("Fine Batch", "Numero notifiche per stato "
					+ Constants.PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA + " " + avvisicontrodedotta.toString(), 200);
			dao.insertAudit("Fine Batch", "Numero notifiche per stato " + Constants.AMMESSA_RISERVA_IN_PAGAMENTO + " "
					+ avvisiammessaapreavviso.toString(), 200);
			dao.insertAudit("Fine Batch", "Numero notifiche per stato " + Constants.PREAVVISO_DINIEGO_IN_PAGAMENTO + " "
					+ avvisipreavvisodiniego.toString(), 200);
			dao.insertAudit("Fine Batch",
					"Numero notifiche per stato " + Constants.AMMESSA_RISERVA + " " + avvisiammessariserva.toString(),
					200);
			dao.insertAudit("Fine Batch",
					"Numero notifiche per stato " + Constants.AMMESSA + " " + avvisiammessa.toString(), 200);
			dao.insertAudit("Fine Batch", "Numero notifiche per stato " + Constants.DINIEGO
					+ " invio notifiche post determina " + avvisidinieghi.toString(), 200);

			// check and count errors from t batch tables
			int errorCount = batchDao.countBatchExecutionErrors();
			if (errorCount == -1) {
				BatchLoggerFactory.getLogger(this).error(
						"--- ERRORE countBatchExecutionErrors: non è possibile contare gli errori sul database");
			} else {
				if (systemReturn == 0) {
					systemReturn = errorCount > 0 ? 4 : 0;
				}
			}

		} catch (DatabaseConnectionException e) {

			systemReturn = 1;
			BatchLoggerFactory.getLogger(this).error("--- ERRORE INIT DATABASE: ", e);
			saveError(e, "ERRORE INIT DATABASE");
		} catch (WebServiceException e) {

			systemReturn = 2;
			BatchLoggerFactory.getLogger(this).error("--- ERRORE INTERROGA MEF: ", e);
			saveError(e, "ERRORE INTERROGA MEF");
		} catch (SQLException e) {

			systemReturn = 3;
			BatchLoggerFactory.getLogger(this).error("--- ERRORE QUERY DAO: ", e);
			saveError(e, "ERRORE QUERY DAO");

		} catch (Exception e) {
			systemReturn = 5;
			BatchLoggerFactory.getLogger(this).error("--- ERRORE GENERICO: ", e);

			saveError(e, "ERRORE GENERICO");
		} finally {
			BatchLoggerFactory.getLogger(this).info("--- CANCELLO RECORD DUPLICATI NELLE TABELLE DI LOG ---");
			eliminaRecordVuoti();
			BatchLoggerFactory.getLogger(this).info("--- END BATCH ---");
			if (dao != null) {
				dao.closeAll();
			}

			System.exit(systemReturn);
		}
	}

	private void saveError(Exception e, String errorMessage) {
		try {
			BatchLoggerFactory.getLogger(this).info("SAVE_ERROR " + errorMessage);
			dao.insertAudit(errorMessage, ExceptionUtils.getStackTrace(e), 500);

		} catch (SQLException e2) {
			BatchLoggerFactory.getLogger(this).error("--- ERRORE GENERICO: ", e2);
		}
	}

	public ResponseRest getInviaNotifica(String numerodomanda, String tipoNotifica, String cf, String batchCod,
			String motivoCod) {
		ResponseRest response = new ResponseRest();
		String utente = "BUONORES_BATCH";
		int esitopos = 0;
		Long numseqbatchKo = null;
		Long numseqbatchOk = null;
		try {
			esitopos = batchDao.selectEsitoPositivo(numerodomanda, "INVIO_NOTIFICA_" + tipoNotifica);
			if (esitopos == 0) {
				numseqbatchKo = batchDao.selectEventoKo(numerodomanda, "STATO_KO", batchCod, motivoCod, utente);
				numseqbatchOk = batchDao.selectEventoOk(numerodomanda, "STATO_OK", batchCod, motivoCod, utente);
				response = restbase.eseguiGet(cf,
						restbase.getBuonoressrvurl() + "/notifiche/" + numerodomanda + "/" + tipoNotifica, true, null,
						true);
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// inserisco nella det
					batchDao.inserisciBatchStepOK("INVIO_NOTIFICA_" + tipoNotifica,
							"CAMBIO_STATO_DOMANDA_" + tipoNotifica, numseqbatchOk, utente, response.getJson());
				} else {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						// inserisco nella det
						batchDao.inserisciBatchStepKO("INVIO_NOTIFICA_" + tipoNotifica,
								"CAMBIO_STATO_DOMANDA_" + tipoNotifica + " " + response.getJson(), numseqbatchKo,
								utente);
					}
				}
			}
		} catch (Exception e) {
			if (esitopos == 0) {
				try {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						batchDao.inserisciBatchStepKO("INVIO_NOTIFICA_" + tipoNotifica,
								"CAMBIO_STATO_DOMANDA_" + tipoNotifica + " " + response.getJson(), numseqbatchKo,
								utente);
					}
				} catch (SQLException e1) {
					BatchLoggerFactory.getLogger(this).error("--- ERRORE getInviaNotifica: ", e1);
				}
			}
			BatchLoggerFactory.getLogger(this).error("--- ERRORE getInviaNotifica: ", e);
		}
		return response;
	}

	public ResponseRest getCreaDomanda(String numerodomanda, String cf, String batchCod, String motivoCod) {
		ResponseRest response = new ResponseRest();
		String utente = "BUONORES_BATCH";
		Long numseqbatchKo = null;
		Long numseqbatchOk = null;
		int esitopos = 0;
		try {
			esitopos = batchDao.selectEsitoPositivo(numerodomanda, motivoCod);
			if (esitopos == 0) {
				numseqbatchKo = batchDao.selectEventoKo(numerodomanda, "STATO_KO", batchCod, motivoCod, utente);
				numseqbatchOk = batchDao.selectEventoOk(numerodomanda, "STATO_OK", batchCod, motivoCod, utente);
				response = restbase.eseguiGet(cf, restbase.getBuonoressrvurl() + "/crea_domanda/" + numerodomanda, true,
						null, true);
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// inserisco nella det
					batchDao.inserisciBatchStepOK(motivoCod, motivoCod, numseqbatchOk, utente, null);
				} else {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						// inserisco nella det
						batchDao.inserisciBatchStepKO(motivoCod, motivoCod + " " + response.getJson(), numseqbatchKo,
								utente);
					}
				}
			}
		} catch (Exception e) {
			if (esitopos == 0) {
				try {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						// inserisco nella det
						batchDao.inserisciBatchStepKO(motivoCod, motivoCod + " " + response.getJson(), numseqbatchKo,
								utente);
					}
				} catch (SQLException e1) {
					BatchLoggerFactory.getLogger(this).error("--- ERRORE getCreaDomanda: ", e1);
				}
			}
			BatchLoggerFactory.getLogger(this).error("--- ERRORE getCreaDomanda: ", e);
		}
		return response;
	}

	public ResponseRest getStartDasPartenza(String numerodomanda, String cf, String tipoLettera, String batchCod,
			String motivoCod) {
		ResponseRest response = new ResponseRest();
		String utente = "BUONORES_BATCH";
		int esitopos = 0;
		Long numseqbatchKo = null;
		Long numseqbatchOk = null;
		try {
			esitopos = batchDao.selectEsitoPositivo(numerodomanda, "STARDAS_PARTENZA_" + tipoLettera);
			if (esitopos == 0) {
				numseqbatchKo = batchDao.selectEventoKo(numerodomanda, "STATO_KO", batchCod, motivoCod, utente);
				numseqbatchOk = batchDao.selectEventoOk(numerodomanda, "STATO_OK", batchCod, motivoCod, utente);
				response = restbase.eseguiGet(cf,
						restbase.getBuonoressrvurl() + "/smistadocumentopartenza/" + numerodomanda + "/" + tipoLettera,
						true, null, true);
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// inserisco nella det
					batchDao.inserisciBatchStepOK("STARDAS_PARTENZA_" + tipoLettera,
							"CAMBIO_STATO_DOMANDA_" + tipoLettera, numseqbatchOk, utente, null);
				} else {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						// inserisco nella det
						batchDao.inserisciBatchStepKO("STARDAS_PARTENZA_" + tipoLettera,
								"CAMBIO_STATO_DOMANDA_" + tipoLettera + " " + response.getJson(), numseqbatchKo,
								utente);
					}
				}
			}
		} catch (Exception e) {
			if (esitopos == 0) {
				try {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						batchDao.inserisciBatchStepKO("STARDAS_PARTENZA_" + tipoLettera,
								"CAMBIO_STATO_DOMANDA_" + tipoLettera + " " + response.getJson(), numseqbatchKo,
								utente);
					}
				} catch (SQLException e1) {
					BatchLoggerFactory.getLogger(this).error("--- ERRORE getStartDasPartenza: ", e1);
				}
			}

			BatchLoggerFactory.getLogger(this).error("--- ERRORE getStartDasPartenza: ", e);
		}
		return response;
	}

	public ResponseRest getCreaLettera(String numerodomanda, String cf, String tipoLettera, String batchCod,
			String motivoCod) {
		ResponseRest response = new ResponseRest();
		String utente = "BUONORES_BATCH";
		int esitopos = 0;
		Long numseqbatchKo = null;
		Long numseqbatchOk = null;
		try {
			esitopos = batchDao.selectEsitoPositivo(numerodomanda, "CREA_LETTERA_" + tipoLettera);
			if (esitopos == 0) {
				numseqbatchKo = batchDao.selectEventoKo(numerodomanda, "STATO_KO", batchCod, motivoCod, utente);
				numseqbatchOk = batchDao.selectEventoOk(numerodomanda, "STATO_OK", batchCod, motivoCod, utente);
				response = restbase.eseguiGet(cf,
						restbase.getBuonoressrvurl() + "/crea_lettera/" + numerodomanda + "/" + tipoLettera, true, null,
						true);
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// inserisco nella det
					batchDao.inserisciBatchStepOK("CREA_LETTERA_" + tipoLettera, "CAMBIO_STATO_DOMANDA_" + tipoLettera,
							numseqbatchOk, utente, null);
				} else {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						// inserisco nella det
						batchDao.inserisciBatchStepKO("CREA_LETTERA_" + tipoLettera,
								"CAMBIO_STATO_DOMANDA_" + tipoLettera + " " + response.getJson(), numseqbatchKo,
								utente);
					}
				}
			}
		} catch (Exception e) {
			if (esitopos == 0) {
				try {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						batchDao.inserisciBatchStepKO("CREA_LETTERA_" + tipoLettera,
								"CAMBIO_STATO_DOMANDA_" + tipoLettera + " " + response.getJson(), numseqbatchKo,
								utente);
					}
				} catch (SQLException e1) {
					BatchLoggerFactory.getLogger(this).error("--- ERRORE getCreaLettera: ", e);
				}
			}
			BatchLoggerFactory.getLogger(this).error("--- ERRORE getCreaLettera: ", e);
		}
		return response;
	}

	public void eliminaRecordVuoti() {
		try {
			batchDao.deleteRecordSenzaStepOk();
			batchDao.deleteRecordSenzaStepKo();
			batchDao.DeleteRecordDaCancellareDuplicati();
		} catch (SQLException e) {
			BatchLoggerFactory.getLogger(this).error("--- ERRORE eliminaRecordVuoti: ", e);
		}
	}

	public ResponseRest setSegnalazioneBandi(String cf, String jsonSegnalazione, String numerodomanda,
			String tipoSegnalazione, String batchCod, String motivoCod) {
		ResponseRest response = new ResponseRest();
		String utente = "BUONORES_BATCH";
		int esitopos = 0;
		Long numseqbatchKo = null;
		Long numseqbatchOk = null;
		try {
			esitopos = batchDao.selectEsitoPositivo(numerodomanda, "SEGNALAZIONE_" + tipoSegnalazione);
			if (esitopos == 0) {
				numseqbatchKo = batchDao.selectEventoKo(numerodomanda, "STATO_KO", batchCod, motivoCod, utente);
				numseqbatchOk = batchDao.selectEventoOk(numerodomanda, "STATO_OK", batchCod, motivoCod, utente);
				response = restbase.eseguiPost(cf,
						restbase.getBuonoresbandisrvurl() + "/segnalazione/setRicezioneSegnalazioni", jsonSegnalazione,
						true, true);
				BatchLoggerFactory.getLogger(this)
				.info("--- Cf " + cf + " --- NumeroDomanda " + numerodomanda + 
						" --- setSegnalazioneBandi chiamata a Bandi - response " + response.toString());
				
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// inserisco nella det
					batchDao.inserisciBatchStepOK("SEGNALAZIONE_" + tipoSegnalazione,
							"SEGNALAZIONE_" + tipoSegnalazione, numseqbatchOk, utente, null);
				} else {
					// inserisco nella det
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						batchDao.inserisciBatchStepKO("SEGNALAZIONE_" + tipoSegnalazione,
								"SEGNALAZIONE_" + tipoSegnalazione + " " + response.getJson(), numseqbatchKo, utente);
					}
				}
			}
		} catch (Exception e) {
			if (esitopos == 0) {
				try {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						batchDao.inserisciBatchStepKO("SEGNALAZIONE_" + tipoSegnalazione,
								"SEGNALAZIONE_" + tipoSegnalazione + " " + response.getJson(), numseqbatchKo, utente);
					}
				} catch (SQLException e1) {
					BatchLoggerFactory.getLogger(this).error("--- ERRORE setSegnalazioneBandi: ", e1);
				}
			}
			BatchLoggerFactory.getLogger(this).error("--- ERRORE setSegnalazioneBandi: ", e);
		}

		return response;
	}
}
