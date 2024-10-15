<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <q-btn
    flat
    round
    dense
    icon="notifications"
    class="lms-layout-header-notification-button"
    v-on="listeners"
    aria-label="notifiche"
  >
    <template v-if="unseenCount > 0">
      <q-badge
        color="red"
        floating
        class="lms-layout-header-notification-button__badge"
      >
        {{ unseenCount }}
      </q-badge>
    </template>
  </q-btn>
</template>

<script>
export default {
  name: "LmsLayoutHeaderNotificationButton",
  inheritAttrs: false,
  props: {
    unseenCount: { type: [Number, String], required: false, default: 0 }
  },
  data() {
    return {};
  },
  computed: {
    listeners() {
      const { ...listeners } = this.$listeners;
      return listeners;
    }
  },
  methods: {}
};
</script>
