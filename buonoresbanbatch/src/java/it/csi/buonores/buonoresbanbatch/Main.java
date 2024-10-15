/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbanbatch;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.codehaus.jackson.map.ObjectMapper;

import it.csi.buonores.buonoresbanbatch.dao.BuonoresbanBatchDAO;
import it.csi.buonores.buonoresbanbatch.dao.BuonoresbanBatchLogDAO;
import it.csi.buonores.buonoresbanbatch.dao.DBConnectionManager;
import it.csi.buonores.buonoresbanbatch.dto.ContrattoDto;
import it.csi.buonores.buonoresbanbatch.dto.ModelBandiMessage;
import it.csi.buonores.buonoresbanbatch.dto.RichiestaDto;
import it.csi.buonores.buonoresbanbatch.exception.BandiConnectionException;
import it.csi.buonores.buonoresbanbatch.exception.ConnectionException;
import it.csi.buonores.buonoresbanbatch.exception.DatabaseConnectionException;
import it.csi.buonores.buonoresbanbatch.integration.rest.RestBaseService;
import it.csi.buonores.buonoresbanbatch.logger.BatchLoggerFactory;
import it.csi.buonores.buonoresbanbatch.util.Constants;
import it.csi.buonores.buonoresbanbatch.util.Util;
import it.csi.buonores.buonoresbanbatch.util.rest.ResponseRest;

public class Main {
	private static final String ACQUISIZIONE_DOMANDE_BANDI = "/acquisizione_domande_bandi/";
	private static final String FORNITORE = "/fornitore/";
	private static final String CREA_LETTERA = "/crea_lettera/";
	private static final String SMISTADOCUMENTOPARTENZA = "/smistadocumentopartenza/";
	private static final String CREA_DOMANDA = "/crea_domanda/";
	private static final String NOTIFICHE = "/notifiche/";

	private Connection conn;
	private BuonoresbanBatchDAO dao = null;
	private RestBaseService restbase = null;
	private BuonoresbanBatchLogDAO batchDao = null;
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
			dao = new BuonoresbanBatchDAO(this.conn);
			batchDao = new BuonoresbanBatchLogDAO(this.conn);
			restbase = RestBaseService.getInstance(dao);
			BatchLoggerFactory.getLogger(this).info("--- CONNESSIONE EFFETTUATA ---");

		} catch (Exception e) {
			throw new DatabaseConnectionException("Database connection fail: " + e.getMessage());
		}
	}

	private void run() {

		try {
			BatchLoggerFactory.getLogger(this).info("--- AVVIO BATCH ---");
			// Stabilisco la connessione con il Database
			init();
			BatchLoggerFactory.getLogger(this).info("--- PRENDO ELENCO CF CON RICHIESTE IN PAGAMENTO ---");
			List<RichiestaDto> richieste = new ArrayList<RichiestaDto>();
			ResponseRest response = new ResponseRest();
			dao.insertAudit("Inizio BanBatch", "Conteggio pratiche di pagamento per avvisi", 0);
			Integer avvisipagamento = 0;
			// verifico se inviare a bandi le domande
			boolean eseguoinviobandidomande = dao.getParametro(Constants.INVIO_DOMANDE_BANDI, Constants.BATCH)
					.toLowerCase().equalsIgnoreCase("true") ? true : false;
			boolean notificaammessafinanziata = dao.getParametro(Constants.NOTIFICA_AMMESSA_FINANZIATA, Constants.BATCH)
					.toLowerCase().equalsIgnoreCase("true") ? true : false;
			String ultimaGraduatoria = dao.getCodGraduatoriaUltima();
			richieste = dao.getRichieste();
			for (RichiestaDto rich : richieste) {
				try {
					// domanda in pagamento che proviene da ammessa per cui esiste almeno uno step
					// andato in ko
					BatchLoggerFactory.getLogger(this).info(
							"---Ban batch creazione pdf domanda da " + Constants.AMMESSA + " a " + rich.getStato());
					response = new ResponseRest();
					response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(), "PUBBLICAZIONE",
							"CAMBIO_STATO_DOMANDA_BANDI", ultimaGraduatoria);
					BatchLoggerFactory.getLogger(this).info("Chiamata a Crea Domanda - " + response.toString());

					BatchLoggerFactory.getLogger(this).info("---Ban batch creazione pdf lettera finanziata");
					response = new ResponseRest();
					response = getCreaLettera(rich.getDomandaNumero(), rich.getRichiedenteCf(), "PUBBLICAZIONE",
							"CAMBIO_STATO_DOMANDA_BANDI", Constants.LETTERA_AMMISSIONE_FINANZIAMENTO,
							ultimaGraduatoria);
					BatchLoggerFactory.getLogger(this).info("Chiamata a Crea Lettera - " + response.toString());

					if (notificaammessafinanziata) {
						BatchLoggerFactory.getLogger(this)
								.info("---Ban batch invio stardas partenza pdf lettera finanziata");
						response = new ResponseRest();
						response = getStartDasPartenza(rich.getDomandaNumero(), rich.getRichiedenteCf(),
								"PUBBLICAZIONE", "CAMBIO_STATO_DOMANDA_BANDI",
								Constants.LETTERA_AMMISSIONE_FINANZIAMENTO, ultimaGraduatoria);
						BatchLoggerFactory.getLogger(this)
								.info("Chiamata a Stardas Partenza - " + response.toString());
					}

					BatchLoggerFactory.getLogger(this).info("---Ban batch chiamata bandi");
					// creo le tabelle del buono
					creaBuonoRendicontazione(rich, "PUBBLICAZIONE", "CAMBIO_STATO_DOMANDA_BANDI");
					if (eseguoinviobandidomande) {
						// chiama bandi
						response = new ResponseRest();
						response = invioBandi(rich.getDomandaNumero(), rich.getRichiedenteCf(), "PUBBLICAZIONE",
								"CAMBIO_STATO_DOMANDA_BANDI", ultimaGraduatoria);
						BatchLoggerFactory.getLogger(this).info("Chiamata a Bandi - " + response.toString());
					}
					BatchLoggerFactory.getLogger(this).info("---Ban batch creo record buono");
					if (notificaammessafinanziata) {
						String statoultimo = dao.selectStatoPrecedente(rich.getDomandaNumero());
						if (statoultimo.contains(Constants.PERFEZIONATA_IN_PAGAMENTO)) {
							BatchLoggerFactory.getLogger(this).info(
									"---Ban batch invio notifica finanziata proveniente da perfezionata in pagamento");
							response = new ResponseRest();
							response = getInviaNotifica(rich.getDomandaNumero(),
									Constants.BO_PERFEZIONATA_IN_PAGAMENTO, rich.getRichiedenteCf(), ultimaGraduatoria,
									"PUBBLICAZIONE", "CAMBIO_STATO_DOMANDA_BANDI");
							BatchLoggerFactory.getLogger(this).info("Invio Notifica - " + response.toString());
						} else {
							BatchLoggerFactory.getLogger(this).info("---Ban batch invio notifica finanziata");
							response = new ResponseRest();
							response = getInviaNotifica(rich.getDomandaNumero(), Constants.BO_AMMESSA_FINANZIATA,
									rich.getRichiedenteCf(), ultimaGraduatoria, "PUBBLICAZIONE",
									"CAMBIO_STATO_DOMANDA_BANDI");
							BatchLoggerFactory.getLogger(this).info("Invio Notifica - " + response.toString());
						}
					}

					avvisipagamento = avvisipagamento + 1;
					dao.insertAudit("Domanda numero " + rich.getDomandaNumero() + " richiedente "
							+ rich.getRichiedenteCf() + " destinatario " + rich.getCf(),
							"Domanda finanziata in pagamento ", 200);
				} catch (SQLException e) {
					BatchLoggerFactory.getLogger(this)
							.error("Errore nella gestione della domanda - " + rich.getDomandaNumero());
					systemReturn = 2;
					continue;
				}
			}
			dao.insertAudit("Fine primo step BanBatch",
					"Numero notifiche per stato " + Constants.IN_PAGAMENTO + " " + avvisipagamento.toString(), 200);
			boolean notificaammessanonfinanziata = dao
					.getParametro(Constants.NOTIFICA_AMMESSA_NON_FINANZIATA, Constants.BATCH).toLowerCase()
					.equalsIgnoreCase("true") ? true : false;
			richieste = dao.getRichiesteNonFinanziate();
			avvisipagamento = 0;
			BatchLoggerFactory.getLogger(this).info("---Prende ultima graduatoria pubblicata---");
			for (RichiestaDto rich : richieste) {
				try {
					// domanda ammessa e ammmessa con riserva non finanziata in ultimo sportello
					BatchLoggerFactory.getLogger(this).info("---Ban batch creazione pdf lettera non finanziata");
					response = new ResponseRest();
					response = getCreaLettera(rich.getDomandaNumero(), rich.getRichiedenteCf(), "NON_FINANZIAMENTO",
							"DOMANDA_NON_FINANZIATA", Constants.LETTERA_AMMISSIONE_NON_FINANZIAMENTO,
							ultimaGraduatoria);
					BatchLoggerFactory.getLogger(this).info("Crea Lettera Non Finanziata - " + response.toString());

					if (notificaammessanonfinanziata) {
						BatchLoggerFactory.getLogger(this)
								.info("---Ban batch invio stardas pertenza pdf lettera non finanziata");
						response = new ResponseRest();
						response = getStartDasPartenza(rich.getDomandaNumero(), rich.getRichiedenteCf(),
								"NON_FINANZIAMENTO", "DOMANDA_NON_FINANZIATA",
								Constants.LETTERA_AMMISSIONE_NON_FINANZIAMENTO, ultimaGraduatoria);
						BatchLoggerFactory.getLogger(this).info("Stardas Partenza  - " + response.toString());

						BatchLoggerFactory.getLogger(this).info("---Ban batch invio notifica non finanziata");
						response = new ResponseRest();
						response = getInviaNotifica(rich.getDomandaNumero(), Constants.BO_AMMESSA_NON_FINANZIATA,
								rich.getRichiedenteCf(), ultimaGraduatoria, "NON_FINANZIAMENTO",
								"DOMANDA_NON_FINANZIATA");
						BatchLoggerFactory.getLogger(this).info("Invio Notifica - " + response.toString());
					}

					avvisipagamento = avvisipagamento + 1;
					dao.insertAudit("Domanda numero " + rich.getDomandaNumero() + " richiedente "
							+ rich.getRichiedenteCf() + " destinatario " + rich.getCf(), "Domanda non finanziata",
							200);
				} catch (SQLException e) {
					BatchLoggerFactory.getLogger(this).error(
							"Errore nella gestione della domanda non finanziata- " + rich.getDomandaNumero());
					systemReturn = 2;
					continue;
				}
			}
			dao.insertAudit("Fine secondo step BanBatch",
					"Numero notifiche per domande non finanziate " + avvisipagamento.toString(), 200);
			// INVIO CONTRATTI BANDI
			Integer contrattiInviati = 0;
			// CHECK PARAMETRO INVIO CONTRATTI
			boolean invioContrattiBandi = dao.getParametro(Constants.INVIO_CONTRATTI_BANDI, Constants.BATCH)
					.toLowerCase().equalsIgnoreCase("true") ? true : false;
			if (invioContrattiBandi) {
				// ESTRAZIONE CONTRATTI
				BatchLoggerFactory.getLogger(this).info("--- INIZIO INVIO CONTRATTI A BANDI ---");
				List<ContrattoDto> listaContratti = dao.getContrattiNonInviati();
				String esito="";
				String domandaNumeroTmp="";
				for (ContrattoDto contratto : listaContratti) {
					try {
						if(!domandaNumeroTmp.equals(contratto.getDomandaNumero())) {
							domandaNumeroTmp=contratto.getDomandaNumero();
							esito="";
							// INVIO CONTRATTI A BANDI
							BatchLoggerFactory.getLogger(this).info("--- CHIAMO SET FORNITORE PER LA DOMANDA: " + domandaNumeroTmp + "---");
							esito = setFornitore(domandaNumeroTmp, contratto.getRichiedenteCf(), "INVIO_CONTRATTI_BANDI", "SET_FORNITORE");
							BatchLoggerFactory.getLogger(this).info("--- ESITO SET FORNITORE PER LA DOMANDA: " + domandaNumeroTmp + " --- " + esito + " ---");
						}
						if (esito == "OK") {
							contrattiInviati++;
							// update il contratto_id_inviato
							// se la domanda numero e' uguale e esito ok aggiorno il contratto
							BatchLoggerFactory.getLogger(this).info("--- UPDATE CONTRATTO: " + contratto.getContrattoId() + " ---");
							dao.updateContrattoInviato(contratto.getContrattoId());
							dao.insertAudit("Domanda numero " + contratto.getDomandaNumero() + " richiedente "
									+ contratto.getRichiedenteCf() + " destinatario " + contratto.getBeneficiarioCf(),
									"Inserimento fornitore-i ", 200);
						} else {
							BatchLoggerFactory.getLogger(this).error("---ERRORE UPDATE CONTRATTO: "+contratto.getContrattoId()+" ---");
							dao.insertAudit("Domanda numero " + contratto.getDomandaNumero() + " richiedente "
									+ contratto.getRichiedenteCf() + " destinatario " + contratto.getBeneficiarioCf(),
									"Inserimento fornitore-i", 500);
						}

					} catch (SQLException e) {
						BatchLoggerFactory.getLogger(this).error("Errore nell'invio a bandi del contratto "
								+ contratto.getContrattoId() + ", domanda numero " + contratto.getDomandaNumero());
						systemReturn = 2;
						continue;
					}
				}

			}

			dao.insertAudit("Fine BanBatch", "Contratti elaborati con successo :" + contrattiInviati.toString(), 200);
			// check and count errors from t batch tables
			int errorCount = batchDao.countBatchExecutionErrors();
			if (errorCount == -1) {
				BatchLoggerFactory.getLogger(this).error(
						"--- ERRORE countBatchExecutionErrors: non è possibile contare gli errori sul database");
			} else {
				if (systemReturn == 0) {
					systemReturn = errorCount > 0 ? 3 : 0;
				}
			}

		} catch (DatabaseConnectionException e) {
			systemReturn = 1;
			BatchLoggerFactory.getLogger(this).error("ERRORE INIT DATABASE: ", e);
			saveError(e, "ERRORE INIT DATABASE");
		} catch (SQLException e) {
			systemReturn = 2;
			BatchLoggerFactory.getLogger(this).error("ERRORE QUERY DAO: ", e);
			saveError(e, "ERRORE QUERY DAO");
		} catch (BandiConnectionException e) {
			systemReturn = 4;
			BatchLoggerFactory.getLogger(this).error("--- ERRORE CONNESSIONE PIATTAFORMA BANDI: ", e);

			saveError(e, "ERRORE CONNESSIONE PIATTAFORMA BANDI");
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

	public ResponseRest getInviaNotifica(String numerodomanda, String tipoNotifica, String cf, String codGraduatoria,
			String batchCod, String motivoCod) {
		ResponseRest response = new ResponseRest();
		String utente = "BUONORESBANBATCH";
		int esitopos = 0;
		Long numseqbatchKo = null;
		Long numseqbatchOk = null;
		try {
			esitopos = batchDao.selectEsitoPositivo(numerodomanda,
					"INVIO_NOTIFICA_" + tipoNotifica + "_" + codGraduatoria);
			if (esitopos == 0) {
				numseqbatchKo = batchDao.selectEventoKo(numerodomanda, "STATO_KO", batchCod, motivoCod, utente);
				numseqbatchOk = batchDao.selectEventoOk(numerodomanda, "STATO_OK", batchCod, motivoCod, utente);

				response = restbase.eseguiGet(cf,
						restbase.getBuonoressrvurl() + NOTIFICHE + numerodomanda + "/" + tipoNotifica, true, null,
						true);

				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
						//nserisco nella det
					batchDao.inserisciBatchStepOK("INVIO_NOTIFICA_" + tipoNotifica + "_" + codGraduatoria,
							"Domanda " + numerodomanda + " CAMBIO_STATO_DOMANDA_" + tipoNotifica, numseqbatchOk,
							utente, response.getJson());
				} else {
					// inserisco nella det
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						batchDao.inserisciBatchStepKO(
								"INVIO_NOTIFICA_" + tipoNotifica + "_" + codGraduatoria, "Domanda " + numerodomanda
										+ " CAMBIO_STATO_DOMANDA_" + tipoNotifica + " " + response.getJson(),
								numseqbatchKo, utente);
					}
				}
			}
		} catch (Exception e) {
			if (esitopos == 0) {
				try {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						batchDao.inserisciBatchStepKO(
								"INVIO_NOTIFICA_" + tipoNotifica + "_" + codGraduatoria, "Domanda " + numerodomanda
										+ " CAMBIO_STATO_DOMANDA_" + tipoNotifica + " " + response.getJson(),
								numseqbatchKo, utente);
					}
				} catch (SQLException e1) {
					BatchLoggerFactory.getLogger(this).error("ERRORE SQLException: ", e1);
				}
			}
			BatchLoggerFactory.getLogger(this).error("ERRORE Exception: ", e);
		}
		return response;
	}

	public ResponseRest getCreaDomanda(String numerodomanda, String cf, String batchCod, String motivoCod,
			String codGraduatoria) {
		ResponseRest response = new ResponseRest();
		String utente = "BUONORESBANBATCH";
		Long numseqbatchKo = null;
		Long numseqbatchOk = null;
		int esitopos = 0;
		try {
			esitopos = batchDao.selectEsitoPositivo(numerodomanda, motivoCod + "_" + codGraduatoria);
			if (esitopos == 0) {
				// se solo domanda o combinata con altri servizi
				// verifico se ho un errore o esito positivo per scrivere i log
				// ci sono altri step crea domanda crea lettera invio stardas invio notifica
				numseqbatchKo = batchDao.selectEventoKo(numerodomanda, "STATO_KO", batchCod, motivoCod, utente);
				numseqbatchOk = batchDao.selectEventoOk(numerodomanda, "STATO_OK", batchCod, motivoCod, utente);
				response = restbase.eseguiGet(cf, restbase.getBuonoressrvurl() + CREA_DOMANDA + numerodomanda, true,
						null, true);
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// esito positivo
					// inserisco nella det
					batchDao.inserisciBatchStepOK(motivoCod + "_" + codGraduatoria,
							"Domanda " + numerodomanda + " " + motivoCod, numseqbatchOk, utente, null);
				} else {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						// inserisco nella det
						batchDao.inserisciBatchStepKO(motivoCod + "_" + codGraduatoria,
								"Domanda " + numerodomanda + " " + motivoCod + " " + response.getJson(), numseqbatchKo,
								utente);
					}
				}

			}
		} catch (Exception e) {
			if (esitopos == 0) {
				try {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						// inserisco nella det
						batchDao.inserisciBatchStepKO(motivoCod + "_" + codGraduatoria,
								"Domanda " + numerodomanda + " " + motivoCod + " " + response.getJson(), numseqbatchKo,
								utente);
					}
				} catch (SQLException e1) {
					BatchLoggerFactory.getLogger(this).error("ERRORE SQLException: ", e1);
				}
			}
			BatchLoggerFactory.getLogger(this).error("ERRORE Exception: ", e);
		}
		return response;
	}

	public ResponseRest getStartDasPartenza(String numerodomanda, String cf, String batchCod, String motivoCod,
			String tipoLettera, String codGraduatoria) {
		ResponseRest response = new ResponseRest();
		String utente = "BUONORESBANBATCH";
		int esitopos = 0;
		Long numseqbatchKo = null;
		Long numseqbatchOk = null;
		try {
			esitopos = batchDao.selectEsitoPositivo(numerodomanda,
					"STARDAS_PARTENZA_" + tipoLettera + "_" + codGraduatoria);
			if (esitopos == 0) {
				numseqbatchKo = batchDao.selectEventoKo(numerodomanda, "STATO_KO", batchCod, motivoCod, utente);
				numseqbatchOk = batchDao.selectEventoOk(numerodomanda, "STATO_OK", batchCod, motivoCod, utente);
				
				response = restbase.eseguiGet(cf,
						restbase.getBuonoressrvurl() + SMISTADOCUMENTOPARTENZA + numerodomanda + "/" + tipoLettera,
						true, null, true);
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// inserisco nella det
					batchDao.inserisciBatchStepOK("STARDAS_PARTENZA_" + tipoLettera + "_" + codGraduatoria,
							"Domanda " + numerodomanda + " CAMBIO_STATO_DOMANDA_" + tipoLettera, numseqbatchOk, utente, null);
				} else {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						// inserisco nella det
						batchDao.inserisciBatchStepKO(
								"STARDAS_PARTENZA_" + tipoLettera + "_" + codGraduatoria, "Domanda " + numerodomanda
										+ " CAMBIO_STATO_DOMANDA_" + tipoLettera + " " + response.getJson(),
								numseqbatchKo, utente);
					}
				}
			}
		} catch (Exception e) {
			if (esitopos == 0) {
				try {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						batchDao.inserisciBatchStepKO(
								"STARDAS_PARTENZA_" + tipoLettera + "_" + codGraduatoria, "Domanda " + numerodomanda
										+ " CAMBIO_STATO_DOMANDA_" + tipoLettera + " " + response.getJson(),
								numseqbatchKo, utente);
					}
				} catch (SQLException e1) {
					BatchLoggerFactory.getLogger(this).error("ERRORE SQLException: ", e1);
				}
			}
			BatchLoggerFactory.getLogger(this).error("ERRORE Exception: ", e);
		}
		return response;
	}

	public ResponseRest getCreaLettera(String numerodomanda, String cf, String batchCod, String motivoCod,
			String tipoLettera, String codGraduatoria) {
		ResponseRest response = new ResponseRest();
		String utente = "BUONORESBANBATCH";
		int esitopos = 0;
		Long numseqbatchKo = null;
		Long numseqbatchOk = null;
		try {
			esitopos = batchDao.selectEsitoPositivo(numerodomanda,
					"CREA_LETTERA_" + tipoLettera + "_" + codGraduatoria);
			if (esitopos == 0) {
				numseqbatchKo = batchDao.selectEventoKo(numerodomanda, "STATO_KO", batchCod, motivoCod, utente);
				numseqbatchOk = batchDao.selectEventoOk(numerodomanda, "STATO_OK", batchCod, motivoCod, utente);
				response = restbase.eseguiGet(cf,
						restbase.getBuonoressrvurl() + CREA_LETTERA + numerodomanda + "/" + tipoLettera, true, null,
						true);
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// inserisco nella det
					batchDao.inserisciBatchStepOK("CREA_LETTERA_" + tipoLettera + "_" + codGraduatoria,
							"Domanda " + numerodomanda + " CAMBIO_STATO_DOMANDA_" + tipoLettera, numseqbatchOk, utente, null);
				} else {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						// inserisco nella det
						batchDao.inserisciBatchStepKO(
								"CREA_LETTERA_" + tipoLettera + "_" + codGraduatoria, "Domanda " + numerodomanda
										+ " CAMBIO_STATO_DOMANDA_" + tipoLettera + " " + response.getJson(),
								numseqbatchKo, utente);
					}
				}
			}
		} catch (Exception e) {
			if (esitopos == 0) {
				try {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						batchDao.inserisciBatchStepKO(
								"CREA_LETTERA_" + tipoLettera + "_" + codGraduatoria, "Domanda " + numerodomanda
										+ " CAMBIO_STATO_DOMANDA_" + tipoLettera + " " + response.getJson(),
								numseqbatchKo, utente);
					}
				} catch (SQLException e1) {
					BatchLoggerFactory.getLogger(this).error("ERRORE SQLException: ", e1);
				}
			}
			BatchLoggerFactory.getLogger(this).error("ERRORE Exception: ", e);
		}

		return response;
	}

	public ResponseRest invioBandi(String numerodomanda, String cf, String batchCod, String motivoCod,
			String codGraduatoria) throws BandiConnectionException {
		ResponseRest response = new ResponseRest();
		String utente = "BUONORESBANBATCH";
		int esitopos = 0;
		Long numseqbatchKo = null;
		Long numseqbatchOk = null;
		try {
			esitopos = batchDao.selectEsitoPositivo(numerodomanda,
					"INVIO_BANDI_" + numerodomanda + "_" + codGraduatoria);
			if (esitopos == 0) {
				numseqbatchKo = batchDao.selectEventoKo(numerodomanda, "STATO_KO", batchCod, motivoCod, utente);
				numseqbatchOk = batchDao.selectEventoOk(numerodomanda, "STATO_OK", batchCod, motivoCod, utente);
				response = restbase.eseguiGetBandi(cf,
						restbase.getBuonoresbandisrvurl() + ACQUISIZIONE_DOMANDE_BANDI + numerodomanda, true, null,
						true);
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// inserisco nella det
					ModelBandiMessage body = new ModelBandiMessage();
					if (response.getJson() != null) {
						ObjectMapper mapper = new ObjectMapper();
						if (response.getJson().contains("status")) {
							if (numseqbatchKo != null && numseqbatchKo > 0) {
								// inserisco nella det
								systemReturn = 4;
								BatchLoggerFactory.getLogger(this)
										.error("ERRORE invioBandi statusCode=200||201 response:" + response.getJson());
								batchDao.inserisciBatchStepKO("INVIO_BANDI_" + numerodomanda + "_" + codGraduatoria,
										"Domanda " + numerodomanda + " INVIO_BANDI" + " " + response.getJson(),
										numseqbatchKo, utente);
							}
						} else {
							body = mapper.readValue(response.getJson(), ModelBandiMessage.class);
							batchDao.inserisciBatchStepOK("INVIO_BANDI_" + numerodomanda + "_" + codGraduatoria,
									"Domanda " + numerodomanda + " INVIO_BANDI_MESSAGEUUID_" + body.getUuid(),
									numseqbatchOk, utente, null);
						}
					}
				} else {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						// inserisco nella det
						batchDao.inserisciBatchStepKO("INVIO_BANDI_" + numerodomanda + "_" + codGraduatoria,
								"Domanda " + numerodomanda + " INVIO_BANDI" + " " + response.getJson(), numseqbatchKo,
								utente);
						BatchLoggerFactory.getLogger(this)
								.error("ERRORE invioBandi StatusCode: " + response.getStatusCode());
						systemReturn = 4;
					}
				}
			}
		} catch (Exception e) {
			if (esitopos == 0) {
				try {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						batchDao.inserisciBatchStepKO("INVIO_BANDI_" + numerodomanda + "_" + codGraduatoria,
								"Domanda " + numerodomanda + " INVIO_BANDI" + " " + response.getJson(), numseqbatchKo,
								utente);
					}
				} catch (SQLException e1) {
					BatchLoggerFactory.getLogger(this).error("ERRORE SQLException: ", e1);
				}
			}
			BatchLoggerFactory.getLogger(this).error("ERRORE Exception: ", e);
			throw new BandiConnectionException(e);
		}

		return response;
	}

	private void creaBuonoRendicontazione(RichiestaDto richiesta, String batchCod, String motivoCod)
			throws SQLException {
		// verifico se il buono creato se si non faccio nulla
		String utente = "BUONORESBANBATCH";
		int esitopos = 0;
		Long numseqbatchKo = null;
		Long numseqbatchOk = null;
		long buonoId = 0;
		BatchLoggerFactory.getLogger(this)
				.info("--- INIZIO CREAZIONE TABELLE BUONI DOMANDA --- " + richiesta.getDomandaNumero());
		BatchLoggerFactory.getLogger(this).info("--- SE ESISTE BUONO NON FARE NULLA ---");
		try {
			esitopos = batchDao.selectEsitoPositivo(richiesta.getDomandaNumero(),
					"CREA_BUONO_" + richiesta.getDomandaNumero());
			if (esitopos == 0) {
				numseqbatchKo = batchDao.selectEventoKo(richiesta.getDomandaNumero(), "STATO_KO", batchCod, motivoCod,
						utente);
				numseqbatchOk = batchDao.selectEventoOk(richiesta.getDomandaNumero(), "STATO_OK", batchCod, motivoCod,
						utente);
				buonoId = dao.selectEsisteBuonoRendicontazione(richiesta.getDomandaId(), richiesta.getSportelloId());
				if (!Util.isValorizzato(buonoId)) {
					BatchLoggerFactory.getLogger(this).info("--- BUONO NON ESISTE CREA --- " + buonoId);
					buonoId = dao.insertBuono(richiesta.getDomandaDetId(), richiesta.getSportelloId(),
							richiesta.getDomandaId(),richiesta.getDomandaNumero());
				}
				BatchLoggerFactory.getLogger(this)
						.info("--- FINE CREAZIONE TABELLE BUONI DOMANDA --- " + richiesta.getDomandaNumero());

				batchDao.inserisciBatchStepOK("CREA_BUONO_" + richiesta.getDomandaNumero(),
						"Domanda " + richiesta.getDomandaNumero() + " CREA_BUONO_DOMANDA", numseqbatchOk, utente, null);
			}
		} catch (SQLException e) {
			if (esitopos == 0) {
				try {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						batchDao.inserisciBatchStepKO(
								"CREA_BUONO_" + richiesta.getDomandaNumero(), "Domanda " + richiesta.getDomandaNumero()
										+ " CREA_BUONO_DOMANDA" + " " + ExceptionUtils.getStackTrace(e),
								numseqbatchKo, utente);
					}
				} catch (SQLException e1) {
					BatchLoggerFactory.getLogger(this).error("ERRORE SQLException: ", e1);
				}
			}
			BatchLoggerFactory.getLogger(this).error("ERRORE SQLException: ", e);
			throw new SQLException(e);
		}
	}

	public void eliminaRecordVuoti() {
		try {
			batchDao.DeleteRecordSenzaStepOk();
			batchDao.DeleteRecordSenzaStepKo();
			batchDao.DeleteRecordDaCancellareDuplicati();
		} catch (SQLException e) {
			BatchLoggerFactory.getLogger(this).error("ERRORE eliminaRecordVuoti SQLException: ", e);
		}
	}

	// INVIO RENDICONTAZIONE
	// SET FORNITORE
	// batchcod=RENDICONTAZIONE_BUONI e motivocod=SET_FORNITORE
	public String setFornitore(String numerodomanda, String cf, String batchCod, String motivoCod) {
		ResponseRest response = new ResponseRest();
		String utente = "BUONORESBANBATCH";
		int esitopos = 0;
		Long numseqbatchOk = null;
		Long numseqbatchKo = null;
		String esitoresponse = "OK";
		String batchEsecDetStep="SET_FORNITORE_" + numerodomanda+"_"+Util.getDateAndTime();
		try {
			esitopos = batchDao.selectEsitoPositivo(numerodomanda, batchEsecDetStep);
			if (esitopos == 0) {
				numseqbatchKo = batchDao.selectEventoKo(numerodomanda, "STATO_KO", batchCod, motivoCod, utente);
				numseqbatchOk = batchDao.selectEventoOk(numerodomanda, "STATO_OK", batchCod, motivoCod, utente);

				response =restbase.eseguiGetBandi(cf, restbase.getBuonoresbandisrvurl() + FORNITORE + numerodomanda, true, null,
						true);
				BatchLoggerFactory.getLogger(this).info("--- SET_FORNITORE RESPONSE: "+response+" ---");
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					ObjectMapper mapper = new ObjectMapper();
					if (response.getJson() == null) {
						if (numseqbatchKo != null && numseqbatchKo > 0) {
							// inserisco nella det
							batchDao.inserisciBatchStepKO(batchEsecDetStep,
									"Domanda " + numerodomanda + " SET_FORNITORE_" + " " + response.getJson(),
									numseqbatchKo, utente);
						}
						return "KO";
					}
					List<ModelBandiMessage> body = Arrays
							.asList(mapper.readValue(response.getJson(), ModelBandiMessage[].class));
					for (ModelBandiMessage singBody : body) {
						if (singBody.getEsitoServizio().equalsIgnoreCase("KO")) {
							esitoresponse = "KO";
							break;
						}
					}
					if (esitoresponse.equalsIgnoreCase("OK")) {
						batchDao.inserisciBatchStepOK(batchEsecDetStep,
								"Domanda " + numerodomanda + " SET_FORNITORE_" + " " + response.getJson(),
								numseqbatchOk, utente, null);
					} else if (numseqbatchKo != null && numseqbatchKo > 0) {
						// inserisco nella det
						batchDao.inserisciBatchStepKO(batchEsecDetStep,
								"Domanda " + numerodomanda + " SET_FORNITORE_" + " " + response.getJson(),
								numseqbatchKo, utente);
						esitoresponse = "KO";
					}
				} else {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						// inserisco nella det
						batchDao.inserisciBatchStepKO(batchEsecDetStep,
								"Domanda " + numerodomanda + " SET_FORNITORE_" + " " + response.getJson(),
								numseqbatchKo, utente);
						esitoresponse = "KO";
					}
				}
			}
		} catch (Exception e) {
			if (esitopos == 0) {
				try {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						batchDao.inserisciBatchStepKO(batchEsecDetStep,
								"Domanda " + numerodomanda + " SET_FORNITORE_" + " " + response.getJson(),
								numseqbatchKo, utente);
						esitoresponse = "KO";
					}
				} catch (SQLException e1) {
					BatchLoggerFactory.getLogger(this).error("ERRORE SQLException: ", e1);
				}
			}
			BatchLoggerFactory.getLogger(this).error("ERRORE Exception: ", e);
		}
		return esitoresponse;
	}
}
