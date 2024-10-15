/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.business.be.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonoresbo.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbo.dto.Errore;
import it.csi.buonores.buonoresbo.dto.ModelSportello;
import it.csi.buonores.buonoresbo.dto.UserInfo;
import it.csi.buonores.buonoresbo.exception.DatabaseException;
import it.csi.buonores.buonoresbo.filter.IrideIdAdapterFilter;
import it.csi.buonores.buonoresbo.integration.dao.custom.CodParametroDao;
import it.csi.buonores.buonoresbo.integration.dao.custom.OperatoreRegionaleDao;
import it.csi.buonores.buonoresbo.integration.dao.custom.RicercaDao;
import it.csi.buonores.buonoresbo.util.Constants;
import it.csi.buonores.buonoresbo.util.validator.impl.ValidateGenericImpl;

@Service
public class OperatoreRegionaleService extends BaseService {

	@Autowired
	OperatoreRegionaleDao operatoreRegionaleDao;

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	CodParametroDao codParametroDao;

	@Autowired
	RicercaDao ricercaDao;

	public Response creaSportello(ModelSportello nSportello, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Errore error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			// Ottengo la lista degli sportelli
			List<ModelSportello> listaSportelli = ricercaDao.selectSportelli();
			ModelSportello ultimoSportello = listaSportelli.get(0);
			Date dataFineUltimoSportello = dateFormat.parse(ultimoSportello.getDataFine());
			Calendar c1Usportello = Calendar.getInstance();
			c1Usportello.setTime(dataFineUltimoSportello);

			// Controlli validazione date nuovo sportello
			Date dataInizio = dateFormat.parse(nSportello.getDataInizio());
			Date dataFine = dateFormat.parse(nSportello.getDataFine());
			Calendar c1Nsportello = Calendar.getInstance();
			Calendar c2Nsportello = Calendar.getInstance();
			c1Nsportello.setTime(dataInizio);
			c2Nsportello.setTime(dataFine);

			// Comparazione date
			int compareResult1 = c1Usportello.compareTo(c1Nsportello);
			int compareResult2 = c1Nsportello.compareTo(c2Nsportello);

			// Se DataFine uSportello e' precedente a DataInizio nSportello
			// Se DataInizio e' precedente a DataFine
			if (compareResult1 < 0 && compareResult2 < 0) {
				// Creo nuovo sportello
				operatoreRegionaleDao.creaSportello(nSportello, userInfo.getCodFisc());
				logInfo(metodo, "Sportello inserito correttamente");
				// Init delle directory su server
				initDirectory(nSportello.getCodSportello());
				logInfo(metodo, "Directory create con successo");
				return Response.ok().entity(true).build();
			} else {
				logInfo(metodo, "Errore: datInizio nuovo sportello errata");
				return Response.ok().entity(false).build();
			}
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	private void initDirectory(String sportelloCod) throws DatabaseException {
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();

		String alfabeto[] = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
				"S", "T", "U", "V", "W", "X", "Y", "Z" };
		// Cartella di root del S.O. del server
		StringBuilder path = new StringBuilder();
		path.append(codParametroDao.selectValoreParametroFromCod(Constants.PATH_ARCHIVIAZIONE,
				Constants.PARAMETRO_GENERICO)).append("/");
		try {
			path.append(sportelloCod);
			// Creazione cartella dello sportello
			File dir = new File(path.toString());
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String initialPath = path.toString();
			for (String lettera : alfabeto) {
				path.append(File.separator + lettera);
				dir = new File(path.toString());
				if (!dir.exists()) {
					dir.mkdirs();
				}
				path.delete(0, path.length());
				path.append(initialPath);
			}
		} catch (Exception e) {
			handleException(metodo, e);
		}
	}

}
