/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.business.be.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.xml.ws.WebServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonoresbff.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbff.dto.Errore;
import it.csi.buonores.buonoresbff.dto.ErroreDettaglio;
import it.csi.buonores.buonoresbff.dto.ModelPersona;
import it.csi.buonores.buonoresbff.exception.DatabaseException;
import it.csi.buonores.buonoresbff.exception.ResponseErrorException;
import it.csi.buonores.buonoresbff.external.anagraficaservice.InterrogaMefEsenredd1Soap;
import it.csi.buonores.buonoresbff.external.anagraficaservice.InterrogaMefEsenreddRes;
import it.csi.buonores.buonoresbff.external.anagraficaservice.InterrogaMefEsenreddResponseBody;
import it.csi.buonores.buonoresbff.integration.dao.custom.CodParametroDao;
import it.csi.buonores.buonoresbff.util.Constants;
import it.csi.buonores.buonoresbff.util.Util;
import it.csi.buonores.buonoresbff.util.enumerator.CodeErrorEnum;

@Service
public class AnagraficaService extends BaseService {

	@Autowired
	InterrogaMefEsenredd1Soap anagraficaService;

	@Autowired
	CodParametroDao codParametroDao;

	public Response anagraficaCfGet(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest, String cf) {

		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			checkValidateAnagrafica(xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale,
					securityContext, httpHeaders, httpRequest, cf);
			boolean verificasechiamare = true;
			String parametro = codParametroDao.selectValoreParametroFromCod(Constants.CHIAMA_INTERROGAMEF,
					Constants.PARAMETRO_GENERICO);
			if (parametro != null) {
				verificasechiamare = parametro.equalsIgnoreCase("TRUE") ? true : false;
			}
			InterrogaMefEsenreddRes response = new InterrogaMefEsenreddRes();
			if (verificasechiamare) {
				response = anagraficaService.interrogaMefEsenredd(cf);
			} else {
				response = buildMock(cf);
			}

			logInfo(metodo, "Risposta da Mef: " + response.toString());

			// modifica TAG 007 buonodom
			if (response.getEsitomef().contains("Codice di errore: 17")) {
				// errore deve continuare con il solo cf Codice di errore: 17
				// L'assistito risulta con indirizzo assente. Invitare il soggetto a recarsi
				// presso ufficio anagrafico del comune di competenza
				logError(metodo, "Errore con Codice 17 per cf " + cf);
				ModelPersona datianagrafici = buildModelPersonaWithError17(cf);
				return Response.ok().entity(datianagrafici).build();
			} else if (!response.getEsitomef().equalsIgnoreCase("OK")) {
				logError(metodo, "Errore durante la chiamata a InterrogaMef: ");
				generateResponseErrorException(CodeErrorEnum.ERR07, HttpStatus.NOT_FOUND,
						"errore in interroga mef", cf);
			} else if (response.getBody() != null) {
				ModelPersona datianagrafici = buildModelPersonaFromInterrogaMefEsenreddRes(response);
				return Response.ok().entity(datianagrafici).build();
			}
		} catch (WebServiceException e) {
			error = handleWebServiceException(metodo, e);
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}
		return error.generateResponseError();
	}

	private void checkValidateAnagrafica(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest, String cf) throws DatabaseException, ResponseErrorException {
		// validate
		List<ErroreDettaglio> listError = validateGeneric.validateAnagrafica(shibIdentitaCodiceFiscale,
				xRequestId, xForwardedFor, xCodiceServizio, cf, securityContext, httpHeaders,
				httpRequest);

		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "errore in validate");
	}

	private ModelPersona buildModelPersonaFromInterrogaMefEsenreddRes(InterrogaMefEsenreddRes response) {
		ModelPersona datianagrafici = new ModelPersona();
		datianagrafici.setCf(response.getBody().getCodicefiscale());
		datianagrafici.setCognome(response.getBody().getCognome());
		datianagrafici.setNome(response.getBody().getNome());
		datianagrafici.setComuneNascita(response.getBody().getComunenasc());
		datianagrafici.setComuneResidenza(response.getBody().getComuneResidenza());
		datianagrafici.setDataNascita(
				Util.getData(response.getBody().getDatanascita(), Constants.DATE_FORMAT_ITALIAN_PATTERN));
		datianagrafici.setIndirizzoResidenza(response.getBody().getIndirizzoResidenza());
		datianagrafici.setProvinciaNascita(response.getBody().getProvincianasc());
		datianagrafici.setProvinciaResidenza(response.getBody().getProvinciaResidenza());
		datianagrafici.setStatoNascita(response.getBody().getNazionenasc());
		datianagrafici.setDataDecesso(
				Util.getData(response.getBody().getDatadecesso(), Constants.DATE_FORMAT_ITALIAN_PATTERN));
		return datianagrafici;
	}

	private ModelPersona buildModelPersonaWithError17(String cf) {
		ModelPersona datianagrafici = new ModelPersona();
		datianagrafici.setCf(cf);
		datianagrafici.setCognome(null);
		datianagrafici.setNome(null);
		datianagrafici.setComuneNascita(null);
		datianagrafici.setComuneResidenza(null);
		datianagrafici.setDataNascita(null);
		datianagrafici.setIndirizzoResidenza(null);
		datianagrafici.setProvinciaNascita(null);
		datianagrafici.setProvinciaResidenza(null);
		datianagrafici.setStatoNascita(null);
		datianagrafici.setDataDecesso(null);
		return datianagrafici;
	}

	private InterrogaMefEsenreddRes buildMock(String cf) {
		InterrogaMefEsenreddRes response = new InterrogaMefEsenreddRes();
		response.setEsitomef("OK");
		InterrogaMefEsenreddResponseBody bodymock = new InterrogaMefEsenreddResponseBody();
		bodymock.setCodicefiscale(cf);
		bodymock.setCognome("CARICO");
		bodymock.setComunenasc("TORINO");
		bodymock.setComuneResidenza("TORINO");
		bodymock.setDatanascita("02/01/1990");
		bodymock.setIndirizzoResidenza("VIA ROMA 10");
		bodymock.setNazionenasc("ITALIA");
		bodymock.setNome("TEST");
		bodymock.setProvincianasc("TO");
		bodymock.setProvinciaResidenza("TO");
		response.setBody(bodymock);
		return response;
	}
}
