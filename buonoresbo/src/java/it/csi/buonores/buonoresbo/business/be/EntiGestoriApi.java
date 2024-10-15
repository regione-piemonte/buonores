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

import it.csi.buonores.buonoresbo.dto.CambioStatoPopUp;
import it.csi.buonores.buonoresbo.dto.ModelVerificheEnte;

@Path("/entigestori")

public interface EntiGestoriApi {

	@GET
	@Produces({ "application/json" })
	public Response getEntiGestori(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@POST
	@Path("/richiedirettifica")
	@Produces({ "application/json" })
	public Response richiediRettifica(CambioStatoPopUp nota, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@POST
	@Path("/updateToVerificaInCorso")
	@Produces({ "application/json" })
	public Response updateToVerificaInCorso(String numeroDomande[], @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/getverificheentegestore/{numeroDomanda}")
	@Produces({ "application/json" })
	public Response getVerificheEnteGestore(@PathParam("numeroDomanda") String buonoCod,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	/*
	 * Nuova parte verifica da parte dell'ente gestore
	 */
	@POST
	@Path("/salvaverificaentenew/{numero_domanda}")
	@Produces({ "application/json" })
	public Response salvaVerificaEnteNew(@PathParam("numero_domanda") String numeroDomanda,
			ModelVerificheEnte verificheEnte, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@POST
	@Path("/salvaconcludiverificaente/{numero_domanda}")
	@Produces({ "application/json" })
	public Response salvaConcludiVerificaEnte(@PathParam("numero_domanda") String numeroDomanda,
			ModelVerificheEnte verificheEnte, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}
