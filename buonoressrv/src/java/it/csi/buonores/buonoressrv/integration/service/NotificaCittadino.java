/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.integration.service;

import java.net.http.HttpResponse;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonoressrv.business.be.service.base.BaseService;
import it.csi.buonores.buonoressrv.dto.Contact;
import it.csi.buonores.buonoressrv.dto.Errore;
import it.csi.buonores.buonoressrv.dto.ErroreDettaglio;
import it.csi.buonores.buonoressrv.dto.ModelPersonaSintesi;
import it.csi.buonores.buonoressrv.dto.ModelRichiesta;
import it.csi.buonores.buonoressrv.dto.Preferences;
import it.csi.buonores.buonoressrv.exception.DatabaseException;
import it.csi.buonores.buonoressrv.exception.ResponseErrorException;
import it.csi.buonores.buonoressrv.integration.dao.custom.CodParametroDao;
import it.csi.buonores.buonoressrv.integration.dao.custom.IseeDao;
import it.csi.buonores.buonoressrv.integration.dao.custom.RichiesteDao;
import it.csi.buonores.buonoressrv.integration.notificatore.NotificatoreService;
import it.csi.buonores.buonoressrv.integration.notificatore.util.RichiedenteNotificaAsync;
import it.csi.buonores.buonoressrv.util.Constants;
import it.csi.buonores.buonoressrv.util.Util;
import it.csi.buonores.buonoressrv.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoressrv.util.enumerator.HeaderEnum;

@Service
public class NotificaCittadino extends BaseService {


	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	RichiedenteNotificaAsync richiedentenotificaasync;

	@Autowired
	private NotificatoreService notificatore;

	@Autowired
	CodParametroDao parametroDao;

    @Autowired
    IseeDao iseeDao;
    
	public Response execute(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String numeroRichiesta, String tipoNotifica,
			SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		ModelRichiesta richiesta = new ModelRichiesta();
		HttpResponse<String> response = null;
		String uuidIn = null;
		try {

			List<ErroreDettaglio> listError = validateGeneric.validateNotifica(shibIdentitaCodiceFiscale,
					xRequestId, xForwardedFor, xCodiceServizio, numeroRichiesta, tipoNotifica, securityContext,
					httpHeaders,
					httpRequest);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						Errore.generateErrore(HttpStatus.BAD_REQUEST, listError),
						"errore in validate");
			}
			richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			if (richiesta != null) {
				ModelPersonaSintesi richiedente = new ModelPersonaSintesi();
				richiedente.setCf(richiesta.getRichiedente().getCf());
				richiedente.setCognome(richiesta.getRichiedente().getCognome());
				richiedente.setNome(richiesta.getRichiedente().getNome());
				richiedente.setComuneNascita(richiesta.getRichiedente().getComuneNascita());
				richiedente.setDataNascita(richiesta.getRichiedente().getDataNascita());
				richiedente.setProvinciaNascita(richiesta.getRichiedente().getProvinciaNascita());
				richiedente.setStatoNascita(richiesta.getRichiedente().getStatoNascita());
				String destinatario = null;
				if (richiesta.getDestinatario() != null) {
					if (richiesta.getDestinatario().getCf() != null) {
						if (!richiesta.getDestinatario().getCf().equalsIgnoreCase(shibIdentitaCodiceFiscale)) {
							destinatario = richiesta.getDestinatario().getNome() + " "
									+ richiesta.getDestinatario().getCognome();
						}
					}
				}
				String parametronotificatore = parametroDao.selectValoreParametroFromCod(Constants.CHIAMA_NOTIFICATORE,
						Constants.PARAMETRO_GENERICO);
				boolean verificasechiamareNotificatore = true;
				if (parametronotificatore != null)
					verificasechiamareNotificatore = parametronotificatore.equalsIgnoreCase("TRUE") ? true : false;
				if (verificasechiamareNotificatore) {
                     uuidIn=UUID.randomUUID().toString();
                     if (tipoNotifica.equalsIgnoreCase(Constants.RESPINGI_ISEE) || tipoNotifica.equalsIgnoreCase(Constants.NON_CONFORME_ISEE)) {
                         //prendi anno
                         String today = Util.getAnno(new Date());
                         String nota = iseeDao.selectNotaIsee(numeroRichiesta, today);
                         response = notificaCittadino(richiedente, shibIdentitaCodiceFiscale, xRequestId,  xForwardedFor,
                        		 xCodiceServizio,tipoNotifica,destinatario,richiesta.getNumero(),nota,
                        		 richiesta.getStato(), Util.getDataNomeMese(richiesta.getDataRendicontazione()), uuidIn);    
                     } else {
                         response = notificaCittadino(richiedente, shibIdentitaCodiceFiscale, xRequestId,  xForwardedFor,
                        		 xCodiceServizio,tipoNotifica,destinatario,richiesta.getNumero(),richiesta.getNote(),
                        		 richiesta.getStato(), Util.getDataNomeMese(richiesta.getDataRendicontazione()), uuidIn);
                     }
				}
				if(response == null || !(response.statusCode()==200 || response.statusCode()==201)) {
					String body = (response != null && response.body() != null) ? response.body() : "";
					String messageError= "Errore di invio notifica " + body + " per domanda " + richiesta.getNumero();
					generateResponseErrorException(messageError,
							CodeErrorEnum.ERR03, HttpStatus.INTERNAL_SERVER_ERROR,
							" errore in validate " + messageError);	
					
				}
				return Response.ok().entity(uuidIn).header(HeaderEnum.X_REQUEST_ID.getCode(), xRequestId).build();
			} else {
				generateResponseErrorException("Richiesta non trovata",CodeErrorEnum.ERR01,HttpStatus.BAD_REQUEST,"errore in validate");
			}
		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}
		Response esito = error.generateResponseError();
		return esito;
	}

	private HttpResponse<String> notificaCittadino(ModelPersonaSintesi richiedente, String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor,
			String xCodiceServizio, String tipoNotifica, String destinatario, String numero, String motivo,
			String stato, String datarendicontazione, String uuid) {

		return richiedentenotificaasync.notifyAsync(shibIdentitaCodiceFiscale, richiedente, xRequestId, xCodiceServizio,
				tipoNotifica, destinatario, numero, motivo, stato, datarendicontazione, uuid);
	}

	public Response executeContatti(String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Errore error = new Errore();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {

			List<ErroreDettaglio> listError = validateGeneric.validateContatti(shibIdentitaCodiceFiscale,
					xRequestId, xForwardedFor, xCodiceServizio, securityContext, httpHeaders,
					httpRequest);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						Errore.generateErrore(HttpStatus.BAD_REQUEST, listError),
						"errore in validate");
			}
			String parametrocontatti = parametroDao.selectValoreParametroFromCod(Constants.CHIAMA_CONTATTI,
					Constants.PARAMETRO_GENERICO);
			boolean verificasechiamarecontatti = true;
			Contact contatto = new Contact();
			if (parametrocontatti != null)
				verificasechiamarecontatti = parametrocontatti.equalsIgnoreCase("TRUE") ? true : false;
			if (verificasechiamarecontatti) {
				contatto = notificatore.notificaContact(shibIdentitaCodiceFiscale, shibIdentitaCodiceFiscale);
			} else {
				contatto.setEmail("provamail@testcarico.it");
				contatto.setSms("cellcarico");
				contatto.setPhone("telcarico");
				contatto.setUserId(shibIdentitaCodiceFiscale);
			}
			return Response.ok().entity(contatto).header(HeaderEnum.X_REQUEST_ID.getCode(), xRequestId).build();

		} catch (ResponseErrorException e) {
			error = handleResponseErrorException(metodo, e);
		} catch (DatabaseException e) {
			error = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			error = handleException(metodo, e);
		}
		Response esito = error.generateResponseError();
		return esito;
	}
	
    //MODIFICA TAG 004 buonodom
    public Response executePreferenze(String xRequestId, String xForwardedFor,
            String xCodiceServizio,String shibIdentitaCodiceFiscale,SecurityContext securityContext,
            HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
        Errore error = new Errore();
        String metodo = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            List<ErroreDettaglio> listError = validateGeneric.validateContatti(shibIdentitaCodiceFiscale,
                    xRequestId, xForwardedFor, xCodiceServizio, securityContext, httpHeaders,
                    httpRequest);
                         
            if (!listError.isEmpty()) {
	            throw new ResponseErrorException(
	            Errore.generateErrore(HttpStatus.BAD_REQUEST, listError), "errore in validate");
            }
            String parametrocontatti = parametroDao.selectValoreParametroFromCod(Constants.CHIAMA_CONTATTI,Constants.PARAMETRO_GENERICO);
            boolean verificasechiamarecontatti = true;
            Preferences preferenze = new Preferences();
            if (parametrocontatti != null) {
                verificasechiamarecontatti = parametrocontatti.equalsIgnoreCase("TRUE") ? true : false;
            }
        
        	if(verificasechiamarecontatti) {
                preferenze = notificatore.notificaPreferenze(shibIdentitaCodiceFiscale, shibIdentitaCodiceFiscale);
            } else {
                preferenze.setChannels("push,email");
                preferenze.setUuid("152b8fc3-a960-4cde-825b-4b3d2bbb5ed3");
                preferenze.setServiceName("welfres");
                preferenze.setUserId(shibIdentitaCodiceFiscale);
            }
           	
        	return Response.ok().entity(preferenze).header(HeaderEnum.X_REQUEST_ID.getCode(), xRequestId).build();
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
