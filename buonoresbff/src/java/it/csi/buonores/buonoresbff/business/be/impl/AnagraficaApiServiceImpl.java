/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonores.buonoresbff.business.be.AnagraficaApi;
import it.csi.buonores.buonoresbff.business.be.service.AnagraficaService;

@Component
public class AnagraficaApiServiceImpl implements AnagraficaApi {

	@Autowired
	AnagraficaService anagraficaService;

	@Override
	public Response anagraficaCfGet(String cf, String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {

		return anagraficaService.anagraficaCfGet(xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale,
				securityContext, httpHeaders, httpRequest, cf);
	}
}
