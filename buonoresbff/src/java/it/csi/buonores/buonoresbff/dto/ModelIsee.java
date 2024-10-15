/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.dto;

import java.util.Objects;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelIsee {
	private String anno = null;

	/**
	 * anno di riferimento dell&#39;ISEE
	 **/

	@JsonProperty("anno")

	@Pattern(regexp = "^\\d{4}$")
	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelIsee modelIsee = (ModelIsee) o;
		return Objects.equals(anno, modelIsee.anno);
	}

	@Override
	public int hashCode() {
		return Objects.hash(anno);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelIsee {\n");

		sb.append("    anno: ").append(toIndentedString(anno)).append("\n");
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
