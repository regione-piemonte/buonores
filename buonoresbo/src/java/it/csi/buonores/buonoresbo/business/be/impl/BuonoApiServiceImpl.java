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

import it.csi.buonores.buonoresbo.business.be.BuonoApi;
import it.csi.buonores.buonoresbo.business.be.service.BuonoService;
import it.csi.buonores.buonoresbo.dto.ModelFiltriRicercaBuoni;

@Component
public class BuonoApiServiceImpl implements BuonoApi {

	@Autowired
	BuonoService buonoService;

	@Override
	public Response ricercaBuoni(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest, ModelFiltriRicercaBuoni filtri) {
		return buonoService.ricercaBuoni(securityContext, httpHeaders, httpRequest, filtri);
	}

	@Override
	public Response getStatiBuono(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return buonoService.getStatiBuono(securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response getAllegatoBuono(Integer idAllegato, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return buonoService.getAllegatoBuono(idAllegato, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response getRendicontazioneBuono(Integer idAllegato, SecurityContext securityContext,
			HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return buonoService.getRendicontazioneBuono(idAllegato, securityContext, httpHeaders, httpRequest);
	}

}
