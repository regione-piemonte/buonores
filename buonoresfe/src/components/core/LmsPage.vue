<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <q-page v-bind="attrs" v-on="listeners" class="lms-page">
    <slot />
  </q-page>
</template>

<script>
export default {
  name: "LmsPage",
  inheritAttrs: false,
  computed: {
    listeners() {
      const { ...listeners } = this.$listeners;
      return listeners;
    },
    attrs() {
      const { ...attrs } = this.$attrs;
      return attrs;
    }
  }
};
</script>

<style lang="sass">
// Impostiamo una larghezza massima alle pagine
@media (min-width: $breakpoint-sm)
  .lms-page
    max-width: $breakpoint-sm
    margin: auto


@media (min-width: $breakpoint-md)
  .lms-page
    max-width: $breakpoint-md
    margin: auto
</style>
