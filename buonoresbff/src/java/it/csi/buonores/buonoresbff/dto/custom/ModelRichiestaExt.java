/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.dto.custom;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.csi.buonores.buonoresbff.dto.ModelRichiesta;

public class ModelRichiestaExt extends ModelRichiesta {

	private String domandaDetCod = null;
	private BigDecimal domandaDetId = null;
	private BigDecimal sportelloId = null;

	public BigDecimal getDomandaDetId() {
		return domandaDetId;
	}

	public void setDomandaDetId(BigDecimal domandaDetId) {
		this.domandaDetId = domandaDetId;
	}

	public BigDecimal getSportelloId() {
		return sportelloId;
	}

	public void setSportelloId(BigDecimal sportelloId) {
		this.sportelloId = sportelloId;
	}

	@JsonProperty("domanda_det_cod")

	public String getDomandaDetCod() {
		return domandaDetCod;
	}

	public void setDomandaDetCod(String domandaDetCod) {
		this.domandaDetCod = domandaDetCod;
	}
}
