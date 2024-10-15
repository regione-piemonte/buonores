/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import Vue from "vue";
import Vuex from "vuex";
import * as getters from "src/store/getters";
import * as mutations from "src/store/mutations";
import * as actions from "src/store/actions";
import state from "src/store/state";

Vue.use(Vuex);

export const store = new Vuex.Store({
  getters,
  mutations,
  actions,
  state,
  // enable strict mode (adds overhead!)
  // for dev mode only
  strict: process.env.DEV
});

export default store;
