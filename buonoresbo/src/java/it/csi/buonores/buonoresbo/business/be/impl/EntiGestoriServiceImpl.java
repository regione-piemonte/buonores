/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonores.buonoresbo.business.be.EntiGestoriApi;
import it.csi.buonores.buonoresbo.business.be.service.EntiGestoriService;
import it.csi.buonores.buonoresbo.dto.CambioStatoPopUp;
import it.csi.buonores.buonoresbo.dto.ModelVerificheEnte;

@Component
public class EntiGestoriServiceImpl implements EntiGestoriApi {

	@Autowired
	EntiGestoriService entigestoriService;

	@Override
	public Response richiediRettifica(CambioStatoPopUp nota, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return entigestoriService.richiediRettifica(nota, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response getEntiGestori(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return entigestoriService.getEntiGestori(securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response updateToVerificaInCorso(String numeroDomande[], SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return entigestoriService.updateToVerificaInCorso(numeroDomande, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response getVerificheEnteGestore(String numeroDomanda, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return entigestoriService.selectVerificheEnteGestore(numeroDomanda, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response salvaVerificaEnteNew(String numeroDomanda, ModelVerificheEnte verificheEnte,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return entigestoriService.salvaVerificaEnteNew(numeroDomanda, verificheEnte, securityContext, httpHeaders,
				httpRequest);
	}

	@Override
	public Response salvaConcludiVerificaEnte(String numeroDomanda, ModelVerificheEnte verificheEnte,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return entigestoriService.salvaConcludiVerificaEnte(numeroDomanda, verificheEnte, securityContext, httpHeaders,
				httpRequest);
	}
}
