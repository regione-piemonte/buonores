/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.business.be.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonoresbff.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbff.dto.Errore;
import it.csi.buonores.buonoresbff.dto.ErroreDettaglio;
import it.csi.buonores.buonoresbff.dto.ModelIsee;
import it.csi.buonores.buonoresbff.dto.ModelIseeBuono;
import it.csi.buonores.buonoresbff.dto.custom.ModelRichiestaExt;
import it.csi.buonores.buonoresbff.exception.DatabaseException;
import it.csi.buonores.buonoresbff.exception.ResponseErrorException;
import it.csi.buonores.buonoresbff.integration.dao.custom.IseeDao;
import it.csi.buonores.buonoresbff.integration.dao.custom.RichiesteDao;
import it.csi.buonores.buonoresbff.util.Util;
import it.csi.buonores.buonoresbff.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoresbff.util.enumerator.IseeStatoEnum;
import it.csi.buonores.buonoresbff.util.validator.impl.ValidateGenericImpl;

@Service
public class IseeService extends BaseService {

	@Autowired
	IseeDao iseeDao;

	@Autowired
	RichiesteDao richiesteDao;

	private void checkValidateConformitaIsee(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio,
			String shibIdentitaCodiceFiscale, ModelIsee isee) throws DatabaseException, ResponseErrorException {
		List<ErroreDettaglio> listError = validateGeneric.validateAddConformitaIsee(numeroRichiesta,
				xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale, isee);
		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "errore in validate");
	}

	private void checkValidateGetConformitaIsee(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio,
			String shibIdentitaCodiceFiscale) throws DatabaseException, ResponseErrorException {
		List<ErroreDettaglio> listError = validateGeneric.validateGetConformitaIsee(numeroRichiesta,
				xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale);
		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "errore in validate");
	}

	private void validateCodFiscaleAndShibIden(String cfRichiedente, String shibIdentitaCodiceFiscale)
			throws DatabaseException, ResponseErrorException {
		List<ErroreDettaglio> listError = new ArrayList<ErroreDettaglio>();
		validateGeneric.checkCodFiscaleAndShibIden(listError, cfRichiedente, shibIdentitaCodiceFiscale);
		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST,
				"Il codice fiscale del richiedente della domanda non corrisponde");
	}

	public Response addConformitaIsee(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, ModelIsee isee, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		Errore error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();

		try {
			checkValidateConformitaIsee(numeroRichiesta, xRequestId, xForwardedFor,
					xCodiceServizio, shibIdentitaCodiceFiscale, isee);

			ModelRichiestaExt richiesta = richiesteDao.selectNumeroRichiestaExt(numeroRichiesta);
			if (richiesta != null) {
				validateCodFiscaleAndShibIden(richiesta.getRichiedente().getCf(), shibIdentitaCodiceFiscale);
			} else {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND,
						"domanda " + numeroRichiesta + " inesistente", numeroRichiesta);
			}

			// prendo isee e verifico se esiste per anno passato
			ModelIseeBuono iseeOld = iseeDao.selectIsee(numeroRichiesta, isee.getAnno());
			// se esiste record solo operatore e no cittadino update
			if (iseeOld != null &&
					Util.isValorizzato(iseeOld.getIseeVerificaEsitoCod())
					&& iseeOld.getIseeVerificaEsitoCod().equalsIgnoreCase(IseeStatoEnum.RESPINTO.getStato())) {
				// devo chiudere record operatore respinto ed inserire nuovo record cittadino
				// iseeDao.updateIseeConformeCittadino(iseeOld.getIseeId());
				iseeDao.updateIseeConforme(iseeOld.getIseeId());
				iseeDao.insertIseeConforme(isee.getAnno(), numeroRichiesta, shibIdentitaCodiceFiscale);
			}
			if (iseeOld != null && iseeOld.isIseeConforme() == null &&
					Util.isValorizzato(iseeOld.getIseeVerificaEsitoCod())
					&& !iseeOld.getIseeVerificaEsitoCod().equalsIgnoreCase(IseeStatoEnum.RESPINTO.getStato())) {
				// vado in update del record mettendo true per cittadino non cambio utente
				// altrimendi vedrei cf cittadino
				iseeDao.updateIseeConformeCittadino(iseeOld.getIseeId());
			} else if (iseeOld == null) {
				iseeDao.insertIseeConforme(isee.getAnno(), numeroRichiesta, shibIdentitaCodiceFiscale);
			}
			List<ModelIsee> iseeinDb = new ArrayList<ModelIsee>();

			iseeinDb = iseeDao.selectIsee(numeroRichiesta);

			return Response.status(200).entity(iseeinDb).build();

		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	public Response getConformitaIsee(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		Errore error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			checkValidateGetConformitaIsee(numeroRichiesta, xRequestId, xForwardedFor,
					xCodiceServizio, shibIdentitaCodiceFiscale);

			ModelRichiestaExt richiesta = richiesteDao.selectNumeroRichiestaExt(numeroRichiesta);
			if (richiesta != null) {
				validateCodFiscaleAndShibIden(richiesta.getRichiedente().getCf(), shibIdentitaCodiceFiscale);
			} else {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND,
						"domanda " + numeroRichiesta + " inesistente", numeroRichiesta);
			}

			List<ModelIsee> isee = new ArrayList<ModelIsee>();

			isee = iseeDao.selectIsee(numeroRichiesta);

			return Response.status(200).entity(isee).build();

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
