/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.business.be.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonoresbff.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbff.exception.DatabaseException;
import it.csi.buonores.buonoresbff.integration.rest.RestBaseService;
import it.csi.buonores.buonoresbff.util.BuonoResBffProperties;
import it.csi.buonores.buonoresbff.util.Constants;
import it.csi.buonores.buonoresbff.util.UrlBuilder;
import it.csi.buonores.buonoresbff.util.rest.ResponseRest;

@Service
public class ServizioRestService extends BaseService {
	@Autowired
	private BuonoResBffProperties properties;

	@Autowired
	RestBaseService restbase;

	@Autowired
	BatchService batchService;

	private static final String CREA_DOMANDA = "crea_domanda";
	private static final String NOTIFICHE = "notifiche";
	private static final String CONTATTI = "contatti";
	private static final String PREFERENZE = "preferenze";
	private static final String SMISTA_DOCUMENTO = "smistadocumento";
	private static final String STRUTTURE_RSA = "strutturersa";
	private static final String COMUNI_RSA = "strutturersa/comuni";

	public ResponseRest getCreaDomanda(String xRequestId, String xForwardedFor, String xCodiceServizio,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest,
			String numerodomanda) {
		ResponseRest response = new ResponseRest();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		Long risultOk = null;
		Long risultKo = null;
		String batchCod = null;
		String motivoCod = null;
		boolean esisteinvianotifica = false;
		if (httpRequest.getMethod().equalsIgnoreCase("PUT") && httpRequest.getRequestURI().contains("richieste")) {
			batchCod = "PUT_RICHIESTA";
			motivoCod = "AGGIORNA_DOMANDA";
		} else if (httpRequest.getMethod().equalsIgnoreCase("POST")
				&& httpRequest.getRequestURI().contains("richieste")) {
			batchCod = "POST_RICHIESTA";
			motivoCod = "CREA_DOMANDA";
		} else if (httpRequest.getMethod().equalsIgnoreCase("POST")
				&& httpRequest.getRequestURI().contains("cronologia")) {
			batchCod = "POST_CRONOLOGIA";
			// prendi il body e cattura lo stato
			String stato = batchService.selectStatoDomanda(numerodomanda);
			if (stato != null)
				motivoCod = "CAMBIO_STATO_DOMANDA_" + stato;
			else
				motivoCod = "CAMBIO_STATO_DOMANDA";
			if (stato.equalsIgnoreCase(Constants.INVIATA) || stato.equalsIgnoreCase(Constants.RETTIFICATA)
					|| stato.equalsIgnoreCase(Constants.CONTRODEDOTTA) || stato.equalsIgnoreCase(Constants.RINUNCIATA)
					|| stato.equalsIgnoreCase(Constants.PERFEZIONATA_IN_PAGAMENTO)) {
				esisteinvianotifica = true;
			}
		}

		String utente = null;
		if (httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale") != null
				&& httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").get(0);
		} else if (httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale") != null
				&& httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").get(0);
		}

		try {
			// chiamo servizio se ok allora inserisco in t_batch_esecuzione e step se ko in
			// s_batch_esecuzione
			// verifico se ho un errore o esito positivo per scrivere i log
			if (batchCod.equalsIgnoreCase("POST_CRONOLOGIA")) {
				response = restbase.eseguiGet(httpHeaders, buildUrlPath(CREA_DOMANDA).path(numerodomanda).buildUrl(),
						true, null, true);
				if (esisteinvianotifica) {
					// ci sono tre step crea domanda invio stardas invio notifica
					risultOk = batchService.inserisciBatchOK(numerodomanda, batchCod, motivoCod, utente, "STATO_OK");
					risultKo = batchService.inserisciBatchKO(numerodomanda, batchCod, motivoCod, utente, "STATO_KO");
				}
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					if (!esisteinvianotifica) {
						risultOk = batchService.inserisciBatchOK(numerodomanda, batchCod, motivoCod, utente,
								"STATO_OK");
					}
					// esito positivo
					if (risultOk != null && risultOk > 0) {
						// inserisco nella det
						batchService.inserisciBatchStepOK("CREA_DOMANDA", "Domanda " + numerodomanda + " " + motivoCod,
								risultOk, utente, null);
					}
				} else {
					if (!esisteinvianotifica) {
						risultKo = batchService.inserisciBatchKO(numerodomanda, batchCod, motivoCod, utente,
								"STATO_KO");
					}
					if (risultKo != null && risultKo > 0) {
						// inserisco nella det
						batchService.inserisciBatchStepKO("CREA_DOMANDA",
								"Domanda " + numerodomanda + " " + motivoCod + " " + response.getJson(), risultKo,
								utente);
					}
				}
			} else {
				response = restbase.eseguiGet(httpHeaders, buildUrlPath(CREA_DOMANDA).path(numerodomanda).buildUrl(),
						true, null, true);
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// esito positivo
					risultOk = batchService.inserisciBatchOK(numerodomanda, batchCod, motivoCod, utente, "STATO_OK");
					if (risultOk != null && risultOk > 0) {
						// inserisco nella det
						batchService.inserisciBatchStepOK("CREA_DOMANDA", "Domanda " + numerodomanda + " " + motivoCod,
								risultOk, utente, null);
					}
				} else {
					// esito negativo
					risultKo = batchService.inserisciBatchKO(numerodomanda, batchCod, motivoCod, utente, "STATO_KO");
					if (risultKo != null && risultKo > 0) {
						// inserisco nella det
						batchService.inserisciBatchStepKO("CREA_DOMANDA",
								"Domanda " + numerodomanda + " " + motivoCod + " " + response.getJson(), risultKo,
								utente);
					}
				}
			}
			log.info(metodo + " - Fine ");
		} catch (DatabaseException e) {
			handleDatabaseException(metodo, e);
		} catch (Exception e) {
			risultKo = batchService.inserisciBatchKO(numerodomanda, batchCod, motivoCod, utente, "STATO_KO");
			if (risultKo != null && risultKo > 0) {
				// inserisco nella det
				batchService.inserisciBatchStepKO("CREA_DOMANDA",
						"Domanda " + numerodomanda + " " + motivoCod + " " + response.getJson(), risultKo, utente);
			}
			handleException(metodo, e);
		}

		return response;
	}

	public ResponseRest getInviaNotifica(String xRequestId, String xForwardedFor, String xCodiceServizio,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest,
			String numerodomanda, String tipoNotifica) {
		ResponseRest response = new ResponseRest();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		String utente = null;
		int esitopos = 0;
		if (httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale") != null
				&& httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").get(0);
		} else if (httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale") != null
				&& httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").get(0);
		}
		Long numseqbatchKo = batchService.selectEventoKo(numerodomanda, "STATO_KO", "POST_CRONOLOGIA");
		Long numseqbatchOk = batchService.selectEventoOk(numerodomanda, "STATO_OK", "POST_CRONOLOGIA");

		try {
			esitopos = batchService.selectEsitoPositivo(numerodomanda, "INVIO_NOTIFICA_" + tipoNotifica);
			if (esitopos == 0) {
				response = restbase.eseguiGet(httpHeaders,
						buildUrlPath(NOTIFICHE).path(numerodomanda).path(tipoNotifica).buildUrl(), true, null, true);
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// inserisco nella det
					batchService.inserisciBatchStepOK("INVIO_NOTIFICA_" + tipoNotifica,
							"Domanda " + numerodomanda + " CAMBIO_STATO_DOMANDA_" + tipoNotifica, numseqbatchOk,
							utente, response.getJson());
				} else {
					// inserisco nella det
					batchService
							.inserisciBatchStepKO(
									"INVIO_NOTIFICA_" + tipoNotifica, "Domanda " + numerodomanda
											+ " CAMBIO_STATO_DOMANDA_" + tipoNotifica + " " + response.getJson(),
									numseqbatchKo, utente);
				}
			}
			log.info(metodo + " - Fine ");
		} catch (DatabaseException e) {
			handleDatabaseException(metodo, e);
		} catch (Exception e) {
			if (esitopos == 0) {
				batchService.inserisciBatchStepKO("INVIO_NOTIFICA_" + tipoNotifica,
						"Domanda " + numerodomanda + " CAMBIO_STATO_DOMANDA_" + tipoNotifica + " " + response.getJson(),
						numseqbatchKo, utente);
			}

			handleException(metodo, e);
		}

		return response;
	}

	public ResponseRest getContatti(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		ResponseRest response = new ResponseRest();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		try {
			response = restbase.eseguiGet(httpHeaders, buildUrlPath(NOTIFICHE).path(CONTATTI).buildUrl(), true, null,
					true);
			log.info(metodo + " - Fine ");
		} catch (Exception e) {
			logError("getContatti", e.getMessage(), e);
		}
		return response;
	}

	public ResponseRest getStartDas(String xRequestId, String xForwardedFor, String xCodiceServizio,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest,
			String numerodomanda) {
		ResponseRest response = new ResponseRest();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		String utente = null;
		int esitopos = 0;
		if (httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale") != null
				&& httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").get(0);
		} else if (httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale") != null
				&& httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").get(0);
		}
		Long numseqbatchKo = batchService.selectEventoKo(numerodomanda, "STATO_KO", "POST_CRONOLOGIA");
		Long numseqbatchOk = batchService.selectEventoOk(numerodomanda, "STATO_OK", "POST_CRONOLOGIA");

		try {
			esitopos = batchService.selectEsitoPositivo(numerodomanda, "STARDAS_PROTOCOLLO_ARRIVO");
			if (esitopos == 0) {
				response = restbase.eseguiGet(httpHeaders,
						buildUrlPath(SMISTA_DOCUMENTO).path(numerodomanda).buildUrl(), true, null, true);
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// inserisco nella det
					batchService.inserisciBatchStepOK("STARDAS_PROTOCOLLO_ARRIVO",
							"Domanda " + numerodomanda + " CAMBIO_STATO_DOMANDA", numseqbatchOk, utente, null);
				} else {
					// inserisco nella det
					batchService.inserisciBatchStepKO("STARDAS_PROTOCOLLO_ARRIVO",
							"Domanda " + numerodomanda + " CAMBIO_STATO_DOMANDA" + " " + response.getJson(),
							numseqbatchKo, utente);
				}
			}
			log.info(metodo + " - Fine ");
		} catch (DatabaseException e) {
			handleDatabaseException(metodo, e);
		} catch (Exception e) {
			if (esitopos == 0) {
				batchService.inserisciBatchStepKO("STARDAS_PROTOCOLLO_ARRIVO",
						"Domanda " + numerodomanda + " CAMBIO_STATO_DOMANDA" + " " + response.getJson(), numseqbatchKo,
						utente);
			}
			handleException(metodo, e);
		}
		return response;
	}

	// TODO for BATCH ???
	public ResponseRest getStruttureRsa(String xRequestId, String xForwardedFor, String xCodiceServizio,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest,
			String denominazione, String comune, String indirizzo) {
		ResponseRest response = null;
		try {
			UrlBuilder urlBuilder = buildUrl(STRUTTURE_RSA, denominazione, comune, indirizzo);
			String url = urlBuilder.buildUrl();
			response = restbase.eseguiGet(httpHeaders, url, true, null, true);
		} catch (Exception e) {
			logError("getStruttureRsa", e.getMessage(), e);
		}
		return response;
	}

	// TODO for BATCH ???
	public ResponseRest getComuniRsa(String xRequestId, String xForwardedFor, String xCodiceServizio,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ResponseRest response = null;
		try {
			UrlBuilder urlBuilder = buildUrlPath(COMUNI_RSA);
			String url = urlBuilder.buildUrl();
			response = restbase.eseguiGet(httpHeaders, url, true, null, true);
		} catch (Exception e) {
			logError("getComuniRsa", e.getMessage(), e);
		}
		return response;
	}

	public ResponseRest getPreferenze(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		ResponseRest response = new ResponseRest();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		try {
			UrlBuilder urlBuilder = buildUrlPath(NOTIFICHE).path(PREFERENZE);
			response = restbase.eseguiGet(httpHeaders, urlBuilder.buildUrl(), true, null, true);
			log.info(metodo + " - Fine ");
		} catch (Exception e) {
			logError("getPreferenze", e.getMessage(), e);
		}
		return response;
	}

	public void deleteBatch(String numerodomanda) {
		// verifico come chiudere il batch se tutti gli step hanno esito ok o ko
		Long numseqbatchKo = batchService.selectEventoKo(numerodomanda, "STATO_KO", "POST_CRONOLOGIA");
		Long numseqbatchOk = batchService.selectEventoOk(numerodomanda, "STATO_OK", "POST_CRONOLOGIA");

		batchService.deleteEsitoVuotoOk(numseqbatchOk);
		batchService.deleteEsitoVuotoKo(numseqbatchKo);
		batchService.DeleteRecordDaCancellareDuplicati();
	}

	private UrlBuilder buildUrlPath(String path) {
		UrlBuilder urlBuilder = new UrlBuilder(properties.getBuonoressrvurl()).path(path);
		return urlBuilder;
	}

	private UrlBuilder buildUrl(String path, String denominazione, String comune, String indirizzo) {
		UrlBuilder urlBuilder = buildUrlPath(path);
		fillUrlBuilderIfNotNull("denominazione", denominazione, urlBuilder);
		fillUrlBuilderIfNotNull("comune", comune, urlBuilder);
		fillUrlBuilderIfNotNull("indirizzo", indirizzo, urlBuilder);
		return urlBuilder;
	}

	private void fillUrlBuilderIfNotNull(String queryName, String value, UrlBuilder urlBuilder) {
		if (StringUtils.isNotBlank(value)) {
			urlBuilder.query(queryName, value);
		}
	}
}
