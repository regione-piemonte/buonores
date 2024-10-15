/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export default function () {
  return {
    config: {},
    user: null,
    userInfo: null,
    notifyContacts: null,
    fcmToken: null,
    sportelli: null,
    contatti: null,
    preferenze: null,
    utente_richiedente: null,
    utente_destinatario: null,
    spinner: false,
    spinner_stepper: false,
    stepperList: [
      {
        label: 'Informativa sulla privacy e condizioni per ricevere il buono'
      },
      {
        label: 'Destinatario buono'
      },
      {
        label: 'Compilazione modulo'
      },
      {
        label: 'Riepilogo e invio'
      }
    ],
    tipi_rapporto: null,
    titoli_studio: null,
    asl: null,
    tipi_contratto: null,
    stati_domanda: null,
    comuni: null,
    strutture: null,
    // SALVIAMO NELLO STORE OGNI VOLTA CHE CLICCHIAMO AVANTI
    // CAMPI ESTERNI ALL'OGGETTO
    step: 0,
    step_1: false,
    step_2: false,
    step_3: true,
    privacy_toogle: false,
    consenso_toogle: false,
    stepperForm: {
      domicilio_radio: null,
      destinatario_radio: null,
      show_destinatario: null,
    },

    // OGGETTO EFFETTIVO DA INVIARE
    richiesta: {
      numero: null,
      stato: 'BOZZA',
      data_aggiornamento: null,
      richiedente: {
        cf: null,
        nome: null,
        cognome: null,
        data_nascita: null,
        stato_nascita: null,
        comune_nascita: null,
        provincia_nascita: null,
        indirizzo_residenza: null,
        comune_residenza: null,
        provincia_residenza: null
      },
      destinatario: {
        cf: null,
        nome: null,
        cognome: null,
        data_nascita: null,
        stato_nascita: null,
        comune_nascita: null,
        provincia_nascita: null,
        indirizzo_residenza: null,
        comune_residenza: null,
        provincia_residenza: null
      },
      note: null,
      studio_destinatario: null,
      lavoro_destinatario: null,
      domicilio_destinatario: {
        indirizzo: null,
        comune: null,
        provincia: ""
      },
      asl_destinatario: null,
      delega: null,
      attestazione_isee: false,
      punteggio_bisogno_sociale: null,
      contratto: {
        tipo: null,
        data_inizio: null,
        struttura: {
          id: null,
          id_arpe: null,
          nome: null,
          indirizzo: null,
          comune: null,
          provincia: null,
          piva: null,
          sede_legale: {
            id: null,
            id_arpe: null,
            nome: null,
            indirizzo: null,
            comune: null,
            provincia: null
          }
        },
        titolo_privato: false,
        incompatibilita_per_contratto: null
      },
      nessuna_incompatibilita: null,
    }
  }
}
