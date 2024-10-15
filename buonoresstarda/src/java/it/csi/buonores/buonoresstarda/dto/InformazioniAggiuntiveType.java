/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresstarda.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InformazioniAggiuntiveType {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private List<InformazioneType> informazione = new ArrayList<InformazioneType>();

	@JsonProperty("informazione")
	@NotNull
	public List<InformazioneType> getInformazione() {
		return informazione;
	}

	public void setInformazione(List<InformazioneType> informazione) {
		this.informazione = informazione;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		InformazioniAggiuntiveType informazioniAggiuntiveType = (InformazioniAggiuntiveType) o;
		return Objects.equals(informazione, informazioniAggiuntiveType.informazione);
	}

	@Override
	public int hashCode() {
		return Objects.hash(informazione);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class InformazioniAggiuntiveType {\n");

		sb.append("    informazione: ").append(toIndentedString(informazione)).append("\n");
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
