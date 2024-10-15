/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelIseeBuono {
	private BigDecimal iseeValore = null;
	private Date iseeDataRilascio = null;
	private Date iseeScadenza = null;
	private String iseeVerificaEsitoCod;
	private Date iseeVerificatoInData = null;
	private String iseeNote = null;
	private String iseeAnno = null;
	private Boolean iseeConforme;
	private Long buonoId;
	private Long iseeId;
	private String validitaInizio = null;
	private String utenteCreazione = null;
	private String utenteModifica = null;
	private Date validitaFine = null;

	@JsonProperty("isee_valore")
	public BigDecimal getIseeValore() {
		return iseeValore;
	}

	public void setIseeValore(BigDecimal iseeValore) {
		this.iseeValore = iseeValore;
	}

	@JsonProperty("isee_id")
	public Long getIseeId() {
		return iseeId;
	}

	public void setIseeId(Long iseeId) {
		this.iseeId = iseeId;
	}

	@JsonProperty("isee_data_rilascio")
	public Date getIseeDataRilascio() {
		return iseeDataRilascio;
	}

	public void setIseeDataRilascio(Date iseeDataRilascio) {
		this.iseeDataRilascio = iseeDataRilascio;
	}

	@JsonProperty("isee_scadenza")
	public Date getIseeScadenza() {
		return iseeScadenza;
	}

	public void setIseeScadenza(Date iseeScadenza) {
		this.iseeScadenza = iseeScadenza;
	}

	@JsonProperty("isee_verifica_esito_cod")

	public String getIseeVerificaEsitoCod() {
		return iseeVerificaEsitoCod;
	}

	public void setIseeVerificaEsitoCod(String iseeVerificaEsitoCod) {
		this.iseeVerificaEsitoCod = iseeVerificaEsitoCod;
	}

	@JsonProperty("isee_verificato_in_data")
	public Date getIseeVerificatoInData() {
		return iseeVerificatoInData;
	}

	public void setIseeVerificatoInData(Date iseeVerificatoInData) {
		this.iseeVerificatoInData = iseeVerificatoInData;
	}

	@JsonProperty("isee_note")
	public String getIseeNote() {
		return iseeNote;
	}

	public void setIseeNote(String iseeNote) {
		this.iseeNote = iseeNote;
	}

	@JsonProperty("isee_anno")
	public String getIseeAnno() {
		return iseeAnno;
	}

	public void setIseeAnno(String iseeAnno) {
		this.iseeAnno = iseeAnno;
	}

	@JsonProperty("isee_conforme")
	public Boolean isIseeConforme() {
		return iseeConforme;
	}

	public void setIseeConforme(Boolean iseeConforme) {
		this.iseeConforme = iseeConforme;
	}

	@JsonProperty("buono_id")
	public Long getBuonoId() {
		return buonoId;
	}

	public void setBuonoId(Long buonoId) {
		this.buonoId = buonoId;
	}

	@JsonProperty("validita_inizio")
	public String getValiditaInizio() {
		return validitaInizio;
	}

	public void setValiditaInizio(String validitaInizio) {
		this.validitaInizio = validitaInizio;
	}

	@JsonProperty("validita_fine")
	public Date getValiditaFine() {
		return validitaFine;
	}

	public void setValiditaFine(Date validitaFine) {
		this.validitaFine = validitaFine;
	}

	@JsonProperty("utente_creazione")
	public String getUtenteCreazione() {
		return utenteCreazione;
	}

	public void setUtenteCreazione(String utenteCreazione) {
		this.utenteCreazione = utenteCreazione;
	}

	@JsonProperty("utente_modifica")
	public String getUtenteModifica() {
		return utenteModifica;
	}

	public void setUtenteModifica(String utenteModifica) {
		this.utenteModifica = utenteModifica;
	}

	@Override
	public int hashCode() {
		return Objects.hash(iseeDataRilascio, iseeScadenza, iseeValore, iseeVerificaEsitoCod, iseeVerificatoInData,
				iseeNote, iseeAnno, iseeConforme, buonoId, validitaInizio, utenteCreazione, utenteModifica,
				validitaFine, iseeId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelIseeBuono other = (ModelIseeBuono) obj;
		return Objects.equals(iseeDataRilascio, other.iseeDataRilascio)
				&& Objects.equals(iseeScadenza, other.iseeScadenza) && Objects.equals(iseeValore, other.iseeValore)
				&& Objects.equals(iseeVerificaEsitoCod, other.iseeVerificaEsitoCod)
				&& Objects.equals(iseeVerificatoInData, other.iseeVerificatoInData)
				&& Objects.equals(iseeNote, other.iseeNote) && Objects.equals(iseeConforme, other.iseeConforme)
				&& Objects.equals(iseeAnno, other.iseeAnno) && Objects.equals(buonoId, other.buonoId)
				&& Objects.equals(validitaInizio, other.validitaInizio)
				&& Objects.equals(validitaFine, other.validitaFine) && Objects.equals(iseeId, other.iseeId)
				&& Objects.equals(utenteCreazione, other.utenteCreazione)
				&& Objects.equals(utenteModifica, other.utenteModifica);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ModelIsee [iseeValore=");
		builder.append(iseeValore);
		builder.append(", iseeDataRilascio=");
		builder.append(iseeDataRilascio);
		builder.append(", iseeScadenza=");
		builder.append(iseeScadenza);
		builder.append(", iseeVerificaEsitoCod=");
		builder.append(iseeVerificaEsitoCod);
		builder.append(", iseeVerificatoInData=");
		builder.append(iseeVerificatoInData);
		builder.append(", iseeAnno=");
		builder.append(iseeAnno);
		builder.append(", iseeConforme=");
		builder.append(iseeConforme);
		builder.append(", iseeNote=");
		builder.append(iseeNote);
		builder.append(", buonoId=");
		builder.append(buonoId);
		builder.append(", validitaInizio=");
		builder.append(validitaInizio);
		builder.append(", validitaFine=");
		builder.append(validitaFine);
		builder.append(", utenteCreazione=");
		builder.append(utenteCreazione);
		builder.append(", utenteModifica=");
		builder.append(utenteModifica);
		builder.append(", iseeId=");
		builder.append(iseeId);
		builder.append("]");
		return builder.toString();
	}

}
