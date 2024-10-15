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
import it.csi.buonores.buonoresbff.dto.ModelAllegato;
import it.csi.buonores.buonoresbff.dto.ModelRichiesta;
import it.csi.buonores.buonoresbff.dto.custom.ModelRichiesteExt;
import it.csi.buonores.buonoresbff.exception.DatabaseException;
import it.csi.buonores.buonoresbff.exception.ResponseErrorException;
import it.csi.buonores.buonoresbff.integration.dao.custom.RichiesteDao;
import it.csi.buonores.buonoresbff.integration.dao.custom.SportelliDao;
import it.csi.buonores.buonoresbff.util.Constants;
import it.csi.buonores.buonoresbff.util.enumerator.CodeErrorEnum;

@Service
public class GetRichiesteService extends BaseService {

	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	SportelliDao sportelliDao;

	public Response getRichieste(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Errore error = null;
		List<ModelRichiesteExt> richieste = new ArrayList<ModelRichiesteExt>();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();

		try {
			checkValidateRichieste(xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale,
					securityContext, httpHeaders, httpRequest);

			richieste = richiesteDao.selectRichieste(shibIdentitaCodiceFiscale);

			return Response.ok().entity(richieste).build();

		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}
		return error.generateResponseError();
	}

	public Response getNumeroRichiesta(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = null;
		ModelRichiesta richiesta = new ModelRichiesta();
		List<ErroreDettaglio> listError = new ArrayList<ErroreDettaglio>();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		long tempoPartenza = System.currentTimeMillis();
		log.info("richiesteget - BEGIN time: ");

		try {
			// validate stessa delle decodifica
			long tempoValidate = System.currentTimeMillis();

			checkValidateRichieste(xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale,
					securityContext, httpHeaders, httpRequest);

			log.info("richiesteget - tempoValidete: " + System.currentTimeMillis() + " millis: "
					+ (tempoPartenza - tempoValidate));

			richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			if (richiesta != null) {
				if (!richiesta.getRichiedente().getCf().equals(shibIdentitaCodiceFiscale)) {
					generateResponseErrorException(CodeErrorEnum.ERR12, HttpStatus.BAD_REQUEST,
							"Il codice fiscale del richiedente della domanda non corrisponde", numeroRichiesta);
				}

				if (richiesta.getStato().equalsIgnoreCase(Constants.BOZZA)) {
					boolean inCorso = sportelliDao.isSportelliCorrente(richiesta.getNumero());
					if (!inCorso) {
						validateGeneric.checkSportelloCorrente(listError, inCorso);
						generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST,
								"sportello chiuso");
					}
				}

				List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();
				allegati = richiesteDao.selectAllegatiFromNumeroRichiesta(numeroRichiesta);

				long tempoAllegati = System.currentTimeMillis();
				log.info("richiesteget - tempoAllegati: " + System.currentTimeMillis() + " millis: "
						+ (tempoValidate - tempoAllegati));

				if (!allegati.isEmpty()) {
					richiesta.setAllegati(allegati);
				}

				List<String> rettificare = new ArrayList<String>();
				rettificare = richiesteDao.selectRettificareFromNumeroRichiesta(numeroRichiesta);

				long tempoRettificare = System.currentTimeMillis();
				log.info("richiesteget - tempoAllegati: " + System.currentTimeMillis() + " millis: "
						+ (tempoAllegati - tempoRettificare));

				if (!rettificare.isEmpty())
					richiesta.setRettificare(rettificare);

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
