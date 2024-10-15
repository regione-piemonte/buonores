/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.util.enumerator;

public enum StatusEnum {

	BAD_REQUEST(400, "VALORI ERRATI", "VALORI ERRATI TITLE"),
	NOT_FOUND(404, "DATI NON TROVATI", "DATI NON TROVATI TITLE"),
	SERVER_ERROR(500, "SERVER ERROR", "SERVER ERROR TITLE");

	private final Integer status;
	private final String code;
	private final String title;

	StatusEnum(Integer status, String code, String title) {
		this.status = status;
		this.code = code;
		this.title = title;

	}

	public String getCode() {
		return code;
	}

	public String getTitle() {
		return title;
	}

	public Integer getStatus() {
		return status;
	}
}
