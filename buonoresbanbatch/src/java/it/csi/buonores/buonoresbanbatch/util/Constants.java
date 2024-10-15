/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbanbatch.util;

public class Constants {

	public final static String COMPONENT_NAME = "buonoresbanbatch";

	public static final String SHIB_IDENTITA_CODICE_FISCALE = "Shib-Identita-CodiceFiscale";
	public static final String SHIB_IRIDE_IDENTITADIGITALE = "Shib-Iride-IdentitaDigitale";
	public static final String X_REQUEST_ID = "X-Request-ID";
	public static final String X_CODICE_SERVIZIO = "X-Codice-Servizio";
	public static final String X_FORWARDED_FOR = "X-Forwarded-For";
	public static final String XREQUESTID = "buonoresbanbatch";
	public static final String XCODICESERVIZIO = "BUONORESBANBATCH";
	public static final String XFORWARDEDFOR = "127.127.127.127";
	// CODICI STATO DOMANDA
	public static final String AMMESSA = "AMMESSA";
	public static final String IN_PAGAMENTO = "IN_PAGAMENTO";
	public static final String PERFEZIONATA_IN_PAGAMENTO = "PERFEZIONATA_IN_PAGAMENTO";

	public static final String LETTERA_AMMISSIONE_FINANZIAMENTO = "LETTERA_AMMISSIONE_FINANZIAMENTO";
	public static final String LETTERA_AMMISSIONE_NON_FINANZIAMENTO = "LETTERA_AMMISSIONE_NON_FINANZIAMENTO";
	// giorni batch

	public static final String BO_AMMESSA_FINANZIATA = "BO_AMMESSA_FINANZIATA";
	public static final String BO_AMMESSA_NON_FINANZIATA = "BO_AMMESSA_NON_FINANZIATA";
	public static final String BO_PERFEZIONATA_IN_PAGAMENTO = "BO_PERFEZIONATA_IN_PAGAMENTO";

	public static final String CREATO = "CREATO";
	
    public static final String INVIO_DOMANDE_BANDI = "INVIO_DOMANDE_BANDI";
    public static final String BATCH = "BATCH";
    
    public static final String NOTIFICA_AMMESSA_FINANZIATA = "NOTIFICA_AMMESSA_FINANZIATA";
	public static final String NOTIFICA_AMMESSA_NON_FINANZIATA = "NOTIFICA_AMMESSA_NON_FINANZIATA";
	public static final String INVIO_CONTRATTI_BANDI="INVIO_CONTRATTI_BANDI";
	
}
