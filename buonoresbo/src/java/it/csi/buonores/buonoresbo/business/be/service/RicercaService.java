/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.business.be.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonoresbo.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbo.dto.Errore;
import it.csi.buonores.buonoresbo.dto.ErroreDettaglio;
import it.csi.buonores.buonoresbo.dto.ModelDomandeAperta;
import it.csi.buonores.buonoresbo.dto.ModelFiltriDomandeAperte;
import it.csi.buonores.buonoresbo.dto.ModelSportelli;
import it.csi.buonores.buonoresbo.dto.ModelSportello;
import it.csi.buonores.buonoresbo.dto.ModelStati;
import it.csi.buonores.buonoresbo.exception.DatabaseException;
import it.csi.buonores.buonoresbo.exception.ResponseErrorException;
import it.csi.buonores.buonoresbo.integration.dao.custom.RicercaDao;
import it.csi.buonores.buonoresbo.util.validator.impl.ValidateGenericImpl;

@Service
public class RicercaService extends BaseService {

	@Autowired
	RicercaDao ricercaDao;

	@Autowired
	ValidateGenericImpl validateGeneric;

	public Response getStati(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest,
			String tipoMenu) {
		Errore error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			List<ModelStati> listaStati = ricercaDao.selectStati(tipoMenu);

			return Response.ok().entity(listaStati).build();
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	public Response getSportelli(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Errore error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			List<ModelSportello> listaSportelli = ricercaDao.selectSportelli();
			ModelSportelli sportelli = new ModelSportelli();
			sportelli.setSportelli(listaSportelli);
			ModelSportello s = listaSportelli.stream().filter(f -> f.isCorrente()).findFirst().orElse(null);
			if (s != null) {
				sportelli.setSportelloCorrente(s);
			} else {
				sportelli.setSportelloCorrente(listaSportelli.get(0));
			}
			return Response.ok().entity(sportelli).build();
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	private void validateDomandeAperte(ModelFiltriDomandeAperte filtri)
			throws DatabaseException, ResponseErrorException {
		List<ErroreDettaglio> listError = validateGeneric.validateDomandeAperte(filtri);
		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "errore in validate");
	}

	public Response ricercaDomandeAperte(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest,
			ModelFiltriDomandeAperte filtri) {
		List<ModelDomandeAperta> lista = new ArrayList<ModelDomandeAperta>();
		Errore error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			validateDomandeAperte(filtri);
			List<ModelStati> stati = ricercaDao.selectStati(filtri.getMenu());
			lista = ricercaDao.selectDomandeAperte(filtri, stati);

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
}
