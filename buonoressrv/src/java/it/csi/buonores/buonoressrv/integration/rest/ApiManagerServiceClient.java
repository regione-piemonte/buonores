/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.integration.rest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.buonores.buonoressrv.dto.apimanager.TokenApiManager;
import it.csi.buonores.buonoressrv.util.BuonoResSrvProperties;
import it.csi.buonores.buonoressrv.util.LoggerUtil;

@Component
public class ApiManagerServiceClient extends LoggerUtil {

	private volatile String token = null;
	protected Client client;

	@Autowired
	private BuonoResSrvProperties properties;

	protected Client getClient() {
		if (client == null) {
			client = ClientBuilder.newBuilder().connectTimeout(45, TimeUnit.SECONDS).readTimeout(45, TimeUnit.SECONDS)
					.register(new BasicAuthInterceptor(properties.getConsumerKey(), properties.getConsumerSecret()))
					.build();
		}

		return client;
	}

	private synchronized void callToken() throws IOException {
		try {
			String strBody = "grant_type=client_credentials";
			getClient();
			Invocation.Builder builder = client.target(properties.getApiManagerUrl()).request();
			Response response = builder.post(Entity.entity(strBody, MediaType.APPLICATION_FORM_URLENCODED));

			String json = response.readEntity(String.class);
			if (response.getStatus() == HttpStatus.OK.value()) {
				ObjectMapper mapper = new ObjectMapper();
				TokenApiManager tokenApi = mapper.readValue(json, new TypeReference<TokenApiManager>() {
				});
				token = new StringBuffer("Bearer").append(" ").append(tokenApi.getAccessToken()).toString();
			} else {

			}
		} catch (Exception e) {
			logError("callToken", e.getMessage(), e);
		}
	}

	public String getAccessToken() throws IOException {
		if (this.token == null) {
			callToken();
		}
		return this.token;
	}

	public String refreshToken() throws IOException {
		callToken();
		return this.token;
	}

}
