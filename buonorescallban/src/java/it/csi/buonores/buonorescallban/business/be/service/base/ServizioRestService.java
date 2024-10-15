/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonorescallban.business.be.service.base;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonorescallban.exception.DatabaseException;
import it.csi.buonores.buonorescallban.integration.rest.RestBaseService;
import it.csi.buonores.buonorescallban.util.BuonoResCallbanProperties;
import it.csi.buonores.buonorescallban.util.rest.ResponseRest;

@Service
public class ServizioRestService extends BaseService{
	@Autowired
	BuonoResCallbanProperties properties;
	
	@Autowired
	RestBaseService restbase;
	
    @Autowired
    BatchService batchService;
	
	private static final String NOTIFICHE = "/notifiche/";
	
	public ResponseRest getInviaNotifica(String xRequestId, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest, String numerodomanda, String tipoNotifica) {
		ResponseRest response = new ResponseRest();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
        Long risultOk = null;
        String batchCod = null;
        String motivoCod = null;
        batchCod = "CALLBACK_NOTIFICA";
        motivoCod = "CALLBACK_NOTIFICA";
        
		try {
            risultOk = batchService.inserisciBatchOK(numerodomanda,batchCod,motivoCod,"CALL_BACK", "STATO_OK");
			response = restbase.eseguiGet(httpHeaders,
					properties.getBuonoressrvurl() + NOTIFICHE + numerodomanda + "/" + tipoNotifica, false, null);
            if (response.getStatusCode()==200 || response.getStatusCode()==201) {
                //inserisco nella det
                batchService.inserisciBatchStepOK("INVIO_NOTIFICA_" + tipoNotifica,"Domanda " + numerodomanda + " CALL_BACK_BANDI_" + tipoNotifica, risultOk, "CALL_BACK", response.getJson());
            }
            batchService.deleteEsitoVuotoOk(risultOk);
            log.info(metodo + " - Fine ");
		} catch (DatabaseException e) {
			log.error(metodo + " - errore db - " + e);
            batchService.deleteEsitoVuotoOk(risultOk);
		} catch (Exception e) {
			log.error(metodo + " - errore generico - " + e);
            batchService.deleteEsitoVuotoOk(risultOk);
		}
		return response;
	}
	
	
}
