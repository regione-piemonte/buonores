/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.dto.custom;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelComuneExt {
	private String strResComune;
	private String strResProvincia;

	public ModelComuneExt() {
	}

	public ModelComuneExt(String strResComune, String strResProvincia) {
		super();
		this.strResComune = strResComune;
		this.strResProvincia = strResProvincia;
	}

	@JsonProperty("strResComune")
	public String getStrResComune() {
		return strResComune;
	}

	public void setStrResComune(String strResComune) {
		this.strResComune = strResComune;
	}

	@JsonProperty("strResProvincia")
	public String getStrResProvincia() {
		return strResProvincia;
	}

	public void setStrResProvincia(String strResProvincia) {
		this.strResProvincia = strResProvincia;
	}
}
