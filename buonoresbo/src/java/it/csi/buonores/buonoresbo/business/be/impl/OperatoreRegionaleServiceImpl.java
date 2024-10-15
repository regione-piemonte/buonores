/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonores.buonoresbo.business.be.OperatoreRegionaleAPI;
import it.csi.buonores.buonoresbo.business.be.service.OperatoreRegionaleService;
import it.csi.buonores.buonoresbo.dto.ModelSportello;

@Component
public class OperatoreRegionaleServiceImpl implements OperatoreRegionaleAPI {
	@Autowired
	OperatoreRegionaleService operatoreRegionaleService;

	@Override
	public Response creaSportello(ModelSportello nSportello, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return operatoreRegionaleService.creaSportello(nSportello, securityContext, httpHeaders, httpRequest);
	}

}
