<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div class="lms-the-guard-bootstrap">
    <!-- LOADING -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <template v-if="isLoading">
      <q-layout>
        <!-- APP HEADER -->
        <!-- ---------- -->
        <lms-layout-header menu />

        <!-- PAGE CONTAINER -->
        <!-- -------------- -->
        <q-page-container>
          <q-page
            class="flex flex-center"
            padding
            style="min-height: 0px; max-height: 200px"
          >
            <lms-inner-loading :showing="true" block />
          </q-page>
        </q-page-container>

        <!-- FOOTER -->
        <!-- ------ -->
        <lms-layout-footer no-help no-rating />
      </q-layout>
    </template>

    <!-- MANUTENZIONE -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <template v-else-if="isInMaintenance">
      <q-layout>
        <!-- APP HEADER -->
        <!-- ---------- -->
        <lms-layout-header />

        <!-- PAGE CONTAINER -->
        <!-- -------------- -->
        <q-page-container>
          <lms-page padding style="min-height: 0px;">
            <lms-maintenance />
          </lms-page>
        </q-page-container>

        <!-- FOOTER -->
        <!-- ------ -->
        <lms-layout-footer no-help no-rating />
      </q-layout>
    </template>

    <!-- AVVIAMO L'APP -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <template v-else>
      <slot />
    </template>
  </div>
</template>

<script>
import {
  getConfig,
  getUser,
  getNotifyContacts,
} from "src/services/api";
import { login } from "../services/utils";
import LmsLayoutHeader from "components/core/LmsLayoutHeader";
import LmsLayoutFooter from "components/core/LmsLayoutFooter";
import { date } from "quasar";
import LmsMaintenance from "./core/LmsMaintenance";


const { addToDate, subtractFromDate, isBetweenDates } = date;

export default {
  name: "TheGuardBootstrap",
  components: { LmsMaintenance, LmsLayoutFooter, LmsLayoutHeader },
  data() {
    return {
      isLoading: false,
      isMinor: false,
      hasNoDoctor: false
    };
  },
  computed: {
    user() {
      return this.$store.getters["getUser"];
    },
    userDoctor(){
      return this.$store.getters["getUserDoctor"];
    },
    workingApp() {
      return this.$store.getters["getWorkingApp"];
    },
    isInMaintenance() {
      // Se non riuscimo a trovare la configurazione => come fallback diciamo che non c'è manutenzione
      if (!this.workingApp) return false;

      let now = new Date();
      let startDate = this.workingApp?.manutenzione_data_inizio;
      let endDate = this.workingApp?.manutenzione_data_fine;

      // Se entrambe le date non sono popolate => non c'è manutenzione
      if (!startDate && !endDate) return false;

      startDate = startDate || subtractFromDate(now, { days: 1 });
      endDate = endDate || addToDate(now, { days: 1 });

      return isBetweenDates(now, startDate, endDate, {
        inclusiveFrom: true,
        inclusiveTo: true
      });
    },
    isUserMinorByTaxCode() {
      return this.$store.getters["isUserMinorByTaxCode"];
    },
    isUserMinor() {
      return this.$store.getters["isUserMinor"];
    },
  },
  async created() {
    this.isLoading = true;
    let configPromise = getConfig();
    let userPromise = getUser();

    // Prima di tutto carichiamo la configurazione generale di "La mia salute"
    // conterrà informazioni su tutte le applicazioni che compongono il portale, compresa questa che stiamo sviluppando
    // e conterrà informazioni relative a Firebase per l'integrazione delle notifiche push
    try {
      let { data: config } = await configPromise;
      await this.$store.dispatch("setConfig", { config });
    } catch (e) {
      console.error(e);
    }

    // Se l'applicazione è in manutenzione => non facciamo altre chiamate inutili
    if (this.isInMaintenance) {
      this.isLoading = false;
      return;
    }

    try {
      // Poi carichiamo le informazioni dell'utente
      // Questo passaggio ci permette di sapere se l'utente è loggato o meno.
      // Se le API rispondono con un errore => l'utente non è loggato
      let { data: user } = await userPromise;
      await this.$store.dispatch("setUser", { user });

      // Solo se l'utente è loggato carichiam//
      // 1.controllo che lutente non sia minorenne
      //  1. I contatti impostati sul notificatore
      //     Se questa chiamata fallisce significa che l'utente non ha
      //     ancora attivato il profilo del notificatore e quindi non può
      //     ricevere SMS, MAIL e Push
      //
      //  2. Informazioni più dettagliate sull'utente
      //     Se questa chiamata fallisce significa che l'utente non è un assistito piemontese
      //     e quindi alcune funzionalità potrebbero essere limitate
      //
      //  3. Informazioni relative all'apertura del FSE
      //
      //  4. Informazioni relative al medico

      let taxCode = user.cf;


      // Se l'utente sembra essere un minorenne in base al CF
      // => dobbiamo effettuare un controllo incrociato con i dati su AURA
      if (this.isUserMinorByTaxCode) {
        try {
          let { data: userInfo } = await getUserInfo(taxCode);
          await this.$store.dispatch("setUserInfo", { userInfo });
        } catch (e) {
          console.error(e);
        }
      }

      // Se l'utente sembra essere un minorenne in base al CF
      // => dobbiamo effettuare un controllo incrociato con i dati su AURA
      if (this.isUserMinor) {
        this.isLoading = false;
        return;
      }

      let contactsPromise = getNotifyContacts(taxCode);

      // Solo se l'applicazione su cui stiamo lavorando è segnata come "delegabile"
      // carichiamo l'elenco dei deleganti per permettere all'utente di operare
      // per conto di un suo delegante

      try {
        let { data: contacts } = await contactsPromise;
        await this.$store.dispatch("setNotifyContacts", { contacts });
      } catch (e) {
        console.error(e);
      }

    } catch (e) {
      console.error(e);
    }

    // Se l'utente non è loggato e l'applicazione attuale non è configurata come "pubblica"
    // cioè accessibile anche da utenti anonimi => mandiamo l'utente alla pagina di login
    if (!this.user && !this.workingApp?.pubblico) {
      login("/api/bff/login");
      return;
    }
    this.isLoading = false;
  }
};
</script>

<style scoped></style>
