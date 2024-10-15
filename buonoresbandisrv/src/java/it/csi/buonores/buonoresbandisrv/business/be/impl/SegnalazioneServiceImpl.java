/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonores.buonoresbandisrv.business.be.SegnalazioneBandiApi;
import it.csi.buonores.buonoresbandisrv.business.be.service.SegnalazioneBandiService;
import it.csi.buonores.buonoresbandisrv.dto.ModelRicezioneSegnalazione;

@Component
public class SegnalazioneServiceImpl implements SegnalazioneBandiApi {

	@Autowired
	SegnalazioneBandiService segnalazioneBandiService;

	@Override
	public Response setRicezioneSegnalazioni(ModelRicezioneSegnalazione segnalazione, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String shibIdentitaCodiceFiscale,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return segnalazioneBandiService.setRicezioneSegnalazioni(segnalazione, xRequestId, xForwardedFor,
				xCodiceServizio,
				shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
	}
}
