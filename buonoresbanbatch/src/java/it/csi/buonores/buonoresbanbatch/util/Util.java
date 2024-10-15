/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbanbatch.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Util {

    public static boolean isValorizzato(long num) {
		if (num == 0)
			return false;
		return true;
	}
    
    public static java.sql.Date getDate(java.util.Date date){
    	java.util.Date utilDate = date;
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        return sqlDate;
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
    
    public static String getDateAndTime() {
	    	Long currentTime = System.currentTimeMillis();
	    	Date now = new Date(currentTime);
	    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy_hh:mm");
	    	return dateFormat.format(now);
    }
}
