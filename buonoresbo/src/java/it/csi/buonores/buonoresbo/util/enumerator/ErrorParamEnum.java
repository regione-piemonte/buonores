/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.util.enumerator;

public enum ErrorParamEnum {

	X_REQUEST_ID("x-Request-Id"),
	SHIB_IDENTITA_CODICEFISCALE("Shib-Identita-CodiceFiscale"),
	X_FORWARDED_FOR("x-Forwarded-for"),
	X_CODICE_SERVIZIO("x-Codice-Servizio"),
	X_CODICE_VERTICALE("x-Codice-Verticale"),
	VALORE_ISEE("Valore Isee"),
	ISEE("Isee"),
	SCADENZA_ISEE("Data scadenza Isee"),
	RILASCIO_ISEE("Data rilascio"),
	VERIFICA_CONFORME("Conformita'"),
	VERIFICA_NOTE("Nota ente gestore"),
	STATO("Stato"),
	CONTATTI("Contatti"),
	NOTE_ISEE("Note"),
	VERIFICA_CONFORME_BUONO("Esito Verifica"),
	DECESSO_RESIDENZA("Decesso Residenza"),
	DESTINATARIO("Destinatario"),
	ISEE_CONFORME("Isee Conforme"),
	ISEE_VERIFICA_CONFORME("Isee Verifica Conforme"),
	NUMERO_BUONO("Numero Buono"),
	NUMERO_DOMANDA("Numero Domanda"),
	RICHIEDENTE("Richiedente"),
	STATO_BUONO("Stato Buono"),
	SPORTELLO("Sportello"),
	STATO_DOMANDA("Stato Domanda"),
	MENU("Menu"),
	STATO_VERIFICA_ENTE_GESTORE("Stato Verifica Ente Gestore"),
	VERIFICA_ENTE_GESTORE("Verifica Ente Gestore"),
	ESITO_VERIFICA_ENTE_GESTORE("Esito Verifica Ente Gestore"),
	DATA_VERIFICA_DA("Data Verifica Da"),
	DATA_VERIFICA_A("Data Verifica A"),
	ULTIMA_DATA_VERIFICA("Ultima Data Verifica");

	private final String code;

	ErrorParamEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
