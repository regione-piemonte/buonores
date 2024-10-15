/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.business.be.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.buonores.buonoresbandisrv.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbandisrv.dto.Contact;
import it.csi.buonores.buonoresbandisrv.dto.DatiBuono;
import it.csi.buonores.buonoresbandisrv.dto.Errore;
import it.csi.buonores.buonoresbandisrv.dto.ErroreDettaglio;
import it.csi.buonores.buonoresbandisrv.dto.ModelAnagraficaBeneficiario;
import it.csi.buonores.buonoresbandisrv.dto.ModelAnagraficaPersonaFisica;
import it.csi.buonores.buonoresbandisrv.dto.ModelBandiMessage;
import it.csi.buonores.buonoresbandisrv.dto.ModelFornitore;
import it.csi.buonores.buonoresbandisrv.dto.ModelInvioBandi;
import it.csi.buonores.buonoresbandisrv.dto.ModelProgettoAgevolazione;
import it.csi.buonores.buonoresbandisrv.dto.ModelProgettoBandi;
import it.csi.buonores.buonoresbandisrv.dto.ModelRichiesta;
import it.csi.buonores.buonoresbandisrv.dto.ModelSpeseProgetto;
import it.csi.buonores.buonoresbandisrv.dto.ModelSportello;
import it.csi.buonores.buonoresbandisrv.exception.DatabaseException;
import it.csi.buonores.buonoresbandisrv.exception.ResponseErrorException;
import it.csi.buonores.buonoresbandisrv.integration.dao.custom.BuoniDao;
import it.csi.buonores.buonoresbandisrv.integration.dao.custom.CodParametroDao;
import it.csi.buonores.buonoresbandisrv.integration.dao.custom.LogBandiDao;
import it.csi.buonores.buonoresbandisrv.integration.dao.custom.RichiesteDao;
import it.csi.buonores.buonoresbandisrv.integration.dao.custom.SportelliDao;
import it.csi.buonores.buonoresbandisrv.integration.service.util.FilesEncrypt;
import it.csi.buonores.buonoresbandisrv.util.Constants;
import it.csi.buonores.buonoresbandisrv.util.Util;
import it.csi.buonores.buonoresbandisrv.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoresbandisrv.util.rest.ResponseRest;

@Service
public class ServiziVersoBandiService extends BaseService {

	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	CodParametroDao parametroDao;

	@Autowired
	SportelliDao sportelliDao;

	@Autowired
	LogBandiDao logBandiDao;

	@Autowired
	BuoniDao buoniDao;

	@Autowired
	private FilesEncrypt filesEncrypt;

	@Autowired
	ServizioRestService restbase;

	private String stringXml = null;

	public Response acquisizioneDomandaBandi(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ModelRichiesta richiesta = new ModelRichiesta();
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			List<ErroreDettaglio> listError = validateGeneric.validateDomanda(shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio, numeroRichiesta, securityContext, httpHeaders, httpRequest);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(Errore.generateErrore(HttpStatus.BAD_REQUEST, listError),
						"errore in validate");
			}

			richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			ResponseRest responserest = new ResponseRest();
			ModelBandiMessage body = new ModelBandiMessage();
			if (richiesta != null) {
				// verifico se il buono creato allora faccio ma se attivo o altro no
				String stato = "'" + Constants.CREATO + "'";
				DatiBuono buono = buoniDao.selectBuono(richiesta.getSportelloId(), richiesta.getDomandaId(), stato);
				if (buono != null) {
					// prendo i contatti
					responserest = restbase.getContatti(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
							xCodiceServizio, securityContext, httpHeaders, httpRequest);
					Contact contatto = new Contact();
					ObjectMapper mapper = new ObjectMapper();
					if (responserest != null && responserest.getStatusCode() == 200) {
						contatto = mapper.readValue(responserest.getJson(), Contact.class);
					}
					File bandi = generaXml(richiesta, contatto, buono.getDecorrenzaInizio());

					FileInputStream fl = new FileInputStream(bandi);

					byte[] xml = new byte[(int) bandi.length()];

					fl.read(xml);
					fl.close();

					String BANDI_XML = bandi.getName() + "_" + richiesta.getDomandaId() + "_1_"
							+ Util.getDataOra(new Timestamp(System.currentTimeMillis())) + ".xml";
					if (xml.length < 1000) { // NO DATA
						xml = null;
					}
					if (xml != null) {
						archiviaxml(xml, richiesta.getNumero(), richiesta.getRichiedente().getCf(),
								richiesta.getDomandaDetCod(), richiesta.getSportelloId(), richiesta.getDomandaDetId(),
								BANDI_XML);

						responserest = new ResponseRest();
						responserest = restbase.postAcquisizioneDomande(richiesta.getNumero(),
								shibIdentitaCodiceFiscale,
								xRequestId, xForwardedFor, xCodiceServizio, securityContext, httpHeaders, httpRequest,
								"file", BANDI_XML, bandi);
						mapper = new ObjectMapper();
						body = mapper.readValue(responserest.getJson(), ModelBandiMessage.class);
						// scrivo nella tabella di log di bandi
						if (body.getEsitoServizio().equalsIgnoreCase(Constants.BANDI_OK)) {
							logBandiDao.insertLogBandi(richiesta.getNumero(), buono.getBuonoId(), body.getUuid(),
									stringXml,
									body.getEsitoServizio(), null, null, Constants.INVIO_DOMANDA);
						} else {
							logBandiDao.insertLogBandi(richiesta.getNumero(), buono.getBuonoId(), null, stringXml,
									body.getEsitoServizio(), body.getCodiceErrore(), body.getMessaggio(),
									Constants.INVIO_DOMANDA);
						}

					} else {
						String param = "Errore nella generazione del xml per bandi";
						logError(metodo, "Errore Crea xml bandi ", param);
						generateResponseErrorException(CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR,
								"errore crea xml bandi", param);
					}
				} else {
					// errore non esiste il buonoid
					String param = "Errore non trovato o buono non in stato creato";
					logError(metodo, "Errore Crea xml bandi ", param);
					generateResponseErrorException(CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR,
							"errore crea xml bandi", param);
				}
			} else {
				String param = "Errore nell'invio a bandi - domanda non trovata";
				logError(metodo, param);
				generateResponseErrorException(CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR,
						"errore in invio a bandi domanda", param);
			}

			return Response.ok().entity(body).build();

		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	private ModelProgettoAgevolazione buildProgettoDiAgevolazione(ModelRichiesta richiesta, String importo,
			Date decorrenzaInizio,
			Map<String, String> parametri) throws Exception {
		ModelProgettoAgevolazione progettoDiAgevolazione = new ModelProgettoAgevolazione();
		progettoDiAgevolazione.setDataPresentazione(richiesta.getDataDomanda());
		if (Util.isValorizzato(richiesta.getOraDomanda()) && richiesta.getOraDomanda().length() >= 8) {
			progettoDiAgevolazione.setOraPresentazione(richiesta.getOraDomanda().substring(0, 8));
		} else
			progettoDiAgevolazione.setOraPresentazione(richiesta.getOraDomanda());
		progettoDiAgevolazione.setPartitaivaUnitalocale("");
		progettoDiAgevolazione.setSedeNonAttivataInPiemonte("");
		progettoDiAgevolazione.setCodiceAteco("");
		progettoDiAgevolazione.setDescrizioneAttivitaUnitalocale("");
		progettoDiAgevolazione.setCodiceSettoreUnitalocale("");
		progettoDiAgevolazione.setDescrizioneSettoreUnitalocale("");
		progettoDiAgevolazione.setStatoUnitalocale(Constants.STATO);
		progettoDiAgevolazione.setCodStatoUnitalocale(Constants.COD_STATO);
		progettoDiAgevolazione.setCapUnitalocale("");
		// preso istat da descrizione comune
		progettoDiAgevolazione.setCodiceComuneUnitalocale(
				richiesteDao.selectIstatComune(richiesta.getDestinatario().getComuneResidenza()));
		progettoDiAgevolazione.setComuneUnitalocale(richiesta.getDestinatario().getComuneResidenza());
		progettoDiAgevolazione.setProvinciaUnitalocale(richiesta.getDestinatario().getProvinciaResidenza());
		progettoDiAgevolazione.setModalitaRegistrazione(parametri.get(Constants.MODALITA_REGISTRAZIONE));
		progettoDiAgevolazione.setIndirizzoUnitalocale("");
		progettoDiAgevolazione.setTitoloProgetto("");
		progettoDiAgevolazione.setDescrizioneProgetto("");
		progettoDiAgevolazione.setAbstractProgetto("");
		progettoDiAgevolazione.setContributoRichiesto(importo);
		progettoDiAgevolazione.setFinanziamentoRichiesto("");
		progettoDiAgevolazione.setSpesaAmmessa("");
		progettoDiAgevolazione.setFinanziamentoBancario("");
		if (decorrenzaInizio != null) {
			progettoDiAgevolazione.setDataConcessione(Util.getDataISO(decorrenzaInizio));
		} else {
			progettoDiAgevolazione.setDataConcessione("");
		}

		progettoDiAgevolazione.setCodSportello(richiesteDao.selectSportello(richiesta.getSportelloId()));
		if (richiesta.getAreaId() != null)
			progettoDiAgevolazione.setCodAreaTerritoriale(richiesteDao.selectArea(richiesta.getAreaId()));
		else
			progettoDiAgevolazione.setCodAreaTerritoriale("N.D.");

		return progettoDiAgevolazione;
	}

	private ModelAnagraficaBeneficiario buildAnagraficaBeneficiario(ModelRichiesta richiesta,
			Map<String, String> parametri, Contact contatto) throws Exception {
		ModelAnagraficaBeneficiario anagraficaBeneficiario = new ModelAnagraficaBeneficiario();
		anagraficaBeneficiario.setCodTipoUtente(parametri.get(Constants.COD_TIPO_UTENTE));
		anagraficaBeneficiario.setTipoUtente(parametri.get(Constants.TIPO_UTENTE));
		anagraficaBeneficiario.setTipologiaEnte(parametri.get(Constants.TIPOLOGIA_ENTE));
		anagraficaBeneficiario.setCodiceDipartimento("");
		anagraficaBeneficiario.setDescrizioneDipartimento("");
		anagraficaBeneficiario.setFormaGiuridicaImpresa("");
		anagraficaBeneficiario.setCodiceFiscaleImpresa(richiesta.getDestinatario().getCf());
		anagraficaBeneficiario.setRagioneSocialeImpresa(
				richiesta.getDestinatario().getNome() + " " + richiesta.getDestinatario().getCognome());
		anagraficaBeneficiario.setPartitaivaSedelegale("");
		anagraficaBeneficiario.setCodiceAteco("");
		anagraficaBeneficiario.setStatoSedelegale(Constants.STATO);
		anagraficaBeneficiario.setCodStatoSedelegale(Constants.COD_STATO);
		anagraficaBeneficiario.setTipologiaRegistroIscrizione("");
		anagraficaBeneficiario.setIscrizioneIncorso("");
		anagraficaBeneficiario.setCin("");
		anagraficaBeneficiario.setAbi("");
		anagraficaBeneficiario.setCab("");
		anagraficaBeneficiario.setIban("");
		anagraficaBeneficiario.setBic("");
		anagraficaBeneficiario.setnContocorrente("");
		anagraficaBeneficiario.setRuoloBeneficiario(parametri.get(Constants.RUOLO_BENEFICIARIO));
		anagraficaBeneficiario.setClassificazioneEnte(parametri.get(Constants.CLASSIFICAZIONE_ENTE));
		anagraficaBeneficiario.setCognomePersonaFisica(richiesta.getDestinatario().getCognome());
		anagraficaBeneficiario.setNomePersonaFisica(richiesta.getDestinatario().getNome());
		anagraficaBeneficiario.setStatoNascitaPersonaFisica(richiesta.getDestinatario().getStatoNascita());
		anagraficaBeneficiario.setProvinciaNascitaPersonaFisica("");
		anagraficaBeneficiario.setProvinciaNascitaDescrizionePersonaFisica("");
		String comunenascita = richiesteDao.selectIstatComune(richiesta.getDestinatario().getComuneNascita());
		if (comunenascita != null)
			anagraficaBeneficiario.setComuneNascitaPersonaFisica(comunenascita);
		else
			anagraficaBeneficiario.setComuneNascitaPersonaFisica("");
		anagraficaBeneficiario.setStatoEsteroNascitaPersonaFisica("");
		anagraficaBeneficiario
				.setDataNascitaPersonaFisica(Util.getDataISO(richiesta.getDestinatario().getDataNascita()));
		anagraficaBeneficiario.setCodiceComuneSedelegale(
				richiesteDao.selectIstatComune(richiesta.getDestinatario().getComuneResidenza()));
		anagraficaBeneficiario.setComuneSedelegale(richiesta.getDestinatario().getComuneResidenza());
		anagraficaBeneficiario.setIndirizzoSedelegale(richiesta.getDestinatario().getIndirizzoResidenza());
		anagraficaBeneficiario.setCapSedelegale("");
		if (contatto != null) {
			anagraficaBeneficiario.setTelefonoSedelegale(contatto.getPhone());
			anagraficaBeneficiario.setEmailSedelegale(contatto.getEmail());
		} else {
			anagraficaBeneficiario.setTelefonoSedelegale("");
			anagraficaBeneficiario.setEmailSedelegale("");
		}
		anagraficaBeneficiario.setPecSedelegale("");

		return anagraficaBeneficiario;
	}

	private ModelAnagraficaPersonaFisica buildAnagraficaPersonaFisica(ModelRichiesta richiesta,
			Map<String, String> parametri) throws Exception {
		ModelAnagraficaPersonaFisica anagraficaPersonaFisica = new ModelAnagraficaPersonaFisica();
		anagraficaPersonaFisica.setCodiceFiscaleImpresa(richiesta.getRichiedente().getCf());
		anagraficaPersonaFisica.setTipoPersonaFisica(parametri.get(Constants.TIPO_PERSONAFISICA));
		anagraficaPersonaFisica.setCodiceFiscaleSoggetto(richiesta.getRichiedente().getCf());
		anagraficaPersonaFisica.setCognome(richiesta.getRichiedente().getCognome());
		anagraficaPersonaFisica.setNome(richiesta.getRichiedente().getNome());
		anagraficaPersonaFisica
				.setDataNascita(Util.getDataISO(richiesta.getRichiedente().getDataNascita()));
		anagraficaPersonaFisica.setStatoNascita(richiesta.getRichiedente().getStatoNascita());
		String comunenascitarichiedente = richiesteDao
				.selectIstatComune(richiesta.getRichiedente().getComuneNascita());
		if (comunenascitarichiedente != null)
			anagraficaPersonaFisica.setCodiceComuneNascita(comunenascitarichiedente);
		else
			anagraficaPersonaFisica.setCodiceComuneNascita("");
		anagraficaPersonaFisica.setComuneNascita(richiesta.getRichiedente().getComuneNascita());
		anagraficaPersonaFisica.setStatoIndirizzo("");
		anagraficaPersonaFisica.setCodStatoIndirizzo("");
		String comuneresidenzarichiedente = richiesteDao
				.selectIstatComune(richiesta.getRichiedente().getComuneResidenza());
		if (comuneresidenzarichiedente != null)
			anagraficaPersonaFisica.setCodiceComuneIndirizzo(comuneresidenzarichiedente);
		else
			anagraficaPersonaFisica.setCodiceComuneIndirizzo("");
		anagraficaPersonaFisica.setComuneIndirizzo(richiesta.getRichiedente().getComuneResidenza());
		anagraficaPersonaFisica.setCapIndirizzo("");
		anagraficaPersonaFisica.setIndirizzo(richiesta.getRichiedente().getIndirizzoResidenza());

		return anagraficaPersonaFisica;
	}

	private ModelSpeseProgetto buildSpeseProgetto(Map<String, String> parametri, String importo) {
		ModelSpeseProgetto speseProgetto = new ModelSpeseProgetto();
		speseProgetto.setCodiceTipologiaSpesa(parametri.get(Constants.CODICE_TIPOLOGIA_SPESA));
		speseProgetto.setDescrTipologiaSpesa(parametri.get(Constants.DESCR_TIPOLOGIA_SPESA));
		speseProgetto.setImportoSpesaRichiestaFinanziata(importo);
		speseProgetto.setImportoSpesaRichiestaNonFinanziata("");
		speseProgetto.setCodDettIntervento("");
		speseProgetto.setFlagIvaCosto(parametri.get(Constants.FLAG_IVA_COSTO));

		return speseProgetto;
	}

	private ModelFornitore buildFornitore(ModelRichiesta richiesta, Map<String, String> parametri) {
		ModelFornitore fornitore = new ModelFornitore();
		if (richiesta.getContratto() != null) {
			if (richiesta.getContratto().getTipo() != null
					&& richiesta.getContratto().getTipo().equals(Constants.CONTRATTO_RSA)) {
				String cfPiva = richiesta.getContratto().getStruttura().getPiva() != null
						? richiesta.getContratto().getStruttura().getPiva()
						: "";
				fornitore.setCodiceFiscaleFornitore(cfPiva);
				if (cfPiva.length() == 11) {
					fornitore.setPartitaIvaFornitore(cfPiva);
				}
				fornitore.setRagioneSociale(richiesta.getContratto().getStruttura().getNome());

				if (richiesta.getContratto().getDataInizio() != null)
					fornitore.setDataInizioContratto(Util.getData(richiesta.getContratto().getDataInizio()));
				else
					fornitore.setDataInizioContratto("");

				if (richiesta.getContratto().getDataFine() != null)
					fornitore.setDataFineContratto(Util.getData(richiesta.getContratto().getDataFine()));
				else
					fornitore.setDataFineContratto("");
			}
		} else {
			fornitore.setCodiceFiscaleFornitore("");
			fornitore.setPartitaIvaFornitore("");
			fornitore.setRagioneSociale("");
			fornitore.setDataInizioContratto("");
			fornitore.setDataFineContratto("");
		}

		fornitore.setCognomeFornitore("");
		fornitore.setNomeFornitore("");
		fornitore.setQualificaFornitore(parametri.get(Constants.QUALIFICAFORNITORE));
		fornitore.setFormaGiuridica("");

		return fornitore;
	}

	private File generaXml(ModelRichiesta richiesta, Contact contatto, Date decorrenzaInizio) throws DatabaseException {

		ModelInvioBandi bandiXml = new ModelInvioBandi();
		ModelProgettoBandi progetto = new ModelProgettoBandi();
		List<ModelProgettoBandi> progetti = new ArrayList<ModelProgettoBandi>();

		bandiXml.setVersion("1.0.0");
		bandiXml.setXmlns("http://www.csi.it/interscambio/finaziamenti");
		try {

			Map<String, String> parametri = parametroDao.selectParametriPerTipo(Constants.BANDI);
			String importo = richiesteDao.selectImporto(richiesta.getNumero()).toString();

			progetto.setNormaIncentivazione(parametri.get(Constants.NORMA_INCENTIVAZIONE));
			// aspettare risposta manca domanda id
			progetto.setIdProgetto(richiesta.getNumero());
			progetto.setCodiceAsse(parametri.get(Constants.CODICE_ASSE));
			progetto.setDescrizioneAsse(parametri.get(Constants.DESCRIZIONE_ASSE));
			progetto.setCodiceMisura(parametri.get(Constants.CODICE_MISURA));
			progetto.setDescrizioneMisura(parametri.get(Constants.DESCRIZIONE_MISURA));
			progetto.setDescrizioneBando(parametri.get(Constants.DESCRIZIONE_BANDO));
			progetto.setDestinatarioDescrizione(parametri.get(Constants.DESTINATARIO_DESCRIZIONE));
			progetto.setDestinatarioDirezione(parametri.get(Constants.DESTINATARIO_DIREZIONE));
			progetto.setCodiceIntervento(parametri.get(Constants.CODICE_INTERVENTO));
			progetto.setDescrizioneIntervento(parametri.get(Constants.DESCRIZIONE_INTERVENTO));

			progetto.setProgettoDiAgevolazione(
					buildProgettoDiAgevolazione(richiesta, importo, decorrenzaInizio, parametri));
			progetto.setAnagraficaBeneficiario(buildAnagraficaBeneficiario(richiesta, parametri, contatto));

			if (!richiesta.getDestinatario().getCf().equalsIgnoreCase(richiesta.getRichiedente().getCf())) {
				progetto.setAnagraficaPersonaFisica(buildAnagraficaPersonaFisica(richiesta, parametri));
			}

			progetto.setSpeseProgetto(buildSpeseProgetto(parametri, importo));
			progetto.setFornitore(buildFornitore(richiesta, parametri));

			progetti.add(progetto);
			bandiXml.setProgetti(progetti);

			JAXBContext jCont = JAXBContext.newInstance(ModelInvioBandi.class);
			Marshaller marshal = jCont.createMarshaller();
			marshal.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshal.marshal(bandiXml, System.out);
			StringWriter sw = new StringWriter();
			marshal.marshal(bandiXml, sw);
			stringXml = sw.toString();
			String BANDI_XML = parametri.get(Constants.NOME_FILE);
			File pdfFile = new File(BANDI_XML);
			marshal.marshal(bandiXml, pdfFile);

			return pdfFile;
		} catch (JAXBException e) {
			logError("generaXml", "Error creating file");
		} catch (Exception e) {
			logError("generaXml", "Generic error creating file - " + e);
		}
		return null;
	}

	public void archiviaxml(byte[] lettera, String numerodomanda, String richiedente, String detCod,
			BigDecimal sportelloid, BigDecimal domandadetid, String nomefile) throws DatabaseException, IOException {
		String path = parametroDao.selectValoreParametroFromCod(Constants.PATH_ARCHIVIAZIONE,
				Constants.PARAMETRO_GENERICO);
		ModelSportello sportello = new ModelSportello();
		try {

			path += File.separator;

			sportello = sportelliDao.selectSportelli(sportelloid);
			path += sportello.getSportelloCod();
			createFolder(path);

			path += File.separator + richiedente.substring(0, 1).toUpperCase();
			createFolder(path);

			// aggiunta cartella numero domanda
			path += File.separator + numerodomanda.toUpperCase();
			createFolder(path);

			String buonoCod = buoniDao.selectBuonoCodFromNumeroDomanda(numerodomanda);

			path += File.separator + buonoCod.toUpperCase();
			createFolder(path);

			path += File.separator + "XML";
			createFolder(path);

			File file = new File(path + File.separator + nomefile);

			logInfo("creo xml bandi ", "xml bandi lettera" + path + " /" + nomefile);
			OutputStream out = new FileOutputStream(file);
			// cripto il file
			byte[] domandaCifrata = filesEncrypt.creaFileCifratoByte(Cipher.ENCRYPT_MODE, lettera);
			out.write(domandaCifrata, 0, domandaCifrata.length);

			out.close();
		} catch (Exception e) {
			logError("Archiviazione xml bandi", "Errore archivia xml bandi", e);
		}
	}

	private void createFolder(String path) {
		File dir = new File(path + File.separator);
		if (!dir.exists()) {
			dir.mkdirs();
			logInfo("creo cartella", dir.getPath());
		} else {
			logInfo("cartella esiste ", dir.getPath());
		}
	}
}
