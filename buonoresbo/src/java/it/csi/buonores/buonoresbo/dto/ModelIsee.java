/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelIsee {
	private BigDecimal iseeValore = null;
	private Date iseeDataRilascio = null;
	private Date iseeScadenza = null;
	private Boolean iseeVerificatoConforme;
	private Date iseeVerificatoInData = null;

	@JsonProperty("isee_valore")
	public BigDecimal getIseeValore() {
		return iseeValore;
	}

	public void setIseeValore(BigDecimal iseeValore) {
		this.iseeValore = iseeValore;
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

	@JsonProperty("isee_verificato_conforme")
	public Boolean isIseeVerificatoConforme() {
		return iseeVerificatoConforme;
	}

	public void setIseeVerificatoConforme(Boolean iseeVerificatoConforme) {
		this.iseeVerificatoConforme = iseeVerificatoConforme;
	}

	@JsonProperty("isee_verificato_in_data")
	public Date getIseeVerificatoInData() {
		return iseeVerificatoInData;
	}

	public void setIseeVerificatoInData(Date iseeVerificatoInData) {
		this.iseeVerificatoInData = iseeVerificatoInData;
	}

	@Override
	public int hashCode() {
		return Objects.hash(iseeDataRilascio, iseeScadenza, iseeValore, iseeVerificatoConforme, iseeVerificatoInData);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelIsee other = (ModelIsee) obj;
		return Objects.equals(iseeDataRilascio, other.iseeDataRilascio)
				&& Objects.equals(iseeScadenza, other.iseeScadenza) && Objects.equals(iseeValore, other.iseeValore)
				&& Objects.equals(iseeVerificatoConforme, other.iseeVerificatoConforme)
				&& Objects.equals(iseeVerificatoInData, other.iseeVerificatoInData);
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
		builder.append(", iseeVerificatoConforme=");
		builder.append(iseeVerificatoConforme);
		builder.append(", iseeVerificatoInData=");
		builder.append(iseeVerificatoInData);
		builder.append("]");
		return builder.toString();
	}
}
