/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.rest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.buonores.buonoresbo.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbo.dto.custom.TokenApiManager;
import it.csi.buonores.buonoresbo.util.BuonoResBoProperties;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class ApiManagerServiceClient extends BaseService {

	@Autowired
	BuonoResBoProperties properties;

	private volatile String token = null;

	private OkHttpClient client;

	private OkHttpClient getHttpClient() {
		if (client == null) {
			client = new OkHttpClient.Builder()
					.connectTimeout(45, TimeUnit.SECONDS)
					.writeTimeout(45, TimeUnit.SECONDS)
					.readTimeout(45, TimeUnit.SECONDS)
					.addInterceptor(
							new BasicAuthInterceptor(properties.getConsumerKey(), properties.getConsumerSecret()))
					.build();
		}

		return client;
	}

	private synchronized void callToken() throws IOException {
		final String methodName = "callToken";
		logInfo(methodName, "BEGIN");
		String strBody = "grant_type=client_credentials";
		RequestBody body = RequestBody.create(strBody, MediaType.parse("application/x-www-form-urlencoded"));
		Request reqToken = new Request.Builder().url(properties.getApiManagerUrl()).post(body).build();
		Response respToken;
		try {
			getHttpClient();
			respToken = client.newCall(reqToken).execute();
		} catch (IOException e) {
			logError(methodName, "okHttpToken.newCall Exception", e);
			throw e;
		}
		String json = respToken.body().string();
		if (respToken.isSuccessful()) {
			ObjectMapper mapper = new ObjectMapper();
			TokenApiManager tokenApi = mapper.readValue(json, new TypeReference<TokenApiManager>() {
			});
			token = new StringBuffer("Bearer").append(" ").append(tokenApi.getAccessToken()).toString();
		} else {
			logError("callToken", "Errore nella chiamata a api manager");
		}
	}

	public String getAccessToken() throws IOException {
		final String methodName = "getAccessToken";
		logInfo(methodName, "BEGIN");
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
