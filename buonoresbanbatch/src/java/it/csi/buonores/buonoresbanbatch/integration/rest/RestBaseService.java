/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbanbatch.integration.rest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import it.csi.buonores.buonoresbanbatch.configuration.Configuration;
import it.csi.buonores.buonoresbanbatch.dao.BuonoresbanBatchDAO;
import it.csi.buonores.buonoresbanbatch.logger.BatchLoggerFactory;
import it.csi.buonores.buonoresbanbatch.util.Constants;
import it.csi.buonores.buonoresbanbatch.util.rest.ResponseRest;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Component
public class RestBaseService extends BatchLoggerFactory {

	private String buonoressrvusername = Configuration.get("buonoressrvusername");
	private String buonoressrvpassword = Configuration.get("buonoressrvpassword");
	private String buonoressrvurl = Configuration.get("buonoressrvurl");

	private String buonoresbandisrvusername = Configuration.get("buonoresbandisrvusername");
	private String buonoresvbandisrvpassword = Configuration.get("buonoresbandisrvpassword");
	private String buonoresbandisrvurl = Configuration.get("buonoresbandisrvurl");

	private OkHttpClient okHttpClient;

	private static RestBaseService instance;

	private RestBaseService() {
	}

	public static RestBaseService getInstance(BuonoresbanBatchDAO dao) {
		if (instance == null) {
			instance = new RestBaseService();
		}
		return instance;
	}

	public static RestBaseService getInstance() {
		return instance;
	}

	private OkHttpClient getHttpClient() {
		if (okHttpClient == null) {
			okHttpClient = new OkHttpClient.Builder().connectTimeout(45, TimeUnit.SECONDS)
					.writeTimeout(45, TimeUnit.SECONDS).readTimeout(45, TimeUnit.SECONDS)
					.addInterceptor(new BasicAuthInterceptor(this.buonoressrvusername, this.buonoressrvpassword))
					.build();
		}

		return okHttpClient;
	}

	private OkHttpClient getHttpClientBandi() {
		if (okHttpClient == null) {
			okHttpClient = new OkHttpClient.Builder().connectTimeout(45, TimeUnit.SECONDS)
					.writeTimeout(45, TimeUnit.SECONDS).readTimeout(45, TimeUnit.SECONDS)
					.addInterceptor(
							new BasicAuthInterceptor(this.buonoresbandisrvusername, this.buonoresvbandisrvpassword))
					.build();
		}

		return okHttpClient;
	}
	
	public ResponseRest eseguiGet(String cf, String url, boolean shibboleth, List<String> headerName, boolean iride)
			throws Exception {
		Request request = createGetRequest(cf, url, shibboleth, iride);
		Response resp = execute(request);
		ResponseRest response = new ResponseRest();
		response.setStatusCode(resp.code());
		String jsonRit = resp.body().string(); // con questa chiamata ho chiuso la response
		response.setJson(jsonRit);
		if (headerName != null) {
			headerName.stream().forEach(c -> response.addHeaders(c, resp.header(c)));
		}

		return response;
	}

	public ResponseRest eseguiGetBandi(String cf, String url, boolean shibboleth, List<String> headerName,
			boolean iride) throws Exception {
		Request request = createGetRequest(cf, url, shibboleth, iride);
		Response resp = executeBandi(request);
		ResponseRest response = new ResponseRest();
		response.setStatusCode(resp.code());
		String jsonRit = resp.body().string(); // con questa chiamata ho chiuso la response
		response.setJson(jsonRit);
		if (headerName != null) {
			headerName.stream().forEach(c -> response.addHeaders(c, resp.header(c)));
		}

		return response;
	}

	private Response execute(Request request) throws Exception {
		final String METHOD_NAME = "execute";
		try {
			getHttpClient();
			return okHttpClient.newCall(request).execute();
		} catch (Exception e) {
			getLogger(this).error(METHOD_NAME + " OkHttp post error", e);
			throw new Exception("OkHttp post error", e);
		}
	}

	private Response executeBandi(Request request) throws Exception {
		final String METHOD_NAME = "execute";
		try {
			getHttpClientBandi();
			return okHttpClient.newCall(request).execute();
		} catch (Exception e) {
			getLogger(this).error(METHOD_NAME + " OkHttp post error", e);
			throw new Exception("OkHttp post error", e);
		}
	}

	private Request createGetRequest(String cf, String url, boolean shibboleth, boolean iride) {
		Request request = addCustomHeaders(cf, Constants.XREQUESTID, Constants.XFORWARDEDFOR, Constants.XCODICESERVIZIO,
				new Request.Builder(), shibboleth, iride).url(url).build();
		getLogger(this).info("createGetRequest url: %s" + url);
		return request;
	}

	private Request.Builder addCustomHeaders(String cf, String xRequestId, String xForwardedFor, String xCodiceServizio,
			Request.Builder reqBuilder, boolean shibboleth, boolean iride) {

		reqBuilder.addHeader(Constants.X_CODICE_SERVIZIO, xCodiceServizio).addHeader(Constants.X_REQUEST_ID,
				xRequestId);
		if (iride) {
			reqBuilder.addHeader(Constants.SHIB_IRIDE_IDENTITADIGITALE, cf);
		}
		if (shibboleth) {
			reqBuilder.addHeader(Constants.SHIB_IDENTITA_CODICE_FISCALE, cf);
		}
		if (!StringUtils.isEmpty(xForwardedFor)) {
			reqBuilder.addHeader(Constants.X_FORWARDED_FOR, xForwardedFor);
		}
		return reqBuilder;
	}

	public String getBuonoressrvusername() {
		return buonoressrvusername;
	}

	public void setBuonoressrvusername(String buonoressrvusername) {
		this.buonoressrvusername = buonoressrvusername;
	}

	public String getBuonoressrvpassword() {
		return buonoressrvpassword;
	}

	public void setBuonoressrvpassword(String buonoressrvpassword) {
		this.buonoressrvpassword = buonoressrvpassword;
	}

	public String getBuonoressrvurl() {
		return buonoressrvurl;
	}

	public void setBuonoressrvurl(String buonoressrvurl) {
		this.buonoressrvurl = buonoressrvurl;
	}

	public String getBuonoresbandisrvusername() {
		return buonoresbandisrvusername;
	}

	public void setBuonoresbandisrvusername(String buonoresbandisrvusername) {
		this.buonoresbandisrvusername = buonoresbandisrvusername;
	}

	public String getBuonoresvbandisrvpassword() {
		return buonoresvbandisrvpassword;
	}

	public void setBuonoresvbandisrvpassword(String buonoresvbandisrvpassword) {
		this.buonoresvbandisrvpassword = buonoresvbandisrvpassword;
	}

	public String getBuonoresbandisrvurl() {
		return buonoresbandisrvurl;
	}

	public void setBuonoresbandisrvurl(String buonoresbandisrvurl) {
		this.buonoresbandisrvurl = buonoresbandisrvurl;
	}

}
