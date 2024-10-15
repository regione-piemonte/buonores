/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import * as faker from "faker";
import MockAdapter from "axios-mock-adapter";
import { httpScelSoc, httpBuonoRes } from "boot/http";

export const fakeList = (fn, length = null) => {
  if (length === null) length = faker.random.number();
  return [...new Array(length)].map(() => fn());
};

// FAKE MODEL
// ---------------------------------------------------------------------------------------------------------------------
export const fakeModel = () => {
  return {
    id: faker.random.number(),
    codice: faker.random.arrayElement([null, "N", "P"]),
    descrizione: faker.random.sentences(),
    codice_fiscale: faker.random.alphaNumeric(16).toUpperCase(),
    nome: faker.name.firstName(),
    cognome: faker.name.lastName(),
    telefono: faker.phone.phoneNumber(),
    mail: faker.internet.email(),
    data: faker.date.recent(90)
  };
};

// CONTROLLERS
// ---------------------------------------------------------------------------------------------------------------------
export const getListExample = httpConfig => {
  let data = fakeList(fakeModel, 99);
  return [200, data];
};

export const getDetailExample = httpConfig => {
  let data = fakeModel();
  return [200, data];
};

export const postExample = httpConfig => {
  let data = fakeModel();
  return [201, data];
};

export const putExample = httpConfig => {
  let data = fakeModel();
  return [200, data];
};

export const deleteExample = httpConfig => {
  return [200, data];
};

// INTERCEPTOR AXIOS
// ---------------------------------------------------------------------------------------------------------------------
export const apiInterceptorMockJs = http => {
  faker.locale = "it";
  let mock = new MockAdapter(http, { delayResponse: 700 });

  mock.onGet(`/path`).reply(getListExample);
  mock.onGet(new RegExp(`/path/.+`)).reply(getDetailExample);
  mock.onPost(new RegExp(`/path`)).reply(postExample);
  mock.onPut(new RegExp(`/path/.+`)).reply(putExample);
  mock.onDelete(new RegExp(`/path/.+`)).reply(deleteExample);

  // Tutto ciò che non è mockato viene richiesto alle API
  mock.onAny().passThrough();
};

// INFINE LO COLLEGHIAMO ALLE ISTANZE AXIOS CHE CI INTERESSANO
// ---------------------------------------------------------------------------------------------------------------------
if (process.env.APP_API_MOCKS_JS_ENABLED) {
  [httpScelSoc, httpBuonoRes].forEach(http => apiInterceptorMockJs(http));
}
