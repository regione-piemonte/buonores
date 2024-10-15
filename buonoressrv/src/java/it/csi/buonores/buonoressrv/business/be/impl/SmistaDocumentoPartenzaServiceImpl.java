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

import it.csi.buonores.buonoressrv.business.be.SmistaDocumentoPartenzaApi;
import it.csi.buonores.buonoressrv.business.be.service.SmistaDocumentoPartenzaService;


@Component
public class SmistaDocumentoPartenzaServiceImpl implements SmistaDocumentoPartenzaApi {
	
	@Autowired
	SmistaDocumentoPartenzaService smistadocumentoPartenza;

		
	@Override
	public Response smistaDocumentoPartenza(String numerorichiesta,String tipoLettera,String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return smistadocumentoPartenza.execute(numerorichiesta,tipoLettera,shibIdentitaCodiceFiscale,xRequestId,  xForwardedFor, xCodiceServizio,securityContext, httpHeaders, httpRequest);
	
	}

}
