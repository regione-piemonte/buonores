/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.util.validator.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import it.csi.buonores.buonoressrv.dto.ErroreDettaglio;
import it.csi.buonores.buonoressrv.exception.DatabaseException;
import it.csi.buonores.buonoressrv.util.Util;
import it.csi.buonores.buonoressrv.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoressrv.util.enumerator.ErrorParamEnum;

@Service
public class ValidateGenericImpl extends BaseValidate {

	// 2. Verifica parametri in input (Criteri di validazione della richiesta)
	// 2a) Obbligatorieta'
	private List<ErroreDettaglio> commonCheck(List<ErroreDettaglio> result, String shibIdentitaCodiceFiscale,
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

	private List<ErroreDettaglio> checkIsValorizzato(List<ErroreDettaglio> result, String element, String tipoId)
			throws DatabaseException {

		if (!Util.isValorizzato(element)) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), tipoId));
		}
		return result;
	}

	public List<ErroreDettaglio> validateDomanda(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String numeroRichiesta, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws DatabaseException {
		logInfo(new Object() {
		}.getClass().getEnclosingMethod().getName(), "BEGIN");

		List<ErroreDettaglio> result = new ArrayList<>();

		commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);
		// inserimento controllo specifico citid
		checkIsValorizzato(result, numeroRichiesta, ErrorParamEnum.NUMERODOMANDA.getCode());

		return result;
	}

	public List<ErroreDettaglio> validateLettera(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String numeroRichiesta, String tipoLettera,
			SecurityContext securityContext, HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest)
			throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

//		2. Verifica parametri in input (Criteri di validazione della richiesta)
//		2a) Obbligatorieta'
		List<ErroreDettaglio> result = new ArrayList<>();

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		// inserimento controllo specifico citid
		String[] param = new String[1];
		if (!Util.isValorizzato(numeroRichiesta)) {
			param[0] = ErrorParamEnum.NUMERODOMANDA.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		if (!Util.isValorizzato(tipoLettera)) {
			param[0] = ErrorParamEnum.TIPOLETTERA.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		return result;
	}

	public List<ErroreDettaglio> validateNotifica(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String numeroRichiesta, String tipoNotifica,
			SecurityContext securityContext, HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest)
			throws DatabaseException {
		logInfo(new Object() {
		}.getClass().getEnclosingMethod().getName(), "BEGIN");

		List<ErroreDettaglio> result = new ArrayList<>();

		commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		// inserimento controllo specifico citid
		checkIsValorizzato(result, numeroRichiesta, ErrorParamEnum.NUMERODOMANDA.getCode());
		checkIsValorizzato(result, tipoNotifica, ErrorParamEnum.TIPONOTIFICA.getCode());

		return result;
	}

	public List<ErroreDettaglio> validateContatti(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) throws DatabaseException {
		logInfo(new Object() {
		}.getClass().getEnclosingMethod().getName(), "BEGIN");

		List<ErroreDettaglio> result = new ArrayList<>();

		commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		return result;
	}
}
