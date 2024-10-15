/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.dto;

import java.math.BigDecimal;

public class ModelContrattoFornitore {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private BigDecimal contrattoId = null;
	private BigDecimal fornitoreId = null;
	private String stato = null;

	public BigDecimal getContrattoId() {
		return contrattoId;
	}

	public void setContrattoId(BigDecimal contrattoId) {
		this.contrattoId = contrattoId;
	}

	public BigDecimal getFornitoreId() {
		return fornitoreId;
	}

	public void setFornitoreId(BigDecimal fornitoreId) {
		this.fornitoreId = fornitoreId;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

}
