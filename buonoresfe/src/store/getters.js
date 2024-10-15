/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import {
  APPLICATION_CODE,
  DELEGATION_STATUS_MAP,
  ENROLLMENT_CODES,
  USER_ENABLING_MAP,
} from "src/services/config";
import { orderBy } from "src/services/utils";
import { getUserMinor } from "src/services/business-logic";
import { date } from "quasar";

const { subtractFromDate, isBetweenDates } = date;

export const getAppList = state => {
  return state.config?.apps ?? [];
};

export const getFcmVapidKey = state => {
  return state.config?.fcm?.vapid_key;
};

export const getFcmConfig = state => {
  return state.config?.fcm?.config;
};

export const getFcmToken = state => {
  return state.fcmToken;
};

export const getUser = state => {
  return state.user;
};

// sportelli sono fondamentali senza essi l'applicativo non può fare nuove domande o continuare bozze
export const getSpinner = state => {
  return state.spinner;
};

export const getSpinnerStepper = state => {
  return state.spinner_stepper;
};

// sportelli sono fondamentali senza essi l'applicativo non può fare nuove domande o continuare bozze
export const getSportelli = state => {
  return state.sportelli;
};

// stepper list
export const getStepperList = state => {
  return state.stepperList;
};

// step attuale
export const getStep = state => {
  return state.step;
};

// situazione richiesta attuale
export const getUtenteRichiedente = state => {
  return state.utente_richiedente;
};

// utente destinatario
export const getUtenteDestinatario = state => {
  return state.utente_destinatario;
};

// situazione richiesta attuale
export const getRichiesta = state => {
  return state.richiesta;
};

export const getTaxCode = state => {
  if (state.delegatorSelected) {
    return state.delegatorSelected?.codice_fiscale_delega;
  }
  return state.user?.cf;
};

export const isUserMinorByTaxCode = state => {
  let taxCode = state.user?.cf;
  if (!taxCode) return false;
  return getUserMinor(taxCode);
};

export const isUserMinor = (state, getters) => {
  let userInfo = getters.getUserInfo;
  let isUserMinorByTaxCode = getters.isUserMinorByTaxCode;

  // Se non abbiamo i dati di AURA => ci basiamo solo sul codice fiscale
  if (!userInfo) return isUserMinorByTaxCode;

  let birthDate = userInfo?.info_anag?.dati_primari?.data_nascita ?? null;

  // Se AURA non ci forinisce la data di nascita => ci basiamo solo sul codice fiscale
  if (!birthDate) return isUserMinorByTaxCode;

  let now = new Date();
  let maxDate = now;
  let minDate = subtractFromDate(now, { year: 18 });

  console.log({ minDate, maxDate, birthDate });
  return isBetweenDates(birthDate, minDate, maxDate, { onlyDate: true });
};

export const getUserInfo = state => {
  return state.userInfo;
};

export const getNotifyContacts = state => {
  return state.notifyContacts;
};

export const getContatti = state => {
  return state.contatti;
};

export const getWorkingApp = (state, getters) => {
  let appList = getters["getAppList"];
  return appList?.find(a => a.codice === APPLICATION_CODE);
};


