/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonores.buonoresbff.business.be.CronologiaApi;
import it.csi.buonores.buonoresbff.business.be.service.CronologiaService;

@Component
public class CronologiaApiServiceImpl implements CronologiaApi {

  @Autowired
  CronologiaService cronologia;

  @Override
  public Response cronologiaNumeroRichiestaGet(String numeroRichiesta, String xRequestId, String xForwardedFor,
      String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
      HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
    return cronologia.getCronologiaRichieste(numeroRichiesta, xRequestId, xForwardedFor, xCodiceServizio,
        shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
  }

  @Override
  public Response cronologiaNumeroRichiestaPost(String numeroRichiesta, String xRequestId, String xForwardedFor,
      String xCodiceServizio, String shibIdentitaCodiceFiscale, String stato, SecurityContext securityContext,
      HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
    // do some magic!
    return cronologia.postCronologiaRichieste(numeroRichiesta, xRequestId, xForwardedFor, xCodiceServizio,
        shibIdentitaCodiceFiscale, stato, securityContext, httpHeaders, httpRequest);
  }
}
