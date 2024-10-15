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

import it.csi.buonores.buonoresbo.business.be.RicercaApi;
import it.csi.buonores.buonoresbo.business.be.service.RicercaService;
import it.csi.buonores.buonoresbo.dto.ModelFiltriDomandeAperte;

@Component
public class RicercaServiceImpl implements RicercaApi {

	@Autowired
	RicercaService ricercaService;

	@Override
	public Response getStati(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest,
			String tipoMenu) {
		return ricercaService.getStati(securityContext, httpHeaders, httpRequest, tipoMenu);
	}

	@Override
	public Response getSportelli(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return ricercaService.getSportelli(securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response ricerca(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest,
			ModelFiltriDomandeAperte filtri) {
		return ricercaService.ricercaDomandeAperte(securityContext, httpHeaders, httpRequest, filtri);
	}
}
