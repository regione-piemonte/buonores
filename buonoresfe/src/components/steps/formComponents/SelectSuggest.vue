<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <q-select
    filled
    v-model="model"
    use-input
    input-debounce="0"
    :label="label"
    :options="listOptions"
    @filter="filterFn"
    :option-label="column ? column : null"
    option-disable="inactive"
    emit-value
    map-options
    clearable
  >
    <template v-slot:no-option>
      <q-item>
        <q-item-section class="text-grey"> Nessun risultato </q-item-section>
      </q-item>
    </template>
  </q-select>
</template>
<script>
export default {
  name: "SelectSuggest",
  props: {
    options: Array,
    field: String,
    subField: String,
    subFieldChild: String,
    column: String,
    label: String,
  },
  data() {
    return {
      model: null,
      listOptions: this.options
    };
  },

  methods: {
    filterFn(val, update) {
      if (val === "") {
        update(() => {
          this.listOptions = this.options;
        });
        return;
      }

      update(() => {
        const needle = val.toLowerCase();
        if (this.column) {
          this.listOptions = this.options.filter(
            (v) => v[this.column].toString().toLowerCase().indexOf(needle) > -1
          );
        } else {
          this.listOptions = this.options.filter(
            (v) => v.toString().toLowerCase().indexOf(needle) > -1
          );
        }
      });
    },
  },
  computed: {
    value() {
      return this.model ? this.model : null;
    },

    listValues() {
      return this.options;
    },
  },
  watch: {
    listValues() {
      this.model = null;
    },
    value() {
      let obj = {
        field: this.field,
        subField:this.subField,
        subFieldChild: this.subFieldChild,
        model: this.model,
      };
      this.$emit("setValue", obj);
    },
  },
};
</script>
