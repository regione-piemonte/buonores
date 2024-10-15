/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

import java.util.Date;
import java.util.List;

public class ModelDocumentoSpesa {

	private long idDocSpesa;
	private String doc_tipo;
	private String doc_numero;
	private Date periodoInizio;
	private Date periodoFine;
	private String rsaCod;
	private String rsaCodArpe;
	private String rsaPIva;
	private String rsaNome;
	private String rsaIBAN;
	private String fascia;
	private boolean flagPrimoIngresso;
	private Date movUscitaData;
	private String movUscitaMotivazione;
	private String note;
	private List<ModelDocumentoSpesaDettaglio> documentiSpesaDettaglio;
	private String dataIstruttoria;
	private String noteIstruttoria;

	public long getIdDocSpesa() {
		return idDocSpesa;
	}

	public void setIdDocSpesa(long idDocSpesa) {
		this.idDocSpesa = idDocSpesa;
	}

	public String getDoc_tipo() {
		return doc_tipo;
	}

	public void setDoc_tipo(String doc_tipo) {
		this.doc_tipo = doc_tipo;
	}

	public String getDoc_numero() {
		return doc_numero;
	}

	public void setDoc_numero(String doc_numero) {
		this.doc_numero = doc_numero;
	}

	public Date getPeriodoInizio() {
		return periodoInizio;
	}

	public void setPeriodoInizio(Date periodoInizio) {
		this.periodoInizio = periodoInizio;
	}

	public Date getPeriodoFine() {
		return periodoFine;
	}

	public void setPeriodoFine(Date periodoFine) {
		this.periodoFine = periodoFine;
	}

	public List<ModelDocumentoSpesaDettaglio> getDocumentiSpesaDettaglio() {
		return documentiSpesaDettaglio;
	}

	public void setDocumentiSpesaDettaglio(List<ModelDocumentoSpesaDettaglio> documentiSpesaDettaglio) {
		this.documentiSpesaDettaglio = documentiSpesaDettaglio;
	}

	public String getRsaCod() {
		return rsaCod;
	}

	public void setRsaCod(String rsaCod) {
		this.rsaCod = rsaCod;
	}

	public String getRsaCodArpe() {
		return rsaCodArpe;
	}

	public void setRsaCodArpe(String rsaCodArpe) {
		this.rsaCodArpe = rsaCodArpe;
	}

	public String getRsaPIva() {
		return rsaPIva;
	}

	public void setRsaPIva(String rsaPIva) {
		this.rsaPIva = rsaPIva;
	}

	public String getRsaNome() {
		return rsaNome;
	}

	public void setRsaNome(String rsaNome) {
		this.rsaNome = rsaNome;
	}

	public String getRsaIBAN() {
		return rsaIBAN;
	}

	public void setRsaIBAN(String rsaIBAN) {
		this.rsaIBAN = rsaIBAN;
	}

	public String getFascia() {
		return fascia;
	}

	public void setFascia(String fascia) {
		this.fascia = fascia;
	}

	public boolean isFlagPrimoIngresso() {
		return flagPrimoIngresso;
	}

	public void setFlagPrimoIngresso(boolean flagPrimoIngresso) {
		this.flagPrimoIngresso = flagPrimoIngresso;
	}

	public Date getMovUscitaData() {
		return movUscitaData;
	}

	public void setMovUscitaData(Date movUscitaData) {
		this.movUscitaData = movUscitaData;
	}

	public String getMovUscitaMotivazione() {
		return movUscitaMotivazione;
	}

	public void setMovUscitaMotivazione(String movUscitaMotivazione) {
		this.movUscitaMotivazione = movUscitaMotivazione;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getDataIstruttoria() {
		return dataIstruttoria;
	}

	public void setDataIstruttoria(String dataIstruttoria) {
		this.dataIstruttoria = dataIstruttoria;
	}

	public String getNoteIstruttoria() {
		return noteIstruttoria;
	}

	public void setNoteIstruttoria(String noteIstruttoria) {
		this.noteIstruttoria = noteIstruttoria;
	}

	@Override
	public String toString() {
		return "ModelDocumentoSpesa [idDocSpesa=" + idDocSpesa + ", doc_tipo=" + doc_tipo + ", doc_numero=" + doc_numero
				+ ", periodoInizio=" + periodoInizio + ", periodoFine=" + periodoFine + ", rsaCod=" + rsaCod
				+ ", rsaCodArpe=" + rsaCodArpe + ", rsaPIva=" + rsaPIva + ", rsaNome=" + rsaNome + ", rsaIBAN="
				+ rsaIBAN + ", fascia=" + fascia + ", flagPrimoIngresso=" + flagPrimoIngresso + ", movUscitaData="
				+ movUscitaData + ", movUscitaMotivazione=" + movUscitaMotivazione + ", note=" + note
				+ ", documentiSpesaDettaglio=" + documentiSpesaDettaglio + ", dataIstruttoria=" + dataIstruttoria
				+ ", noteIstruttoria=" + noteIstruttoria + "]";
	}

}
