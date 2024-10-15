/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.business.be;

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

import it.csi.buonores.buonoresbo.dto.ModelDecorrenzaBuono;
import it.csi.buonores.buonoresbo.dto.ModelFiltroDateIntegrazione;

@Path("/buonodettaglio")

public interface BuonoDettaglioApi {

	@GET
	@Path("/getallegati/{numeroRichiesta}")
	@Produces({ "application/json" })
	public Response getAllegatiBuono(@PathParam("numeroRichiesta") String numeroRichiesta,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/getcontratti/{numeroRichiesta}")
	@Produces({ "application/json" })
	public Response getContrattiBuono(@PathParam("numeroRichiesta") String buonoCod,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/getdecorrenza/{numeroRichiesta}")
	@Produces({ "application/json" })
	public Response getDecorrenzaBuono(@PathParam("numeroRichiesta") String numeroRichiesta,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@POST
	@Path("/integrazioni/{numero_richiesta}")
	@Produces({ "application/json" })
	public Response getIntegrazioni(@PathParam("numero_richiesta") String numeroRichiesta,
			@HeaderParam("X-Request-Id") String xRequestId, @HeaderParam("X-Forwarded-For") String xForwardedFor,
			@HeaderParam("X-Codice-Servizio") String xCodiceServizio,
			@HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest, ModelFiltroDateIntegrazione date);

	@POST
	@Path("/updatedecorrenza/{numeroRichiesta}")
	@Produces({ "application/json" })
	public Response updateDecorrenzaBuono(@PathParam("numeroRichiesta") String numeroRichiesta,
			ModelDecorrenzaBuono decorrenza, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/getbuonoinformazioni/{numeroRichiesta}")
	@Produces({ "application/json" })
	public Response getBuonoInformazioni(@PathParam("numeroRichiesta") String numeroRichiesta,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/getverificainformazioni/{numeroRichiesta}")
	@Produces({ "application/json" })
	public Response getVerificaInformazioni(@PathParam("numeroRichiesta") String numeroRichiesta,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/getstoricorendicontazioni/{numeroBuono}/{numeroRichiesta}")
	@Produces({ "application/json" })
	public Response getStoricoRendicontazioni(@PathParam("numeroBuono") String numeroBuono,
			@PathParam("numeroRichiesta") String numeroRichiesta,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
}