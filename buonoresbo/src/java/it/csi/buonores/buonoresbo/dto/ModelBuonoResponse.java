/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

public class ModelBuonoResponse {
	private String buonoCod;
	private String buonoStatoCod;
	private String numeroDomanda;
	private String richiedenteCf;
	private String destinatarioCf;
	private String nomeRichiedente;
	private String cognomeRichiedente;
	private String nomeDestinatario;
	private String cognomeDestinatario;
	private String decorrenzaInizio;
	private String decorrenzaFine;
	private String dataDecesso;
	private String dataCambioResidenza;
	private String iseeConforme;
	private String iseeVerificaEsitoCod;

	public String getBuonoCod() {
		return buonoCod;
	}

	public void setBuonoCod(String buonoCod) {
		this.buonoCod = buonoCod;
	}

	public String getBuonoStatoCod() {
		return buonoStatoCod;
	}

	public void setBuonoStatoCod(String buonoStatoCod) {
		this.buonoStatoCod = buonoStatoCod;
	}

	public String getNumeroDomanda() {
		return numeroDomanda;
	}

	public void setNumeroDomanda(String numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	public String getRichiedenteCf() {
		return richiedenteCf;
	}

	public void setRichiedenteCf(String richiedenteCf) {
		this.richiedenteCf = richiedenteCf;
	}

	public String getDestinatarioCf() {
		return destinatarioCf;
	}

	public void setDestinatarioCf(String destinatarioCf) {
		this.destinatarioCf = destinatarioCf;
	}

	public String getNomeRichiedente() {
		return nomeRichiedente;
	}

	public void setNomeRichiedente(String nomeRichiedente) {
		this.nomeRichiedente = nomeRichiedente;
	}

	public String getCognomeRichiedente() {
		return cognomeRichiedente;
	}

	public void setCognomeRichiedente(String cognomeRichiedente) {
		this.cognomeRichiedente = cognomeRichiedente;
	}

	public String getNomeDestinatario() {
		return nomeDestinatario;
	}

	public void setNomeDestinatario(String nomeDestinatario) {
		this.nomeDestinatario = nomeDestinatario;
	}

	public String getCognomeDestinatario() {
		return cognomeDestinatario;
	}

	public void setCognomeDestinatario(String cognomeDestinatario) {
		this.cognomeDestinatario = cognomeDestinatario;
	}

	public String getDecorrenzaInizio() {
		return decorrenzaInizio;
	}

	public void setDecorrenzaInizio(String decorrenzaInizio) {
		this.decorrenzaInizio = decorrenzaInizio;
	}

	public String getDecorrenzaFine() {
		return decorrenzaFine;
	}

	public void setDecorrenzaFine(String decorrenzaFine) {
		this.decorrenzaFine = decorrenzaFine;
	}

	public String getDataDecesso() {
		return dataDecesso;
	}

	public void setDataDecesso(String dataDecesso) {
		this.dataDecesso = dataDecesso;
	}

	public String getDataCambioResidenza() {
		return dataCambioResidenza;
	}

	public void setDataCambioResidenza(String dataCambioResidenza) {
		this.dataCambioResidenza = dataCambioResidenza;
	}

	public String getIseeConforme() {
		return iseeConforme;
	}

	public void setIseeConforme(String iseeConforme) {
		this.iseeConforme = iseeConforme;
	}

	public String getIseeVerificaEsitoCod() {
		return iseeVerificaEsitoCod;
	}

	public void setIseeVerificaEsitoCod(String iseeVerificaEsitoCod) {
		this.iseeVerificaEsitoCod = iseeVerificaEsitoCod;
	}

	@Override
	public String toString() {
		return "ModelBuonoResponse [buonoCod=" + buonoCod + ", buonoStatoCod=" + buonoStatoCod + ", numeroDomanda="
				+ numeroDomanda + ", richiedenteCf=" + richiedenteCf + ", destinatarioCf=" + destinatarioCf
				+ ", nomeRichiedente=" + nomeRichiedente + ", cognomeRichiedente=" + cognomeRichiedente
				+ ", nomeDestinatario=" + nomeDestinatario + ", cognomeDestinatario=" + cognomeDestinatario
				+ ", decorrenzaInizio=" + decorrenzaInizio + ", decorrenzaFine=" + decorrenzaFine + ", dataDecesso="
				+ dataDecesso + ", dataCambioResidenza=" + dataCambioResidenza
				+ ", iseeConforme=" + iseeConforme + ", iseeVerificaEsitoCod=" + iseeVerificaEsitoCod
				+ "]";
	}

}
