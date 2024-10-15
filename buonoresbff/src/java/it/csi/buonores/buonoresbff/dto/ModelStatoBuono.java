/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelStatoBuono {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String stato = null;
	private Date decorrenza = null;

	/**
	 * codice stato del buono
	 **/
	@JsonProperty("stato")
	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	/**
	 * data di decorrenza (per lo stato RINUNCIATO)
	 **/
	@JsonProperty("decorrenza")
	public Date getDecorrenza() {
		return decorrenza;
	}

	public void setDecorrenza(Date decorrenza) {
		this.decorrenza = decorrenza;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelStatoBuono modelStatoBuono = (ModelStatoBuono) o;
		return Objects.equals(stato, modelStatoBuono.stato) && Objects.equals(decorrenza, modelStatoBuono.decorrenza);
	}

	@Override
	public int hashCode() {
		return Objects.hash(stato, decorrenza);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelStatoBuono {\n");

		sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
		sb.append("    decorrenza: ").append(toIndentedString(decorrenza)).append("\n");
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
