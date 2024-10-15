/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonores.buonoresbo.business.be.BuonoDettaglioApi;
import it.csi.buonores.buonoresbo.business.be.service.BuonoDettaglioService;
import it.csi.buonores.buonoresbo.dto.ModelDecorrenzaBuono;
import it.csi.buonores.buonoresbo.dto.ModelFiltroDateIntegrazione;

@Component
public class BuonoDettaglioServiceImpl implements BuonoDettaglioApi {

	@Autowired
	BuonoDettaglioService buonoDettaglioService;

	@Override
	public Response getAllegatiBuono(String numeroRichiesta, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return buonoDettaglioService.getAllegatiBuono(numeroRichiesta, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response getContrattiBuono(String numeroRichiesta, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return buonoDettaglioService.getContrattiBuono(numeroRichiesta, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response getDecorrenzaBuono(String numeroRichiesta, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return buonoDettaglioService.getDecorrenzaBuono(numeroRichiesta, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response updateDecorrenzaBuono(String numeroRichiesta, ModelDecorrenzaBuono decorrenza,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return buonoDettaglioService.updateDecorrenzaBuono(numeroRichiesta, decorrenza, securityContext, httpHeaders,
				httpRequest);
	}

	@Override
	public Response getIntegrazioni(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest, ModelFiltroDateIntegrazione date) {
		return buonoDettaglioService.getIntegrazione(numeroRichiesta, xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest, date);
	}

	@Override
	public Response getBuonoInformazioni(String numeroRichiesta, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return buonoDettaglioService.getBuonoInformazioni(numeroRichiesta, numeroRichiesta, numeroRichiesta,
				numeroRichiesta, numeroRichiesta, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response getVerificaInformazioni(String numeroRichiesta, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return buonoDettaglioService.getVerificaInformazioni(numeroRichiesta, numeroRichiesta, numeroRichiesta,
				numeroRichiesta, numeroRichiesta, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response getStoricoRendicontazioni(String numeroBuono, String numeroRichiesta,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return buonoDettaglioService.getStoricoRendicontazioni(numeroBuono, numeroRichiesta, numeroRichiesta,
				numeroRichiesta, numeroBuono, numeroRichiesta, securityContext, httpHeaders, httpRequest);
	}
}