/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import VueSync from "vue-sync";
import LmsButton from "components/core/LmsButton";
import LmsButtons from "components/core/LmsButtons";
import LmsInnerLoading from "components/core/LmsInnerLoading";
import LmsPage from "components/core/LmsPage";
import LmsPageTitle from "components/core/LmsPageTitle";
import Vuelidate from 'vuelidate'
import moment from 'moment';

export default ({ app, router, store, Vue }) => {
  Vue.use(VueSync);
  Vue.use(Vuelidate);
  Vue.use(moment);
  Vue.component(LmsButton.name, LmsButton);
  Vue.component(LmsButtons.name, LmsButtons);
  Vue.component(LmsInnerLoading.name, LmsInnerLoading);
  Vue.component(LmsPage.name, LmsPage);
  Vue.component(LmsPageTitle.name, LmsPageTitle);
};




