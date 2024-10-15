/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import axios from "axios";
import { apiInterceptorRequestId } from "src/services/utils";

export const httpScelSoc = axios.create({
  baseURL: process.env.APP_API_BASE_URL_SCELSOC
});

export const httpBuonoRes = axios.create({
  baseURL: process.env.APP_API_BASE_URL_BUONORES
});

[httpScelSoc, httpBuonoRes].forEach(http => apiInterceptorRequestId(http));
