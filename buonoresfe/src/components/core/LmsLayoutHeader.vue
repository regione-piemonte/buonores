<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <q-header v-bind="attrs" v-on="listeners" class="lms-layout-header">
    <slot name="before" />
    <div class="container">
    <q-toolbar>
      <slot name="left">
        <a
        :href="linkPortale"
        class="lms-layout-header__toolbar__title"
        aria-label="Torna alla homepage del Portale Scelta sociale"
      >
        <img
          src="../../../public/img/scelta-sociale/logo-scelta-sociale.png"
          alt="Logo Scelta Sociale"
          class="logo-portale"
        />
      </a>
      </slot>




      <q-space />

      <slot name="right" />
    </q-toolbar>
  </div>
    <slot name="after" />
  </q-header>
</template>

<script>
import { IS_PROD } from "src/services/config";
export default {
  name: "LmsLayoutHeader",
  inheritAttrs: false,
  props: {
    menu: { type: Boolean, required: false, default: false }
  },
  computed: {
    listeners() {
      const { ...listeners } = this.$listeners;
      return listeners;
    },
    attrs() {
      const { ...attrs } = this.$attrs;
      if (!("reveal" in attrs)) attrs.reveal = true;
      if (!("elevated" in attrs)) attrs.elevated = true;
      return attrs;
    },
    linkPortale() {
      return (IS_PROD) ? 'https://www.sceltasociale.it/#/' : 'https://tst-www-sceltasociale.portali.csi.it/#/';
    },
  },
};
</script>

<style lang="sass">
.lms-layout-header__title
  cursor: pointer

.lms-layout-header__toolbar__title
  max-width: 250px
</style>
