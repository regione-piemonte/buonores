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

import it.csi.buonores.buonoresbff.business.be.ContrattiApi;
import it.csi.buonores.buonoresbff.business.be.service.ContrattiService;
import it.csi.buonores.buonoresbff.dto.ModelContratto;
import it.csi.buonores.buonoresbff.dto.ModelContrattoAllegati;

@Component
public class ContrattiApiServiceImpl implements ContrattiApi {

	@Autowired
	ContrattiService contrattiService;

	@Override
	public Response addContratto(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, ModelContrattoAllegati contrattoAllegati,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return contrattiService.addContratto(numeroRichiesta, xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, contrattoAllegati, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response getContratti(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return contrattiService.getContratti(numeroRichiesta, xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response putContratto(String numeroRichiesta, Integer idContratto, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, ModelContratto contratto,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return contrattiService.putContratto(numeroRichiesta, idContratto, xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, contratto, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response deleteContratto(String numeroRichiesta, Integer idContratto, String xRequestId,
			String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return contrattiService.deleteContratto(numeroRichiesta, idContratto, xRequestId, xForwardedFor,
				xCodiceServizio,
				shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
	}

}
