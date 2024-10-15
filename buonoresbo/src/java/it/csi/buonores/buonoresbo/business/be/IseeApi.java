/**********************************************
 * CSI PIEMONTE 
 **********************************************/
/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.buonores.buonoresbo.dto.ModelIseeBuono;

@Path("/isee")

public interface IseeApi {

	@POST
	@Path("/{numero_richiesta}")
	@Produces({ "application/json" })
	public Response addConformitaIsee(@PathParam("numero_richiesta") String numeroRichiesta, ModelIseeBuono isee,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/{numero_richiesta}")
	@Produces({ "application/json" })
	public Response getConformitaIsee(@PathParam("numero_richiesta") String numeroRichiesta,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/lista/{numero_richiesta}")
	@Produces({ "application/json" })
	public Response getListaConformitaIsee(@PathParam("numero_richiesta") String numeroRichiesta,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/listaesitiisee")
	@Produces({ "application/json" })
	public Response getListaEsitiIsee(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
}