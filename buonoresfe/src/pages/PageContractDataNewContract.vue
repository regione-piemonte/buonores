<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div>
    <div class="q-page lms-page">
      <BreadcrumbsCustom
        :name="utenteDestinatario.nome + ' ' + utenteDestinatario.cognome"
        :path1="'gestione'"
        :label1="'Gestione'"
        :path2="'daticontratto'"
        :label2="'Dati del contratto'"
        :path3="'nuovocontratto'"
        :label3="'Nuovo contratto'"
        :title="'Nuovo contratto'"
        :id="idRichiesta"
      ></BreadcrumbsCustom>
      <ContractData
        @validare="validare($event)"
        @richiedere="richiedere($event)"
        @allegare="allegare($event)"
        :destinatario="utenteDestinatario"
        :richiedente="utenteRichiedente"
      ></ContractData>
      <!-- PULSANTI -->
      <div class="row button-action-stepper q-my-xxl">
        <div class="col-12 col-md second-block">
          <div class="row justify-end q-gutter-y-lg">
            <div class="col-auto">
              <q-btn
                :disable="validForm !== false"
                class="q-ml-xl"
                color="primary"
                @click="inviaContratto()"
                label="INSERISCI CONTRATTO"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import store from "src/store/index";
import {
  postContratti,
  postAllegatoBuono,
} from "src/services/api";

import { snackbarError } from "src/services/utils";
import BreadcrumbsCustom from 'src/components/reporting/BreadcrumbsCustom.vue';
import ContractData from 'src/components/reporting/ContractData.vue';


export default {
  name: "PageContractDataNewContract",
  components: { ContractData, BreadcrumbsCustom },
  data () {
    return {
      allegatiOk: true,
      idRichiesta: null,
      valid: null,
      allegati: null
    }
  },
  methods: {
    async goHome () {
      if (this.$route.name !== "personalarea") {
        this.$router.push("/");
      }
    },
    validare (value) {
      this.valid = value
    },
    richiedere (item) {
      this.richiestaContratto = item.richiesta
    },
    allegare (item) {
      this.allegati = item
    },
    async postAllegatoBuonoSingolo (type) {
      try {
        let { data } = await postAllegatoBuono(
          this.idRichiesta,
          type,
          this.allegati[type],
          {
            headers: {
              "Content-type": this.allegati[type].type,
              "X-Filename-Originale": this.allegati[type].name.replace(
                /[^0-9a-zA-Z-.]+/g,
                ""
              ),
            },
          }
        );
        if (!this.richiestaContratto.hasOwnProperty("allegati")) {
          this.richiestaContratto['allegati'] = []
          this.richiestaContratto.allegati.push(data)
        } else {
          this.richiestaContratto.allegati.push(data)
        }
      } catch (err) {
        this.allegatiOk = false;
        snackbarError(err);
        await store.dispatch("setSpinner", false);
      }
    },
    async controlloAllegati (allegati) {
      for (let attribute in allegati) {
        try {
          if (allegati[attribute]) {
            await this.postAllegatoBuonoSingolo(attribute);
          }
        } catch (err) {
          snackbarError(err);
          this.allegatiOk = false;
        }
      }
    },
    async inviaContratto () {
      await store.dispatch("setSpinner", true);
      await this.controlloAllegati(this.allegati)
      if (this.allegatiOk) {
            this.richiestaContratto
        try {
          let { data } = await postContratti(
            this.idRichiesta,
            this.richiestaContratto
          );
          await store.dispatch("setSpinner", false);
          this.$q.notify({
            type: "positive",
            message: "contratto aggiunto",
          });
          this.$router.push({ name: 'daticontratto', params: { id: this.idRichiesta } });
        } catch (err) {
          this.$router.push({ name: 'daticontratto', params: { id: this.idRichiesta } });
          await store.dispatch("setSpinner", false);
          snackbarError(err);
        }
      } else {
        this.allegatiOk = null;
        await store.dispatch("setSpinner", false);
        this.$q.notify({
          type: "negative",
          message: "Ci sono problemi con gli allegati",
        });
      }
    },
  },
  computed: {
    utenteRichiedente () {
      return store.getters["getUtenteRichiedente"];
    },
    utenteDestinatario () {
      return store.getters["getUtenteDestinatario"];
    },
    validForm () {
      return this.valid
    }
  },
  async created () {
    window.scrollTo(0, 0);
    this.idRichiesta = this.$route.params.id
      ? this.$route.params.id
      : null;
    if (this.utenteDestinatario == null) {
      this.$router.push("/");
    }
  }
};
</script>
