/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelDecodificaRapportoAllegato {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String allegatoTipoCod = null;
	private Boolean allegatoObbligatorio = null;

	/**
	 * indica l&#39;allegato richiesto
	 **/
	@JsonProperty("allegato_tipo_cod")
	public String getAllegatoTipoCod() {
		return allegatoTipoCod;
	}

	public void setAllegatoTipoCod(String allegatoTipoCod) {
		this.allegatoTipoCod = allegatoTipoCod;
	}

	/**
	 * indica se l&#39;allegato è obbligatorio o opzionale
	 **/
	@JsonProperty("allegato_obbligatorio")
	public Boolean isAllegatoObbligatorio() {
		return allegatoObbligatorio;
	}

	public void setAllegatoObbligatorio(Boolean allegatoObbligatorio) {
		this.allegatoObbligatorio = allegatoObbligatorio;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelDecodificaRapportoAllegato modelDecodificaRapportoAllegato = (ModelDecodificaRapportoAllegato) o;
		return Objects.equals(allegatoTipoCod, modelDecodificaRapportoAllegato.allegatoTipoCod)
				&& Objects.equals(allegatoObbligatorio, modelDecodificaRapportoAllegato.allegatoObbligatorio);
	}

	@Override
	public int hashCode() {
		return Objects.hash(allegatoTipoCod, allegatoObbligatorio);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelDecodificaRapportoAllegato {\n");

		sb.append("    allegatoTipoCod: ").append(toIndentedString(allegatoTipoCod)).append("\n");
		sb.append("    allegatoObbligatorio: ").append(toIndentedString(allegatoObbligatorio)).append("\n");
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
