/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.business.be.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonoressrv.business.be.service.base.BaseService;
import it.csi.buonores.buonoressrv.dto.Errore;
import it.csi.buonores.buonoressrv.dto.ModelGetAllegatoExt;
import it.csi.buonores.buonoressrv.dto.ModelRichiesta;
import it.csi.buonores.buonoressrv.dto.ModelSportello;
import it.csi.buonores.buonoressrv.exception.DatabaseException;
import it.csi.buonores.buonoressrv.exception.ResponseErrorException;
import it.csi.buonores.buonoressrv.external.startdas.services.stardascommontypes.ConfigurazioneChiamanteType;
import it.csi.buonores.buonoressrv.external.startdas.services.stardascommontypes.DatiSmistaDocumentoType;
import it.csi.buonores.buonoressrv.external.startdas.services.stardascommontypes.DocumentoElettronicoType;
import it.csi.buonores.buonoressrv.external.startdas.services.stardascommontypes.EmbeddedBinaryType;
import it.csi.buonores.buonoressrv.external.startdas.services.stardascommontypes.MetadatiType;
import it.csi.buonores.buonoressrv.external.startdas.services.stardascommontypes.MetadatoType;
import it.csi.buonores.buonoressrv.external.startdas.services.stardascommontypes.ResultType;
import it.csi.buonores.buonoressrv.external.startdas.stardasservice.SmistaDocumentoRequestType;
import it.csi.buonores.buonoressrv.external.startdas.stardasservice.SmistaDocumentoResponseType;
import it.csi.buonores.buonoressrv.external.startdas.stardasservice.StardasServiceProxy;
import it.csi.buonores.buonoressrv.external.startdas.stardasservice.StardasServiceProxyPortType;
import it.csi.buonores.buonoressrv.integration.dao.custom.CodParametroDao;
import it.csi.buonores.buonoressrv.integration.dao.custom.RichiesteDao;
import it.csi.buonores.buonoressrv.integration.dao.custom.SportelliDao;
import it.csi.buonores.buonoressrv.integration.rest.ApiManagerServiceClient;
import it.csi.buonores.buonoressrv.integration.service.util.FilesEncrypt;
import it.csi.buonores.buonoressrv.util.BuonoResSrvProperties;
import it.csi.buonores.buonoressrv.util.Constants;
import it.csi.buonores.buonoressrv.util.Util;
import it.csi.buonores.buonoressrv.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoressrv.util.validator.impl.ValidateGenericImpl;

@Service
public class SmistaDocumentoPartenzaService extends BaseService {
	@Autowired
	private BuonoResSrvProperties properties;
	@Autowired
	ApiManagerServiceClient tokenApiManager;
	@Autowired
	RichiesteDao richiesteDao;
	@Autowired
	CodParametroDao codParametro;
	@Autowired
	private FilesEncrypt filesEncrypt;
	@Autowired
	private SportelliDao sportelliDao;

	public Response execute(String numerorichiesta, String tipolettera, String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		ModelRichiesta richiesta = new ModelRichiesta();
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		SmistaDocumentoResponseType result = new SmistaDocumentoResponseType();
		try {
			ModelGetAllegatoExt lettera = new ModelGetAllegatoExt();
			// prendo la richiesta
			richiesta = richiesteDao.selectNumeroRichiesta(numerorichiesta);
			if (richiesta != null) {
				// prendo allegati solo della tipologia DINIEGO AMMESSA FINANZIATA E AMMESSA NON
				// FINANZIATA
				lettera = richiesteDao.selectAllegatiProtPartenza(richiesta.getDomandaDetId(), tipolettera);
				String parametrostardas = codParametro.selectValoreParametroFromCod(Constants.CHIAMA_STARDAS,
						Constants.PARAMETRO_GENERICO);
				boolean verificasechiamareStardas = true;
				if (parametrostardas != null)
					verificasechiamareStardas = parametrostardas.equalsIgnoreCase("TRUE") ? true : false;
				if (lettera != null) {
					lettera.setFilebyte(getAllegato(lettera.getFilePath(), lettera.getFileName()));
					if (lettera.getFilebyte().length > 0) {
						StardasServiceProxy stardas = new StardasServiceProxy();
						StardasServiceProxyPortType port = stardas.getStardasServiceProxyHttpEndpoint();
						setWSSecurity((BindingProvider) port);
						if (verificasechiamareStardas) {
							// documento prinicipale
							result = sendStardas(lettera, richiesta, 0, port);
						} else {
							ResultType risultato = new ResultType();
							result.setMessageUUID("urn:uuidstartdasdomandatestdicarico");
							risultato.setCodice(Constants.STARDAS_OK);
							risultato.setMessaggio("EsitoOK");
							result.setResult(risultato);
						}
						String messageuuid = null;
						if (result == null || result.getResult() == null  
								|| (!result.getResult().getCodice().equalsIgnoreCase(Constants.STARDAS_OK)
								&& !result.getResult().getCodice().equalsIgnoreCase(Constants.STARDAS_OK_PARZIALE))) {
							String message = (result != null && result.getResult() != null)
									? result.getResult().getMessaggio()
											: "Errore riguardante Stardas Invio Lettera";
							generateResponseErrorException(message, CodeErrorEnum.ERR03,
									HttpStatus.INTERNAL_SERVER_ERROR, "Errore riguardante Stardas Invio Lettera");
						} else {
							messageuuid = result.getMessageUUID();
							// se esito ok per allegati allora aggiorno uuid nella tabella
							richiesteDao.updateMessageUUID(richiesta.getDomandaDetCod(), messageuuid);
							return Response.ok().entity("OKSTARDAS").build();
						}
					} else {
						generateResponseErrorException("Lettera domanda vuota " + numerorichiesta, CodeErrorEnum.ERR03,
								HttpStatus.INTERNAL_SERVER_ERROR, "Lettera domanda vuota  " + numerorichiesta);
					}
				} else {
					generateResponseErrorException("Lettera domanda non trovata " + numerorichiesta,
							CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR,
							"Lettera domanda non trovata " + numerorichiesta);
				}
			} else {
				generateResponseErrorException("Domanda non trovata " + numerorichiesta, CodeErrorEnum.ERR03,
						HttpStatus.INTERNAL_SERVER_ERROR, "Domanda non trovata " + numerorichiesta);
			}
		} catch (WebServiceException ws) {
			error = handleWebServiceException(metodo, ws);
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

	/*
	 * • CODICE_SERIE_DOSSIER • DESCRIZIONE_SERIE_DOSSIER (Coincide con
	 * PAROLA_CHIAVE_SERIE_DOSSIER che carica in automatico da
	 * DESCRIZIONE_SERIE_DOSSIER) • NUMERO_DOSSIER • DESCRIZIONE_DOSSIER
	 * (Coincide con PAROLA_CHIAVE_DOSSIER che carica in automatico da
	 * DESCRIZIONE_DOSSIER) • NUMERO_FASCICOLO • OGGETTO FASCICOLO ( Coincide
	 * con PAROLA_CHIAVE_FASCICOLO che carica in automatico da OGGETTO _FASCICOLO)
	 * • OGGETTO_DOCUMENTO • PAROLA_CHIAVE_DOCUMENTO • AUTORE_FISICO_DOCUMENTO
	 * • DATA_CRONICA_DOCUMENTO • MITTENTE_PF_COGNOME • MITTENTE_PF_NOME •
	 * MITTENTE_PF_CODICE_FISCALE Allegati (BUONODOM_ALL): • OGGETTO_DOCUMENTO •
	 * PAROLA_CHIAVE_DOCUMENTO • DATA_CRONICA_DOCUMENTO
	 */
	private SmistaDocumentoResponseType sendStardas(ModelGetAllegatoExt allegato, ModelRichiesta richiesta,
			int numallegati, StardasServiceProxyPortType port) {
		SmistaDocumentoResponseType smistaResponse = new SmistaDocumentoResponseType();
		SmistaDocumentoRequestType smistaRequest = new SmistaDocumentoRequestType();
		ConfigurazioneChiamanteType chiamante = new ConfigurazioneChiamanteType();
		DatiSmistaDocumentoType smistadocumento = new DatiSmistaDocumentoType();
		try {
			Map<String, String> parametri = codParametro.selectParametriPerTipo(Constants.STARDAS);
			chiamante.setCodiceApplicazione(parametri.get(Constants.CODICEAPPLICAZIONE));
			chiamante.setCodiceFiscaleEnte(parametri.get(Constants.CODICEFISCALEENTE));
			chiamante.setCodiceFruitore(parametri.get(Constants.CODICEFRUITORE));
			chiamante.setCodiceTipoDocumento(parametri.get(Constants.CODICETIPODOCUMENTOPAR));
			smistaRequest.setConfigurazioneChiamante(chiamante);

			// smistadocumento
			// verificare se mettere cf generico o quello richiedente
			smistadocumento.setIdDocumentoFruitore(richiesta.getDomandaDetCod() + "_" + allegato.getCodTipoAllegato());
			smistadocumento.setResponsabileTrattamento(parametri.get(Constants.CFTRATTAMENTO));

			DocumentoElettronicoType docelettronico = new DocumentoElettronicoType();
			EmbeddedBinaryType binarytype = new EmbeddedBinaryType();
			// invio allegato
			docelettronico.setNomeFile(allegato.getFileName());
			docelettronico.setMimeType(allegato.getFileType());
			// aggiunto
			docelettronico.setDocumentoFirmato(false);
			binarytype.setContent(allegato.getFilebyte());
			docelettronico.setContenutoBinario(binarytype);
			smistadocumento.setDocumentoElettronico(docelettronico);

			MetadatiType metadati = new MetadatiType();
			List<MetadatoType> metadato = new ArrayList<MetadatoType>();
			MetadatoType metadatosin = new MetadatoType();
			ModelSportello sportelli = sportelliDao.selectSportelli(richiesta.getSportelloId());
			// SERIE DOSSIER
			metadatosin.setNome(Constants.CODICE_SERIE_DOSSIER);
			metadatosin.setValore(parametri.get(Constants.CODICEFRUITORE) + " " + sportelli.getSportelloAnno());
			metadato.add(metadatosin);
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.DESCRIZIONE_SERIE_DOSSIER);
			metadatosin.setValore(richiesta.getContributoTipoDesc() + " " + sportelli.getSportelloAnno());
			metadato.add(metadatosin);
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.PAROLA_CHIAVE_SERIE_DOSSIER);
			metadatosin.setValore(richiesta.getContributoTipoDesc() + " " + sportelli.getSportelloAnno());
			metadato.add(metadatosin);
			// DOSSIER
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.CODICE_DOSSIER);
			metadatosin.setValore(sportelli.getSportelloCod());
			metadato.add(metadatosin);
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.DESCRIZIONE_DOSSIER);
			metadatosin.setValore("Sportello " + richiesta.getContributoTipoDesc() + " " + sportelli.getSportelloCod());
			metadato.add(metadatosin);
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.PAROLA_CHIAVE_DOSSIER);
			metadatosin.setValore("Sportello " + richiesta.getContributoTipoDesc() + " " + sportelli.getSportelloCod());
			metadato.add(metadatosin);
			// FASCICOLO
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.NUMERO_FASCICOLO);
			metadatosin.setValore(richiesta.getNumero());
			metadato.add(metadatosin);
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.OGGETTO_FASCICOLO);
			if (richiesta.getDestinatario() != null) {
				if (richiesta.getDestinatario().getCf() != null) {
					// mattiamo anche la domanda errore
					metadatosin.setValore(richiesta.getNumero() + " Richiedente "
							+ richiesta.getRichiedente().getCognome() + " " + richiesta.getRichiedente().getNome() + " "
							+ richiesta.getRichiedente().getCf() + " - Destinatario "
							+ richiesta.getDestinatario().getCognome() + " " + richiesta.getDestinatario().getNome()
							+ " " + richiesta.getDestinatario().getCf());
				}
			} else {
				metadatosin.setValore("Richiedente " + richiesta.getRichiedente().getCognome() + " "
						+ richiesta.getRichiedente().getNome() + " " + richiesta.getRichiedente().getCf());
			}
			metadato.add(metadatosin);
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.PAROLA_CHIAVE_FASCICOLO);
			if (richiesta.getDestinatario() != null) {
				if (richiesta.getDestinatario().getCf() != null) {
					// mattiamo anche la domanda errore
					metadatosin.setValore(richiesta.getNumero() + " Richiedente "
							+ richiesta.getRichiedente().getCognome() + " " + richiesta.getRichiedente().getNome() + " "
							+ richiesta.getRichiedente().getCf() + " - Destinatario "
							+ richiesta.getDestinatario().getCognome() + " " + richiesta.getDestinatario().getNome()
							+ " " + richiesta.getDestinatario().getCf());
				}
			} else {
				metadatosin.setValore("Richiedente " + richiesta.getRichiedente().getCognome() + " "
						+ richiesta.getRichiedente().getNome() + " " + richiesta.getRichiedente().getCf());
			}
			metadato.add(metadatosin);
			metadatosin = new MetadatoType();
			// DOCUMENTO
			metadatosin.setNome(Constants.OGGETTO_DOCUMENTO);
			metadatosin.setValore(richiesta.getContributoTipoDesc() + " " + allegato.getCodTipoAllegato() + " di "
					+ richiesta.getRichiedente().getCognome() + " " + richiesta.getRichiedente().getNome());
			metadato.add(metadatosin);
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.PAROLE_CHIAVE_DOCUMENTO);
			metadatosin.setValore(richiesta.getDomandaDetCod() + "_" + richiesta.getContributoTipoDesc() + " "
					+ allegato.getCodTipoAllegato() + " di " + richiesta.getRichiedente().getCognome() + " "
					+ richiesta.getRichiedente().getNome());
			metadato.add(metadatosin);
			// nuovo metadato
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.DESTINATARIO_GIURIDICO_DOCUMENTO);
			metadatosin.setValore(richiesta.getRichiedente().getCognome() + " " + richiesta.getRichiedente().getNome());
			metadato.add(metadatosin);
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.DATA_CRONICA_DOCUMENTO);
			metadatosin.setValore(Util.getDataISO(new Date()));
			metadato.add(metadatosin);
			// RGZ in Partenza
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.DESTINATARIO_PF_COGNOME);
			metadatosin.setValore(richiesta.getRichiedente().getCognome());
			metadato.add(metadatosin);
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.DESTINATARIO_PF_NOME);
			metadatosin.setValore(richiesta.getRichiedente().getNome());
			metadato.add(metadatosin);
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.DESTINATARIO_PF_CODICE_FISCALE);
			metadatosin.setValore(richiesta.getRichiedente().getCf());
			metadato.add(metadatosin);
			metadati.setMetadato(metadato);
			smistadocumento.setMetadati(metadati);
			// verificare se bisogna mettere il numero degli allegati
			smistadocumento.setNumAllegati(0);

			smistaRequest.setDatiSmistaDocumento(smistadocumento);

			// chiamo stardas
			System.out.println("--- Request Stardas ---");
			JAXBContext jaxRes = JAXBContext.newInstance(SmistaDocumentoRequestType.class);
			Marshaller jaxMres = jaxRes.createMarshaller();
			jaxMres.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxMres.marshal(smistaRequest, System.out);
			System.out.println("---------------------------\n");

			// chiamo stardas
			smistaResponse = port.smistaDocumento(smistaRequest);

			System.out.println("--- Response Stardas ---");
			jaxRes = JAXBContext.newInstance(SmistaDocumentoResponseType.class);
			jaxMres = jaxRes.createMarshaller();
			jaxMres.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxMres.marshal(smistaResponse, System.out);
			System.out.println("---------------------------\n");
			// if
			// (smistaResponse.getResult().getCodice().equalsIgnoreCase(Constants.STARDAS_OK)
			// ||
			// smistaResponse.getResult().getCodice().equalsIgnoreCase(Constants.STARDAS_OK_PARZIALE))
			// {
			// esito positivo prendo l'uuid per il prossimo invio
			return smistaResponse;
			// }
		} catch (WebServiceException ws) {
			logError("SmistaDocumentoStardas", "Errore ws ", ws);
		} catch (DatabaseException e) {
			logError("SmistaDocumentoStardas", "Errore db ", e);
		} catch (Exception e) {
			logError("SmistaDocumentoStardas", "Errore generico ", e);
		}
		return null;
	}

	private void setWSSecurity(BindingProvider prov) {
		Map<String, List<String>> requestHeaders = new HashMap<>();
		prov.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, properties.getStardasUrl());
		String token;
		try {
			token = tokenApiManager.refreshToken();
			if (token != null) {
				requestHeaders.put("Authorization", Collections.singletonList(token));
				prov.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, requestHeaders);
			}
		} catch (IOException e) {
			logError("setWSSecurity", "Errore generico ", e);
		}
	}

	private byte[] getAllegato(String filePath, String fileName) {

		String os = System.getProperty("os.name");
		boolean locale = false;
		File file = null;
		byte[] allegatoDecifrato = null;
		try {
			if (os.toLowerCase().contains("win")) {
				locale = true;
			}
			if (!locale)
				file = new File(filePath + "/" + fileName);
			else
				file = new File(filePath + "\\" + fileName);

			byte[] bytes;

			bytes = Files.readAllBytes(file.toPath());

			allegatoDecifrato = filesEncrypt.creaFileDeCifratoToByte(Cipher.DECRYPT_MODE, bytes);
		} catch (IOException e) {
			logError("Decripta docuemnto", "Errore generico ", e);
		}

		return allegatoDecifrato;
	}
}