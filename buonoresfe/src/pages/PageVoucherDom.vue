<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <lms-page>
   <stepper-bonus></stepper-bonus>
  </lms-page>
</template>

<script>
import StepperBonus from "components/steps/StepperBonus";
export default {
  name: "PageVoucherDom",
  components: {
    StepperBonus
  },
  data() {
    return {
      step: 1,
      name: "Guglielmo Rossi",
      isLoading: false,
      tab: "richieste",
    };
  },
  computed: {
    
  },
  methods: {},
  async created() {},
};
</script>

<style lang="sass">
    
</style>
