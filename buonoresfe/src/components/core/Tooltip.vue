<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div style="display: inline-flex;">
    <q-btn round color="primary" icon="info" flat @click="alert = true" :aria-label="'maggiori informazioni su ' + titleBanner" :title="'clicca per maggiori info su ' + titleBanner"/>
    <q-dialog v-model="alert">
      <q-card style="min-width: 300px;">
        <q-card-section>
          <lms-banner type="info">
            <h2>{{ titleBanner }}</h2>
            <p v-html="textBanner"></p>
          </lms-banner>
        </q-card-section>

        <q-card-actions style="align-content: end;">
          <q-btn flat label="Chiudi" color="primary" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>

const VALID_TYPES = ['info', 'warning', 'positive', 'negative'];
import LmsBanner from "components/core/LmsBanner";
export default {
  name: "Tooltip",
  components: {LmsBanner},
  props: {
    titleBanner: {type: String, default: ''},
    textBanner: {type: String, default: ''}
  },
  data() {
    return {
      alert: false,
    }
  }
}
</script>