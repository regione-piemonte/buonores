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

import it.csi.buonores.buonoresrend.business.be.DomandeApi;
import it.csi.buonores.buonoresrend.business.be.service.GetDomandeService;

@Component
public class DomandeApiImpl implements DomandeApi {
	
	@Autowired
	GetDomandeService domandeService;

	@Override
	public Response domandaByNumeroDomandaGet(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return domandeService.domandaByNumeroDomandaGet(numeroRichiesta, xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
	}

}
