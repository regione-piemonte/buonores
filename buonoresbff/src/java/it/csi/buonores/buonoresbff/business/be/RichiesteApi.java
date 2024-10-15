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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.buonores.buonoresbff.dto.ModelBozzaRichiesta;
import it.csi.buonores.buonoresbff.dto.ModelRichiesta;

@Path("/richieste")

public interface RichiesteApi {

	@GET // SER 001
	@Produces({ "application/json" })
	public Response richiesteGet(@HeaderParam("X-Request-Id") String xRequestId,
			@HeaderParam("X-Forwarded-For") String xForwardedFor,
			@HeaderParam("X-Codice-Servizio") String xCodiceServizio,
			@HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET // SER 008
	@Path("/{numero_richiesta}")
	@Produces({ "application/json" })
	public Response richiesteNumeroRichiestaGet(@PathParam("numero_richiesta") String numeroRichiesta,
			@HeaderParam("X-Request-Id") String xRequestId, @HeaderParam("X-Forwarded-For") String xForwardedFor,
			@HeaderParam("X-Codice-Servizio") String xCodiceServizio,
			@HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@PUT // SER 006
	@Path("/{numero_richiesta}")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	public Response richiesteNumeroRichiestaPut(@PathParam("numero_richiesta") String numeroRichiesta,
			@HeaderParam("X-Request-Id") String xRequestId, @HeaderParam("X-Forwarded-For") String xForwardedFor,
			@HeaderParam("X-Codice-Servizio") String xCodiceServizio,
			@HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale, ModelRichiesta richiesta,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@POST // SER 005
	@Produces({ "application/json" })
	public Response richiestePost(@HeaderParam("X-Request-Id") String xRequestId,
			@HeaderParam("X-Forwarded-For") String xForwardedFor,
			@HeaderParam("X-Codice-Servizio") String xCodiceServizio,
			@HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale, ModelBozzaRichiesta richiesta,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
}
