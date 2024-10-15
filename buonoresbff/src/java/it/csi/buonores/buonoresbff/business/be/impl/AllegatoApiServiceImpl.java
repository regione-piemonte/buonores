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

import it.csi.buonores.buonoresbff.business.be.AllegatoApi;
import it.csi.buonores.buonoresbff.business.be.service.AllegatiService;

@Component
public class AllegatoApiServiceImpl implements AllegatoApi {

	@Autowired
	AllegatiService allegatiService;

	@Override
	public Response allegatoNumeroRichiestaTipoAllegatoGet(String tipoAllegato, String numeroRichiesta,
			String xForwardedFor, String shibIdentitaCodiceFiscale,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return allegatiService.getAllegato(tipoAllegato, numeroRichiesta, xForwardedFor, shibIdentitaCodiceFiscale,
				securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response allegatoNumeroRichiestaTipoAllegatoPost(String tipoAllegato, String numeroRichiesta,
			String xRequestId, String xForwardedFor, String xCodiceServizio, String shibIdentitaCodiceFiscale,
			String xFilenameOriginale,
			String contentType, byte[] allegato, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return allegatiService.execute(tipoAllegato, numeroRichiesta, xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, xFilenameOriginale, contentType, allegato, securityContext, httpHeaders,
				httpRequest);
	}
}
