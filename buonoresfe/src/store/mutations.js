/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export const SET_CONFIG = (state, { config }) => {
  state.config = config;
};

export const SET_USER = (state, { user }) => {
  state.user = user;
};

export const SET_USER_INFO = (state, { userInfo }) => {
  state.userInfo = userInfo;
};

export const SET_NOTIFY_CONTACTS = (state, { contacts }) => {
  state.notifyContacts = contacts;
};

export const SET_CONTATTI = (state, { contatti }) => {
  state.contatti = contatti;
};

export const SET_PREFERENZE = (state, { preferenze }) => {
  state.preferenze = preferenze;
};


// METODI NUOVI BONODOM

export const SET_SPINNER = (state, { spinner }) => {
  state.spinner = spinner;
};

export const SET_SPINNER_STEPPER = (state, { spinner_stepper }) => {
  state.spinner_stepper = spinner_stepper;
};

export const SET_UTENTE_RICHIEDENTE = (state, { utente_richiedente }) => {
  state.utente_richiedente = utente_richiedente;
};

export const SET_UTENTE_DESTINATARIO = (state, { utente_destinatario }) => {
  state.utente_destinatario = utente_destinatario;
};

export const SET_SPORTELLI = (state, { sportelli }) => {
  state.sportelli = sportelli;
};


export const SET_OPTIONS = (state, payload) =>{
  state[payload.name] = payload.value
};


export const SET_STEPPER_LIST = (state, { stepperList }) => {
  state.stepperList = stepperList;
};

export const SET_STEP = (state, { step }) => {
  state.step = step;
};
// NEXT STEP GESTISCE IL BLOCCO SUL PUSANTE AVANTI USIAMO STEP_1, STEP_2 ETC.. DELLO STORE COME RIFERIMENTO
export const SET_NEXT_STEP = (state, payload) =>{
  state['step_' + payload.step] = !payload.bool
};

export const RESET_ALL_STEP = (state) =>{
  state.step = 0
  state.step_0 = false
  state.step_1 = false
  state.step_2 = false
  state.step_3 = true
};

export const BOZZA_ALL_STEP = (state) =>{
  state.step = 2
  state.step_0 = true
  state.step_1 = true
  state.step_2 = true
  state.step_3 = true
};
export const SET_RICHIESTA = (state, richiesta) =>{
  state.richiesta = {...state.richiesta, ...richiesta}
};

export const SET_STEPPER_FORM = (state, stepperForm) =>{
  state.stepperForm = {...state.stepperForm, ...stepperForm}
};

// STEP 2 SETTA SOLO IL RADIO INZIALE
export const SET_DESTINATARIO_RADIO = (state, destinatario_radio) =>{
  state.destinatario_radio = destinatario_radio
};

export const SET_SHOW_DESTINATARIO = (state, show_destinatario) =>{
  state.show_destinatario = show_destinatario
};

  
export const SET_PRIVACY = (state, value ) => {
  state.privacy_toogle = value;
};

export const SET_AGREMENT = (state,  value) => {
  state.consenso_toogle = value;
};
// FINE

//TOKEN FIREBASE
export const SET_FCM_TOKEN = (state, { fcmToken }) => {
  state.fcmToken = fcmToken;
};


