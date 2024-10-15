/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresrend.util;

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

	public static Date getDataWithoutTime(String data, String dateFormat) {
		if (!Util.isValorizzato(data))
			return null;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date dataDate = sdf.parse(data);

			return dataDate;
		} catch (ParseException pe) {
			return null;
		}
	}
}
