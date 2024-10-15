/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.business.be.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.buonores.buonoresbo.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbo.dto.CambioStatoPopUp;
import it.csi.buonores.buonoresbo.dto.Errore;
import it.csi.buonores.buonoresbo.dto.ErroreDettaglio;
import it.csi.buonores.buonoresbo.dto.ModelEmailDetId;
import it.csi.buonores.buonoresbo.dto.ModelRicezioneSegnalazione;
import it.csi.buonores.buonoresbo.dto.ModelVerificaEnte;
import it.csi.buonores.buonoresbo.dto.ModelVerificheEnte;
import it.csi.buonores.buonoresbo.dto.UserInfo;
import it.csi.buonores.buonoresbo.dto.custom.ModelBandiMessage;
import it.csi.buonores.buonoresbo.dto.custom.ModelBuono;
import it.csi.buonores.buonoresbo.exception.DatabaseException;
import it.csi.buonores.buonoresbo.exception.ResponseErrorException;
import it.csi.buonores.buonoresbo.filter.IrideIdAdapterFilter;
import it.csi.buonores.buonoresbo.integration.dao.custom.BuonoDao;
import it.csi.buonores.buonoresbo.integration.dao.custom.CodParametroDao;
import it.csi.buonores.buonoresbo.integration.dao.custom.EntiGestoriDao;
import it.csi.buonores.buonoresbo.integration.dao.custom.GraduatoriaDao;
import it.csi.buonores.buonoresbo.integration.dao.custom.RichiesteDao;
import it.csi.buonores.buonoresbo.util.Constants;
import it.csi.buonores.buonoresbo.util.SendEmailSMTP;
import it.csi.buonores.buonoresbo.util.Util;
import it.csi.buonores.buonoresbo.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoresbo.util.enumerator.ErrorParamEnum;
import it.csi.buonores.buonoresbo.util.rest.ResponseRest;
import it.csi.buonores.buonoresbo.util.validator.impl.ValidateGenericImpl;

@Service
public class EntiGestoriService extends BaseService {

	@Autowired
	EntiGestoriDao entigestoriDao;

	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	SendEmailSMTP sendEmailSMTP;

	@Autowired
	CodParametroDao parametroDao;

	@Autowired
	GraduatoriaDao graduatoriaDao;

	@Autowired
	ServizioRestBandiService restbase;

	@Autowired
	BuonoDao buonoDao;

	// Get lista degli Enti Gestori
	public Response getEntiGestori(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			return Response.ok().entity(entigestoriDao.selectEntiGestori()).build();
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	public Response richiediRettifica(CambioStatoPopUp datiPopUp, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		Errore error = new Errore();

		try {
			ModelEmailDetId richiesta = richiesteDao.selectEmailDetId(datiPopUp.getNumerodomanda());
			if (richiesta != null) {
				entigestoriDao.updateValiditaFineUltimaVerifica(richiesta.getDomandaId(), userInfo.getCodFisc());
				entigestoriDao.insertVerificaByRichiesta(datiPopUp.getNumerodomanda(), userInfo.getCodFisc(),
						datiPopUp.getNotaEnte());
				String bodyMessage = parametroDao.selectValoreParametroFromCod(Constants.BODY_MESSAGE,
						Constants.PARAMETRO_GENERICO);
				String objectMessage = parametroDao
						.selectValoreParametroFromCod(Constants.OBJECT_MESSAGE, Constants.PARAMETRO_GENERICO)
						.replace("{0}", datiPopUp.getNumerodomanda());
				sendEmailSMTP.send(richiesta.getEmail(), bodyMessage, objectMessage);
				return Response.status(200).entity(true).build();
			} else {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND, "domanda inesistente",
						datiPopUp.getNumerodomanda());
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

	public Response updateToVerificaInCorso(String numeroDomande[], SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {

		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);

		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		Errore error = new Errore();
		try {
			entigestoriDao.updateToVerificaInCorso(numeroDomande, userInfo.getCodFisc());
			return Response.status(200).entity(true).build();
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}
		return error.generateResponseError();
	}

	public Response selectVerificheEnteGestore(String numeroDomanda, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		Errore error = new Errore();

		try {
			ModelVerificheEnte verifiche = entigestoriDao.selectVerificheEnteByNumeroDomanda(numeroDomanda);

			return Response.status(200).entity(verifiche).build();
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	public Response salvaVerificaEnteNew(String numeroDomanda, ModelVerificheEnte verificheEnte,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		Errore error = new Errore();
		List<ErroreDettaglio> listerrorservice = new ArrayList<ErroreDettaglio>();

		try {
			if (verificheEnte.getPresenzaAltreIncompatibilita()) {
				listerrorservice = validateGeneric.checkEmptyString(listerrorservice, verificheEnte.getNoteEnte(),
						ErrorParamEnum.VERIFICA_NOTE.getCode());

				generateResponseErrorExceptionForValidate(listerrorservice, HttpStatus.BAD_REQUEST,
						"errore in validate");

				ModelBuono buono = buonoDao.selectBuono(numeroDomanda);
				if (buono != null && !(buono.getStato().equalsIgnoreCase(Constants.CREATO))) { // verificano solo che
																								// buono != null
					// se ci sono incompatibilita devo inviare la segnalazione a bandi con tipo
					// notifica PAM
					ModelRicezioneSegnalazione segnalazione = new ModelRicezioneSegnalazione();
					segnalazione.setCodiceNotifica(Constants.PAM);
					segnalazione.setData(Util.getDataISO(verificheEnte.getDataVerifica()));
					segnalazione.setNumeroDomanda(numeroDomanda);
					segnalazione.setDescrizioneNotifica(verificheEnte.getNoteEnte());
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
							logError(metodo,
									"errore riguardante la comunicazione con bandi per invio delle segnalazioni "
											+ responserest.toString());
							generateResponseErrorException(CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR,
									"errore riguardante la comunicazione con bandi per invio delle segnalazioni "
											+ responserest.toString(),
									numeroDomanda);
						}
					} else {
						logError(metodo, "errore riguardante la comunicazione con bandi per invio delle segnalazioni "
								+ responserest.toString());
						generateResponseErrorException(CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR,
								"errore riguardante la comunicazione con bandi per invio delle segnalazioni "
										+ responserest.toString(),
								numeroDomanda);
					}
				}
			}

			ModelVerificaEnte verificaEnte = entigestoriDao.selectVerificaByNumeroDomanda(numeroDomanda);
			if (verificaEnte != null) {
				if (verificheEnte.getPresenzaAltreIncompatibilita() != null) {
					Integer esito = entigestoriDao.updateVerificaEnte(verificaEnte.getVerificaId(),
							userInfo.getCodFisc());
					if (esito != null) {
						esito = entigestoriDao.insertVerificaEnte(numeroDomanda, verificheEnte.getNoteEnte(),
								!verificheEnte.getPresenzaAltreIncompatibilita(), userInfo.getCodFisc(),
								userInfo.getCodFisc(), verificaEnte.getVerificaEgRichiesta(),
								verificaEnte.getVerificaEgInCorso(), verificaEnte.getVerificaEgConclusa(),
								verificaEnte.getDataVerificaRichiesta());
						if (esito != null) {
							return Response.status(200).entity(true).build();
						} else {
							generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.INTERNAL_SERVER_ERROR,
									"errore riguardante il database su inserimento verifica ente ", numeroDomanda);
						}
					} else {
						generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.INTERNAL_SERVER_ERROR,
								"errore riguardante il database su aggiornamento verifica ente ", numeroDomanda);
					}
				} else {
					generateResponseErrorException(CodeErrorEnum.ERR01, HttpStatus.BAD_REQUEST,
							"errore riguardante il database per verifica ente non trovata ", numeroDomanda);
				}
			} else {
				// Se non sono ancora state effettuate verifica
				if (verificheEnte.getPresenzaAltreIncompatibilita() != null) {
					// La data verifica non puo' essere nulla
					Date currentDate = new Date();
					SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
					String currentDateString = dateFormat.format(currentDate);
					// Insert del primo record nella tabella delle verifiche
					Integer esito = entigestoriDao.insertVerificaEnte(numeroDomanda, verificheEnte.getNoteEnte(),
							!verificheEnte.getPresenzaAltreIncompatibilita(), userInfo.getCodFisc(),
							userInfo.getCodFisc(), null, null, null, currentDateString);
					if (esito != null) {
						return Response.status(200).entity(true).build();
					} else {
						generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.INTERNAL_SERVER_ERROR,
								"errore riguardante il database su inserimento verifiche ente ", numeroDomanda);
					}
				} else {
					generateResponseErrorException(CodeErrorEnum.ERR01, HttpStatus.BAD_REQUEST,
							"errore riguardante il database per verifiche ente non trovate ", numeroDomanda);
				}
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

	public Response salvaConcludiVerificaEnte(String numeroDomanda, ModelVerificheEnte verificheEnte,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName(); // nome del metodo, inserito nei log in caso di errore
		List<ErroreDettaglio> listerrorservice = new ArrayList<ErroreDettaglio>();
		Errore error = new Errore();

		try {
			/*
			 * - Controllo se esiste il record delle verifiche
			 * - Controllo che sia settato il booleano verifica in corso
			 * - Update del record con booleani a null, verificaConclusa a true,
			 * note ente, dataverifica a now()
			 */
			if (verificheEnte.getPresenzaAltreIncompatibilita()) {
				listerrorservice = validateGeneric.checkEmptyString(listerrorservice, verificheEnte.getNoteEnte(),
						ErrorParamEnum.VERIFICA_NOTE.getCode());
				generateResponseErrorExceptionForValidate(listerrorservice, HttpStatus.BAD_REQUEST,
						"errore in validate");

				ModelBuono buono = buonoDao.selectBuono(numeroDomanda);
				if (buono != null && !(buono.getStato().equalsIgnoreCase(Constants.CREATO))) {
					// se ci sono incompatibilita devo inviare la segnalazione a bandi con tipo
					// notifica PAM
					ModelRicezioneSegnalazione segnalazione = new ModelRicezioneSegnalazione();
					segnalazione.setCodiceNotifica(Constants.PAM);
					segnalazione.setData(Util.getDataISO(verificheEnte.getDataVerifica()));
					segnalazione.setNumeroDomanda(numeroDomanda);
					segnalazione.setDescrizioneNotifica(verificheEnte.getNoteEnte());
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
							logError(metodo,
									"errore riguardante la comunicazione con bandi per invio delle segnalazioni "
											+ responserest.toString());
							generateResponseErrorException(CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR,
									"errore riguardante la comunicazione con bandi per invio delle segnalazioni "
											+ responserest.toString(),
									numeroDomanda);
						}
					} else {
						logError(metodo, "errore riguardante la comunicazione con bandi per invio delle segnalazioni "
								+ responserest.toString());
						generateResponseErrorException(CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR,
								"errore riguardante la comunicazione con bandi per invio delle segnalazioni "
										+ responserest.toString(),
								numeroDomanda);

					}
				}
			}

			ModelVerificaEnte verificaEnte = entigestoriDao.selectVerificaByNumeroDomanda(numeroDomanda);
			if (verificaEnte != null) {
				if (verificaEnte.getVerificaEgInCorso() != null && verificaEnte.getVerificaEgInCorso()) {
					if (verificheEnte.getPresenzaAltreIncompatibilita() != null) {
						Integer esito = entigestoriDao.updateVerificaEnteToConclusa(verificaEnte.getVerificaId(),
								userInfo.getCodFisc(), verificheEnte.getNoteEnte(),
								verificheEnte.getPresenzaAltreIncompatibilita());
						if (esito != null) {
							return Response.status(200).entity(true).build();
						} else {
							generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.INTERNAL_SERVER_ERROR,
									"errore riguardante il database per aggiornamento verifica ente ", numeroDomanda);
						}
					} else {
						generateResponseErrorException(CodeErrorEnum.ERR01, HttpStatus.BAD_REQUEST,
								"errore riguardante il database per verifiche ente non trovate ", numeroDomanda);
					}
				} else {
					generateResponseErrorException(CodeErrorEnum.ERR02, HttpStatus.INTERNAL_SERVER_ERROR,
							" Verifica non in stato: IN_CORSO ", numeroDomanda);
				}
			} else {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.INTERNAL_SERVER_ERROR,
						" Verifica inesistente ", numeroDomanda);
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
