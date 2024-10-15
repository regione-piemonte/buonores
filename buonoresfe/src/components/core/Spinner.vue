<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div>
    <lms-banner type="info">
      <h2>Caricamento in corso</h2>
      <p>Attendere il caricamento della pagina di Scelta sociale - Buono residenzialità</p>
    </lms-banner>

    <div class="overlay-spinner">
      <div class="spinner-container">
        <q-spinner color="primary" size="10em" />
      </div>
    </div>
  </div>
</template>

<script>

import LmsBanner from 'components/core/LmsBanner.vue';
export default {
  name: "Spinner",
  components: {
    LmsBanner
  },
}
</script>
