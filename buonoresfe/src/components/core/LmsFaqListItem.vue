<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <q-card class="lms-faq-list-item-wrapper">
    <q-expansion-item v-bind="attrs" v-on="listeners" class="lms-faq-list-item">
      <template v-slot:header>
        <q-item-section
          class="lms-faq-list-item__header__text text-body1 text-weight-bold"
        >
          <slot name="title">
            <div>
              <template v-if="number">
                <span class="q-mr-sm" style="white-space: pre"
                  >{{ number | padLeft }}.</span
                >
              </template>

              {{ title }}
            </div>
          </slot>
        </q-item-section>
      </template>

      <q-card class="lms-faq-list-item__body">
        <q-separator inset />
        <q-card-section>
          <slot />
        </q-card-section>
      </q-card>
    </q-expansion-item>
  </q-card>
</template>

<script>
export default {
  name: "LmsFaqListItem",
  inheritAttrs: false,
  props: {
    title: { type: String, required: false, default: "" },
    number: { type: [String, Number], required: false, default: "" }
  },
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
.lms-faq-list-item-wrapper:nth-of-type(2n) > .lms-faq-list-item
  border-left: 4px solid $indigo-6

.lms-faq-list-item-wrapper:nth-of-type(2n + 1) > .lms-faq-list-item
  border-left: 4px solid $teal-6

.lms-faq-list-item__header__number
  min-width: 32px
  align-items: flex-end
  font-weight: map-get($text-weights, 'bold')
  color: $primary
</style>
