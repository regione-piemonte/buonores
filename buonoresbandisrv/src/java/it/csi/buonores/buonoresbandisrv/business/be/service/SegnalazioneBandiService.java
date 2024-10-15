/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.business.be.service;

import java.io.IOException;
import java.util.List;

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
import it.csi.buonores.buonoresbandisrv.dto.ModelBandiMessage;
import it.csi.buonores.buonoresbandisrv.dto.ModelBandiMessageSegnalazione;
import it.csi.buonores.buonoresbandisrv.dto.ModelRicezioneSegnalazione;
import it.csi.buonores.buonoresbandisrv.dto.ModelRichiesta;
import it.csi.buonores.buonoresbandisrv.exception.DatabaseException;
import it.csi.buonores.buonoresbandisrv.exception.ResponseErrorException;
import it.csi.buonores.buonoresbandisrv.integration.dao.custom.BuoniDao;
import it.csi.buonores.buonoresbandisrv.integration.dao.custom.CodParametroDao;
import it.csi.buonores.buonoresbandisrv.integration.dao.custom.RichiesteDao;
import it.csi.buonores.buonoresbandisrv.integration.dao.custom.SportelliDao;
import it.csi.buonores.buonoresbandisrv.util.Constants;
import it.csi.buonores.buonoresbandisrv.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoresbandisrv.util.rest.ResponseRest;
import it.csi.buonores.buonoresbandisrv.util.validator.impl.ValidateGenericImpl;

@Service
public class SegnalazioneBandiService extends BaseService {

	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	BuoniDao buoniDao;

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	CodParametroDao parametroDao;

	@Autowired
	SportelliDao sportelliDao;

	@Autowired
	ServizioRestService restbase;

	public Response setRicezioneSegnalazioni(ModelRicezioneSegnalazione segnalazione, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String shibIdentitaCodiceFiscale,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ModelRichiesta richiesta = new ModelRichiesta();
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		ModelBandiMessage bodysin = new ModelBandiMessage();
		ModelBandiMessageSegnalazione bodysinsegn = new ModelBandiMessageSegnalazione();
		try {
			List<ErroreDettaglio> listError = validateGeneric.validateSegnalazioni(shibIdentitaCodiceFiscale,
					xRequestId, xForwardedFor, xCodiceServizio, segnalazione, securityContext, httpHeaders,
					httpRequest);

			generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "errore in validate");

			richiesta = richiesteDao.selectNumeroRichiesta(segnalazione.getNumeroDomanda());
			ResponseRest responserest = new ResponseRest();
			if (richiesta != null) {
				String stati = "'" + Constants.ATTIVO + "','" + Constants.RINUNCIATO + "'";
				DatiBuono buonoId = buoniDao.selectBuono(richiesta.getSportelloId(), richiesta.getDomandaId(), stati);
				if (buonoId != null) {
					responserest = new ResponseRest();
					ObjectMapper mapper = new ObjectMapper();
					mapper.setSerializationInclusion(Include.NON_EMPTY);
					String jsonSegnalazione = mapper.writeValueAsString(segnalazione);
					responserest = restbase.postRicezioneSegnalazione(jsonSegnalazione, shibIdentitaCodiceFiscale,
							xRequestId, xForwardedFor, xCodiceServizio, securityContext, httpHeaders, httpRequest);
					if (responserest.getStatusCode() == 400) {
						logError(metodo, "Errore set ricezione Segnalazione ", responserest.getJson());
						generateResponseErrorException(CodeErrorEnum.ERR17, HttpStatus.BAD_REQUEST,
								"bad request for set ricezione Segnalazione", responserest.getJson());
					} else if (responserest.getStatusCode() != 200 && responserest.getStatusCode() != 201) {
						logError(metodo, "Errore set ricezione Segnalazione ", responserest.getJson());
						generateResponseErrorException(CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR,
								"error set ricezione Segnalazione", responserest.getJson());
					} else {
						mapper = new ObjectMapper();
						try {
							if (responserest.getJson().contains("messaggioErrore")) {
								bodysinsegn = mapper.readValue(responserest.getJson(),
										ModelBandiMessageSegnalazione.class);
							} else {
								bodysin = mapper.readValue(responserest.getJson(), ModelBandiMessage.class);
							}

							if (bodysinsegn.getEsitoServizio() != null
									&& bodysinsegn.getEsitoServizio().equalsIgnoreCase("KO")) {
								logError(metodo,
										"Errore set ricezione Segnalazione con esito servizio KO per segnalazione ",
										bodysinsegn.toString());
								generateResponseErrorException(CodeErrorEnum.ERR17, HttpStatus.BAD_REQUEST,
										"set ricezione Segnalazione con esito servizio KO per segnalazione",
										responserest.getJson());
							} else if (bodysin.getEsitoServizio() != null
									&& bodysin.getEsitoServizio().equalsIgnoreCase("KO")) {
								logError(metodo, "Errore set ricezione Segnalazione con esito servizio KO",
										bodysin.toString());
								generateResponseErrorException(CodeErrorEnum.ERR17, HttpStatus.BAD_REQUEST,
										"set ricezione Segnalazione con esito servizio KO", responserest.getJson());
							}
						} catch (IOException e) {
							logError(metodo, "Errore set ricezione Segnalazione ", responserest.getJson() + " - " + e);
						}
					}
				} else {
					logError(metodo,
							"Errore set ricezione Segnalazione Bandi - buono non trovato o buono non in stato attivo");
					// generateResponseErrorException(CodeErrorEnum.ERR03, HttpStatus.NOT_FOUND,
					// "set ricezione Segnalazione Bandi buono non trovato o buono non in stato
					// attivo",
					// "Errore set ricezione Segnalazione Bandi buono non trovato o buono non in
					// stato attivo per domanda " + richiesta.getNumero());
					// TODO DA CAPIRE
					// simulo esito positivo
					bodysin.setEsitoServizio("OK");
				}
			} else {
				logError(metodo, "Errore set ricezione Segnalazione Bandi domanda non trovata");
				generateResponseErrorException(CodeErrorEnum.ERR03, HttpStatus.NOT_FOUND,
						"errore set ricezione Segnalazione bandi domanda non trovata");
			}

			return Response.ok().entity(bodysin).build();

		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}
}
