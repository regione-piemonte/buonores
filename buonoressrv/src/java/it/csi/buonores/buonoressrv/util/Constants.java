/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.util;

public class Constants {

	public final static String COMPONENT_NAME = "buonoressrv";

	public static final String CONTEXT_CHIAVE_ID = "chiaveid";
	public static final String CONTEXT_TEMPO_PARTENZA = "tempopartenza";
	public static final String SHIB_IDENTITA_CODICE_FISCALE = "Shib-Identita-CodiceFiscale";
	public static final String X_REQUEST_ID = "X-Request-ID";
	public static final String X_CODICE_SERVIZIO = "X-Codice-Servizio";
	public static final String X_FORWARDED_FOR = "X-Forwarded-For";

	// PARAMETRI
	public static final String PARAMETRO_ERRORE_TIPO = "ERRORE";
	public static final String PARAMETRO_MESSAGGIO_TIPO = "MESSAGGIO";
	public static final String PARAMETRO_GENERICO = "GENERICO";
	public static final String NOTIFICATORE = "NOTIFICATORE";

	// CODICI STATO DOMANDA
//	public static final String BOZZA = "BOZZA";
	public static final String INVIATA ="INVIATA";
//	public static final String ANNULLATA = "ANNULLATA";
	public static final String RETTIFICATA = "RETTIFICATA";
	public static final String IN_RETTIFICA = "IN_RETTIFICA";
	public static final String AMMESSA_RISERVA = "AMMESSA_RISERVA";
	public static final String AMMESSA_RISERVA_IN_PAGAMENTO = "AMMESSA_RISERVA_IN_PAGAMENTO";
//	public static final String PREAVVISO_DINIEGO = "PREAVVISO_DINIEGO";
	public static final String IN_PAGAMENTO = "IN_PAGAMENTO";
	public static final String DINIEGO = "DINIEGO";
//	public static final String AMMESSA = "AMMESSA";
	public static final String DINIEGO_SCADENZA_REQUISTI_DECESSO = "DINIEGO_SCADENZA_REQUISTI_DECESSO";
	public static final String DINIEGO_SCADENZA_REQUISTI_CAMBIO_RESIDENZA = "DINIEGO_SCADENZA_REQUISTI_CAMBIO_RESIDENZA";
	public static final String AMMESSA_NON_FINANZIATA = "AMMESSA_NON_FINANZIATA";
	public static final String DA_RETTIFICARE = "DA_RETTIFICARE";
	public static final String NON_AMMISSIBILE = "NON_AMMISSIBILE";
	public static final String PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA = "PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA";
//	public static final String AMMISSIBILE = "AMMISSIBILE";
	public static final String AMMESSA_FINANZIATA = "AMMESSA_FINANZIATA";
	public static final String AMMESSA = "AMMESSA";
	public static final String PREAVVISO_DINIEGO_IN_PAGAMENTO ="PREAVVISO_DINIEGO_IN_PAGAMENTO";
  
	// CODICI CONTRIBUTI
	public static final String BUONORES = "BUONORES";

	// TIPO PERSONA
	public static final String RICHIEDENTE = "RICHIEDENTE";
	public static final String DESTINATARIO = "DESTINATARIO";

	// VALUTAZIONE MULTIDIMENSIONALE
	public static final String PERSONA_PIU_65_ANNI = "UVG";
	public static final String PERSONA_DISABILE = "UMVD";

	// ISEE
	public static final Integer ATTESTAZIONE_ISEE_NON_DISABILE = 50000;
	public static final Integer ATTESTAZIONE_ISEE_DISABILE = 65000;

	// titoloStudio
	public static final String DIPLOMA_SECONDO_GRADO = "DIPLOMA_SECONDO_GRADO";
	public static final String DIPLOMA_TERZIARIA = "DIPLOMA_TERZIARIA";
	public static final String DIPLOMA_PRIMO_GRADO = "DIPLOMA_PRIMO_GRADO";
	public static final String NESSUN_TITOLO_STUDIO = "NESSUN_TITOLO_STUDIO";

	// rapporto
	public static final String POTESTA_GENITORIALE = "POTESTA_GENITORIALE";
	public static final String NUCLEO_FAMILIARE = "NUCLEO_FAMILIARE";
	public static final String CONIUGE = "CONIUGE";
	public static final String PARENTE_PRIMO_GRADO = "PARENTE_PRIMO_GRADO";
	public static final String TUTELA = "TUTELA";
	public static final String CURATELA = "CURATELA";
	public static final String AMMINISTRAZIONE_SOSTEGNO = "AMMINISTRAZIONE_SOSTEGNO";
	public static final String ALTRO = "ALTRO";
	public static final String PROCURA_SPECIALE = "PROCURA_SPECIALE";

	// contratto
	public static final String CONTRATTO_RSA = "CONTRATTO_RSA";
	public static final String NESSUN_CONTRATTO = "NESSUN_CONTRATTO";

	// DGR
	public static final String DGR = "DGR";

	// path
	public static final String PATH_ARCHIVIAZIONE = "PATH_ARCHIVIAZIONE";

	// StatiNotificatore
	public static final String INVIO_CITTADINO = "INVIO_CITTADINO";
	public static final String RETTIFICA_CITTADINO = "RETTIFICA_CITTADINO";
	public static final String BO_AMMESSA_RISERVA_CONTRATTO = "BO_AMMESSA_RISERVA_CONTRATTO";
	public static final String BO_RETTIFICA = "BO_RETTIFICA";
	public static final String BO_AMMETTI_RISERVA = "BO_AMMETTI_RISERVA";
	public static final String BO_DINIEGO_DOPO_PREAVVISO = "BO_DINIEGO_DOPO_PREAVVISO";
	public static final String BO_PREAVVISO_DINIEGO_CON_CONTRODEDUZIONI = "BO_PREAVVISO_DINIEGO_CON_CONTRODEDUZIONI";
	public static final String BO_PERFEZIONATA_IN_PAGAMENTO = "BO_PERFEZIONATA_IN_PAGAMENTO";
	public static final String BO_PREAVVISO_DINIEGO_NON_AMMISSIBILITA = "BO_PREAVVISO_DINIEGO_NON_AMMISSIBILITA";
	public static final String BO_PREAVVISO_DI_DINIEGO_IN_PAGAMENTO = "BO_PREAVVISO_DI_DINIEGO_IN_PAGAMENTO";
	public static final String CONTRODEDOTTA = "CONTRODEDOTTA";
	public static final String CONTRODEDOTTA_CITTADINO = "CONTRODEDOTTA_CITTADINO";
	public static final String RINUNCIATA_CITTADINO = "RINUNCIATA_CITTADINO";
	public static final String RINUNCIATA = "RINUNCIATA";
	public static final String BO_DINIEGO_SENZA_PREAVVISO = "BO_DINIEGO_SENZA_PREAVVISO";
	public static final String BO_DINIEGO_SCADENZA_REQUISTI = "BO_DINIEGO_SCADENZA_REQUISTI";
	public static final String PERFEZIONATA_IN_PAGAMENTO = "PERFEZIONATA_IN_PAGAMENTO";
	public static final String PERFEZIONATA_IN_PAGAMENTO_CITTADINO = "PERFEZIONATA_IN_PAGAMENTO_CITTADINO";
	public static final String BO_AMMESSA_FINANZIATA = "BO_AMMESSA_FINANZIATA";
	public static final String BO_AMMESSA_NON_FINANZIATA = "BO_AMMESSA_NON_FINANZIATA";
	public static final String BO_DINIEGO = "BO_DINIEGO";
	public static final String BO_DINIEGO_SCADENZA_REQUISTI_DECESSO = "BO_DINIEGO_SCADENZA_REQUISTI_DECESSO";
	public static final String BO_DINIEGO_SCADENZA_REQUISTI_CAMBIO_RESIDENZA = "BO_DINIEGO_SCADENZA_REQUISTI_CAMBIO_RESIDENZA";
	
	// stardas
	public static final String CODICEFISCALEENTE = "CODICEFISCALEENTE";
	public static final String CODICEFRUITORE = "CODICEFRUITORE";
	public static final String CODICEAPPLICAZIONE = "CODICEAPPLICAZIONE";
	public static final String CODICETIPODOCUMENTO = "CODICETIPODOCUMENTO";
	public static final String CODICETIPODOCUMENTOPAR = "CODICETIPODOCUMENTOPAR";
	public static final String CODICETIPOALLEGATO = "CODICETIPOALLEGATO";
	public static final String CFTRATTAMENTO = "CFTRATTAMENTO";

	public static final String STARDAS = "STARDAS";

	public static final String DOMANDA = "DOMANDA";
	public static final String STARDAS_OK = "000";
	public static final String STARDAS_OK_PARZIALE = "001";
	public static final String PRINCIPALE = "PRINCIPALE";
	public static final String ALLEGATI = "ALLEGATI";

    //MODIFICA TAG 004 buonodom
    public static final String CONTRODEDUZIONE = "CONTRODEDUZIONE";
    
	public static final String CODICE_SERIE_DOSSIER = "CODICE_SERIE_DOSSIER";
	public static final String DESCRIZIONE_SERIE_DOSSIER = "DESCRIZIONE_SERIE_DOSSIER";
	public static final String PAROLA_CHIAVE_SERIE_DOSSIER= "PAROLA_CHIAVE_SERIE_DOSSIER";
	public static final String PAROLA_CHIAVE_DOSSIER= "PAROLA_CHIAVE_DOSSIER";
	public static final String PAROLA_CHIAVE_FASCICOLO= "PAROLA_CHIAVE_FASCICOLO";
	public static final String CODICE_DOSSIER = "CODICE_DOSSIER";
	public static final String DESCRIZIONE_DOSSIER = "DESCRIZIONE_DOSSIER";
	public static final String NUMERO_FASCICOLO = "NUMERO_FASCICOLO";
	public static final String OGGETTO_FASCICOLO = "OGGETTO_FASCICOLO";
	public static final String OGGETTO_DOCUMENTO = "OGGETTO_DOCUMENTO";
	public static final String PAROLE_CHIAVE_DOCUMENTO = "PAROLE_CHIAVE_DOCUMENTO";
	public static final String AUTORE_FISICO_DOCUMENTO = "AUTORE_FISICO_DOCUMENTO";
	public static final String DATA_CRONICA_DOCUMENTO = "DATA_CRONICA_DOCUMENTO";
	public static final String MITTENTE_PF_COGNOME = "MITTENTE_PF_COGNOME";
	public static final String MITTENTE_PF_NOME = "MITTENTE_PF_NOME";
	public static final String MITTENTE_PF_CODICE_FISCALE = "MITTENTE_PF_CODICE_FISCALE";
	public static final String DESTINATARIO_PF_COGNOME= "DESTINATARIO_PF_COGNOME";
	public static final String DESTINATARIO_PF_NOME= "DESTINATARIO_PF_NOME";
	public static final String DESTINATARIO_PF_CODICE_FISCALE= "DESTINATARIO_PF_CODICE_FISCALE";
	public static final String CHIAMA_NOTIFICATORE = "CHIAMA_NOTIFICATORE";
	public static final String CHIAMA_CONTATTI = "CHIAMA_CONTATTI";
	public static final String CHIAMA_STARDAS = "CHIAMA_STARDAS";
	public static final String DESTINATARIO_GIURIDICO_DOCUMENTO = "DESTINATARIO_GIURIDICO_DOCUMENTO";
	
	public static final String LETTERA_AMMISSIONE_FINANZIAMENTO = "LETTERA_AMMISSIONE_FINANZIAMENTO";
	public static final String LETTERA_AMMISSIONE_NON_FINANZIAMENTO = "LETTERA_AMMISSIONE_NON_FINANZIAMENTO";
	public static final String LETTERA_DINIEGO = "LETTERA_DINIEGO";
	public static final String AMMISSIONE_FINANZIAMENTO = "AMMISSIONE_FINANZIAMENTO";
	public static final String AMMISSIONE_NON_FINANZIAMENTO = "AMMISSIONE_NON_FINANZIAMENTO";

    public static final String REVOCA_BUONO ="REVOCA_BUONO";
    
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_FORMAT_ITALIAN = "dd/MM/yyyy";
	public static final String DATE_FORMAT_NOME_MESE = "dd MMMM yyyy";
	
    public static final String RESPINGI_ISEE ="RESPINGI_ISEE";
    public static final String NON_CONFORME_ISEE ="NON_CONFORME_ISEE";
}
