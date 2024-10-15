/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelIseeBuono {
	private String iseeVerificaEsitoCod;
	private Boolean iseeConforme;
	private Long iseeId;

	@JsonProperty("isee_id")
	public Long getIseeId() {
		return iseeId;
	}

	public void setIseeId(Long iseeId) {
		this.iseeId = iseeId;
	}

	@JsonProperty("isee_verifica_esito_cod")
	public String getIseeVerificaEsitoCod() {
		return iseeVerificaEsitoCod;
	}

	public void setIseeVerificaEsitoCod(String iseeVerificaEsitoCod) {
		this.iseeVerificaEsitoCod = iseeVerificaEsitoCod;
	}

	@JsonProperty("isee_conforme")
	public Boolean isIseeConforme() {
		return iseeConforme;
	}

	public void setIseeConforme(Boolean iseeConforme) {
		this.iseeConforme = iseeConforme;
	}

	@Override
	public int hashCode() {
		return Objects.hash(iseeVerificaEsitoCod, iseeConforme, iseeId);
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
		return Objects.equals(iseeId, other.iseeId) && Objects.equals(iseeVerificaEsitoCod, other.iseeVerificaEsitoCod)
				&& Objects.equals(iseeConforme, other.iseeConforme);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ModelIsee [iseeId=");
		builder.append(iseeId);
		builder.append(", iseeVerificaEsitoCod=");
		builder.append(iseeVerificaEsitoCod);
		builder.append(", iseeConforme=");
		builder.append(iseeConforme);
		builder.append("]");
		return builder.toString();
	}

}
