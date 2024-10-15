/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.business.be.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.buonores.buonoresbandisrv.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbandisrv.dto.DatiBuono;
import it.csi.buonores.buonoresbandisrv.dto.Errore;
import it.csi.buonores.buonoresbandisrv.dto.ErroreDettaglio;
import it.csi.buonores.buonoresbandisrv.dto.ModelAllegatoContratto;
import it.csi.buonores.buonoresbandisrv.dto.ModelBandiMessage;
import it.csi.buonores.buonoresbandisrv.dto.ModelRichiesta;
import it.csi.buonores.buonoresbandisrv.dto.ModelSetFornitore;
import it.csi.buonores.buonoresbandisrv.exception.DatabaseException;
import it.csi.buonores.buonoresbandisrv.exception.ResponseErrorException;
import it.csi.buonores.buonoresbandisrv.integration.dao.custom.BuoniDao;
import it.csi.buonores.buonoresbandisrv.integration.dao.custom.CodParametroDao;
import it.csi.buonores.buonoresbandisrv.integration.dao.custom.LogBandiDao;
import it.csi.buonores.buonoresbandisrv.integration.dao.custom.RichiesteDao;
import it.csi.buonores.buonoresbandisrv.integration.dao.custom.SportelliDao;
import it.csi.buonores.buonoresbandisrv.integration.service.util.FilesEncrypt;
import it.csi.buonores.buonoresbandisrv.util.Constants;
import it.csi.buonores.buonoresbandisrv.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoresbandisrv.util.rest.ResponseRest;
import it.csi.buonores.buonoresbandisrv.util.validator.impl.ValidateGenericImpl;

@Service
public class FornitoreBandiService extends BaseService {

	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	ValidateGenericImpl validateGeneric;

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

	public Response setFornitoreBandi(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ModelRichiesta richiesta = new ModelRichiesta();
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		String paramErroreServizio = "";
		try {
			List<ErroreDettaglio> listError = validateGeneric.validateDomanda(shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio, numeroRichiesta, securityContext, httpHeaders, httpRequest);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(Errore.generateErrore(HttpStatus.BAD_REQUEST, listError),
						"errore in validate");
			}

			logInfo(metodo, "Numero Domanda: " + numeroRichiesta);
			richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			ResponseRest responserest = new ResponseRest();
			List<ModelBandiMessage> body = new ArrayList<ModelBandiMessage>();
			ObjectMapper mapper = new ObjectMapper();
			if (richiesta != null) {
				// verifico se il buono attivo allora faccio ma se creato o altro no
				String stati = "'" + Constants.ATTIVO + "','" + Constants.RINUNCIATO + "','" + Constants.REVOCATO
						+ "','" + Constants.REVOCATO_PRESA_VISIONE + "'";
				DatiBuono buono = buoniDao.selectBuono(richiesta.getSportelloId(), richiesta.getDomandaId(), stati);
				if (buono != null) {
					// genero il fornitore da comunicare
					List<String> fornitoreJson = generaJson(numeroRichiesta);
					if (fornitoreJson == null || fornitoreJson.size() == 0) {
						String param = "Errore nella generazione del JSON del fornitore ";
						logError(metodo, param);
						generateResponseErrorException(CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR,
								"Errore Crea JSON del fornitore ", param);
					}
					// chiama il servizio post fornitore di bandi per quanti sono i fornitori nella
					// lista
					boolean erroreservizio = false;
					for (String json : fornitoreJson) {
						responserest = new ResponseRest();
						responserest = restbase.postSetFornitore(json, shibIdentitaCodiceFiscale, xRequestId,
								xForwardedFor, xCodiceServizio, securityContext, httpHeaders, httpRequest);
						if (responserest.getStatusCode() != 200 && responserest.getStatusCode() != 201) {
							paramErroreServizio = responserest.getJson();
							logError(metodo, "Errore set fornitore ", paramErroreServizio);
							logBandiDao.insertLogBandi(richiesta.getNumero(), buono.getBuonoId(), null, json, "KO",
									String.valueOf(responserest.getStatusCode()),
									"Errore set fornitore " + paramErroreServizio,
									Constants.SET_FORNITORE);
							erroreservizio = true;
						} else {
							mapper = new ObjectMapper();
							ModelBandiMessage bodysin = new ModelBandiMessage();
							try {
								bodysin = mapper.readValue(responserest.getJson(), ModelBandiMessage.class);
							} catch (IOException e) {
								String param = responserest.getJson();
								logError(metodo, "Errore set fornitore mapper ", param);
								logBandiDao.insertLogBandi(richiesta.getNumero(), buono.getBuonoId(), null, json,
										"KO", String.valueOf(responserest.getStatusCode()),
										"Errore set fornitore mapper " + param, Constants.SET_FORNITORE);
							}

							// scrivo nella tabella di log di bandi
							if (bodysin.getEsitoServizio().equalsIgnoreCase(Constants.BANDI_OK)) {
								logBandiDao.insertLogBandi(richiesta.getNumero(), buono.getBuonoId(),
										bodysin.getUuid(), json, bodysin.getEsitoServizio(), null, null,
										Constants.SET_FORNITORE);
							} else {
								logBandiDao.insertLogBandi(richiesta.getNumero(), buono.getBuonoId(), null, json,
										bodysin.getEsitoServizio(), bodysin.getCodiceErrore(), bodysin.getMessaggio(),
										Constants.SET_FORNITORE);
							}
							body.add(bodysin);
						}
					}
					if (erroreservizio) {
						String param = responserest.getJson();
						logError(metodo, "Errore set fornitore almeno su una chiamata post ", paramErroreServizio,
								param);
						generateResponseErrorException(CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR,
								"Errore set fornitore almeno su una chiamata post ", param);
					}
				} else {
					// errore non esiste il buonoid
					String param = "Errore non trovato o buono non in stato attivo per domanda " + numeroRichiesta;
					logError(metodo, "Errore Crea xml bandi ", param);
					generateResponseErrorException(CodeErrorEnum.ERR03, HttpStatus.NOT_FOUND,
							param);
				}
			} else {
				String param = "Errore set fornitore Bandi domanda " + numeroRichiesta + "non trovata";
				logError(metodo, param);
				generateResponseErrorException(CodeErrorEnum.ERR03, HttpStatus.NOT_FOUND,
						"errore set fornitore bandi domanda non trovata");
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

	public List<String> generaJson(String numeroDomanda) throws DatabaseException, Exception {

		List<Integer> fornitori = buoniDao.selectFornitori(numeroDomanda);
		ModelSetFornitore fornitore = new ModelSetFornitore();
		List<String> jsonFornitori = new ArrayList<String>();
		if (fornitori != null && fornitori.size() > 0) {
			for (Integer forn : fornitori) {
				// prendo il contratto per fornitore
				List<Integer> contratti = new ArrayList<Integer>();
				contratti = buoniDao.selectContratti(forn);
				for (Integer contratto : contratti) {
					fornitore = buoniDao.selectSetFornitore(contratto, forn);
					if (fornitore != null) {
						fornitore.setNumeroDomanda(numeroDomanda);
						logInfo("generaJson Fornitore: ", fornitore.toStringLog());
						if (fornitore.getFiles() != null && fornitore.getFiles().size() > 0) {
							fornitore.setFiles(generaFile(fornitore.getFiles()));
						}
						ObjectMapper mapper = new ObjectMapper();
						mapper.setSerializationInclusion(Include.NON_EMPTY);
						jsonFornitori.add(mapper.writeValueAsString(fornitore));
					}
				}
			}
		}

		return jsonFornitori;
	}

	private List<ModelAllegatoContratto> generaFile(List<ModelAllegatoContratto> allegati) throws IOException {

		File file = null;
		for (int i = 0; i < allegati.size(); i++) {
			file = new File(allegati.get(i).getPath());
			byte[] bytes = Files.readAllBytes(file.toPath());
			byte[] allegatoDecifrato = filesEncrypt.creaFileDeCifratoByte(Cipher.DECRYPT_MODE, bytes);
			allegati.get(i).setFile(allegatoDecifrato);
			allegati.get(i).setPath(null);
		}
		return allegati;
	}

}
