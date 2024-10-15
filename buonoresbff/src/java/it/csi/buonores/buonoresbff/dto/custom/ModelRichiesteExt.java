/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.dto.custom;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.csi.buonores.buonoresbff.dto.ModelRichiesta;

public class ModelRichiesteExt extends ModelRichiesta {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private Boolean esistebuono = null;

	@JsonProperty("esistebuono")
	public Boolean getEsistebuono() {
		return esistebuono;
	}

	public void setEsistebuono(Boolean esistebuono) {
		this.esistebuono = esistebuono;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ModelRichiesteExt [esistebuono=");
		builder.append(esistebuono);
		builder.append("]");
		return builder.toString();
	}

}
