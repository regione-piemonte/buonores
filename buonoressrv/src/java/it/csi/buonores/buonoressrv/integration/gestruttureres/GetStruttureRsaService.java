/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.integration.gestruttureres;

import java.nio.charset.Charset;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import it.csi.buonores.buonoressrv.business.be.service.base.BaseService;
import it.csi.buonores.buonoressrv.dto.Errore;
import it.csi.buonores.buonoressrv.util.BuonoResSrvProperties;
import it.csi.buonores.buonoressrv.util.enumerator.ApiHeaderParamEnum;
import it.csi.buonores.buonoressrv.util.enumerator.CodeErrorEnum;

@Service
public class GetStruttureRsaService extends BaseService {

	@Autowired
	private BuonoResSrvProperties buonoResProperties;
	
	private static final String SERVIZIO_STRUTTURE_RSA = "struttura-welfare/lista";
	private static final String SERVIZIO_COMUNI_RSA = "struttura-welfare/comuni";
	private static final String SERVIZIO_RENDICONTAZIONI ="rendicontazione/bo/lista";
	private static final String SERVIZIO_INTEGRAZIONI ="richieste-bandi/bo/lista";
	private static final String SERVIZIO_DOCUMENTI="file";
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	BuonoResSrvProperties properties;
	
	private String createAuthHeader(String username, String password) {
		String auth = username + ":" + password;
		String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + encodedAuth;
		return authHeader;
	}

	public Response execute(String denominazione, String comune, String indirizzo, String orderby, Integer rowPerPage,
			Integer pageNumber, Boolean descending, String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, SecurityContext securityContext) {
		Errore error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			UrlBuilder urlBuilder = buildUrl(denominazione, comune, indirizzo, orderby, rowPerPage, pageNumber,
					descending);
			String url = urlBuilder.buildUrl();

			logInfo(metodo, "call url: " + url);
			HttpHeaders headers = new HttpHeaders();
			if(StringUtils.isNotBlank(shibIdentitaCodiceFiscale)) {
				headers.add(ApiHeaderParamEnum.SHIB_IDENTITA_CODICEFISCALE.getCode(), shibIdentitaCodiceFiscale);
			}
			headers.add(ApiHeaderParamEnum.CONTENT_TYPE.getCode(), "application/json");
			headers.add(ApiHeaderParamEnum.AUTHORIZATION.getCode(), createAuthHeader(properties.getUsernameGestruttureres(), properties.getPasswordGestruttureres()));
			
			HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

			ResponseEntity<String> responseEntity = restTemplate.exchange(url.toString(), HttpMethod.GET,
					requestEntity, String.class);
			logInfo(metodo, " response status:" + responseEntity.getStatusCode());
			return Response.ok().entity(responseEntity.getBody()).build();
		} catch(HttpClientErrorException e) {
			error= handleHttpClientErrorException(metodo, CodeErrorEnum.ERR17, e.getStatusCode(), e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}
		return error.generateResponseError();
	}
	
	public Response getComuni(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, SecurityContext securityContext) {
		Errore error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			UrlBuilder urlBuilder = buildUrlPath(SERVIZIO_COMUNI_RSA);
			String url = urlBuilder.buildUrl();

			logInfo(metodo, "call url: " + url);
			HttpHeaders headers = new HttpHeaders();
			if(StringUtils.isNotBlank(shibIdentitaCodiceFiscale)) {
				headers.add(ApiHeaderParamEnum.SHIB_IDENTITA_CODICEFISCALE.getCode(), shibIdentitaCodiceFiscale);
			}
			headers.add(ApiHeaderParamEnum.CONTENT_TYPE.getCode(), "application/json");
			headers.add(ApiHeaderParamEnum.AUTHORIZATION.getCode(), createAuthHeader(properties.getUsernameGestruttureres(), properties.getPasswordGestruttureres()));
			
			HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
			
			ResponseEntity<String> responseEntity = restTemplate.exchange(url.toString(), HttpMethod.GET,
					requestEntity, String.class);
			logInfo(metodo, " response status:" + responseEntity.getStatusCode());
			return Response.ok().entity(responseEntity.getBody()).build();
		} catch(HttpClientErrorException e) {
			error= handleHttpClientErrorException(metodo, CodeErrorEnum.ERR17, e.getStatusCode(), e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}
		return error.generateResponseError();
	}

	private UrlBuilder buildUrlPath(String path) {
		UrlBuilder urlBuilder = new UrlBuilder(buonoResProperties.getBaseUrlGestruttureres()).path(path);
		return urlBuilder;
	}
	
	private UrlBuilder buildUrl(String denominazione, String comune, String indirizzo, String orderby,
			Integer rowPerPage, Integer pageNumber, Boolean descending) {
		String orderByIn = StringUtils.isNotBlank(orderby) ? orderby : "str_res_nome";
		Boolean descendingIn = descending == null ? false : descending.booleanValue();
		UrlBuilder urlBuilder = buildUrlPath(SERVIZIO_STRUTTURE_RSA);
		fillUrlBuilderIfNotNull("denominazione", denominazione, urlBuilder);
		fillUrlBuilderIfNotNull("comune", comune, urlBuilder);
		fillUrlBuilderIfNotNull("indirizzo", indirizzo, urlBuilder);
		urlBuilder.query("orderBy", orderByIn);
		fillUrlBuilderIfNotNull("rowPerPage", rowPerPage, urlBuilder);
		fillUrlBuilderIfNotNull("pageNumber", pageNumber, urlBuilder);
		urlBuilder.query("descending", descendingIn.toString());
		return urlBuilder;
	}

	private void fillUrlBuilderIfNotNull(String queryName, String value, UrlBuilder urlBuilder) {
		if (StringUtils.isNotBlank(value)) {
			urlBuilder.query(queryName, value);
		}
	}

	private void fillUrlBuilderIfNotNull(String queryName, Integer value, UrlBuilder urlBuilder) {
		if (value != null) {
			urlBuilder.query(queryName, value.toString());
		}
	}

	public Response getRendicontazioniByNumeroDomanda(String buonoCod, String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio, SecurityContext securityContext,
			 HttpServletRequest httpRequest) {
		Errore error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			UrlBuilder urlBuilder = buildUrlPath(SERVIZIO_RENDICONTAZIONI);
			urlBuilder.query("webappOperazione", "BO_BUONORES")
			.query("idProfiloOperazione", "25")
			.query("ruoloOperazione", "--")
			.query("utenteOperazione", shibIdentitaCodiceFiscale)
			.query("documentoNumero",buonoCod);
			String url = urlBuilder.buildUrl();
			

			logInfo(metodo, "call url: " + url);
			HttpHeaders headers = new HttpHeaders();
			if(StringUtils.isNotBlank(shibIdentitaCodiceFiscale)) {
				headers.add(ApiHeaderParamEnum.SHIB_IDENTITA_CODICEFISCALE.getCode(), shibIdentitaCodiceFiscale);
			}
			headers.add(ApiHeaderParamEnum.X_APPLICAZIONE_CODICE.getCode(),"BO_BUONORES");
			headers.add(ApiHeaderParamEnum.X_REQUEST_ID.getCode(), xRequestId);
			headers.add(ApiHeaderParamEnum.CONTENT_TYPE.getCode(), "application/json");
			headers.add(ApiHeaderParamEnum.AUTHORIZATION.getCode(), createAuthHeader(properties.getUsernameGestruttureres(), properties.getPasswordGestruttureres()));
			
			HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
			
			ResponseEntity<String> responseEntity = restTemplate.exchange(url.toString(), HttpMethod.GET,
					requestEntity, String.class);
			logInfo(metodo, " response status:" + responseEntity.getStatusCode());
			return Response.ok().entity(responseEntity.getBody()).build();
		} catch(HttpClientErrorException e) {
			error= handleHttpClientErrorException(metodo, CodeErrorEnum.ERR17, e.getStatusCode(), e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}
		return error.generateResponseError();
	}

	public Response getDocumentiRendicontazione(String idDocumento, String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, SecurityContext securityContext,
			HttpServletRequest httpRequest) {
		Errore error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			
			UrlBuilder urlBuilder = buildUrlPath(SERVIZIO_DOCUMENTI).path(idDocumento);
			urlBuilder.query("webappOperazione", "BO_BUONORES")
			.query("idProfiloOperazione", "25")
			.query("ruoloOperazione", "--")
			.query("utenteOperazione", shibIdentitaCodiceFiscale)
			.query("idEnteOperazione","210")
			.query("idStrutturaOperazione","670835");
			String url = urlBuilder.buildUrl();

			logInfo(metodo, "call url: " + url);
			HttpHeaders headers = new HttpHeaders();
			if(StringUtils.isNotBlank(shibIdentitaCodiceFiscale)) {
				headers.add(ApiHeaderParamEnum.SHIB_IDENTITA_CODICEFISCALE.getCode(), shibIdentitaCodiceFiscale);
			}
			headers.add(ApiHeaderParamEnum.X_APPLICAZIONE_CODICE.getCode(),"BO_BUONORES");
			headers.add(ApiHeaderParamEnum.X_REQUEST_ID.getCode(), xRequestId);
			headers.add(ApiHeaderParamEnum.CONTENT_TYPE.getCode(), "application/json");
			headers.add(ApiHeaderParamEnum.AUTHORIZATION.getCode(), createAuthHeader(properties.getUsernameGestruttureres(), properties.getPasswordGestruttureres()));
			
			HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
			
			ResponseEntity<String> responseEntity = restTemplate.exchange(url.toString(), HttpMethod.GET,
					requestEntity, String.class);
			logInfo(metodo, " response status:" + responseEntity.getStatusCode());
			return Response.ok().entity(responseEntity.getBody()).build();
		} catch(HttpClientErrorException e) {
			error= handleHttpClientErrorException(metodo, CodeErrorEnum.ERR17, e.getStatusCode(), e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}
		return error.generateResponseError();
	}

	public Response getIntegrazioni(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			javax.ws.rs.core.HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			UrlBuilder urlBuilder = buildUrlPath(SERVIZIO_INTEGRAZIONI);
			urlBuilder.query("webappOperazione", "BO_BUONORES")
			.query("idProfiloOperazione", "25")
			.query("ruoloOperazione", "--")
			.query("utenteOperazione", shibIdentitaCodiceFiscale)
			.query("idEnteOperazione","310")
			.query("idStrutturaOperazione","M00188")
			.query("documentoNumero",numeroRichiesta);
			String url = urlBuilder.buildUrl();
			

			logInfo(metodo, "call url: " + url);
			HttpHeaders headers = new HttpHeaders();
			if(StringUtils.isNotBlank(shibIdentitaCodiceFiscale)) {
				headers.add(ApiHeaderParamEnum.SHIB_IDENTITA_CODICEFISCALE.getCode(), shibIdentitaCodiceFiscale);
			}
			headers.add(ApiHeaderParamEnum.X_APPLICAZIONE_CODICE.getCode(),"BO_BUONORES");
			headers.add(ApiHeaderParamEnum.X_REQUEST_ID.getCode(), xRequestId);
			headers.add(ApiHeaderParamEnum.CONTENT_TYPE.getCode(), "application/json");
			headers.add(ApiHeaderParamEnum.AUTHORIZATION.getCode(), createAuthHeader(properties.getUsernameGestruttureres(), properties.getPasswordGestruttureres()));
			
			HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
			
			ResponseEntity<String> responseEntity = restTemplate.exchange(url.toString(), HttpMethod.GET,
					requestEntity, String.class);
			logInfo(metodo, " response status:" + responseEntity.getStatusCode());
			return Response.ok().entity(responseEntity.getBody()).build();
		} catch(HttpClientErrorException e) {
			error= handleHttpClientErrorException(metodo, CodeErrorEnum.ERR17, e.getStatusCode(), e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}
		return error.generateResponseError();
	}
	

}
