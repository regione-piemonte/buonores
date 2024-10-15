/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresstarda.dto.custom;

import java.sql.Timestamp;

public class StardasLog {

	private long idlog;
	private Timestamp dataEsecuzione;
	private String messageUUID;
	private String idDocumentoFruitore;
	private String tipoTrattamento;
	private String esitiStep;
	private String informazioniAggiuntive;
	private String esitoTrattamento;

	public long getIdlog() {
		return idlog;
	}

	public void setIdlog(long idlog) {
		this.idlog = idlog;
	}

	public Timestamp getDataEsecuzione() {
		return dataEsecuzione;
	}

	public void setDataEsecuzione(Timestamp dataEsecuzione) {
		this.dataEsecuzione = dataEsecuzione;
	}

	public String getMessageUUID() {
		return messageUUID;
	}

	public void setMessageUUID(String messageUUID) {
		this.messageUUID = messageUUID;
	}

	public String getIdDocumentoFruitore() {
		return idDocumentoFruitore;
	}

	public void setIdDocumentoFruitore(String idDocumentoFruitore) {
		this.idDocumentoFruitore = idDocumentoFruitore;
	}

	public String getTipoTrattamento() {
		return tipoTrattamento;
	}

	public void setTipoTrattamento(String tipoTrattamento) {
		this.tipoTrattamento = tipoTrattamento;
	}

	public String getEsitiStep() {
		return esitiStep;
	}

	public void setEsitiStep(String esitiStep) {
		this.esitiStep = esitiStep;
	}

	public String getInformazioniAggiuntive() {
		return informazioniAggiuntive;
	}

	public void setInformazioniAggiuntive(String informazioniAggiuntive) {
		this.informazioniAggiuntive = informazioniAggiuntive;
	}

	public String getEsitoTrattamento() {
		return esitoTrattamento;
	}

	public void setEsitoTrattamento(String esitoTrattamento) {
		this.esitoTrattamento = esitoTrattamento;
	}

}