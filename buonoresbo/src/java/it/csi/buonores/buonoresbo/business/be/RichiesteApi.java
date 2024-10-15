/**********************************************
 * CSI PIEMONTE 
 **********************************************/
/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.business.be;

import java.util.List;

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
import it.csi.buonores.buonoresbo.dto.ModelIsee;
import it.csi.buonores.buonoresbo.dto.ModelPresaInCarico;

@Path("/richieste")
public interface RichiesteApi {

	@GET
	@Path("/{numero_richiesta}")
	@Produces({ "application/json" })
	public Response richiesteNumeroRichiestaGet(@PathParam("numero_richiesta") String numeroRichiesta,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@POST
	@Path("/presa_carico")
	@Produces({ "application/json" })
	public Response presaInCaricoRichiesta(List<ModelPresaInCarico> richieste, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/cronologia/{numero_richiesta}")
	@Produces({ "application/json" })
	public Response cronologiaRichiesta(@PathParam("numero_richiesta") String numeroRichiesta,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/listaallegati/{numero_richiesta}")
	@Produces({ "application/json" })
	public Response listaAllegatoRichiesta(@PathParam("numero_richiesta") String numeroRichiesta,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/listacampi/{numero_richiesta}")
	@Produces({ "application/json" })
	public Response listaCampiRichiesta(@PathParam("numero_richiesta") String numeroRichiesta,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@POST
	@Path("/richiedi_rettifica")
	@Produces({ "application/json" })
	public Response datiDaModificarePost(CambioStatoPopUp datiDaModificare, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/verifiche/{numero_richiesta}")
	@Produces({ "application/json" })
	public Response verifiche(@PathParam("numero_richiesta") String numeroRichiesta,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@POST
	@Path("/ammissibile")
	@Produces({ "application/json" })
	public Response ammissibileRichiesta(CambioStatoPopUp datiPopUp, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@POST
	@Path("/preavvisopernonammissibilita")
	@Produces({ "application/json" })
	public Response preavvisoPerNonAmmissibilitaRichiesta(CambioStatoPopUp datiPopUp,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@POST
	@Path("/ammessaconriserva")
	@Produces({ "application/json" })
	public Response ammessaConRiservaRichiesta(CambioStatoPopUp datiPopUp, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@POST
	@Path("/diniego")
	@Produces({ "application/json" })
	public Response diniegoRichiesta(CambioStatoPopUp datiPopUp, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/verificacontatto/{numero_richiesta}")
	@Produces({ "application/json" })
	public Response verificaContatto(@PathParam("numero_richiesta") String numeroRichiesta,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@POST
	@Path("/ammessa")
	@Produces({ "application/json" })
	public Response ammessaRichiesta(CambioStatoPopUp datiPopUp, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@POST
	@Path("/inpagamento")
	@Produces({ "application/json" })
	public Response inPagamentoRichiesta(CambioStatoPopUp datiPopUp, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@POST
	@Path("/ammessaconriservainpagamento")
	@Produces({ "application/json" })

	public Response ammessaConRiservaInPagamentoRichiesta(CambioStatoPopUp datiPopUp,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@POST
	@Path("/salvadomandaisee/{numero_richiesta}")
	@Produces({ "application/json" })
	public Response salvaDomandaIsee(@PathParam("numero_richiesta") String numeroRichiesta, ModelIsee isee,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@POST
	@Path("/salvadomandanota")
	@Produces({ "application/json" })
	public Response salvaDomandaNota(CambioStatoPopUp nota, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@POST
	@Path("/nonammissibile")
	@Produces({ "application/json" })
	public Response nonammissibileRichiesta(CambioStatoPopUp datiPopUp, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}
