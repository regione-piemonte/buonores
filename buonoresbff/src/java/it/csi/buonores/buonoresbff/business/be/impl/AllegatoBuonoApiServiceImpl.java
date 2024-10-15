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

import it.csi.buonores.buonoresbff.business.be.AllegatoBuonoApi;
import it.csi.buonores.buonoresbff.business.be.service.AllegatoBuonoService;

@Component
public class AllegatoBuonoApiServiceImpl implements AllegatoBuonoApi {

	@Autowired
	AllegatoBuonoService allegatoBuonoService;

	public Response addAllegatoBuono(String tipoAllegato, String numeroRichiesta, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String shibIdentitaCodiceFiscale, String xFilenameOriginale,
			String contentType, byte[] allegato, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return allegatoBuonoService.addAllegatoContratto(tipoAllegato, numeroRichiesta, xRequestId, xForwardedFor,
				xCodiceServizio, shibIdentitaCodiceFiscale, xFilenameOriginale, contentType, allegato, securityContext,
				httpHeaders, httpRequest);
	}

	public Response getAllegatoBuono(Integer idAllegato, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return allegatoBuonoService.getAllegatoBuono(idAllegato, xForwardedFor, shibIdentitaCodiceFiscale,
				securityContext, httpHeaders, httpRequest);
	}
}