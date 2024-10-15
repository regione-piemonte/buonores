/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.business.be.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonoresbo.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbo.dto.Errore;
import it.csi.buonores.buonoresbo.dto.ModelProfili;
import it.csi.buonores.buonoresbo.dto.ModelRuolo;
import it.csi.buonores.buonoresbo.dto.ModelUserInfo;
import it.csi.buonores.buonoresbo.dto.ModelUserRuolo;
import it.csi.buonores.buonoresbo.dto.UserInfo;
import it.csi.buonores.buonoresbo.exception.DatabaseException;
import it.csi.buonores.buonoresbo.exception.ResponseErrorException;
import it.csi.buonores.buonoresbo.filter.IrideIdAdapterFilter;
import it.csi.buonores.buonoresbo.integration.dao.custom.LoginDao;
import it.csi.buonores.buonoresbo.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoresbo.util.validator.impl.ValidateGenericImpl;

@Service
public class LoginService extends BaseService {

	@Autowired
	LoginDao loginDao;

	@Autowired
	ValidateGenericImpl validateGeneric;

	public Response login(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		List<ModelUserRuolo> lista = new ArrayList<ModelUserRuolo>();
		Errore error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {

			// TEST, CF SOGGETTO GESTORE ENTE
			// userInfo.setCodFisc("ENTEAA10A11C000F");

			lista = loginDao.selectRuoli(userInfo.getCodFisc());

			if (lista == null || lista.isEmpty()) {
				generateResponseErrorException(CodeErrorEnum.ERR06, HttpStatus.BAD_REQUEST, "utente non presente",
						userInfo.getCodFisc());
			}
			ModelUserInfo utente = new ModelUserInfo();

			utente.setCodFisc(lista.get(0).getCodFisc());
			utente.setNome(lista.get(0).getNome());
			utente.setCognome(lista.get(0).getCognome());
			List<ModelRuolo> ruoli = new ArrayList<ModelRuolo>();
			for (ModelUserRuolo ruolo : lista) {
				ModelRuolo r = new ModelRuolo();
				r.setCodRuolo(ruolo.getCodRuolo());
				r.setDescRuolo(ruolo.getDescRuolo());
				List<ModelProfili> profili = loginDao.selectProfili(r.getCodRuolo());
				for (ModelProfili profilo : profili) {
					profilo.setListaAzioni(loginDao.selectAzioni(profilo.getCodProfilo()));
				}
				r.setListaProfili(profili);
				ruoli.add(r);
			}
			utente.setListaRuoli(ruoli);

			utente.setListaEntiGestore(loginDao.selectEntiGestore(userInfo.getCodFisc()));

			return Response.ok().entity(utente).build();
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	public Response logout(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		Errore error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			httpRequest.getSession(false).setMaxInactiveInterval(1);
			httpRequest.getSession().removeAttribute("XSRF_SESSION_TOKEN");
			httpRequest.getSession().removeAttribute("X-XSRF-TOKEN");
			httpRequest.getSession().removeAttribute("XSRF-TOKEN");
			httpRequest.getSession().removeAttribute(IrideIdAdapterFilter.IRIDE_ID_SESSIONATTR);
			httpRequest.getSession().removeAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
			httpRequest.getSession().removeAttribute(IrideIdAdapterFilter.AUTH_ID_MARKER);
			// javax.servlet.http.Cookie[] cookies = httpRequest.getCookies();
			NewCookie[] elencocookie = null;
			// if (cookies!=null) {
			// elencocookie = new NewCookie[cookies.length+1];
			// logInfo("Cookie Numero ",Integer.toString(cookies.length));
			// for (int i = 0; i < cookies.length; i++) {
			// logInfo("Cookie " + i,cookies[i].getName() + " " + cookies[i].getValue());
			// Cookie cookie = new Cookie(cookies[i].getName(), "deleted");
			// logInfo("Cookie " + i,cookie.getName() + " " + cookie.getValue());
			// NewCookie newCookie = new NewCookie(cookie, null, 0, new Date(0),
			// securityContext.isSecure(), true);
			// elencocookie[i] = newCookie;
			// }
			// Cookie cookie = new Cookie("XSRF-TOKEN", "deleted");
			// logInfo("Cookie ",cookie.getName() + " " + cookie.getValue());
			// NewCookie newCookie = new NewCookie(cookie, null, 0, new Date(0),
			// securityContext.isSecure(), true);
			// elencocookie[cookies.length] = newCookie;
			// cancello solo Sessionid e XSRF-TOKEN
			elencocookie = new NewCookie[2];
			Cookie cookie = new Cookie("JSESSIONID", "deleted");
			NewCookie newCookie = new NewCookie(cookie, null, 0, new Date(0), securityContext.isSecure(), true);
			elencocookie[0] = newCookie;
			cookie = new Cookie("XSRF-TOKEN", "deleted");
			newCookie = new NewCookie(cookie, null, 0, new Date(0), securityContext.isSecure(), true);
			elencocookie[1] = newCookie;
			// }
			httpRequest.getSession().invalidate();
			return Response.ok().entity(userInfo).cookie(elencocookie).build();
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}
}
