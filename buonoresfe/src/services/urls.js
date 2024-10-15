/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { store } from "src/store";

export const appDetailFaq = () => {
  let workingApp = store.getters["getWorkingApp"];
  let appCode = workingApp?.portale_codice ?? null;
  let url = "https://dev-www-salute-piemonte.portali.csi.it/assistenza";
  if (process.env.APP_IS_PROD) url = "https://www.salutepiemonte.it/assistenza";
  url = `${url}?codice=${appCode}`;
  return url;
};

export const privacyPolicy = () => {
  let url = "https://www.sceltasociale.it/#/note-legali-privacy";
  return url;
};

export const cookiePolicy = () => {
  let url = "https://www.sceltasociale.it/#/cookie-policy";
  return url;
};

export const urlPiemonteTu = () => {
  let user = store.getters["getUser"];
  return user
    ? "https://www.piemontetu.it/api/auth/login?redirect=true"
    : "https://www.piemontetu.it/";
};

export const appAssistanceTree = (appCode) => {
  let url =`/la-mia-salute/assistenza/#/percorsi-guidati/${appCode}`
  return url;
};

export const appAssistanceForm = (appCode) => {
  let url =`/la-mia-salute/assistenza/#/richieste/nuova/${appCode}`
  return url;
};
