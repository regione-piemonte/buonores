/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.util.enumerator;

public enum ApiHeaderParamEnum {

	SHIB_IRIDE_IDENTITADIGITALE("Shib-Iride-IdentitaDigitale"),
	SHIB_IDENTITA_CODICEFISCALE("Shib-Identita-CodiceFiscale"),
	X_AUTHENTICATION("x-authentication"),
	AUTHORIZATION("Authorization"),
	CONTENT_TYPE("Content-Type"),
	X_FORWARDED_FOR("X-Forwarded-For"),
	X_REQUEST_ID("X-Request-Id"),
	X_APPLICAZIONE_CODICE("X-Applicazione-Codice");

	private final String code;

	private ApiHeaderParamEnum(String inCode) {
		this.code = inCode;
	}

	public String getCode() {
		return code;
	}

}
