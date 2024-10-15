/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.util.enumerator;

public enum ErrorParamEnum {

	X_REQUEST_ID("x-Request-Id"),
	SHIB_IDENTITA_CODICEFISCALE("Shib-Identita-CodiceFiscale"),
	X_FORWARDED_FOR("x-Forwarded-for"),
	X_CODICE_SERVIZIO("x-Codice-Servizio"),
	X_CODICE_VERTICALE("x-Codice-Verticale"),
	TIPONOTIFICA("TipoNotifica"),
	NUMERODOMANDA("NumeroDomanda"),
	TIPOLETTERA("TipoLettera"),
	CODNOTIFICA("Codice notifica"),
	DESCRNOTIFICA("Descrizione notifica"),
	DATANOTIFICA("Data notifica");

	private final String code;

	ErrorParamEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
