/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.business.be.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonoresbff.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbff.business.be.service.util.FilesEncrypt;
import it.csi.buonores.buonoresbff.dto.Errore;
import it.csi.buonores.buonoresbff.dto.ErroreDettaglio;
import it.csi.buonores.buonoresbff.dto.ModelAllegato;
import it.csi.buonores.buonoresbff.dto.custom.ModelGetAllegato;
import it.csi.buonores.buonoresbff.dto.custom.ResponseGetAllegato;
import it.csi.buonores.buonoresbff.exception.DatabaseException;
import it.csi.buonores.buonoresbff.exception.ResponseErrorException;
import it.csi.buonores.buonoresbff.integration.dao.custom.AllegatoBuonoDao;
import it.csi.buonores.buonoresbff.integration.dao.custom.CodParametroDao;
import it.csi.buonores.buonoresbff.integration.service.LogAuditService;
import it.csi.buonores.buonoresbff.util.Constants;
import it.csi.buonores.buonoresbff.util.enumerator.CodeErrorEnum;

@Service
public class AllegatoBuonoService extends BaseService {

	@Autowired
	LogAuditService logaudit;

	@Autowired
	private FilesEncrypt filesEncrypt;

	@Autowired
	AllegatoBuonoDao allegatoBuonoDao;

	@Autowired
	CodParametroDao parametroDao;

	private void validateAllegato(String xForwardedFor, String shibIdentitaCodiceFiscale,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws DatabaseException, ResponseErrorException {
		List<ErroreDettaglio> listError = validateGeneric.validateLight(shibIdentitaCodiceFiscale, xForwardedFor,
				securityContext, httpHeaders, httpRequest);

		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "errore in validate");
	}

	public Response getAllegatoBuono(Integer idAllegato, String xForwardedFor, String shibIdentitaCodiceFiscale,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			validateAllegato(xForwardedFor, shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);

			ResponseGetAllegato allegatoContratto = new ResponseGetAllegato();
			allegatoContratto = getAllegatoBuonoFileSystem(idAllegato);
			if (allegatoContratto == null) {
				generateResponseErrorException(CodeErrorEnum.ERR11, HttpStatus.NOT_FOUND,
						"allegato inesistente", String.valueOf(idAllegato));
			} else {
				return Response.status(200).entity(allegatoContratto.getAllegato())
						.header("Content-Disposition",
								"attachment; filename=\"" + allegatoContratto.getFileName() + "\"")
						.build();
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

	private ResponseGetAllegato getAllegatoBuonoFileSystem(Integer idAllegato) throws DatabaseException, IOException {
		ModelGetAllegato allegato = allegatoBuonoDao.selectAllegatoBuono(idAllegato);
		if (allegato == null) {
			return null;
		}

		ResponseGetAllegato getAllegato = new ResponseGetAllegato();
		File file = new File(allegato.getFilePath() + File.separator + allegato.getFileName());
		byte[] bytes = Files.readAllBytes(file.toPath());

		byte[] allegatoDecifrato = filesEncrypt.creaFileDeCifratoByte(Cipher.DECRYPT_MODE, bytes);
		getAllegato.setAllegato(allegatoDecifrato);
		getAllegato.setFileName(allegato.getFileName());
		logInfo("getAllegatoBuonoFileSystem: ", "file decifrato e recuperato, nome_file: " + allegato.getFileName());
		return getAllegato;
	}

	private void validatePostAllegato(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest, byte[] allegato, String xFilenameOriginale)
			throws DatabaseException, ResponseErrorException {
		List<ErroreDettaglio> listError = validateGeneric.validatePostAllegati(xRequestId, xForwardedFor,
				xCodiceServizio, shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest, allegato,
				xFilenameOriginale);

		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "errore in validate");
	}

	public Response addAllegatoContratto(String tipoAllegato, String numeroDomanda, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String shibIdentitaCodiceFiscale, String xFilenameOriginale,
			String contentType, byte[] allegato, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();

		try {
			validatePostAllegato(xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale,
					securityContext, httpHeaders, httpRequest, allegato, xFilenameOriginale);

			String richiedenteCf = allegatoBuonoDao.selectCfRichiedenteNumeroDomanda(numeroDomanda);
			ModelAllegato archiviadomanda = new ModelAllegato();
			if (richiedenteCf != null) {
				/*
				 * Controllo per error 403, l'utente non ha i permessi necessari per
				 * visualizzare queste risorse.
				 */
				if (!richiedenteCf.equals(shibIdentitaCodiceFiscale)) {
					logError("Il codice fiscale del richiedente della domanda non corrisponde", tipoAllegato);
					generateResponseErrorException(CodeErrorEnum.ERR12, HttpStatus.BAD_REQUEST,
							"Il codice fiscale del richiedente della domanda non corrisponde", tipoAllegato);
				}

				// Archivio l'allegato
				archiviadomanda = archiviaAllegato(allegato, richiedenteCf, tipoAllegato, numeroDomanda, contentType,
						xFilenameOriginale.replaceAll("\\s+", ""), shibIdentitaCodiceFiscale);
			} else {
				logError("Buono inesistente per la domanda ", numeroDomanda);
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND,
						"Buono inesistente", numeroDomanda);
			}

			if (archiviadomanda != null) {
				return Response.status(200).entity(archiviadomanda).build();
			} else {
				logError("Allegato non salvato nel filesystem per errori ", tipoAllegato);
				generateResponseErrorException(CodeErrorEnum.ERR16, HttpStatus.INTERNAL_SERVER_ERROR,
						"Allegato non salvato", tipoAllegato);
			}

		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (DuplicateKeyException e) {
			error = handleDuplicateKeyException(metodo, e);
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	private void createFolder(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
			logInfo("creo cartella", dir.getPath());
		}
	}

	private ModelAllegato archiviaAllegato(byte[] allegato, String cfRichiedente, String tipoAllegato,
			String numeroDomanda, String contentType, String XFilenameOriginale, String shibIdentitaCodiceFiscale)
			throws DatabaseException, IOException, DuplicateKeyException, ResponseErrorException {
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();

		String path = parametroDao.selectValoreParametroFromCod(Constants.PATH_ARCHIVIAZIONE,
				Constants.PARAMETRO_GENERICO);
		ModelAllegato allegatoResponse = new ModelAllegato();
		Long allegatoId = null;

		path += File.separator;

		/*
		 * Genero/navigo il PATH per l'inserimento dell'allegato
		 * {sportello}/{lettera}/{numero_domanda}/{buono_cod}/{
		 * tipologia_allegato_idallegato}
		 */

		String sportello = allegatoBuonoDao.selectSportelloCodFromNumeroDomanda(numeroDomanda);
		path += sportello;
		createFolder(path);

		char primaLetteraRaw = cfRichiedente.charAt(0); // Prima lettere del cfRichiedente
		String primaLettera = Character.toString(primaLetteraRaw).toUpperCase();
		path += File.separator + primaLettera;
		createFolder(path);

		path += File.separator + numeroDomanda.toUpperCase();
		createFolder(path);

		String buonoCod = allegatoBuonoDao.selectBuonoCodFromNumeroDomanda(numeroDomanda);
		if (buonoCod == null) {
			return null;
		}
		path += File.separator + buonoCod.toUpperCase();
		createFolder(path);

		path += File.separator + tipoAllegato;
		String fileName = tipoAllegato + "_" + XFilenameOriginale;

		if (allegatoBuonoDao.checkEsistenzaTipoAllegato(tipoAllegato)) {
			allegatoId = allegatoBuonoDao.insertAllegato(fileName, contentType, path, tipoAllegato,
					shibIdentitaCodiceFiscale,
					shibIdentitaCodiceFiscale);
			path += "_" + allegatoId;
			allegatoBuonoDao.updateAllegato(fileName, contentType, path, tipoAllegato, shibIdentitaCodiceFiscale,
					allegatoId);
			logInfo("allegato non esiste " + tipoAllegato, path + "/" + XFilenameOriginale);
			logInfo("inserisco allegato su db " + tipoAllegato, path + "/" + XFilenameOriginale);
		} else {
			logError(metodo, "Errore riguardante database:", "allegatoTipo inesistente");
			generateResponseErrorException(CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR,
					"L'allegatoTipo passato non e' corretto", ": errore tipoAllegato");
		}

		createFolder(path);

		File file = new File(path + File.separator + fileName);

		logInfo("cifro allegato ", tipoAllegato);
		byte[] allegatoCifrato = filesEncrypt.creaFileCifratoByte(Cipher.ENCRYPT_MODE, allegato);
		if (allegatoCifrato != null) {
			try (OutputStream out = new FileOutputStream(file)) {
				out.write(allegatoCifrato, 0, allegatoCifrato.length);
			}
			logInfo("scrivo su su file system allegato cifrato ", tipoAllegato);
			allegatoResponse.setTipo(tipoAllegato);
			allegatoResponse.setFilename(fileName);
			allegatoResponse.setId(allegatoId);
			return allegatoResponse;
		} else {
			logInfo("errore in cifratura allegato non scritto su db ", tipoAllegato);
			ModelAllegato modelAllegatoReturn = null;
			return modelAllegatoReturn;
		}
	}
}
