/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresrend.util.validator.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import it.csi.buonores.buonoresrend.dto.ErroreDettaglio;
import it.csi.buonores.buonoresrend.exception.DatabaseException;
import it.csi.buonores.buonoresrend.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoresrend.util.enumerator.ErrorParamEnum;

@Service
public class ValidateGenericImpl extends BaseValidate {

	// 2. Verifica parametri in input (Criteri di validazione della richiesta)
	// 2a) Obbligatorieta'
	public List<ErroreDettaglio> commonCheck(List<ErroreDettaglio> result, String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio) throws DatabaseException {

		checkEmptyString(result, xRequestId, ErrorParamEnum.X_REQUEST_ID.getCode());
		checkEmptyString(result, shibIdentitaCodiceFiscale, ErrorParamEnum.SHIB_IDENTITA_CODICEFISCALE.getCode());
		checkEmptyString(result, xForwardedFor, ErrorParamEnum.X_FORWARDED_FOR.getCode());
		checkEmptyString(result, xCodiceServizio, ErrorParamEnum.X_CODICE_SERVIZIO.getCode());

		return result;
	}

	private List<ErroreDettaglio> checkEmptyString(List<ErroreDettaglio> result, String element, String tipoElement)
			throws DatabaseException {

		if (StringUtils.isEmpty(element)) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), tipoElement));
		}
		return result;
	}
	
	public List<ErroreDettaglio> validateGeneric(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) throws DatabaseException {
		logInfo(new Object() {
			}.getClass().getEnclosingMethod().getName(), "BEGIN");

		List<ErroreDettaglio> result = new ArrayList<>();
		commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);
		return result;
	}
}
