/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.business.be.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.buonores.buonoresbff.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbff.business.mock.MockStruttureRsaComponent;
import it.csi.buonores.buonoresbff.dto.Errore;
import it.csi.buonores.buonoresbff.dto.ErroreDettaglio;
import it.csi.buonores.buonoresbff.dto.ModelDecodifica;
import it.csi.buonores.buonoresbff.dto.ModelDecodificaRapporto;
import it.csi.buonores.buonoresbff.dto.ModelStruttura;
import it.csi.buonores.buonoresbff.dto.ModelStrutturaSedeLegale;
import it.csi.buonores.buonoresbff.dto.custom.ModelComuneExt;
import it.csi.buonores.buonoresbff.dto.custom.ModelStrutturaExt;
import it.csi.buonores.buonoresbff.exception.DatabaseException;
import it.csi.buonores.buonoresbff.exception.ResponseErrorException;
import it.csi.buonores.buonoresbff.integration.dao.custom.CodParametroDao;
import it.csi.buonores.buonoresbff.integration.dao.custom.GetListeDao;
import it.csi.buonores.buonoresbff.util.Constants;
import it.csi.buonores.buonoresbff.util.rest.ResponseRest;

@Service
public class DecodificaService extends BaseService {

	@Autowired
	GetListeDao listeDao;

	@Autowired
	ServizioRestService servizioRestService;

	@Autowired
	MockStruttureRsaComponent mockStruttureRsaComponent;

	@Autowired
	CodParametroDao codParametroDao;

	public Response decodificheAslGet(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();

		try {
			checkValidateListaDecodifica(xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale,
					securityContext, httpHeaders, httpRequest);
			List<ModelDecodifica> lista = listeDao.selectAsl();
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

	public Response decodificheStatiDomandaGet(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();

		try {
			checkValidateListaDecodifica(xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale,
					securityContext, httpHeaders, httpRequest);
			List<ModelDecodifica> lista = listeDao.selectStatiDomanda();
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

	public Response decodificheTipiAllegatoGet(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();

		try {
			checkValidateListaDecodifica(xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale,
					securityContext, httpHeaders, httpRequest);
			List<ModelDecodifica> lista = listeDao.selectTipiAllegati();
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

	public Response decodificheTipiContrattoGet(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();

		try {
			checkValidateListaDecodifica(xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale,
					securityContext, httpHeaders, httpRequest);
			List<ModelDecodifica> lista = listeDao.selectTipiContratti();
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

	public Response decodificheTipiRapportoGet(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();

		try {
			checkValidateListaDecodifica(xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale,
					securityContext, httpHeaders, httpRequest);
			List<ModelDecodificaRapporto> lista = listeDao.selectTipiRapporto();
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

	public Response decodificheTitoliStudioGet(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();

		try {
			checkValidateListaDecodifica(xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale,
					securityContext, httpHeaders, httpRequest);
			List<ModelDecodifica> lista = listeDao.selectTitoliStudio();
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

	public Response decodificheValutazioniMultidimensionaliGet(String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();

		try {
			checkValidateListaDecodifica(xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale,
					securityContext, httpHeaders, httpRequest);
			List<ModelDecodifica> lista = listeDao.selectValutazioniMultidimensionali();
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

	public Response decodificheTipiAllegatoBuonoGet(String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();

		try {
			checkValidateListaDecodifica(xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale,
					securityContext, httpHeaders, httpRequest);
			List<ModelDecodifica> lista = listeDao.selectTipiAllegatoBuono();
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

	public Response decodificheStruttureGet(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest, String comune, String denominazione, String indirizzo) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		ObjectMapper mapper = new ObjectMapper();

		try {
			checkValidateListaDecodificaStrutture(xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale,
					securityContext, httpHeaders, httpRequest, comune);
			String parametro = codParametroDao.selectValoreParametroFromCod(Constants.CHIAMA_STRUTTURE,
					Constants.PARAMETRO_GENERICO);
			Boolean chiamaStrutture = (parametro != null) ? Boolean.parseBoolean(parametro) : true;

			if (chiamaStrutture) {
				ResponseRest response = servizioRestService.getStruttureRsa(xRequestId, xForwardedFor, xCodiceServizio,
						securityContext, httpHeaders, httpRequest, denominazione, comune, indirizzo);

				if (response == null || response.getStatusCode() != HttpStatus.OK.value()) {
					// errore
					String json = (response != null) ? response.getJson() : new Errore().toString();
					Errore errore = mapper.readValue(json, Errore.class);
					generateResponseErrorException(errore.getDetail(),
							HttpStatus.valueOf(errore.getStatus()), errore.getDetail().toString());
				} else {
					String resp = response.getJson();

					List<ModelStrutturaExt> struttureExt = mapper.readValue(resp,
							new TypeReference<List<ModelStrutturaExt>>() {
							});

					List<ModelStruttura> strutture = new ArrayList<ModelStruttura>();
					struttureExt.forEach(s -> {
						strutture.add(fromModelStrutturaExtToModelStruttura(s));
					});

					return Response.ok().entity(strutture).build();
				}
			} else {
				return Response.ok().entity(mockStruttureRsaComponent.getStruttureByComune(comune)).build();
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

	public Response decodificheComuniGet(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		ObjectMapper mapper = new ObjectMapper();

		try {
			checkValidateListaDecodifica(xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale,
					securityContext, httpHeaders, httpRequest);

			String parametro = codParametroDao.selectValoreParametroFromCod(Constants.CHIAMA_STRUTTURE,
					Constants.PARAMETRO_GENERICO);
			Boolean chiamaStrutture = (parametro != null) ? Boolean.parseBoolean(parametro) : true;
			List<String> comuni = new ArrayList<String>();
			if (chiamaStrutture) {
				ResponseRest response = servizioRestService.getComuniRsa(xRequestId, xForwardedFor, xCodiceServizio,
						securityContext, httpHeaders, httpRequest);

				if (response == null || response.getStatusCode() != HttpStatus.OK.value()) {
					// errore
					String json = (response != null) ? response.getJson() : new Errore().toString();
					Errore errore = mapper.readValue(json, Errore.class);
					generateResponseErrorException(errore.getDetail(),
							HttpStatus.valueOf(errore.getStatus()), errore.getDetail().toString());
				} else {
					String resp = response.getJson();
					List<ModelComuneExt> comuniExt = mapper.readValue(resp, new TypeReference<List<ModelComuneExt>>() {
					});

					comuni = comuniExt.stream().map(c -> c.getStrResComune()).collect(Collectors.toList());
				}
			} else {
				comuni = mockStruttureRsaComponent.getStruttureMock().stream().map(c -> c.getComune()).distinct()
						.collect(Collectors.toList());
			}

			Collections.sort(comuni); // sort alphabetically
			return Response.ok().entity(comuni).build();

		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}
		return error.generateResponseError();
	}

	private void checkValidateListaDecodifica(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws DatabaseException, ResponseErrorException {
		// validate
		List<ErroreDettaglio> listError = validateGeneric.validateGeneric(shibIdentitaCodiceFiscale,
				xRequestId, xForwardedFor, xCodiceServizio, securityContext, httpHeaders, httpRequest);
		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "errore in validate");
	}

	private void checkValidateListaDecodificaStrutture(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest, String comune) throws DatabaseException, ResponseErrorException {
		// validate
		List<ErroreDettaglio> listError = validateGeneric.validateListaDecodificaStrutture(shibIdentitaCodiceFiscale,
				xRequestId, xForwardedFor, xCodiceServizio, securityContext, httpHeaders, httpRequest, comune);
		generateResponseErrorExceptionForValidate(listError, HttpStatus.BAD_REQUEST, "errore in validate");
	}

	public ModelStruttura fromModelStrutturaExtToModelStruttura(ModelStrutturaExt strutturaExt) {
		ModelStruttura struttura = new ModelStruttura();
		struttura.setComune(strutturaExt.getStrResComune());
		struttura.setId(strutturaExt.getIdStruttura());
		if (strutturaExt.getStruttura() != null) {
			struttura.setIdArpe(strutturaExt.getStruttura().getCodStrutturaArpe());
		}
		struttura.setIndirizzo(strutturaExt.getStrResIndirizzo());
		struttura.setNome(strutturaExt.getStrResNome());
		struttura.setPiva(strutturaExt.getEntGestCfPiva());
		struttura.setProvincia(strutturaExt.getStrResProvincia());

		ModelStrutturaSedeLegale sedeLegale = new ModelStrutturaSedeLegale();
		sedeLegale.setComune(strutturaExt.getEntGestComune());
		sedeLegale.setIndirizzo(strutturaExt.getEntGestIndirizzo());
		sedeLegale.setNome(strutturaExt.getEntGestRagioneSociale());
		sedeLegale.setProvincia(strutturaExt.getEntGestProvincia());
		struttura.setSedeLegale(sedeLegale);

		return struttura;
	}
}
