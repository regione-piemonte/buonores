<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <q-bar class="lms-layout-delegator-toolbar">
    <div class="white-space-default">
      <slot>
        Stai utilizzando il servizio per conto di
        <span class="text-bold"> {{ lastName }} {{ firstName }} </span>
      </slot>
    </div>

    <q-space />

    <q-btn flat round dense icon="close" @click="$emit('close')"  aria-label="cancella"/>
  </q-bar>
</template>

<script>
export default {
  name: "LmsLayoutDelegatorToolbar",
  props: {
    firstName: { type: String, required: false, default: "" },
    lastName: { type: String, required: false, default: "" }
  }
};
</script>

<style lang="sass">
.lms-layout-delegator-toolbar
  min-height: 32px
  height: auto
  background-color: $lime-14
  color: #000
</style>
