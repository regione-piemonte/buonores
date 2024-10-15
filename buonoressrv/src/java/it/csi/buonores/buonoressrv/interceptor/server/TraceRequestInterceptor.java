/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.interceptor.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.common.util.StringUtils;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import it.csi.buonores.buonoressrv.dto.custom.LogAudit;
import it.csi.buonores.buonoressrv.integration.service.LogAuditService;
import it.csi.buonores.buonoressrv.util.Constants;
import it.csi.buonores.buonoressrv.util.LoggerUtil;

@Provider
@ServerInterceptor
public class TraceRequestInterceptor extends LoggerUtil implements ContainerRequestFilter, ContainerResponseFilter {
	private @Context HttpServletRequest httpRequest;
	private @Context HttpServletResponse response;
	private @Context SecurityContext secContext;

	/**
	 * Verificare se corretto innestare il dao qui o se meglio mettere un BEAN che
	 * si occupi di 'slegare' la tracciatura su database
	 */

	@Autowired
	LogAuditService logauditService;

	private void getAuditDao() {
		if (logauditService == null) {
			logauditService = WebApplicationContextUtils
					.getRequiredWebApplicationContext(httpRequest.getServletContext()).getBean(LogAuditService.class);
		}
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		getAuditDao();
		final String methodName = "filter";
		LogAudit logaudit = new LogAudit();
		try {
			logaudit.setIdApp(Constants.COMPONENT_NAME.toUpperCase());
			logaudit.setIpAddress(httpRequest.getHeader("X-Forwarded-For"));
			if (httpRequest.getHeader("Shib-Identita-CodiceFiscale") != null)
				logaudit.setUtente(httpRequest.getHeader("Shib-Identita-CodiceFiscale"));
			else
				logaudit.setUtente("UTENTE NON PRESENTE");
			logaudit.setOperazione(httpRequest.getPathInfo());
			if (getRichiedenteFromURL(httpRequest.getRequestURI()) == null) {
				logaudit.setOggOper(httpRequest.getPathInfo());
			} else {
				logaudit.setOggOper(getRichiedenteFromURL(httpRequest.getRequestURI()));
			}
			logaudit.setKeyOper(httpRequest.getMethod() + " " + httpRequest.getRequestURI()
					+ (StringUtils.isEmpty(httpRequest.getQueryString()) ? "" : "?" + httpRequest.getQueryString()));
			logaudit.setUuid(httpRequest.getHeader("X-Request-ID"));

			if (isJson(requestContext)) {
				try {
					String json = IOUtils.toString(requestContext.getEntityStream(), StandardCharsets.UTF_8);
					logaudit.setRequestPayload(json);
					InputStream in = IOUtils.toInputStream(json, StandardCharsets.UTF_8);
					requestContext.setEntityStream(in);

				} catch (IOException ex) {
					logError(methodName, "IOUtils exception:", ex.getMessage());
				}
			} else {
				logaudit.setRequestPayload(
						StringUtils.isEmpty(httpRequest.getQueryString()) ? "" : "?" + httpRequest.getQueryString());
			}
			logaudit.setResponsePayload(null);
			logaudit.setEsitoChiamata(null);
			logauditService.insertLogAudit(logaudit);

			httpRequest.setAttribute(Constants.CONTEXT_TEMPO_PARTENZA, Long.valueOf(System.currentTimeMillis()));
		} catch (Exception e) {
			logError(methodName, "generic exception in request:", e.getMessage());
		}

	}

	private boolean isJson(ContainerRequestContext request) {
		return request.getMediaType() != null && request.getMediaType().toString().contains("application/json");
	}

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		getAuditDao();
		final String methodName = "filter";
		try {

			LogAudit logaudit = new LogAudit();
			logaudit.setIdApp(Constants.COMPONENT_NAME.toUpperCase());
			logaudit.setIpAddress(httpRequest.getHeader("X-Forwarded-For"));
			if (httpRequest.getHeader("Shib-Identita-CodiceFiscale") != null)
				logaudit.setUtente(httpRequest.getHeader("Shib-Identita-CodiceFiscale"));
			else
				logaudit.setUtente("UTENTE NON PRESENTE");
			logaudit.setOperazione(httpRequest.getPathInfo());
			if (getRichiedenteFromURL(httpRequest.getRequestURI()) == null) {
				logaudit.setOggOper(httpRequest.getPathInfo());
			} else {
				logaudit.setOggOper(getRichiedenteFromURL(httpRequest.getRequestURI()));
			}
			logaudit.setKeyOper(httpRequest.getMethod() + " " + httpRequest.getRequestURI()
					+ (StringUtils.isEmpty(httpRequest.getQueryString()) ? "" : "?" + httpRequest.getQueryString()));
			logaudit.setUuid(httpRequest.getHeader("X-Request-ID"));

			logaudit.setRequestPayload(
					StringUtils.isEmpty(httpRequest.getQueryString()) ? "" : "?" + httpRequest.getQueryString());

			logaudit.setEsitoChiamata(responseContext.getStatus());

			Object entity = responseContext.getEntity();
			try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
				if (entity != null)
					logaudit.setResponsePayload(entity.toString());
				else
					logaudit.setResponsePayload(null);
			} catch (Exception e) {
				logError(methodName, "errore in ByteArrayOutputStream:", e.getMessage());
			}
			logauditService.insertLogAudit(logaudit);

		} catch (Exception e) {
			logError(methodName, "generic exception in response:", e.getMessage());
		}

	}

	private static String getRichiedenteFromURL(String url) {
		String[] regexs = new String[] { ".*\\/anagrafica\\/(.*?)\\/.*", ".*\\/allegato\\/(.*?)\\/.*",
				".*\\/cronologia\\/(.*?)\\/.*", ".*\\/decodifiche\\/(.*?)\\/.*", ".*\\/files\\/(.*?)\\/.*",
				".*\\/sportelli\\/(.*?)\\/.*" };

		for (String regex : regexs) {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(url);
			if (matcher.matches()) {
				return matcher.group(1);
			}
		}
		return null;
	}

	@SuppressWarnings("unused")
	private String getHeaderParam(HttpHeaders httpHeaders, String headerParam) {
		List<String> values = httpHeaders.getRequestHeader(headerParam);
		if (values == null || values.isEmpty()) {
			return null;
		}
		return values.get(0);
	}
}
