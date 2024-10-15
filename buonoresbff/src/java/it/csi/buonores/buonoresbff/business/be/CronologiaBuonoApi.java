/**********************************************
 * CSI PIEMONTE 
 **********************************************/
/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.business.be;

import javax.servlet.http.HttpServletRequest;
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

import it.csi.buonores.buonoresbff.dto.ModelStatoBuono;

@Path("/cronologia-buono")

public interface CronologiaBuonoApi {

	@GET
	@Path("/{numero_richiesta}")

	@Produces({ "application/json" })

	public Response getCronologiaBuono(@PathParam("numero_richiesta") String numeroRichiesta,
			@HeaderParam("X-Request-Id") String xRequestId, @HeaderParam("X-Forwarded-For") String xForwardedFor,
			@HeaderParam("X-Codice-Servizio") String xCodiceServizio,
			@HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@POST
	@Path("/{numero_richiesta}")

	@Produces({ "application/json" })

	public Response postCronologiaBuono(@PathParam("numero_richiesta") String numeroRichiesta,
			@HeaderParam("X-Request-Id") String xRequestId, @HeaderParam("X-Forwarded-For") String xForwardedFor,
			@HeaderParam("X-Codice-Servizio") String xCodiceServizio,
			@HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale, ModelStatoBuono stato,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
}
