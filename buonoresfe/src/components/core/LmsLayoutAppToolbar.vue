<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <q-toolbar class="">
    <slot name="left" />

    <q-toolbar-title
      class="lms-layout-app-toolbar__title"
      :class="{'col-10': $q.screen.lt.sm}"
      shrink
      @click.native="onClick"
    >
      <h1>
      <slot />
    </h1>
    </q-toolbar-title>

    <q-space />

    <slot name="right" />
  </q-toolbar>
</template>

<script>
export default {
  name: "LmsLayoutAppToolbar",
  methods: {
    onClick() {
      let eventName = "click-title";
      let url = window.location.origin + window.location.pathname + '#/';

      if (eventName in this.$listeners) return this.$emit(eventName, url);

      window.location.assign(url);
    }
  }
};
</script>
