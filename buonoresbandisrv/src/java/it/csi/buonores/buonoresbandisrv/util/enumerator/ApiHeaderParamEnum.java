/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.util.enumerator;

public enum ApiHeaderParamEnum {

	SHIB_IRIDE_IDENTITADIGITALE("Shib-Iride-IdentitaDigitale"),
	X_AUTHENTICATION("x-authentication"),
	AUTHORIZATION("Authorization"),
	CONTENT_TYPE("Content-Type"),
	X_FORWARDED_FOR("X-Forwarded-For"),
	X_REQUEST_ID("X-Request-Id");

	private final String code;

	private ApiHeaderParamEnum(String inCode) {
		this.code = inCode;
	}

	public String getCode() {
		return code;
	}

}
