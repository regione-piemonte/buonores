/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbanbatch.exception;

public class DatabaseConnectionException extends Exception {
	
	private static final long serialVersionUID = -7946929978410044787L;

	public DatabaseConnectionException(String s){
		super(s);
	}
	
	public DatabaseConnectionException(){
		super();
	}
}
