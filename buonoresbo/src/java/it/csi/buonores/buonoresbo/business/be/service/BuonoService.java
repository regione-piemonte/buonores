/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.business.be.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.buonores.buonoresbo.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbo.business.be.service.util.FilesEncrypt;
import it.csi.buonores.buonoresbo.dto.Errore;
import it.csi.buonores.buonoresbo.dto.ErroreDettaglio;
import it.csi.buonores.buonoresbo.dto.ModelBuonoResponse;
import it.csi.buonores.buonoresbo.dto.ModelFiltriRicercaBuoni;
import it.csi.buonores.buonoresbo.dto.ModelStatoBuono;
import it.csi.buonores.buonoresbo.dto.UserInfo;
import it.csi.buonores.buonoresbo.dto.custom.ModelGetAllegato;
import it.csi.buonores.buonoresbo.dto.custom.ResponseGetAllegato;
import it.csi.buonores.buonoresbo.dto.custom.ResponseGetRendicontazione;
import it.csi.buonores.buonoresbo.exception.DatabaseException;
import it.csi.buonores.buonoresbo.exception.ResponseErrorException;
import it.csi.buonores.buonoresbo.filter.IrideIdAdapterFilter;
import it.csi.buonores.buonoresbo.integration.dao.custom.BuonoDao;
import it.csi.buonores.buonoresbo.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoresbo.util.rest.ResponseRest;
import it.csi.buonores.buonoresbo.util.validator.impl.ValidateGenericImpl;

@Service
public class BuonoService extends BaseService {

	@Autowired
	BuonoDao buonoDao;

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	private ServizioRestService restSrv;

	@Autowired
	private FilesEncrypt filesEncrypt;

	private void validateRicercaBuoni(ModelFiltriRicercaBuoni filtri) throws DatabaseException, ResponseErrorException {
		List<ErroreDettaglio> listError = validateGeneric.validateRicercaBuoni(filtri);
		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "errore in validate");
	}

	public Response ricercaBuoni(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest,
			ModelFiltriRicercaBuoni filtri) {
		List<ModelBuonoResponse> lista = new ArrayList<ModelBuonoResponse>();
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			validateRicercaBuoni(filtri);
			lista = buonoDao.selectRicercaBuoni(filtri);
			return Response.ok().entity(lista).build();
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	public Response getStatiBuono(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		List<ModelStatoBuono> listaStatiBuono = new ArrayList<ModelStatoBuono>();
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			listaStatiBuono = buonoDao.selectStatiBuono();
			return Response.ok().entity(listaStatiBuono).build();
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	private ResponseGetAllegato getAllegatoBuonoFileSystem(Integer idAllegato) throws DatabaseException, IOException {
		ModelGetAllegato allegato = buonoDao.selectAllegatoBuono(idAllegato);
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

	public Response getAllegatoBuono(Integer allegatoId, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();

		try {
			ResponseGetAllegato allegatoContratto = new ResponseGetAllegato();
			allegatoContratto = getAllegatoBuonoFileSystem(allegatoId);
			if (allegatoContratto == null) {
				generateResponseErrorException(CodeErrorEnum.ERR11, HttpStatus.NOT_FOUND,
						"allegato inesistente", String.valueOf(allegatoId));
			}

			return Response.status(200).entity(allegatoContratto.getAllegato())
					.header("Content-Disposition", "attachment; filename=\"" + allegatoContratto.getFileName() + "\"")
					.build();
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	/*
	 * Servizio per ottenere gli allegati della rendicontazione riferiti ai buoni
	 */
	public Response getRendicontazioneBuono(Integer allegatoId, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		ObjectMapper mapper = new ObjectMapper();

		try {
			ResponseGetRendicontazione allegatoContratto = new ResponseGetRendicontazione();
			ResponseRest response = restSrv.getAllegatoRendicontazioniRsa(securityContext, httpHeaders, httpRequest,
					allegatoId, userInfo.getCodFisc());

			if (response == null || response.getStatusCode() != HttpStatus.OK.value()) {
				// errore
				String json = (response != null) ? response.getJson() : new Errore().toString();
				Errore errore = mapper.readValue(json, Errore.class);
				generateResponseErrorException(errore.getDetail(),
						HttpStatus.valueOf(errore.getStatus()), errore.getDetail().toString());
			} else {
				String resp = response.getJson();

				allegatoContratto = mapper.readValue(resp, new TypeReference<ResponseGetRendicontazione>() {
				});

				return Response.status(200).entity(allegatoContratto.getFile())
						.header("Content-Disposition",
								"attachment; filename=\"" + allegatoContratto.getFileName() + "\"")
						// .header("Content-Type", allegatoContratto.getFileType())
						// .header("Content-Length", allegatoContratto.getFileSize())
						.build();
			}

			return Response.ok().entity(allegatoContratto).build();
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
