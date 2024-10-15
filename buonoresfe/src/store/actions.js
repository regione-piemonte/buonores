/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import store from "src/store/index";
import {extend, LocalStorage} from "quasar";
import {
  updateNotifyContacts
} from "src/services/api";
import {apiErrorNotify, apiErrorNotifyDialog, orderBy} from "src/services/utils";


export const setConfig = (context, { config }) => {
  context.commit("SET_CONFIG", { config });
};

export const setUser = (context, { user }) => {
  context.commit("SET_USER", { user });
};

export const setUserInfo = (context, { userInfo }) => {
  context.commit("SET_USER_INFO", { userInfo });
};

export const setNotifyContacts = (context, contacts ) => {
  context.commit("SET_NOTIFY_CONTACTS", { contacts });
};

export const setContatti = (context, contatti ) => {
  context.commit("SET_CONTATTI", { contatti });
};

export const setPreferenze = (context, preferenze ) => {
  context.commit("SET_PREFERENZE", { preferenze });
};

//LOGICHE LEGATE AL BUONO
export const setUtenteRichiedente = (context,  utente_richiedente ) => {
  context.commit("SET_UTENTE_RICHIEDENTE", { utente_richiedente });
};

export const setUtenteDestinatario = (context,  utente_destinatario ) => {
  context.commit("SET_UTENTE_DESTINATARIO", { utente_destinatario });
};

export const setSpinner = (context,  spinner ) => {
  context.commit("SET_SPINNER", { spinner });
};

export const setSpinnerStepper = (context,  spinner_stepper ) => {
  context.commit("SET_SPINNER_STEPPER", { spinner_stepper });
};

export const setSportelli = (context, { sportelli }) => {
  context.commit("SET_SPORTELLI", { sportelli });
};

export const setOptions = (context,  item ) => {
  context.commit("SET_OPTIONS",  item );
};

export const setStepperList = (context, { stepperList }) => {
  context.commit("SET_STEPPER_LIST", { stepperList });
};

export const setStep = (context, step ) => {
  context.commit("SET_STEP", { step });
};

export const setNextStep = (context, item ) => {
  context.commit("SET_NEXT_STEP", item);
};

export const resetAllStep = (context) => {
  context.commit("RESET_ALL_STEP");
};

export const bozzaAllStep = (context) => {
  context.commit("BOZZA_ALL_STEP");
};

export const setRichiesta = (context, item ) => {
  context.commit("SET_RICHIESTA", item);
};

export const setStepperForm = (context, item ) => {
  context.commit("SET_STEPPER_FORM", item);
};

export const resetAllState = async (context) => {
  let stepperForm = {
    stepperForm: {
      domicilio_radio: null,
      destinatario_radio: null,
    },
  };
  let richiesta = {
    // OGGETTO EFFETTIVO DA INVIARE
    numero: null,
    stato: "BOZZA",
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
      provincia_residenza: null,
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
      provincia_residenza: null,
    },
    note: null,
    studio_destinatario: null,
    lavoro_destinatario: null,
    domicilio_destinatario: {
      indirizzo: null,
      comune: null,
      provincia: "",
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
  };
  await store.dispatch("resetAllStep");
  await store.commit("SET_PRIVACY", false);
  await store.commit("SET_AGREMENT", false);
  await store.dispatch("setStepperForm", stepperForm);
  await store.dispatch("setRichiesta", richiesta);
};



//TOKEN FIREBASE
export const setFcmToken = async (context, { fcmToken }) => {
  context.commit("SET_FCM_TOKEN", { fcmToken });

  // Proviamo ad aggiungere il nuovo token a tutti i servizi che non ce l'hanno già impostato
  try {
    let user = context.getters["getUser"];
    let taxCode = user?.cf;

    let appList = context.getters["getAppList"] ?? [];
    let appListNotifiable = appList.filter(app => app.notificabile);

    let notifyContacts = context.getters["getNotifyContacts"];
    notifyContacts = extend(true, {}, notifyContacts);

    // Prendiamo solo le app che non hanno già questo token FCM collegato
    let appListWithoutFcmToken = appListNotifiable.filter(s => {
      let code = s.codice_servizio?.toLowerCase() ?? null;
      let fcmTokenList = notifyContacts?.push?.[code] ?? [];
      return !fcmTokenList.includes(fcmToken);
    });

    // Se non ci sono applicazioni a cui aggiungere il nuovo token
    // => non facciamo nulla evitando una chiamata inutile
    if (!appListWithoutFcmToken.length) return;

    // Aggiungiamo il nuovo token a tutte le applicazioni che non hanno il nuovo token
    let pushListNew = appListWithoutFcmToken.reduce((out, s) => {
      let code = s.codice_servizio?.toLowerCase();
      let fcmTokenListOld = notifyContacts?.push?.[code] ?? [];
      out[code] = [...fcmTokenListOld, fcmToken];
      return out;
    }, {});

    notifyContacts.push = { ...notifyContacts.push, ...pushListNew };
    let { data } = await updateNotifyContacts(taxCode, notifyContacts);

    context.commit("SET_NOTIFY_CONTACTS", { notifyContacts: data });
  } catch (err) {
    console.error(err);
  }
};



