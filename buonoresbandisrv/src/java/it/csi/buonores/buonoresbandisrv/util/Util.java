/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.util;

import java.sql.Timestamp;
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

	public static String getData(Timestamp data) {
		if (data == null)
			return "";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dataString = sdf.format(data);
		return dataString;
	}

	public static Date getDataWithoutTime(String data) {
		if (!Util.isValorizzato(data))
			return null;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			Date dataDate = sdf.parse(data);

			return dataDate;
		} catch (ParseException pe) {
			// throw new ApplicationException("Errore di conversione da stringa a data.");
			return null;
		}
	}

	public static String getDataISO(Date data) {
		if (data == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String dataString = sdf.format(data);
		return dataString;
	}

	public static String getDataOra(Timestamp data) {
		if (data == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		String dataString = sdf.format(data);
		return dataString;
	}
}
