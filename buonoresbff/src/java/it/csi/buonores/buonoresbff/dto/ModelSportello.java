/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelSportello {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private Date dataInizio = null;
	private Date dataFine = null;
	private Boolean inCorso = null;

	@JsonProperty("data_inizio")
	public Date getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	@JsonProperty("data_fine")
	public Date getDataFine() {
		return dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	/**
	 * indica se lo sportello è quello corrente
	 **/

	@JsonProperty("in_corso")
	public Boolean isInCorso() {
		return inCorso;
	}

	public void setInCorso(Boolean inCorso) {
		this.inCorso = inCorso;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelSportello modelSportello = (ModelSportello) o;
		return Objects.equals(dataInizio, modelSportello.dataInizio)
				&& Objects.equals(dataFine, modelSportello.dataFine) && Objects.equals(inCorso, modelSportello.inCorso);
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataInizio, dataFine, inCorso);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelSportello {\n");

		sb.append("    dataInizio: ").append(toIndentedString(dataInizio)).append("\n");
		sb.append("    dataFine: ").append(toIndentedString(dataFine)).append("\n");
		sb.append("    inCorso: ").append(toIndentedString(inCorso)).append("\n");
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
