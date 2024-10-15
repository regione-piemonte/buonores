/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.business.be.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonoressrv.business.be.service.base.BaseService;
import it.csi.buonores.buonoressrv.dto.Errore;
import it.csi.buonores.buonoressrv.dto.ModelDecodifica;
import it.csi.buonores.buonoressrv.dto.ModelRichiesta;
import it.csi.buonores.buonoressrv.dto.ModelSportello;
import it.csi.buonores.buonoressrv.exception.DatabaseException;
import it.csi.buonores.buonoressrv.exception.ResponseErrorException;
import it.csi.buonores.buonoressrv.integration.dao.custom.CodParametroDao;
import it.csi.buonores.buonoressrv.integration.dao.custom.RichiesteDao;
import it.csi.buonores.buonoressrv.integration.dao.custom.SportelliDao;
import it.csi.buonores.buonoressrv.integration.service.util.FilesEncrypt;
import it.csi.buonores.buonoressrv.util.Constants;
import it.csi.buonores.buonoressrv.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoressrv.util.validator.impl.ValidateGenericImpl;

@Service
public class CryptFileService extends BaseService {

	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	CodParametroDao parametroDao;

	@Autowired
	SportelliDao sportelliDao;

	@Autowired
	private FilesEncrypt filesEncrypt;

	public Response uploadFileDomanda(MultipartFormDataInput input, String numeroRichiesta, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String shibIdentitaCodiceFiscale,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws IOException {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		ModelRichiesta richiesta = new ModelRichiesta();
		boolean entrato = false;
		
		try {
			richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			if (richiesta == null) {
				logError(metodo, "Errore domanda non trovata");
				generateResponseErrorException("Errore domanda non trovata", CodeErrorEnum.ERR03, 
						HttpStatus.NOT_FOUND, "errore domanda non trovata");
			}else {
				List<ModelDecodifica> tipiAllegato = richiesteDao.selectTipiAllegati();
				for (ModelDecodifica tipoall : tipiAllegato) {
					List<InputPart> inputParts = uploadForm.get(tipoall.getCodice());
					if (inputParts != null) {
						entrato = true;
						for (InputPart inputPart : inputParts) {
							MultivaluedMap<String, String> header = inputPart.getHeaders();
							String fileName = getFileName(header);
							InputStream inputStream = inputPart.getBody(InputStream.class, null);
							byte[] bytes = IOUtils.toByteArray(inputStream);
							archiviafilecifrato(bytes, numeroRichiesta, richiesta.getRichiedente().getCf(),
									richiesta.getDomandaDetCod(), richiesta.getSportelloId(), richiesta.getDomandaDetId(),
									fileName, tipoall.getCodice(), header.getFirst("Content-Type"));
							inputStream.close();
						}
					}
				}
				if (entrato) {
					return Response.ok().entity(richiesta).build();
				} else {
					return Response.ok().entity("Nessun tipo allegato trovato").build();
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

	private String getFileName(MultivaluedMap<String, String> header) {
		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {
				String[] name = filename.split("=");
				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}
		return "unknown";
	}

	public void archiviafilecifrato(byte[] domanda, String numerodomanda, String richiedente, String detCod,
			BigDecimal sportelloid, BigDecimal domandadetid, String nomeFile, String tipoAllegato, String contentType)
			throws DatabaseException, IOException {
		String path = parametroDao.selectValoreParametroFromCod(Constants.PATH_ARCHIVIAZIONE,
				Constants.PARAMETRO_GENERICO);
		ModelSportello sportello = new ModelSportello();
		try {
			path += File.separator;
			sportello = sportelliDao.selectSportelli(sportelloid);
			path += sportello.getSportelloCod();
			createFolder(path);

			path += File.separator + richiedente.substring(0, 1).toUpperCase();
			createFolder(path);

			path += File.separator + numerodomanda.toUpperCase();
			createFolder(path);

			path += File.separator + detCod.toUpperCase();
			createFolder(path);

			File file = new File(path + File.separator + nomeFile);
			logInfo("scrivo allegato su db", path + " " + nomeFile);

			if (richiesteDao.selectEsisteAllegato(detCod, tipoAllegato))
				richiesteDao.updateAllegato(detCod, richiedente, nomeFile, contentType, path, tipoAllegato);
			else
				richiesteDao.insertAllegato(nomeFile, contentType, path, sportelloid, domandadetid, detCod,
						tipoAllegato, richiedente, richiedente);

			logInfo("creo file ", path + " " + nomeFile);
			try(OutputStream out = new FileOutputStream(file)){
				// cripto il file
				byte[] domandaCifrata = filesEncrypt.creaFileCifratoByte(Cipher.ENCRYPT_MODE, domanda);
				out.write(domandaCifrata, 0, domandaCifrata.length);
			}
		} catch (Exception e) {
			logError("Archiviazione file", "Errore archivia file", e);
		}
	}
	
	private void createFolder(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
			logInfo("creo cartella", dir.getPath());
		}
	}
}
