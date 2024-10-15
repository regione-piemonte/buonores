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

import it.csi.buonores.buonoresbff.business.be.IseeApi;
import it.csi.buonores.buonoresbff.business.be.service.IseeService;
import it.csi.buonores.buonoresbff.dto.ModelIsee;

@Component
public class IseeApiServiceImpl implements IseeApi {

	@Autowired
	IseeService iseeService;

	public Response addConformitaIsee(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, ModelIsee isee, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
		return iseeService.addConformitaIsee(numeroRichiesta, xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, isee, securityContext, httpHeaders, httpRequest);
	}

	public Response getConformitaIsee(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
		return iseeService.getConformitaIsee(numeroRichiesta, xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
	}
}
