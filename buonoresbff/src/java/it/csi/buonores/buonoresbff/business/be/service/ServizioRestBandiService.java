/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.business.be.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonoresbff.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbff.integration.rest.RestBaseBandiService;
import it.csi.buonores.buonoresbff.util.BuonoResBffProperties;
import it.csi.buonores.buonoresbff.util.rest.ResponseRest;

@Service
public class ServizioRestBandiService extends BaseService {
	@Autowired
	BuonoResBffProperties properties;

	@Autowired
	RestBaseBandiService restbase;

	private static final String RICEZIONE_SEGNALAZIONI = "/segnalazione/setRicezioneSegnalazioni";
	// private static final String SET_FORNITORE = "/fornitore/";

	public ResponseRest setRicezioneSegnalazioni(String segnalazione, String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ResponseRest response = new ResponseRest();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio - segnalazione richiesta: " + segnalazione);
		try {
			response = restbase.eseguiPost(httpHeaders, properties.getBuonoresbandisrvurl() + RICEZIONE_SEGNALAZIONI,
					segnalazione, true, true);
			log.info(metodo + " - Fine - segnalazione risposta: " + response.toString());
		} catch (Exception e) {
			log.error(metodo + " - errore " + e.getMessage());
		}
		return response;
	}

	// public ResponseRest setFornitore(String numeroRichiesta, String
	// shibIdentitaCodiceFiscale,
	// String xRequestId, String xForwardedFor, String xCodiceServizio,
	// SecurityContext securityContext,
	// HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	// ResponseRest response = new ResponseRest();
	// String metodo = new Object() {}.getClass().getEnclosingMethod().getName();
	// log.info(metodo +" - Inizio " );
	// try {
	// response = restbase.eseguiGet(httpHeaders,
	// properties.getBuonoresbandisrvurl() + SET_FORNITORE + numeroRichiesta, true,
	// null, true);
	// log.info(metodo +" - Fine " );
	// } catch (Exception e) {
	// log.error(metodo +" - errore " + e.getMessage() );
	// }
	// return response;
	// }
}
