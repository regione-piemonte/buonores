/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.util.enumerator;

public enum ListaAssistitiStatusEnum {
	ABILITATI_SUCCESSO("ABILITATO"),
	ABILITATI_PRECEDENTEMENTE("GIA_ABILITATO"),
	ABILITATI_NON_ABILITABILI("NON_ABILITABILE"),
	DISABILITATI_SUCCESSO("DISABILITATO"),
	DISABILITATI_PRECEDENTEMENTE("GIA_DISABILITATO"),
	DISABILITATI_NON_DISABILITABILI("NON_DISABILITABILE_SOGGETTO_MANCANTE");

	private final String code;

	private ListaAssistitiStatusEnum(String inCode) {
		this.code = inCode;
	}

	public String getCode() {
		return code;
	}

}
