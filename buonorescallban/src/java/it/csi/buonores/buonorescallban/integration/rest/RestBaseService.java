/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonorescallban.integration.rest;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonores.buonorescallban.util.BuonoResCallbanProperties;
import it.csi.buonores.buonorescallban.util.Constants;
import it.csi.buonores.buonorescallban.util.LoggerUtil;
import it.csi.buonores.buonorescallban.util.Util;
import it.csi.buonores.buonorescallban.util.rest.ResponseRest;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


@Component
public class RestBaseService extends LoggerUtil {
	
	@Autowired
	BuonoResCallbanProperties properties; 
	
	private OkHttpClient okHttpClient;
	
	private OkHttpClient getHttpClient() {
		if(okHttpClient == null) {
			okHttpClient = new OkHttpClient.Builder()
			        .connectTimeout(45, TimeUnit.SECONDS)
			        .writeTimeout(45, TimeUnit.SECONDS)
			        .readTimeout(45, TimeUnit.SECONDS)
			        .addInterceptor(new BasicAuthInterceptor(properties.getBuonoressrvusername(), properties.getBuonoressrvpassword()))
			        .build();
		}
		
		return okHttpClient; 
	}
	
	public ResponseRest eseguiGet(HttpHeaders headers, String url, boolean shibboleth, List<String> headerName) throws Exception{
		
		Request request = createGetRequest(headers, url, shibboleth);
		Response resp = execute(request);
		ResponseRest response = new ResponseRest(); 
		response.setStatusCode(resp.code());
		String jsonRit = resp.body().string(); //con questa chiamata ho chiuso la response
		response.setJson(jsonRit);
		if(headerName != null) {
			headerName.stream().forEach(c -> response.addHeaders(c, resp.header(c)) );
		}
		
		return response;
	}
	
	
	public ResponseRest eseguiPost(HttpHeaders headers, String url, String jsonBody, boolean shibboleth) throws Exception{
		
		Request request = createPostRequest(headers, url, jsonBody, shibboleth);
		Response resp = execute(request);
		ResponseRest response = new ResponseRest(); 
		response.setStatusCode(resp.code());
		String jsonRit = resp.body().string(); //con questa chiamata ho chiuso la response
		response.setJson(jsonRit);
		return response;
	}
	
	public ResponseRest eseguiPut(HttpHeaders headers, String url, String jsonBody, boolean shibboleth) throws Exception{
		
		Request request = createPutRequest(headers, url, jsonBody, shibboleth);
		Response resp = execute(request);
		ResponseRest response = new ResponseRest(); 
		response.setStatusCode(resp.code());
		String jsonRit = resp.body().string(); //con questa chiamata ho chiuso la response
		response.setJson(jsonRit);
		return response;
	}		
	
	private Response execute(Request request) throws Exception {
		final String METHOD_NAME =  "execute";
		try{
			getHttpClient();
			return okHttpClient.newCall(request).execute();
		}catch (Exception e){
			logError(METHOD_NAME, "OkHttp post error", e);
			throw new Exception("OkHttp post error", e);
		}
	}

	private String getHeaderParam(HttpHeaders httpHeaders, String headerParam) {
		List<String> values = httpHeaders.getRequestHeader(headerParam);
		if (values == null || values.isEmpty()) {
			return null;
		}
		return values.get(0);
	}
	
	private Request createGetRequest(HttpHeaders headers, String url, boolean shibboleth){
		Request request = addCustomHeaders(headers, new Request.Builder(), shibboleth)
				.url(url)
				.build();
		logInfo("createGetRequest", "url: %s" + url);
		return request;
	}
	
	private Request.Builder addCustomHeaders(HttpHeaders httpHeaders, Request.Builder reqBuilder, boolean shibboleth) {
        
        if (getHeaderParam(httpHeaders, Constants.X_REQUEST_ID)!=null) {
            reqBuilder.addHeader(Constants.X_REQUEST_ID, getHeaderParam(httpHeaders, Constants.X_REQUEST_ID));
        }
        else {
        	UUID uuid = UUID.randomUUID();
            String uuidAsString = uuid.toString();
            reqBuilder.addHeader(Constants.X_REQUEST_ID,uuidAsString);
        }
        
		String xCodiceServizio = getHeaderParam(httpHeaders, Constants.X_CODICE_SERVIZIO);
		if(!Util.isValorizzato(xCodiceServizio)) {
            xCodiceServizio = Constants.COMPONENT_NAME;
            reqBuilder.addHeader(Constants.X_CODICE_SERVIZIO, xCodiceServizio); //TODO ??
		}
				
		if(shibboleth) {
			reqBuilder.addHeader(Constants.SHIB_IDENTITA_CODICE_FISCALE, getHeaderParam(httpHeaders, Constants.SHIB_IDENTITA_CODICE_FISCALE));
		} else {
            reqBuilder.addHeader(Constants.SHIB_IDENTITA_CODICE_FISCALE, Constants.COMPONENT_NAME.toUpperCase());
		}
		
		String xForwardedFor = getHeaderParam(httpHeaders, Constants.X_FORWARDED_FOR);
        if(!Util.isValorizzato(xForwardedFor)) {
            xForwardedFor="10.0.0.0";
			reqBuilder.addHeader(Constants.X_FORWARDED_FOR, xForwardedFor);
		}
		return reqBuilder;
	}
	
    private Request createPostRequest(HttpHeaders headers, String url, String json, boolean shibboleth){
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8") );
        Request request = addCustomHeaders(headers, new Request.Builder(), shibboleth)
                .url(url)
                .post(body)
                .build();

        return request;
    }
    
    private Request createPutRequest(HttpHeaders headers, String url, String json, boolean shibboleth){
    	RequestBody	body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8") );
    	Request request = addCustomHeaders(headers, new Request.Builder(), shibboleth)
    			.url(url)
    			.put(body)
    			.build();

    	return request;
    }    
    
}
