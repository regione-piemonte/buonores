/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { date as _date, format } from "quasar";
import { sentenceCase } from "change-case";
import {
  DATETIME_MESSAGES_FORMAT,
  DEFAULT_FORMAT_DATE,
  DEFAULT_FORMAT_DATETIME,
  DEFAULT_FORMAT_TIME
} from "src/services/utils";

const { formatDate } = _date;
const { pad } = format;

export const date = (v, format = DEFAULT_FORMAT_DATE) => {
  return v ? formatDate(v, format) : "";
};

export const time = (v, format = DEFAULT_FORMAT_TIME) => {
  return v ? formatDate(v, format) : "";
};

export const datetime = (v, format = DATETIME_MESSAGES_FORMAT) => {
  return v ? formatDate(v, format) : "";
};

export const empty = (v, fallback = "-") => {
  return v || fallback;
};

export const trimRight = (v, toTrim = "s") => {
  let pattern = "(" + toTrim + ")+$";
  let regex = new RegExp(pattern, "g");
  return v.replace(regex, "");
};

export const trimLeft = (v, toTrim = "s") => {
  let pattern = "^(" + toTrim + ")+";
  let regex = new RegExp(pattern, "g");
  return v.replace(regex, "");
};

export const padLeft = (v, length = 2, char = " ") => {
  return pad(v, length, char);
};

export const number = v => {
  return Number(v).toLocaleString();
};

export const toFixed = (v, digit = 2) => {
  return Number(v).toFixed(digit);
};

export const decimals = (v, decimals = 2) => {
  return Number(v).toFixed(decimals);
};

export const caseSentence = (v) => {
  return sentenceCase(v);
};

export const truncate = (v, length) => {
  if (!v) return "";
  v = v.toString();
  if (v.length > length) {
    return v.substring(0, length) + "...";
  } else {
    return v;
  }
};

export default ({ app, router, store, Vue }) => {
  // Installazione filtri
  Vue.filter("date", date);
  Vue.filter("time", time);
  Vue.filter("datetime", datetime);

  Vue.filter("empty", empty);
  Vue.filter("trimRight", trimRight);
  Vue.filter("trimLeft", trimLeft);
  Vue.filter("padLeft", padLeft);

  Vue.filter("number", number);
  Vue.filter("toFixed", toFixed);
  Vue.filter("decimals", decimals);

  Vue.filter("caseSentence", caseSentence);
  Vue.filter("truncate", truncate);

};
