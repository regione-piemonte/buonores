/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.business.be.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonoresbo.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbo.dto.Errore;
import it.csi.buonores.buonoresbo.dto.ModelInfoServizio;
import it.csi.buonores.buonoresbo.exception.DatabaseException;
import it.csi.buonores.buonoresbo.integration.dao.custom.CodParametroDao;
import it.csi.buonores.buonoresbo.util.Constants;
import it.csi.buonores.buonoresbo.util.enumerator.CodeErrorEnum;

@Service
public class ServizioAttivoService extends BaseService {

	@Autowired
	CodParametroDao codParametroDao;

	public Response execute(String xRequestId, String xForwardedFor, String xCodiceServizio,
			SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		ModelInfoServizio info = new ModelInfoServizio();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		Errore error = null;
		try {
			info.setData(new Date());
			info.setNome(Constants.COMPONENT_NAME);
			info.setDescrizione("Api per la gestione del backoffice del buono residenzialità");

			try {
				codParametroDao.selectValoreParametroFromCod(CodeErrorEnum.ERR01.getCode(),
						Constants.PARAMETRO_ERRORE_TIPO);
			} catch (DatabaseException e) {
				info.setServizioAttivo(false);
				log.error(metodo + " - errore su Servizio Attivo per Symon " + e);
				return Response.serverError().entity(info).build();
			}

			info.setServizioAttivo(true);
			return Response.ok().entity(info).build();
		} catch (Exception e) {
			error = handleException(metodo, e);
		}
		return error.generateResponseError();
	}
}