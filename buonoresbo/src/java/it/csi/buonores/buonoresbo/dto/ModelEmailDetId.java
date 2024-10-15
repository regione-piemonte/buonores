/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

import java.math.BigDecimal;

public class ModelEmailDetId {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private BigDecimal domandaId = null;
	private String email = null;

	public BigDecimal getDomandaId() {
		return domandaId;
	}

	public void setDomandaId(BigDecimal domandaId) {
		this.domandaId = domandaId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "ModelEmailDetId [domandaId=" + domandaId + ", email=" + email + "]";
	}
}
