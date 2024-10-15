/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.business.be.service;

import java.math.BigDecimal;
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

import it.csi.buonores.buonoresbff.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbff.dto.Errore;
import it.csi.buonores.buonoresbff.dto.ErroreDettaglio;
import it.csi.buonores.buonoresbff.dto.ModelAllegato;
import it.csi.buonores.buonoresbff.dto.ModelCronologia;
import it.csi.buonores.buonoresbff.dto.custom.ModelGetAllegatoExt;
import it.csi.buonores.buonoresbff.dto.custom.ModelRichiestaExt;
import it.csi.buonores.buonoresbff.dto.custom.ModelUpdateCronologia;
import it.csi.buonores.buonoresbff.exception.DatabaseException;
import it.csi.buonores.buonoresbff.exception.ResponseErrorException;
import it.csi.buonores.buonoresbff.integration.dao.custom.AllegatoDao;
import it.csi.buonores.buonoresbff.integration.dao.custom.CronologiaDao;
import it.csi.buonores.buonoresbff.integration.dao.custom.RichiesteDao;
import it.csi.buonores.buonoresbff.integration.dao.custom.SportelliDao;
import it.csi.buonores.buonoresbff.integration.service.LogAuditService;
import it.csi.buonores.buonoresbff.util.Constants;
import it.csi.buonores.buonoresbff.util.enumerator.CodeErrorEnum;

@Service
public class CronologiaService extends BaseService {

	@Autowired
	LogAuditService logaudit;

	@Autowired
	CronologiaDao cronologiaDao;

	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	AllegatoDao allegatoDao;

	@Autowired
	SportelliDao sportelliDao;

	@Autowired
	ServizioRestService servizioRestService;

	public Response getCronologiaRichieste(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = null;
		List<ModelCronologia> cronologia = new ArrayList<ModelCronologia>();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			// validate
			String cf = cronologiaDao.selectCf(numeroRichiesta);
			if (cf == null) {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND,
						"domanda inesistente", numeroRichiesta);
			}
			checkValidateCronologiaGet(xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale,
					securityContext, httpHeaders, httpRequest, cf);

			cronologia = cronologiaDao.selectCronologia(numeroRichiesta);

			return Response.ok().entity(cronologia).build();

		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}
		return error.generateResponseError();
	}

	private void checkValidateCronologiaGet(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest, String cf) throws DatabaseException, ResponseErrorException {
		List<ErroreDettaglio> listError = validateGeneric.validateCronologia(xRequestId, xForwardedFor,
				xCodiceServizio, shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest, cf);

		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "errore in validate");
	}

	public Response postCronologiaRichieste(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, String stato, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
		Errore error = null;
		ModelCronologia cronologia = new ModelCronologia();
		ModelUpdateCronologia update = new ModelUpdateCronologia();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglio> listError = new ArrayList<ErroreDettaglio>();
		boolean statidacontrollare = false;
		boolean pulisciBatch = false;
		try {
			if (stato.equalsIgnoreCase(Constants.ANNULLATA) || stato.equalsIgnoreCase(Constants.IN_RETTIFICA)
					|| stato.equalsIgnoreCase(Constants.CONTRODEDOTTA)// MODIFICA TAG 03 buonodom
					|| stato.equalsIgnoreCase(Constants.RINUNCIATA)) {// Modifica TAG 2.2.0v002 buonodom
				statidacontrollare = true;
			}

			checkValidatePostCronologia(xRequestId, xForwardedFor, xCodiceServizio,
					shibIdentitaCodiceFiscale, stato, securityContext, httpHeaders, httpRequest, listError);
			ModelRichiestaExt richiesta = richiesteDao.selectNumeroRichiestaExt(numeroRichiesta);
			if (richiesta != null) {
				if (!richiesta.getRichiedente().getCf().equals(shibIdentitaCodiceFiscale)) {
					generateResponseErrorException(CodeErrorEnum.ERR12, HttpStatus.BAD_REQUEST,
							"Il codice fiscale del richiedente della domanda non corrisponde", numeroRichiesta);
				}
				if (!stato.equalsIgnoreCase(Constants.ANNULLATA)
						&& !stato.equalsIgnoreCase(Constants.RINUNCIATA)) {// Modifica TAG 2.2.0v002 buonodom
					Boolean residentePiemonte = richiesteDao
							.isResidenzaPiemontese(richiesta.getDestinatario().getProvinciaResidenza());
					// validate
					if (!residentePiemonte) {
						String destinatario = richiesta.getDestinatario().getCognome() + " "
								+ richiesta.getDestinatario().getNome();
						generateResponseErrorException(CodeErrorEnum.ERR19, HttpStatus.UNPROCESSABLE_ENTITY,
								"destinatario non piemontese", destinatario);
					}
				}
				// Valida se il cambio stato si considera coerente
				checkValidateCambioStatoCoerente(stato, listError, richiesta);

				// Modifica TAG 2.2.0v002 buonodom
				if (stato.equalsIgnoreCase(Constants.INVIATA)) {
					boolean inCorso = sportelliDao.isSportelliCorrente(richiesta.getNumero());
					if (!inCorso) {
						checkSportelloCorrente(listError, inCorso);
					}
				}

				if (!stato.equalsIgnoreCase(Constants.ANNULLATA)
						&& !stato.equalsIgnoreCase(Constants.RINUNCIATA)) {// Modifica TAG 2.2.0v002 buonodom
					checkValidateCambioStatoPossibile(listError, richiesta);
				}
				// MODIFICA TAG 03 buonodom
				if (stato.equalsIgnoreCase(Constants.CONTRODEDOTTA)) {
					listError = validateGeneric.notaControdeduzione(listError, richiesta.getNoteRichiedente());
					generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST,
							"cambio stato non possibile");
				}

				List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();
				allegati = richiesteDao.selectAllegatiFromNumeroRichiesta(numeroRichiesta);

				if (!allegati.isEmpty()) {
					richiesta.setAllegati(allegati);
				}

				String detCod = richiesteDao.selectDetCod(numeroRichiesta);
				// valida allegato
				checkValidateAllegato(shibIdentitaCodiceFiscale, stato, listError, richiesta, detCod,
						statidacontrollare);

				if (!stato.equalsIgnoreCase(Constants.ANNULLATA)
						&& !stato.equalsIgnoreCase(Constants.RINUNCIATA)) {// Modifica TAG 2.2.0v002 buonodom
					checkAllegatoPresenteInFileSystem(shibIdentitaCodiceFiscale, stato, listError, richiesta, detCod);
				}
				if (stato.equalsIgnoreCase(Constants.PERFEZIONATA_IN_PAGAMENTO)) {
					checkEliminazioneIncompatibilita(listError, richiesta);
				}

				update = cambioStatoDomanda(richiesta, stato, shibIdentitaCodiceFiscale, numeroRichiesta, xRequestId,
						xForwardedFor,
						xCodiceServizio, securityContext, httpHeaders, httpRequest);
				pulisciBatch = true;
				cronologia.setStato(stato);
				cronologia.setDataAggiornamento(new Date());
				cronologia.setNumero(numeroRichiesta);
				cronologia.setNote(richiesta.getNote());
				// se lo stato in_rettifica allora devo portarmi appresso anche i dati da
				// modificare facendo update sulla tabella dati da modificare
				if (stato.equalsIgnoreCase(Constants.IN_RETTIFICA)) {
					// MODIFICA TAG 71 buonodom
					List<ModelAllegato> allegatinew = new ArrayList<ModelAllegato>();
					allegatinew = richiesteDao.selectAllegatiFromNumeroRichiesta(numeroRichiesta);
					richiesteDao.updateDatiDaModificare(update.getIdDettaglio(), richiesta.getDomandaDetId());
					for (ModelAllegato allegato : allegati) {
						ModelAllegato al = allegatinew.stream()
								.filter(f -> allegato.getTipo().equalsIgnoreCase(f.getTipo())).findFirst().orElse(null);
						if (al != null) {
							richiesteDao.updateallegatiDaModificare(update.getIdDettaglio(), allegato.getId(),
									al.getId());
						}
					}
				}
				if (stato.equalsIgnoreCase(Constants.INVIATA)) {
					richiesteDao.updateDataDomandaValDomanda(numeroRichiesta, shibIdentitaCodiceFiscale);
					// chiamo smista documento STARDAS
					servizioRestService.getStartDas(xRequestId, xForwardedFor, xCodiceServizio, securityContext,
							httpHeaders, httpRequest, numeroRichiesta);
					servizioRestService.getInviaNotifica(xRequestId, xForwardedFor, xCodiceServizio, securityContext,
							httpHeaders, httpRequest, numeroRichiesta, "INVIO_CITTADINO");
				} else if (stato.equalsIgnoreCase(Constants.RETTIFICATA)) {
					// chiamo smista documento STARDAS
					servizioRestService.getStartDas(xRequestId, xForwardedFor, xCodiceServizio, securityContext,
							httpHeaders, httpRequest, numeroRichiesta);
					servizioRestService.getInviaNotifica(xRequestId, xForwardedFor, xCodiceServizio, securityContext,
							httpHeaders, httpRequest, numeroRichiesta, "RETTIFICA_CITTADINO");
				} else if (stato.equalsIgnoreCase(Constants.CONTRODEDOTTA)) { // MODIFICA TAG 03 buonodom
					// chiamo smista documento STARDAS
					servizioRestService.getStartDas(xRequestId, xForwardedFor, xCodiceServizio, securityContext,
							httpHeaders, httpRequest, numeroRichiesta);
					servizioRestService.getInviaNotifica(xRequestId, xForwardedFor, xCodiceServizio, securityContext,
							httpHeaders, httpRequest, numeroRichiesta, "CONTRODEDOTTA_CITTADINO");
				} else if (stato.equalsIgnoreCase(Constants.RINUNCIATA)) { // Modifica TAG 2.2.0v002 buonodom
					// verifico se lo stato precedente della domanda ammessa e ammessa con riserva
					// aggiorno la graduatoria se esiste
					if (richiesta.getStato().equalsIgnoreCase(Constants.AMMESSA)
							|| richiesta.getStato().equalsIgnoreCase(Constants.AMMESSA_RISERVA)) {
						long idGraduatoria = richiesteDao.selectGraduatoriaIdBySportelloId(richiesta.getSportelloId());
						if (richiesteDao.checkStatoGraduatoria(idGraduatoria, Constants.PROVVISORIA)) {
							richiesteDao.updateRGraduatoriaStato(idGraduatoria, Constants.DA_AGGIORNARE,
									richiesta.getRichiedente().getCf());
						}
					}
					// chiamo smista documento STARDAS
					servizioRestService.getStartDas(xRequestId, xForwardedFor, xCodiceServizio, securityContext,
							httpHeaders, httpRequest, numeroRichiesta);
					servizioRestService.getInviaNotifica(xRequestId, xForwardedFor, xCodiceServizio, securityContext,
							httpHeaders, httpRequest, numeroRichiesta, "RINUNCIATA_CITTADINO");
				} else if (stato.equalsIgnoreCase(Constants.PERFEZIONATA_IN_PAGAMENTO)) { // Modifica TAG 2.2.0v002
																							// buonodom
					// chiamo smista documento STARDAS
					servizioRestService.getStartDas(xRequestId, xForwardedFor, xCodiceServizio, securityContext,
							httpHeaders, httpRequest, numeroRichiesta);
					servizioRestService.getInviaNotifica(xRequestId, xForwardedFor, xCodiceServizio, securityContext,
							httpHeaders, httpRequest, numeroRichiesta, "PERFEZIONATA_IN_PAGAMENTO_CITTADINO");
				}
			} else {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND,
						"domanda inesistente", numeroRichiesta);
			}
			return Response.ok().entity(cronologia).build();

		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		} finally {
			if (pulisciBatch) {
				servizioRestService.deleteBatch(numeroRichiesta);
			}
		}
		return error.generateResponseError();
	}

	private void checkValidateAllegato(String shibIdentitaCodiceFiscale, String stato,
			List<ErroreDettaglio> listError, ModelRichiestaExt richiesta, String detCod, boolean statidacontrollare)
			throws DatabaseException, ResponseErrorException {
		listError = validateGeneric.checkAllegato(listError, richiesta, detCod, shibIdentitaCodiceFiscale,
				stato, statidacontrollare);
		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "errore in validate");
	}

	private void checkValidateCambioStatoPossibile(List<ErroreDettaglio> listError,
			ModelRichiestaExt richiesta) throws DatabaseException, ResponseErrorException {
		listError = validateGeneric.checkSeCambioStatoPossibile(listError, richiesta);
		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "cambio stato non possibile");
	}

	private void checkValidateCambioStatoCoerente(String stato, List<ErroreDettaglio> listError,
			ModelRichiestaExt richiesta) throws DatabaseException, ResponseErrorException {
		listError = validateGeneric.checkCambioStatoCoerente(listError, stato, richiesta.getStato());
		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "stato non coerente");
	}

	private void checkValidatePostCronologia(String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, String stato, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest, List<ErroreDettaglio> listError)
			throws DatabaseException, ResponseErrorException {
		listError = validateGeneric.validatePostCronologia(listError, xRequestId, xForwardedFor,
				xCodiceServizio, shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest, stato);
		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "errore in validate");
	}

	private void checkAllegatoPresenteInFileSystem(String shibIdentitaCodiceFiscale, String stato,
			List<ErroreDettaglio> listError, ModelRichiestaExt richiesta, String detCod)
			throws DatabaseException, ResponseErrorException {
		listError = validateGeneric.checkAllegatoFileSystem(listError, richiesta, detCod,
				shibIdentitaCodiceFiscale, stato);
		generateResponseErrorExceptionForValidate(listError, HttpStatus.INTERNAL_SERVER_ERROR,
				"errore in allega documenti");
	}

	private void checkEliminazioneIncompatibilita(List<ErroreDettaglio> listError,
			ModelRichiestaExt richiesta)
			throws DatabaseException, ResponseErrorException {
		listError = validateGeneric.checkEliminazioneIncompatibilita(listError, richiesta);
		generateResponseErrorExceptionForValidate(listError, HttpStatus.INTERNAL_SERVER_ERROR,
				"errore in perfezionamento domanda");
	}

	private void checkSportelloCorrente(List<ErroreDettaglio> listError, boolean inCorso)
			throws DatabaseException, ResponseErrorException {
		listError = validateGeneric.checkSportelloCorrente(listError, inCorso);
		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "cambio stato non possibile");
	}

	private ModelUpdateCronologia cambioStatoDomanda(ModelRichiestaExt richiesta, String stato, String codFiscale,
			String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws DatabaseException {
		ModelUpdateCronologia update = richiesteDao.insertTDettaglioDomandaCambioStato(richiesta, stato, codFiscale);
		richiesteDao.updateDataFineValDettaglioDomanda(richiesta.getDomandaDetId());
		long idDettaglio = update.getIdDettaglio();
		String detCod = update.getDetCod();
		if (!richiesta.getAllegati().isEmpty()) {
			List<ModelGetAllegatoExt> allegati = allegatoDao.selectAllegati(richiesta.getDomandaDetId());
			if (!allegati.isEmpty()) {
				// MODIFICA TAG 70 buonodom
				for (ModelGetAllegatoExt a : allegati) {
					if (!a.getAllegatoTipoCod().equalsIgnoreCase(Constants.DOMANDA)) {
						allegatoDao.insertAllegatoConAllegatoTipoId(a.getFileName(), a.getFileType(),
								a.getFilePath(), richiesta.getSportelloId(), BigDecimal.valueOf(idDettaglio), detCod,
								a.getAllegatoId(), codFiscale, codFiscale);
					}
				}
			}
		}
		servizioRestService.getCreaDomanda(xRequestId, xForwardedFor, xCodiceServizio, securityContext,
				httpHeaders, httpRequest, richiesta.getNumero());

		update.setDetCod(detCod);
		update.setIdDettaglio(idDettaglio);

		return update;
	}
}
