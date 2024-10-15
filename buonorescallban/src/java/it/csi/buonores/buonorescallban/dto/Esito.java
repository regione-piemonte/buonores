/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonorescallban.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Esito {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String codice = null;
	private String messaggio = null;

	@JsonProperty("codice")

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	@JsonProperty("messaggio")

	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Esito esito = (Esito) o;
		return Objects.equals(codice, esito.codice) && Objects.equals(messaggio, esito.messaggio);
	}

	@Override
	public int hashCode() {
		return Objects.hash(codice, messaggio);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Esito {\n");

		sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
		sb.append("    messaggio: ").append(toIndentedString(messaggio)).append("\n");
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
