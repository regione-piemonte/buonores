/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonorescallban.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PayloadEsitoAcquisizione {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private ModelEsitoAcquisizione esitoAcquisizione = null;

	@JsonProperty("esito_acquisizione")

	public ModelEsitoAcquisizione getEsitoAcquisizione() {
		return esitoAcquisizione;
	}

	public void setEsitoAcquisizione(ModelEsitoAcquisizione esitoAcquisizione) {
		this.esitoAcquisizione = esitoAcquisizione;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PayloadEsitoAcquisizione payloadEsitoAcquisizione = (PayloadEsitoAcquisizione) o;
		return Objects.equals(esitoAcquisizione, payloadEsitoAcquisizione.esitoAcquisizione);
	}

	@Override
	public int hashCode() {
		return Objects.hash(esitoAcquisizione);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class PayloadEsitoAcquisizione {\n");

		sb.append("    esitoAcquisizione: ").append(toIndentedString(esitoAcquisizione)).append("\n");
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
