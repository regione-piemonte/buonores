/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

import java.util.Date;

public class ModelVisualizzaCronologia {

	private String numeroDomanda;
	private String statoDomanda;
	private Date dataInizioValidita;
	private String note;
	private String notaInterna;
	private String utente;
	private boolean isRichiedente;
	private String notaEnte;

	public String getNumeroDomanda() {
		return numeroDomanda;
	}

	public void setNumeroDomanda(String numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	public String getStatoDomanda() {
		return statoDomanda;
	}

	public void setStatoDomanda(String statoDomanda) {
		this.statoDomanda = statoDomanda;
	}

	public Date getDataInizioValidita() {
		return dataInizioValidita;
	}

	public void setDataInizioValidita(Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNotaInterna() {
		return notaInterna;
	}

	public void setNotaInterna(String notaInterna) {
		this.notaInterna = notaInterna;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public boolean isRichiedente() {
		return isRichiedente;
	}

	public void setRichiedente(boolean isRichiedente) {
		this.isRichiedente = isRichiedente;
	}

	public String getNotaEnte() {
		return notaEnte;
	}

	public void setNotaEnte(String notaEnte) {
		this.notaEnte = notaEnte;
	}

	@Override
	public String toString() {
		return "ModelVisualizzaCronologia [numeroDomanda=" + numeroDomanda + ", statoDomanda=" + statoDomanda
				+ ", dataInizioValidita=" + dataInizioValidita + ", note=" + note + ", notaInterna=" + notaInterna
				+ ", utente=" + utente + ", isRichiedente=" + isRichiedente + ", notaEnte=" + notaEnte + "]";
	}

}
