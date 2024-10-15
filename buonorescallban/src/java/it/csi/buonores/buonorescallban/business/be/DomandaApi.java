/**********************************************
 * CSI PIEMONTE 
 **********************************************/
/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonorescallban.business.be;

import javax.servlet.http.HttpServletRequest;
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

import it.csi.buonores.buonorescallban.dto.PayloadEsitoAcquisizione;
import it.csi.buonores.buonorescallban.dto.PayloadRevDomanda;

@Path("/domanda")

public interface DomandaApi {

	@POST
	@Produces({ "application/json" })
	@Path("/{numero_domanda}/esito-acquisizione")
	public Response domandaNumeroDomandaEsitoAcquisizionePost(@HeaderParam("X-Request-Id") String xRequestId,
			@PathParam("numero_domanda") String numeroDomanda, PayloadEsitoAcquisizione payloadEsitoAcquisizione,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@PUT
	@Produces({ "application/json" })
	@Path("/{numero_domanda}/esito-acquisizione")
	public Response domandaNumeroDomandaEsitoAcquisizionePut(@HeaderParam("X-Request-Id") String xRequestId,
			@PathParam("numero_domanda") String numeroDomanda, PayloadEsitoAcquisizione payloadEsitoAcquisizione,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@PUT
	@Produces({ "application/json" })
	@Path("/{numero_domanda}/revoca")
	public Response domandaNumeroDomandaRevocaPut(@HeaderParam("X-Request-Id") String xRequestId,
			@PathParam("numero_domanda") String numeroDomanda, PayloadRevDomanda payloadRevDomanda,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
}
