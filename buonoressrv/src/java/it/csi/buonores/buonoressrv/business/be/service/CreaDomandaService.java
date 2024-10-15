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
import java.util.ArrayList;
import java.util.Date;
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

import it.csi.buonores.buonoressrv.business.be.service.base.BaseService;
import it.csi.buonores.buonoressrv.dto.Contact;
import it.csi.buonores.buonoressrv.dto.Errore;
import it.csi.buonores.buonoressrv.dto.ErroreDettaglio;
import it.csi.buonores.buonoressrv.dto.ModelGetAllegatoExt;
import it.csi.buonores.buonoressrv.dto.ModelPersona;
import it.csi.buonores.buonoressrv.dto.ModelRichiesta;
import it.csi.buonores.buonoressrv.dto.ModelSportello;
import it.csi.buonores.buonoressrv.exception.DatabaseException;
import it.csi.buonores.buonoressrv.exception.ResponseErrorException;
import it.csi.buonores.buonoressrv.integration.dao.custom.CodParametroDao;
import it.csi.buonores.buonoressrv.integration.dao.custom.RichiesteDao;
import it.csi.buonores.buonoressrv.integration.dao.custom.SportelliDao;
import it.csi.buonores.buonoressrv.integration.notificatore.NotificatoreService;
import it.csi.buonores.buonoressrv.integration.service.util.FilesEncrypt;
import it.csi.buonores.buonoressrv.util.BuonoResSrvProperties;
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
public class CreaDomandaService extends BaseService {

	@Autowired
	BuonoResSrvProperties properties;
	@Autowired
	RichiesteDao richiesteDao;
	@Autowired
	CodParametroDao parametroDao;
	@Autowired
	SportelliDao sportelliDao;
	@Autowired
	private FilesEncrypt filesEncrypt;
	@Autowired
	private NotificatoreService notificatore;

	public Response creaDomanda(String numeroRichiesta, String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Errore error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			List<ErroreDettaglio> listError = validateGeneric.validateDomanda(shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio, numeroRichiesta, securityContext, httpHeaders, httpRequest);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(Errore.generateErrore(HttpStatus.BAD_REQUEST, listError),
						"errore in validate");
			}
			ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			byte[] domanda = null;
			if (richiesta != null) {
				domanda = creadomandaByteArray(richiesta, shibIdentitaCodiceFiscale);
				if (domanda != null)
					archiviadomanda(domanda, richiesta.getNumero(), richiesta.getRichiedente().getCf(),
							richiesta.getDomandaDetCod(), richiesta.getSportelloId(), richiesta.getDomandaDetId());
				else {
					String param = "Errore nella generazione del PDF Domanda";
					logError(metodo, "Errore Crea Domanda ", param);
					generateResponseErrorException(param, CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR,
							"errore in crea domanda");
				}
			}
			return Response.ok().entity(richiesta).build();
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}
		return error.generateResponseError();
	}

	public byte[] getPdfDomanda(Map<String, Object> parameters) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//		 InputStream input = classloader.getResourceAsStream("/report/domanda.jrxml");
		InputStream input = classloader.getResourceAsStream("/report/domanda.jasper");
		JasperReport jasperReport = null;
		try {
//			 logInfo("creo pdf", "pdf domanda");
//			 jasperReport = JasperCompileManager.compileReport(input);
//			 logInfo("fine creo pdf", "pdf domanda compilata");
//			 JasperPrint jasperPrint = null;
//			 jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new
//			 JREmptyDataSource());
//			 logInfo("pdf", "fill report");
			JRPdfExporter exporter = new JRPdfExporter();

			jasperReport = (JasperReport) JRLoader.loadObject(input);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			logInfo(metodo, "esporter input");
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			logInfo(metodo, "pdf domanda");
			try {
				outputStream.close();
			} catch (IOException e) {
				logError("IOException pdf", "Errore crea domanda", e);
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

	public byte[] creadomandaByteArray(ModelRichiesta domanda, String iride) throws DatabaseException {

		Map<String, Object> parameters = new HashMap<String, Object>();
		logInfo("creo pdf parametri", "pdf domanda parametri");
		try {
			if (Util.isValorizzato(domanda.getNumero())) {
				parameters.put("NUMERO_DOMANDA", domanda.getNumero().toUpperCase());
			}
			parameters.put("STATO", getValueOrDefaultByUtils(domanda.getDomandaStatoDesc()).toUpperCase());

			// Il controllo del destinatario non viene fatto, perche' e' obbligatorio su body richiesta
			if (domanda.getRichiedente() != null) {
				configuraRichiedente(domanda, iride, parameters);
				configuraDestinatario(domanda, parameters);
			}

			// STUDI DESTINATARIO
			if (domanda.getStudioDestinatario() != null) {
				configuraStudiDestinatario(domanda, parameters);
			}
			// TIPO DELEGA
			if (domanda.getDelega() != null) {
				configuraDelega(domanda, parameters);
			}
			// MULTIDIMENSIONALITA null valore dei default se non over65 o disabile
			parameters.put("ULTRA65", null);
			if (domanda.getValutazioneMultidimensionale() != null) {
				if (domanda.getValutazioneMultidimensionale().equalsIgnoreCase(Constants.PERSONA_PIU_65_ANNI)) {
					parameters.put("ULTRA65", true);
				} else if (domanda.getValutazioneMultidimensionale().equalsIgnoreCase(Constants.PERSONA_DISABILE)) {
					parameters.put("ULTRA65", false);
				}
			}

			// NESSUNA INCOMPATIBILITA Default null or true or false start to BOOLEAN
			parameters.put("NESSUNA_INCOMPATIBILITA", domanda.isNessunaIncompatibilita());

			parameters.put("PUNTEGGIO", domanda.getPunteggioBisognoSociale());
			
			// CONTRATTO
			if (domanda.getContratto() != null) {
				configuraContratto(domanda, parameters);
			}
			String dgr = parametroDao.selectValoreParametroFromCod(Constants.DGR, Constants.PARAMETRO_GENERICO);
			parameters.put("DGR", getValueOrDefault(dgr).toUpperCase());
			
			// ALLEGATI
			List<ModelGetAllegatoExt> allegati = new ArrayList<ModelGetAllegatoExt>();
			allegati = richiesteDao.selectAllegati(domanda.getDomandaDetId());
			String elenco_allegati = "";
			String elenco_allegati_controdeduzione = "";
			for (ModelGetAllegatoExt allegato : allegati) {
				if (!allegato.getCodTipoAllegato().equalsIgnoreCase(Constants.DOMANDA)
						&& !allegato.getCodTipoAllegato().equalsIgnoreCase(Constants.CONTRODEDUZIONE)) { // MODIFICA TAG
					elenco_allegati = elenco_allegati + allegato.getDescTipoAllegato();
					elenco_allegati = elenco_allegati + " : " + allegato.getFileName() + System.lineSeparator();
				} else if (allegato.getCodTipoAllegato().equalsIgnoreCase(Constants.CONTRODEDUZIONE)) { // MODIFICA TAG
																										// 004 buonodom
					elenco_allegati_controdeduzione = elenco_allegati_controdeduzione + allegato.getDescTipoAllegato();
					elenco_allegati_controdeduzione = elenco_allegati_controdeduzione + " : " + allegato.getFileName()
							+ System.lineSeparator();
				}
			}

			parameters.put("ELENCO_ALLEGATI", getValueOrDefaultByUtils(elenco_allegati));

			// MODIFICA TAG 004 buonodom
			if (Util.isValorizzato(elenco_allegati_controdeduzione)
					|| Util.isValorizzato(domanda.getNoteRichiedente())) {
				parameters.put("CONTRODEDUZIONE", true);
			} else {
				parameters.put("CONTRODEDUZIONE", false);
			}

			parameters.put("ELENCO_ALLEGATI_CONTRODEDUZIONE",
					getValueOrDefaultByUtils(elenco_allegati_controdeduzione));
			parameters.put("NOTA_CONTRODEDUZIONE", getValueOrDefaultByUtils(domanda.getNoteRichiedente()));
			//
			byte[] byteArray = getPdfDomanda(parameters);
			if (byteArray.length < 1000) // NO DATA
				return null;
			logInfo("creo pdf fine parametri", "pdf domanda");
			return byteArray;
		} catch (Exception e) {
			logError("Creazione PDF Domanda", "Errore crea domanda", e);
		}
		return null;
	}

	private void configuraDelega(ModelRichiesta domanda, Map<String, Object> parameters) {
		boolean isPotestaGenitoriale = domanda.getDelega().equalsIgnoreCase(Constants.POTESTA_GENITORIALE);
		boolean isNucleoFamiliare = domanda.getDelega().equalsIgnoreCase(Constants.NUCLEO_FAMILIARE);
		boolean isConiuge = domanda.getDelega().equalsIgnoreCase(Constants.CONIUGE);
		boolean isRapportoParente = domanda.getDelega().equalsIgnoreCase(Constants.PARENTE_PRIMO_GRADO);
		boolean isTutela = domanda.getDelega().equalsIgnoreCase(Constants.TUTELA);
		boolean isCuratela = domanda.getDelega().equalsIgnoreCase(Constants.CURATELA);
		boolean isAmministrazioneSostegno = domanda.getDelega().equalsIgnoreCase(Constants.AMMINISTRAZIONE_SOSTEGNO);
		boolean isAltro = domanda.getDelega().equalsIgnoreCase(Constants.ALTRO);
		parameters.put("RAPPORTO_POTESTA", isPotestaGenitoriale);
		parameters.put("RAPPORTO_CONIUGE", isConiuge);
		parameters.put("RAPPORTO_PARENTE", isRapportoParente);
		parameters.put("RAPPORTO_TUTELA", isTutela);
		parameters.put("RAPPORTO_CURATELA", isCuratela);
		parameters.put("RAPPORTO_SOSTEGNO", isAmministrazioneSostegno);
		parameters.put("RAPPORTO_NUCLEO", isNucleoFamiliare);
		parameters.put("RAPPORTO_ALTRO", isAltro);
	}

	private void configuraStudiDestinatario(ModelRichiesta domanda, Map<String, Object> parameters) {
		boolean isDiplomaPrimoGrado = domanda.getStudioDestinatario().equalsIgnoreCase(Constants.DIPLOMA_PRIMO_GRADO);
		boolean isDiplomaSecondoGrado = domanda.getStudioDestinatario()
				.equalsIgnoreCase(Constants.DIPLOMA_SECONDO_GRADO);
		boolean isDiplomaTerziaria = domanda.getStudioDestinatario().equalsIgnoreCase(Constants.DIPLOMA_TERZIARIA);
		boolean isNessunTitolo = domanda.getStudioDestinatario().equalsIgnoreCase(Constants.NESSUN_TITOLO_STUDIO);
		parameters.put("STUDIO_PRIMO", isDiplomaPrimoGrado);
		parameters.put("STUDIO_SECONDO", isDiplomaSecondoGrado);
		parameters.put("STUDIO_TERZO", isDiplomaTerziaria);
		parameters.put("NESSUN_TITOLO_STUDIO", isNessunTitolo);
	}

	// CONFIGURA I DATI DEL RICHIEDENTE
	private void configuraRichiedente(ModelRichiesta domanda, String iride, Map<String, Object> parameters)
			throws DatabaseException, Exception {
		ModelPersona richiedente = domanda.getRichiedente();
		parameters.put("RICHIEDENTE_DENOMINAZIONE",
				(richiedente.getCognome() + " " + richiedente.getNome()).toUpperCase());
		String richiedenteNatoA = (Util.isValorizzato(richiedente.getComuneNascita()))
				? richiedente.getComuneNascita() + " (" + richiedente.getProvinciaNascita() + ")"
				: richiedente.getStatoNascita();
		parameters.put("RICHIEDENTE_NATOA", richiedenteNatoA.toUpperCase());
		parameters.put("RICHIEDENTE_NATOIL", Util.getDataISO(richiedente.getDataNascita()));
		parameters.put("RICHIEDENTE_RESIDENTEA", richiedente.getComuneResidenza().toUpperCase() + " ("
				+ richiedente.getProvinciaResidenza().toUpperCase() + ")");
		parameters.put("RICHIEDENTE_RESIDENTEIND", richiedente.getIndirizzoResidenza().toUpperCase());
		parameters.put("RICHIEDENTE_CF", richiedente.getCf().toUpperCase());
		if (domanda.getDomicilioDestinatario() != null) {
			parameters.put("DESTINATARIO_DOMICILIO", getValueOrDefault(domanda.getDomicilioDestinatario().getComune()));
			parameters.put("DESTINATARIO_DOMICILIOIND",
					getValueOrDefault(domanda.getDomicilioDestinatario().getIndirizzo()));
		}
		parameters.put("ASL", getValueOrDefaultByUtils(domanda.getAslDestinatario()));
		// LAVORO DESTINATARIO DEFAULT NULL => start to Boolean
		parameters.put("LAVORO_DESTINATARIO", domanda.isLavoroDestinatario());
		// Richiedente contatto
		Contact contatto = estraiContatti(richiedente.getCf(), iride);
		if (contatto != null) {
			if (contatto.getSms() != null) {
				parameters.put("RICHIEDENTE_TEL", contatto.getSms());
			} else if (contatto.getPhone() != null) {
				parameters.put("RICHIEDENTE_TEL", contatto.getPhone());
			} else {
				parameters.put("RICHIEDENTE_TEL", "");
			}
			parameters.put("RICHIEDENTE_MAIL", getValueOrDefault(contatto.getEmail()));
		}
	}

	private void configuraDestinatario(ModelRichiesta domanda, Map<String, Object> parameters) {
		if (domanda.getRichiedente().getCf().equalsIgnoreCase(domanda.getDestinatario().getCf())) {
			parameters.put("SESTESSO", true);
			parameters.put("DESTINATARIO_DENOMINAZIONE", "");
			parameters.put("DESTINATARIO_NATOA", "");
			parameters.put("DESTINATARIO_NATOIL", "");
			parameters.put("DESTINATARIO_RESIDENTEA", "");
			parameters.put("DESTINATARIO_RESIDENTEIND", "");
			parameters.put("DESTINATARIO_CF", "");
		} else {
			ModelPersona destinatario = domanda.getDestinatario();
			// per altra persona
			parameters.put("SESTESSO", false);
			parameters.put("DESTINATARIO_DENOMINAZIONE",
					(destinatario.getCognome() + " " + destinatario.getNome()).toUpperCase());
			if (Util.isValorizzato(destinatario.getComuneNascita())) {
				parameters.put("DESTINATARIO_NATOA",
						(destinatario.getComuneNascita() + " (" + destinatario.getProvinciaNascita() + ")")
								.toUpperCase());
			} else {
				parameters.put("DESTINATARIO_NATOA", destinatario.getStatoNascita().toUpperCase());
			}

			parameters.put("DESTINATARIO_NATOIL", Util.getDataISO(destinatario.getDataNascita()));
			parameters.put("DESTINATARIO_RESIDENTEA",
					(destinatario.getComuneResidenza() + " (" + destinatario.getProvinciaResidenza() + ")")
							.toUpperCase());
			parameters.put("DESTINATARIO_RESIDENTEIND", destinatario.getIndirizzoResidenza().toUpperCase());
			parameters.put("DESTINATARIO_CF", domanda.getDestinatario().getCf().toUpperCase());
		}
	}

	private void configuraContratto(ModelRichiesta domanda, Map<String, Object> parameters) {
		parameters.put("DATA_INI_CONTRATTO", getValueOrDefaultTimestamp(domanda.getContratto().getDataInizio()));
		// RIMOZIONE_INTESTATARIO_DATA_FINE POST_DEMO 14_04_2023
//		parameters.put("DATA_FINE_CONTRATTO", getValueOrDefaultTimestamp(domanda.getContratto().getDataFine()));
		parameters.put("DATA_FINE_CONTRATTO","");
		parameters.put("CONTRATTO_TIPO_RSA", false);
		parameters.put("CONTRATTO_TIPO_NESSUNO", true);
		if (domanda.getContratto().getTipo() != null) {
			boolean isContrattoRSA = domanda.getContratto().getTipo().equalsIgnoreCase(Constants.CONTRATTO_RSA);
			boolean isNessunContratto = domanda.getContratto().getTipo().equalsIgnoreCase(Constants.NESSUN_CONTRATTO);
			parameters.put("CONTRATTO_TIPO_RSA", isContrattoRSA);
			parameters.put("CONTRATTO_TIPO_NESSUNO", isNessunContratto);
		}
//		configuraIntestatario(domanda, parameters);
		// RIMOZIONE_INTESTATARIO_DATA_FINE POST_DEMO 14_04_2023
//		RelazioneDestinatario
//		parameters.put("RELAZIONE_DESTINATARIO",
//				getValueOrDefault(domanda.getContratto().getRelazioneDestinatario()).toUpperCase());
		if (domanda.getContratto().getStruttura() != null) {
			parameters.put("RSA_DENOMINAZIONE", domanda.getContratto().getStruttura().getNome());
			parameters.put("RSA_COMUNE", domanda.getContratto().getStruttura().getComune());
			parameters.put("RSA_INDIRIZZO", domanda.getContratto().getStruttura().getIndirizzo());
		}
	}

	private Contact estraiContatti(String cf, String iride) throws DatabaseException, Exception {
		String parametrocontatti = parametroDao.selectValoreParametroFromCod(Constants.CHIAMA_CONTATTI,
				Constants.PARAMETRO_GENERICO);
		Contact contatto = new Contact();
		boolean verificasechiamarecontatti = true;
		if (parametrocontatti != null)
			verificasechiamarecontatti = parametrocontatti.equalsIgnoreCase("TRUE") ? true : false;
		if (verificasechiamarecontatti) {
			contatto = notificatore.notificaContact(cf, iride);
		} else {
			contatto.setEmail("provamail@testcarico.it");
			contatto.setSms("cellcarico");
			contatto.setPhone("telcarico");
		}
		return contatto;
	}
	// RIMOZIONE_INTESTATARIO_DATA_FINE POST_DEMO 14_04_2023
//	private void configuraIntestatario(ModelRichiesta domanda, Map<String, Object> parameters) {
//		parameters.put("DATORE_DESTINATARIO", false);
//		parameters.put("DATORE_RICHIEDENTE", false);
//		parameters.put("DATORE_ALTRO", false);
//		parameters.put("DATORE_CF", "");
//		parameters.put("DATORE_NATOA", "");
//		parameters.put("DATORE_DENOMINAZIONE", "");
//		parameters.put("DATORE_NATOIL", "");
//		if (domanda.getContratto().getIntestatario() != null) {
//			ModelPersonaSintesi intestatario = domanda.getContratto().getIntestatario();
//			if (intestatario.getCf() != null) {
//				boolean isDatoreDestinatario = intestatario.getCf().equalsIgnoreCase(domanda.getDestinatario().getCf());
//				boolean isDatoreRichiedente = intestatario.getCf().equalsIgnoreCase(domanda.getRichiedente().getCf());
//				boolean isAltro = !isDatoreDestinatario && !isDatoreRichiedente;
//				parameters.put("DATORE_DESTINATARIO", isDatoreDestinatario);
//				parameters.put("DATORE_RICHIEDENTE", isDatoreRichiedente);
//				parameters.put("DATORE_ALTRO", isAltro);
//			}
//			String datoreDenominazione = (Util.isValorizzato(intestatario.getCognome())
//					&& Util.isValorizzato(intestatario.getNome()))
//							? intestatario.getCognome().toUpperCase() + " " + intestatario.getNome().toUpperCase()
//							: "";
//			parameters.put("DATORE_DENOMINAZIONE", datoreDenominazione);
//			String natoA = (Util.isValorizzato(intestatario.getComuneNascita()))
//					? intestatario.getComuneNascita().toUpperCase()
//					: "";
//			parameters.put("DATORE_NATOA", natoA);
//			parameters.put("DATORE_NATOIL", Util.getDataISO(intestatario.getDataNascita()));
//			String datoreCF = Util.isValorizzato(intestatario.getCf()) ? intestatario.getCf().toUpperCase() : "";
//			parameters.put("DATORE_CF", datoreCF);
//		}
//	}

	/**
	 * ARCHIVIA DOMANDA
	 * 
	 * @param domanda
	 * @param numerodomanda
	 * @param richiedente
	 * @param detCod
	 * @param sportelloid
	 * @param domandadetid
	 * @throws DatabaseException
	 * @throws IOException
	 */
	public void archiviadomanda(byte[] domanda, String numerodomanda, String richiedente, String detCod,
			BigDecimal sportelloid, BigDecimal domandadetid) throws DatabaseException, IOException {
		String path = parametroDao.selectValoreParametroFromCod(Constants.PATH_ARCHIVIAZIONE,
				Constants.PARAMETRO_GENERICO);
		ModelSportello sportello = new ModelSportello();
		try {
			sportello = sportelliDao.selectSportelli(sportelloid);
			path += File.separator + sportello.getSportelloCod();
			File dir = new File(path + File.separator);
			if (!dir.exists()) {
				dir.mkdirs();
				logInfo("creo cartella", dir.getPath());
			} else {
				logInfo("cartella esiste ", dir.getPath());
			}
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

			File file = new File(path + File.separator + "DOMANDA.pdf");
			logInfo("scrivo allegato su db", "pdf domanda " + path + File.separator + "DOMANDA.pdf");
			// faccio update o insert su tabella allegato
			if (richiesteDao.selectEsisteAllegato(detCod, "DOMANDA"))
				richiesteDao.updateAllegato(detCod, richiedente, "DOMANDA.pdf", "application/pdf", path, "DOMANDA");
			else
				richiesteDao.insertAllegato("DOMANDA.pdf", "application/pdf", path, sportelloid, domandadetid, detCod,
						"DOMANDA", richiedente, richiedente);

			logInfo("creo pdf archivio ", "pdf domanda" + path + File.separator + "DOMANDA.pdf");
			try(OutputStream out = new FileOutputStream(file)) {
				// cripto il file
				byte[] domandaCifrata = filesEncrypt.creaFileCifratoByte(Cipher.ENCRYPT_MODE, domanda);
				out.write(domandaCifrata, 0, domandaCifrata.length);
				out.close();
			} catch(Exception e)  {
				logError("cryptFile", "Errore cifratura allegati", e);
			}
		} catch (Exception e) {
			logError("Archiviazione PDF Domanda", "Errore archivia domanda", e);
		}
	}

	private String getValueOrDefault(String input) {
		return StringUtils.isNotBlank(input) ? input : "";
	}

	private String getValueOrDefaultByUtils(String input) {
		return Util.isValorizzato(input) ? input : "";
	}

	private String getValueOrDefaultTimestamp(Date date) {
		return date != null ? Util.getDataISO(date) : "";
	}
}
