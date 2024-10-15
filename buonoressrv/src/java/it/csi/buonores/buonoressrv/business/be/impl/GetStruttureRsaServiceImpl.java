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

import it.csi.buonores.buonoressrv.business.be.GetStruttureRsaApi;
import it.csi.buonores.buonoressrv.integration.gestruttureres.GetStruttureRsaService;

@Component
public class GetStruttureRsaServiceImpl implements GetStruttureRsaApi {

	@Autowired
	GetStruttureRsaService getStruttureRsaService;

	@Override
	public Response getStruttureRsaApi(String denominazione, String comune, String indirizzo, String orderby,
			Integer rowPerPage, Integer pageNumber, Boolean descending, String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return getStruttureRsaService.execute( denominazione,  comune,  indirizzo,  orderby,
			 rowPerPage,  pageNumber,  descending,  shibIdentitaCodiceFiscale,
			 xRequestId,  xForwardedFor,  xCodiceServizio,  securityContext);
	}

	@Override
	public Response getComuniRsaApi(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return getStruttureRsaService.getComuni( shibIdentitaCodiceFiscale,
				 xRequestId,  xForwardedFor,  xCodiceServizio,  securityContext);
	}

	@Override
	public Response getRendicontazioneByNumeroDomanda(String buonoCod, String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return getStruttureRsaService.getRendicontazioniByNumeroDomanda(  buonoCod,  shibIdentitaCodiceFiscale,
				 xRequestId,  xForwardedFor,  xCodiceServizio,  securityContext,
				   httpRequest);
	}

	@Override
	public Response getDocumentiRendicontazione(String idDocumento, String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return getStruttureRsaService.getDocumentiRendicontazione(  idDocumento,  shibIdentitaCodiceFiscale,
				 xRequestId,  xForwardedFor,  xCodiceServizio,  securityContext,
				   httpRequest);
	}

	@Override
	public Response getIntegrazioni(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return getStruttureRsaService.getIntegrazioni( numeroRichiesta,  xRequestId,  xForwardedFor,
				 xCodiceServizio,  shibIdentitaCodiceFiscale,  securityContext,
				 httpHeaders,  httpRequest);
	}

}
