/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonorescallban.util.rest.validator;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import it.csi.buonores.buonorescallban.dto.ErroreDettaglio;
import it.csi.buonores.buonorescallban.dto.PayloadEsitoAcquisizione;
import it.csi.buonores.buonorescallban.dto.PayloadRevDomanda;
import it.csi.buonores.buonorescallban.exception.DatabaseException;
import it.csi.buonores.buonorescallban.util.Constants;
import it.csi.buonores.buonorescallban.util.Util;
import it.csi.buonores.buonorescallban.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonorescallban.util.enumerator.ErrorParamEnum;

@Service
public class ValidateGenericImpl extends BaseValidate {

	public List<ErroreDettaglio> validateAcquisizione(String xRequestId, PayloadEsitoAcquisizione esito,
			SecurityContext securityContext, HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest)
			throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

		List<ErroreDettaglio> result = new ArrayList<ErroreDettaglio>();

		if (StringUtils.isEmpty(xRequestId)) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.X_REQUEST_ID.getCode()));
		}

		if (esito != null) {
			if (esito.getEsitoAcquisizione() != null) {
				if (!Util.isValorizzato(esito.getEsitoAcquisizione().getEsito())) {
					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.ESITO_OK_KO.getCode()));
				} else if (esito.getEsitoAcquisizione().getEsito().equalsIgnoreCase(Constants.KO)) {
                    if (!Util.isValorizzato(esito.getEsitoAcquisizione().getUuid())) {
                        result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.MESSAGGE_UUID.getCode()));
                    }
                    if (esito.getEsitoAcquisizione().getErrore() != null) {
						if (!Util.isValorizzato(esito.getEsitoAcquisizione().getErrore().getCodice())) {
							result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.CODICE_ERRORE.getCode()));
						}
						if (!Util.isValorizzato(esito.getEsitoAcquisizione().getErrore().getMessaggio())) {
							result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.MESSAGGIO_ERRORE.getCode()));
						}
					} else {
						result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.ERRORE.getCode()));
					}
				} else if (esito.getEsitoAcquisizione().getEsito().equalsIgnoreCase(Constants.OK)) {
					if (!Util.isValorizzato(esito.getEsitoAcquisizione().getUuid())) {
						result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.MESSAGGE_UUID.getCode()));
					}
				}
				// esito non ok o ko
				else {
					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.MANCA_ESITO.getCode()));
				}
			} else {
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.ESITOACQUISIZIONE.getCode()));
			}
		} else {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.ESITO.getCode()));
		}

		return result;
	}
	
	public List<ErroreDettaglio> validateRevoca(String xRequestId, PayloadRevDomanda esito,
			SecurityContext securityContext, HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest)
			throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

		List<ErroreDettaglio> result = new ArrayList<ErroreDettaglio>();

		// inserimento controllo specifico citid
		if (StringUtils.isEmpty(xRequestId)) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.X_REQUEST_ID.getCode()));
		}

		if (esito != null) {
			if (esito.getEsitoAcquisizione() != null) {
				if (!Util.isValorizzato(esito.getEsitoAcquisizione().getCfBeneficiario())) {
					result.add(
							getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.BENEFICIARIO.getCode()));
				}
				if (!Util.isValorizzato(esito.getEsitoAcquisizione().getCfRichiedente())) {
					result.add(
							getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.RICHIEDENTE.getCode()));
				}
				if (!Util.isValorizzato(esito.getEsitoAcquisizione().getDataDecorrenzaRevoca())) {
					result.add(
							getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.DATAREVOCA.getCode()));
				}
				if (!Util.isValorizzato(esito.getEsitoAcquisizione().getNota())) {
					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.NOTA.getCode()));
				}
			} else {
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(),
						ErrorParamEnum.ESITOACQUISIZIONE.getCode()));
			}
		} else {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.ESITO.getCode()));
		}
		return result;
	}

}
