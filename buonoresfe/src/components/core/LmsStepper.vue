<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <q-stepper
    ref="stepper"
    v-bind="attrs"
    v-on="listeners"
    class="lms-stepper"
    @transition="onStep"
  >
    <slot />
  </q-stepper>
</template>

<script>
export default {
  name: "LmsStepper",
  props: {},
  computed: {
    listeners() {
      const { transition, ...listeners } = this.$listeners;
      return listeners;
    },
    attrs() {
      const { ...attrs } = this.$attrs;
      return attrs;
    }
  },
  methods: {
    getActiveStep() {
      return this.$refs.stepper.$el.querySelector(".q-stepper__tab--active");
    },
    scrollToElement(element) {
      element.scrollIntoView(true);
    },
    onStep(event) {
      this.$emit("transition", event);
      let step = this.getActiveStep();
      this.scrollToElement(step);
    },
    next() {
      this.$refs.stepper.next();
    },
    previous() {
      this.$refs.stepper.previous();
    },
    goTo(name) {
      this.$refs.stepper.goTo(name);
    }
  }
};
</script>
