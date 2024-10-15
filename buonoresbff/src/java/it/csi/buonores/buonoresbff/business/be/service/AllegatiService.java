/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.business.be.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.List;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonoresbff.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbff.business.be.service.util.FilesEncrypt;
import it.csi.buonores.buonoresbff.dto.Errore;
import it.csi.buonores.buonoresbff.dto.ErroreDettaglio;
import it.csi.buonores.buonoresbff.dto.ModelAllegato;
import it.csi.buonores.buonoresbff.dto.custom.ModelGetAllegato;
import it.csi.buonores.buonoresbff.dto.custom.ModelRichiestaLight;
import it.csi.buonores.buonoresbff.dto.custom.ModelSportelloExt;
import it.csi.buonores.buonoresbff.dto.custom.ResponseGetAllegato;
import it.csi.buonores.buonoresbff.exception.DatabaseException;
import it.csi.buonores.buonoresbff.exception.ResponseErrorException;
import it.csi.buonores.buonoresbff.integration.dao.custom.AllegatoDao;
import it.csi.buonores.buonoresbff.integration.dao.custom.CodParametroDao;
import it.csi.buonores.buonoresbff.integration.dao.custom.RichiesteDao;
import it.csi.buonores.buonoresbff.integration.dao.custom.SportelliDao;
import it.csi.buonores.buonoresbff.util.Constants;
import it.csi.buonores.buonoresbff.util.enumerator.CodeErrorEnum;

@Service
public class AllegatiService extends BaseService {

	@Autowired
	private FilesEncrypt filesEncrypt;

	@Autowired
	CodParametroDao parametroDao;

	@Autowired
	SportelliDao sportelliDao;

	@Autowired
	AllegatoDao allegatoDao;

	@Autowired
	RichiesteDao richiesteDao;

	public Response execute(String tipoAllegato, String numeroRichiesta,
			String xRequestId, String xForwardedFor, String xCodiceServizio, String shibIdentitaCodiceFiscale,
			String xFilenameOriginale,
			String contentType, byte[] allegato, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			validatePostAllegati(xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale,
					xFilenameOriginale, allegato, securityContext, httpHeaders, httpRequest);
			ModelRichiestaLight richiesta = richiesteDao.selectRichiestaLight(numeroRichiesta);
			ModelAllegato archiviadomanda = new ModelAllegato();
			if (richiesta != null) {
				if (!richiesta.getRichiedenteCf().equals(shibIdentitaCodiceFiscale)) {
					generateResponseErrorException(CodeErrorEnum.ERR12, HttpStatus.BAD_REQUEST,
							"Il codice fiscale del richiedente della domanda non corrisponde", numeroRichiesta);
				}
				archiviadomanda = archiviaDomanda(allegato, shibIdentitaCodiceFiscale, richiesta.getDomandaDetCod(),
						contentType, tipoAllegato, richiesta, xFilenameOriginale.replaceAll("\\s+", ""),
						numeroRichiesta);
			} else {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND,
						"domanda inesistente", numeroRichiesta);
			}
			if (archiviadomanda != null)
				return Response.ok().entity(archiviadomanda).build();
			else {
				logError("allegato non salvato nel filesystem per errori ", tipoAllegato);
				generateResponseErrorException(CodeErrorEnum.ERR16, HttpStatus.INTERNAL_SERVER_ERROR,
						"allegato non salvato", numeroRichiesta);
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

	private void validatePostAllegati(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, String xFilenameOriginale, byte[] allegato,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws DatabaseException, ResponseErrorException {
		// validate
		List<ErroreDettaglio> listError = validateGeneric.validatePostAllegati(xRequestId, xForwardedFor,
				xCodiceServizio, shibIdentitaCodiceFiscale, securityContext, httpHeaders,
				httpRequest, allegato, xFilenameOriginale);

		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "errore in validate");
	}

	public Response getAllegato(String tipoAllegato, String numeroRichiesta,
			String xForwardedFor, String shibIdentitaCodiceFiscale,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			validateGetAllegati(xForwardedFor, shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
			ModelRichiestaLight richiesta = richiesteDao.selectRichiestaLight(numeroRichiesta);
			ResponseGetAllegato domanda = new ResponseGetAllegato();
			if (richiesta != null) {
				if (!richiesta.getRichiedenteCf().equals(shibIdentitaCodiceFiscale)) {
					generateResponseErrorException(CodeErrorEnum.ERR12, HttpStatus.BAD_REQUEST,
							"Il codice fiscale del richiedente della domanda non corrisponde", numeroRichiesta);
				}
				domanda = getDomanda(richiesta, tipoAllegato);
				if (domanda == null) {
					generateResponseErrorException(CodeErrorEnum.ERR11, HttpStatus.NOT_FOUND,
							"allegato inesistente", tipoAllegato, numeroRichiesta);
				} else {
					return Response.ok().entity(domanda.getAllegato())
							.header("Content-Disposition", "attachment; filename=\"" + domanda.getFileName() + "\"")
							.header(HttpHeaders.CONTENT_TYPE, domanda.getMimeType()).build();
				}

			} else {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND,
						"domanda inesistente", numeroRichiesta);
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

	private void validateGetAllegati(String xForwardedFor, String shibIdentitaCodiceFiscale,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws DatabaseException, ResponseErrorException {
		// validate stessa del cronologia
		List<ErroreDettaglio> listError = validateGeneric.validateLight(shibIdentitaCodiceFiscale, xForwardedFor,
				securityContext, httpHeaders, httpRequest);

		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "errore in validate");
	}

	private void createFolder(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
			logInfo("creo cartella", dir.getPath());
		}
	}

	private ModelAllegato archiviaDomanda(byte[] domanda, String cfRichiedente, String detCod, String contentType,
			String tipoAllegato, ModelRichiestaLight richiesta, String XFilenameOriginale, String numeroRichiesta)
			throws DatabaseException, IOException {
		String path = parametroDao.selectValoreParametroFromCod(Constants.PATH_ARCHIVIAZIONE,
				Constants.PARAMETRO_GENERICO);
		if (path == null) {
			return null;
		}

		ModelSportelloExt sportello = new ModelSportelloExt();
		ModelAllegato allegato = new ModelAllegato();
		try {
			sportello = sportelliDao.selectSportelliCod(richiesta.getSportelloId());

			path += File.separator + sportello.getSportelloCod();
			createFolder(path);

			path += File.separator + richiesta.getRichiedenteCf().substring(0, 1).toUpperCase();
			createFolder(path);

			path += File.separator + numeroRichiesta.toUpperCase();
			createFolder(path);

			path += File.separator + detCod.toUpperCase();
			createFolder(path);

			String fileName = tipoAllegato + "_" + XFilenameOriginale;
			File file = new File(path + File.separator + fileName);

			if (allegatoDao.selectEsisteAllegato(tipoAllegato, richiesta.getSportelloId(), richiesta.getDomandaDetId(),
					detCod)) {
				ModelGetAllegato a = allegatoDao.selectAllegato(detCod, tipoAllegato, richiesta.getDomandaDetId());
				logInfo("allegato gia esistente " + tipoAllegato, a.getFilePath() + File.separator + a.getFileName());
				File f = new File(a.getFilePath() + File.separator + a.getFileName());

				boolean resultDelete = f.delete();
				if (resultDelete) {
					logInfo("cancello allegato su file system ", a.getFilePath() + File.separator + a.getFileName());
				}
				allegatoDao.updateAllegato(detCod, cfRichiedente, fileName, contentType, path, tipoAllegato);
				logInfo("aggiorno allegato su db " + tipoAllegato, a.getFilePath() + File.separator + a.getFileName());
			} else {
				logInfo("allegato non esiste " + tipoAllegato, path + File.separator + fileName);
				allegatoDao.insertAllegato(fileName, contentType,
						path, richiesta.getSportelloId(), richiesta.getDomandaDetId(), detCod, tipoAllegato,
						cfRichiedente, cfRichiedente);
				logInfo("inserisco allegato su db " + tipoAllegato, path + File.separator + fileName);
			}
			// cripto il file
			return cryptFile(domanda, tipoAllegato, allegato, fileName, file);

		} catch (Exception e) {
			logError("Archiviazione allegati", "Errore archivia allegati", e);
			return null;
		}
	}

	private ModelAllegato cryptFile(byte[] domanda, String tipoAllegato, ModelAllegato allegato, String fileName,
			File file) throws Exception {
		try (OutputStream out = new FileOutputStream(file)) {
			logInfo("cifro allegato ", tipoAllegato);
			byte[] domandaCifrata = filesEncrypt.creaFileCifratoByte(Cipher.ENCRYPT_MODE, domanda);
			if (domandaCifrata != null) {
				out.write(domandaCifrata, 0, domandaCifrata.length);
				out.close();
				logInfo("scrivo su file system allegato cifrato ", tipoAllegato);
				allegato.setTipo(tipoAllegato);
				allegato.setFilename(fileName);
				return allegato;
			} else {
				logInfo("errore in cifratura allegato non scritto su db ", tipoAllegato);
				out.close();
				return null;
			}
		} catch (Exception e) {
			logError("cryptFile", "Errore cifratura allegati", e);
			return null;
		}
	}

	private ResponseGetAllegato getDomanda(ModelRichiestaLight richiesta, String tipoAllegato)
			throws DatabaseException, IOException {
		ModelGetAllegato allegato = allegatoDao.selectGetAllegato(tipoAllegato, richiesta.getDomandaDetId());
		if (allegato == null) {
			return null;
		}
		ResponseGetAllegato getAllegato = new ResponseGetAllegato();
		File file = new File(allegato.getFilePath() + File.separator + allegato.getFileName());

		String mimeType = URLConnection.guessContentTypeFromName(file.getName());

		byte[] bytes = Files.readAllBytes(file.toPath());

		byte[] allegatoDecifrato = filesEncrypt.creaFileDeCifratoByte(Cipher.DECRYPT_MODE, bytes);
		getAllegato.setAllegato(allegatoDecifrato);
		getAllegato.setFileName(allegato.getFileName());
		getAllegato.setMimeType(mimeType);

		return getAllegato;
	}
}
