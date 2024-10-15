/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbanbatch.exception;

public class BandiConnectionException extends Exception {
	
	private static final long serialVersionUID = -7946929978410044787L;

	public BandiConnectionException(Exception e){
		super(e);
	}
	
	public BandiConnectionException(String s){
		super(s);
	}
	
	public BandiConnectionException(){
		super();
	}
}
