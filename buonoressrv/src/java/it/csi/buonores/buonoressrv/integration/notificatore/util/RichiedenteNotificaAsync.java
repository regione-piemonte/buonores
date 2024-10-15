/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.integration.notificatore.util;

import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import it.csi.buonores.buonoressrv.dto.ModelPersonaSintesi;
import it.csi.buonores.buonoressrv.exception.DatabaseException;
import it.csi.buonores.buonoressrv.integration.notificatore.NotificatoreService;
import it.csi.buonores.buonoressrv.util.LoggerUtil;

@Component
public class RichiedenteNotificaAsync extends LoggerUtil {

	@Autowired
	NotificatoreService notificatoreService;

	@Async
	public HttpResponse<String> notifyAsync(String shibIdentitaCodiceFiscale, ModelPersonaSintesi richiedente, String xRequestId,
			String xCodiceServizio,
			String tipoNotifica, String destinatario, String numero, String motivo, String stato, String datarendicontazione, String uuid) {
		HttpResponse<String> response = null;
		StringBuffer sb = new StringBuffer(
				" NOTIFICA INVIATA PER " + richiedente.getCf() + "- STATO NOTIFICA " + tipoNotifica);
		try {
			response=notificatoreService.notificaEventoRichiedente(shibIdentitaCodiceFiscale, richiedente, xRequestId,
					tipoNotifica, destinatario, numero, motivo, stato, datarendicontazione, uuid);

			sb.append(" OK");

		} catch (DatabaseException e1) {
			logError("notifyAsync", "Errore riguardante database:", e1.getMessage());
			sb.append("Errore riguardante database:" + e1.getMessage());
		} catch (Exception e) {
			logError("notifyAsync", "Exception invio notifica:" + e.getCause() + " - " + e.getMessage());
			sb.append("Exception invio notifica:" + e.getCause() + " - " + e.getMessage());
		}
		logInfo("execute-notifyAsync", sb.toString());
		return response;
	}

	@Async
	public void notifyContactAsync(String cf, String shibIdentitaCodiceFiscale) {

		StringBuffer sb = new StringBuffer(" NOTIFICA INVIATA PER " + cf);
		try {
			notificatoreService.notificaContact(cf, shibIdentitaCodiceFiscale);

			sb.append(" OK");

		} catch (DatabaseException e1) {
			logError("notifyAsync", "Errore riguardante database:" + e1.getMessage());
			sb.append("Errore riguardante database:" + e1.getMessage());
		} catch (Exception e) {
			logError("notifyAsync", "Exception invio notifica:" + e.getCause() + " - " + e.getMessage());
			sb.append("Exception invio notifica:" + e.getCause() + " - " + e.getMessage());
		}
		logInfo("execute-notifyAsync", sb.toString());
	}

}
