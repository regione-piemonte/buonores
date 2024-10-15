/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresstarda.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EsitoSmistaDocumentoRequest {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private EsitoSmistaDocumento esitoSmistaDocumento = null;

	@JsonProperty("esitoSmistaDocumento")
	public EsitoSmistaDocumento getEsitoSmistaDocumento() {
		return esitoSmistaDocumento;
	}

	public void setEsitoSmistaDocumento(EsitoSmistaDocumento esitoSmistaDocumento) {
		this.esitoSmistaDocumento = esitoSmistaDocumento;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		EsitoSmistaDocumentoRequest esitoSmistaDocumentoRequest = (EsitoSmistaDocumentoRequest) o;
		return Objects.equals(esitoSmistaDocumento, esitoSmistaDocumentoRequest.esitoSmistaDocumento);
	}

	@Override
	public int hashCode() {
		return Objects.hash(esitoSmistaDocumento);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class EsitoSmistaDocumentoRequest {\n");

		sb.append("    esitoSmistaDocumento: ").append(toIndentedString(esitoSmistaDocumento)).append("\n");
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
