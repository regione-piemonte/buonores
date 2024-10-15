<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<!--
Perchè c'è bisogno di questo componente ?

La nuova QCheckbox non supporta icone custom.
Finchè non verrà reinserita questa feature in quasar, usiamo questo componente custom per tutte quelle
checkbox che richiedono delle icone custom
-->

<template>
  <div class="lms-checkbox-button">
    <q-btn
      flat
      round
      dense
      :icon="icon"
      :color="finalColor"
      :disable="disable"
      @click="onClick"
    />

    <slot />
  </div>
</template>

<script>
export default {
  name: "LmsCheckboxButton",
  props: {
    value: { required: true },
    toggleIndeterminate: { type: Boolean, required: false, default: false },
    uncheckedValue: { required: false, default: false },
    checkedValue: { required: false, default: true },
    indeterminateValue: { required: false, default: null },
    uncheckedIcon: {
      type: String,
      required: false,
      default: "check_box_outline_blank"
    },
    checkedIcon: { type: String, required: false, default: "check_box" },
    indeterminateIcon: {
      type: String,
      required: false,
      default: "indeterminate_check_box"
    },
    uncheckedColor: { type: String, required: false, default: "black" },
    checkedColor: { type: String, required: false, default: "primary" },
    indeterminateColor: { type: String, required: false, default: "primary" },
    disable: { type: Boolean, required: false, default: false }
  },
  data() {
    return {};
  },
  computed: {
    isValueArray() {
      return Array.isArray(this.value);
    },
    isChecked() {
      if (!this.isValueArray) return this.value === this.checkedValue;
      return this.value.includes(this.checkedValue);
    },
    isIndeterminate() {
      return this.value === this.indeterminateValue;
    },
    isUnchecked() {
      return !this.isChecked && !this.isIndeterminate;
    },
    icon() {
      if (this.isChecked) return this.checkedIcon;
      if (this.isUnchecked) return this.uncheckedIcon;
      return this.indeterminateIcon;
    },
    finalColor() {
      if (this.isChecked) return this.checkedColor;
      if (this.isIndeterminate) return this.indeterminateColor;
      return this.uncheckedColor;
    }
  },
  created() {},
  methods: {
    getNewValueArray() {
      if (this.isUnchecked) return [...this.value, this.checkedValue];
      return this.value.filter(v => v !== this.checkedValue);
    },
    getNewValue() {
      // macchina stati
      // if(toggleIndeterminate) => checked -> indeterminate -> unchecked -> checked
      // if(!toggleIndeterminate) => checked -> unchecked -> cheched
      if (this.isUnchecked) return this.checkedValue;
      if (this.isIndeterminate) return this.uncheckedValue;
      return this.toggleIndeterminate
        ? this.indeterminateValue
        : this.uncheckedValue;
    },
    onClick() {
      let value = this.isValueArray
        ? this.getNewValueArray()
        : this.getNewValue();

      this.$emit("input", value);
    }
  }
};
</script>

<style lang="sass">
.lms-checkbox-button
  display: inline-block
</style>
