/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.dto;

import java.math.BigDecimal;
import java.util.Date;

public class DatiBuono {

	private BigDecimal buonoId = null;
	private Date decorrenzaInizio = null;

	public BigDecimal getBuonoId() {
		return buonoId;
	}

	public void setBuonoId(BigDecimal buonoId) {
		this.buonoId = buonoId;
	}

	public Date getDecorrenzaInizio() {
		return decorrenzaInizio;
	}

	public void setDecorrenzaInizio(Date decorrenzaInizio) {
		this.decorrenzaInizio = decorrenzaInizio;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DatiBuono [buonoId=");
		builder.append(buonoId);
		builder.append(", decorrenzaInizio=");
		builder.append(decorrenzaInizio);
		builder.append("]");
		return builder.toString();
	}

}
