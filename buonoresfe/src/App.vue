<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div id="q-app">
    <!-- <the-guard-bootstrap>
      <the-guard-auth-valid>
         -->
         <the-guard-fcm>
            <router-view />
            </the-guard-fcm>
        <!-- 
      </the-guard-auth-valid>
    </the-guard-bootstrap> -->
  </div>
</template>
<script>
// import TheGuardBootstrap from "src/components/TheGuardBootstrap";
import TheGuardFcm from "./components/TheGuardFcm";
// import TheGuardAuthValid from "./components/TheGuardAuthValid";

import {
  getServizioAttivo,
} from "src/services/api";
import {version} from '../package'

export default {
  name: "App",
  components: {
    // TheGuardAuthValid,
  TheGuardFcm,
    // TheGuardBootstrap
  },
  async created() {
    
    // console.log('versione ', version)
    // npm version patch/minor/major per aumentare il package json dopo la commit
  }
};
</script>
