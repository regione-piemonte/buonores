/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbanbatch.configuration;

import java.io.FileInputStream;
import java.io.IOException;
//import java.io.InputStream;
import java.util.Properties;

public class Configuration {

    private static Configuration instance;
    private Properties properties;
//    private InputStream inputStream;

    private Configuration() throws IOException {
        properties = new Properties();
	
        // Da scommentare per i test in locale
//        inputStream = getClass().getClassLoader().getResourceAsStream("configuration.properties");
//		properties.load(inputStream);
			
        //FINE 	
        
        // Da scommentare per PROD-TST-DEV
		String propFile = System.getProperty("PROP_FILE");
		properties.load(new FileInputStream(propFile));
		//FINE */		
    }

    private static Configuration getInstance() {
        if (instance == null) {
            try {
                instance = new Configuration();
            } catch (IOException e) {
                System.err.println("Error in loading configuration");
            }
        }
        return instance;
    }

    private String valueOf(String key) {
        return properties.getProperty(key);
    }

    public static String get(String key) {
        return getInstance().valueOf(key);
    }

}
