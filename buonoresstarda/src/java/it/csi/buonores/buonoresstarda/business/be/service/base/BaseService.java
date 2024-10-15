/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresstarda.business.be.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import it.csi.buonores.buonoresstarda.dto.EsitoStep;
import it.csi.buonores.buonoresstarda.dto.ResultType;
import it.csi.buonores.buonoresstarda.exception.DatabaseException;
import it.csi.buonores.buonoresstarda.util.Constants;
import it.csi.buonores.buonoresstarda.util.LoggerUtil;
import it.csi.buonores.buonoresstarda.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoresstarda.util.validator.impl.ValidateGenericImpl;

public abstract class BaseService extends LoggerUtil {
	
	@Autowired
	protected ValidateGenericImpl validateGeneric;
	
	protected EsitoStep handleDatabaseException(String metodo, DatabaseException e) {
		String messageError = (e.getMessage() != null) ? (metodo + " Errore riguardante database")
				: (metodo + " " + " Null pointer exception");
		return handleError(metodo, CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR, e, messageError, CodeErrorEnum.ERR17.getCode());
	}

	protected EsitoStep handleException(String metodo, Exception e) {
		String messageError = (e.getMessage() != null) ? (metodo + e.getMessage())
				: (metodo + " " + " Null pointer exception");
		return handleError(metodo, CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR, e, messageError, CodeErrorEnum.ERR16.getCode());
	}
	
	private EsitoStep handleError(String metodo, CodeErrorEnum codErrore, HttpStatus status, Exception e,
			String messageError, String code) {
		logError(metodo, messageError, e);
		ResultType esitocallback = new ResultType();
		esitocallback.setCodice(Constants.KO_APPLICATIVI);
		esitocallback.setMessaggio(validateGeneric.getValueGenericErrorStardas(code, messageError));
		EsitoStep esitoStep = new EsitoStep();
		esitoStep.setEsito(esitocallback);
		return esitoStep;
	}
	
}
