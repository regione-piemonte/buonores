/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonorescallban.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonores.buonorescallban.business.be.ServizioAttivoApi;
import it.csi.buonores.buonorescallban.business.be.service.base.ServizioAttivoService;

@Component
public class ServizioAttivoServiceImpl implements ServizioAttivoApi {
	
	@Autowired
	ServizioAttivoService servizioAttivo;
	
	@Override
	public Response servizioAttivoGet(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return servizioAttivo.execute(securityContext, httpHeaders, httpRequest);
	}

}
