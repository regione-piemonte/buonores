/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresstarda.util.validator.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.springframework.stereotype.Service;

import it.csi.buonores.buonoresstarda.dto.ErroreDettaglio;
import it.csi.buonores.buonoresstarda.dto.EsitoSmistaDocumentoRequest;
import it.csi.buonores.buonoresstarda.exception.DatabaseException;
import it.csi.buonores.buonoresstarda.util.Util;
import it.csi.buonores.buonoresstarda.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoresstarda.util.enumerator.ErrorParamEnum;

@Service
public class ValidateGenericImpl extends BaseValidate {
	//	2. Verifica parametri in input (Criteri di validazione della richiesta)
	//	2a) Obbligatorieta'
	public List<ErroreDettaglio> validateSmista(EsitoSmistaDocumentoRequest body, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

		List<ErroreDettaglio> result = new ArrayList<>();

		// inserimento controllo specifico citid
		if (body.getEsitoSmistaDocumento() != null) {
			if (body.getEsitoSmistaDocumento().getEsito()!=null) {
				if (!Util.isValorizzato(body.getEsitoSmistaDocumento().getEsito().getMessageUUID())) {
					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.MESSAGGE_UUID.getCode()));
				}

				if (!Util.isValorizzato(body.getEsitoSmistaDocumento().getEsito().getIdDocumentoFruitore())) {
					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.IDDOCUMENTOFRUITORE.getCode()));
				}
			}
			else {
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.ESITO.getCode()));
			}
		}
		else {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.ESITOSMISTA.getCode()));
		}
		
		return result;
	}
}
