/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresstarda.util.enumerator;

public enum CodeErrorEnum {
	ERR01("ERR01"),
	ERR03("ERR03"),
	ERRSTARD01("ERRSTARD01"),
	ERR16("ERR16"),
	ERR17("ERR17");
	
	private final String code;

	CodeErrorEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}


}
