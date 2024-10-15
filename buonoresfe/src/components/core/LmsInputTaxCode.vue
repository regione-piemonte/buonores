<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <q-input
    v-bind="attrs"
    v-on="listeners"
    name="tax-code"
    class="lms-input-tax-code"
    @input="onInput"

  >
    <slot />
    <slot slot="prepend" name="prepend" />
    <slot slot="append" name="append" />
    <slot slot="before" name="before" />
    <slot slot="after" name="after" />
    <slot slot="error" name="error" />
    <slot slot="hint" name="hint" />
    <slot slot="counter" name="counter" />
    <slot slot="loading" name="loading" />
  </q-input>
</template>

<script>
import { LENGTH } from "src/services/tax-code";

export default {
  name: "LmsInputTaxCode",
  inheritAttrs: false,
  data(){
    return{
      LENGTH
    }
  },
  computed: {
    listeners() {
      const { input, ...listeners } = this.$listeners;
      return listeners;
    },
    attrs() {
      const { ...attrs } = this.$attrs;
      if (!("type" in attrs)) attrs.type = "text";
      if (!("label" in attrs)) attrs.label = "Codice fiscale";
      if (!("maxlength" in attrs)) attrs.maxlength = LENGTH;
      if (!("upperCase" in attrs)) attrs.upperCase = true;
      if (!("autocomplete" in attrs)) attrs.autocomplete = "on";
      if (!("counter" in attrs)) attrs.counter = true;
      return attrs;
    }
  },
  methods: {
    onInput(val) {
      let emitValue = val ? val.toUpperCase() : "";
      this.$emit("input", emitValue);
    }
  }
};
</script>
