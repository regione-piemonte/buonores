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

import it.csi.buonores.buonoresbo.dto.ModelFiltriRicercaBuoni;

@Path("/buono")

public interface BuonoApi {

	@POST
	@Path("/ricerca")
	@Produces({ "application/json" })
	public Response ricercaBuoni(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest, ModelFiltriRicercaBuoni filtri);

	@GET
	@Path("/statibuono")
	@Produces({ "application/json" })
	public Response getStatiBuono(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/allegato/{id_allegato}")
	@Produces({ "application/json", "application/pdf", "image/jpeg", "image/png" })
	public Response getAllegatoBuono(@PathParam("id_allegato") Integer idAllegato,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/rendicontazione/{id_allegato}")
	@Produces({ "application/json", "application/pdf", "image/jpeg", "image/png" })
	public Response getRendicontazioneBuono(@PathParam("id_allegato") Integer idAllegato,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

}
