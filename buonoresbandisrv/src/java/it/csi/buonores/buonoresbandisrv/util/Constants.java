/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.util;

public class Constants {

	public final static String COMPONENT_NAME = "buonoresbandisrv";

	public static final String CONTEXT_TEMPO_PARTENZA = "tempopartenza";
	public static final String SHIB_IDENTITA_CODICE_FISCALE = "Shib-Identita-CodiceFiscale";
	public static final String X_REQUEST_ID = "X-Request-ID";
	public static final String X_CODICE_SERVIZIO = "X-Codice-Servizio";
	public static final String X_FORWARDED_FOR = "X-Forwarded-For";

	// PARAMETRI
	public static final String PARAMETRO_ERRORE_TIPO = "ERRORE";
	public static final String PARAMETRO_GENERICO = "GENERICO";
	public static final String BANDI = "BANDI";

	// VALUTAZIONE MULTIDIMENSIONALE
	public static final String PERSONA_PIU_65_ANNI = "UVG";
	public static final String PERSONA_DISABILE = "UMVD";

	// ISEE
	public static final Integer ATTESTAZIONE_ISEE_NON_DISABILE = 50000;
	public static final Integer ATTESTAZIONE_ISEE_DISABILE = 65000;

	// contratto
	public static final String CONTRATTO_RSA = "CONTRATTO_RSA";

	// path
	public static final String PATH_ARCHIVIAZIONE = "PATH_ARCHIVIAZIONE";

	// parametri bandi
	public static final String NORMA_INCENTIVAZIONE = "NORMA_INCENTIVAZIONE";
	public static final String CODICE_ASSE = "CODICE_ASSE";
	public static final String DESCRIZIONE_ASSE = "DESCRIZIONE_ASSE";
	public static final String CODICE_MISURA = "CODICE_MISURA";
	public static final String DESCRIZIONE_MISURA = "DESCRIZIONE_MISURA";
	public static final String DESCRIZIONE_BANDO = "DESCRIZIONE_BANDO";
	public static final String DESTINATARIO_DESCRIZIONE = "DESTINATARIO_DESCRIZIONE";
	public static final String DESTINATARIO_DIREZIONE = "DESTINATARIO_DIREZIONE";
	public static final String CODICE_INTERVENTO = "CODICE_INTERVENTO";
	public static final String DESCRIZIONE_INTERVENTO = "DESCRIZIONE_INTERVENTO";
	public static final String MODALITA_REGISTRAZIONE = "MODALITA_REGISTRAZIONE";
	public static final String COD_TIPO_UTENTE = "COD_TIPO_UTENTE";
	public static final String TIPO_UTENTE = "TIPO_UTENTE";
	public static final String TIPOLOGIA_ENTE = "TIPOLOGIA_ENTE";
	public static final String RUOLO_BENEFICIARIO = "RUOLO_BENEFICIARIO";
	public static final String CLASSIFICAZIONE_ENTE = "CLASSIFICAZIONE_ENTE";
	public static final String TIPO_PERSONAFISICA = "TIPO_PERSONAFISICA";
	public static final String CODICE_TIPOLOGIA_SPESA = "CODICE_TIPOLOGIA_SPESA";
	public static final String DESCR_TIPOLOGIA_SPESA = "DESCR_TIPOLOGIA_SPESA";
	public static final String FLAG_IVA_COSTO = "FLAG_IVA_COSTO";
	public static final String QUALIFICAFORNITORE = "QUALIFICAFORNITORE";
	public static final String WS = "WS";
	public static final String STATO = "ITALIA";
	public static final String COD_STATO = "000";
	public static final String NOME_FILE = "NOME_FILE";

	public static final String BANDI_OK = "OK";
	public static final String BANDI_KO = "KO";

	// documenti di spesa
	public static final String CREATO = "CREATO";
	public static final String ATTIVO = "ATTIVO";
	public static final String RINUNCIATO = "RINUNCIATO";
	public static final String REVOCATO = "REVOCATO";
	public static final String REVOCATO_PRESA_VISIONE = "REVOCATO_PRESA_VISIONE";

	public static final String INVIO_DOMANDA = "INVIO_DOMANDA";
	public static final String SET_FORNITORE = "SET_FORNITORE";

	public static final String DA_INVIARE = "DA_INVIARE";

	// motivo stati buoni
	public static final String DEC = "DEC"; // decesso
	public static final String CR = "CR"; // cambio residenza
	public static final String RIN = "RIN"; // rinuncia beneficiario
	public static final String PAM = "PAM"; // presenza altre misure
	public static final String ISEE = "ISEE"; // modifica isee
	public static final String DTCONC = "DTCONC"; // modifica data concessione
}
