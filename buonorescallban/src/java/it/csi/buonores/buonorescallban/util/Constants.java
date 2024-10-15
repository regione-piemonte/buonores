/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonorescallban.util;

public class Constants {

	public final static String COMPONENT_NAME = "buonorescallban";

	public static final String CONTEXT_TEMPO_PARTENZA = "tempopartenza";
	public static final String SHIB_IDENTITA_CODICE_FISCALE = "Shib-Identita-CodiceFiscale";
	public static final String X_CODICE_SERVIZIO = "X-Codice-Servizio";
	public static final String X_REQUEST_ID = "X-Request-ID";
	public static final String X_FORWARDED_FOR = "X-Forwarded-For";

	// PARAMETRI
	public static final String PARAMETRO_ERRORE_TIPO = "ERRORE";
	public static final String PARAMETRO_MESSAGGIO_TIPO = "MESSAGGIO";

	// stardas esito postivo
	public static final String OK = "OK";
	public static final String KO = "KO";
	
	//stati buono
	public static final String CREATO = "CREATO";
	public static final String ATTIVO = "ATTIVO";
	public static final String RINUNCIATO = "RINUNCIATO";
	public static final String REVOCATO = "REVOCATO";
	
	public static final String REVOCA_BUONO ="REVOCA_BUONO";
}
