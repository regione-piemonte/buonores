/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonorescallban.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelDomanda {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String cfBeneficiario = null;
	private String cfRichiedente = null;
	private String dataDecorrenzaRevoca = null;
	private String nota = null;

	/**
	 * codice fiscale del beneficiario
	 **/
	@JsonProperty("cf_beneficiario")
	public String getCfBeneficiario() {
		return cfBeneficiario;
	}

	public void setCfBeneficiario(String cfBeneficiario) {
		this.cfBeneficiario = cfBeneficiario;
	}

	/**
	 * codice fiscale del richiedente
	 **/
	@JsonProperty("cf_richiedente")
	public String getCfRichiedente() {
		return cfRichiedente;
	}

	public void setCfRichiedente(String cfRichiedente) {
		this.cfRichiedente = cfRichiedente;
	}

	@JsonProperty("data_decorrenza_revoca")
	public String getDataDecorrenzaRevoca() {
		return dataDecorrenzaRevoca;
	}

	public void setDataDecorrenzaRevoca(String dataDecorrenzaRevoca) {
		this.dataDecorrenzaRevoca = dataDecorrenzaRevoca;
	}

	@JsonProperty("nota")
	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cfBeneficiario, cfRichiedente, dataDecorrenzaRevoca, nota);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelDomanda other = (ModelDomanda) obj;
		return Objects.equals(cfBeneficiario, other.cfBeneficiario)
				&& Objects.equals(cfRichiedente, other.cfRichiedente)
				&& Objects.equals(dataDecorrenzaRevoca, other.dataDecorrenzaRevoca) && Objects.equals(nota, other.nota);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ModelDomanda [cfBeneficiario=");
		builder.append(cfBeneficiario);
		builder.append(", cfRichiedente=");
		builder.append(cfRichiedente);
		builder.append(", dataDecorrenzaRevoca=");
		builder.append(dataDecorrenzaRevoca);
		builder.append(", nota=");
		builder.append(nota);
		builder.append("]");
		return builder.toString();
	}
}
