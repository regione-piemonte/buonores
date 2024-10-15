/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.util.enumerator;

public enum CodeErrorEnum {
	ERR01("ERR01"),
	ERR02("ERR02"),
	ERR03("ERR03"),
	ERR04("ERR04"),
	ERR05("ERR05"),
	ERR06("ERR06"),
	ERR07("ERR07"),
	ERR08("ERR08"),
	ERR09("ERR09"),
	ERR10("ERR10"),
	ERR11("ERR11"),
	ERR12("ERR12"),
	ERR13("ERR13"),
	ERR14("ERR14"),
	ERR15("ERR15"),
	ERR16("ERR16"),
	ERR18("ERR18"),
	ERR19("ERR19"),
	ERR20("ERR20");

	private final String code;

	CodeErrorEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
