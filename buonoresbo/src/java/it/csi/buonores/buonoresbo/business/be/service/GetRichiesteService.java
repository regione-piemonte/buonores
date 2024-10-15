/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.business.be.service;

import java.math.BigDecimal;
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
import it.csi.buonores.buonoresbo.dto.CambioStatoPopUp;
import it.csi.buonores.buonoresbo.dto.Contact;
import it.csi.buonores.buonoresbo.dto.Errore;
import it.csi.buonores.buonoresbo.dto.ErroreDettaglio;
import it.csi.buonores.buonoresbo.dto.ModelAllegato;
import it.csi.buonores.buonoresbo.dto.ModelDatiDaModificare;
import it.csi.buonores.buonoresbo.dto.ModelIsee;
import it.csi.buonores.buonoresbo.dto.ModelMessaggio;
import it.csi.buonores.buonoresbo.dto.ModelPresaInCarico;
import it.csi.buonores.buonoresbo.dto.ModelRichiesta;
import it.csi.buonores.buonoresbo.dto.ModelUpdateCronologia;
import it.csi.buonores.buonoresbo.dto.ModelVerificheDomanda;
import it.csi.buonores.buonoresbo.dto.ModelVisualizzaCronologia;
import it.csi.buonores.buonoresbo.dto.UserInfo;
import it.csi.buonores.buonoresbo.dto.custom.ModelGetAllegatoExt;
import it.csi.buonores.buonoresbo.exception.DatabaseException;
import it.csi.buonores.buonoresbo.exception.ResponseErrorException;
import it.csi.buonores.buonoresbo.filter.IrideIdAdapterFilter;
import it.csi.buonores.buonoresbo.integration.dao.custom.AllegatoDao;
import it.csi.buonores.buonoresbo.integration.dao.custom.GraduatoriaDao;
import it.csi.buonores.buonoresbo.integration.dao.custom.RichiesteDao;
import it.csi.buonores.buonoresbo.util.Constants;
import it.csi.buonores.buonoresbo.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoresbo.util.validator.impl.ValidateGenericImpl;

@Service
public class GetRichiesteService extends BaseService {

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	AllegatoDao allegatoDao;

	@Autowired
	ServizioRestService servizioRestService;

	@Autowired
	NotificatoreService contattiService;

	@Autowired
	GraduatoriaDao graduatoriaDao;

	public Response getNumeroRichiesta(String numeroRichiesta, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		Errore error = new Errore();
		ModelRichiesta richiesta = new ModelRichiesta();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		long tempoPartenza = System.currentTimeMillis();
		log.info("richiesteget - BEGIN time: ");

		try {

			long tempoValidate = System.currentTimeMillis();

			log.info("richiesteget - tempoValidete: " + System.currentTimeMillis() + " millis: "
					+ (tempoPartenza - tempoValidate));

			richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			if (richiesta != null) {
				List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();
				allegati = richiesteDao.selectAllegatiFromNumeroRichiesta(richiesta.getDomandaDetId());

				long tempoAllegati = System.currentTimeMillis();
				log.info("richiesteget - tempoAllegati: " + System.currentTimeMillis() + " millis: "
						+ (tempoValidate - tempoAllegati));

				if (!allegati.isEmpty()) {
					richiesta.setAllegati(allegati);
				}
				// prendo i contatti del richiedente
				Contact contatto = new Contact();
				contatto = contattiService.sendContact(richiesta.getRichiedente().getCf(), userInfo.getCodFisc());
				if (contatto.getSms() == null) {
					contatto.setSms(contatto.getPhone());
				}
				richiesta.setContatto(contatto);
			} else {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND, "domanda inesistente",
						numeroRichiesta);
			}

			return Response.status(200).entity(richiesta).build();

		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	public Response presaInCaricoRichiesta(List<ModelPresaInCarico> richieste, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = new Errore();
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			// validate
			for (ModelPresaInCarico richiesta : richieste) {
				ModelRichiesta rich = richiesteDao.selectNumeroRichiesta(richiesta.getNumero());
				if (richiesta != null && (rich.getStato().equalsIgnoreCase(Constants.RETTIFICATA)
						|| rich.getStato().equalsIgnoreCase(Constants.CONTRODEDOTTA)
						|| rich.getStato().equalsIgnoreCase(Constants.INVIATA))) {

					List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();
					allegati = richiesteDao.selectAllegatiFromNumeroRichiesta(rich.getDomandaDetId());

					if (!allegati.isEmpty()) {
						rich.setAllegati(allegati);
					}
					cambioStatoDomanda(rich, Constants.PRESA_IN_CARICO, userInfo.getCodFisc(), rich.getNumero(), null,
							null, securityContext, httpHeaders, httpRequest);
				}

			}
			return Response.status(200).entity(true).build();
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	public ModelUpdateCronologia cambioStatoDomanda(ModelRichiesta richiesta, String stato, String codFiscale,
			String numeroRichiesta, String notacittadino, String notainterna, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws DatabaseException {

		ModelUpdateCronologia update = richiesteDao.insertTDettaglioDomandaCambioStato(richiesta, stato, codFiscale,
				notacittadino, notainterna);

		richiesteDao.updateDataFineValDettaglioDomanda(richiesta.getDomandaDetId());
		long idDettaglio = update.getIdDettaglio();
		String detCod = update.getDetCod();

		if (!richiesta.getAllegati().isEmpty()) {
			List<ModelGetAllegatoExt> allegati = allegatoDao.selectAllegati(richiesta.getDomandaDetId());
			if (!allegati.isEmpty()) {
				for (ModelGetAllegatoExt a : allegati) {
					if (!a.getAllegatoTipoCod().equalsIgnoreCase(Constants.DOMANDA)) {
						allegatoDao.insertAllegatoConAllegatoTipoId(a.getFileName(), a.getFileType(), a.getFilePath(),
								richiesta.getSportelloId(), BigDecimal.valueOf(idDettaglio), detCod, a.getAllegatoId(),
								codFiscale, codFiscale);
					}
				}
			}
		}

		if (!stato.equalsIgnoreCase(Constants.IN_PAGAMENTO)) {
			servizioRestService.getCreaDomanda(securityContext, httpHeaders, httpRequest, richiesta.getNumero(),
					richiesta.getRichiedente().getCf());
		}
		return update;
	}

	public Response getListaAllegatoRichiesta(String numeroRichiesta, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = new Errore();
		List<ModelDatiDaModificare> listaAllegati = new ArrayList<ModelDatiDaModificare>();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		long tempoPartenza = System.currentTimeMillis();
		log.info("getListaAllegatoRichiesta - BEGIN time: ");

		try {
			long tempoValidate = System.currentTimeMillis();

			log.info("getListaAllegatoRichiesta - tempoValidete: " + System.currentTimeMillis() + " millis: "
					+ (tempoPartenza - tempoValidate));

			listaAllegati = richiesteDao.selectListaAllegatoRettifica(numeroRichiesta);

			return Response.status(200).entity(listaAllegati).build();

		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	public Response getListaCampoRichiesta(String numeroRichiesta, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = new Errore();
		List<ModelDatiDaModificare> listaCampi = new ArrayList<ModelDatiDaModificare>();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		long tempoPartenza = System.currentTimeMillis();
		log.info("getListaCampoRichiesta - BEGIN time: ");

		try {

			long tempoValidate = System.currentTimeMillis();

			log.info("getListaCampoRichiesta - tempoValidete: " + System.currentTimeMillis() + " millis: "
					+ (tempoPartenza - tempoValidate));

			listaCampi = richiesteDao.selectListaCampiRettifica(numeroRichiesta);

			return Response.status(200).entity(listaCampi).build();

		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	public Response datiDaModificarePost(CambioStatoPopUp datiDaModificare, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = new Errore();
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglio> listerrorservice = new ArrayList<ErroreDettaglio>();
		try {
			if (datiDaModificare.getDatidamodificare().size() > 0) {
				ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(datiDaModificare.getNumerodomanda());
				if (richiesta != null) {
					listerrorservice = validateGeneric.checkCambioStatoCoerente(listerrorservice,
							Constants.DA_RETTIFICARE, richiesta.getStato());
					generateResponseErrorExceptionForValidate(listerrorservice, HttpStatus.BAD_REQUEST,
							"stato non coerente");

					List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();
					allegati = richiesteDao.selectAllegatiFromNumeroRichiesta(richiesta.getDomandaDetId());

					if (!allegati.isEmpty())
						richiesta.setAllegati(allegati);

					cambioStatoDomanda(richiesta, Constants.DA_RETTIFICARE, userInfo.getCodFisc(),
							richiesta.getNumero(), datiDaModificare.getNotaCittadino(),
							datiDaModificare.getNotaInterna(), securityContext, httpHeaders, httpRequest);

					ModelRichiesta richiestacambiata = richiesteDao
							.selectNumeroRichiesta(datiDaModificare.getNumerodomanda());

					richiesteDao.deleteTDatoDaModificare(richiestacambiata.getDomandaDetId());
					for (ModelDatiDaModificare campo : datiDaModificare.getDatidamodificare()) {
						if (campo.getNometabella() != null) {
							// sono campi
							richiesteDao.insertTDatoDaModificare(null, richiestacambiata.getSportelloId(),
									richiestacambiata.getDomandaDetId(), userInfo.getCodFisc(), campo.getId());
						} else {
							// prendere id allegato relativo a stato
							BigDecimal newAllegatiId = richiesteDao
									.selectAllegatoId(richiestacambiata.getDomandaDetId(), campo.getId());
							richiesteDao.insertTDatoDaModificare(newAllegatiId, richiestacambiata.getSportelloId(),
									richiestacambiata.getDomandaDetId(), userInfo.getCodFisc(), null);
						}
					}
				} else {
					generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND, "domanda inesistente",
							datiDaModificare.getNumerodomanda());
				}
				servizioRestService.getInviaNotifica(httpHeaders, httpRequest, datiDaModificare.getNumerodomanda(),
						"BO_RETTIFICA", richiesta.getRichiedente().getCf());
				return Response.status(200).entity(richiesta).build();
			} else {
				generateResponseErrorException(CodeErrorEnum.ERR14, HttpStatus.NOT_FOUND, "domanda inesistente",
						"lista campi da modificare");
			}
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	public Response cronologiaRichiesta(String numeroRichiesta, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		long tempoPartenza = System.currentTimeMillis();
		log.info("richiesteget - BEGIN time: ");
		try {
			long tempoValidate = System.currentTimeMillis();

			log.info("cronologia - tempoValidete: " + System.currentTimeMillis() + " millis: "
					+ (tempoPartenza - tempoValidate));
			List<ModelVisualizzaCronologia> selectCronologia = richiesteDao.selectCronologia(numeroRichiesta);
			return Response.status(200).entity(selectCronologia).build();
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	public Response verificheDomanda(String numeroRichiesta, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		long tempoPartenza = System.currentTimeMillis();
		log.info("verifiche - BEGIN time: ");
		try {
			long tempoValidate = System.currentTimeMillis();

			log.info("verifiche - tempoValidete: " + System.currentTimeMillis() + " millis: "
					+ (tempoPartenza - tempoValidate));
			List<ModelVerificheDomanda> verifica = new ArrayList<ModelVerificheDomanda>();
			verifica = richiesteDao.selectVerifiche(numeroRichiesta);
			return Response.status(200).entity(verifica).build();
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	// Dal numero della richiesta, controlla se lo stato è PRESA_IN_CARICO e ne
	// cambia lo stato in AMMISSIBILE
	public Response ammissibileRichiesta(CambioStatoPopUp datiPopUp, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglio> listerrorservice = new ArrayList<ErroreDettaglio>();
		Errore error = new Errore();
		try {
			ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(datiPopUp.getNumerodomanda());
			if (richiesta != null) {
				listerrorservice = validateGeneric.validateIsee(richiesta.getIsee(), Constants.AMMISSIBILE);

				generateResponseErrorExceptionForValidate(listerrorservice, HttpStatus.BAD_REQUEST,
						"errore in validate");

				List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();
				allegati = richiesteDao.selectAllegatiFromNumeroRichiesta(richiesta.getDomandaDetId());

				if (!allegati.isEmpty())
					richiesta.setAllegati(allegati);
				if (richiesta.getStato().equalsIgnoreCase(Constants.PRESA_IN_CARICO)) {
					cambioStatoDomanda(richiesta, Constants.AMMISSIBILE, userInfo.getCodFisc(),
							datiPopUp.getNumerodomanda(), datiPopUp.getNotaCittadino(), datiPopUp.getNotaInterna(),
							securityContext, httpHeaders, httpRequest);

					long idGraduatoria = graduatoriaDao.selectGraduatoriaIdBySportelloId(richiesta.getSportelloId())
							.longValue();
					if (graduatoriaDao.checkStatoGraduatoria(idGraduatoria, Constants.PROVVISORIA)) {
						graduatoriaDao.updateRGraduatoriaStato(idGraduatoria, Constants.DA_AGGIORNARE,
								userInfo.getCodFisc());
						log.info("Cambio stato graduatoria in DA_AGGIORNARE");
					}

					return Response.status(200).entity(true).build();
				} else {
					generateResponseErrorException(CodeErrorEnum.ERR13, HttpStatus.BAD_REQUEST,
							"stato richiesta errato", userInfo.getCodFisc());
				}
			} else {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND, "domanda inesistente",
						datiPopUp.getNumerodomanda());
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

	// Dal numero della richiesta, controlla se lo stato è PRESA_IN_CARICO o
	// AMMISSIBILE e ne
	// cambia lo stato in PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA
	public Response preavvisoPerNonAmmissibilitaRichiesta(CambioStatoPopUp datiPopUp, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);

		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName(); // nome del metodo, inserito nei log in caso di errore

		Errore error = new Errore();

		try {
			ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(datiPopUp.getNumerodomanda());
			if (richiesta != null) {
				List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();
				allegati = richiesteDao.selectAllegatiFromNumeroRichiesta(richiesta.getDomandaDetId());

				if (!allegati.isEmpty())
					richiesta.setAllegati(allegati);

				if (richiesta != null && richiesta.getStato().equalsIgnoreCase(Constants.PRESA_IN_CARICO)) {
					cambioStatoDomanda(richiesta, Constants.PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA,
							userInfo.getCodFisc(), datiPopUp.getNumerodomanda(), datiPopUp.getNotaCittadino(),
							datiPopUp.getNotaInterna(), securityContext, httpHeaders, httpRequest);

					servizioRestService.getInviaNotifica(httpHeaders, httpRequest, datiPopUp.getNumerodomanda(),
							"BO_PREAVVISO_DINIEGO_NON_AMMISSIBILITA", richiesta.getRichiedente().getCf());

					return Response.status(200).entity(true).build();
				} else if (richiesta != null && richiesta.getStato().equalsIgnoreCase(Constants.AMMISSIBILE)) {
					// DA MODIFICARE NEL CASO SI DEBBA FARE QUALCOSA DI DIVERSO PER LO STATO
					// AMMISSIBILE

					cambioStatoDomanda(richiesta, Constants.PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA,
							userInfo.getCodFisc(), datiPopUp.getNumerodomanda(), datiPopUp.getNotaCittadino(),
							datiPopUp.getNotaInterna(), securityContext, httpHeaders, httpRequest);

					servizioRestService.getInviaNotifica(httpHeaders, httpRequest, datiPopUp.getNumerodomanda(),
							"BO_PREAVVISO_DINIEGO_NON_AMMISSIBILITA", richiesta.getRichiedente().getCf());

					return Response.status(200).entity(true).build();
				} else {
					// dico al front-end che c'è stato un errore
					generateResponseErrorException(CodeErrorEnum.ERR13, HttpStatus.BAD_REQUEST,
							"stato richiesta errato", userInfo.getCodFisc());
				}
			} else {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND, "domanda inesistente",
						datiPopUp.getNumerodomanda());
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

	public Response verificaContatto(String numeroRichiesta, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName(); // nome del metodo, inserito nei log in caso di errore
		ModelMessaggio messaggio = new ModelMessaggio();
		List<ErroreDettaglio> listerrorservice = new ArrayList<ErroreDettaglio>();
		Errore error = new Errore();
		try {
			ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			if (richiesta != null) {
				// controllo se impostata email e preferenze nel caso prima di cominciare tutto
				listerrorservice = validateGeneric.checkContatti(listerrorservice, richiesta.getRichiedente().getCf(),
						userInfo.getCodFisc());
				if (!listerrorservice.isEmpty()) {
					messaggio.setCodice("ERR20");
					messaggio.setDescrizione(listerrorservice.get(0).getValore());
					return Response.status(200).entity(messaggio).build();
				}
			} else {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND, "domanda inesistente",
						numeroRichiesta);
			}
			messaggio.setCodice("ERR20");
			messaggio.setDescrizione("contattoesiste");
			return Response.status(200).entity(messaggio).build();
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}

		return error.generateResponseError();
	}

	// Dal numero della richiesta, controlla se lo stato è AMMISSIBILE e ne
	// cambia lo stato in AMMESSA
	public Response ammessaRichiesta(CambioStatoPopUp datiPopUp, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);

		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName(); // nome del metodo, inserito nei log in caso di errore

		Errore error = new Errore();

		try {
			ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(datiPopUp.getNumerodomanda());
			if (richiesta != null) {
				List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();
				allegati = richiesteDao.selectAllegatiFromNumeroRichiesta(richiesta.getDomandaDetId());

				if (!allegati.isEmpty())
					richiesta.setAllegati(allegati);

				if (richiesta.getStato().equalsIgnoreCase(Constants.AMMISSIBILE)) {
					cambioStatoDomanda(richiesta, Constants.AMMESSA, userInfo.getCodFisc(),
							datiPopUp.getNumerodomanda(), datiPopUp.getNotaCittadino(), datiPopUp.getNotaInterna(),
							securityContext, httpHeaders, httpRequest);

					long idGraduatoria = graduatoriaDao.selectGraduatoriaIdBySportelloId(richiesta.getSportelloId());
					if (graduatoriaDao.checkStatoGraduatoria(idGraduatoria, Constants.PROVVISORIA)) {
						graduatoriaDao.updateRGraduatoriaStato(idGraduatoria, Constants.DA_AGGIORNARE,
								userInfo.getCodFisc());
					}
					return Response.status(200).entity(true).build();
				} else if (richiesta.getStato().equalsIgnoreCase(Constants.CONTRODEDOTTA)) {
					cambioStatoDomanda(richiesta, Constants.AMMESSA, userInfo.getCodFisc(),
							datiPopUp.getNumerodomanda(), datiPopUp.getNotaCittadino(), datiPopUp.getNotaInterna(),
							securityContext, httpHeaders, httpRequest);

					long idGraduatoria = graduatoriaDao.selectGraduatoriaIdBySportelloId(richiesta.getSportelloId());
					if (graduatoriaDao.checkStatoGraduatoria(idGraduatoria, Constants.PROVVISORIA)) {
						graduatoriaDao.updateRGraduatoriaStato(idGraduatoria, Constants.DA_AGGIORNARE,
								userInfo.getCodFisc());
					}

					return Response.status(200).entity(true).build();
				} else {
					// dico al front-end che c'è stato un errore
					generateResponseErrorException(CodeErrorEnum.ERR13, HttpStatus.BAD_REQUEST,
							"stato richiesta errato", userInfo.getCodFisc());
				}
			} else {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND, "domanda inesistente",
						datiPopUp.getNumerodomanda());
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

	// Dal numero della richiesta, controlla se lo stato è AMMESSA o
	// PERFEZIONATA e ne cambia lo stato in IN PAGAMENTO
	public Response inPagamentoRichiesta(CambioStatoPopUp datiPopUp, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);

		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName(); // nome del metodo, inserito nei log in caso di errore

		Errore error = new Errore();

		try {
			ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(datiPopUp.getNumerodomanda());
			if (richiesta != null) {
				List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();
				allegati = richiesteDao.selectAllegatiFromNumeroRichiesta(richiesta.getDomandaDetId());

				if (!allegati.isEmpty())
					richiesta.setAllegati(allegati);

				// if (richiesta.getStato().equalsIgnoreCase(Constants.AMMESSA)) {
				// cambioStatoDomanda(richiesta, Constants.IN_PAGAMENTO, userInfo.getCodFisc(),
				// datiPopUp.getNumerodomanda(), datiPopUp.getNotaCittadino(),
				// datiPopUp.getNotaInterna(),
				// securityContext, httpHeaders, httpRequest);
				//
				// servizioRestService.getInviaNotifica(httpHeaders, httpRequest,
				// datiPopUp.getNumerodomanda(),
				// "BO_AVVISO_PAGAMENTO", richiesta.getRichiedente().getCf());
				//
				// return Response.status(200).entity(true).build();
				// } else
				if (richiesta.getStato().equalsIgnoreCase(Constants.PERFEZIONATA_IN_PAGAMENTO)) {
					// DA MODIFICARE NEL CASO SI DEBBA FARE QUALCOSA DI DIVERSO PER LO STATO
					// PERFEZIONATA IN PAGAMENTO

					cambioStatoDomanda(richiesta, Constants.IN_PAGAMENTO, userInfo.getCodFisc(),
							datiPopUp.getNumerodomanda(), datiPopUp.getNotaCittadino(), datiPopUp.getNotaInterna(),
							securityContext, httpHeaders, httpRequest);

					// servizioRestService.getInviaNotifica(httpHeaders, httpRequest,
					// datiPopUp.getNumerodomanda(),
					// "BO_PERFEZIONATA_IN_PAGAMENTO", richiesta.getRichiedente().getCf());

					return Response.status(200).entity(true).build();
				} else {
					// dico al front-end che c'è stato un errore
					generateResponseErrorException(CodeErrorEnum.ERR13, HttpStatus.BAD_REQUEST,
							"stato richiesta errato", userInfo.getCodFisc());
				}
			} else {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND, "domanda inesistente",
						datiPopUp.getNumerodomanda());
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

	/*
	 * Cambio stato domanda: AMMISSIBILE --> AMMESSA CON RISERVA Viene controllato
	 * se la richiesta e' nello stato di AMMISSIBILE, viene poi cambiato lo stato in
	 * AMMESSA CON RISERVA
	 */
	public Response ammessaConRiservaRichiesta(CambioStatoPopUp datiPopUp, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		Errore error = new Errore();
		try {
			ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(datiPopUp.getNumerodomanda());
			if (richiesta != null) {

				List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();
				allegati = richiesteDao.selectAllegatiFromNumeroRichiesta(richiesta.getDomandaDetId());

				if (!allegati.isEmpty()) {
					richiesta.setAllegati(allegati);
				}
				if (richiesta.getStato().equalsIgnoreCase(Constants.AMMISSIBILE)) { // Stato
																					// AMMISSIBILE?
					cambioStatoDomanda(richiesta, Constants.AMMESSA_RISERVA, userInfo.getCodFisc(),
							datiPopUp.getNumerodomanda(), datiPopUp.getNotaCittadino(), datiPopUp.getNotaInterna(),
							securityContext, httpHeaders, httpRequest);

					long idGraduatoria = graduatoriaDao.selectGraduatoriaIdBySportelloId(richiesta.getSportelloId());
					if (graduatoriaDao.checkStatoGraduatoria(idGraduatoria, Constants.PROVVISORIA)) {
						graduatoriaDao.updateRGraduatoriaStato(idGraduatoria, Constants.DA_AGGIORNARE,
								userInfo.getCodFisc());
					}

					return Response.status(200).entity(true).build();
				} else if (richiesta.getStato().equalsIgnoreCase(Constants.CONTRODEDOTTA)) { // Stato AMMISSIBILE?
					cambioStatoDomanda(richiesta, Constants.AMMESSA_RISERVA, userInfo.getCodFisc(),
							datiPopUp.getNumerodomanda(), datiPopUp.getNotaCittadino(), datiPopUp.getNotaInterna(),
							securityContext, httpHeaders, httpRequest);

					long idGraduatoria = graduatoriaDao.selectGraduatoriaIdBySportelloId(richiesta.getSportelloId());
					if (graduatoriaDao.checkStatoGraduatoria(idGraduatoria, Constants.PROVVISORIA)) {
						graduatoriaDao.updateRGraduatoriaStato(idGraduatoria, Constants.DA_AGGIORNARE,
								userInfo.getCodFisc());
					}
					return Response.status(200).entity(true).build();
				} else {
					generateResponseErrorException(CodeErrorEnum.ERR13, HttpStatus.BAD_REQUEST,
							"stato richiesta errato", userInfo.getCodFisc());
				}
			} else {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND, "domanda inesistente",
						datiPopUp.getNumerodomanda());
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

	/*
	 * Cambio stato domanda: AMMESSA_CON_RISERVA -->
	 * AMMESSA_CON_RISERVA_IN_PAGAMENTO. Viene controllato se la richiesta e' nello
	 * stato di AMMESSA_CON_RISERVA, viene poi cambiato lo stato in
	 * AMMESSA_CON_RISERVA_IN_PAGAMENTO
	 * METODO NON UTILIZZATO
	 */
	public Response ammessaConRiservaInPagamentoRichiesta(CambioStatoPopUp datiPopUp, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		Errore error = new Errore();
		try {
			ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(datiPopUp.getNumerodomanda());
			if (richiesta != null) {

				List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();
				allegati = richiesteDao.selectAllegatiFromNumeroRichiesta(richiesta.getDomandaDetId());

				if (!allegati.isEmpty()) {
					richiesta.setAllegati(allegati);
				}
				if (richiesta.getStato().equalsIgnoreCase(Constants.AMMESSA_RISERVA)) { // Stato AMMESSA_CON_RISERVA?
					cambioStatoDomanda(richiesta, Constants.AMMESSA_RISERVA_IN_PAGAMENTO, userInfo.getCodFisc(),
							datiPopUp.getNumerodomanda(), datiPopUp.getNotaCittadino(), datiPopUp.getNotaInterna(),
							securityContext, httpHeaders, httpRequest);
					// Invio della notifica, BO_AMMETTI_RISERVA_IN_PAGAMENTO
					// servizioRestService.getInviaNotifica(httpHeaders, httpRequest,
					// datiPopUp.getNumerodomanda(),
					// "BO_AMMETTI_RISERVA_IN_PAGAMENTO", richiesta.getRichiedente().getCf());
					return Response.status(200).entity(true).build();
				} else {
					generateResponseErrorException(CodeErrorEnum.ERR13, HttpStatus.BAD_REQUEST,
							"stato richiesta errato", userInfo.getCodFisc());
				}
			} else {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND, "domanda inesistente",
						datiPopUp.getNumerodomanda());
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

	/*
	 * Cambio stato domanda: PERFEZIONATA IN PAGAMENTO --> DINIEGO oppure AMMESSA
	 * --> DINIEGO
	 * Viene controllato se la richiesta e' nello stato di AMMESSA, viene poi
	 * cambiato lo stato in DINIEGO Viene controllato se la richiesta e' nello stato
	 * di PERFEZIONATA IN PAGAMENTO, viene poi cambiato lo stato in DINIEGO
	 */
	public Response diniegoRichiesta(CambioStatoPopUp datiPopUp, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		Errore error = new Errore();
		try {
			ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(datiPopUp.getNumerodomanda());
			if (richiesta != null) {
				List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();
				allegati = richiesteDao.selectAllegatiFromNumeroRichiesta(richiesta.getDomandaDetId());

				if (!allegati.isEmpty()) {
					richiesta.setAllegati(allegati);
				}

				// feature rimozione diniego ammessa or ammessa riserva
				// if (richiesta.getStato().equalsIgnoreCase(Constants.AMMESSA)) {
				// cambioStatoDomanda(richiesta, Constants.DINIEGO, userInfo.getCodFisc(),
				// datiPopUp.getNumerodomanda(), datiPopUp.getNotaCittadino(),
				// datiPopUp.getNotaInterna(),
				// securityContext, httpHeaders, httpRequest);
				// //genero lettera
				// servizioRestService.getCreaLettera(securityContext, httpHeaders, httpRequest,
				// richiesta.getNumero(), richiesta.getRichiedente().getCf(),
				// Constants.LETTERA_DINIEGO,
				// richiesta.getStato());
				// //invio a stardas in partenza eliminato perche' spostato nel batch 20 07 2023
				// long idGraduatoria =
				// graduatoriaDao.selectGraduatoriaIdByDomandaDettaglioId(richiesta.getDomandaDetId());
				// if(idGraduatoria != 0) {
				// if (graduatoriaDao.checkStatoGraduatoria(idGraduatoria,
				// Constants.PROVVISORIA)) {
				// graduatoriaDao.updateRGraduatoriaStato(idGraduatoria,
				// Constants.DA_AGGIORNARE,
				// userInfo.getCodFisc());
				// }
				// }
				// return Response.status(200).entity(true).build();
				// } else
				if (richiesta.getStato().equalsIgnoreCase(Constants.PERFEZIONATA_IN_PAGAMENTO)) {

					cambioStatoDomanda(richiesta, Constants.DINIEGO, userInfo.getCodFisc(),
							datiPopUp.getNumerodomanda(), datiPopUp.getNotaCittadino(), datiPopUp.getNotaInterna(),
							securityContext, httpHeaders, httpRequest);
					// genero lettera
					servizioRestService.getCreaLettera(securityContext, httpHeaders, httpRequest,
							richiesta.getNumero(), richiesta.getRichiedente().getCf(), Constants.LETTERA_DINIEGO,
							richiesta.getStato());
					// invio a stardas in partenza
					servizioRestService.getStarDasPartenza(securityContext, httpHeaders, httpRequest,
							richiesta.getNumero(), richiesta.getRichiedente().getCf(), Constants.LETTERA_DINIEGO);
					servizioRestService.getInviaNotifica(httpHeaders, httpRequest, datiPopUp.getNumerodomanda(),
							"BO_DINIEGO", richiesta.getRichiedente().getCf());
					return Response.status(200).entity(true).build();
				} else if (richiesta.getStato().equalsIgnoreCase(Constants.CONTRODEDOTTA)) {
					cambioStatoDomanda(richiesta, Constants.DINIEGO, userInfo.getCodFisc(),
							datiPopUp.getNumerodomanda(), datiPopUp.getNotaCittadino(), datiPopUp.getNotaInterna(),
							securityContext, httpHeaders, httpRequest);
					// genero lettera
					servizioRestService.getCreaLettera(securityContext, httpHeaders, httpRequest,
							richiesta.getNumero(), richiesta.getRichiedente().getCf(), Constants.LETTERA_DINIEGO,
							richiesta.getStato());
					// invio a stardas in partenza eliminato perche'spostato nel batch 20 07 2023
					return Response.status(200).entity(true).build();
				} else if (richiesta.getStato().equalsIgnoreCase(Constants.NON_AMMISSIBILE)) {
					cambioStatoDomanda(richiesta, Constants.DINIEGO, userInfo.getCodFisc(),
							datiPopUp.getNumerodomanda(), datiPopUp.getNotaCittadino(), datiPopUp.getNotaInterna(),
							securityContext, httpHeaders, httpRequest);
					// genero lettera
					servizioRestService.getCreaLettera(securityContext, httpHeaders, httpRequest,
							richiesta.getNumero(), richiesta.getRichiedente().getCf(), Constants.LETTERA_DINIEGO,
							richiesta.getStato());
					// invio a stardas in partenza eliminato perche'spostato nel batch 20 07 2023
					return Response.status(200).entity(true).build();
					// feature rimozione diniego ammessa or ammessa riserva
					// } else if (richiesta.getStato().equalsIgnoreCase(Constants.AMMESSA_RISERVA))
					// {
					//
					// cambioStatoDomanda(richiesta, Constants.DINIEGO, userInfo.getCodFisc(),
					// datiPopUp.getNumerodomanda(), datiPopUp.getNotaCittadino(),
					// datiPopUp.getNotaInterna(),
					// securityContext, httpHeaders, httpRequest);
					// //genero lettera
					// servizioRestService.getCreaLettera(securityContext, httpHeaders, httpRequest,
					// richiesta.getNumero(), richiesta.getRichiedente().getCf(),
					// Constants.LETTERA_DINIEGO,
					// richiesta.getStato());
					// //invio a stardas in partenza eliminato perche'spostato nel batch 20 07 2023
					// long idGraduatoria =
					// graduatoriaDao.selectGraduatoriaIdByDomandaDettaglioId(richiesta.getDomandaDetId());
					// if(idGraduatoria != 0) {
					// if (graduatoriaDao.checkStatoGraduatoria(idGraduatoria,
					// Constants.PROVVISORIA)) {
					// graduatoriaDao.updateRGraduatoriaStato(idGraduatoria,
					// Constants.DA_AGGIORNARE,
					// userInfo.getCodFisc());
					// }
					// }
					// return Response.status(200).entity(true).build();
				} else {
					generateResponseErrorException(CodeErrorEnum.ERR13, HttpStatus.BAD_REQUEST,
							"stato richiesta errato", userInfo.getCodFisc());
				}
			} else {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND, "domanda inesistente",
						datiPopUp.getNumerodomanda());
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

	public Response salvaDomandaIsee(String numeroRichiesta, ModelIsee isee, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);

		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName(); // nome del metodo, inserito nei log in caso di errore

		List<ErroreDettaglio> listerrorservice = new ArrayList<ErroreDettaglio>();
		Errore error = new Errore();

		try {
			listerrorservice = validateGeneric.validateIsee(isee, "SALVAISEE");
			generateResponseErrorExceptionForValidate(listerrorservice, HttpStatus.BAD_REQUEST, "errore in validate");

			ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			if (richiesta != null) {
				if (richiesta.getStato().equalsIgnoreCase(Constants.PRESA_IN_CARICO)) {
					richiesteDao.salvaIsee(richiesta.getDomandaDetId(), isee, userInfo.getCodFisc());

					return Response.status(200).entity(true).build();
				} else {
					// dico al front-end che c'è stato un errore
					generateResponseErrorException(CodeErrorEnum.ERR13, HttpStatus.BAD_REQUEST,
							"stato richiesta errato", userInfo.getCodFisc());
				}
			} else {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND, "domanda inesistente",
						numeroRichiesta);
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

	public Response salvaDomandaNota(CambioStatoPopUp nota, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {

		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);

		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName(); // nome del metodo, inserito nei log in caso di errore

		Errore error = new Errore();

		try {
			ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(nota.getNumerodomanda());
			if (richiesta != null) {
				if (richiesta.getStato().equalsIgnoreCase(Constants.PRESA_IN_CARICO)) {
					richiesteDao.salvaNota(richiesta.getDomandaDetId(), nota.getNotaInterna(), userInfo.getCodFisc());
					return Response.status(200).entity(true).build();
				} else {
					// dico al front-end che c'è stato un errore
					generateResponseErrorException(CodeErrorEnum.ERR13, HttpStatus.BAD_REQUEST,
							"stato richiesta errato", userInfo.getCodFisc());
				}
			} else {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND, "domanda inesistente",
						nota.getNumerodomanda());
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

	// Dal numero della richiesta, controlla se lo stato CONTRODEDOTTA
	// cambia lo stato in NON AMMISSIBILE
	public Response nonammissibileRichiesta(CambioStatoPopUp datiPopUp, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		Errore error = new Errore();
		try {
			ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(datiPopUp.getNumerodomanda());
			if (richiesta != null) {
				// MODIFICA BUONODOM PROD-1.3.0-006
				// listerrorservice = validateGeneric.validateIsee(richiesta.getIsee(),
				// Constants.NON_AMMISSIBILE);
				//
				// generateResponseErrorExceptionForValidate(listerrorservice,
				// HttpStatus.BAD_REQUEST,
				// "errore in validate");

				List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();
				allegati = richiesteDao.selectAllegatiFromNumeroRichiesta(richiesta.getDomandaDetId());

				if (!allegati.isEmpty()) {
					richiesta.setAllegati(allegati);
				}

				if (richiesta.getStato().equalsIgnoreCase(Constants.CONTRODEDOTTA)) {
					cambioStatoDomanda(richiesta, Constants.NON_AMMISSIBILE, userInfo.getCodFisc(),
							datiPopUp.getNumerodomanda(), datiPopUp.getNotaCittadino(), datiPopUp.getNotaInterna(),
							securityContext, httpHeaders, httpRequest);
					return Response.status(200).entity(true).build();
				} else {
					generateResponseErrorException(CodeErrorEnum.ERR13, HttpStatus.BAD_REQUEST,
							"stato richiesta errato", userInfo.getCodFisc());
				}
			} else {
				generateResponseErrorException(CodeErrorEnum.ERR10, HttpStatus.NOT_FOUND, "domanda inesistente",
						datiPopUp.getNumerodomanda());
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

}
