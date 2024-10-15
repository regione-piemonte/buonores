/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

import java.util.Date;

public class ModelVerificheDomanda {

	private String numeroDomanda;
	private Date dataInizioValidita;
	private String misure;
	private String tipo;
	private String fonte;
	private String note;
	private String noteRichiesta;
	private String dataRichiesta;

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNumeroDomanda() {
		return numeroDomanda;
	}

	public void setNumeroDomanda(String numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	public Date getDataInizioValidita() {
		return dataInizioValidita;
	}

	public void setDataInizioValidita(Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	public String getMisure() {
		return misure;
	}

	public void setMisure(String misure) {
		this.misure = misure;
	}

	public String getFonte() {
		return fonte;
	}

	public void setFonte(String fonte) {
		this.fonte = fonte;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNoteRichiesta() {
		return noteRichiesta;
	}

	public void setNoteRichiesta(String noteRichiesta) {
		this.noteRichiesta = noteRichiesta;
	}

	public String getDataRichiesta() {
		return dataRichiesta;
	}

	public void setDataRichiesta(String dataRichiesta) {
		this.dataRichiesta = dataRichiesta;
	}

	@Override
	public String toString() {
		return "ModelVerificheDomanda [numeroDomanda="
				+ numeroDomanda
				+ ", dataInizioValidita="
				+ dataInizioValidita
				+ ", misure="
				+ misure
				+ ", tipo="
				+ tipo
				+ ", fonte="
				+ fonte
				+ ", note="
				+ note
				+ ", noteRichiesta="
				+ noteRichiesta
				+ ", dataRichiesta="
				+ dataRichiesta
				+ "]";
	}
}
