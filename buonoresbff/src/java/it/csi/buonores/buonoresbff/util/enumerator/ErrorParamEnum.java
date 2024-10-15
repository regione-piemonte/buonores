/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.util.enumerator;

public enum ErrorParamEnum {

	X_REQUEST_ID("x-Request-Id"),
	SHIB_IDENTITA_CODICEFISCALE("Shib-Identita-CodiceFiscale"),
	X_FORWARDED_FOR("x-Forwarded-for"),
	X_CODICE_SERVIZIO("x-Codice-Servizio"),
	X_CODICE_VERTICALE("x-Codice-Verticale"),
	STATO("Stato"),
	CF("CF"),
	CF_RICHIEDENTE("CF Richiedente"),
	CF_DESTINATARIO("CF Destinatario"),
	CF_DATORE("CF Datore di Lavoro"),
	NOME_RICHIEDENTE("Nome Richiedente"),
	NOME_DESTINATARIO("Nome Destinatario"),
	NOME_DATORE("Nome Datore di Lavoro"),
	COGNOME_RICHIEDENTE("Cognome Richiedente"),
	COGNOME_DESTINATARIO("Cognome Destinatario"),
	COGNOME_DATORE("Cognome Datore di Lavoro"),
	DATA_NASCITA_RICHIEDENTE("Data di Nascita Richiedente"),
	DATA_NASCITA_DESTINATARIO("Data di Nascita Destinatario"),
	DATA_NASCITA_DATORE("Data di Nascita Datore di Lavoro"),
	STATO_NASCITA_RICHIEDENTE("Stato di Nascita Richiedente"),
	STATO_NASCITA_DESTINATARIO("Stato di Nascita Destinatario"),
	STATO_NASCITA_DATORE("Stato di Nascita Datore di Lavoro"),
	COMUNE_NASCITA_RICHIEDENTE("Comune di Nascita Richiedente"),
	COMUNE_NASCITA_DESTINATARIO("Comune di Nascita Destinatario"),
	COMUNE_NASCITA_DATORE("Comune di Nascita Datore di Lavoro"),
	PROVINCIA_NASCITA_RICHIEDENTE("Provincia di Nascita Richiedente"),
	PROVINCIA_NASCITA_DESTINATARIO("Provincia di Nascita Destinatario"),
	PROVINCIA_NASCITA_DATORE("Provincia di Nascita Datore di Lavoro"),
	INDIRIZZO_RESIDENZA_RICHIEDENTE("Indirizzo di Residenza Richiedente"),
	INDIRIZZO_RESIDENZA_DESTINATARIO("Indirizzo di Residenza Destinatario"),
	INDIRIZZO_RESIDENZA_DATORE("Indirizzo di Residenza Datore di Lavoro"),
	COMUNE_RESIDENZA_RICHIEDENTE("Comune di Residenza Richiedente"),
	COMUNE_RESIDENZA_DESTINATARIO("Comune di Residenza Destinatario"),
	COMUNE_RESIDENZA_DATORE("Comune di Residenza Datore di Lavoro"),
	PROVINCIA_RESIDENZA_RICHIEDENTE("Provincia di Residenza Richiedente"),
	PROVINCIA_RESIDENZA_DESTINATARIO("Provincia di Residenza Destinatario"),
	PROVINCIA_RESIDENZA_DATORE("Provincia di Residenza Datore di Lavoro"),
	PUNTEGGIO_SOCIALE("Punteggio Sociale"),
	NOTA_CONTRODEDUZIONE("Nota Controdeduzione"), // MODIFICA TAG 003 buonodom
	STATO_DOMANDA("Stato Domanda"),
	TIPO_CONTRIBUTO("Tipo Contributo"),
	TITOLO_STUDIO_DESTINATARIO("Titolo Studio Destinatario"),
	TIPO_RAPPORTO("Tipo Rapporto"),
	TIPO_RELAZIONE("Tipo Relazione"),
	VALUTAZIONE_MULTIDIMENSIONALE("Valutazione Dimensionale"),
	TIPO_CONTRATTO("Tipo Contratto"),
	CONTRATTO_RSA("Contratto RSA"),
	CONTRATTO_RSA_PIVA("Partita IVA Contratto RSA"),
	ASL_DESTINATARIO("Asl Destinatario"),
	SPORTELLO_ATTIVO("Sportello Attivo"),
	SPORTELLO_CHIUSO("Sportello Chiuso"), // Modifica TAG 2.2.0v002 buonodom
	SITUAZIONE_LAVORATIVA_ATTIVA("Situazione lavorativa attiva"),
	TITOLO_PRIVATO("Titolo privato"),
	INCOMPATIBILITA_PER_CONTRATTO("Incompatibilita per contratto"),
	NESSUNA_INCOMPATIBILITA("Nessuna Incompatibilita'"),
	ISEE_CONFORME("Isee Conforme"),
	STRUTTURA("Struttura"),
	STRUTTURA_NOME("Nome Struttura"),
	INTESTATARIO("Intestatario"),
	DATA_INIZIO_CONTRATTO("Data inizio contratto"),
	DATA_FINE_CONTRATTO("Data fine contratto"),
	COMUNE("Comune Struttura"),
	ALLEGATO("Allegato"),
	ALLEGATO_NOME_FILE("Nome File"),
	VERBALE_VALUTAZIONE_UVG("Verbale valutazione UVG"),
	VERBALE_VALUTAZIONE_UMVD("Verbale valutazione UMVD"),
	DOCUMENTO_IDENTITA("Documento di Identita'"),
	TUTELA("Tutela"),
	PROCURA_SPECIALE("Procura Speciale"),
	RSA("Fornitore Rsa");

	private final String code;

	ErrorParamEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
