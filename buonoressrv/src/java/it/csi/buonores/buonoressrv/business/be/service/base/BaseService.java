/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.business.be.service.base;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.WebServiceException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import it.csi.buonores.buonoressrv.dto.Errore;
import it.csi.buonores.buonoressrv.dto.ErroreDettaglio;
import it.csi.buonores.buonoressrv.exception.DatabaseException;
import it.csi.buonores.buonoressrv.exception.ResponseErrorException;
import it.csi.buonores.buonoressrv.util.LoggerUtil;
import it.csi.buonores.buonoressrv.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoressrv.util.validator.impl.ValidateGenericImpl;

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
	
	protected Errore handleWebServiceException(String metodo, WebServiceException e) {
		String messageError = "Errore riguardante ws: ";
		return handleError(metodo, CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR, e, messageError);
	}

	protected Errore handleException(String metodo, Exception e) {
		String messageError = "Errore generico: ";
		return handleError(metodo, CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR, e, messageError);
	}
	
	protected Errore handleError(String metodo, CodeErrorEnum codErrore, HttpStatus status, Exception e,
			String messageError) {
		List<ErroreDettaglio> listerrorservice = new ArrayList<ErroreDettaglio>();
		logError(metodo, messageError, e.getMessage());
		logError(metodo, messageError, e);
		listerrorservice.add(validateGeneric.getValueGenericError(codErrore.getCode(), e.getMessage()));
		return Errore.generateErrore(status, listerrorservice);
	}
	
	protected Errore createGenericServerError(String metodo, String messaggio) {
		logError(metodo, "Errore generico " + messaggio, null);
		List<ErroreDettaglio> listerrorservice = new ArrayList<ErroreDettaglio>();
		listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), "Errore " + messaggio));
		return Errore.generateErrore(HttpStatus.INTERNAL_SERVER_ERROR, listerrorservice);
	}

	protected void generateResponseErrorException(String pamaramIn,CodeErrorEnum codErrore,HttpStatus status,String errorMessage)  throws ResponseErrorException{
		List<ErroreDettaglio> listerrorservice = new ArrayList<ErroreDettaglio>();
		listerrorservice.add(validateGeneric.getValueGenericError(codErrore.getCode(), pamaramIn));
		throw new ResponseErrorException(
				Errore.generateErrore(status, listerrorservice),
				errorMessage);
	}
	
	protected Errore handleHttpClientErrorException(String metodo, CodeErrorEnum codErrore, HttpStatus status, HttpClientErrorException e) {
		String messaggioErrore = StringUtils.isNotBlank(e.getResponseBodyAsString()) ? e.getResponseBodyAsString() : e.getMessage();
		List<ErroreDettaglio> listerrorservice = new ArrayList<ErroreDettaglio>();
		logError(metodo, messaggioErrore, e.getMessage());
		logError(metodo, messaggioErrore, e);
		listerrorservice.add(validateGeneric.getValueGenericError(codErrore.getCode(), messaggioErrore));
		return Errore.generateErrore(status, listerrorservice);
	}
}
