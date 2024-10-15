/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.rest;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
//import javax.annotation.PostConstruct;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonores.buonoresbo.dto.UserInfo;
import it.csi.buonores.buonoresbo.filter.IrideIdAdapterFilter;
import it.csi.buonores.buonoresbo.util.BuonoResBoProperties;
import it.csi.buonores.buonoresbo.util.Constants;
import it.csi.buonores.buonoresbo.util.LoggerUtil;
import it.csi.buonores.buonoresbo.util.rest.ResponseRest;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class RestBaseBandiService extends LoggerUtil {

	private OkHttpClient okHttpClient;

	@Autowired
	BuonoResBoProperties properties;

	private OkHttpClient getHttpClient() {
		if (okHttpClient == null) {
			okHttpClient = new OkHttpClient.Builder()
					.connectTimeout(45, TimeUnit.SECONDS)
					.writeTimeout(45, TimeUnit.SECONDS)
					.readTimeout(45, TimeUnit.SECONDS)
					.addInterceptor(new BasicAuthInterceptor(properties.getBuonoresbandisrvusername(),
							properties.getBuonoresbandisrvpassword()))
					.build();
		}

		return okHttpClient;
	}

	public ResponseRest eseguiGet(HttpServletRequest httpRequest, HttpHeaders headers, String url, boolean shibboleth,
			List<String> headerName, boolean iride, String cf) throws Exception {
		Request request = createGetRequest(httpRequest, headers, url, shibboleth, iride, cf);
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

	public ResponseRest eseguiPost(HttpServletRequest httpRequest, HttpHeaders headers, String url, String jsonBody,
			boolean shibboleth, boolean iride, String cf) throws Exception {

		Request request = createPostRequest(httpRequest, headers, url, jsonBody, shibboleth, iride, cf);
		Response resp = execute(request);
		ResponseRest response = new ResponseRest();
		response.setStatusCode(resp.code());
		String jsonRit = resp.body().string(); // con questa chiamata ho chiuso la response
		response.setJson(jsonRit);
		return response;
	}

	public ResponseRest eseguiPut(HttpServletRequest httpRequest, HttpHeaders headers, String url, String jsonBody,
			boolean shibboleth, boolean iride, String cf) throws Exception {

		Request request = createPutRequest(httpRequest, headers, url, jsonBody, shibboleth, iride, cf);
		Response resp = execute(request);
		ResponseRest response = new ResponseRest();
		response.setStatusCode(resp.code());
		String jsonRit = resp.body().string(); // con questa chiamata ho chiuso la response
		response.setJson(jsonRit);
		return response;
	}

	private Response execute(Request request) throws Exception {
		final String METHOD_NAME = "execute";
		try {
			getHttpClient();
			return okHttpClient.newCall(request).execute();
		} catch (Exception e) {
			logError(METHOD_NAME, "OkHttp post error", e);
			throw new Exception("OkHttp post error", e);
		}
	}

	private String getHeaderParam(HttpHeaders httpHeaders, String headerParam) {
		if (httpHeaders != null) {
			List<String> values = httpHeaders.getRequestHeader(headerParam);
			if (values == null || values.isEmpty()) {
				return null;
			}
			return values.get(0);
		}
		return null;
	}

	private Request createGetRequest(HttpServletRequest httpRequest, HttpHeaders headers, String url,
			boolean shibboleth, boolean iride, String cf) {
		Request request = addCustomHeaders(httpRequest, headers, new Request.Builder(), shibboleth, iride, cf)
				.url(url)
				.build();
		logInfo("createGetRequest", "url: %s" + url);
		return request;
	}

	private Request.Builder addCustomHeaders(HttpServletRequest httpRequest, HttpHeaders httpHeaders,
			Request.Builder reqBuilder, boolean shibboleth, boolean iride, String cf) {

		String ipAddress = httpRequest.getRemoteAddr();
		UUID uuid = UUID.randomUUID();
		String uuidAsString = uuid.toString();
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		// nel caso manchino gli header li aggiungo
		if (getHeaderParam(httpHeaders, Constants.X_CODICE_SERVIZIO) != null) {
			reqBuilder.addHeader(Constants.X_CODICE_SERVIZIO, getHeaderParam(httpHeaders, Constants.X_CODICE_SERVIZIO));
		} else {
			reqBuilder.addHeader(Constants.X_CODICE_SERVIZIO, Constants.BUONORES);
		}

		if (getHeaderParam(httpHeaders, Constants.X_REQUEST_ID) != null) {
			reqBuilder.addHeader(Constants.X_REQUEST_ID, getHeaderParam(httpHeaders, Constants.X_REQUEST_ID));
		} else {
			reqBuilder.addHeader(Constants.X_REQUEST_ID, uuidAsString);
		}

		if (iride) {
			reqBuilder.addHeader(Constants.SHIB_IRIDE_IDENTITADIGITALE, userInfo.getCodFisc());
		}
		if (shibboleth) {
			if (getHeaderParam(httpHeaders, Constants.SHIB_IDENTITA_CODICE_FISCALE) != null) {
				reqBuilder.addHeader(Constants.SHIB_IDENTITA_CODICE_FISCALE,
						getHeaderParam(httpHeaders, Constants.SHIB_IDENTITA_CODICE_FISCALE));
			} else {
				reqBuilder.addHeader(Constants.SHIB_IDENTITA_CODICE_FISCALE, cf);
			}
		}
		String xForwardedFor = getHeaderParam(httpHeaders, Constants.X_FORWARDED_FOR);
		if (!StringUtils.isEmpty(xForwardedFor)) {
			reqBuilder.addHeader(Constants.X_FORWARDED_FOR, xForwardedFor);
		} else {
			reqBuilder.addHeader(Constants.X_FORWARDED_FOR, ipAddress);
		}
		return reqBuilder;
	}

	private Request createPostRequest(HttpServletRequest httpRequest, HttpHeaders headers, String url, String json,
			boolean shibboleth, boolean iride, String cf) {
		RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
		Request request = addCustomHeaders(httpRequest, headers, new Request.Builder(), shibboleth, iride, cf)
				.url(url)
				.post(body)
				.build();

		return request;
	}

	private Request createPutRequest(HttpServletRequest httpRequest, HttpHeaders headers, String url, String json,
			boolean shibboleth, boolean iride, String cf) {
		RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
		Request request = addCustomHeaders(httpRequest, headers, new Request.Builder(), shibboleth, iride, cf)
				.url(url)
				.put(body)
				.build();

		return request;
	}
}
