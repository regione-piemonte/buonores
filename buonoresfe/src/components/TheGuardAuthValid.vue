<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div class="lms-the-guard-auth-valid">
    <template v-if="isAuthValid">
      <slot />
    </template>

    <template v-else>
      <q-layout>
        <lms-layout-header />

        <q-page-container>
          <lms-page padding>
            <q-banner rounded class="bg-info text-black">
              <div>
                Siamo spiacenti ma le tue credenziali non sono abilitate ad
                accedere a questo servizio.
                <br />
                <br />
                <a :href="URL">
                  <strong>Scopri come attenere le nuove credenziali</strong>
                </a>
              </div>
            </q-banner>
          </lms-page>
        </q-page-container>

        <lms-layout-footer />
      </q-layout>
    </template>
  </div>
</template>

<script>
import LmsLayoutHeader from "./core/LmsLayoutHeader";
import LmsLayoutFooter from "./core/LmsLayoutFooter";

const URL =
  "https://www.regione.piemonte.it/web/temi/sanita/accesso-ai-servizi-sanitari/cosa-serve-per-accedere-ai-servizi-on-line";

export default {
  name: "TheGuardAuthValid",
  components: { LmsLayoutFooter, LmsLayoutHeader },
  data() {
    return {
      URL
    };
  },
  computed: {
    user() {
      return this.$store.getters["getUser"];
    },
    isAuthValid() {
      if (!this.user) return true;
      return this.user && this.user.autenticazione_valida;
    }
  }
};
</script>

<style scoped></style>
