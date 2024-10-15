<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <lms-page padding>
    <lms-page-title>Contatti</lms-page-title>

    <!--    <q-tabs indicator-color="primary" active-color="primary" align="left">-->
    <!--      <q-route-tab :to="HELP_CONTACTS" label="Contatti" exact />-->
    <!--      <q-route-tab :to="HELP_FAQ" label="FAQ" exact />-->
    <!--    </q-tabs>-->

    <router-view />
  </lms-page>
</template>

<script>
import { HELP_CONTACTS, HELP_FAQ } from "src/router/routes";

export default {
  name: "PageHelp",
  props: {},
  data() {
    return {
      HELP_CONTACTS,
      HELP_FAQ
    };
  },
  computed: {},
  created() {},
  methods: {}
};
</script>

<style scoped lang="sass"></style>
