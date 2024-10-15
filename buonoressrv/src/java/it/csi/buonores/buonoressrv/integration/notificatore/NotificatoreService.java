/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.integration.notificatore;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import it.csi.buonores.buonoressrv.dto.Contact;
import it.csi.buonores.buonoressrv.dto.DmaccREvnotDestMsg;
import it.csi.buonores.buonoressrv.dto.ModelIncompatibilitaRichiesta;
import it.csi.buonores.buonoressrv.dto.ModelPersonaSintesi;
import it.csi.buonores.buonoressrv.dto.Preferences;
import it.csi.buonores.buonoressrv.exception.DatabaseException;
import it.csi.buonores.buonoressrv.integration.dao.custom.CodParametroDao;
import it.csi.buonores.buonoressrv.integration.dao.custom.RichiesteDao;
import it.csi.buonores.buonoressrv.integration.notificatore.dto.EmailPayload;
import it.csi.buonores.buonoressrv.integration.notificatore.dto.MexPayload;
import it.csi.buonores.buonoressrv.integration.notificatore.dto.NotificaCustom;
import it.csi.buonores.buonoressrv.integration.notificatore.dto.PayloadNotifica;
import it.csi.buonores.buonoressrv.integration.notificatore.dto.PushPayload;
import it.csi.buonores.buonoressrv.integration.notificatore.enumerator.NotificatoreEventCode;
import it.csi.buonores.buonoressrv.util.BuonoResSrvProperties;
import it.csi.buonores.buonoressrv.util.Constants;
import it.csi.buonores.buonoressrv.util.LoggerUtil;
import it.csi.buonores.buonoressrv.util.enumerator.ApiHeaderParamEnum;

@Service
public class NotificatoreService extends LoggerUtil {
	// https://gitlab.csi.it/user-notification-platform/unpdocumentazione/blob/master/documentazione%20fornitori/NOTIFY-specifiche.md

	@Autowired
	private BuonoResSrvProperties properties;

	private final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

	private final DateFormat DATE_FORMAT_ISO_8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	@Autowired
	private CodParametroDao parametroDao;

	@Autowired
	private RichiesteDao richiestaDao;

	public HttpResponse<String> notificaEventoRichiedente(String cfMittente, ModelPersonaSintesi soggetto,
			String xRequestId, String tipoNotifica, String destinatario, String numero, String motivo, String stato, String datarendicontazione, String uuid)
			throws Exception {
		HttpResponse<String> response = notificaMessaggio(cfMittente, soggetto, xRequestId, tipoNotifica, destinatario,
				numero, motivo, stato, datarendicontazione, uuid);
		return response;
	}

	public Contact notificaContact(String cfMittente, String iride) throws Exception {
		return sendContact(cfMittente, iride);
	}

	// MODIFICA TAG 004 buonodom
	public Preferences notificaPreferenze(String cfMittente, String iride) throws Exception {
		return sendPreferences(cfMittente, iride);
	}

	/**
	 * SEND notifica
	 * 
	 * @param cfSoggetto
	 * @param uuid
	 * @param codiceEventoTitle
	 * @param codiceEventoBody
	 * @throws DatabaseException
	 */
	private HttpResponse<String> notificaMessaggio(String cfMittente, ModelPersonaSintesi soggettoRicevente,
			String xRequestId, String tipoNotifica, String destinatario, String numero, String motivo, String stato,
			String datarendicontazione, String uuid)
			throws Exception {
		NotificaCustom notificaCustom = buildNotificaCustom(cfMittente, soggettoRicevente, tipoNotifica, destinatario,
				numero, motivo, stato, datarendicontazione, uuid);
		HttpResponse<String> result = sendNotificaEvento(notificaCustom, cfMittente, properties.getTokenApplicativo(),
				xRequestId);
		return result;

	}

	private NotificaCustom buildNotificaCustom(String cfMittente, ModelPersonaSintesi soggettoRicevente,
			String tipoNotifica, String destinatario, String numero, String motivo, String stato, 
			String datarendicontazione, String uuidIn)
			throws DatabaseException {
		NotificaCustom notifica = new NotificaCustom();
		notifica.setUuid(uuidIn);
		notifica.setExpireAt(DATE_FORMAT_ISO_8601.format(DateUtils.addHours(new Date(), 1)));
		PayloadNotifica payload = buildPayloadNotifica(cfMittente, soggettoRicevente, tipoNotifica, destinatario,
				uuidIn, numero, motivo, stato, datarendicontazione);
		notifica.setPayload(payload);
		return notifica;
	}

	private PayloadNotifica buildPayloadNotifica(String cfMittente, ModelPersonaSintesi soggettoRicevente,
			String tipoNotifica, String destinatario, String uuidIn, String numero, String motivo, String stato,
			String datarendicontazione)
			throws DatabaseException {
		PayloadNotifica payload = new PayloadNotifica();
		payload.setId(uuidIn);
		payload.setUserId(soggettoRicevente.getCf());
		payload.setTag(properties.getTag());
		DmaccREvnotDestMsg eventoBody = new DmaccREvnotDestMsg();
		String datacontrodedotta = null;
		String urlCodCit = parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.URL_BUONO_SOCIALE.getCode(),
				Constants.NOTIFICATORE);
		if (tipoNotifica.equalsIgnoreCase(Constants.INVIO_CITTADINO) && stato.equalsIgnoreCase(Constants.INVIATA)) {
			eventoBody
					.setSubject_mail(
							buildSubject(
									parametroDao.selectValoreParametroFromCod(
											NotificatoreEventCode.MSG_SUBJECT_INVIO.getCode(), Constants.NOTIFICATORE),
									NotificatoreEventCode.MSG_SUBJECT_INVIO.getTitle()));
			eventoBody
					.setSubject_push(
							buildSubject(
									parametroDao.selectValoreParametroFromCod(
											NotificatoreEventCode.MSG_SUBJECT_INVIO.getCode(), Constants.NOTIFICATORE),
									NotificatoreEventCode.MSG_SUBJECT_INVIO.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			if (destinatario != null)
				eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
						NotificatoreEventCode.MSG_BODY_EMAIL_INVIO_DESTINATARIO.getCode(), Constants.NOTIFICATORE));
			else
				eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
						NotificatoreEventCode.MSG_BODY_EMAIL_INVIO.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_PUSH_INVIO.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.RETTIFICA_CITTADINO)
				&& stato.equalsIgnoreCase(Constants.RETTIFICATA)) {
			eventoBody
					.setSubject_mail(buildSubject(
							parametroDao.selectValoreParametroFromCod(
									NotificatoreEventCode.MSG_SUBJECT_RETTIFICA.getCode(), Constants.NOTIFICATORE),
							NotificatoreEventCode.MSG_SUBJECT_RETTIFICA.getTitle()));
			eventoBody
					.setSubject_push(buildSubject(
							parametroDao.selectValoreParametroFromCod(
									NotificatoreEventCode.MSG_SUBJECT_RETTIFICA.getCode(), Constants.NOTIFICATORE),
							NotificatoreEventCode.MSG_SUBJECT_RETTIFICA.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_EMAIL_RETTIFICA.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_PUSH_RETTIFICA.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.BO_AMMESSA_RISERVA_CONTRATTO)
				&& stato.equalsIgnoreCase(Constants.AMMESSA_RISERVA_IN_PAGAMENTO)) {
			eventoBody.setSubject_mail(buildSubject(
					parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_SUBJECT_BO_AMMESSA_RISERVA_CONDIZIONATA.getCode(),
							Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_AMMESSA_RISERVA_CONDIZIONATA.getTitle()));
			eventoBody.setSubject_push(buildSubject(
					parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_SUBJECT_BO_AMMESSA_RISERVA_CONDIZIONATA.getCode(),
							Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_AMMESSA_RISERVA_CONDIZIONATA.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			ModelIncompatibilitaRichiesta incompatibilita = richiestaDao.selectIncompatibilitaRichiesta(numero);
			if (incompatibilita != null) {
				if (incompatibilita.isIncompatibilitaPerContratto() && !incompatibilita.isNessunaIncompatibilita()) {
					eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_BODY_MAIL_BO_AMMESSA_RISERVA_INCOMPATIBILITA_CONTRATTO.getCode(),
							Constants.NOTIFICATORE));
					eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_BODY_PUSH_BO_AMMESSA_RISERVA_INCOMPATIBILITA_CONTRATTO.getCode(),
							Constants.NOTIFICATORE));
				} else if (incompatibilita.isIncompatibilitaPerContratto()) {
					eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_BODY_EMAIL_BO_AMMESSA_RISERVA_CONTRATTO.getCode(),
							Constants.NOTIFICATORE));
					eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_BODY_PUSH_BO_AMMESSA_RISERVA_CONTRATTO.getCode(),
							Constants.NOTIFICATORE));
				} else if (!incompatibilita.isNessunaIncompatibilita()) {
					eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_BODY_EMAIL_BO_AMMESSA_RISERVA_INCOMPATIBILITA.getCode(),
							Constants.NOTIFICATORE));
					eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_BODY_PUSH_BO_AMMESSA_RISERVA_INCOMPATIBILITA.getCode(),
							Constants.NOTIFICATORE));
				}
			}

			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.BO_PERFEZIONATA_IN_PAGAMENTO)
				&& stato.equalsIgnoreCase(Constants.IN_PAGAMENTO)) {
			eventoBody.setSubject_mail(buildSubject(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_SUBJECT_BO_PERFEZIONATA_IN_PAGAMENTO.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_PERFEZIONATA_IN_PAGAMENTO.getTitle()));
			eventoBody.setSubject_push(buildSubject(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_SUBJECT_BO_PERFEZIONATA_IN_PAGAMENTO.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_PERFEZIONATA_IN_PAGAMENTO.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_MAIL_BO_PERFEZIONATA_IN_PAGAMENTO.getCode(),
					Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_PUSH_BO_PERFEZIONATA_IN_PAGAMENTO.getCode(),
					Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.BO_PREAVVISO_DI_DINIEGO_IN_PAGAMENTO)
				&& stato.equalsIgnoreCase(Constants.PREAVVISO_DINIEGO_IN_PAGAMENTO)) {
			eventoBody.setSubject_mail(buildSubject(
					parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_SUBJECT_BO_PREAVVISO_DINIEGO.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_PREAVVISO_DINIEGO.getTitle()));
			eventoBody.setSubject_push(buildSubject(
					parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_SUBJECT_BO_PREAVVISO_DINIEGO.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_PREAVVISO_DINIEGO.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_MAIL_BO_PREAVVISO_DINIEGO_IN_PAGAMENTO.getCode(),
					Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_PUSH_BO_PREAVVISO_DINIEGO_IN_PAGAMENTO.getCode(),
					Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.BO_DINIEGO) && stato.equalsIgnoreCase(Constants.DINIEGO)) {
			// query per trovare data controdeduzione
			datacontrodedotta = richiestaDao.selectDataControdedotta(numero);
			eventoBody
					.setSubject_mail(buildSubject(
							parametroDao.selectValoreParametroFromCod(
									NotificatoreEventCode.MSG_SUBJECT_BO_DINIEGO.getCode(), Constants.NOTIFICATORE),
							NotificatoreEventCode.MSG_SUBJECT_BO_DINIEGO.getTitle()));
			eventoBody
					.setSubject_push(buildSubject(
							parametroDao.selectValoreParametroFromCod(
									NotificatoreEventCode.MSG_SUBJECT_BO_DINIEGO.getCode(), Constants.NOTIFICATORE),
							NotificatoreEventCode.MSG_SUBJECT_BO_DINIEGO.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			String statopernultimo = richiestaDao.selectStatoPenultimo(numero);
			String statoultimo = richiestaDao.selectStatoPrecedente(numero);
			boolean preavvisodiniego = false;
			boolean rettifica = false;
			boolean controdedotta = false;
			boolean preavvisodiniegoinpagamento = false;
			boolean perfezionatainpagamento = false;
			if (statopernultimo != null && statoultimo != null) {
				if (statopernultimo.contains(Constants.PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA)
						&& statoultimo.contains(Constants.NON_AMMISSIBILE)) {
					preavvisodiniego = true;
				} else if (statopernultimo.contains(Constants.DA_RETTIFICARE)
						|| statopernultimo.contains(Constants.IN_RETTIFICA)
								&& statoultimo.contains(Constants.NON_AMMISSIBILE)) {
					rettifica = true;
				} else if (statoultimo.contains(Constants.AMMESSA) || statoultimo.contains(Constants.AMMESSA_RISERVA)) {
					rettifica = true;
				} else if (statoultimo.contains(Constants.CONTRODEDOTTA)) {
					controdedotta = true;
				} else if (statoultimo.contains(Constants.NON_AMMISSIBILE)
						&& statopernultimo.contains(Constants.CONTRODEDOTTA)) {
					controdedotta = true;
				} else if (statoultimo.contains(Constants.PREAVVISO_DINIEGO_IN_PAGAMENTO)) {
					preavvisodiniegoinpagamento = true;
				} else if (statoultimo.contains(Constants.PERFEZIONATA_IN_PAGAMENTO)) {
					perfezionatainpagamento = true;
				}
			}
			// messagio 3.4.3 si arriva da preavviso di diniego per non ammissibilita non
			// invia le controdeduzioni dopo averle richieste
			// prendo il penultimo stato e vedo se preavviso di diniego per non
			// ammissibilita
			if (preavvisodiniego) {
				eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
						NotificatoreEventCode.MSG_BODY_EMAIL_BO_DINIEGO_DOPO_PREAVVISO.getCode(),
						Constants.NOTIFICATORE));
				eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
						NotificatoreEventCode.MSG_BODY_PUSH_BO_DINIEGO_DOPO_PREAVVISO.getCode(),
						Constants.NOTIFICATORE));
			}
			// messagio 3.4.5 si arriva da in rettifica rettificata non richieste
			// controdeduzioni
			// prendo il penultimo stato e vedo se da rettificare e rettifica
			else if (rettifica) {
				eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
						NotificatoreEventCode.MSG_BODY_EMAIL_BO_DINIEGO_SENZA_PREAVVISO.getCode(),
						Constants.NOTIFICATORE));
				eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
						NotificatoreEventCode.MSG_BODY_PUSH_BO_DINIEGO_SENZA_PREAVVISO.getCode(),
						Constants.NOTIFICATORE));
			}
			// messagio 3.4.4 si arriva da controdedotta
			// prendo il penultimo stato e vedo se da controdedotta
			else if (controdedotta) {
				eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
						NotificatoreEventCode.MSG_BODY_EMAIL_BO_DINIEGO_CON_CONTRODEDUZIONI.getCode(),
						Constants.NOTIFICATORE));
				eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
						NotificatoreEventCode.MSG_BODY_PUSH_BO_DINIEGO_CON_CONTRODEDUZIONI.getCode(),
						Constants.NOTIFICATORE));
			}
			// messaggio da definire
			else if (preavvisodiniegoinpagamento) {
				eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
						NotificatoreEventCode.MSG_BODY_MAIL_DINIEGO_DOPO_PREAVVISO_DI_DINIEGO_IN_PAGAMENTO.getCode(),
						Constants.NOTIFICATORE));
				eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
						NotificatoreEventCode.MSG_BODY_PUSH_DINIEGO_DOPO_PREAVVISO_DI_DINIEGO_IN_PAGAMENTO.getCode(),
						Constants.NOTIFICATORE));
			}
			// messaggio da definire
			else if (perfezionatainpagamento) {
				eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
						NotificatoreEventCode.MSG_BODY_MAIL_DINIEGO_DOPO_PERFEZIONATA_IN_PAGAMENTO.getCode(),
						Constants.NOTIFICATORE));
				eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
						NotificatoreEventCode.MSG_BODY_PUSH_DINIEGO_DOPO_PERFEZIONATA_IN_PAGAMENTO.getCode(),
						Constants.NOTIFICATORE));
			}
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.BO_RETTIFICA)
				&& stato.equalsIgnoreCase(Constants.DA_RETTIFICARE)) {
			eventoBody
					.setSubject_mail(buildSubject(
							parametroDao.selectValoreParametroFromCod(
									NotificatoreEventCode.MSG_SUBJECT_BO_RETTIFICA.getCode(), Constants.NOTIFICATORE),
							NotificatoreEventCode.MSG_SUBJECT_BO_RETTIFICA.getTitle()));
			eventoBody
					.setSubject_push(buildSubject(
							parametroDao.selectValoreParametroFromCod(
									NotificatoreEventCode.MSG_SUBJECT_BO_RETTIFICA.getCode(), Constants.NOTIFICATORE),
							NotificatoreEventCode.MSG_SUBJECT_BO_RETTIFICA.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_EMAIL_BO_RETTIFICA.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_PUSH_BO_RETTIFICA.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} // MODIFICA TAG 004 buonodom
		else if (tipoNotifica.equalsIgnoreCase(Constants.BO_PREAVVISO_DINIEGO_NON_AMMISSIBILITA)
				&& stato.equalsIgnoreCase(Constants.PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA)) {
			eventoBody.setSubject_mail(buildSubject(
					parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_SUBJECT_BO_PREAVVISO_DINIEGO.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_PREAVVISO_DINIEGO.getTitle()));
			eventoBody.setSubject_push(buildSubject(
					parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_SUBJECT_BO_PREAVVISO_DINIEGO.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_PREAVVISO_DINIEGO.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_EMAIL_BO_PREAVVISO_DINIEGO_NON_AMMISSIBILITA.getCode(),
					Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_PUSH_BO_PREAVVISO_DINIEGO_NON_AMMISSIBILITA.getCode(),
					Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.CONTRODEDOTTA_CITTADINO)
				&& stato.equalsIgnoreCase(Constants.CONTRODEDOTTA)) {
			eventoBody.setSubject_mail(buildSubject(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_SUBJECT_CONTRODEDOTTA_CITTADINO.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_CONTRODEDOTTA_CITTADINO.getTitle()));
			eventoBody.setSubject_push(buildSubject(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_SUBJECT_CONTRODEDOTTA_CITTADINO.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_CONTRODEDOTTA_CITTADINO.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_EMAIL_CONTRODEDOTTA_CITTADINO.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_PUSH_CONTRODEDOTTA_CITTADINO.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
			// TAG PROD DOM 2.1.0-001
		} else if (tipoNotifica.equalsIgnoreCase(Constants.RINUNCIATA_CITTADINO)
				&& stato.equalsIgnoreCase(Constants.RINUNCIATA)) {
			eventoBody.setSubject_mail(buildSubject(
					parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_SUBJECT_RINUNCIATA_CITTADINO.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_RINUNCIATA_CITTADINO.getTitle()));
			eventoBody.setSubject_push(buildSubject(
					parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_SUBJECT_RINUNCIATA_CITTADINO.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_RINUNCIATA_CITTADINO.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_EMAIL_RINUNCIATA_CITTADINO.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_PUSH_RINUNCIATA_CITTADINO.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.BO_DINIEGO_SCADENZA_REQUISTI_DECESSO) && stato.equalsIgnoreCase(Constants.DINIEGO)) {
			eventoBody
					.setSubject_mail(buildSubject(
							parametroDao.selectValoreParametroFromCod(
									NotificatoreEventCode.MSG_SUBJECT_BO_DINIEGO.getCode(), Constants.NOTIFICATORE),
							NotificatoreEventCode.MSG_SUBJECT_BO_DINIEGO.getTitle()) + " per decadenza requisiti");
			eventoBody
					.setSubject_push(buildSubject(
							parametroDao.selectValoreParametroFromCod(
									NotificatoreEventCode.MSG_SUBJECT_BO_DINIEGO.getCode(), Constants.NOTIFICATORE),
							NotificatoreEventCode.MSG_SUBJECT_BO_DINIEGO.getTitle1()) + " per decadenza requisiti");
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.MSG_BODY_EMAIL_BO_DINIEGO_SCADENZA_REQUISTI_DECESSO.getCode(),Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.MSG_BODY_PUSH_BO_DINIEGO_SCADENZA_REQUISTI.getCode(),Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody"+eventoBody.getSubject_mail());
		}
		else if (tipoNotifica.equalsIgnoreCase(Constants.BO_DINIEGO_SCADENZA_REQUISTI_CAMBIO_RESIDENZA) && stato.equalsIgnoreCase(Constants.DINIEGO)) {
			eventoBody.setSubject_mail(buildSubject(
					parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.MSG_SUBJECT_BO_DINIEGO.getCode(),Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_DINIEGO.getTitle()) + " per decadenza requisiti") ;
			eventoBody.setSubject_push(buildSubject(
					parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.MSG_SUBJECT_BO_DINIEGO.getCode(),Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_DINIEGO.getTitle1()) + " per decadenza requisiti");
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.MSG_BODY_EMAIL_BO_DINIEGO_SCADENZA_REQUISTI_CAMBIO_RESIDENZA.getCode(),Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.MSG_BODY_PUSH_BO_DINIEGO_SCADENZA_REQUISTI.getCode(),Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody"+eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.PERFEZIONATA_IN_PAGAMENTO_CITTADINO)
				&& stato.equalsIgnoreCase(Constants.PERFEZIONATA_IN_PAGAMENTO)) {
			eventoBody.setSubject_mail(buildSubject(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_SUBJECT_PERFEZIONATA_IN_PAGAMENTO.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_PERFEZIONATA_IN_PAGAMENTO.getTitle()));
			eventoBody.setSubject_push(buildSubject(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_SUBJECT_PERFEZIONATA_IN_PAGAMENTO.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_PERFEZIONATA_IN_PAGAMENTO.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_EMAIL_PERFEZIONATA_IN_PAGAMENTO_CITTADINO.getCode(),
					Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_PUSH_PERFEZIONATA_IN_PAGAMENTO_CITTADINO.getCode(),
					Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.BO_AMMESSA_FINANZIATA)
				&& stato.equalsIgnoreCase(Constants.IN_PAGAMENTO)) {
			eventoBody.setSubject_mail(buildSubject(
					parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_SUBJECT_BO_AMMESSA_FINANZIATA.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_AMMESSA_FINANZIATA.getTitle()));
			eventoBody.setSubject_push(buildSubject(
					parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_SUBJECT_BO_AMMESSA_FINANZIATA.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_AMMESSA_FINANZIATA.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_EMAIL_BO_AMMESSA_FINANZIATA.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_PUSH_BO_AMMESSA_FINANZIATA.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.BO_AMMESSA_NON_FINANZIATA)
				&& (stato.equalsIgnoreCase(Constants.AMMESSA) || stato.equalsIgnoreCase(Constants.AMMESSA_RISERVA))) {
			eventoBody.setSubject_mail(buildSubject(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_SUBJECT_BO_AMMESSA_NON_FINANZIATA.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_AMMESSA_NON_FINANZIATA.getTitle()));
			eventoBody.setSubject_push(buildSubject(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_SUBJECT_BO_AMMESSA_NON_FINANZIATA.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_AMMESSA_NON_FINANZIATA.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_EMAIL_BO_AMMESSA_NON_FINANZIATA.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_PUSH_BO_AMMESSA_NON_FINANZIATA.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.REVOCA_BUONO)) {
            //eventoBody.setSubject_mail(buildSubject(
            //        parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.MSG_SUBJECT_REVOCA_BUONO.getCode(),Constants.NOTIFICATORE),
            //        NotificatoreEventCode.MSG_SUBJECT_REVOCA_BUONO.getTitle())) ;
            eventoBody.setSubject_push(buildSubject(
                    parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.MSG_SUBJECT_REVOCA_BUONO.getCode(),Constants.NOTIFICATORE),
                    NotificatoreEventCode.MSG_SUBJECT_REVOCA_BUONO.getTitle1()));
            eventoBody.setSubject_mex(eventoBody.getSubject_push());
            //eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.MSG_BODY_MAIL_REVOCA_BUONO.getCode(),Constants.NOTIFICATORE));
            eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.MSG_BODY_PUSH_REVOCA_BUONO.getCode(),Constants.NOTIFICATORE));
            eventoBody.setMsg_mex(eventoBody.getMsg_push());
            logInfo("buildPayloadNotifica", "codiceEventoBody"+eventoBody.getSubject_push());
        } else if (tipoNotifica.equalsIgnoreCase(Constants.RESPINGI_ISEE)) { 
            eventoBody.setSubject_mail(buildSubject(
                    parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.MSG_SUBJECT_ISEE_RESPINTO.getCode(),Constants.NOTIFICATORE),
                    NotificatoreEventCode.MSG_SUBJECT_ISEE_RESPINTO.getTitle())) ;
            eventoBody.setSubject_push(buildSubject(
                    parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.MSG_SUBJECT_ISEE_RESPINTO.getCode(),Constants.NOTIFICATORE),
                    NotificatoreEventCode.MSG_SUBJECT_ISEE_RESPINTO.getTitle1()));
            eventoBody.setSubject_mex(eventoBody.getSubject_push());
            eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.MSG_BODY_EMAIL_ISEE_RESPINTO.getCode(),Constants.NOTIFICATORE));
            eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.MSG_BODY_PUSH_ISEE_RESPINTO.getCode(),Constants.NOTIFICATORE));
            eventoBody.setMsg_mex(eventoBody.getMsg_push());
            logInfo("buildPayloadNotifica", "codiceEventoBody"+eventoBody.getSubject_mail());
        } else if (tipoNotifica.equalsIgnoreCase(Constants.NON_CONFORME_ISEE)) { //da implementare nel db
        	eventoBody.setSubject_mail(buildSubject(
                    parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.MSG_SUBJECT_ISEE_NON_CONFORME.getCode(),Constants.NOTIFICATORE),
                    NotificatoreEventCode.MSG_SUBJECT_ISEE_NON_CONFORME.getTitle())) ;
            eventoBody.setSubject_push(buildSubject(
                    parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.MSG_SUBJECT_ISEE_NON_CONFORME.getCode(),Constants.NOTIFICATORE),
                    NotificatoreEventCode.MSG_SUBJECT_ISEE_NON_CONFORME.getTitle1()));
            eventoBody.setSubject_mex(eventoBody.getSubject_push());
            eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.MSG_BODY_EMAIL_ISEE_NON_CONFORME.getCode(),Constants.NOTIFICATORE));
            eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.MSG_BODY_PUSH_ISEE_NON_CONFORME.getCode(),Constants.NOTIFICATORE));
            eventoBody.setMsg_mex(eventoBody.getMsg_push());
            logInfo("buildPayloadNotifica", "codiceEventoBody"+eventoBody.getSubject_mail());
        }

        if (eventoBody.getSubject_mail()!=null) {
			EmailPayload email = buildEmailPayload(eventoBody, eventoBody.getSubject_mail(), soggettoRicevente, urlCodCit,
					destinatario, numero, motivo, datacontrodedotta, datarendicontazione);
			payload.setEmail(email);
        }

        if (eventoBody.getSubject_push()!=null) {
			PushPayload push = buildPushPayload(eventoBody, eventoBody.getSubject_push(), soggettoRicevente, urlCodCit,
					destinatario, numero, motivo);
			payload.setPush(push);
        }

        if (eventoBody.getSubject_mex()!=null) {
			MexPayload mex = buildMexPayload(eventoBody, eventoBody.getSubject_mex(), soggettoRicevente, urlCodCit,
					destinatario, numero, motivo);
			payload.setMex(mex);
        }
		return payload;
	}

	// TAG PROD DOM 2.1.0-001
	private MexPayload buildMexPayload(DmaccREvnotDestMsg eventoBody, String descrizioneEvento,
			ModelPersonaSintesi soggettoRicevente, String urlCodCit, String destinatario, String numero,
			String motivo) {
		MexPayload mex = new MexPayload();
		mex.setTitle(descrizioneEvento);
		String body = buildBody(eventoBody.getMsg_mex(), soggettoRicevente, urlCodCit, destinatario, numero, motivo,
				null, null);
		mex.setBody(body);
		mex.setCalltoaction(urlCodCit);
		return mex;
	}

	// sostituire nei vari body
	// {0}SOGGETTO_NOME_COGNOME richiente
	// {1} NUMERO DOMANDA
	// {2} SE DESTINATARIO DIVERSO DA RICHIEDENTE NOME_COGNOME
	// {3} modivi di revoca diniego ecc
	// {5}URL_SITO SOCIALE
	// {4}DATA CONTRODEDOTTA
	// TAG PROD DOM 2.1.0-001
	private String buildBody(String msg, ModelPersonaSintesi soggettoRicevente, String urlCodCit, String destinatario,
			String numero, String motivo, String datacontrodedotta, String datarendicontazione) {
		String result = msg;
		result = result.replace("{0}", soggettoRicevente.getNome() + " " + soggettoRicevente.getCognome());
		result = result.replace("{1}", numero);
		if (StringUtils.isNotBlank(urlCodCit)) {
			result = result.replace("{5}", urlCodCit);
		} else
			result = result.replace("{5}", "");
		if (StringUtils.isNotBlank(destinatario)) {
			result = result.replace("{2}", destinatario);
		} else
			result = result.replace("{2}", "");
		if (StringUtils.isNotBlank(motivo)) {
			result = result.replace("{3}", motivo);
		} else
			result = result.replace("{3}", "");
		if (StringUtils.isNotBlank(datacontrodedotta)) {
			result = result.replace("{4}", datacontrodedotta);
		} else
			result = result.replace("{4}", "");
		if (StringUtils.isNotBlank(datarendicontazione)) {
			result = result.replace("{6}", datarendicontazione);
		} else
			result = result.replace("{6}", "");
		
		return result;
	}

	private String buildSubject(String msg, String tipoSubject) {
		String result = msg;
		result = result.replace("{0}", tipoSubject);
		return result;
	}

	private PushPayload buildPushPayload(DmaccREvnotDestMsg eventoBody, String descrizioneEvento,
			ModelPersonaSintesi soggettoRicevente, String urlCodCit, String destinatario, String numero,
			String motivo) {
		PushPayload push = new PushPayload();
		push.setTitle(descrizioneEvento);
		String body = buildBody(eventoBody.getMsg_push(), soggettoRicevente, urlCodCit, destinatario, numero, motivo,
				null, null);
		push.setBody(body);
		return push;
	}

	private EmailPayload buildEmailPayload(DmaccREvnotDestMsg eventoBody, String descrizioneEvento,
			ModelPersonaSintesi soggettoRicevente, String urlCodCit, String destinatario, String numero, String motivo,
			String datacontrodedotta, String datarendicontazione) {
		EmailPayload email = new EmailPayload();
		email.setSubject(descrizioneEvento);
		String body = buildBody(eventoBody.getMsg_mail(), soggettoRicevente, urlCodCit, destinatario, numero, motivo,
				datacontrodedotta, datarendicontazione);
		email.setBody(body);
		email.setTemplateId(properties.getTemplateId());
		return email;
	}

	private HttpResponse<String> sendNotificaEvento(NotificaCustom notifica, String cfSoggetto, String tokenApplicativo,
			String xRequestId) throws Exception {
		logInfo("sendNotificaEvento ",
				"params: \n" + ApiHeaderParamEnum.SHIB_IRIDE_IDENTITADIGITALE.getCode() + ": " + cfSoggetto + ", \n"
						+ ApiHeaderParamEnum.X_AUTHENTICATION.getCode() + ": " + tokenApplicativo + "\n"
						+ ApiHeaderParamEnum.X_REQUEST_ID.getCode() + ": " + xRequestId + "\n" + " payload ID: "
						+ notifica.getPayload().getId() + "\n");

		String jsonPayloadString = fromObjectToJsonString(notifica);
		logInfo("sendNotificaEvento-", "payload: " + jsonPayloadString);
		HttpRequest request = HttpRequest.newBuilder().POST(BodyPublishers.ofString(jsonPayloadString))
				.uri(URI.create(properties.getUrlNotificatore()))
				.setHeader(ApiHeaderParamEnum.SHIB_IRIDE_IDENTITADIGITALE.getCode(), cfSoggetto)
				.setHeader(ApiHeaderParamEnum.X_REQUEST_ID.getCode(), xRequestId)
				.setHeader(ApiHeaderParamEnum.X_AUTHENTICATION.getCode(), tokenApplicativo)
				.setHeader("Content-Type", "application/json").build();

		HttpResponse<String> response = null;
		response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		// print status code
		logInfo("sendNotificaEvento ", "status:" + response.statusCode() + " - notifica_uuid: " + notifica.getUuid());
		logInfo("sendNotificaEvento", "response: " + response.toString());
		logInfo("sendNotificaEvento", "responsebody: " + response.body());

		if (!(response.statusCode() == 200 || response.statusCode() == 201)) {
			logError("sendNotificaEvento: ", response.body() + " - notifica_uuid:" + notifica.getUuid());
		}
		return response;
	}

	private String fromObjectToJsonString(NotificaCustom notificaCustom) throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		return ow.writeValueAsString(notificaCustom);
	}

	private Contact sendContact(String cfSoggetto, String iride) throws Exception {
		logInfo("sendNotificaEvento ",
				"params: \n" + ApiHeaderParamEnum.SHIB_IRIDE_IDENTITADIGITALE.getCode() + ": " + iride + ", \n"
						+ ApiHeaderParamEnum.X_AUTHENTICATION.getCode() + ": " + properties.getTokenApplicativoContact()
						+ "\n");

		HttpRequest request = HttpRequest.newBuilder().GET()
				.uri(URI.create(properties.getUrlNotificatoreContact() + "/" + cfSoggetto + "/contacts"))
				.setHeader(ApiHeaderParamEnum.SHIB_IRIDE_IDENTITADIGITALE.getCode(), iride)
				.setHeader(ApiHeaderParamEnum.X_AUTHENTICATION.getCode(), properties.getTokenApplicativoContact())
				.setHeader("Content-Type", "application/json").build();

		HttpResponse<String> response = null;
		response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		// print status code
		logInfo("sendNotificaEvento ", "status:" + response.statusCode());
		logInfo("sendNotificaEvento", "response: " + response.toString());
		logInfo("sendNotificaEvento", "responsebody: " + response.body());
		Contact contatto = new Contact();
		if (!(response.statusCode() == 200 || response.statusCode() == 201)) {
			logError("sendNotificaEvento: ", response.body());
		} else {
			String contattireturn = response.body(); 
			ObjectMapper mapper = new ObjectMapper();
			HashMap<String, Object> map = mapper.readValue(contattireturn, HashMap.class); 
			contatto = buildContact(map); 
		}
		return contatto;
	}
	
	private Contact buildContact(HashMap map){ 
		 Contact contact = new Contact(); 
		 contact.setUserId((String) map.get("user_id")); 
		 contact.setEmail((String) map.get("email"));  
		 contact.setPhone((String) map.get("phone"));
		 contact.setSms((String) map.get("sms"));
		 return contact; 
		} 

	// MODIFICA TAG 004 buonodom
	private Preferences sendPreferences(String cfSoggetto, String iride) throws Exception {
		logInfo("sendPreferenze ",
				"params: \n" + ApiHeaderParamEnum.SHIB_IRIDE_IDENTITADIGITALE.getCode() + ": " + iride + ", \n"
						+ ApiHeaderParamEnum.X_AUTHENTICATION.getCode() + ": " + properties.getTokenApplicativoContact()
						+ "\n");

		HttpRequest request = HttpRequest.newBuilder().GET()
				.uri(URI.create(properties.getUrlNotificatoreContact() + "/" + cfSoggetto + "/preferences/"
						+ properties.getNomeApplicazione()))
				.setHeader(ApiHeaderParamEnum.SHIB_IRIDE_IDENTITADIGITALE.getCode(), iride)
				.setHeader(ApiHeaderParamEnum.X_AUTHENTICATION.getCode(), properties.getTokenApplicativoContact())
				.setHeader("Content-Type", "application/json").build();

		HttpResponse<String> response = null;
		response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		// print status code
		logInfo("sendPreferenzeEvento ", "status:" + response.statusCode());
		logInfo("sendPreferenzeEvento", "response: " + response.toString());
		logInfo("sendPreferenzeEvento", "responsebody: " + response.body());
		Preferences preferenze = new Preferences();
		if (!(response.statusCode() == 200 || response.statusCode() == 201)) {
			logError("sendPreferenzeEvento: ", response.body());
		} else {
			preferenze = new ObjectMapper().readValue(response.body(), Preferences.class);
		}
		return preferenze;
	}
}
