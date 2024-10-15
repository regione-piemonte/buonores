/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.business.be.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.buonores.buonoresbo.dto.Contact;
import it.csi.buonores.buonoresbo.dto.Preferences;
import it.csi.buonores.buonoresbo.integration.rest.ApiManagerServiceClient;
import it.csi.buonores.buonoresbo.util.BuonoResBoProperties;
import it.csi.buonores.buonoresbo.util.Constants;
import it.csi.buonores.buonoresbo.util.LoggerUtil;

@Service
public class NotificatoreService extends LoggerUtil {

	@Autowired
	ApiManagerServiceClient tokenApiManager;

	@Autowired
	BuonoResBoProperties properties;

	private final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

	public Contact sendContact(String cfSoggetto, String iride) throws Exception {
		HttpResponse<String> response = createGetRequestToken(cfSoggetto, iride,
				properties.getUrlNotificatoreContact() + "/" + cfSoggetto + "/contacts");
		Contact contatto = new Contact();
		if (!(response.statusCode() == 200 || response.statusCode() == 201 || response.statusCode() == 401)) {
			logError("sendNotificaEvento: ", response.body());
		} else {
			// lascio solo utenza email e telefono
			String contattireturn = response.body();
			ObjectMapper mapper = new ObjectMapper();
			HashMap<String, Object> map = mapper.readValue(contattireturn, HashMap.class);
			contatto = buildContact(map);
		}
		return contatto;
	}

	private Contact buildContact(HashMap map) {
		Contact contact = new Contact();
		contact.setUserId((String) map.get("user_id"));
		contact.setEmail((String) map.get("email"));
		contact.setPhone((String) map.get("phone"));
		contact.setSms((String) map.get("sms"));
		return contact;
	}

	public Preferences sendPreferenze(String cfSoggetto, String iride) throws Exception {
		HttpResponse<String> response = createGetRequestToken(cfSoggetto, iride,
				properties.getUrlNotificatoreContact() + "/" + cfSoggetto + "/preferences/"
						+ properties.getRichiedenteApplicazioneNotificatore());
		Preferences preferenze = new Preferences();
		if (!(response.statusCode() == 200 || response.statusCode() == 201 || response.statusCode() == 401)) {
			logError("sendPreferenzeEvento: ", response.body());
		} else {
			preferenze = new ObjectMapper().readValue(response.body(), Preferences.class);
		}
		return preferenze;
	}

	private HttpResponse<String> createGetRequestToken(String cfSoggetto, String iride, String url) throws Exception {
		logInfo("sendNotificaEvento ",
				"params: \n" + Constants.SHIB_IRIDE_IDENTITADIGITALE + ": " + iride + ", \n"
						+ Constants.X_AUTHENTICATION + ": " + properties.getTokenApplicativoContact() + "\n");
		String token = tokenApiManager.getAccessToken();
		HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url))
				.setHeader(Constants.SHIB_IRIDE_IDENTITADIGITALE, iride)
				.setHeader(Constants.X_AUTHENTICATION, properties.getTokenApplicativoContact())
				.setHeader(Constants.AUTHORIZATION, token)
				.setHeader("Content-Type", "application/json").build();

		HttpResponse<String> response = null;
		response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		// print status code
		logInfo("sendNotificaEvento ", "status:" + response.statusCode());
		logInfo("sendNotificaEvento", "response: " + response.toString());
		logInfo("sendNotificaEvento", "responsebody: " + response.body());
		if (response.statusCode() == 401) {
			token = tokenApiManager.refreshToken();
			request = HttpRequest.newBuilder().GET().uri(URI.create(url))
					.setHeader(Constants.SHIB_IRIDE_IDENTITADIGITALE, iride)
					.setHeader(Constants.X_AUTHENTICATION, properties.getTokenApplicativoContact())
					.setHeader(Constants.AUTHORIZATION, token)
					.setHeader("Content-Type", "application/json").build();
			response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			logError("token scaduto nuovo token: ", token);
			logInfo("sendNotificaEvento ", "status:" + response.statusCode());
			logInfo("sendNotificaEvento", "response: " + response.toString());
			logInfo("sendNotificaEvento", "responsebody: " + response.body());
		}

		return response;
	}

}
