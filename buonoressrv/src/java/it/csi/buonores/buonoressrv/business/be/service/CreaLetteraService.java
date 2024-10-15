/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.business.be.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.buonores.buonoressrv.business.be.service.base.BaseService;
import it.csi.buonores.buonoressrv.dto.Errore;
import it.csi.buonores.buonoressrv.dto.ErroreDettaglio;
import it.csi.buonores.buonoressrv.dto.ModelIncompatibilitaRichiesta;
import it.csi.buonores.buonoressrv.dto.ModelRichiesta;
import it.csi.buonores.buonoressrv.dto.ModelSportello;
import it.csi.buonores.buonoressrv.exception.DatabaseException;
import it.csi.buonores.buonoressrv.exception.ResponseErrorException;
import it.csi.buonores.buonoressrv.integration.dao.custom.CodParametroDao;
import it.csi.buonores.buonoressrv.integration.dao.custom.RichiesteDao;
import it.csi.buonores.buonoressrv.integration.dao.custom.SportelliDao;
import it.csi.buonores.buonoressrv.integration.notificatore.enumerator.NotificatoreEventCode;
import it.csi.buonores.buonoressrv.integration.service.util.FilesEncrypt;
import it.csi.buonores.buonoressrv.util.Constants;
import it.csi.buonores.buonoressrv.util.Util;
import it.csi.buonores.buonoressrv.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoressrv.util.validator.impl.ValidateGenericImpl;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;

@Service
public class CreaLetteraService extends BaseService {

	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	CodParametroDao parametroDao;

	@Autowired
	SportelliDao sportelliDao;

	@Autowired
	private FilesEncrypt filesEncrypt;

	public Response creaLettera(String numeroRichiesta, String tipoLettera, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ModelRichiesta richiesta = new ModelRichiesta();
		Errore error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {

			List<ErroreDettaglio> listError = validateGeneric.validateLettera(shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio, numeroRichiesta, tipoLettera, securityContext, httpHeaders,
					httpRequest);
			if (!listError.isEmpty()) {
				throw new ResponseErrorException(Errore.generateErrore(HttpStatus.BAD_REQUEST, listError),
						"errore in validate");
			}

			richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
	        if (!richiesta.getStato().equalsIgnoreCase(Constants.DINIEGO) && !richiesta.getStato().equalsIgnoreCase(Constants.IN_PAGAMENTO)
	                && !richiesta.getStato().equalsIgnoreCase(Constants.AMMESSA) && !richiesta.getStato().equalsIgnoreCase(Constants.AMMESSA_RISERVA)
	                && !richiesta.getStato().equalsIgnoreCase(Constants.AMMESSA_RISERVA_IN_PAGAMENTO)) {
	        	generateResponseErrorException("Domanda in stato non coerente", CodeErrorEnum.ERR03,
						HttpStatus.INTERNAL_SERVER_ERROR, "Errore Crea Lettera " + " Domanda in stato non coerente");
			}
			byte[] lettera = null;
			Map<String, Object> parameters = new HashMap<String, Object>();
			logInfo("creo pdf parametri", "pdf lettera prendendo notifica");
			boolean trovato = false;
			if (richiesta != null) {
	            String statoultimo = richiesteDao.selectStatoPrecedente(richiesta.getNumero());
				if (richiesta.getStato().equalsIgnoreCase(Constants.DINIEGO)
						&& tipoLettera.equalsIgnoreCase(Constants.LETTERA_DINIEGO)) {
					parameters.put("TIPO_LETTERA", Constants.LETTERA_DINIEGO.replace("_", " "));
					String statopernultimo = richiesteDao.selectStatoPenultimo(richiesta.getNumero());
					String datacontrodedotta = null;
					datacontrodedotta = richiesteDao.selectDataControdedotta(richiesta.getNumero());
					boolean preavvisodiniego = false;
					boolean rettifica = false;
					boolean controdedotta = false;
					boolean preavvisodiniegoinpagamento = false;
					boolean perfezionatainpagamento = false;
					if (statopernultimo != null && statoultimo != null) {
						if (statopernultimo.contains(Constants.PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA)
								&& statoultimo.contains(Constants.NON_AMMISSIBILE)) {
							preavvisodiniego = true;
						} else if (statopernultimo.contains(Constants.DA_RETTIFICARE)
								|| statopernultimo.contains(Constants.IN_RETTIFICA)
										&& statoultimo.contains(Constants.NON_AMMISSIBILE)) {
							rettifica = true;
						} else if (statoultimo.contains(Constants.CONTRODEDOTTA)) {
							controdedotta = true;
						} else if (statoultimo.contains(Constants.NON_AMMISSIBILE)
								&& statopernultimo.contains(Constants.CONTRODEDOTTA)) {
							controdedotta = true;
// feature rimozione diniego ammessa or ammessa riserva e rami di codice obsoleti
//						} else if (statoultimo.contains(Constants.AMMESSA)
//								|| statoultimo.contains(Constants.AMMESSA_RISERVA)) {
//							rettifica = true;
						} else if (statoultimo.contains(Constants.PREAVVISO_DINIEGO_IN_PAGAMENTO)) {
							preavvisodiniegoinpagamento = true;
						} else if (statoultimo.contains(Constants.PERFEZIONATA_IN_PAGAMENTO)) {
							perfezionatainpagamento = true;
						}
					}
					// messagio 3.4.3 si arriva da preavviso di diniego per non ammissibilita non
					// invia le controdeduzioni dopo averle richieste
					// prendo il penultimo stato e vedo se preavviso di diniego per non
					// ammissibilita
					if (preavvisodiniego) {
						trovato = true;
						parameters.put("TESTO_EMAIL", buildBody(
								parametroDao.selectValoreParametroFromCod(
										NotificatoreEventCode.MSG_BODY_EMAIL_BO_DINIEGO_DOPO_PREAVVISO.getCode(),
										Constants.NOTIFICATORE),
								richiesta.getRichiedente().getNome(), richiesta.getRichiedente().getCognome(),
								richiesta.getNumero(), richiesta.getNote(), datacontrodedotta, null));
					}
					// messagio 3.4.5 si arriva da in rettifica rettificata non richieste
					// controdeduzioni
					// prendo il penultimo stato e vedo se da rettificare e rettifica
					else if (rettifica) {
						trovato = true;
						parameters.put("TESTO_EMAIL", buildBody(
								parametroDao.selectValoreParametroFromCod(
										NotificatoreEventCode.MSG_BODY_EMAIL_BO_DINIEGO_SENZA_PREAVVISO.getCode(),
										Constants.NOTIFICATORE),
								richiesta.getRichiedente().getNome(), richiesta.getRichiedente().getCognome(),
								richiesta.getNumero(), richiesta.getNote(), datacontrodedotta, null));
					}
					// messagio 3.4.4 si arriva da controdedotta
					// prendo il penultimo stato e vedo se da controdedotta
					else if (controdedotta) {
						trovato = true;
						parameters.put("TESTO_EMAIL", buildBody(
								parametroDao.selectValoreParametroFromCod(
										NotificatoreEventCode.MSG_BODY_EMAIL_BO_DINIEGO_CON_CONTRODEDUZIONI.getCode(),
										Constants.NOTIFICATORE),
								richiesta.getRichiedente().getNome(), richiesta.getRichiedente().getCognome(),
								richiesta.getNumero(), richiesta.getNote(), datacontrodedotta, null));
					} else if (preavvisodiniegoinpagamento) {
						trovato = true;
						parameters.put("TESTO_EMAIL", buildBody(parametroDao.selectValoreParametroFromCod(
								NotificatoreEventCode.MSG_BODY_MAIL_DINIEGO_DOPO_PREAVVISO_DI_DINIEGO_IN_PAGAMENTO
										.getCode(),
								Constants.NOTIFICATORE), richiesta.getRichiedente().getNome(),
								richiesta.getRichiedente().getCognome(), richiesta.getNumero(), richiesta.getNote(),
								datacontrodedotta, null));
					} else if (perfezionatainpagamento) {
						trovato = true;
						parameters.put("TESTO_EMAIL", buildBody(
								parametroDao.selectValoreParametroFromCod(
										NotificatoreEventCode.MSG_BODY_MAIL_DINIEGO_DOPO_PERFEZIONATA_IN_PAGAMENTO
												.getCode(),
										Constants.NOTIFICATORE),
								richiesta.getRichiedente().getNome(), richiesta.getRichiedente().getCognome(),
								richiesta.getNumero(), richiesta.getNote(), datacontrodedotta, null));
					}
					
				} else if (richiesta.getStato().equalsIgnoreCase(Constants.DINIEGO)
						&& tipoLettera.equalsIgnoreCase(Constants.DINIEGO_SCADENZA_REQUISTI_DECESSO)) {
					parameters.put("TIPO_LETTERA", Constants.LETTERA_DINIEGO.replace("_", " "));
					trovato = true;
					parameters.put("TESTO_EMAIL", 
							buildBody(parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.MSG_BODY_EMAIL_BO_DINIEGO_SCADENZA_REQUISTI_DECESSO.getCode(),Constants.NOTIFICATORE),
									richiesta.getRichiedente().getNome(), richiesta.getRichiedente().getCognome(),richiesta.getNumero(),richiesta.getNote(),null, null));
	    		}else if (richiesta.getStato().equalsIgnoreCase(Constants.DINIEGO) && tipoLettera.equalsIgnoreCase(Constants.DINIEGO_SCADENZA_REQUISTI_CAMBIO_RESIDENZA)) {
	    			parameters.put("TIPO_LETTERA",Constants.LETTERA_DINIEGO.replace("_", " "));
	    			trovato = true;
	    			parameters.put("TESTO_EMAIL",
							buildBody(parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.MSG_BODY_EMAIL_BO_DINIEGO_SCADENZA_REQUISTI_CAMBIO_RESIDENZA.getCode(),Constants.NOTIFICATORE),
									richiesta.getRichiedente().getNome(), richiesta.getRichiedente().getCognome(),richiesta.getNumero(),richiesta.getNote(),null, null));
	    			// feature rimozione diniego ammessa or ammessa riserva e rami di codice obsoleti
				} else if (richiesta.getStato().equalsIgnoreCase(Constants.IN_PAGAMENTO) 
						&& tipoLettera.equalsIgnoreCase(Constants.LETTERA_AMMISSIONE_FINANZIAMENTO)) {
					parameters.put("TIPO_LETTERA", Constants.LETTERA_AMMISSIONE_FINANZIAMENTO.replace("_", " "));
					trovato = true;
	                if (statoultimo.contains(Constants.PERFEZIONATA_IN_PAGAMENTO)) {
	                    parameters.put("TESTO_EMAIL",
	                            buildBody(parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.MSG_BODY_MAIL_BO_PERFEZIONATA_IN_PAGAMENTO.getCode(),Constants.NOTIFICATORE),
	                                    richiesta.getRichiedente().getNome(), richiesta.getRichiedente().getCognome(),richiesta.getNumero(),richiesta.getNote(),null,
	                                    null)); 
	                } else {
	                	parameters.put("TESTO_EMAIL", buildBody(
							parametroDao.selectValoreParametroFromCod(
									NotificatoreEventCode.MSG_BODY_EMAIL_BO_AMMESSA_FINANZIATA.getCode(),
									Constants.NOTIFICATORE),
							richiesta.getRichiedente().getNome(), richiesta.getRichiedente().getCognome(),
							richiesta.getNumero(), richiesta.getNote(), null, Util.getDataNomeMese(richiesta.getDataRendicontazione())));
	                }
				} else if (richiesta.getStato().equalsIgnoreCase(Constants.AMMESSA_RISERVA_IN_PAGAMENTO) && tipoLettera.equalsIgnoreCase(Constants.LETTERA_AMMISSIONE_FINANZIAMENTO)) {
	                parameters.put("TIPO_LETTERA",Constants.LETTERA_AMMISSIONE_FINANZIAMENTO.replace("_", " "));
	                trovato = true;
	                ModelIncompatibilitaRichiesta incompatibilita = richiesteDao.selectIncompatibilitaRichiesta(richiesta.getNumero());
	                if (incompatibilita!=null) {
	                    if (incompatibilita.isIncompatibilitaPerContratto() && !incompatibilita.isNessunaIncompatibilita()) {
	                        parameters.put("TESTO_EMAIL",
	                                buildBody(parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.MSG_BODY_MAIL_BO_AMMESSA_RISERVA_INCOMPATIBILITA_CONTRATTO.getCode(),Constants.NOTIFICATORE),
	                                        richiesta.getRichiedente().getNome(), richiesta.getRichiedente().getCognome(),richiesta.getNumero(),richiesta.getNote(),null,null));                        
	                    } else if (incompatibilita.isIncompatibilitaPerContratto()) {
	                        parameters.put("TESTO_EMAIL",
	                                buildBody(parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.MSG_BODY_EMAIL_BO_AMMESSA_RISERVA_CONTRATTO.getCode(),Constants.NOTIFICATORE),
	                                        richiesta.getRichiedente().getNome(), richiesta.getRichiedente().getCognome(),richiesta.getNumero(),richiesta.getNote(),null,null));                    
	                    } else if (!incompatibilita.isNessunaIncompatibilita()) {
	                        parameters.put("TESTO_EMAIL",
	                                buildBody(parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.MSG_BODY_EMAIL_BO_AMMESSA_RISERVA_INCOMPATIBILITA.getCode(),Constants.NOTIFICATORE),
	                                        richiesta.getRichiedente().getNome(), richiesta.getRichiedente().getCognome(),richiesta.getNumero(),richiesta.getNote(),null,null));        
	                    }
	                }
	            } else if ((richiesta.getStato().equalsIgnoreCase(Constants.AMMESSA) 
						|| richiesta.getStato().equalsIgnoreCase(Constants.AMMESSA_RISERVA)) 
						&& tipoLettera.equalsIgnoreCase(Constants.LETTERA_AMMISSIONE_NON_FINANZIAMENTO)) {
					parameters.put("TIPO_LETTERA", Constants.LETTERA_AMMISSIONE_NON_FINANZIAMENTO.replace("_", " "));
					trovato = true;
					parameters.put("TESTO_EMAIL", buildBody(
							parametroDao.selectValoreParametroFromCod(
									NotificatoreEventCode.MSG_BODY_EMAIL_BO_AMMESSA_NON_FINANZIATA.getCode(),
									Constants.NOTIFICATORE),
							richiesta.getRichiedente().getNome(), richiesta.getRichiedente().getCognome(),
							richiesta.getNumero(), richiesta.getNote(), null, null));
				}
				if (trovato) {
					lettera = getPdfLettera(parameters);
					logInfo("creo pdf fine parametri", "pdf lettera");
					if (lettera.length < 1000) { // NO DATA
						lettera = null;
					}
					
					if (lettera != null) {
						archivialettera(lettera, richiesta.getNumero(), richiesta.getRichiedente().getCf(),
								richiesta.getDomandaDetCod(), richiesta.getSportelloId(), richiesta.getDomandaDetId(),
								tipoLettera);
					} else {
						generateResponseErrorException("Errore nella generazione del PDF della lettera",
								CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR,
								"Errore Crea Lettera " + "Errore nella generazione del PDF della lettera");
					}
				} else {
					generateResponseErrorException("Errore nella generazione del PDF della lettera",
							CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR,
							"Errore Crea Lettera " + "PDF non creato trovato=false flusso del cambio di stati non gestito");
				}
			}
			return Response.ok().entity(richiesta).build();
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}
		Response esito = error.generateResponseError();
		return esito;
	}

	//@Transactional(readOnly = true)
	public byte[] getPdfLettera(Map<String, Object> parameters) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream input = classloader.getResourceAsStream("/report/lettera.jasper");
		JasperReport jasperReport = null;
		try {
			JRPdfExporter exporter = new JRPdfExporter();

			jasperReport = (JasperReport) JRLoader.loadObject(input);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			logInfo("pdf", "esporter input");
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			logInfo("fine creo pdf", "pdf lettera");
			try {
				outputStream.close();
			} catch (IOException e) {
				logError("IOException pdf", "Errore crea lettera", e);
				return null;
			}

			SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
			reportConfig.setSizePageToContent(true);
			reportConfig.setForceLineBreakPolicy(false);
			SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();

			exportConfig.setMetadataAuthor("SISTEMA PIEMONTE");
			exportConfig.setAllowedPermissionsHint("PRINTING");

			exporter.setConfiguration(reportConfig);
			exporter.setConfiguration(exportConfig);
			exporter.exportReport();

			return outputStream.toByteArray();
		} catch (JRException e) {
			logError("JRException pdf", "Errore jasper", e);
			return null;
		}

	}

	public void archivialettera(byte[] lettera, String numerodomanda, String richiedente, String detCod,
			BigDecimal sportelloid, BigDecimal domandadetid, String tipoLettera) throws DatabaseException, IOException {
		String path = parametroDao.selectValoreParametroFromCod(Constants.PATH_ARCHIVIAZIONE,
				Constants.PARAMETRO_GENERICO);
		ModelSportello sportello = new ModelSportello();
		try {
			String nomefile = null;
			if (tipoLettera.contains(Constants.DINIEGO))
				nomefile = Constants.LETTERA_DINIEGO;
			else if (tipoLettera.contains(Constants.AMMISSIONE_FINANZIAMENTO))
				nomefile = Constants.LETTERA_AMMISSIONE_FINANZIAMENTO;
			else if (tipoLettera.contains(Constants.AMMISSIONE_NON_FINANZIAMENTO))
				nomefile = Constants.LETTERA_AMMISSIONE_NON_FINANZIAMENTO;

			path += File.separator;
			sportello = sportelliDao.selectSportelli(sportelloid);
			path += sportello.getSportelloCod();
			File dir = new File(path + File.separator);
			if (!dir.exists()) {
				dir.mkdirs();
				logInfo("creo cartella", dir.getPath());
			} else
				logInfo("cartella esiste ", dir.getPath());
			path += File.separator + richiedente.substring(0, 1).toUpperCase();
			dir = new File(path + File.separator);
			if (!dir.exists()) {
				dir.mkdirs();
				logInfo("creo cartella", dir.getPath());
			} else
				logInfo("cartella esiste ", dir.getPath());
			// aggiunta cartella numero domanda
			path += File.separator + numerodomanda.toUpperCase();
			dir = new File(path + File.separator);
			if (!dir.exists()) {
				dir.mkdirs();
				logInfo("creo cartella", dir.getPath());
			} else
				logInfo("cartella esiste ", dir.getPath());

			path += File.separator + detCod.toUpperCase();
			dir = new File(path + File.separator);
			if (!dir.exists()) {
				dir.mkdirs();
				logInfo("creo cartella", dir.getPath());
			} else
				logInfo("cartella esiste ", dir.getPath());

			File file = null;
			file = new File(path + File.separator + nomefile + ".pdf");
			logInfo("scrivo allegato su db", "pdf lettera " + path + File.separator + nomefile + ".pdf");
			// faccio update o insert su tabella allegato
			if (richiesteDao.selectEsisteAllegato(detCod, nomefile))
				richiesteDao.updateAllegato(detCod, richiedente, nomefile + ".pdf", "application/pdf", path,
						"nomefile");
			else
				richiesteDao.insertAllegato(nomefile + ".pdf", "application/pdf", path, sportelloid, domandadetid,
						detCod, nomefile, richiedente, richiedente);

			logInfo("creo pdf archivio ", "pdf lettera" + path + File.separator + nomefile + ".pdf");
			try(OutputStream out = new FileOutputStream(file)) {
				// cripto il file
				byte[] domandaCifrata = filesEncrypt.creaFileCifratoByte(Cipher.ENCRYPT_MODE, lettera);
				out.write(domandaCifrata, 0, domandaCifrata.length);
				out.close();
			} catch(Exception e)  {
				logError("cryptFile", "Errore cifratura lettera", e);
			}
		} catch (Exception e) {
			logError("Archiviazione PDF lettera", "Errore archivia lettera", e);
		}
	}

	private String buildBody(String msg, String nome, String cognome, String numero, String motivo,
			String datacontrodedotta, String datarendicontazione) {
		String result = msg;
		result = result.replace("{0}", nome + " " + cognome);
		result = result.replace("{1}", numero);
		if (StringUtils.isNotBlank(motivo)) {
			result = result.replace("{3}", motivo);
		} else
			result = result.replace("{3}", "");
		if (StringUtils.isNotBlank(datacontrodedotta)) {
			result = result.replace("{4}", datacontrodedotta);
		} else
			result = result.replace("{4}", "");
		if (StringUtils.isNotBlank(datarendicontazione)) {
			result = result.replace("{6}", datarendicontazione);
		} else
			result = result.replace("{6}", "");
		
		return result;
	}
}
