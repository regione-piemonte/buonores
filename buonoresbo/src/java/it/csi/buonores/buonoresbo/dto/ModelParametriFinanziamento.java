/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

import java.math.BigDecimal;

public class ModelParametriFinanziamento {
	private BigDecimal importoMensile;
	private byte mesi;
	private int soggettiFinanziati;
	private BigDecimal importoTotale;
	private BigDecimal importoResiduo;
	private BigDecimal importoDistribuibile;
	private int soggettiFinanziabili;
	private String aree;

	public BigDecimal getImportoMensile() {
		return importoMensile;
	}

	public void setImportoMensile(BigDecimal importoMensile) {
		this.importoMensile = importoMensile;
	}

	public byte getMesi() {
		return mesi;
	}

	public void setMesi(byte mesi) {
		this.mesi = mesi;
	}

	public int getSoggettiFinanziati() {
		return soggettiFinanziati;
	}

	public void setSoggettiFinanziati(int soggettiFinanziati) {
		this.soggettiFinanziati = soggettiFinanziati;
	}

	public BigDecimal getImportoTotale() {
		return importoTotale;
	}

	public void setImportoTotale(BigDecimal importoTotale) {
		this.importoTotale = importoTotale;
	}

	public int getSoggettiFinanziabili() {
		return soggettiFinanziabili;
	}

	public void setSoggettiFinanziabili(int soggettiFinanziabili) {
		this.soggettiFinanziabili = soggettiFinanziabili;
	}

	public String getAree() {
		return aree;
	}

	public void setAree(String aree) {
		this.aree = aree;
	}

	public BigDecimal getImportoResiduo() {
		return importoResiduo;
	}

	public void setImportoResiduo(BigDecimal importoResiduo) {
		this.importoResiduo = importoResiduo;
	}

	public BigDecimal getImportoDistribuibile() {
		return importoDistribuibile;
	}

	public void setImportoDistribuibile(BigDecimal importoDistribuibile) {
		this.importoDistribuibile = importoDistribuibile;
	}

	@Override
	public String toString() {
		return "ModelParametriFinanziamento [importoMensile=" + importoMensile + ", mesi=" + mesi
				+ ", soggettiFinanziati=" + soggettiFinanziati + ", importoTotale=" + importoTotale
				+ ", importoResiduo=" + importoResiduo + ", importoDistribuibile=" + importoDistribuibile
				+ ", soggettiFinanziabili=" + soggettiFinanziabili + ", aree=" + aree + "]";
	}
}
