/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.business.be.service;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonoresbo.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbo.integration.rest.RestBaseService;
import it.csi.buonores.buonoresbo.util.BuonoResBoProperties;
import it.csi.buonores.buonoresbo.util.Constants;
import it.csi.buonores.buonoresbo.util.UrlBuilder;
import it.csi.buonores.buonoresbo.util.Util;
import it.csi.buonores.buonoresbo.util.rest.ResponseRest;

@Service
public class ServizioRestService extends BaseService {

	private static final String CREA_DOMANDA = "/crea_domanda/";
	private static final String NOTIFICHE = "/notifiche/";
	private static final String SMISTA_DOCUMENTO_PARTENZA = "/smistadocumentopartenza/";
	private static final String CREA_LETTERA = "/crea_lettera/";
	private static final String RENDICONTAZIONI_RSA = "/strutturersa/rendicontazioni";
	private static final String ALLEGATO_RENDICONTAZIONI_RSA = "/strutturersa/rendicontazioni/allegati";
	private static final String INTEGRAZIONI_RSA = "/strutturersa/integrazioni";
	@Autowired
	private BuonoResBoProperties properties;

	@Autowired
	RestBaseService restbase;

	@Autowired
	BatchService batchService;

	// solo domanda per richiedi_rettifica diniego inpagamento
	// preavvisopernonammissibilita

	public ResponseRest getCreaDomanda(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest, String numerodomanda, String cf) {
		ResponseRest response = new ResponseRest();
		Long risultOk = null;
		Long risultKo = null;
		String batchCod = null;
		String motivoCod = null;
		batchCod = "CAMBIO_STATO_OPERATORE";
		// prendi il body e cattura lo stato
		String stato = batchService.selectStatoDomanda(numerodomanda);
		if (stato != null)
			motivoCod = "CAMBIO_STATO_DOMANDA_" + stato;
		else
			motivoCod = "CAMBIO_STATO_DOMANDA";
		String utente = cf;
		if (httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale") != null
				&& httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").get(0);
		} else if (httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale") != null
				&& httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").get(0);
		}

		try {
			response = restbase.eseguiGet(httpRequest, httpHeaders,
					properties.getBuonoressrvurl() + CREA_DOMANDA + numerodomanda, true, null, true, cf);
			// verifico se ho un errore o esito positivo per scrivere i log
			if (httpRequest.getRequestURI().contains("richiedi_rettifica")
					|| httpRequest.getRequestURI().contains("diniego")
					|| httpRequest.getRequestURI().contains("preavvisopernonammissibilita")) {
				// ci sono altri step oltre a crea domanda
				risultOk = batchService.inserisciBatchOK(numerodomanda, batchCod, motivoCod, utente, "STATO_OK");
				risultKo = batchService.inserisciBatchKO(numerodomanda, batchCod, motivoCod, utente, "STATO_KO");
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// esito positivo
					if (risultOk > 0) {
						// inserisco nella det
						batchService.inserisciBatchStepOK("CREA_DOMANDA", "Domanda " + numerodomanda + " " + motivoCod,
								risultOk, utente, null);
					}
				} else {
					if (risultKo != null && risultKo > 0) {
						// inserisco nella det
						batchService.inserisciBatchStepKO("CREA_DOMANDA",
								"Domanda " + numerodomanda + " " + motivoCod + " " + response.getJson(), risultKo,
								utente);
					}
				}
			} else {
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// esito positivo
					risultOk = batchService.inserisciBatchOK(numerodomanda, batchCod, motivoCod, utente, "STATO_OK");
					if (risultOk > 0) {
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
		} catch (Exception e) {
			risultKo = batchService.inserisciBatchKO(numerodomanda, batchCod, motivoCod, utente, "STATO_KO");
			if (risultKo != null && risultKo > 0) {
				// inserisco nella det
				batchService.inserisciBatchStepKO("CREA_DOMANDA",
						"Domanda " + numerodomanda + " " + motivoCod + " " + response.getJson(), risultKo, utente);
			}

			handleException("getCreaDomanda", e);
		}
		return response;
	}

	public ResponseRest getInviaNotifica(HttpHeaders httpHeaders, HttpServletRequest httpRequest, String numerodomanda,
			String tipoNotifica, String cf) {
		ResponseRest response = new ResponseRest();
		String utente = cf;
		int esitopos = 0;
		long numseqbatchKo = 0;
		long numseqbatchOk = 0;
		String batchCod = "CAMBIO_STATO_OPERATORE";
		String motivoCod = "ISEE";

		String batchEsecDetStep = "INVIO_NOTIFICA_" + tipoNotifica;
		if (tipoNotifica.contains("RESPINGI")) {
			batchEsecDetStep += "_" + Util.getDateAndTime();
		}

		if (httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale") != null
				&& httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").get(0);
		} else if (httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale") != null
				&& httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").get(0);
		}

		try {
			esitopos = batchService.selectEsitoPositivo(numerodomanda, batchEsecDetStep);
			if (esitopos == 0) {
				numseqbatchKo = batchService.selectEventoKo(numerodomanda, "STATO_KO", "CAMBIO_STATO_OPERATORE");
				numseqbatchOk = batchService.selectEventoOk(numerodomanda, "STATO_OK", "CAMBIO_STATO_OPERATORE");
				if (numseqbatchOk == 0 && tipoNotifica.contains("ISEE"))
					numseqbatchOk = batchService.inserisciBatchOK(numerodomanda, batchCod, motivoCod, utente,
							"STATO_OK");
				if (numseqbatchKo == 0 & tipoNotifica.contains("ISEE"))
					numseqbatchKo = batchService.inserisciBatchKO(numerodomanda, batchCod, motivoCod, utente,
							"STATO_KO");

				response = restbase.eseguiGet(httpRequest, httpHeaders,
						properties.getBuonoressrvurl() + NOTIFICHE + numerodomanda + File.separator + tipoNotifica,
						true, null, true, cf);
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// inserisco nella det
					batchService.inserisciBatchStepOK(batchEsecDetStep,
							"Domanda " + numerodomanda + " CAMBIO_STATO_DOMANDA_" + tipoNotifica, numseqbatchOk,
							utente, response.getJson());
				} else {
					// inserisco nella det
					batchService
							.inserisciBatchStepKO(
									batchEsecDetStep, "Domanda " + numerodomanda
											+ " CAMBIO_STATO_DOMANDA_" + tipoNotifica + " " + response.getJson(),
									numseqbatchKo, utente);
				}
				// verifico come chiudere il batch se tutti gli step hanno esito ok o ko
				batchService.deleteEsitoVuotoOk(numseqbatchOk);
				batchService.deleteEsitoVuotoKo(numseqbatchKo);
				batchService.DeleteRecordDaCancellareDuplicati();
			}

		} catch (Exception e) {
			if (esitopos == 0) {
				batchService.inserisciBatchStepKO("INVIO_NOTIFICA_" + tipoNotifica,
						"Domanda " + numerodomanda + " CAMBIO_STATO_DOMANDA_" + tipoNotifica + " " + response.getJson(),
						numseqbatchKo, utente);
				batchService.deleteEsitoVuotoOk(numseqbatchOk);
				batchService.deleteEsitoVuotoKo(numseqbatchKo);
				batchService.DeleteRecordDaCancellareDuplicati();
			}
			handleException("getInviaNotifica", e);
		}
		return response;
	}

	public ResponseRest getStarDasPartenza(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest, String numerodomanda, String cf, String tipoLettera) {
		ResponseRest response = new ResponseRest();
		int esitopos = 0;
		long numseqbatchKo = 0;
		long numseqbatchOk = 0;
		String utente = cf;
		if (httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale") != null
				&& httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").get(0);
		} else if (httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale") != null
				&& httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").get(0);
		}

		try {
			esitopos = batchService.selectEsitoPositivo(numerodomanda, "STARDAS_PARTENZA_" + tipoLettera);
			if (esitopos == 0) {
				numseqbatchKo = batchService.selectEventoKo(numerodomanda, "STATO_KO", "CAMBIO_STATO_OPERATORE");
				numseqbatchOk = batchService.selectEventoOk(numerodomanda, "STATO_OK", "CAMBIO_STATO_OPERATORE");
				response = restbase.eseguiGet(httpRequest, httpHeaders, properties.getBuonoressrvurl()
						+ SMISTA_DOCUMENTO_PARTENZA + numerodomanda + File.separator + tipoLettera, true, null, true,
						cf);
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// inserisco nella det
					batchService.inserisciBatchStepOK("STARDAS_PARTENZA_" + tipoLettera,
							"Domanda " + numerodomanda + " CAMBIO_STATO_DOMANDA_" + tipoLettera, numseqbatchOk, utente,
							null);
				} else {
					// inserisco nella det
					batchService.inserisciBatchStepKO("STARDAS_PARTENZA_" + tipoLettera, "Domanda " + numerodomanda
							+ " CAMBIO_STATO_DOMANDA_" + tipoLettera + " " + response.getJson(), numseqbatchKo, utente);
				}
			}
		} catch (Exception e) {
			if (esitopos == 0) {
				batchService.inserisciBatchStepKO("STARDAS_PARTENZA_" + tipoLettera,
						"Domanda " + numerodomanda + " CAMBIO_STATO_DOMANDA_" + tipoLettera + " " + response.getJson(),
						numseqbatchKo, utente);
			}
			handleException("getStarDasPartenza", e);
		}

		return response;
	}

	public ResponseRest getCreaLettera(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest, String numerodomanda, String cf, String tipoLettera, String stato) {
		ResponseRest response = new ResponseRest();
		int esitopos = 0;
		long numseqbatchKo = 0;
		long numseqbatchOk = 0;
		String utente = cf;
		if (httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale") != null
				&& httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").get(0);
		} else if (httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale") != null
				&& httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").get(0);
		}

		try {
			esitopos = batchService.selectEsitoPositivo(numerodomanda, "CREA_LETTERA_" + tipoLettera);
			if (esitopos == 0) {
				numseqbatchKo = batchService.selectEventoKo(numerodomanda, "STATO_KO", "CAMBIO_STATO_OPERATORE");
				numseqbatchOk = batchService.selectEventoOk(numerodomanda, "STATO_OK", "CAMBIO_STATO_OPERATORE");
				response = restbase.eseguiGet(httpRequest, httpHeaders,
						properties.getBuonoressrvurl() + CREA_LETTERA + numerodomanda + File.separator + tipoLettera,
						true, null, true, cf);
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// inserisco nella det
					batchService.inserisciBatchStepOK("CREA_LETTERA_" + tipoLettera,
							"CAMBIO_STATO_DOMANDA_" + tipoLettera, numseqbatchOk, utente, null);
				} else {
					// inserisco nella det
					batchService.inserisciBatchStepKO("CREA_LETTERA_" + tipoLettera, "Domanda " + numerodomanda
							+ " CAMBIO_STATO_DOMANDA_" + tipoLettera + " " + response.getJson(), numseqbatchKo, utente);
				}

				if (!stato.equalsIgnoreCase(Constants.PERFEZIONATA_IN_PAGAMENTO)) {
					// verifico come chiudere il batch se tutti gli step hanno esito ok o ko
					batchService.deleteEsitoVuotoOk(numseqbatchOk);
					batchService.deleteEsitoVuotoKo(numseqbatchKo);
					batchService.DeleteRecordDaCancellareDuplicati();
				}
			}
		} catch (Exception e) {
			if (esitopos == 0) {
				batchService.inserisciBatchStepKO("CREA_LETTERA_" + tipoLettera,
						"Domanda " + numerodomanda + " CAMBIO_STATO_DOMANDA_" + tipoLettera + " " + response.getJson(),
						numseqbatchKo, utente);
				// verifico come chiudere il batch se tutti gli step hanno esito ok o ko
				if (!stato.equalsIgnoreCase(Constants.PERFEZIONATA_IN_PAGAMENTO)) {
					// verifico come chiudere il batch se tutti gli step hanno esito ok o ko
					batchService.deleteEsitoVuotoOk(numseqbatchOk);
					batchService.deleteEsitoVuotoKo(numseqbatchKo);
					batchService.DeleteRecordDaCancellareDuplicati();
				}
			}
			handleException("getCreaLettera", e);
		}
		return response;
	}

	public ResponseRest getRendicontazioniRsa(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest,
			String numeroDomanda, String cf) {
		ResponseRest response = null;
		try {
			UrlBuilder urlBuilder = buildUrlPath(RENDICONTAZIONI_RSA).path(numeroDomanda);
			String url = urlBuilder.buildUrl();
			response = restbase.eseguiGet(httpRequest, httpHeaders, url, true, null, true, cf);
		} catch (Exception e) {
			logError("getRendicontazioniRsa", e.getMessage(), e);
		}
		return response;
	}

	public ResponseRest getIntegrazioniRsa(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest,
			String numeroDomanda, String cf) {
		ResponseRest response = null;
		try {
			UrlBuilder urlBuilder = buildUrlPath(INTEGRAZIONI_RSA).path(numeroDomanda);
			String url = urlBuilder.buildUrl();
			response = restbase.eseguiGet(httpRequest, httpHeaders, url, true, null, true, cf);
		} catch (Exception e) {
			logError("getIntegrazioniRsa", e.getMessage(), e);
		}
		return response;
	}

	public ResponseRest getAllegatoRendicontazioniRsa(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest,
			Integer allegatoId, String cf) {
		ResponseRest response = null;
		try {
			UrlBuilder urlBuilder = buildUrlPath(ALLEGATO_RENDICONTAZIONI_RSA).path(allegatoId.toString());
			String url = urlBuilder.buildUrl();
			response = restbase.eseguiGet(httpRequest, httpHeaders, url, true, null, true, cf);
		} catch (Exception e) {
			logError("getAllegatoRendicontazioniRsa", e.getMessage(), e);
		}
		return response;
	}

	private UrlBuilder buildUrlPath(String path) {
		UrlBuilder urlBuilder = new UrlBuilder(properties.getBuonoressrvurl()).path(path);
		return urlBuilder;
	}
}
