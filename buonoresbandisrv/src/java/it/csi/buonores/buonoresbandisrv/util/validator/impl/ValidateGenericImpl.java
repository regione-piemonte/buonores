/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.util.validator.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import it.csi.buonores.buonoresbandisrv.dto.ErroreDettaglio;
import it.csi.buonores.buonoresbandisrv.dto.ModelRicezioneSegnalazione;
import it.csi.buonores.buonoresbandisrv.exception.DatabaseException;
import it.csi.buonores.buonoresbandisrv.util.Constants;
import it.csi.buonores.buonoresbandisrv.util.Util;
import it.csi.buonores.buonoresbandisrv.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoresbandisrv.util.enumerator.ErrorParamEnum;

@Service
public class ValidateGenericImpl extends BaseValidate {

	private List<ErroreDettaglio> commonCheck(List<ErroreDettaglio> result, String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio) throws DatabaseException {

		String[] param = new String[1];
		if (StringUtils.isEmpty(xRequestId)) {
			param[0] = ErrorParamEnum.X_REQUEST_ID.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (StringUtils.isEmpty(shibIdentitaCodiceFiscale)) {
			param[0] = ErrorParamEnum.SHIB_IDENTITA_CODICEFISCALE.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (StringUtils.isEmpty(xForwardedFor)) {
			param[0] = ErrorParamEnum.X_FORWARDED_FOR.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (StringUtils.isEmpty(xCodiceServizio)) {
			param[0] = ErrorParamEnum.X_CODICE_SERVIZIO.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		return result;
	}

	public List<ErroreDettaglio> validateDomanda(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String numeroRichiesta, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

		// 2. Verifica parametri in input (Criteri di validazione della richiesta)
		// 2a) Obbligatorieta'
		List<ErroreDettaglio> result = new ArrayList<>();

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		// inserimento controllo specifico citid
		String[] param = new String[1];
		if (!Util.isValorizzato(numeroRichiesta)) {
			param[0] = ErrorParamEnum.NUMERODOMANDA.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		return result;
	}

	public List<ErroreDettaglio> validateLettera(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String numeroRichiesta, String tipoLettera,
			SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

		// 2. Verifica parametri in input (Criteri di validazione della richiesta)
		// 2a) Obbligatorieta'
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
			SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

		// 2. Verifica parametri in input (Criteri di validazione della richiesta)
		// 2a) Obbligatorieta'
		List<ErroreDettaglio> result = new ArrayList<>();

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		// inserimento controllo specifico citid
		String[] param = new String[1];
		if (!Util.isValorizzato(numeroRichiesta)) {
			param[0] = ErrorParamEnum.NUMERODOMANDA.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (!Util.isValorizzato(tipoNotifica)) {
			param[0] = ErrorParamEnum.TIPONOTIFICA.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		return result;
	}

	public List<ErroreDettaglio> validateContatti(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

		// 2. Verifica parametri in input (Criteri di validazione della richiesta)
		// 2a) Obbligatorieta'
		List<ErroreDettaglio> result = new ArrayList<>();

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		return result;
	}

	public List<ErroreDettaglio> validateSegnalazioni(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, ModelRicezioneSegnalazione segnalazione,
			SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

		List<ErroreDettaglio> result = new ArrayList<ErroreDettaglio>();

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		if (!Util.isValorizzato(segnalazione.getNumeroDomanda())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.NUMERODOMANDA.getCode()));
		}

		if (!Util.isValorizzato(segnalazione.getCodiceNotifica())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.CODNOTIFICA.getCode()));
		}

		if (!segnalazione.getCodiceNotifica().equalsIgnoreCase(Constants.DEC) &&
				!segnalazione.getCodiceNotifica().equalsIgnoreCase(Constants.CR) &&
				!segnalazione.getCodiceNotifica().equalsIgnoreCase(Constants.RIN) &&
				!segnalazione.getCodiceNotifica().equalsIgnoreCase(Constants.DTCONC) &&
				!segnalazione.getCodiceNotifica().equalsIgnoreCase(Constants.PAM) &&
				!segnalazione.getCodiceNotifica().equalsIgnoreCase(Constants.ISEE)) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), ErrorParamEnum.CODNOTIFICA.getCode()));
		}

		if (segnalazione.getCodiceNotifica().equalsIgnoreCase(Constants.DEC) ||
				segnalazione.getCodiceNotifica().equalsIgnoreCase(Constants.CR) ||
				segnalazione.getCodiceNotifica().equalsIgnoreCase(Constants.RIN) ||
				segnalazione.getCodiceNotifica().equalsIgnoreCase(Constants.DTCONC)) {
			if (!Util.isValorizzato(segnalazione.getData())) {
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.DATANOTIFICA.getCode()));
			}
		}

		if (segnalazione.getCodiceNotifica().equalsIgnoreCase("PAM") ||
				segnalazione.getCodiceNotifica().equalsIgnoreCase("ISEE")) {
			if (!Util.isValorizzato(segnalazione.getDescrizioneNotifica())) {
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.DESCRNOTIFICA.getCode()));
			}
		}

		return result;
	}
}
