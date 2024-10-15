/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.util;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.buonores.buonoresbo.integration.rest.BasicAuthInterceptor;

public abstract class LoggerUtil {

	@Autowired
	protected BuonoResBoProperties properties;

	private static final String GENERIC_SEPARATOR = " ";
	private static final String SEPARATOR = ": ";
	protected final Logger log = Logger.getLogger(getClass());
	protected Client client;
	protected Client clientApiManager;

	protected Client getClientApiManager() {
		if (clientApiManager == null) {
			clientApiManager = ClientBuilder.newBuilder().connectTimeout(45, TimeUnit.SECONDS)
					.readTimeout(45, TimeUnit.SECONDS)
					.register(new BasicAuthInterceptor(properties.getConsumerKey(), properties.getConsumerSecret()))
					.build();
		}

		return client;
	}

	protected Client getClient() {
		if (client == null) {
			client = ClientBuilder.newBuilder()
					.connectTimeout(45, TimeUnit.SECONDS)
					.readTimeout(45, TimeUnit.SECONDS)
					.register(new BasicAuthInterceptor(properties.getBuonoressrvusername(),
							properties.getBuonoressrvpassword()))
					.build();
		}

		return client;
	}

	protected void logInfo(String methodName, String string) {
		log.info(methodName + SEPARATOR + string);

	}

	protected void logError(String methodName, String string) {
		log.error(methodName + SEPARATOR + string);

	}

	protected void logError(String methodName, String string, Exception e) {
		log.error(methodName + SEPARATOR + string, e);

	}

	protected void logError(String methodName, String... extra) {

		var string = new StringBuilder();
		var first = true;
		for (String s : extra) {
			if (first) {
				first = false;
			} else {
				string.append(GENERIC_SEPARATOR);
			}

			string.append(s);
		}
		log.error(methodName + SEPARATOR + string.toString());

	}

}
