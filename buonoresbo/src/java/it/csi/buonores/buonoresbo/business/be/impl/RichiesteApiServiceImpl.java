/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.business.be.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonores.buonoresbo.business.be.RichiesteApi;
import it.csi.buonores.buonoresbo.business.be.service.GetRichiesteService;
import it.csi.buonores.buonoresbo.dto.CambioStatoPopUp;
import it.csi.buonores.buonoresbo.dto.ModelIsee;
import it.csi.buonores.buonoresbo.dto.ModelPresaInCarico;

@Component
public class RichiesteApiServiceImpl implements RichiesteApi {

	@Autowired
	GetRichiesteService richiesteService;

	@Override
	public Response richiesteNumeroRichiestaGet(String numeroRichiesta, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return richiesteService.getNumeroRichiesta(numeroRichiesta, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response presaInCaricoRichiesta(List<ModelPresaInCarico> richieste, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
		return richiesteService.presaInCaricoRichiesta(richieste, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response listaAllegatoRichiesta(String numeroRichiesta, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return richiesteService.getListaAllegatoRichiesta(numeroRichiesta, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response listaCampiRichiesta(String numeroRichiesta, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return richiesteService.getListaCampoRichiesta(numeroRichiesta, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response datiDaModificarePost(CambioStatoPopUp datiDaModificare, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
		return richiesteService.datiDaModificarePost(datiDaModificare, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response cronologiaRichiesta(String numeroRichiesta, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return richiesteService.cronologiaRichiesta(numeroRichiesta, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response verifiche(String numeroRichiesta, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return richiesteService.verificheDomanda(numeroRichiesta, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response ammissibileRichiesta(CambioStatoPopUp datiPopUp, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return richiesteService.ammissibileRichiesta(datiPopUp, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response preavvisoPerNonAmmissibilitaRichiesta(CambioStatoPopUp datiPopUp, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return richiesteService.preavvisoPerNonAmmissibilitaRichiesta(datiPopUp, securityContext, httpHeaders,
				httpRequest);
	}

	@Override
	public Response ammessaConRiservaRichiesta(CambioStatoPopUp datiPopUp, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return richiesteService.ammessaConRiservaRichiesta(datiPopUp, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response diniegoRichiesta(CambioStatoPopUp datiPopUp, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return richiesteService.diniegoRichiesta(datiPopUp, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response verificaContatto(String numeroRichiesta, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return richiesteService.verificaContatto(numeroRichiesta, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response ammessaRichiesta(CambioStatoPopUp datiPopUp, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return richiesteService.ammessaRichiesta(datiPopUp, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response inPagamentoRichiesta(CambioStatoPopUp datiPopUp, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return richiesteService.inPagamentoRichiesta(datiPopUp, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response ammessaConRiservaInPagamentoRichiesta(CambioStatoPopUp datiPopUp, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return richiesteService.ammessaConRiservaInPagamentoRichiesta(datiPopUp, securityContext, httpHeaders,
				httpRequest);
	}

	@Override
	public Response salvaDomandaIsee(String numeroRichiesta, ModelIsee isee, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return richiesteService.salvaDomandaIsee(numeroRichiesta, isee, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response salvaDomandaNota(CambioStatoPopUp nota, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return richiesteService.salvaDomandaNota(nota, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response nonammissibileRichiesta(CambioStatoPopUp datiPopUp, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return richiesteService.nonammissibileRichiesta(datiPopUp, securityContext, httpHeaders, httpRequest);
	}
}
