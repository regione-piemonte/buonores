/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export const enum DATIENTE_INFO {
  INFO_PARAM_TITLE = 'Informazioni Parametri',
  INFO_DOC_TITLE = 'Informazioni Documentazione',

  INFO_DOC_INIZIALI_TITLE = 'Informazioni Documenti Iniziali',
  INFO_DOC_FINALI_TITLE = 'Informazioni Documenti Finali',
}


export const enum PROFILES {
  EG = 'EG',
  O_REG = 'O-REG',
}

export const enum PROJECT_CONSTANTS {
  BACKOFFICE_TITLE = 'Backoffice Welfare',
  LOADING = 'Loading...',
  LOADING_TEXT = 'Caricamento del sistema in corso...'
}

export const enum FOOTER_CONSTANTS {
  ASSISTENZA = 'ASSISTENZA',
  HELP = 'Hai bisogno di aiuto?',
  WRITE = 'Per richieste di assistenza, compilare il ',
  FORM = ' form.',
  SISTEMA = '',
  SERVIZIO = 'Servizio a cura della Regione Piemonte',
  P_IVA = 'P.Iva-CF 80087670016',
  PORTAL = 'sisp',
  LINK='https://assistenzasalute.csi.it/#/assistenza/BO_BUONO_RES'
}

export const enum MENU {
  HELP = 'HELP',
}

export const enum PATTERN {
  CHARS = '^[a-zA-ZÃ?Â Ã?Â¡Ã?Â¢Ã?Â¤Ã?Â£Ã?Â¥Ã?â?¦Ã?ï¿½Ã?â?¡Ã?â?¢Ã?Â¨Ã?Â©Ã?ÂªÃ?Â«Ã?â??Ã?Â¯Ã?Â¬Ã?Â­Ã?Â®Ã?Â¯Ã?â??Ã?â??Ã?Â²Ã?Â³Ã?Â´Ã?Â¶Ã?ÂµÃ?Â¸Ã?Â¹Ã?ÂºÃ?Â»Ã?Â¼Ã?Â³Ã?Â«Ã?Â¿Ã?Â½Ã?Â¼Ã?ÂºÃ?Â±Ã?Â§Ã?ï¿½Ã?Â¡Ã?Â¾Ã?â?¬Ã?ï¿½Ã?â??Ã?â??Ã?Æ?Ã?â?¦Ã?â??Ã?â? Ã?Å?Ã?â??Ã?Ë?Ã?Ë?Ã?â?°Ã?Å Ã?â?¹Ã?Å?Ã?ï¿½Ã?Å½Ã?ï¿½Ã?Â®Ã?ï¿½Ã?Æ?Ã?â??Ã?â??Ã?â??Ã?â??Ã?â?¢Ã?Ë?Ã?â?¢Ã?Å¡Ã?â?ºÃ?Å?Ã?Â²Ã?ÂªÃ?Â¸Ã?ï¿½Ã?Â»Ã?Â¹Ã?â??Ã?Å¸Ã?â?¡Ã?â??Ã?â? Ã?Å?Ã?Â Ã?Â½Ã¢Ë?â??Ã?Â° ,.\'-]*$',
  others = '^[\/\\\*\^]$'
}


export const enum STATO_DOMANDA {
  INVIATA = 'INVIATA',
  RETTIFICATA = 'RETTIFICATA',
  DA_RETTIFICARE = 'DA_RETTIFICARE',
  PRESA_IN_CARICO = 'PRESA_IN_CARICO',
  INVIATA_DESC = 'INVIATA',
  RETTIFICATA_DESC = 'RETTIFICATA',
  DA_RETTIFICARE_DESC = 'DA RETTIFICARE',
  PRESA_IN_CARICO_DESC = 'PRESA IN CARICO',
  AMMESSA = 'AMMESSA',
  AMMISSIBILE = 'AMMISSIBILE',
  NON_AMMESSA = 'NON AMMESSA',
  NON_AMMISSIBILE = 'NON_AMMISSIBILE',
  PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA = 'PREAVVISO DI DINIEGO PER NON AMMISSIBILITA',
  PERFEZIONATA_IN_PAGAMENTO = 'PERFEZIONATA_IN_PAGAMENTO',
  AMMESSA_CON_RISERVA = 'AMMESSA_RISERVA',
  AMMESSA_CON_RISERVA_IN_PAGAMENTO = 'AMMESSA_RISERVA_IN_PAGAMENTO',
  IN_PAGAMENTO = 'IN_PAGAMENTO',
  DINIEGO = 'DINIEGO',
  CONTRODEDOTTA = 'CONTRODEDOTTA',
  RINUNCIATA = 'RINUNCIATA',
  ANNULLATA = 'ANNULLATA',
  REVOCATA = 'REVOCATA',
}

export const enum STATO_GRADUATORIA {
  PROVVISORIA = 'PROVVISORIA',
  DA_AGGIORNARE = 'DA_AGGIORNARE',
  PUBBLICATA = 'PUBBLICATA'
}

export const enum AZIONE {
  OP_RicercaDomandeAperte = 'OP-RicercaDomandeAperte',
  OP_RichiediRettifica = 'OP-RichiediRettifica',
  OP_VerificaISEE = 'OP-VerificaISEE',
  OP_PrendiInCarico = 'OP-PrendiInCarico',
  OP_VisualizzaCronologia = 'OP-VisualizzaCronologia',
  OP_VisualizzaVerifiche = 'OP-VisualizzaVerifiche',
  OP_ScaricaAllegati = 'OP-ScaricaAllegati',
  OP_Archivio = 'OP-Archivio',
  OP_AmmettiDomanda = 'OP-AmmettiDomanda',
  OP_RespingiDomanda = 'OP-RespingiDomanda',
  OP_AmmissibileDomanda = 'OP-AmmissibileDomanda',
  OP_PreavvisopernonammissibilitaDomanda = 'OP-PreavvisopernonammissibilitaDomanda',
  OP_AmmessaConRiserva = 'OP-AmmessaConRiserva',
  OP_InPagamento = 'OP-InPagamento',
  OP_Diniego = 'OP-Diniego',
  OP_Istruttoria = 'OP-Istruttoria',
  OP_RichiediRettificaEnte = 'OP-RichiediRettificaEnte',
  OP_Salva_Verifica_Ente = 'OP-SalvaVerificaEnte',
  OP_Avvia_Verifica_Ente = 'OP-AvviaVerificaEnte',
  OP_Concludi_Verifica_Ente = 'OP-ConcludiVerificaEnte',
  OP_Graduatoria = 'OP-Graduatoria',
  OP_NuovaGraduatoria = 'OP-NuovaGraduatoria',
  OP_PubblicaGraduatoria = 'OP-PubblicaGraduatoria',
  OP_AggiornaGraduatoria = 'OP-AggiornaGraduatoria',
  OP_SimulaGraduatoria = 'OP-SimulaGraduatoria',
  OP_AmmessaConRiservaInPagamento = 'OP-AmmessaConRiservaInPagamento',
  OP_NuovoSportello = 'OP-NuovoSportello',
  OP_NonAmmissibileDomanda = 'OP-NonAmmissibileDomanda',
  OP_Buono = 'OP-Buono',
  OP_RendicontazioneEnte = 'OP-RendicontazioneEnte',
  OP_RevocaBuono = 'OP-RevocaBuono',
  OP_BuonoDettaglio = 'OP-BuonoDettaglio',
  OP_ArchivioGraduatorie = 'OP-ArchivioGraduatorie'
}

export const enum ALLEGATI {
  CONTRATTO_RSA='CONTRATTO_RSA',
  VERBALE_UVG = 'VERBALE_UVG',
  VERBALE_UMVD = 'VERBALE_UMVD',
  DOMANDA = 'DOMANDA',
  CARTA_IDENTITA = 'CARTA_IDENTITA',
  DELEGA = 'DELEGA',
  PROCURA_SPECIALE = 'PROCURA_SPECIALE',
  LETTERA_INCARICO = 'LETTERA_INCARICO',
  NOMINA_TUTORE = 'NOMINA_TUTORE',
  CONTRODEDUZIONE = 'CONTRODEDUZIONE'
}

export const enum CONTRATTO {
  COOPERATIVA = 'COOPERATIVA',
  NESSUN_CONTRATTO = 'NESSUN_CONTRATTO',
  ASSISTENTE_FAMILIARE = 'ASSISTENTE_FAMILIARE',
  PARTITA_IVA = 'PARTITA_IVA'
}

export const enum VALUTAZIONE {
  UVG = 'UVG',
  UMVD = 'UMVD'
}
export const enum DOC {
  DOC_INIZIALE = 'Documento Iniziale',
  DOC_FINALE = 'Documento Finale'
}

export const enum SECTION {
  RICERCA_DOMANDE_APERTE = 'RICERCA_ISTANZE_APERTE',
  RICERCA_ARCHIVIO = 'RICERCA_ARCHIVIO',
  RENDICONTAZIONE = 'RENDICONTAZIONE',
  DATI_ENTE = 'DATI ENTE',
  MODELLOA1 = 'MODELLO A1',
  MODELLOA2 = 'MODELLO A2',
  MODELLOA = 'MODELLO A',
  MODELLOB = 'MODELLO B',
  MODELLOD = 'MODELLO D',
  MODELLOB1 = 'MODELLO B1',
  MODELLOMACRO = 'MODELLO MACRO',
  MACROAGGREGATITOOLTIP = 'MACROAGGREGATI TOOLTIP',
  MACROAGGREGATIIMGM = 'MACROAGGREGATI IMG M',
  MACROAGGREGATIPOPUP = 'MACROAGGREGATI POP UP',
  ALLD01 = 'ALLD01',
  ALLD02 = 'ALLD02',
  ALLD03 = 'ALLD03',
  ALLD04 = 'ALLD04',
  ALLD05 = 'ALLD05',
  ALLD06 = 'ALLD06',
  ALLD07 = 'ALLD07',
  MODELLOCINFO = 'MODELLO C INFO',
  MODELLOCINFODISABILITA = 'MODELLO C INFO DISABILITA',
  MODELLOCINFOTOTDISABILITA = 'MODELLO C INFO TOTDISABILITA',
  MODELLOCINFOTOTDISABILITAADULTI = 'MODELLO C INFO TOTDISABILITA ADULTI',
  MODELLOC_R_A_1_1 = 'MODELLO C R_A.1.1',
  MODELLOC_R_A_2_1 = 'MODELLO C R_A.2.1',
  MODELLOC_TAB = 'MODELLO C TAB',
  MODELLOE = 'MODELLO E',
  MODELLOE_POPOVER = 'MODELLO E TOOLTIP',
  MODELLOE_CANCELLAZIONE = 'MODELLO E CANCELLAZIONE',
  MODELLOF = 'MODELLO F',
  MODELLOFTITOLO = 'MODELLO F TITOLO',
  MODELLOFMSG1 = 'MODELLO F MSG1',
  MODELLOFMSG2 = 'MODELLO F MSG2',
  MODELLOFMSG3 = 'MODELLO F MSG3',
  MODELLOFMSG4 = 'MODELLO F MSG4',
  MODELLOFMSG5 = 'MODELLO F MSG5',
  MODELLOC = 'MODELLO C',
  CODMODELLOC = 'C',
  CODMODELLOA1 = 'A1',
  CODMODELLOA2 = 'A2',
  CODMODELLOA = 'A',
  CODMODELLOB = 'B',
  CODMODELLOD = 'D',
  CODMODELLOB1 = 'B1',
  CODMODELLOMACRO = 'MA',
  CODMODELLOE = 'E',
  CODMODELLOF = 'F',
  ALLEGATOD = 'Modulo MFNPS',
  CODALLEGATOD = 'MFNPS',
  MODELLOBTAB = 'MODELLO B TAB',
  MODELLOBDESCTOTALI = 'MODELLO B DESC TOTALI',
  MODELLOBDESCTOTALIMISSIONE = 'MODELLO B DESC TOTALI MISSIONE',
  SOGLIAFNPS = 'SOGLIAFNPS',
  MODATOOLTIP = 'MODATOOLTIP',
  MODFTOOLTIP = 'MODFTOOLTIP',
  ANNOESERCIZIO = 'ANNO_ESERCIZIO',
  MESSAGGIOELIMINA = 'MESSAGGIOELIMINA',
  MESSAGGIOANNULLA = 'MESSAGGIOANNULLA',
  MESSAGGIOCONCLUDISINGOLO = 'MESSAGGIOCONCLUDISINGOLO',
  MESSAGGIOCONCLUDIMULTIPLO = 'MESSAGGIOCONCLUDIMULTIPLO',
  MESSAGGIORIAPRISINGOLO = 'MESSAGGIORIAPRISINGOLO',
  MESSAGGIORIAPRIMULTIPLO = 'MESSAGGIORIAPRIMULTIPLO',
  MESSAGGIOSALVACONMODPASS = 'MESSAGGIOSALVACONMODPASS',
}

export const enum ERRORS {
  ERROR_ANNO_CONTABILE = 'ERR13',
  ERROR_DELETE_PRESTAZIONI = 'ERR16',
  ERROR_UPDATE_DOC_ALLEGATI = 'ERR20',
  ERROR_COMUNE_ALREADY_PRESENT = 'ERR21',
  ERROR_PREST_ALREADY_PRESENT = 'ERR22',
  ERROR_UPLOAD_ALLEGATI = 'ERR23',
  ERROR_DELETE_PREST_ASS = 'ERR30',
  ERROR_DELETE_PREST_ASS_MODC = 'ERR43',
  ERROR_DELETE_COMUNE_ASS = 'ERR31',
  ERROR_EXPORT = 'ERR35',
  ERROR_MODB1 = 'ERRMODB1',
  ERROR_MODC01 = 'ERRMODC01',
  ERROR_MODC02 = 'ERRMODC02',
  ERROR_MODC03 = 'ERRMODC03',
  ERROR_MODC04 = 'ERRMODC04',
  ERROR_MODC05 = 'ERRMODC05',
  ERROR_MODC06 = 'ERRMODC06',
  ERROR_MODC07 = 'ERRMODC07',
  ERROR_MODC08 = 'ERRMODC08',
  ERROR_MODC09 = 'ERRMODC09',
  ERROR_MODC10 = 'ERRMODC10',
  ERROR_MODF01 = 'ERRMODF01',
  ERROR_FNPS01 = 'ERRFNPS01',
  ERROR_FNPS02 = 'ERRFNPS02',
  ERROR_FNPS03 = 'ERRFNPS03',
  ERROR_FNPS04 = 'ERRFNPS04',
  ERROR_PIPPI_FNPS = 'ERRPIPPIFNPS',
  ERROR_STATO_REND = 'ERRSTATOREND',
  ERROR_MODIF_MOD = 'ERRMODIFMOD',
  ERROR_MODELLO_ALREADY_PRESENT = 'ERR58',
  ERROR_DELETE_MODELLI_ASS = 'ERR59',
}

export const enum PREST_RES_SEMIRES {
  COD_TIPOLOGIA = 'MA03',
  TIPO_STRUTT_RES = '01',
  TIPO_STRUTT_CD = '02',
}

export const enum MSG {
  POPUP_MODELLI_RENDICONTAZIONE_EDIT = 'POPUP01',
  WARN_TAB = 'WARN-TAB',
  WARNDATIANAGRAFICI = 'WARNDATIANAGRAFICI'
}

export const enum TRANCHE {
  I_TRANCHE = 'PT',
  II_TRANCHE = 'ST',
  III_TRANCHE = 'TT',
  PRIMA_TRANCHE = "prima tranche",
  SECONDA_TRANCHE = "seconda tranche",
  TERZA_TRANCHE = "terza tranche",
}

export const enum OPERAZIONE {
  INVIAMODELLI = 'INVIAMODELLI',
  CONFERMAINVIAMODELLI = 'CONFERMAINVIAMODELLI',
  SALVA = 'SALVA',
  CHECK = 'CHECK'
}

export const enum VOCE_TESTUALE_MOD_A {
  COD_TITOLO = '02-ALTRO',
  COD_TIPOLOGIA = 'Tipo_altro',
  COD_VOCE = '32'
}

export const enum VOCE_IMPORTO_MOD_A {
  COD_TITOLO = '02-ALTRO',
  COD_TIPOLOGIA = 'Tipo_altro',
  COD_VOCE = '31'
}

export const enum MESSAGES {
  MSG_INVIO_ATTENZIONE = 'MSG09',
}

export const enum TIPO_PROFILO {
  HOME_REGIONALE = 'OP-HomeRegionale',
  HOME_ENTE = 'OP-HomeEnte',
}

export const enum STATO_ENTE {
  APERTO = 'APE',
  CHIUSO = 'CHI',
}

export const enum STATO_ENTE_DESC_LUNGA {
  APERTO = 'APERTO',
  CHIUSO = 'CHIUSO',
}

export const enum TIPO_FEEDBACK {
  SUCCESS = "success",
  // FAIL = "fail",
  // MESSAGGIO_SALVATAGGIO_SUCCESS = "Salvataggio avvenuto con successo",
  // MESSSAGGIO_SALVATAGGIO_FAIL = "Salvataggio non riuscito",
  ERROR = "error"
}
