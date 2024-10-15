/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.business.be.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonoressrv.business.be.service.base.BaseService;
import it.csi.buonores.buonoressrv.dto.Errore;
import it.csi.buonores.buonoressrv.dto.ErroreDettaglio;
import it.csi.buonores.buonoressrv.dto.ModelGetAllegatoExt;
import it.csi.buonores.buonoressrv.dto.ModelRichiestaRecupero;
import it.csi.buonores.buonoressrv.exception.DatabaseException;
import it.csi.buonores.buonoressrv.integration.dao.custom.RichiesteDao;
import it.csi.buonores.buonoressrv.util.Constants;
import it.csi.buonores.buonoressrv.util.validator.impl.ValidateGenericImpl;

@Service
public class RecuperoErroriService extends BaseService {

	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	private SmistaDocumentoPartenzaService smistaDocumentoPartenzaService;

	@Autowired
	private CreaLetteraService creaLetteraService;

	public Response recuperoErroriStardasPartenza(String tipolettera, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		List<ModelRichiestaRecupero> richieste = new ArrayList<ModelRichiestaRecupero>();
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglio> listerrorservice = new ArrayList<ErroreDettaglio>();
		ModelGetAllegatoExt lettera = new ModelGetAllegatoExt();
		String letteradacreare = null;
		try {
			if (tipolettera.equalsIgnoreCase(Constants.LETTERA_DINIEGO)) {
				richieste = richiesteDao.selectNumeroRichiesteRecupero(Constants.DINIEGO);
			}

			if (richieste != null && richieste.size() > 0) {
				for (ModelRichiestaRecupero richiesta : richieste) {
					// verifico se esiste allegato lettera nel caso negativo la creo
					lettera = richiesteDao.selectAllegatiProtPartenza(richiesta.getDomandaDetId(),
							Constants.LETTERA_DINIEGO);
					// non trova la lettera si deve rigenerare
					if (lettera == null) {
						if (richiesta.getNote() != null) {
							if (richiesta.getNote().contains("decesso del destinatario"))
								letteradacreare = Constants.DINIEGO_SCADENZA_REQUISTI_DECESSO;
							else if (richiesta.getNote().contains("cambio di residenza"))
								letteradacreare = Constants.DINIEGO_SCADENZA_REQUISTI_CAMBIO_RESIDENZA;
							else
								letteradacreare = Constants.LETTERA_DINIEGO;
							creaLetteraService.creaLettera(richiesta.getNumero(), letteradacreare, xRequestId,
									xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale, securityContext,
									httpHeaders, httpRequest);
						}
					}
					smistaDocumentoPartenzaService.execute(richiesta.getNumero(), Constants.LETTERA_DINIEGO,
							shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio, securityContext,
							httpHeaders, httpRequest);
				}
			}
			return Response.ok().entity(richieste).build();
		} catch (DatabaseException e) {
			handleDatabaseException(metodo, e);
		} catch (Exception e) {
			handleException(metodo, e);
		}
		
		return error.generateResponseError();
	}
}
