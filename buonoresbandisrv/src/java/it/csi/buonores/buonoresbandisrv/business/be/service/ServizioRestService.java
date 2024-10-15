/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.business.be.service;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonoresbandisrv.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbandisrv.integration.rest.BandiRestBaseService;
import it.csi.buonores.buonoresbandisrv.integration.rest.BuonoresRestBaseService;
import it.csi.buonores.buonoresbandisrv.util.BuonoResBandiSrvProperties;
import it.csi.buonores.buonoresbandisrv.util.rest.ResponseRest;

@Service
public class ServizioRestService extends BaseService {

	@Autowired
	BuonoResBandiSrvProperties properties;

	@Autowired
	BandiRestBaseService restbaseBandi;

	@Autowired
	BuonoresRestBaseService restbaseBuono;

	private static final String NOTIFICHE_CONTATTI = "/notifiche/contatti";
	private static final String DOMANDE_CONCESSE = "acquisizioneDomande/getDomandeConcesse";
	private static final String SET_FORNITORE = "gestioneFornitori/setFornitore";
	private static final String RICEZIONE_SEGNALAZIONI = "ricezioneSegnalazioni/setRicezioneSegnalazioni";

	public ResponseRest getContatti(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		ResponseRest response = new ResponseRest();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		try {
			response = restbaseBuono.eseguiGet(httpHeaders, properties.getBuonoressrvurl() + NOTIFICHE_CONTATTI,
					true, null);
			log.info(metodo + " - Fine with response: " + response.toString());
		} catch (Exception e) {
			log.error(metodo + " - errore: " + e);
		}
		return response;
	}

	public ResponseRest postAcquisizioneDomande(String numeroRichiesta, String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest, String keynome, String valuenome, File xml) {
		ResponseRest response = new ResponseRest();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		try {
			response = restbaseBandi.eseguiPostMultipart(httpHeaders, properties.getBandiacquisizionedomandeurl()
					+ DOMANDE_CONCESSE + "?numeroDomanda=" + numeroRichiesta + "&codiceBando=RES",
					keynome, valuenome, xml, true);
			log.info(metodo + " - Fine with response: " + response.toString());
		} catch (Exception e) {
			log.error(metodo + " - errore: " + e);
		}
		return response;
	}

	public ResponseRest postSetFornitore(String fornitore, String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ResponseRest response = new ResponseRest();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		try {
			response = restbaseBandi.eseguiPost(httpHeaders,
					properties.getBandiacquisizionedomandeurl() + SET_FORNITORE, fornitore, true);
			log.info(metodo + " - Fine with response: " + response.toString());
		} catch (Exception e) {
			log.error(metodo + " - errore: " + e);
		}
		return response;
	}

	public ResponseRest postRicezioneSegnalazione(String segnalazione, String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ResponseRest response = new ResponseRest();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		try {
			response = restbaseBandi.eseguiPost(httpHeaders,
					properties.getBandiacquisizionedomandeurl() + RICEZIONE_SEGNALAZIONI, segnalazione, true);
			log.info(metodo + " - Fine with response: " + response.toString());
		} catch (Exception e) {
			log.error(metodo + " - errore: " + e);
		}
		return response;
	}
}
