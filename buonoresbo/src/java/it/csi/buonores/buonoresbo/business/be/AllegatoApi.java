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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/allegato")

public interface AllegatoApi {

	@GET
	@Path("/{numero_richiesta}/{tipo_allegato}")

	@Produces({ "application/json", "application/pdf", "image/jpeg", "image/png" })

	public Response allegatoNumeroRichiestaTipoAllegatoGet(@PathParam("tipo_allegato") String tipoAllegato,
			@PathParam("numero_richiesta") String numeroRichiesta, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
}
