/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonores.buonoresbff.business.be.DecodificheApi;
import it.csi.buonores.buonoresbff.business.be.service.DecodificaService;

@Component
public class DecodificheApiServiceImpl implements DecodificheApi {

	@Autowired
	DecodificaService decodificaService;

	@Override
	public Response decodificheAslGet(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		return decodificaService.decodificheAslGet(xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response decodificheStatiDomandaGet(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		return decodificaService.decodificheStatiDomandaGet(xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response decodificheTipiAllegatoGet(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {

		return decodificaService.decodificheTipiAllegatoGet(xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response decodificheTipiContrattoGet(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {

		return decodificaService.decodificheTipiContrattoGet(xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response decodificheTipiRapportoGet(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {

		return decodificaService.decodificheTipiRapportoGet(xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response decodificheTitoliStudioGet(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {

		return decodificaService.decodificheTitoliStudioGet(xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response decodificheValutazioniMultidimensionaliGet(String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {

		return decodificaService.decodificheValutazioniMultidimensionaliGet(xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response decodificheStruttureGet(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, @NotNull String comune, String denominazione, String indirizzo,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return decodificaService.decodificheStruttureGet(xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest, comune, denominazione, indirizzo);
	}

	@Override
	public Response decodificheComuniGet(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return decodificaService.decodificheComuniGet(xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response decodificheTipiAllegatoBuonoGet(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		return decodificaService.decodificheTipiAllegatoBuonoGet(xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
	}
}
