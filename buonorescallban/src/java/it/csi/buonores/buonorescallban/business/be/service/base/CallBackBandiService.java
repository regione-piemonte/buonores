/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonorescallban.business.be.service.base;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonorescallban.dto.DatiBuono;
import it.csi.buonores.buonorescallban.dto.Errore;
import it.csi.buonores.buonorescallban.dto.ErroreDettaglio;
import it.csi.buonores.buonorescallban.dto.PayloadEsitoAcquisizione;
import it.csi.buonores.buonorescallban.dto.PayloadRevDomanda;
import it.csi.buonores.buonorescallban.dto.custom.ModelDomandaExt;
import it.csi.buonores.buonorescallban.exception.DatabaseException;
import it.csi.buonores.buonorescallban.exception.ResponseErrorException;
import it.csi.buonores.buonorescallban.integration.dao.custom.LogBandiDao;
import it.csi.buonores.buonorescallban.util.Constants;
import it.csi.buonores.buonorescallban.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonorescallban.util.rest.validator.ValidateGenericImpl;

@Service
public class CallBackBandiService extends BaseService {

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	LogBandiDao logBandiDao;
	
	@Autowired
	ServizioRestService servizioRestService;

	public Response acquisizionePost(String xRequestId, String numeroDomanda,
			PayloadEsitoAcquisizione payloadEsitoAcquisizione, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String message = "CALLBACK_INVIO_DOMANDA_POST";
		return acquisizione(message, xRequestId, numeroDomanda, payloadEsitoAcquisizione, securityContext, httpHeaders,
				httpRequest);
	}

	public Response acquisizionePut(String xRequestId, String numeroDomanda,
			PayloadEsitoAcquisizione payloadEsitoAcquisizione, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String message = "CALLBACK_INVIO_DOMANDA_PUT";
		return acquisizione(message, xRequestId, numeroDomanda, payloadEsitoAcquisizione, securityContext, httpHeaders,
				httpRequest);
	}

	private Response acquisizione(String message, String xRequestId, String numeroDomanda,
			PayloadEsitoAcquisizione payloadEsitoAcquisizione, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		Errore error = new Errore();
		List<ErroreDettaglio> listerrorservice = new ArrayList<ErroreDettaglio>();
		try {
			listerrorservice = validateGeneric.validateAcquisizione(xRequestId, payloadEsitoAcquisizione,
					securityContext, httpHeaders, httpRequest);
			generateResponseErrorExceptionForValidate(listerrorservice, HttpStatus.BAD_REQUEST, "errore in validate");

			// recupero id bandi se esiste buono creato
			String stato = "'" + Constants.CREATO + "'";
			DatiBuono buono = logBandiDao.selectBuono(numeroDomanda, stato);
			if (buono != null) {
				if (payloadEsitoAcquisizione.getEsitoAcquisizione().getEsito().equalsIgnoreCase(Constants.OK)) {
					logBandiDao.insertLogBandi(numeroDomanda, buono.getBuonoId(),
							payloadEsitoAcquisizione.getEsitoAcquisizione().getUuid(),
							payloadEsitoAcquisizione.getEsitoAcquisizione().getEsito(), null, null,
							message, null);
					// devo aggiornare la tabella dei buoni con uuid
					logBandiDao.updateBuonoConUUID(numeroDomanda, buono.getBuonoId(),
							payloadEsitoAcquisizione.getEsitoAcquisizione().getUuid());
					// chiudo il record creato del buono e apro il record ATTIVO
					logBandiDao.chiudoStatoBuono(buono.getBuonoId(), Constants.CREATO);
					logBandiDao.aproRecordBuonoStato(buono.getBuonoId(), Constants.ATTIVO, null, null);
				} else {
					logBandiDao.insertLogBandi(numeroDomanda, buono.getBuonoId(),
							payloadEsitoAcquisizione.getEsitoAcquisizione().getUuid(),
							payloadEsitoAcquisizione.getEsitoAcquisizione().getEsito(),
							payloadEsitoAcquisizione.getEsitoAcquisizione().getErrore().getCodice(),
							payloadEsitoAcquisizione.getEsitoAcquisizione().getErrore().getMessaggio(),
							message, null);
				}
			} else {
				logError(metodo, "Non trovato il buono della domanda " + numeroDomanda);
				generateResponseErrorException(CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR,
						"errore buono non trovato", "Non trovato il buono della domanda " + numeroDomanda);
			}
			return Response.status(201).entity("OK").build();
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}
		return error.generateResponseError();
	}
	
	public Response revocaPut(String xRequestId, String numeroDomanda, PayloadRevDomanda payloadRevDomanda,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		Errore error = new Errore();
		List<ErroreDettaglio> listerrorservice = new ArrayList<ErroreDettaglio>();
		try {
			listerrorservice = validateGeneric.validateRevoca(xRequestId, payloadRevDomanda,
					securityContext, httpHeaders, httpRequest);
			generateResponseErrorExceptionForValidate(listerrorservice, HttpStatus.BAD_REQUEST, "errore in validate");

			// verifico se c'è coerenza tra domanda beneficiario e richiedente
			ModelDomandaExt richiesta = new ModelDomandaExt();
			richiesta = logBandiDao.selectNumeroRichiesta(numeroDomanda);
			if (richiesta == null) {
				logError(metodo, "Errore domanda non trovata");
				generateResponseErrorException(CodeErrorEnum.ERR03, HttpStatus.NOT_FOUND, 
						"Errore domanda non trovata", "Errore domanda non trovata");
			}
			if (!richiesta.getCfBeneficiario()
					.equalsIgnoreCase(payloadRevDomanda.getEsitoAcquisizione().getCfBeneficiario())
					|| !richiesta.getCfRichiedente()
							.equalsIgnoreCase(payloadRevDomanda.getEsitoAcquisizione().getCfRichiedente())) {
				// incoerenza tra i dati
				// errore
				logError(metodo, "Dati revoca bandi diversi da quelli della domanda");
				generateResponseErrorException(CodeErrorEnum.ERR02, HttpStatus.BAD_REQUEST, 
						"Dati revoca bandi diversi da quelli della domanda", "Dati revoca bandi diversi da quelli della domanda");
			}
			
			// recupero id bandi
			String stati = "'" + Constants.ATTIVO + "','" + Constants.RINUNCIATO + "'";
			DatiBuono buono = logBandiDao.selectBuono(numeroDomanda, stati);
			if (buono != null) {
				logBandiDao.insertLogBandi(numeroDomanda, buono.getBuonoId(), null, "OK", null, null,
						"CALLBACK_REVOCA_PUT", payloadRevDomanda.getEsitoAcquisizione().toString());
				// cambia lo stato del buono in revocato
				logBandiDao.chiudoStatoBuono(buono.getBuonoId(), buono.getStato());
				logBandiDao.aproRecordBuonoStato(buono.getBuonoId(), Constants.REVOCATO,
						payloadRevDomanda.getEsitoAcquisizione().getDataDecorrenzaRevoca(),
						payloadRevDomanda.getEsitoAcquisizione().getNota());
				// manda solo mex su portale
				servizioRestService.getInviaNotifica(xRequestId, securityContext, httpHeaders, httpRequest,
						numeroDomanda, Constants.REVOCA_BUONO);
				return Response.status(201).entity("OK").build();
			} else {
				// errore
				logError(metodo, "Non trovato il buono attivo della domanda ", null);
				generateResponseErrorException(CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR, 
						"Non trovato il buono attivo della domanda", "Non trovato il buono attivo della domanda");
			}
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
