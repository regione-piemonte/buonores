<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div class="lms-maintenance">
    <div class="row items-center justify-around q-mt-lg" >
      <div class="col-10 col-sm-8 col-md-6 col-lg-4">
        <img
          src="/statics/la-mia-salute/immagini/manutenzione.svg"
          alt="Manutenzione"
          class="responsive"
        />
      </div>
    </div>

    <h1 class="text-h4 text-primary text-center q-my-md">
      Servizio non disponibile
    </h1>

    <p class="text-center text-body1">
      Servizio al momento non disponibile a causa di un intervento di
      manutenzione. <br />
      Ti consigliamo di riprovare più tardi. Ci scusiamo per il disagio.
    </p>

    <div class="q-my-xl text-center">
      <lms-button @click="goToHome">Torna alla Home</lms-button>
    </div>
  </div>
</template>

<script>

export default {
  name: "LmsMaintenance",
  inheritAttrs: false,
  computed: {},
  methods:{
    goToHome(){
      let homeUrl = "/la-mia-salute/";
      window.location.assign(homeUrl);
    }
  }
};
</script>
