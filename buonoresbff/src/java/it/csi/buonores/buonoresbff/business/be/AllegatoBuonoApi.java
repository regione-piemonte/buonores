/**********************************************
 * CSI PIEMONTE 
 **********************************************/
/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/allegato-buono")
public interface AllegatoBuonoApi {

	@POST
	@Path("/{numero_richiesta}/{tipo_allegato}")
	@Consumes({ "application/pdf", "image/jpeg", "image/png" })
	@Produces({ "application/json" })
	public Response addAllegatoBuono(@PathParam("tipo_allegato") String tipoAllegato,
			@PathParam("numero_richiesta") String numeroRichiesta, @HeaderParam("X-Request-Id") String xRequestId,
			@HeaderParam("X-Forwarded-For") String xForwardedFor,
			@HeaderParam("X-Codice-Servizio") String xCodiceServizio,
			@HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale,
			@HeaderParam("X-Filename-Originale") String xFilenameOriginale,
			@HeaderParam("Content-Type") String contentType, byte[] allegato, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/{id_allegato}")
	@Produces({ "application/json", "application/pdf", "image/jpeg", "image/png" })
	public Response getAllegatoBuono(@PathParam("id_allegato") Integer idAllegato,
			@HeaderParam("X-Request-Id") String xRequestId, @HeaderParam("X-Forwarded-For") String xForwardedFor,
			@HeaderParam("X-Codice-Servizio") String xCodiceServizio,
			@HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
}
