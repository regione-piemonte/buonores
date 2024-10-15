/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbatch.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Converter {

    public static boolean isValorizzato(String stringa) {
        if (stringa == null || stringa.trim().length() == 0)
            return false;
        return true;
    }

    /**
     * Converte una stringa in un intero.
     * Se la stringa non e' valorizzata restituisce 0
     *
     * @param stringa
     * @return
     */
    public static int getInt(String stringa) {
        if (isValorizzato(stringa))
            return Integer.parseInt(stringa);
        else
            return 0;
    }
    
    /**
     * Converte una data in formato Date in una data in formato string in base al pattern e al locale forniti
     *
     * @param data
     * @param pattern ad esempio "dd/MM/yyyy"
     * @param locale
     * @return
     */
    public static String getDataString(Date data, String pattern) {
        if (data == null) return "";

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String dataString = sdf.format(data);
        return dataString;
    }

    /**
     * Converte una data in formato string in una data in formato Date.
     * Controlla: - se il parametro in ingresso � valorizzato
     * - se la stringa rispetta il formato "dd/MM/yyyy"
     * - se il giorno e il mese rispettano la realt�(Es: mese non maggiore di 12, giorni di febbraio nn superiori a 28)
     *
     * @param data data
     * @return data nel formato Date
     */
    public static Date getData(String data) {
        if (!Checker.isValorizzato(data)) return null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_ITALIAN);
            sdf.setLenient(false);
            Date dataDate = sdf.parse(data);

            return dataDate;
        } catch (ParseException pe) {
            return null;
        }
    }
}