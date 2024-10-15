<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <q-btn
    v-bind="attrs"
    v-on="listeners"
    class="lms-header-login-button"
    @click="onClick"
  >
    Accedi
  </q-btn>
</template>

<script>
export default {
  name: "LmsLayoutHeaderLoginButton",
  inheritAttrs: false,
  computed: {
    attrs() {
      const { ...attrs } = this.$attrs;
      if (!("flat" in attrs)) attrs.flat = true;
      if (!("dense" in attrs)) attrs.dense = true;
      return attrs;
    },
    listeners() {
      const { click, ...listeners } = this.$listeners;
      return listeners;
    }
  },
  methods: {
    onClick() {
      let eventName = "click";
      let landingUrl = encodeURIComponent(window.location.pathname);
      let url = "/api/bff/login?landingUrl=" + landingUrl;

      if (eventName in this.$listeners) return this.$emit(eventName, url);

      window.location.assign(url);
    }
  }
};
</script>
