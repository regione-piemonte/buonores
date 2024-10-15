/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresstarda.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EsitoStep {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String nome = null;
	private ResultType esito = null;

	@JsonProperty("nome")
	@NotNull
	@Size(min = 1, max = 200)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@JsonProperty("esito")
	@NotNull
	public ResultType getEsito() {
		return esito;
	}

	public void setEsito(ResultType esito) {
		this.esito = esito;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		EsitoStep esitoStep = (EsitoStep) o;
		return Objects.equals(nome, esitoStep.nome) && Objects.equals(esito, esitoStep.esito);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nome, esito);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class EsitoStep {\n");

		sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
		sb.append("    esito: ").append(toIndentedString(esito)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
