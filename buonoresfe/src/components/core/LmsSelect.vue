<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div class="lms-select">
    <q-field
      :label="label"
      :value="value"
      :for="labelFor"
      :bottom-slots="bottomSlots"
      :loading="loading"
      :lazy-rules="lazyRules"
      :rules="rules"
      :no-error-icon="noErrorIcon"
      :disable="disable"
      :filled="filled"
      :dense="dense"
      :label-slot="labelSlot"
    >
      <template v-slot:label :for="labelFor" v-if="labelSlot">
        <slot></slot>
      </template>

      <template #control>
        <select
          :value="value"
          :required="required"
          :name="name"
          :id="id"
          @input="onInput"
        >
          <option :value="null" :selected="value === null" disabled> - </option>
          <option
            v-for="option in options_"
            :key="option[optionValue]"
            :data-id="option.id_"
            :value="option[optionValue]"
            :selected="value === option[optionValue]"
          >
            <slot
              name="option"
              :option="option"
              :is-selected="value === option[optionValue]"
            >
              {{ option[optionLabel] }}
            </slot>
          </option>
        </select>
      </template>
    </q-field>
  </div>
</template>

<script>
import { uid } from "quasar";

export default {
  name: "LmsSelect",
  props: {
    value: { type: [String, Number, Object], required: false, default: null },
    options: { type: Array, required: false, default: () => [] },
    optionValue: { type: String, required: false, default: "codice" },
    optionLabel: { type: String, required: false, default: "etichetta" },
    label: { type: String, required: false, default: "" },
    required: { type: Boolean, required: false, default: false },
    name: { type: String, required: false, default: null },
    labelFor: { type: String, required: false, default: null },
    id: { type: String, required: false, default: null },
    bottomSlots: { type: Boolean, required: false, default: false },
    loading: { type: Boolean, required: false, default: false },
    lazyRules: { type: [Boolean, String], required: false, default: false },
    rules: { type: Array, required: false, default: () => [] },
    noErrorIcon: { type: Boolean, required: false, default: false },
    disable: { type: Boolean, required: false, default: false },
    filled: { type: Boolean, required: false, default: false },
    dense: { type: Boolean, required: false, default: false },
    labelSlot: { type: Boolean, required: false, default: false }
  },
  data() {
    return {};
  },
  computed: {
    options_() {
      return this.options.map(el => ({
        id_: uid(),
        ...el
      }));
    }
  },
  created() {},
  methods: {
    onInput(event) {
      // Facciamo questo giro perché vogliamo restituire il tipo di dato corretto.
      // Es. se il value è un intero, senza questo hack restituiremmo una stringa dal momento che
      //     l'attributo value del DOM salva le informazioni come stringhe
      let el = event?.target ?? null;
      el = el?.options?.[el?.selectedIndex];
      let id = el?.dataset?.id ?? null;
      let option = this.options_.find(el => el.id_ === id);
      let value = option?.[this.optionValue] ?? null;
      this.$emit("input", value, event);

      // let value = event?.target?.value ?? null;
      // this.$emit("input", value, event);
    }
  }
};
</script>

<style lang="scss">
.lms-select {
  select {
    display: block;
    width: 100%;
    // padding: 8px 8px 8px 0;
    border: none;
    // border-bottom: 1px solid black;
    background-color: transparent;

    &:focus {
      outline: none;
      // border-bottom: 2px solid $primary;
    }
  }
}
</style>
