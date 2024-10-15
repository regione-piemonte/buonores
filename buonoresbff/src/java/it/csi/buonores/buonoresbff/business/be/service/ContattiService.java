/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.business.be.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.buonores.buonoresbff.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbff.dto.Errore;
import it.csi.buonores.buonoresbff.dto.ErroreDettaglio;
import it.csi.buonores.buonoresbff.dto.custom.Preferences;
import it.csi.buonores.buonoresbff.exception.DatabaseException;
import it.csi.buonores.buonoresbff.exception.ResponseErrorException;
import it.csi.buonores.buonoresbff.util.Util;
import it.csi.buonores.buonoresbff.util.rest.ResponseRest;

@Service
public class ContattiService extends BaseService {

	@Autowired
	ServizioRestService restbase;

	public Response execute(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		Errore error = null;
		try {
			checkValidate(xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale,
					securityContext, httpHeaders, httpRequest);
			ResponseRest response = restbase.getContatti(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
					xCodiceServizio, securityContext,
					httpHeaders, httpRequest);
			if (response == null || response.getStatusCode() != HttpStatus.OK.value()) {
				// errore
				error = createGenericServerError(metodo, "get contatti");
			} else {
				return Response.ok().entity(response.getJson()).build();
			}
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}
		return error.generateResponseError();
	}

	public Response executePreferenze(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		Errore error = null;
		Preferences preferenze = new Preferences();
		List<String> channels = new ArrayList<String>();
		try {
			checkValidate(xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale,
					securityContext, httpHeaders, httpRequest);

			ResponseRest response = restbase.getPreferenze(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
					xCodiceServizio, securityContext, httpHeaders, httpRequest);
			if (response == null || response.getStatusCode() != HttpStatus.OK.value()) {
				// errore
				error = createGenericServerError(metodo, "get preferenze");
			} else {
				String resp = response.getJson();
				preferenze = new ObjectMapper().readValue(resp, Preferences.class);

				if (Util.isValorizzato(preferenze.getChannels())) {
					for (String canale : preferenze.getChannels().split(",")) {
						channels.add(canale);
					}
				}
				return Response.ok().entity(channels).build();
			}
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	private void checkValidate(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws DatabaseException, ResponseErrorException {
		// validate
		List<ErroreDettaglio> listError = validateGeneric.validateGeneric(shibIdentitaCodiceFiscale,
				xRequestId, xForwardedFor, xCodiceServizio, securityContext, httpHeaders,
				httpRequest);
		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "errore in validate");
	}
}
