/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonores.buonoressrv.business.be.SmistaDocumentoApi;
import it.csi.buonores.buonoressrv.business.be.service.SmistaDocumentoService;

@Component
public class SmistaDocumentoServiceImpl implements SmistaDocumentoApi {

	@Autowired
	SmistaDocumentoService smistadocumento;

	@Override
	public Response smistaDocumento(String numerorichiesta, String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor,
			String xCodiceServizio, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return smistadocumento.execute(numerorichiesta, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
				xCodiceServizio, securityContext, httpHeaders, httpRequest);

	}

}
