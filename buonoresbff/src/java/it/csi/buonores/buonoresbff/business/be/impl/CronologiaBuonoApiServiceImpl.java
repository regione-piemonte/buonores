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

import it.csi.buonores.buonoresbff.business.be.CronologiaBuonoApi;
import it.csi.buonores.buonoresbff.business.be.service.CronologiaBuonoService;
import it.csi.buonores.buonoresbff.dto.ModelStatoBuono;

@Component
public class CronologiaBuonoApiServiceImpl implements CronologiaBuonoApi {

	@Autowired
	CronologiaBuonoService cronologiaBuono;

	public Response getCronologiaBuono(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return cronologiaBuono.getCronologiaRichieste(numeroRichiesta, xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
	}

	public Response postCronologiaBuono(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, ModelStatoBuono stato,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return cronologiaBuono.postCronologiaRichieste(numeroRichiesta, xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, stato, securityContext, httpHeaders, httpRequest);
	}
}
