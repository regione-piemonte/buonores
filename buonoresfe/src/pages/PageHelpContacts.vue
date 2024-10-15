<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <lms-page padding>
    <lms-page-title>Assistenza</lms-page-title>
    <div class="q-py-lg">
      <q-card>
        <q-card-section>
          <div class="row items-center">
            <div class="col-12 col-sm-6 col-lg-4 gt-sm q-pr-md">
              <q-img
                src="/statics/la-mia-salute/immagini/assistenza.svg"
                alt="Banner assistenza"
                basic
              />
            </div>

            <div class="col-12 col-sm-6 col-lg-8 q-pa-sm">
              <p>Hai bisogno di aiuto?</p>
              <p>
                Scrivi a
                <a href="mailto:assistenza.salute@csi.it" class="lms-link"
                >assistenza.salute@csi.it</a
                >
                specificando:
              </p>
              <ul>
                <li>Nome e cognome</li>
                <li>Recapito telefonico</li>
                <li>Codice Fiscale</li>
                <li>Tipo di esame eseguito (laboratorio, radiologia)</li>
                <li>Struttura presso la quale hai eseguito l'esame</li>
                <li>Data in cui hai effettuato l'esamee</li>
                <li>Tipo di problema riscontrato</li>
              </ul>


            </div>
          </div>
        </q-card-section>
        <q-card-section class="q-pt-sm">
          <q-banner class="bg-blue-2" >
            <div class="text-body1 text-bold">
              Attenzione! La casella offre solo supporto tecnico, perciò ti invitiamo a non utilizzarla per comunicare informazioni personali di tipo sanitario
            </div>
          </q-banner>
        </q-card-section>
      </q-card>
    </div>
  </lms-page>
</template>

<script>
export default {
  name: "PageHelpContacts",
  props: {},
  data() {
    return {};
  },
  computed: {},
  created() {},
  methods: {}
};
</script>

<style scoped lang="sass"></style>
