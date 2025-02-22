/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonores.buonoresbff.business.be.ServizioAttivoApi;
import it.csi.buonores.buonoresbff.business.be.service.ServizioAttivoService;

@Component
public class ServizioAttivoApiServiceImpl implements ServizioAttivoApi {

	@Autowired
	ServizioAttivoService servizioAttivo;

	@Override
	public Response servizioAttivoGet(String xRequestId, String xForwardedFor, String xCodiceServizio,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return servizioAttivo.execute(xRequestId, xForwardedFor, xCodiceServizio, securityContext, httpHeaders,
				httpRequest);
	}

}
