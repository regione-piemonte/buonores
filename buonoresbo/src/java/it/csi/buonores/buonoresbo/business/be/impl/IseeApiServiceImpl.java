/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonores.buonoresbo.business.be.IseeApi;
import it.csi.buonores.buonoresbo.business.be.service.IseeService;
import it.csi.buonores.buonoresbo.dto.ModelIseeBuono;

@Component
public class IseeApiServiceImpl implements IseeApi {

	@Autowired
	IseeService iseeService;

	public Response addConformitaIsee(String numeroRichiesta, ModelIseeBuono isee, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
		return iseeService.addConformitaIsee(numeroRichiesta, isee, securityContext, httpHeaders, httpRequest);
	}

	public Response getConformitaIsee(String numeroRichiesta, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		return iseeService.getConformitaIsee(numeroRichiesta, securityContext, httpHeaders, httpRequest);
	}

	public Response getListaConformitaIsee(String numeroRichiesta, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
		return iseeService.getListaConformitaIsee(numeroRichiesta, securityContext, httpHeaders, httpRequest);
	}

	public Response getListaEsitiIsee(SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		return iseeService.getListaEsitiIsee(securityContext, httpHeaders, httpRequest);
	}
}
