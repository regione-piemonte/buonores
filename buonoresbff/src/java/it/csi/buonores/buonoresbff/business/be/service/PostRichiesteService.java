/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.business.be.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonoresbff.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbff.dto.DDomandaStato;
import it.csi.buonores.buonoresbff.dto.Errore;
import it.csi.buonores.buonoresbff.dto.ErroreDettaglio;
import it.csi.buonores.buonoresbff.dto.ModelAnteprimaRichiesta;
import it.csi.buonores.buonoresbff.dto.ModelBozzaRichiesta;
import it.csi.buonores.buonoresbff.dto.ModelRichiesta;
import it.csi.buonores.buonoresbff.exception.DatabaseException;
import it.csi.buonores.buonoresbff.exception.ResponseErrorException;
import it.csi.buonores.buonoresbff.integration.dao.custom.RichiesteDao;
import it.csi.buonores.buonoresbff.util.Constants;
import it.csi.buonores.buonoresbff.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoresbff.util.rest.ResponseRest;

@Service
public class PostRichiesteService extends BaseService {

	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	ServizioRestService servizioRestService;

	public Response richiestePost(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, ModelBozzaRichiesta richiesta, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		long tempoPartenza = System.currentTimeMillis();
		log.info("richiestepost - BEGIN time: ");
		try {
			if (richiesta == null) {
				generateResponseErrorException(CodeErrorEnum.ERR01, HttpStatus.BAD_REQUEST,
						"La richiesta non è stata valorizzata");
			} else {

				DDomandaStato ddomandaStatoBozza = richiesteDao.selectDDomandaStatoByCod(Constants.BOZZA);

				Long idStato = ddomandaStatoBozza != null ? ddomandaStatoBozza.getIdStato() : null;
				Long idContributo = richiesteDao.selectIdContributo(Constants.BUONORES);
				String codStato = ddomandaStatoBozza != null ? ddomandaStatoBozza.getCodStato() : null;
				Long idSportello = richiesteDao.selectIdSportello();

				Long idTitolo = richiesteDao.selectIdTitolo(richiesta.getStudioDestinatario());
				Long idRapporto = richiesteDao.selectIdRapporto(richiesta.getDelega());
				Long idAsl = richiesteDao.selectIdAsl(richiesta.getAslDestinatario());

				long tempoId = System.currentTimeMillis();

				log.info("richiestePost richiesteDao.selectId " + System.currentTimeMillis() + " millis: "
						+ (tempoPartenza - tempoId));
				Date dataAttuale = new Date();
				// VALIDATE POST RICHIESTA
				List<ErroreDettaglio> listError = validateGeneric.validatePostRichieste(xRequestId, xForwardedFor,
						xCodiceServizio, shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest,
						richiesta, idStato, idContributo, idSportello, idTitolo, idAsl, idRapporto);

				long tempoValidate = System.currentTimeMillis();
				log.info("richiestePost richiesteDao.validatePostRichieste " + System.currentTimeMillis() + " millis: "
						+ (tempoPartenza - tempoId));
				generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "errore in validate");

				Boolean residentePiemonte = richiesteDao
						.isResidenzaPiemontese(richiesta.getDestinatario().getProvinciaResidenza());
				// validate
				if (!residentePiemonte) {
					String destinatario = richiesta.getDestinatario().getCognome() + " "
							+ richiesta.getDestinatario().getNome();
					generateResponseErrorException(CodeErrorEnum.ERR19, HttpStatus.UNPROCESSABLE_ENTITY,
							"destinatario non piemontese", destinatario);
				}
				if (richiesteDao.presenzaDomanda(richiesta.getDestinatario().getCf())) {
					generateResponseErrorException(CodeErrorEnum.ERR09, HttpStatus.CONFLICT, "domanda gia' presente",
							richiesta.getDestinatario().getCf());
				}
				Long idDomanda = richiesteDao.insertDomandaAndDettaglio(shibIdentitaCodiceFiscale, richiesta, idStato,
						idContributo, codStato, idSportello, idTitolo, idRapporto, idAsl);

				String numeroDomanda = richiesteDao.selectNumeroDomanda(idDomanda);

				ModelAnteprimaRichiesta anteprimaRichiesta = buildAnteprimaRichiesta(richiesta, codStato, dataAttuale,
						numeroDomanda);

				long timeInsertDomanda = System.currentTimeMillis();

				log.info("richiestePost richiesteDao.insertTDomanda " + System.currentTimeMillis() + " millis: "
						+ (tempoValidate - timeInsertDomanda));

				ResponseRest response = servizioRestService.getCreaDomanda(xRequestId, xForwardedFor, xCodiceServizio,
						securityContext, httpHeaders, httpRequest, numeroDomanda);

				long timeChiamaRest = System.currentTimeMillis();

				log.info("richiestePost richiesteDao.insertTDomanda " + System.currentTimeMillis() + " millis: "
						+ (timeInsertDomanda - timeChiamaRest));

				if (response == null || response.getStatusCode() != HttpStatus.OK.value()) {
					// errore
					error = createGenericServerError(metodo, "crea domanda");
				} else {
					return Response.ok().entity(anteprimaRichiesta).build();
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

	private ModelAnteprimaRichiesta buildAnteprimaRichiesta(ModelBozzaRichiesta richiesta, String codStato,
			Date dataAttuale, String numeroDomanda) {
		ModelAnteprimaRichiesta anteprimaRichiesta = new ModelAnteprimaRichiesta();
		anteprimaRichiesta.setNumero(numeroDomanda);
		anteprimaRichiesta.setStato(codStato);
		anteprimaRichiesta.setDataAggiornamento(dataAttuale);
		anteprimaRichiesta.setRichiedente(richiesta.getRichiedente());
		anteprimaRichiesta.setDestinatario(richiesta.getDestinatario());
		anteprimaRichiesta.setNote(richiesta.getNote());
		return anteprimaRichiesta;
	}

	public Response richiestePut(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, ModelRichiesta richiesta,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();

		try {
			Long idDettaglio = richiesteDao.selectIdDettaglio(numeroRichiesta);
			if (idDettaglio == null) {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND, "domanda inesistente",
						numeroRichiesta);
			}
			Long idTitolo = richiesteDao.selectIdTitolo(richiesta.getStudioDestinatario());
			Long idRapporto = richiesteDao.selectIdRapporto(richiesta.getDelega());
			Long idAsl = richiesteDao.selectIdAsl(richiesta.getAslDestinatario());
			Long idValutazione = richiesteDao
					.selectIdValutazioneMultidimensionale(richiesta.getValutazioneMultidimensionale());
			Long idRelazione = null;
			Long idContratto = null;
			if (richiesta.getContratto() != null) {
				// RIMOZIONE_INTESTATARIO_DATA_FINE POST_DEMO 14_04_2023
				// idRelazione =
				// richiesteDao.selectIdRapporto(richiesta.getContratto().getRelazioneDestinatario());
				idContratto = richiesteDao.selectIdContratto(richiesta.getContratto().getTipo());
			}

			ModelRichiesta richiestaDb = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			// Validazione aggiornamento richiesta
			checkValidatePutRichiesta(numeroRichiesta, xRequestId, xForwardedFor, xCodiceServizio,
					shibIdentitaCodiceFiscale, richiesta, securityContext, httpHeaders, httpRequest, idTitolo,
					idRapporto, idAsl, idValutazione, idRelazione, idContratto, richiestaDb);

			richiesteDao.updateTDettaglioDomanda(idDettaglio, richiesta, idTitolo, idRapporto, idAsl, idValutazione,
					idRelazione, idContratto, shibIdentitaCodiceFiscale);

			ResponseRest response = servizioRestService.getCreaDomanda(xRequestId, xForwardedFor, xCodiceServizio,
					securityContext, httpHeaders, httpRequest, richiesta.getNumero());

			if (response == null || response.getStatusCode() != HttpStatus.OK.value()) {
				// errore
				error = createGenericServerError(metodo, "crea domanda");
			} else {
				return Response.ok().entity(richiesta).build();
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

	private void checkValidatePutRichiesta(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, ModelRichiesta richiesta,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest, Long idTitolo,
			Long idRapporto, Long idAsl, Long idValutazione, Long idRelazione, Long idContratto,
			ModelRichiesta richiestaDb) throws DatabaseException, ResponseErrorException {
		List<ErroreDettaglio> listError = validateGeneric.validatePutRichieste(xRequestId, xForwardedFor,
				xCodiceServizio, shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest, richiesta,
				idTitolo, idRapporto, idAsl, idRelazione, idValutazione, idContratto, numeroRichiesta, richiestaDb);
		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "errore in validate");
	}
}
