/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

public class ModelNuovaGraduatoria {
	private String codSportello;
	private String descrizione;
	private double importoGenerale;
	private double importoA1;
	private double importoA2;
	private double importoMensile;
	private int numeroMesi;

	public String getCodSportello() {
		return codSportello;
	}

	public void setCodSportello(String codSportello) {
		this.codSportello = codSportello;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public double getImportoGenerale() {
		return importoGenerale;
	}

	public void setImportoGenerale(double importoGenerale) {
		this.importoGenerale = importoGenerale;
	}

	public double getImportoA1() {
		return importoA1;
	}

	public void setImportoA1(double importoA1) {
		this.importoA1 = importoA1;
	}

	public double getImportoA2() {
		return importoA2;
	}

	public void setImportoA2(double importoA2) {
		this.importoA2 = importoA2;
	}

	public double getImportoMensile() {
		return importoMensile;
	}

	public void setImportoMensile(double importoMensile) {
		this.importoMensile = importoMensile;
	}

	public int getNumeroMesi() {
		return numeroMesi;
	}

	public void setNumeroMesi(int numeroMesi) {
		this.numeroMesi = numeroMesi;
	}

	@Override
	public String toString() {
		return "ModelNuovaGraduatoria [codSportello=" + codSportello + ", descrizione=" + descrizione
				+ ", importoGenerale=" + importoGenerale + ", importoA1=" + importoA1 + ", importoA2=" + importoA2
				+ ", importoMensile=" + importoMensile + ", numeroMesi=" + numeroMesi + "]";
	}
}