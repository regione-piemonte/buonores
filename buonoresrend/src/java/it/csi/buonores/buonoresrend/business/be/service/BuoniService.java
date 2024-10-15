/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresrend.business.be.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonoresrend.business.be.service.base.BaseService;
import it.csi.buonores.buonoresrend.dto.Errore;
import it.csi.buonores.buonoresrend.dto.ErroreDettaglio;
import it.csi.buonores.buonoresrend.dto.ModelBuono;
import it.csi.buonores.buonoresrend.exception.DatabaseException;
import it.csi.buonores.buonoresrend.exception.ResponseErrorException;
import it.csi.buonores.buonoresrend.integration.dao.custom.BuoniDao;
import it.csi.buonores.buonoresrend.integration.dao.custom.RichiesteDao;
import it.csi.buonores.buonoresrend.util.validator.impl.ValidateGenericImpl;

@Service
public class BuoniService extends BaseService {

	@Autowired
	BuoniDao buoniDao;
	@Autowired
	RichiesteDao richiesteDao;
	@Autowired
	ValidateGenericImpl validateGeneric;

	public Response getBuoni(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			List<ErroreDettaglio> listError = new ArrayList<ErroreDettaglio>();
			validateGeneric.commonCheck(listError, shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(Errore.generateErrore(HttpStatus.BAD_REQUEST, listError),
						"errore in validate");
			}
			
			List<ModelBuono> buoni = buoniDao.getBuoni();
			return Response.ok().entity(buoni).build();
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}
		return error.generateResponseError();
	}
}
