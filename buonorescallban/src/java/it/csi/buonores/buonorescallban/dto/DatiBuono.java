/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonorescallban.dto;

import java.math.BigDecimal;

public class DatiBuono {

	private BigDecimal buonoId = null;
	private String stato = null;

	public BigDecimal getBuonoId() {
		return buonoId;
	}

	public void setBuonoId(BigDecimal buonoId) {
		this.buonoId = buonoId;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DatiBuono [buonoId=");
		builder.append(buonoId);
		builder.append(", stato=");
		builder.append(stato);
		builder.append("]");
		return builder.toString();
	}

}
