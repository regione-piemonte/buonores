/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonorescallban.util.enumerator;

public enum CodeErrorEnum {
	ERR01("ERR01"),
	ERR02("ERR02"),
	ERR03("ERR03");
	
	private final String code;

	CodeErrorEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}


}
