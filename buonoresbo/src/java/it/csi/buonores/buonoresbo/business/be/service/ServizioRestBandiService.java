/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.business.be.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonoresbo.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbo.integration.rest.RestBaseBandiService;
import it.csi.buonores.buonoresbo.util.BuonoResBoProperties;
import it.csi.buonores.buonoresbo.util.rest.ResponseRest;

@Service
public class ServizioRestBandiService extends BaseService {

	@Autowired
	BuonoResBoProperties properties;

	@Autowired
	RestBaseBandiService restbase;

	private static final String RICEZIONE_SEGNALAZIONI = "/segnalazione/setRicezioneSegnalazioni";

	public ResponseRest setRicezioneSegnalazioni(String segnalazione, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest, String cf) {
		ResponseRest response = new ResponseRest();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio - segnalazione richiesta: " + segnalazione);
		try {
			response = restbase.eseguiPost(httpRequest, httpHeaders,
					properties.getBuonoresbandisrvurl() + RICEZIONE_SEGNALAZIONI, segnalazione, true, true, cf);
			log.info(metodo + " - Fine - segnalazione risposta: " + response.toString());
		} catch (Exception e) {
			log.error(metodo + " - errore " + e);
		}

		return response;
	}
}
