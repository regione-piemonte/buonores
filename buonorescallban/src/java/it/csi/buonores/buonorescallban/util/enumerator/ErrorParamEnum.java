/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonorescallban.util.enumerator;

public enum ErrorParamEnum {
	
	MESSAGGE_UUID("Messaggio UUID"),
	X_REQUEST_ID("x-Request-Id"),
	CODICE_ERRORE("Codice Errore"),
	ERRORE("Errore"),
	MESSAGGIO_ERRORE("Messaggio Errore"),
	ESITO_OK_KO("Esito Operazione"),
	ESITOACQUISIZIONE("Esito Acquisizione Domanda manca"),
	MANCA_ESITO("Esito diverso da OK o KO"),
	ESITO("Esito manca"),
    RICHIEDENTE("Richiedente revoca manca"),
    BENEFICIARIO("Beneficiario revoca manca"),
    DATAREVOCA("Data revoca manca"),
    NOTA("Nota revoca manca");
	
	private final String code;
	
	ErrorParamEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
