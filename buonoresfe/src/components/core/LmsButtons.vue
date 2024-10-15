<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div class="lms-buttons row text-right q-gutter-sm" :class="classes">
    <slot />
  </div>
</template>

<script>
export default {
  name: "LmsButtons",
  components: {},
  props: {
    padding: { type: Boolean, required: false, default: false }
  },
  data() {
    return {};
  },
  computed: {
    classes() {
      return {
        "q-pa-md": this.padding,
        reverse: this.$q.screen.gt.xs
      };
    }
  },
  created() {},
  methods: {}
};
</script>

<style lang="sass">
.lms-buttons > .lms-button:not(.q-btn-round)
  width: 100%

@media (min-width: $breakpoint-sm-min)
  .lms-buttons > .lms-button:not(.q-btn-round)
    width: auto !important
</style>
