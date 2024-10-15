/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelDecodificaRapporto {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String codice = null;
	private String etichetta = null;
	private List<ModelDecodificaRapportoAllegato> allegato = new ArrayList<ModelDecodificaRapportoAllegato>();

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
	@JsonProperty("etichetta")
	public String getEtichetta() {
		return etichetta;
	}

	public void setEtichetta(String etichetta) {
		this.etichetta = etichetta;
	}

	@JsonProperty("allegato")
	public List<ModelDecodificaRapportoAllegato> getAllegato() {
		return allegato;
	}

	public void setAllegato(List<ModelDecodificaRapportoAllegato> allegato) {
		this.allegato = allegato;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelDecodificaRapporto modelDecodificaRapporto = (ModelDecodificaRapporto) o;
		return Objects.equals(codice, modelDecodificaRapporto.codice)
				&& Objects.equals(etichetta, modelDecodificaRapporto.etichetta)
				&& Objects.equals(allegato, modelDecodificaRapporto.allegato);
	}

	@Override
	public int hashCode() {
		return Objects.hash(codice, etichetta, allegato);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelDecodificaRapporto {\n");

		sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
		sb.append("    etichetta: ").append(toIndentedString(etichetta)).append("\n");
		sb.append("    allegato: ").append(toIndentedString(allegato)).append("\n");
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
