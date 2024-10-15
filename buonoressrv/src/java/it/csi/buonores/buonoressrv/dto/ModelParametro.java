/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelParametro {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String codice = null;
	private String valore = null;

	/**
	 * codice utilizzato internamente
	 **/

	@JsonProperty("codice")
	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * etichetta esposta all&#39;utente
	 **/

	@JsonProperty("valore")
	public String getValore() {
		return valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelParametro modelDecodifica = (ModelParametro) o;
		return Objects.equals(codice, modelDecodifica.codice) && Objects.equals(valore, modelDecodifica.valore);
	}

	@Override
	public int hashCode() {
		return Objects.hash(codice, valore);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelParametro {\n");

		sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
		sb.append("    valore: ").append(toIndentedString(valore)).append("\n");
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
