<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <q-btn round @click="showMenu" :aria-label="'apri il menù contestuale per andare alla pagina di profilo di ' + name + ' ' + surname + ' e per uscire dall\'applicazione'" flat :ripple="false" unelevated>
  <q-avatar class="lms-layout-header-profile-button" >
    <div class="lms-layout-header-profile-button__text">
      <slot>{{ avatarText }}</slot>
    </div>

    <q-menu
      :value="isMenuVisible"
      class="lms-layout-header-profile-button__menu"
      @input="updateMenu"
    >
      <q-list style="min-width: 160px">
        <q-item>
          <q-item-section>
            <q-item-label> {{ name }} {{ surname }} </q-item-label>
            <q-item-label caption> {{ taxCode | empty }}</q-item-label>
          </q-item-section>
        </q-item>

        <q-item clickable @click="onClickProfile">
          <q-item-section side>
            <q-icon :name="iconProfile" />
          </q-item-section>

          <q-item-section>
            <q-item-label>Profilo</q-item-label>
          </q-item-section>
        </q-item>

        <q-item clickable @click="onClickLogout">
          <q-item-section side>
            <q-icon :name="iconLogout" />
          </q-item-section>

          <q-item-section>
            <q-item-label>Esci</q-item-label>
          </q-item-section>
        </q-item>
      </q-list>
    </q-menu>
  </q-avatar>
  </q-btn>
</template>

<script>
import { IS_PROD } from "src/services/config";
export default {
  name: "LmsLayoutHeaderProfileButton",
  props: {
    name: { type: String, required: false, default: "" },
    surname: { type: String, required: false, default: "" },
    taxCode: { type: String, required: false, default: "" },
    iconProfile: { type: String, required: false, default: "person" },
    iconLogout: { type: String, required: false, default: "exit_to_app" }
  },
  data() {
    return {
      isMenuVisible: false
    };
  },
  computed: {
    avatarText() {
      let n = this.name ? this.name.charAt(0) : "";
      let c = this.surname ? this.surname.charAt(0) : "";
      let result = `${n}${c}`;
      return result.trim();
    }
  },
  methods: {
    showMenu() {
      this.isMenuVisible = true;
    },
    hideMenu() {
      this.isMenuVisible = false;
    },
    updateMenu(val) {
      this.isMenuVisible = val;
    },
    onClickProfile() {
      this.hideMenu();
      let eventName = "click-profile";
      let url = (IS_PROD) ? 'https://www.sceltasociale.it/#/profilo' : 'https://tst-www-sceltasociale.portali.csi.it/#/profilo';

      if (eventName in this.$listeners) return this.$emit(eventName, url);

      window.location.assign(url);
    },
    onClickLogout() {
      this.hideMenu();
      let eventName = "click-logout";
      let url = (IS_PROD) ? 'https://buonores.sceltasociale.it/buonores_443s_liv3_sispliv2spid_gasprp_salute/logout.do' : "https://tst-buonores.sceltasociale.it/tst_buonores_443s_liv3_sispliv2spid_gasprp_salute/logout.do";

      if (eventName in this.$listeners) return this.$emit(eventName, url);

      window.location.assign(url);
    }
  }
};
</script>

<style lang="sass">
.lms-layout-header-profile-button
  background-color: $accent
  cursor: pointer

.lms-layout-header-profile-button
  font-size: 32px !important

.lms-layout-header-profile-button__text
  text-transform: uppercase
  font-size: 14px
</style>
