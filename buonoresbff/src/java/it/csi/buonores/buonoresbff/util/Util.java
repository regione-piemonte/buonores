/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class Util {

	public static boolean isValorizzato(String stringa) {
		if (stringa == null || stringa.equalsIgnoreCase("null"))
			return false;

		if (stringa.trim().length() == 0)
			return false;

		return true;
	}

	public static boolean isInt(String string) {
		if (!isValorizzato(string)) {
			return false;
		}

		try {
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	public static Date getData(String data, String dateFormatPattern) {
		if (!Util.isValorizzato(data))
			return null;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormatPattern);
			Date dataDate = sdf.parse(data);

			return dataDate;
		} catch (ParseException pe) {
			return null;
		}
	}

	public static boolean isAgeBetween18and65(Date data) {
		LocalDate dataNascita = data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int eta = Period.between(dataNascita, LocalDate.now()).getYears();

		if (eta > 18 && eta < 65) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isAgeMaggiorenne(Date data) {
		LocalDate dataNascita = data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int eta = Period.between(dataNascita, LocalDate.now()).getYears();

		if (eta > 18) {
			return true;
		} else {
			return false;
		}

	}

	public static String getData(Date data) {
		if (data == null)
			return "";

		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_PATTERN);
		String dataString = sdf.format(data);
		return dataString;
	}

	public static Date aggiungiGiorniAData(Date dataScadenzaParametrizzata, int numGiorni) {
		Calendar c = Calendar.getInstance();
		c.setTime(dataScadenzaParametrizzata);
		c.add(Calendar.DAY_OF_YEAR, numGiorni);

		return c.getTime();
	}

	public static boolean isSameDay(Date in) {
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT_ITALIAN_PATTERN);
		return formatter.format(now).equals(formatter.format(in));
	}
}
