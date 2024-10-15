/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { CaptureConsole } from "@sentry/integrations"
import {httpBuonoRes, httpScelSoc} from "src/boot/http";
import {APPLICATION_CODE} from "src/services/config";

const bffPath = 'buonoresbff/api/v1'
// const bffPath = 'buonoresbff-shib/api/v1'

// DA RIVEDERE
export const getUser = (httpConfig = {}) => {
  return httpScelSoc.get(`/bff/identity`, httpConfig);
};

export const getNotifyContacts = (taxCode, httpConfig = {}) => {
  const url = `/notify-preferences/api/v1/users/${taxCode}/contacts`;
  return httpScelSoc.get(url, httpConfig);
};

// API VERTICALE BOZZE

// RICHIESTE

// GET
export const getServizioAttivo = (httpConfig = {}) => {
  return httpBuonoRes.get(`${bffPath}/servizio-attivo`, httpConfig);
};

export const getSportelli = (httpConfig = {}) => {
  return httpBuonoRes.get(`${bffPath}/sportelli`, httpConfig);
};

export const getContatti = (httpConfig = {}) => {
  return httpBuonoRes.get(`${bffPath}/contatti`, httpConfig);
};

export const getPreferenze = (httpConfig = {}) => {
  return httpBuonoRes.get(`${bffPath}/preferenze`, httpConfig);
};

export const getRichieste = (httpConfig = {}) => {
  return httpBuonoRes.get(`${bffPath}/richieste`, httpConfig);
};

export const getRichiesta = (id, httpConfig = {}) => {
  return httpBuonoRes.get(`${bffPath}/richieste/${id}`, httpConfig);
};

// POST
export const postRichiesta = (payload, httpConfig = {}) => {
  const url = `${bffPath}/richieste`;
  return httpBuonoRes.post(url, payload, httpConfig);
};

// PUT
export const putRichiesta = (id,payload, httpConfig = {}) => {
  const url = `${bffPath}/richieste/${id}`;
  return httpBuonoRes.put(url, payload, httpConfig);
};

// CRONOLOGIA

// GET
export const getCronologia = (id, httpConfig = {}) => {
  return httpBuonoRes.get(`${bffPath}/cronologia/${id}`, httpConfig);
};

// POST
export const postCronologia = (id, payload, httpConfig = {}) => {
  const url = `${bffPath}/cronologia/${id}`;
  return httpBuonoRes.post(url, payload, httpConfig);
};

// ALLEGATI

// GET
export const getAllegato = (id, type, httpConfig = {}) => {
  return httpBuonoRes.get(`${bffPath}/allegato/${id}/${type}`, httpConfig);
};

// POST
export const postAllegato = (id, type, payload, httpConfig = {}) => {
  const url = `${bffPath}/allegato/${id}/${type}`;
  return httpBuonoRes.post(url, payload, httpConfig);
};

// ANAGRAFICA

// GET
export const getAnagrafica = (cf, httpConfig = {}) => {
  return httpBuonoRes.get(`${bffPath}/anagrafica/${cf}`, httpConfig);
};

// POST
export const postAnagrafica = (name, payload, httpConfig = {}) => {
  const url = `${bffPath}/anagrafica/${name}`;
  return httpBuonoRes.post(url, payload, httpConfig);
};

// RENDICONTAZIONE CONTRATTI E MONITORAGGIO

// CONTRATTI
//GET
export const getContratti = (id, httpConfig = {}) => {
  return httpBuonoRes.get(`${bffPath}/contratti/${id}`, httpConfig);
};

// POST
export const postContratti = (id, payload, httpConfig = {}) => {
  const url = `${bffPath}/contratti/${id}`;
  return httpBuonoRes.post(url, payload, httpConfig);
};

// PUT
export const putContratti = (id_domanda, id_contract, payload, httpConfig = {}) => {
  const url = `${bffPath}/contratti/${id_domanda}/${id_contract}`;
  return httpBuonoRes.put(url, payload, httpConfig);
};

// DELETE
export const deleteContratti = (id_domanda, id_contract, httpConfig = {}) => {
  const url = `${bffPath}/contratti/${id_domanda}/${id_contract}`;
  return httpBuonoRes.delete(url, httpConfig);
};

// CRONOLOGIA BUONO

// GET
export const getCronologiaBuono = (id, httpConfig = {}) => {
  return httpBuonoRes.get(`${bffPath}/cronologia-buono/${id}`, httpConfig);
};

// POST
export const postCronologiaBuono = (id, payload, httpConfig = {}) => {
  const url = `${bffPath}/cronologia-buono/${id}`;
  return httpBuonoRes.post(url, payload, httpConfig);
};

// ALLEGATI BUONO
//GET
export const getAllegatoBuono = (id, httpConfig = {}) => {
  return httpBuonoRes.get(`${bffPath}/rendicontazione/${id}`, httpConfig);
};


export const postAllegatoBuono = ( id, type, payload, httpConfig = {}) => {
  const url = `${bffPath}/allegato-buono/${id}/${type}`
  return httpBuonoRes.post(url, payload, httpConfig);
};

// DECODIFICHE

// GET
// /asl /titoli-studio /tipi-rapporto /tipi-contratto /tipi-allegato /stati-domanda /comuni /strutture
export const getDecodifiche= (name, httpConfig = {}) => {
  return httpBuonoRes.get(`${bffPath}/decodifiche/${name}`, httpConfig);
};

export const getStrutture= (comune, httpConfig = {}) => {
  return httpBuonoRes.get(`${bffPath}/decodifiche/strutture?comune=${comune}`, httpConfig);
};


// NOTIFICATORE
// ---------------------------------------------------------------------------------------------------------------------
export const getNotifyMessages = (taxCode, httpConfig = {}) => {
  const url = `/notify-messagestore/api/v1/users/${taxCode}/messages-lms`;
  return httpBuonoRes.get(url, httpConfig);
};

export const getNotifyMessagesUnseen = (taxCode, httpConfig = {}) => {
  const url = `/notify-messagestore/api/v1/users/${taxCode}/messages-unseen`;
  return httpBuonoRes.get(url, httpConfig);
};

export const updateMessagesAsSeen = (taxCode, payload, httpConfig = {}) => {
  const url = `/notify-messagestore/api/v1/users/${taxCode}/messages-unseen`;
  return httpBuonoRes.put(url, payload, httpConfig);
};

export const deleteMessage = (taxCode, id, httpConfig = {}) => {
  const url = `/notify-messagestore/api/v1/users/${taxCode}/messages/${id}`;
  return httpBuonoRes.delete(url, httpConfig);
};

export const updateNotifyContacts = (taxCode, payload, httpConfig = {}) => {
  const url = `/notify-preferences/api/v1/users/${taxCode}/contacts`;
  return httpBuonoRes.put(url, payload, httpConfig);
};

// ISEE
//GET
export const getConformitaIsee = (id, httpConfig = {}) => {
  return httpBuonoRes.get(`${bffPath}/isee/${id}`, httpConfig);
};

// POST
export const postConformitaIsee = (id, payload, httpConfig = {}) => {
  return httpBuonoRes.post(`${bffPath}/isee/${id}`, payload, httpConfig);
};
