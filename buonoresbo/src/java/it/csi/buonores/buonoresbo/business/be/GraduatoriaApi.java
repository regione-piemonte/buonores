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

import it.csi.buonores.buonoresbo.dto.ModelNuovaGraduatoria;

@Path("/graduatoria")

public interface GraduatoriaApi {

	@GET
	@Path("/getultimosportellochiuso")
	@Produces({ "application/json" })
	public Response getUltimoSportelloChiuso(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/getsportellichiusi")
	@Produces({ "application/json" })
	public Response getSportelliChiusi(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@POST
	@Path("/creanuovagraduatoria")
	@Produces({ "application/json" })
	public Response creaNuovaGraduatoria(ModelNuovaGraduatoria numeroRichiesta,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/getdomandegraduatoria/{sportello_cod}")
	@Produces({ "application/json" })
	public Response getDomandeGraduatoria(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest, @PathParam("sportello_cod") String sportelloCod);

	@GET
	@Path("/getparametrifinanziamento/{sportello_cod}")
	@Produces({ "application/json" })
	public Response getParametriFinanziamento(@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest,
			@PathParam("sportello_cod") String sportelloCod);

	@GET
	@Path("/getgraduatoriaordinamento")
	@Produces({ "application/json" })
	public Response getGraduatoriaOrdinamento(@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/getgraduatoriadesc/{sportello_cod}")
	@Produces({ "application/json" })
	public Response selectGraduatoriaDesc(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest, @PathParam("sportello_cod") String sportelloCod);

	@GET
	@Path("/checkpubblicagraduatoria/{sportello_cod}")
	@Produces({ "application/json" })
	public Response checkPubblicazioneGraduatoria(@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest, @PathParam("sportello_cod") String sportelloCod);

	@GET
	@Path("/pubblicagraduatoria/{sportello_cod}")
	@Produces({ "application/json" })
	public Response pubblicaGraduatoria(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest, @PathParam("sportello_cod") String sportelloCod);

	@GET
	@Path("/aggiornagraduatoria/{sportello_cod}")
	@Produces({ "application/json" })
	public Response aggiornaGraduatoria(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest, @PathParam("sportello_cod") String sportelloCod);

	@GET
	@Path("/checkesistenzagraduatoria/{sportello_cod}")
	@Produces({ "application/json" })
	public Response checkEsistenzaGraduatoria(@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest, @PathParam("sportello_cod") String sportelloCod);

	@GET
	@Path("/checkestatograduatoria/{sportello_cod}/{stato}")
	@Produces({ "application/json" })
	public Response checkStatoGraduatoria(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest, @PathParam("sportello_cod") String sportelloCod,
			@PathParam("stato") String stato);

	@POST
	@Path("/simulagraduatoria")
	@Produces({ "application/json" })
	public Response simulaGraduatoria(ModelNuovaGraduatoria numeroRichiesta, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/getaree")
	@Produces({ "application/json" })
	public Response getAree(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

}