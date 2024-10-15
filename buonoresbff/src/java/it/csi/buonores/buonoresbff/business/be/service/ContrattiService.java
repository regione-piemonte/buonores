/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.business.be.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.buonores.buonoresbff.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbff.dto.Errore;
import it.csi.buonores.buonoresbff.dto.ErroreDettaglio;
import it.csi.buonores.buonoresbff.dto.ModelContratto;
import it.csi.buonores.buonoresbff.dto.ModelContrattoAllegati;
import it.csi.buonores.buonoresbff.dto.ModelRichiesta;
import it.csi.buonores.buonoresbff.exception.DatabaseException;
import it.csi.buonores.buonoresbff.exception.ResponseErrorException;
import it.csi.buonores.buonoresbff.integration.dao.custom.AllegatoBuonoDao;
import it.csi.buonores.buonoresbff.integration.dao.custom.ContrattiDao;
import it.csi.buonores.buonoresbff.integration.dao.custom.RichiesteDao;
import it.csi.buonores.buonoresbff.util.Constants;
import it.csi.buonores.buonoresbff.util.Util;
import it.csi.buonores.buonoresbff.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoresbff.util.validator.impl.ValidateGenericImpl;

@Service
public class ContrattiService extends BaseService {

	@Autowired
	AllegatoBuonoDao allegatoBuonoDao;

	@Autowired
	ContrattiDao contrattiDao;

	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	ServizioRestBandiService restbase;

	private void validateCodFiscaleAndShibIden(String cfRichiedente, String shibIdentitaCodiceFiscale)
			throws DatabaseException, ResponseErrorException {
		List<ErroreDettaglio> listError = new ArrayList<ErroreDettaglio>();
		validateGeneric.checkCodFiscaleAndShibIden(listError, cfRichiedente, shibIdentitaCodiceFiscale);
		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST,
				"Il codice fiscale del richiedente della domanda non corrisponde");
	}

	private void validateAddContratto(String numeroRichiesta, Long contrattoTipoId,
			Long fornitoreTipoId, String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, ModelContrattoAllegati contrattoAllegati, String cfDestinatario,
			int fornitoreId) throws DatabaseException, ResponseErrorException {
		List<ErroreDettaglio> listError = new ArrayList<ErroreDettaglio>();
		listError = validateGeneric.validateAddContratto(numeroRichiesta, contrattoTipoId, fornitoreTipoId,
				xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale, contrattoAllegati,
				cfDestinatario, fornitoreId);
		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "errore in validate");
	}

	private void validateAllegatoContrattoFileSystem(ModelContrattoAllegati contrattoAllegati)
			throws DatabaseException, ResponseErrorException {
		List<ErroreDettaglio> listError = new ArrayList<ErroreDettaglio>();
		listError = validateGeneric.checkAllegatoContrattoFileSystem(contrattoAllegati);
		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "errore in validate");
	}

	@Transactional
	public Response addContratto(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, ModelContrattoAllegati contrattoAllegati,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();

		try {
			ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			validateCodFiscaleAndShibIden(richiesta.getRichiedente().getCf(), shibIdentitaCodiceFiscale);

			Long contrattoTipoId = richiesteDao.selectIdContratto(contrattoAllegati.getContratto().getTipo());
			Long fornitoreTipoId = contrattiDao
					.selectIdFornitoreTipo(contrattoAllegati.getContratto().getTipoFornitore());
			String codFornitore = null;
			int fornitoreId = 0;
			if (contrattoAllegati.getContratto().getTipo().equals(Constants.CONTRATTO_RSA)) {
				codFornitore = contrattoAllegati.getContratto().getStruttura().getId();
			}

			fornitoreId = contrattiDao.checkEsistenzaFornitore(codFornitore, numeroRichiesta);

			validateAddContratto(numeroRichiesta, contrattoTipoId, fornitoreTipoId, xRequestId,
					xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale, contrattoAllegati,
					richiesta.getDestinatario().getCf(), fornitoreId);

			validateAllegatoContrattoFileSystem(contrattoAllegati);

			Long idDettaglio = richiesteDao.selectIdDettaglio(numeroRichiesta);
			if (idDettaglio == null) {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND, "Domanda inesistente",
						numeroRichiesta);
			}

			if (contrattoAllegati.getAllegati() != null) {
				if (!contrattiDao.checkEsistenzaAllegati(contrattoAllegati.getAllegati())) {
					generateResponseErrorException(CodeErrorEnum.ERR02, HttpStatus.NOT_FOUND, "Allegati inesistenti",
							"allegati");
				}
			}

			ModelContratto contrattoDb = contrattiDao.selectContrattoByNumeroRichiestaAperto(numeroRichiesta);
			if (contrattoDb == null) {
				generateResponseErrorException(CodeErrorEnum.ERR05, HttpStatus.NOT_FOUND,
						"Contratto non esistente per domanda",
						numeroRichiesta);
			} else {
				Date dataFine = Util.aggiungiGiorniAData(contrattoAllegati.getContratto().getDataInizio(), -1);
				contrattiDao.chiudiContratto(contrattoDb.getId(), shibIdentitaCodiceFiscale, dataFine);
			}
			if (fornitoreId == 0) {
				fornitoreId = contrattiDao.insertFornitore(contrattoAllegati.getContratto(), numeroRichiesta,
						shibIdentitaCodiceFiscale);
			} else {
				contrattiDao.updateFornitore(contrattoAllegati.getContratto(), numeroRichiesta,
						shibIdentitaCodiceFiscale);
			}

			ModelContratto anteprimaContratto = contrattiDao.insertContratto(contrattoAllegati.getContratto(),
					numeroRichiesta, shibIdentitaCodiceFiscale, fornitoreId);

			contrattiDao.insertAllegatiContratto(anteprimaContratto.getId(), contrattoAllegati.getAllegati(),
					shibIdentitaCodiceFiscale);

			contrattoAllegati.getContratto().setId(anteprimaContratto.getId());
			return Response.status(200).entity(contrattoAllegati).build();

		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	private void validateGetContratto(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale)
			throws DatabaseException, ResponseErrorException {
		List<ErroreDettaglio> listError = new ArrayList<ErroreDettaglio>();
		listError = validateGeneric.validateGetContratto(numeroRichiesta, xRequestId, xForwardedFor,
				xCodiceServizio, shibIdentitaCodiceFiscale);
		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "errore in validate");
	}

	public Response getContratti(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			validateGetContratto(numeroRichiesta, xRequestId, xForwardedFor,
					xCodiceServizio, shibIdentitaCodiceFiscale);

			Long idDettaglio = richiesteDao.selectIdDettaglio(numeroRichiesta);

			if (idDettaglio == null) {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND, "Domanda inesistente",
						numeroRichiesta);
			}

			ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			validateCodFiscaleAndShibIden(richiesta.getRichiedente().getCf(), shibIdentitaCodiceFiscale);

			List<ModelContrattoAllegati> contrattiAllegati = contrattiDao
					.selectContrattiByNumeroRichiesta(numeroRichiesta);

			return Response.status(200).entity(contrattiAllegati).build();

		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}
		return error.generateResponseError();
	}

	private void validatePutContratto(String numeroRichiesta, Integer idContratto, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String shibIdentitaCodiceFiscale, ModelContratto contratto)
			throws DatabaseException, ResponseErrorException {
		List<ErroreDettaglio> listError = new ArrayList<ErroreDettaglio>();
		listError = validateGeneric.validatePutContratto(numeroRichiesta, idContratto, xRequestId, xForwardedFor,
				xCodiceServizio, shibIdentitaCodiceFiscale, contratto);
		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "errore in validate");
	}

	// METODO NON UTILIZZATO
	public Response putContratto(String numeroRichiesta, Integer idContratto, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, ModelContratto contratto,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {

			validatePutContratto(numeroRichiesta, idContratto, xRequestId, xForwardedFor,
					xCodiceServizio, shibIdentitaCodiceFiscale, contratto);

			Long idDettaglio = richiesteDao.selectIdDettaglio(numeroRichiesta);
			if (idDettaglio == null) {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND, "Domanda inesistente",
						numeroRichiesta);
			}

			ModelContratto contrattoDb = contrattiDao.selectContrattoById(idContratto, numeroRichiesta);
			if (contrattoDb == null) {
				generateResponseErrorException(CodeErrorEnum.ERR05, HttpStatus.NOT_FOUND, "Contratto non esistente",
						idContratto.toString());
			} else {
				ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
				validateCodFiscaleAndShibIden(richiesta.getRichiedente().getCf(), shibIdentitaCodiceFiscale);

				contrattiDao.chiudiContratto(idContratto, shibIdentitaCodiceFiscale, contratto.getDataFine());
				contrattoDb.setDataFine(contratto.getDataFine());
				contrattoDb.setDataInvioABandi(null);
				return Response.status(200).entity(contrattoDb).build();
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

	private void validateDeleteContratto(String numeroRichiesta, Integer idContratto, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String shibIdentitaCodiceFiscale)
			throws DatabaseException, ResponseErrorException {
		List<ErroreDettaglio> listError = new ArrayList<ErroreDettaglio>();
		listError = validateGeneric.validateDeleteContratto(numeroRichiesta, idContratto, xRequestId, xForwardedFor,
				xCodiceServizio, shibIdentitaCodiceFiscale);
		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "errore in validate");
	}

	public Response deleteContratto(String numeroRichiesta, Integer idContratto, String xRequestId,
			String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			validateDeleteContratto(numeroRichiesta, idContratto, xRequestId, xForwardedFor,
					xCodiceServizio, shibIdentitaCodiceFiscale);

			ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			validateCodFiscaleAndShibIden(richiesta.getRichiedente().getCf(), shibIdentitaCodiceFiscale);

			Long idDettaglio = richiesteDao.selectIdDettaglio(numeroRichiesta);
			if (idDettaglio == null) {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND,
						"Domanda inesistente",
						numeroRichiesta);
			}

			// evitare che elimini tutti i contratti
			List<ModelContratto> contratti = contrattiDao.getContrattiByNumeroRichiesta(numeroRichiesta);
			if (contratti.size() <= 1) {
				generateResponseErrorException(CodeErrorEnum.ERR24, HttpStatus.UNPROCESSABLE_ENTITY,
						"Unico contratto esistente per domanda",
						numeroRichiesta);
			}

			ModelContratto contrattoOld = contratti.get(contratti.size() - 2);

			// la cancellazione del contratto deve essere fatta se data creazione è la data
			// odierna oppure non già inviata a bandi
			ModelContratto contrattoToDelete = contrattiDao.selectContrattoById(idContratto, numeroRichiesta);
			if (!Util.isSameDay(contrattoToDelete.getDataCreazione())
					|| contrattoToDelete.getDataInvioABandi() != null) {
				generateResponseErrorException(CodeErrorEnum.ERR24, HttpStatus.UNPROCESSABLE_ENTITY,
						"contratto già inviato a bandi ", numeroRichiesta);
			}

			// eliminare data fine dell'ultimo contratto
			contrattiDao.updateDataFinePrecedente(contrattoOld.getId(), shibIdentitaCodiceFiscale);

			// cancellazione contratto e allegati
			boolean esito = contrattiDao.eliminaAllegatiContratto(idContratto);
			if (esito)
				return Response.status(200).entity("Contratto eliminato per numero domanda " + numeroRichiesta).build();
			else {
				generateResponseErrorException(CodeErrorEnum.ERR05, HttpStatus.NOT_FOUND,
						"Errore sui contratti", numeroRichiesta);
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
