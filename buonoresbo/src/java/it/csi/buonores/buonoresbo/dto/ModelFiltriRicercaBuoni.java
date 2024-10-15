/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

public class ModelFiltriRicercaBuoni {
	private String destinatario;
	private String richiedente;
	private String numeroBuono;
	private String statoBuono;
	private String numeroDomanda;
	private String decessoResidenza;
	private String iseeConforme;
	private String iseeVerificaConforme;

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getRichiedente() {
		return richiedente;
	}

	public void setRichiedente(String richiedente) {
		this.richiedente = richiedente;
	}

	public String getNumeroBuono() {
		return numeroBuono;
	}

	public void setNumeroBuono(String numeroBuono) {
		this.numeroBuono = numeroBuono;
	}

	public String getStatoBuono() {
		return statoBuono;
	}

	public void setStatoBuono(String statoBuono) {
		this.statoBuono = statoBuono;
	}

	public String getNumeroDomanda() {
		return numeroDomanda;
	}

	public void setNumeroDomanda(String numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	public String getDecessoResidenza() {
		return decessoResidenza;
	}

	public void setDecessoResidenza(String decessoResidenza) {
		this.decessoResidenza = decessoResidenza;
	}

	public String getIseeConforme() {
		return iseeConforme;
	}

	public void setIseeConforme(String iseeConforme) {
		this.iseeConforme = iseeConforme;
	}

	public String getIseeVerificaConforme() {
		return iseeVerificaConforme;
	}

	public void setIseeVerificaConforme(String iseeVerificaConforme) {
		this.iseeVerificaConforme = iseeVerificaConforme;
	}

	@Override
	public String toString() {
		return "ModelFiltriRicercaBuoni [destinatario=" + destinatario + ", richiedente=" + richiedente
				+ ", numeroBuono=" + numeroBuono + ", statoBuono=" + statoBuono + ", numeroDomanda=" + numeroDomanda
				+ ", decessoResidenza=" + decessoResidenza
				+ ", iseeConforme=" + iseeConforme + ", iseeVerificaConforme=" + iseeVerificaConforme
				+ "]";
	}

}
