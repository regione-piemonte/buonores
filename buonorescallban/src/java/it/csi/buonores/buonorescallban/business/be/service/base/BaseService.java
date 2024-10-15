/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonorescallban.business.be.service.base;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import it.csi.buonores.buonorescallban.dto.Errore;
import it.csi.buonores.buonorescallban.dto.ErroreDettaglio;
import it.csi.buonores.buonorescallban.exception.DatabaseException;
import it.csi.buonores.buonorescallban.exception.ResponseErrorException;
import it.csi.buonores.buonorescallban.util.LoggerUtil;
import it.csi.buonores.buonorescallban.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonorescallban.util.rest.validator.ValidateGenericImpl;

public abstract class BaseService extends LoggerUtil {
	@Autowired
	protected ValidateGenericImpl validateGeneric;

	protected Errore handleResponseErrorException(String metodo, ResponseErrorException e) {
		logError(metodo, "Errore generico response:", e);
		return e.getResponseError();
	}

	protected Errore handleDatabaseException(String metodo, DatabaseException e) {
		String messageError = "Errore riguardante database: ";
		return handleError(metodo, CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR, e, messageError);
	}

	protected Errore handleException(String metodo, Exception e) {
		String messageError = "Errore generico: ";
		return handleError(metodo, CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR, e, messageError);
	}

	private Errore handleError(String metodo, CodeErrorEnum codErrore, HttpStatus status, Exception e,
			String messageError) {
		List<ErroreDettaglio> listerrorservice = new ArrayList<ErroreDettaglio>();
		logError(metodo, messageError, e.getMessage());
		logError(metodo, messageError, e);
		listerrorservice.add(validateGeneric.getValueGenericError(codErrore.getCode(),  e.getMessage()));
		return Errore.generateErrore(status, listerrorservice);
	}

	protected void generateResponseErrorException(CodeErrorEnum codErrore, HttpStatus status, String errorMessage, String... pamaramIn) throws DatabaseException, ResponseErrorException{
		List<ErroreDettaglio> listerrorservice = new ArrayList<ErroreDettaglio>();
		listerrorservice.add(validateGeneric.getValueGenericError(codErrore.getCode(), pamaramIn));

		throw new ResponseErrorException(
				Errore.generateErrore(status, listerrorservice),
				errorMessage);
	}
	
	protected void generateResponseErrorExceptionForValidate(List<ErroreDettaglio> listerrorservice, HttpStatus status,String errorMessage)  throws DatabaseException, ResponseErrorException{
		if (!listerrorservice.isEmpty()) {
			throw new ResponseErrorException(
				Errore.generateErrore(status, listerrorservice),
				errorMessage);
		}
	}
}

