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

public class EsitiStepType {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private List<EsitoStep> esitoStep = new ArrayList<EsitoStep>();

	@JsonProperty("esitoStep")
	@NotNull
	public List<EsitoStep> getEsitoStep() {
		return esitoStep;
	}

	public void setEsitoStep(List<EsitoStep> esitoStep) {
		this.esitoStep = esitoStep;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		EsitiStepType esitiStepType = (EsitiStepType) o;
		return Objects.equals(esitoStep, esitiStepType.esitoStep);
	}

	@Override
	public int hashCode() {
		return Objects.hash(esitoStep);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class EsitiStepType {\n");

		sb.append("    esitoStep: ").append(toIndentedString(esitoStep)).append("\n");
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
