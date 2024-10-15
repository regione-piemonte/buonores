/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.business.be.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.buonores.buonoresbo.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbo.dto.Errore;
import it.csi.buonores.buonoresbo.dto.ErroreDettaglio;
import it.csi.buonores.buonoresbo.dto.ModelBuonoInformazioni;
import it.csi.buonores.buonoresbo.dto.ModelContrattoAllegati;
import it.csi.buonores.buonoresbo.dto.ModelDecorrenzaBuono;
import it.csi.buonores.buonoresbo.dto.ModelDichiarazioneSpesa;
import it.csi.buonores.buonoresbo.dto.ModelDocumentoSpesa;
import it.csi.buonores.buonoresbo.dto.ModelFiltroDateIntegrazione;
import it.csi.buonores.buonoresbo.dto.ModelIntegrazioneBuonoDettaglio;
import it.csi.buonores.buonoresbo.dto.ModelRicezioneSegnalazione;
import it.csi.buonores.buonoresbo.dto.ModelStoricoRendicontazioni;
import it.csi.buonores.buonoresbo.dto.ModelVerificaInformazioni;
import it.csi.buonores.buonoresbo.dto.UserInfo;
import it.csi.buonores.buonoresbo.dto.custom.ModelBandiMessage;
import it.csi.buonores.buonoresbo.dto.custom.ModelBuono;
import it.csi.buonores.buonoresbo.exception.DatabaseException;
import it.csi.buonores.buonoresbo.filter.IrideIdAdapterFilter;
import it.csi.buonores.buonoresbo.integration.dao.custom.BuonoDao;
import it.csi.buonores.buonoresbo.integration.dao.custom.BuonoDettaglioDao;
import it.csi.buonores.buonoresbo.integration.dao.custom.RichiesteDao;
import it.csi.buonores.buonoresbo.util.Constants;
import it.csi.buonores.buonoresbo.util.Util;
import it.csi.buonores.buonoresbo.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoresbo.util.rest.ResponseRest;
import it.csi.buonores.buonoresbo.util.validator.impl.ValidateGenericImpl;

@Service
public class BuonoDettaglioService extends BaseService {

	@Autowired
	BuonoDettaglioDao buonoDettaglioDao;

	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	BuonoDao buonoDao;

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	ServizioRestBandiService restbase;

	@Autowired
	ServizioRestService restSrv;

	// GET RENDICONTAZIONI
	public Response getAllegatiBuono(String numeroRichiesta, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		ObjectMapper mapper = new ObjectMapper();

		try {
			ModelBuono buono = buonoDao.selectBuono(numeroRichiesta);
			if (buono != null) {
				List<ModelDichiarazioneSpesa> dichiarazioniSpesa = new ArrayList<ModelDichiarazioneSpesa>();
				ResponseRest response = restSrv.getRendicontazioniRsa(securityContext, httpHeaders, httpRequest,
						numeroRichiesta, userInfo.getCodFisc());
				if (response == null || response.getStatusCode() != HttpStatus.OK.value()) {
					// errore
					String json = (response != null) ? response.getJson() : new Errore().toString();
					Errore errore = mapper.readValue(json, Errore.class);
					generateResponseErrorException(errore.getDetail(), HttpStatus.valueOf(errore.getStatus()),
							errore.getDetail().toString());
				} else {
					String resp = response.getJson();

					dichiarazioniSpesa = mapper.readValue(resp, new TypeReference<List<ModelDichiarazioneSpesa>>() {
					});

					return Response.ok().entity(dichiarazioniSpesa).build();
				}

				return Response.ok().entity(dichiarazioniSpesa).build();
			} else {
				generateResponseErrorException(CodeErrorEnum.ERR05, HttpStatus.NOT_FOUND, "Buono inesistente", "buono");
			}

		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	public Response getContrattiBuono(String numeroRichiesta, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();

		try {
			ModelBuono buono = buonoDao.selectBuono(numeroRichiesta);
			if (buono != null) {
				List<ModelContrattoAllegati> contrattiAllegati = buonoDettaglioDao
						.selectContrattiByBuonoCod(buono.getBuonoCod());

				return Response.status(200).entity(contrattiAllegati).build();
			} else {
				generateResponseErrorException(CodeErrorEnum.ERR05, HttpStatus.NOT_FOUND, "Buono inesistente", "buono");
			}

		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	public Response getDecorrenzaBuono(String numeroRichiesta, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();

		try {
			ModelBuono buono = buonoDao.selectBuono(numeroRichiesta);
			if (buono != null) {
				ModelDecorrenzaBuono decorrenza = buonoDettaglioDao.selectDecorrenzaBuono(buono.getBuonoCod());
				if (decorrenza.getDecorrenzaInizio() == null) {
					int mesi = buonoDettaglioDao.selectMesiBando(buono.getBuonoCod());
					UserInfo userInfo = (UserInfo) httpRequest.getSession()
							.getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
					decorrenza.setDecorrenzaInizio(
							Util.getDataWithoutTime(buonoDettaglioDao.selectDataInPagamento(numeroRichiesta)));

					// mettere data fine come data inizio e mesi finanziamento
					if (mesi != 0)
						decorrenza.setDecorrenzaFine(Util.aggiungiGiorniAData(
								Util.aggiungiAnnoAData(decorrenza.getDecorrenzaInizio(), mesi / 12), -1));
					else {
						decorrenza.setDecorrenzaFine(decorrenza.getDecorrenzaInizio());
					}
					// invio a bandi la segnalazione del cambio data decorrenza
					ModelRicezioneSegnalazione segnalazione = new ModelRicezioneSegnalazione();
					segnalazione.setCodiceNotifica(Constants.DTCONC);
					segnalazione.setData(Util.getDataISO(decorrenza.getDecorrenzaInizio()));
					segnalazione.setNumeroDomanda(numeroRichiesta);
					segnalazione.setDescrizioneNotifica(buonoDao.selectDescrMotivoSegnalazione(Constants.DTCONC));
					ObjectMapper mapper = new ObjectMapper();
					mapper.setSerializationInclusion(Include.NON_EMPTY);
					String jsonSegnalazione = "";
					jsonSegnalazione = mapper.writeValueAsString(segnalazione);
					ResponseRest responserest = restbase.setRicezioneSegnalazioni(jsonSegnalazione, httpHeaders,
							httpRequest, userInfo.getCodFisc());
					if (responserest.getStatusCode() == 200 || responserest.getStatusCode() == 201) {
						ModelBandiMessage message = new ModelBandiMessage();
						message = new ObjectMapper().readValue(responserest.getJson(), ModelBandiMessage.class);
						if (message.getEsitoServizio().equalsIgnoreCase("OK")) {
							// aggiorna data inizio e fine decorrenza
							buonoDettaglioDao.updateDecorrenzaBuono(buono.getBuonoCod(), decorrenza,
									userInfo.getCodFisc());
						}
					}
				}
				return Response.status(200).entity(decorrenza).build();
			} else {
				generateResponseErrorException(CodeErrorEnum.ERR05, HttpStatus.NOT_FOUND, "Buono inesistente", "buono");
			}

		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	public Response updateDecorrenzaBuono(String numeroRichiesta, ModelDecorrenzaBuono decorrenza,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		List<ErroreDettaglio> listerrorservice = new ArrayList<ErroreDettaglio>();

		try {
			ModelBuono buono = buonoDao.selectBuono(numeroRichiesta);
			if (buono != null) {
				if (buono.getBuonoCod() != null && decorrenza.getDecorrenzaInizio() != null) {
					// Confronto tra le due date -> non si va indietro da eliminare
					if (buono.getDecorrenzaInizio().after(decorrenza.getDecorrenzaInizio())) {
						generateResponseErrorException(CodeErrorEnum.ERR02, HttpStatus.NOT_FOUND,
								"data inizio decorrenza buono errata", " Data inizio decorrenza buono errata");
					}

					int mesi = buonoDettaglioDao.selectMesiBando(buono.getBuonoCod());
					// mettere data fine come data inizio e mesi finanziamento
					if (mesi != 0) {
						decorrenza.setDecorrenzaFine(Util.aggiungiGiorniAData(
								Util.aggiungiAnnoAData(decorrenza.getDecorrenzaInizio(), mesi / 12), -1));
					} else {
						decorrenza.setDecorrenzaFine(decorrenza.getDecorrenzaInizio());
					}
					// invio a bandi la segnalazione del cambio data decorrenza
					ModelRicezioneSegnalazione segnalazione = new ModelRicezioneSegnalazione();
					segnalazione.setCodiceNotifica(Constants.DTCONC);
					segnalazione.setData(Util.getDataISO(decorrenza.getDecorrenzaInizio()));
					segnalazione.setNumeroDomanda(numeroRichiesta);
					segnalazione.setDescrizioneNotifica(buonoDao.selectDescrMotivoSegnalazione(Constants.DTCONC));
					ObjectMapper mapper = new ObjectMapper();
					mapper.setSerializationInclusion(Include.NON_EMPTY);
					String jsonSegnalazione = "";
					jsonSegnalazione = mapper.writeValueAsString(segnalazione);
					ResponseRest responserest = restbase.setRicezioneSegnalazioni(jsonSegnalazione, httpHeaders,
							httpRequest, userInfo.getCodFisc());
					if (responserest.getStatusCode() == 400) {
						error = generateError(listerrorservice, CodeErrorEnum.ERR03, HttpStatus.BAD_REQUEST, metodo,
								"Errore nel salvataggio della data di decorrenza del buono " + responserest.toString());
					} else if (responserest.getStatusCode() != 200 && responserest.getStatusCode() != 201) {
						error = generateError(listerrorservice, CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR,
								metodo, "Errore generico set ricezione segnalazioni data decorrenza buono "
										+ responserest.toString());
					} else {
						ModelBandiMessage message = new ModelBandiMessage();
						message = new ObjectMapper().readValue(responserest.getJson(), ModelBandiMessage.class);
						if (message.getEsitoServizio().equalsIgnoreCase("OK")) {
							buonoDettaglioDao.updateDecorrenzaBuono(buono.getBuonoCod(), decorrenza,
									userInfo.getCodFisc());
							return Response.status(200).entity(decorrenza).build();
						} else {
							return Response.status(400).entity(null).build();
						}
					}
				} else {
					generateResponseErrorException(CodeErrorEnum.ERR05, HttpStatus.NOT_FOUND,
							"Buono inesistente e senza data decorrenza inizio", "buono");
				}

			} else {
				generateResponseErrorException(CodeErrorEnum.ERR05, HttpStatus.NOT_FOUND, "Buono inesistente", "buono");
			}

		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	public Response getIntegrazione(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest,
			ModelFiltroDateIntegrazione filtroDateIntegrazione) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);

		try {
			ModelBuono buono = buonoDao.selectBuono(numeroRichiesta);
			if (buono == null) {
				generateResponseErrorException(CodeErrorEnum.ERR05, HttpStatus.NOT_FOUND,
						"buono inesistente per domanda " + numeroRichiesta, "buono");
			}
			List<ModelIntegrazioneBuonoDettaglio> docIntegrazioneOut = new ArrayList<ModelIntegrazioneBuonoDettaglio>();

			ResponseRest response = restSrv.getIntegrazioniRsa(securityContext, httpHeaders, httpRequest,
					numeroRichiesta, userInfo.getCodFisc());
			if (response == null || response.getStatusCode() != HttpStatus.OK.value()) {
				// errore
				String json = (response != null) ? response.getJson() : new Errore().toString();
				Errore errore = mapper.readValue(json, Errore.class);
				generateResponseErrorException(errore.getDetail(), HttpStatus.valueOf(errore.getStatus()),
						errore.getDetail().toString());
			} else {
				String resp = response.getJson();

				docIntegrazioneOut = mapper.readValue(resp, new TypeReference<List<ModelIntegrazioneBuonoDettaglio>>() {
				});

				return Response.status(200).entity(filtraIntegrazioni(docIntegrazioneOut, filtroDateIntegrazione))
						.build();
			}

		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	private List<ModelIntegrazioneBuonoDettaglio> filtraIntegrazioni(
			List<ModelIntegrazioneBuonoDettaglio> integrazioniLista,
			ModelFiltroDateIntegrazione filtroDateIntegrazione) {
		List<ModelIntegrazioneBuonoDettaglio> result = new ArrayList<ModelIntegrazioneBuonoDettaglio>();
		if (integrazioniLista != null && !integrazioniLista.isEmpty()) {
			Date meseFine = Util.getDataWithoutTime(filtroDateIntegrazione.getMeseFine());
			Date meseInizio = Util.getDataWithoutTime(filtroDateIntegrazione.getMeseInizio());
			Date dataIntegrazioneTmp = new Date();
			for (ModelIntegrazioneBuonoDettaglio integrazione : integrazioniLista) {
				dataIntegrazioneTmp = Util.getDataWrapperWithoutTime(integrazione.getDicSpesaIntegrazioneData());
				if (dataIntegrazioneTmp != null &&
						(meseInizio.getTime() <= dataIntegrazioneTmp.getTime()) &&
						(dataIntegrazioneTmp.getTime() <= meseFine.getTime())) {
					result.add(integrazione);
				}
			}
		}
		return result;
	}

	public Response getBuonoInformazioni(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			ModelBuono buono = buonoDao.selectBuono(numeroRichiesta);
			if (buono == null) {
				generateResponseErrorException(CodeErrorEnum.ERR05, HttpStatus.NOT_FOUND,
						"buono inesistente per domanda " + numeroRichiesta, "buono");
			}

			ModelBuonoInformazioni buonoInfo = new ModelBuonoInformazioni();
			buonoInfo = buonoDao.selectBuonoInformazioni(numeroRichiesta);
			return Response.status(200).entity(buonoInfo).build();
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	public Response getVerificaInformazioni(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = new Errore();

		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			ModelBuono buono = buonoDao.selectBuono(numeroRichiesta);
			if (buono == null) {
				generateResponseErrorException(CodeErrorEnum.ERR05, HttpStatus.NOT_FOUND,
						"Buono inesistente per domanda " + numeroRichiesta, "buono");
			}
			ModelVerificaInformazioni verificaInfo = new ModelVerificaInformazioni();
			verificaInfo = buonoDao.selectVerificaInformazioni(numeroRichiesta);
			return Response.status(200).entity(verificaInfo).build();
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	public Response getStoricoRendicontazioni(String numeroBuono, String numeroRichiesta, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String shibIdentitaCodiceFiscale,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);

		try {
			ModelBuono buono = buonoDao.selectBuono(numeroRichiesta);
			if (buono == null) {
				generateResponseErrorException(CodeErrorEnum.ERR05, HttpStatus.NOT_FOUND,
						"Buono inesistente per domanda " + numeroRichiesta, "buono");
			}
			List<ModelStoricoRendicontazioni> listaStoricoRendicontazioni = new ArrayList<ModelStoricoRendicontazioni>(
					20);
			listaStoricoRendicontazioni = getStoricoRendicontazioni(securityContext, httpHeaders, httpRequest,
					numeroRichiesta, userInfo.getCodFisc());

			return Response.status(200).entity(listaStoricoRendicontazioni).build();
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	private List<ModelStoricoRendicontazioni> getStoricoRendicontazioni(SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest, String numeroRichiesta, String codiceFiscaleUtente)
			throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		List<ModelStoricoRendicontazioni> storicoDichiarazioniList = new ArrayList<ModelStoricoRendicontazioni>();
		List<ModelDichiarazioneSpesa> dichiarazioniSpesa = new ArrayList<ModelDichiarazioneSpesa>();
		ResponseRest response = restSrv.getRendicontazioniRsa(securityContext, httpHeaders, httpRequest,
				numeroRichiesta, codiceFiscaleUtente);
		if (response == null || response.getStatusCode() != HttpStatus.OK.value()) {
			String json = (response != null) ? response.getJson() : new Errore().toString();
			Errore errore = mapper.readValue(json, Errore.class);
			generateResponseErrorException(errore.getDetail(), HttpStatus.valueOf(errore.getStatus()),
					errore.getDetail().toString());
		} else {
			String resp = response.getJson();
			dichiarazioniSpesa = mapper.readValue(resp, new TypeReference<List<ModelDichiarazioneSpesa>>() {
			});
			storicoDichiarazioniList = convertFromModelDichiarazioneSpesaToModelStoricoRendicontazioni(
					dichiarazioniSpesa);
		}
		return storicoDichiarazioniList;
	}

	private List<ModelStoricoRendicontazioni> convertFromModelDichiarazioneSpesaToModelStoricoRendicontazioni(
			List<ModelDichiarazioneSpesa> dichiarazioniSpesaList) {
		List<ModelStoricoRendicontazioni> result = new ArrayList<ModelStoricoRendicontazioni>();
		if (dichiarazioniSpesaList != null && !dichiarazioniSpesaList.isEmpty()) {
			ModelStoricoRendicontazioni storicoRendTmp = new ModelStoricoRendicontazioni();
			for (ModelDichiarazioneSpesa dichiarazioneSpesa : dichiarazioniSpesaList) {
				if (dichiarazioneSpesa.getDocumentiSpesa() != null
						&& !dichiarazioneSpesa.getDocumentiSpesa().isEmpty()) {
					for (ModelDocumentoSpesa docSpesa : dichiarazioneSpesa.getDocumentiSpesa()) {
						storicoRendTmp = new ModelStoricoRendicontazioni();
						storicoRendTmp.setEsito(dichiarazioneSpesa.getStatoDesc());
						storicoRendTmp.setDataDichiarazione(Util.getDataISO(dichiarazioneSpesa.getDataInvio()));
						storicoRendTmp.setMonthData(Util.getDataISO(docSpesa.getPeriodoInizio()));
						storicoRendTmp.setMese(Util.getMonthDescription(docSpesa.getPeriodoInizio()));
						storicoRendTmp.setAnno(Util.getYear(docSpesa.getPeriodoInizio()));
						storicoRendTmp.setNote(docSpesa.getNoteIstruttoria());
						storicoRendTmp.setDataIstruttoria(docSpesa.getDataIstruttoria());
						result.add(storicoRendTmp);
					}
				}

			}

		}
		return result;
	}

}