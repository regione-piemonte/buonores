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

import it.csi.buonores.buonoresbff.business.be.SportelliApi;
import it.csi.buonores.buonoresbff.business.be.service.SportelliService;

@Component
public class SportelliApiServiceImpl implements SportelliApi {

	@Autowired
	SportelliService sportelli;

	@Override
	public Response sportelliGet(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return sportelli.getSportelli(xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale,
				securityContext, httpHeaders, httpRequest);
	}

}
