<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div class="lms-page-title row items-center no-wrap">
    <div class="col-auto">
      <slot name="back">
        <q-btn
          v-if="!noBack"
          flat
          round
          dense
          icon="arrow_back"
          class="q-mr-xs"
          @click="onBack"
          aria-label="ANNULLA"
        />
      </slot>
    </div>

    <div class="col">
      <h1 class="text-h5 text-bold q-my-none">
        <slot />
      </h1>
    </div>
  </div>
</template>

<script>
export default {
  name: "LmsPageTitle",
  props: {
    noBack: { type: Boolean, default: false }
  },
  data() {
    return {};
  },
  methods: {
    onBack(...args) {
      let eventName = "back";
      if (eventName in this.$listeners) return this.$emit(eventName, ...args);
      console.debug(`default @${eventName}`);
      this.$emit('back')

      //this.$router.back();
    }
  }
};
</script>
