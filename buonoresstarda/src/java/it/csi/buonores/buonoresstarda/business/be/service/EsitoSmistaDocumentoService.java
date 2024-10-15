/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresstarda.business.be.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonoresstarda.business.be.service.base.BaseService;
import it.csi.buonores.buonoresstarda.dto.ErroreDettaglio;
import it.csi.buonores.buonoresstarda.dto.EsitoSmistaDocumentoRequest;
import it.csi.buonores.buonoresstarda.dto.EsitoSmistaDocumentoType;
import it.csi.buonores.buonoresstarda.dto.EsitoStep;
import it.csi.buonores.buonoresstarda.dto.InformazioneType;
import it.csi.buonores.buonoresstarda.dto.ResultType;
import it.csi.buonores.buonoresstarda.dto.custom.StardasLog;
import it.csi.buonores.buonoresstarda.exception.DatabaseException;
import it.csi.buonores.buonoresstarda.integration.dao.custom.RichiesteDao;
import it.csi.buonores.buonoresstarda.integration.dao.custom.StardasLogDao;
import it.csi.buonores.buonoresstarda.util.Constants;
import it.csi.buonores.buonoresstarda.util.Util;
import it.csi.buonores.buonoresstarda.util.enumerator.CodeErrorEnum;

@Service
//Nessuna differenza tra post e put
public class EsitoSmistaDocumentoService extends BaseService {
	@Autowired
	StardasLogDao logStardas;

	@Autowired
	RichiesteDao richiesteDao;

	/*
	 * Nel payload in allegato ci sono i 2 possibili esiti step con due nomi diversi
	 * --> i 2 possibili nomi step sono: - ARCHIVIAZIONE_ACTA - PROTOCOLLAZIONE_ACTA
	 * sono stringhe costanti quindi dovremo testare se sono o meno presenti e
	 * verificarne il codice esito sulla struttura esitoStep corrispondente. La
	 * struttura esitoTrattamento ha come codice esito il risultato dei due codici
	 * esito degli step, vince sempre l'esito negativo (ovvero se è negativo uno dei
	 * due step lo sarà pure l'esito trattamento). La chiamata alla callback potrà
	 * essere effettuata sia con un esitoStep solo o anche con entrambi; nel caso in
	 * cui ci sarà un solo esitoStep stardass avrà eseguito solo il trattamento
	 * dell'archiviazione su acta e non la protocollazione, quindi in questo caso
	 * richiamerà la callback una seconda volta per lo stesso documento usando in
	 * questo caso il servizio di put. Nel nostro caso, in cui abbiamo un documento
	 * principale e altri allegati, potremmo avere uno scenario simile:
	 * 
	 * 1 chiamata callback in post per la domanda (con esitoStep archiviazione_acta)
	 * 2 chiamata callback in post per allegato della domanda (con esitoStep
	 * archiviazione_acta) 3 chiamata callback in post per allegato della domanda
	 * (con esitoStep archiviazione_acta) 4 chiamata callback in put per la domanda
	 * (con esitoStep protocollazione_acta)
	 * 
	 * Potrebbe capitare anche di ricevere una callback che con un esito step non
	 * positivo e in quel caso stardass eseguirà un'altra callback (anche in questo
	 * caso viene chiamata una put).
	 * 
	 * I dati del protocollo sono valorizzati nella sezione del payload
	 * informazioniAggiuntive e hanno il nome (come da allegato di esempio):
	 * Numero_reg_protocollo Tipo_reg_protocollo Data_reg_protocollo
	 * 
	 * ma sono valorizzati solo nel caso in cui ci sia l'esitoStep
	 * PROTOCOLLAZIONE_ACTA.
	 * 
	 * Il campo messageId è valorizzato con l'uuid che ci viene fornito alla
	 * chiamata che eseguiamo noi alla smistadocumento. Il campo idDocumentoFruitore
	 * avrà invece il campo idDocumentoFruitore che abbiamo passato noi alla
	 * request.
	 * 
	 */
	public Response executePut(EsitoSmistaDocumentoRequest body, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return execute(body, securityContext, httpHeaders, httpRequest);
	}
	
	public Response executePost(EsitoSmistaDocumentoRequest body, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return execute(body, securityContext, httpHeaders, httpRequest);
	}

	private Response execute(EsitoSmistaDocumentoRequest body, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		// prendo esito dal body per poter mettere numero protocollo ed esito poi creo
		// tabella per log
		ResultType esitocallback = new ResultType();
		EsitoStep esitostep = new EsitoStep();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		try {
			List<ErroreDettaglio> listError = validateGeneric.validateSmista(body, securityContext, httpHeaders,
					httpRequest);
			if (!listError.isEmpty()) {
				esitocallback.setCodice(Constants.KO_APPLICATIVI);
				esitocallback.setMessaggio(validateGeneric.getValueGenericErrorStardas(CodeErrorEnum.ERR17.getCode(),
						metodo + " " + listError.toString()));
				esitostep.setEsito(esitocallback);
				return Response.ok().entity(esitostep).build();
			}
			// verifica se otteniamo protocollo ed archiviazione
			StardasLog log = new StardasLog();
			boolean protocolloEsiste = false;
			String protocollo = null;
			String idDocumentoFruitore = null;
			String dataprotocollo = null;
			String tipoprotocollo = null;
			if (body.getEsitoSmistaDocumento() != null && body.getEsitoSmistaDocumento().getEsito() != null) {
				EsitoSmistaDocumentoType esitoType = body.getEsitoSmistaDocumento().getEsito();
				log.setMessageUUID(esitoType.getMessageUUID());
				idDocumentoFruitore = esitoType.getIdDocumentoFruitore();
				log.setIdDocumentoFruitore(idDocumentoFruitore);
				log.setTipoTrattamento(esitoType.getTipoTrattamento());
				if (esitoType.getEsitoTrattamento() != null) {
					ResultType esitoTrattamento = esitoType.getEsitoTrattamento();
					log.setEsitoTrattamento(esitoTrattamento.getCodice() + " " + esitoTrattamento.getMessaggio());
				}
				
				if (esitoType.getEsitiStep() != null && esitoType.getEsitiStep().getEsitoStep() != null) {
					List<EsitoStep> esitiStep = esitoType.getEsitiStep().getEsitoStep().stream().filter(e -> e.getNome().equalsIgnoreCase(Constants.PROTOCOLLAZIONE_ACTA)).collect(Collectors.toList());
					protocolloEsiste = esitiStep.size() > 0 ? true : false; // esiste il protocollo con esito positvo
					log.setEsitiStep(esitiStep.toString());
				}
				
				if (esitoType.getInformazioniAggiuntive() != null && esitoType.getInformazioniAggiuntive().getInformazione() != null) {
					List<InformazioneType> infoType = new ArrayList<InformazioneType>();
					for (InformazioneType info : esitoType.getInformazioniAggiuntive().getInformazione()) {
						if (info.getNome().toUpperCase().equalsIgnoreCase(Constants.NUMERO_REG_PROTOCOLLO)) {
							// prelevo i dati del protocollo
							protocollo = info.getValore();
						}
						if (info.getNome().toUpperCase().equalsIgnoreCase(Constants.DATA_REG_PROTOCOLLO)) {
							// prelevo i dati del protocollo
							dataprotocollo = info.getValore();
						}
						if (info.getNome().toUpperCase().equalsIgnoreCase(Constants.TIPO_REG_PROTOCOLLO)) {
							// prelevo i dati del protocollo
							tipoprotocollo = info.getValore();
						}
						infoType.add(info);
					}
					log.setInformazioniAggiuntive(infoType.toString());
				}
				// inserisci i valori
				logStardas.insertLogStardas(log);
			}

			if (!richiesteDao.selectIdDocumentoFruitore(idDocumentoFruitore)) {
				esitocallback.setCodice(Constants.KO_APPLICATIVI);
				esitocallback.setMessaggio(validateGeneric.getValueGenericErrorStardas(CodeErrorEnum.ERR17.getCode(),
						metodo + " " + "idDocumentoFruitore non trovato"));
				esitostep.setEsito(esitocallback);
				return Response.ok().entity(esitostep).build();
			}

			// se esiste il protocollo devo cambiare stato alla domanda ed inserirne il numero
			if (protocolloEsiste) {
	            if (tipoprotocollo.equalsIgnoreCase("PARTENZA")) {
	                if (idDocumentoFruitore.contains("LETTERA_DINIEGO"))
	                    richiesteDao.updateProtocollo(protocollo,  Util.toTimestamp(dataprotocollo), tipoprotocollo,idDocumentoFruitore, "LETTERA_DINIEGO");
	                else if (idDocumentoFruitore.contains("LETTERA_AMMISSIONE_FINANZIAMENTO"))
	                    richiesteDao.updateProtocollo(protocollo,  Util.toTimestamp(dataprotocollo), tipoprotocollo,idDocumentoFruitore, "LETTERA_AMMISSIONE_FINANZIAMENTO");
	                else if (idDocumentoFruitore.contains("LETTERA_AMMISSIONE_NON_FINANZIAMENTO"))
	                    richiesteDao.updateProtocollo(protocollo,  Util.toTimestamp(dataprotocollo), tipoprotocollo,idDocumentoFruitore, "LETTERA_AMMISSIONE_NON_FINANZIAMENTO");
	            } else {
	                richiesteDao.updateProtocollo(protocollo,  Util.toTimestamp(dataprotocollo), 
	                		tipoprotocollo,idDocumentoFruitore, "DOMANDA");
	            }
			}
			esitocallback.setCodice(Constants.OK);
			esitocallback.setMessaggio(validateGeneric.getValueGenericSuccess(Constants.OK_DESCRIZIONE));
			esitostep.setEsito(esitocallback);
		} catch (DatabaseException e) {
			esitostep = handleDatabaseException(metodo, e);
		} catch (Exception e) {
			esitostep = handleException(metodo, e);
		}
		return Response.ok().entity(esitostep).build();
	}
}
