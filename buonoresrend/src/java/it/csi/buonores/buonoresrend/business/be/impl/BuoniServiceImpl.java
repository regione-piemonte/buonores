/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresrend.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonores.buonoresrend.business.be.BuoniApi;
import it.csi.buonores.buonoresrend.business.be.service.BuoniService;

@Component
public class BuoniServiceImpl implements BuoniApi {

	@Autowired
	BuoniService buoni;

	@Override
	public Response getBuoni(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return buoni.getBuoni(xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
	}

	

}
