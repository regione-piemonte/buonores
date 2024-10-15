/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.util.enumerator;

public enum CodeErrorEnum {
	ERR01("ERR01"),
	ERR02("ERR02"),
	ERR03("ERR03"),
	ERR04("ERR04"),
	ERR05("ERR05"),
	ERR06("ERR06"),
	ERR07("ERR07"),
	ERR08("ERR08"),
	ERR17("ERR17");

	private final String code;

	CodeErrorEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
