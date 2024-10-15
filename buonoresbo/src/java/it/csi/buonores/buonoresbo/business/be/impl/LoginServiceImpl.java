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

import it.csi.buonores.buonoresbo.business.be.LoginApi;
import it.csi.buonores.buonoresbo.business.be.service.LoginService;

@Component
public class LoginServiceImpl implements LoginApi {

	@Autowired
	LoginService loginService;

	@Override
	public Response login(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return loginService.login(securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response logout(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return loginService.logout(securityContext, httpHeaders, httpRequest);
	}
}
