/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.dto.custom;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelRichiestaLight {

	private String richiedenteCf = null;
	private BigDecimal domandaDetId = null;
	private String domandaDetCod = null;
	private BigDecimal sportelloId = null;

	@JsonProperty("richiedente_cf")
	public String getRichiedenteCf() {
		return richiedenteCf;
	}

	public void setRichiedenteCf(String richiedenteCf) {
		this.richiedenteCf = richiedenteCf;
	}

	@JsonProperty("domanda_det_id")
	public BigDecimal getDomandaDetId() {
		return domandaDetId;
	}

	public void setDomandaDetId(BigDecimal domandaDetId) {
		this.domandaDetId = domandaDetId;
	}

	@JsonProperty("sportello_id")
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
