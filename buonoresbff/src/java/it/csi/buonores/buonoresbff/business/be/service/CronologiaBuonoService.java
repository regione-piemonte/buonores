/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.business.be.service;

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

import it.csi.buonores.buonoresbff.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbff.dto.Errore;
import it.csi.buonores.buonoresbff.dto.ErroreDettaglio;
import it.csi.buonores.buonoresbff.dto.ModelCronologiaBuono;
import it.csi.buonores.buonoresbff.dto.ModelStatoBuono;
import it.csi.buonores.buonoresbff.dto.custom.ModelBandiMessage;
import it.csi.buonores.buonoresbff.dto.custom.ModelBuono;
import it.csi.buonores.buonoresbff.dto.custom.ModelBuonoAltro;
import it.csi.buonores.buonoresbff.dto.custom.ModelRicezioneSegnalazione;
import it.csi.buonores.buonoresbff.exception.DatabaseException;
import it.csi.buonores.buonoresbff.exception.ResponseErrorException;
import it.csi.buonores.buonoresbff.integration.dao.custom.BuonoDao;
import it.csi.buonores.buonoresbff.integration.service.LogAuditService;
import it.csi.buonores.buonoresbff.util.Constants;
import it.csi.buonores.buonoresbff.util.Util;
import it.csi.buonores.buonoresbff.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoresbff.util.rest.ResponseRest;
import it.csi.buonores.buonoresbff.util.validator.impl.ValidateGenericImpl;

@Service
public class CronologiaBuonoService extends BaseService {

	@Autowired
	LogAuditService logaudit;

	@Autowired
	BuonoDao buonoDao;

	@Autowired
	ServizioRestBandiService restbase;

	public Response getCronologiaRichieste(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = new Errore();
		List<ModelCronologiaBuono> cronologia = new ArrayList<ModelCronologiaBuono>();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglio> listerrorservice = new ArrayList<ErroreDettaglio>();
		try {
			ModelBuono buono = buonoDao.selectBuono(numeroRichiesta);
			if (buono == null) {
				generateResponseErrorException(CodeErrorEnum.ERR23, HttpStatus.NOT_FOUND, "buono inesistente",
						"Buono per la domanda " + numeroRichiesta);
			} else {
				listerrorservice = validateGeneric.validateCronologia(xRequestId, xForwardedFor, xCodiceServizio,
						shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest, buono.getRichiedenteCf());
				generateResponseErrorExceptionForValidate(listerrorservice, HttpStatus.BAD_REQUEST,
						"errore in validate");
				cronologia = buonoDao.selectCronologiaBuono(buono.getBuonoId());
				return Response.status(200).entity(cronologia).build();
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

	public Response postCronologiaRichieste(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, ModelStatoBuono stato,
			SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
		Errore error = new Errore();
		ModelCronologiaBuono cronologia = new ModelCronologiaBuono();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglio> listerrorservice = new ArrayList<ErroreDettaglio>();
		try {
			listerrorservice = validateGeneric.validatePostCronologiaBuono(xRequestId, xForwardedFor,
					xCodiceServizio, shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest, stato);
			generateResponseErrorExceptionForValidate(listerrorservice, HttpStatus.BAD_REQUEST, "errore in validate");

			boolean esisteStato = buonoDao.esisteStato(stato.getStato());
			if (!esisteStato) {
				generateResponseErrorException(CodeErrorEnum.ERR23, HttpStatus.BAD_REQUEST,
						"Stato buono non esiste", "Stato buono non esiste");
			}

			ModelBuono buono = buonoDao.selectBuono(numeroRichiesta);
			if (buono != null) {
				if (!buono.getRichiedenteCf().equals(shibIdentitaCodiceFiscale)) {
					generateResponseErrorException(CodeErrorEnum.ERR12, HttpStatus.BAD_REQUEST,
							"Il codice fiscale del richiedente della domanda non corrisponde", numeroRichiesta);
				}

				listerrorservice = validateGeneric.checkCambioStatoCoerenteBuono(listerrorservice, stato.getStato(),
						buono.getStato());
				generateResponseErrorExceptionForValidate(listerrorservice, HttpStatus.BAD_REQUEST,
						"stato non coerente");

				// verifico se lo stato RINUNCIATO allora chiamo il servizio segnalazione di
				// bandi
				if (stato.getStato().equalsIgnoreCase(Constants.RINUNCIATO)
						&& buono.getStato().equalsIgnoreCase(Constants.ATTIVO)) {
					ModelRicezioneSegnalazione segnalazione = new ModelRicezioneSegnalazione();
					segnalazione.setCodiceNotifica(Constants.RIN);
					segnalazione.setData(Util.getData(stato.getDecorrenza()));
					segnalazione.setNumeroDomanda(numeroRichiesta);
					segnalazione.setDescrizioneNotifica(buonoDao.selectDescrMotivoSegnalazione(Constants.RIN));
					ObjectMapper mapper = new ObjectMapper();
					mapper.setSerializationInclusion(Include.NON_EMPTY);
					String jsonSegnalazione = mapper.writeValueAsString(segnalazione);
					ResponseRest responserest = restbase.setRicezioneSegnalazioni(jsonSegnalazione,
							shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio, securityContext,
							httpHeaders, httpRequest);
					if (responserest.getStatusCode() != 200 && responserest.getStatusCode() != 201) {
						error = createGenericServerError(metodo,
								"Errore  set ricezione segnalazioni buono rinunciato" + responserest.toString());
					} else {
						ModelBandiMessage message = new ModelBandiMessage();
						message = new ObjectMapper().readValue(responserest.getJson(), ModelBandiMessage.class);
						if (message.getEsitoServizio().equalsIgnoreCase("OK")) {
							ModelBuonoAltro buonoAltro = buonoDao.selectBuonoAltro(numeroRichiesta); // TODO serve??
							// eliminato il cambio stato se si tratta di revoca aggiungere logica di update
							// della data di revoca
							buonoDao.updateStatoBuono(buono.getBuonoId(), buono.getRichiedenteCf(),
									buono.getBuonoStatoId());
							buonoDao.insertBuonoStatoConMotivo(stato.getStato(), buono.getRichiedenteCf(),
									buono.getBuonoId(),
									stato.getDecorrenza(), Constants.RIN, buonoAltro.getDataRevoca(),
									buonoAltro.getNota()); // TODO serve
							cronologia = buildModelCronologia(stato, buono);
							return Response.status(200).entity(cronologia).build();
						} else {
							return Response.status(200).entity(message).build();
						}
					}
				} else if (stato.getStato().equalsIgnoreCase(Constants.REVOCATO_PRESA_VISIONE)
						&& buono.getStato().equalsIgnoreCase(Constants.REVOCATO)) {
					// prendo il resto dei dati del buono
					ModelBuonoAltro buonoAltro = buonoDao.selectBuonoAltro(numeroRichiesta);
					// eliminato il cambio stato se si tratta di revoca aggiungere logica di update
					// della data di revoca
					buonoDao.updateStatoBuono(buono.getBuonoId(), buono.getRichiedenteCf(), buono.getBuonoStatoId());
					buonoDao.insertBuonoStatoConMotivo(stato.getStato(), buono.getRichiedenteCf(), buono.getBuonoId(),
							null, null, buonoAltro.getDataRevoca(), buonoAltro.getNota());
					cronologia = buildModelCronologia(stato, buono);
					return Response.status(200).entity(cronologia).build();
				} else {
					generateResponseErrorException(CodeErrorEnum.ERR26, HttpStatus.BAD_REQUEST,
							"Impossibile rinunciare ad un buono in stato non attivo",
							"Buono per la domanda " + numeroRichiesta);
				}

			} else {
				generateResponseErrorException(CodeErrorEnum.ERR23, HttpStatus.NOT_FOUND,
						"Buono inesistente", "Buono per la domanda " + numeroRichiesta);
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

	private ModelCronologiaBuono buildModelCronologia(ModelStatoBuono stato, ModelBuono buono) {
		ModelCronologiaBuono cronologia = new ModelCronologiaBuono();
		ModelStatoBuono statobuono = new ModelStatoBuono();
		statobuono.setDecorrenza(stato.getDecorrenza());
		statobuono.setStato(stato.getStato());
		cronologia.setStato(statobuono);
		cronologia.setDataAggiornamento(new Date());
		cronologia.setNumero(buono.getBuonoCod());
		cronologia.setNote(null);
		return cronologia;
	}
}
