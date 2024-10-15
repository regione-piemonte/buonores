/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.dto.custom;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelStrutturaCoordExt {
	private String codStrutturaArpe;
	private String coordSrid;
	private String coordX;
	private String coordY;

	@JsonProperty("codStrutturaArpe")
	public String getCodStrutturaArpe() {
		return codStrutturaArpe;
	}

	public void setCodStrutturaArpe(String codStrutturaArpe) {
		this.codStrutturaArpe = codStrutturaArpe;
	}

	@JsonProperty("coordSrid")
	public String getCoordSrid() {
		return coordSrid;
	}

	public void setCoordSrid(String coordSrid) {
		this.coordSrid = coordSrid;
	}

	@JsonProperty("coordX")
	public String getCoordX() {
		return coordX;
	}

	public void setCoordX(String coordX) {
		this.coordX = coordX;
	}

	@JsonProperty("coordY")
	public String getCoordY() {
		return coordY;
	}

	public void setCoordY(String coordY) {
		this.coordY = coordY;
	}
}
