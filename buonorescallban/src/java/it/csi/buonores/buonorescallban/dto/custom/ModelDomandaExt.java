/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonorescallban.dto.custom;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.csi.buonores.buonorescallban.dto.ModelDomanda;

public class ModelDomandaExt extends ModelDomanda {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String numeroDomanda = null;

	/**
	 * numero della domanda
	 **/

	@JsonProperty("numero_domanda")
	public String getNumeroDomanda() {
		return numeroDomanda;
	}

	public void setNumeroDomanda(String numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelDomandaExt modelDomanda = (ModelDomandaExt) o;
		return Objects.equals(numeroDomanda, modelDomanda.numeroDomanda);
	}

	@Override
	public int hashCode() {
		return Objects.hash(numeroDomanda);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelDomanda {\n");

		sb.append("    numeroDomanda: ").append(toIndentedString(numeroDomanda)).append("\n");
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
