/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.util;

public class Constants {

	public final static String COMPONENT_NAME = "buonoresbo";

	public static final String CONTEXT_TEMPO_PARTENZA = "tempopartenza";
	public static final String SHIB_IDENTITA_CODICE_FISCALE = "Shib-Identita-CodiceFiscale";
	public static final String X_REQUEST_ID = "X-Request-ID";
	public static final String X_CODICE_SERVIZIO = "X-Codice-Servizio";
	public static final String X_FORWARDED_FOR = "X-Forwarded-For";

	public static final String SHIB_IRIDE_IDENTITADIGITALE = "Shib-Iride-IdentitaDigitale";
	public static final String X_AUTHENTICATION = "x-authentication";
	public static final String AUTHORIZATION = "Authorization";

	// PARAMETRI
	public static final String PARAMETRO_ERRORE_TIPO = "ERRORE";
	public static final String PARAMETRO_GENERICO = "GENERICO";

	// CODICI CONTRIBUTI
	public static final String BUONORES = "RES";
	public static final String BUONORESBO = "BUONORESBO";

	// CODICI STATO DOMANDA
	public static final String BOZZA = "BOZZA";
	public static final String INVIATA = "INVIATA";
	public static final String ANNULLATA = "ANNULLATA";
	public static final String RETTIFICATA = "RETTIFICATA";
	public static final String IN_RETTIFICA = "IN_RETTIFICA";
	public static final String AMMESSA_RISERVA = "AMMESSA_RISERVA";
	public static final String AMMESSA = "AMMESSA";
	public static final String PREAVVISO_DINIEGO = "PREAVVISO_DINIEGO";
	public static final String IN_PAGAMENTO = "IN_PAGAMENTO";
	public static final String DINIEGO = "DINIEGO";
	public static final String AMMISSIBILE = "AMMISSIBILE";
	public static final String DA_RETTIFICARE = "DA_RETTIFICARE";
	public static final String PRESA_IN_CARICO = "PRESA_IN_CARICO";
	public static final String NON_AMMISSIBILE = "NON_AMMISSIBILE";
	public static final String PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA = "PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA";
	public static final String CONTRODEDOTTA = "CONTRODEDOTTA";
	public static final String PERFEZIONATA_IN_PAGAMENTO = "PERFEZIONATA_IN_PAGAMENTO";
	public static final String AMMESSA_RISERVA_IN_PAGAMENTO = "AMMESSA_RISERVA_IN_PAGAMENTO";
	public static final String REVOCATA = "REVOCATA";
	public static final String RINUNCIATA = "RINUNCIATA";

	public static final String LETTERA_DINIEGO = "LETTERA_DINIEGO";

	public static final String DINIEGO_SCADENZA_REQUISTI_DECESSO = "DINIEGO_SCADENZA_REQUISTI_DECESSO";
	public static final String DINIEGO_SCADENZA_REQUISTI_CAMBIO_RESIDENZA = "DINIEGO_SCADENZA_REQUISTI_CAMBIO_RESIDENZA";

	// CODICI STATO GRADUATORIA
	public static final String PROVVISORIA = "PROVVISORIA";
	public static final String DA_AGGIORNARE = "DA_AGGIORNARE";
	public static final String PUBBLICATA = "PUBBLICATA";

	// path
	public static final String PATH_ARCHIVIAZIONE = "PATH_ARCHIVIAZIONE";

	// TIPO FILE
	public static final String DOMANDA = "DOMANDA";

	public static final String CONTRATTO_TIPO_ID = "contratto_tipo_id";

	// TIPO VERIFICA ENTE GESTORE
	public static final String RICHIESTA_VERIFICA = "RICHIESTA_VERIFICA";
	public static final String VERIFICA_IN_CORSO = "VERIFICA_IN_CORSO";
	public static final String VERIFICA_EFFETTUATA = "VERIFICA_EFFETTUATA";
	public static final String VERIFICA_NON_RICHIESTA = "VERIFICA_NON_RICHIESTA";
	public static final String IN_VERIFICA = "IN_VERIFICA";

	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
	public static final String MOUTH_FORMAT = "MMMM";
	public static final String DATE_FORMAT_WRAPPER = "dd/MM/yyyy";

	// EMAIL
	public static final String OBJECT_MESSAGE = "OBJECT_EMAIL_ENTE";
	public static final String BODY_MESSAGE = "BODY_EMAIL_ENTE";

	// motivi stati
	public static final String DTCONC = "DTCONC";
	public static final String PAM = "PAM";
	public static final String ISEE = "ISEE";

	// stati buono
	public static final String CREATO = "CREATO";

	public static final String SI = "SI";
	public static final String VERISEENONINSERITA = "VERIFICA_ISEE_NON_INSERITA";
}
