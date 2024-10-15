/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbatch.util;

public class Checker {
	
	/**
	 * Controllo di valorizzazione su di una stringa.
	 * Se null o vuoto ritorna false.
	 * @param stringa
	 * @return
	 */
	public static boolean isValorizzato(String stringa) {
		if (stringa == null)
			return false;

		if (stringa.trim().length() == 0)
			return false;

		return true;
	}
}