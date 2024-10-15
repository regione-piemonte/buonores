/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.util.enumerator;

public enum IseeStatoEnum {
	CONFORME("CONFORME"),
	NON_CONFORME("NON CONFORME"),
	RESPINTO("RESPINTO");

	private final String stato;

	IseeStatoEnum(String stato) {
		this.stato = stato;
	}

	public String getStato() {
		return stato;
	}
}
