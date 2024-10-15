/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresrend.business.be.service;

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
import it.csi.buonores.buonoresrend.dto.ModelRichiesta;
import it.csi.buonores.buonoresrend.exception.DatabaseException;
import it.csi.buonores.buonoresrend.exception.ResponseErrorException;
import it.csi.buonores.buonoresrend.integration.dao.custom.RichiesteDao;
import it.csi.buonores.buonoresrend.util.enumerator.CodeErrorEnum;

@Service
public class GetDomandeService extends BaseService {

	@Autowired
	RichiesteDao richiesteDao;
	


	public Response domandaByNumeroDomandaGet(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = null;
		ModelRichiesta richiesta = new ModelRichiesta();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		long tempoPartenza = System.currentTimeMillis();
		log.info("richiesteget - BEGIN time: ");

		try {
			// validate stessa delle decodifica
			long tempoValidate = System.currentTimeMillis();

			checkValidateRichieste(xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale,
					securityContext, httpHeaders, httpRequest);

			log.info("domandaByNumeroDomandaGet - tempoValidete: " + System.currentTimeMillis() + " millis: "
					+ (tempoPartenza - tempoValidate));

			richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			if (richiesta != null) {
//				IN CASO SERVONO GLI ALLEGATI
//				List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();
//				allegati = richiesteDao.selectAllegatiFromNumeroRichiesta(numeroRichiesta);

//				long tempoAllegati = System.currentTimeMillis();
//				log.info("richiesteget - tempoAllegati: " + System.currentTimeMillis() + " millis: "
//						+ (tempoValidate - tempoAllegati));
//
//				if (!allegati.isEmpty()) {
//					richiesta.setAllegati(allegati);
//				}
				

			} else {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND,
						"domanda inesistente", numeroRichiesta);
			}

			return Response.ok().entity(richiesta).build();

		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}
		return error.generateResponseError();
	}

	private void checkValidateRichieste(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws DatabaseException, ResponseErrorException {
		// validate stessa delle decodifica
		List<ErroreDettaglio> listError = validateGeneric.validateGeneric(shibIdentitaCodiceFiscale, xRequestId,
				xForwardedFor, xCodiceServizio, securityContext, httpHeaders, httpRequest);
		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "errore in validate");
	}
}
