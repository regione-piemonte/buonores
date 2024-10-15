/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.business.be.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.buonores.buonoresbo.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbo.dto.Errore;
import it.csi.buonores.buonoresbo.dto.ErroreDettaglio;
import it.csi.buonores.buonoresbo.dto.ModelEsitoIsee;
import it.csi.buonores.buonoresbo.dto.ModelIseeBuono;
import it.csi.buonores.buonoresbo.dto.ModelRicezioneSegnalazione;
import it.csi.buonores.buonoresbo.dto.UserInfo;
import it.csi.buonores.buonoresbo.dto.custom.ModelBandiMessage;
import it.csi.buonores.buonoresbo.dto.custom.ModelBuono;
import it.csi.buonores.buonoresbo.exception.DatabaseException;
import it.csi.buonores.buonoresbo.exception.ResponseErrorException;
import it.csi.buonores.buonoresbo.filter.IrideIdAdapterFilter;
import it.csi.buonores.buonoresbo.integration.dao.custom.BuonoDao;
import it.csi.buonores.buonoresbo.integration.dao.custom.IseeDao;
import it.csi.buonores.buonoresbo.util.Constants;
import it.csi.buonores.buonoresbo.util.Util;
import it.csi.buonores.buonoresbo.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoresbo.util.enumerator.IseeStatoEnum;
import it.csi.buonores.buonoresbo.util.rest.ResponseRest;
import it.csi.buonores.buonoresbo.util.validator.impl.ValidateGenericImpl;

@Service
public class IseeService extends BaseService {

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	IseeDao iseeDao;

	@Autowired
	BuonoDao buonoDao;

	@Autowired
	ServizioRestService servizioRestService;

	@Autowired
	ServizioRestBandiService restbase;

	private void checkValidateBuonoIsee(ModelIseeBuono isee) throws DatabaseException, ResponseErrorException {
		List<ErroreDettaglio> listError = validateGeneric.validateBuonoIsee(isee);
		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "errore in validate");
	}

	public Response addConformitaIsee(String numeroRichiesta, ModelIseeBuono isee, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			checkValidateBuonoIsee(isee);

			List<ModelEsitoIsee> esitiIsee = new ArrayList<ModelEsitoIsee>();
			esitiIsee = iseeDao.selectListaEsitoIsee();
			boolean trovato = false;
			for (ModelEsitoIsee esito : esitiIsee) {
				if (esito.getCodEsito().equalsIgnoreCase(isee.getIseeVerificaEsitoCod())) {
					trovato = true;
					break;
				}
			}

			if (!trovato) {
				generateResponseErrorException(CodeErrorEnum.ERR02, HttpStatus.BAD_REQUEST,
						"esito isee " + isee.getIseeVerificaEsitoCod() + " inesistente per domanda " + numeroRichiesta
								+ " per anno " + isee.getIseeAnno(),
						"Esito Isee");
			}

			ModelBuono buono = buonoDao.selectBuono(numeroRichiesta);
			if (buono == null) {
				generateResponseErrorException(CodeErrorEnum.ERR05, HttpStatus.NOT_FOUND,
						"buono inesistente per domanda " + numeroRichiesta, numeroRichiesta);
			}

			ModelIseeBuono idIsee = new ModelIseeBuono();
			idIsee = iseeDao.selectIsee(numeroRichiesta, isee.getIseeAnno());

			// verifico se i dati sono uguali allora errore di salvataggio
			if (idIsee != null && isee.getIseeAnno().equalsIgnoreCase(idIsee.getIseeAnno()) &&
					isee.getIseeDataRilascio() == null && idIsee.getIseeDataRilascio() == null &&
					isee.getIseeNote() != null && isee.getIseeNote().equalsIgnoreCase(idIsee.getIseeNote()) &&
					isee.getIseeScadenza() == null && idIsee.getIseeScadenza() == null &&
					isee.getIseeValore().equals(idIsee.getIseeValore()) &&
					isee.getIseeVerificaEsitoCod().equalsIgnoreCase(idIsee.getIseeVerificaEsitoCod())) {
				// nessun dato variato nulla da fare
				generateResponseErrorException(CodeErrorEnum.ERR02, HttpStatus.BAD_REQUEST,
						"isee non salvato dati uguali per domanda " + numeroRichiesta + " per anno "
								+ isee.getIseeAnno(),
						"Nessun dato variato. Salvataggio non effettuato");
			} else if (idIsee != null && isee.getIseeAnno().equalsIgnoreCase(idIsee.getIseeAnno()) &&
					isee.getIseeDataRilascio() != null
					&& isee.getIseeDataRilascio().equals(idIsee.getIseeDataRilascio()) &&
					isee.getIseeNote() != null && isee.getIseeNote().equalsIgnoreCase(idIsee.getIseeNote()) &&
					isee.getIseeScadenza() != null && isee.getIseeScadenza().equals(idIsee.getIseeScadenza()) &&
					isee.getIseeValore().equals(idIsee.getIseeValore()) &&
					isee.getIseeVerificaEsitoCod().equalsIgnoreCase(idIsee.getIseeVerificaEsitoCod())) {
				// nessun dato variato nulla da fare
				generateResponseErrorException(CodeErrorEnum.ERR02, HttpStatus.BAD_REQUEST,
						"isee non salvato dati uguali per domanda " + numeroRichiesta + " per anno "
								+ isee.getIseeAnno(),
						"Nessun dato variato. Salvataggio non effettuato");
			}

			if (isee.getIseeVerificaEsitoCod().equalsIgnoreCase(IseeStatoEnum.NON_CONFORME.getStato())) {
				// devo comunicare a bandi la segalazione di isee non conforme
				ModelRicezioneSegnalazione segnalazione = new ModelRicezioneSegnalazione();
				segnalazione.setCodiceNotifica(Constants.ISEE);
				segnalazione.setData(Util.getDataISO(new Date()));
				segnalazione.setNumeroDomanda(numeroRichiesta);
				segnalazione.setDescrizioneNotifica(isee.getIseeNote());
				ObjectMapper mapper = new ObjectMapper();
				mapper.setSerializationInclusion(Include.NON_EMPTY);
				String jsonSegnalazione = "";
				jsonSegnalazione = mapper.writeValueAsString(segnalazione);
				ResponseRest responserest = restbase.setRicezioneSegnalazioni(jsonSegnalazione, httpHeaders,
						httpRequest, userInfo.getCodFisc());
				if (responserest.getStatusCode() == 200 || responserest.getStatusCode() == 201) {
					ModelBandiMessage message = new ModelBandiMessage();
					message = new ObjectMapper().readValue(responserest.getJson(), ModelBandiMessage.class);
					if (!message.getEsitoServizio().equalsIgnoreCase("OK")) {
						// esco con errore per utente
						generateResponseErrorException(CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR,
								"errore riguardante la comunicazione con bandi per invio delle segnalazioni per domanda numero "
										+ numeroRichiesta + " con stato diverso da OK",
								"Errore riguardante la comunicazione con bandi per invio delle segnalazioni per domanda numero "
										+ numeroRichiesta + " con stato diverso da OK");
					}
				} else {
					generateResponseErrorException(CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR,
							"errore riguardante la comunicazione con bandi per invio delle segnalazioni per domanda numero "
									+ numeroRichiesta + " con stato diverso da 200",
							"Errore riguardante la comunicazione con bandi per invio delle segnalazioni per domanda numero "
									+ numeroRichiesta + " con stato diverso da 200");
				}
			}

			// se il cittadino ha veriricato si va in update
			// se record aggiorno con i dati di operatore
			if (idIsee != null && idIsee.isIseeConforme() != null
					&& !Util.isValorizzato(idIsee.getIseeVerificaEsitoCod())) {
				isee.setUtenteModifica(userInfo.getCodFisc());
				isee.setIseeId(idIsee.getIseeId());
				iseeDao.updateIseeConforme(isee);
				// if
				// (isee.getIseeVerificaEsitoCod().equalsIgnoreCase(IseeStatoEnum.RESPINTO.getStato()))
				// {
				// iseeDao.updateChiusura(isee.getIseeId(), userInfo.getCodFisc());
				// }
			} else if (idIsee == null) { // nessun record aperto
				isee.setUtenteModifica(userInfo.getCodFisc());
				isee.setUtenteCreazione(userInfo.getCodFisc());
				/* Long idIseenew = */iseeDao.insertIseeConforme(isee, numeroRichiesta);
				// if
				// (isee.getIseeVerificaEsitoCod().equalsIgnoreCase(IseeStatoEnum.RESPINTO.getStato()))
				// {
				// iseeDao.updateChiusura(idIseenew, userInfo.getCodFisc());
				// }
			} else if (idIsee != null && Util.isValorizzato(idIsee.getIseeVerificaEsitoCod())
					&& idIsee.getIseeVerificaEsitoCod().equalsIgnoreCase(IseeStatoEnum.NON_CONFORME.getStato())) {
				generateResponseErrorException(CodeErrorEnum.ERR02, HttpStatus.BAD_REQUEST,
						"esito non conforme presente per domanda numero " + numeroRichiesta + " per anno "
								+ isee.getIseeAnno(),
						"Esito non conforme presente per domanda numero " + numeroRichiesta + " per anno "
								+ isee.getIseeAnno());
			} else {
				// tutti gli altri casi chiudo record precedente aperto e apro quello nuovo
				/* Long idIseeNew = */iseeDao.insertIseeConformeSelect(isee, idIsee.getIseeId(), userInfo.getCodFisc(),
						isee.getIseeVerificaEsitoCod());
				// if
				// (isee.getIseeVerificaEsitoCod().equalsIgnoreCase(IseeStatoEnum.RESPINTO.getStato()))
				// {
				// iseeDao.updateChiusura(idIseeNew, userInfo.getCodFisc());
				// }
				// chiudo il record precedente
				iseeDao.updateChiusura(idIsee.getIseeId(), userInfo.getCodFisc());
			}
			// comunico notifica al cittadino
			if (isee.getIseeVerificaEsitoCod().equalsIgnoreCase(IseeStatoEnum.RESPINTO.getStato())) {
				servizioRestService.getInviaNotifica(httpHeaders, httpRequest, numeroRichiesta, "RESPINGI_ISEE",
						buono.getRichiedenteCf());
			} else if (isee.getIseeVerificaEsitoCod().equalsIgnoreCase(IseeStatoEnum.NON_CONFORME.getStato())) {
				servizioRestService.getInviaNotifica(httpHeaders, httpRequest, numeroRichiesta, "NON_CONFORME_ISEE",
						buono.getRichiedenteCf());
			}

			return Response.status(200).entity(true).build();

		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}
		return error.generateResponseError();
	}

	public Response getConformitaIsee(String numeroRichiesta, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();

		try {
			ModelBuono buono = buonoDao.selectBuono(numeroRichiesta);
			if (buono == null) {
				generateResponseErrorException(CodeErrorEnum.ERR05, HttpStatus.NOT_FOUND,
						"buono inesistente per domanda " + numeroRichiesta,
						"buono");
			}

			ModelIseeBuono isee = new ModelIseeBuono();
			Calendar cal = new GregorianCalendar();
			int anno = cal.get(Calendar.YEAR);
			isee = iseeDao.selectIsee(numeroRichiesta, Integer.toString(anno));
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

	public Response getListaConformitaIsee(String numeroRichiesta, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();

		try {

			ModelBuono buono = buonoDao.selectBuono(numeroRichiesta);
			if (buono == null) {
				generateResponseErrorException(CodeErrorEnum.ERR05, HttpStatus.NOT_FOUND,
						"buono inesistente per domanda " + numeroRichiesta,
						"buono");
			}

			List<ModelIseeBuono> isee = new ArrayList<ModelIseeBuono>();
			isee = iseeDao.selectListaIsee(numeroRichiesta);
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

	public Response getListaEsitiIsee(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();

		try {
			List<ModelEsitoIsee> esitiIsee = new ArrayList<ModelEsitoIsee>();
			esitiIsee = iseeDao.selectListaEsitoIsee();
			return Response.status(200).entity(esitiIsee).build();
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}
}
