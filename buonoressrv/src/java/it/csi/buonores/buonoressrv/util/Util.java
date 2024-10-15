/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

	public static boolean isValorizzato(String stringa) {
		if (stringa == null || stringa.equalsIgnoreCase("null"))
			return false;

		if (stringa.trim().length() == 0)
			return false;

		return true;
	}

	public static boolean isAttestazioneIsee(BigDecimal isee, String disabile) {

		if (isee != null && disabile != null && !disabile.isEmpty()) {
			if (disabile.equals(Constants.PERSONA_DISABILE)) {
				return isee.intValue() > Constants.ATTESTAZIONE_ISEE_DISABILE;
			} else {
				return isee.intValue() > Constants.ATTESTAZIONE_ISEE_NON_DISABILE;
			}

		}

		return false;
	}
	
	public static Date getDataWithoutTime(String data) {
		if (!Util.isValorizzato(data))
			return null;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
			Date dataDate = sdf.parse(data);

			return dataDate;
		} catch (ParseException pe) {
			return null;
		}
	}

	public static String getDataISO(Date data) {
		if (data == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_ITALIAN);
		String dataString = sdf.format(data);
		return dataString;
	}
	
	public static String getDataNomeMese(Date data) {
		if (data == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_NOME_MESE);
		String dataString = sdf.format(data);
		return dataString;
	}
	
    public static String getAnno(Date data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(data);
    }
}
