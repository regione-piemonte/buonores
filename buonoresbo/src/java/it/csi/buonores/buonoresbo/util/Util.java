/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

	public static boolean isValorizzato(String stringa) {
		if (stringa == null || stringa.equalsIgnoreCase("null"))
			return false;

		if (stringa.trim().length() == 0)
			return false;

		return true;
	}

	public static boolean isNormalizedString(String stringa) {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9'_À-ÖØ-öø-ÿ ]+$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(stringa);
		boolean matchFound = matcher.find();
		if (matchFound) {
			return true;
		}

		return false;
	}

	public static boolean isNormalizedDate(String stringa) {
		Pattern pattern = Pattern.compile("^([0-2]?[0-9]|3[0-1])\\/(0?[1-9]|1[0-2])\\/\\d{4}$",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(stringa);
		boolean matchFound = matcher.find();
		if (matchFound) {
			return true;
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
			// throw new ApplicationException("Errore di conversione da stringa a data.");
			return null;
		}
	}

	public static String getDataISO(Date data) {
		if (data == null)
			return "";

		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		String dataString = sdf.format(data);
		return dataString;
	}

	public static Date aggiungiAnnoAData(Date dataScadenzaParametrizzata, int numAnni) {
		Calendar c = Calendar.getInstance();
		c.setTime(dataScadenzaParametrizzata);
		c.add(Calendar.YEAR, numAnni);

		return c.getTime();
	}

	public static Date aggiungiGiorniAData(Date dataScadenzaParametrizzata, int numGiorni) {
		Calendar c = Calendar.getInstance();
		c.setTime(dataScadenzaParametrizzata);
		c.add(Calendar.DAY_OF_YEAR, numGiorni);

		return c.getTime();
	}

	public static Integer getYear(Date date) {
		if (date == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}

	public static String getMonthDescription(Date date) {
		if (date == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return new SimpleDateFormat(Constants.MOUTH_FORMAT).format(c.getTime());
	}

	public static Date getDataWrapperWithoutTime(String data) {
		if (!Util.isValorizzato(data))
			return null;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_WRAPPER);

			Date dataDate = sdf.parse(data);

			return dataDate;
		} catch (ParseException pe) {
			// throw new ApplicationException("Errore di conversione da stringa a data.");
			return null;
		}
	}

	public static String getDateAndTime() {
		Long currentTime = System.currentTimeMillis();
		Date now = new Date(currentTime);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy_hh:mm");
		return dateFormat.format(now);
	}

}
