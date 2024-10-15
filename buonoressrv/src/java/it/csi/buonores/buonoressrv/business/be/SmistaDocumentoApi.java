/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/smistadocumento")

public interface SmistaDocumentoApi {

    @GET
    @Path("/{numero_richiesta}")
    @Produces({ "application/json" })
    public Response smistaDocumento(@PathParam("numero_richiesta") String numeroRichiesta,
            @HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale,
            @HeaderParam("X-Request-Id") String xRequestId, @HeaderParam("X-Forwarded-For") String xForwardedFor,
            @HeaderParam("X-Codice-Servizio") String xCodiceServizio, @Context SecurityContext securityContext,
            @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}
