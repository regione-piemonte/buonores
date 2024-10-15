/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.exception;

import it.csi.buonores.buonoresbandisrv.dto.Errore;

public class ResponseErrorException extends Exception {

	private static final long serialVersionUID = 2134345368882934388L;

	private final transient Errore responseError;

	public ResponseErrorException(Errore errore, Throwable message) {
		super(message);
		this.responseError = errore;
	}

	public ResponseErrorException(Errore errore, String message) {
		super(message);
		this.responseError = errore;
	}

	@SuppressWarnings("unused")
	private ResponseErrorException() {
		this.responseError = null;
	}

	public Errore getResponseError() {
		return responseError;
	}
}
