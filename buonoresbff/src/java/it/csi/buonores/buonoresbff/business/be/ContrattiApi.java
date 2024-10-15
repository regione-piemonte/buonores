/**********************************************
 * CSI PIEMONTE 
 **********************************************/
/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
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

import it.csi.buonores.buonoresbff.dto.ModelContratto;
import it.csi.buonores.buonoresbff.dto.ModelContrattoAllegati;

@Path("/contratti")
public interface ContrattiApi {

	@POST
	@Path("/{numero_richiesta}")
	@Produces({ "application/json" })
	public Response addContratto(@PathParam("numero_richiesta") String numeroRichiesta,
			@HeaderParam("X-Request-Id") String xRequestId, @HeaderParam("X-Forwarded-For") String xForwardedFor,
			@HeaderParam("X-Codice-Servizio") String xCodiceServizio,
			@HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale,
			ModelContrattoAllegati contrattoAllegati, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/{numero_richiesta}")
	@Produces({ "application/json" })
	public Response getContratti(@PathParam("numero_richiesta") String numeroRichiesta,
			@HeaderParam("X-Request-Id") String xRequestId, @HeaderParam("X-Forwarded-For") String xForwardedFor,
			@HeaderParam("X-Codice-Servizio") String xCodiceServizio,
			@HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@PUT
	@Path("/{numero_richiesta}/{id_contratto}")
	@Produces({ "application/json" })
	public Response putContratto(@PathParam("numero_richiesta") String numeroRichiesta,
			@PathParam("id_contratto") Integer idContratto, @HeaderParam("X-Request-Id") String xRequestId,
			@HeaderParam("X-Forwarded-For") String xForwardedFor,
			@HeaderParam("X-Codice-Servizio") String xCodiceServizio,
			@HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale, ModelContratto contratto,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@DELETE
	@Path("/{numero_richiesta}/{id_contratto}")
	@Produces({ "application/json" })
	public Response deleteContratto(@PathParam("numero_richiesta") String numeroRichiesta,
			@PathParam("id_contratto") Integer idContratto, @HeaderParam("X-Request-Id") String xRequestId,
			@HeaderParam("X-Forwarded-For") String xForwardedFor,
			@HeaderParam("X-Codice-Servizio") String xCodiceServizio,
			@HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

}
