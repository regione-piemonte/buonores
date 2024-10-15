/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.business.be.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonoresbo.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbo.business.be.service.util.FilesEncrypt;
import it.csi.buonores.buonoresbo.dto.Errore;
import it.csi.buonores.buonoresbo.dto.ModelRichiesta;
import it.csi.buonores.buonoresbo.dto.custom.ModelGetAllegato;
import it.csi.buonores.buonoresbo.dto.custom.ResponseGetAllegato;
import it.csi.buonores.buonoresbo.exception.DatabaseException;
import it.csi.buonores.buonoresbo.exception.ResponseErrorException;
import it.csi.buonores.buonoresbo.integration.dao.custom.AllegatoDao;
import it.csi.buonores.buonoresbo.integration.dao.custom.CodParametroDao;
import it.csi.buonores.buonoresbo.integration.dao.custom.RichiesteDao;
import it.csi.buonores.buonoresbo.integration.service.LogAuditService;
import it.csi.buonores.buonoresbo.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoresbo.util.validator.impl.ValidateGenericImpl;

@Service
public class AllegatiService extends BaseService {

	@Autowired
	LogAuditService logaudit;

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	private FilesEncrypt filesEncrypt;

	@Autowired
	CodParametroDao parametroDao;

	@Autowired
	AllegatoDao allegatoDao;

	@Autowired
	RichiesteDao richiesteDao;

	public Response getAllegato(String tipoAllegato, String numeroRichiesta, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {

			ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			ResponseGetAllegato domanda = new ResponseGetAllegato();
			if (richiesta != null) {
				domanda = getDomanda(richiesta, tipoAllegato);
				if (domanda == null) {
					generateResponseErrorException(CodeErrorEnum.ERR11, HttpStatus.NOT_FOUND,
							"allegato inesistente", tipoAllegato, numeroRichiesta);
				}
			} else {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND,
						"domanda inesistente", numeroRichiesta);
			}

			return Response.status(200).entity(domanda.getAllegato())
					.header("Content-Disposition", "attachment; filename=\"" + domanda.getFileName() + "\"").build();

		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	private ResponseGetAllegato getDomanda(ModelRichiesta richiesta, String tipoAllegato)
			throws DatabaseException, IOException {
		ModelGetAllegato allegato = allegatoDao.selectGetAllegato(tipoAllegato, richiesta.getDomandaDetId());
		if (allegato == null) {
			return null;
		}
		ResponseGetAllegato getAllegato = new ResponseGetAllegato();
		File file = new File(allegato.getFilePath() + File.separator + allegato.getFileName());

		byte[] bytes = Files.readAllBytes(file.toPath());

		byte[] allegatoDecifrato = filesEncrypt.creaFileDeCifratoByte(Cipher.DECRYPT_MODE, bytes);
		getAllegato.setAllegato(allegatoDecifrato);
		getAllegato.setFileName(allegato.getFileName());

		return getAllegato;
	}
}
