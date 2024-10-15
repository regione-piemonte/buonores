/**********************************************
 * CSI PIEMONTE 
 **********************************************/
/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresrend.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/buoni")

public interface BuoniApi {

    @GET
    @Produces({ "application/json" })
    public Response getBuoni(@HeaderParam("X-Request-Id") String xRequestId,
            @HeaderParam("X-Forwarded-For") String xForwardedFor,
            @HeaderParam("X-Codice-Servizio") String xCodiceServizio,
            @HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale,
            @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
            @Context HttpServletRequest httpRequest);
}
