/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

public class ModelSportello {
	String codSportello;
	String descSportello;
	private String dataInizio;
	private String dataFine;
	private String anno;
	boolean corrente;

	public String getCodSportello() {
		return codSportello;
	}

	public void setCodSportello(String codSportello) {
		this.codSportello = codSportello;
	}

	public String getDescSportello() {
		return descSportello;
	}

	public void setDescSportello(String descSportello) {
		this.descSportello = descSportello;
	}

	public boolean isCorrente() {
		return corrente;
	}

	public void setCorrente(boolean corrente) {
		this.corrente = corrente;
	}

	public String getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(String dataInizio) {
		this.dataInizio = dataInizio;
	}

	public String getDataFine() {
		return dataFine;
	}

	public void setDataFine(String dataFine) {
		this.dataFine = dataFine;
	}

	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	@Override
	public String toString() {
		return "ModelSportello [codSportello=" + codSportello + ", descSportello=" + descSportello + ", dataInizio="
				+ dataInizio + ", dataFine=" + dataFine + ", anno=" + anno + ", corrente=" + corrente + "]";
	}
}
