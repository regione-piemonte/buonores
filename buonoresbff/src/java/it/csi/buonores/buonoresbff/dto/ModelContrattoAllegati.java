/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelContrattoAllegati {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private ModelContratto contratto = null;
	private List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();

	@JsonProperty("contratto")
	public ModelContratto getContratto() {
		return contratto;
	}

	public void setContratto(ModelContratto contratto) {
		this.contratto = contratto;
	}

	@JsonProperty("allegati")
	public List<ModelAllegato> getAllegati() {
		return allegati;
	}

	public void setAllegati(List<ModelAllegato> allegati) {
		this.allegati = allegati;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelContrattoAllegati modelContrattoAllegati = (ModelContrattoAllegati) o;
		return Objects.equals(contratto, modelContrattoAllegati.contratto)
				&& Objects.equals(allegati, modelContrattoAllegati.allegati);
	}

	@Override
	public int hashCode() {
		return Objects.hash(contratto, allegati);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelContrattoAllegati {\n");

		sb.append("    contratto: ").append(toIndentedString(contratto)).append("\n");
		sb.append("    allegati: ").append(toIndentedString(allegati)).append("\n");
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
