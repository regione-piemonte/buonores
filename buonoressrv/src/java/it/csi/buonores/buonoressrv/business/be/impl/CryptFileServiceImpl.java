/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.business.be.impl;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonores.buonoressrv.business.be.CryptFileApi;
import it.csi.buonores.buonoressrv.business.be.service.CryptFileService;
import it.csi.buonores.buonoressrv.util.LoggerUtil;

@Component
public class CryptFileServiceImpl extends LoggerUtil implements CryptFileApi {

	@Autowired
	CryptFileService criptFile;
	
	@Override
	public Response uploadFileDomanda(MultipartFormDataInput input, String numeroRichiesta, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String shibIdentitaCodiceFiscale,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			return criptFile.uploadFileDomanda(input, numeroRichiesta, xRequestId, xForwardedFor, xCodiceServizio,
					shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
		} catch (IOException e) {
			logError("uploadFileDomanda", e.getMessage(),e);
		}
		return null;
	}

}
