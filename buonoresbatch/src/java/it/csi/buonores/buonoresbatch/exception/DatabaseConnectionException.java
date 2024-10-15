/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbatch.exception;

public class DatabaseConnectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7468897775001370683L;

	public DatabaseConnectionException(String s) {
		super(s);
	}

	public DatabaseConnectionException() {
		super();
	}

	public DatabaseConnectionException(String s, Throwable t) {
		super(s, t);
	}
}
