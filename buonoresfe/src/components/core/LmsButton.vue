<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <q-btn v-bind="attrs" class="lms-button" :class="classes" v-on="listeners">
    <!-- Propaghiamo lo slot di default -->
    <slot />

    <!-- Propaghiamo i named slots -->
    <slot slot="loading" name="loading" />
  </q-btn>
</template>

<script>
export default {
  name: "LmsButton",
  inheritAttrs: false,
  props: {
    block: { type: Boolean, required: false, default: false },
    noMinWidth: { type: Boolean, required: false, default: false }
  },
  data() {
    return {};
  },
  computed: {
    listeners() {
      const { ...listeners } = this.$listeners;
      return listeners;
    },
    attrs() {
      // eslint-disable-next-line no-unused-vars
      const { ...attrs } = this.$attrs;
      attrs.color = attrs.color || "primary";
      return attrs;
    },
    classes() {
      return {
        "full-width": this.block,
        "lms-button--min-width": !this.noMinWidth
      };
    }
  },
  created() {},
  methods: {}
};
</script>

<style lang="sass">
.lms-button.disabled,
.lms-button[disabled]
  background-color: #c9cbcb !important
  color: #000 !important
  border: none
  box-shadow: none

.lms-button.focus,
.lms-button:focus
  outline: 3px solid #f3c716

.lms-button--min-width
  min-width: 200px
</style>
