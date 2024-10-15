<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <q-dialog v-model="open" :maximized="$q.screen.lt.md" persistent>
    <q-card style="max-width: 800px" v-if="contractModal">
      <q-card-section>
        <lms-banner type="warning">
          <h2>SEI SICURO DI VOLER CHIUDERE IL CONTRATTO</h2>
          <p>
            Se confermi la chiusura, il contratto sarà cancellato dalla data
            indicata <strong>qui sotto</strong> e dovrai inserirne un altro
            della durata di almeno 12 mesi.
          </p>
        </lms-banner>
        <p class="text--error">
          Tutti i campi contrassegnati con * sono obbligatori
        </p>
        <p class="sr-only">
          Il tasto "comunica chiusura" è bloccato finché non viene inserita la data di fine contratto, la quale deve essere successiva a quella di inzio contratto.
        </p>
        <div
          class="row q-gutter-y-lg q-gutter-x-lg date-start-end"
          v-if="contractModal.contratto.data_inizio"
        >
          <div class="col-8 col-lg-4">
            <strong>Data inizio contratto</strong>
            <div class="q-mt-md q-mb-sm">
              {{ contractModal.contratto.data_inizio | formatDate }}
            </div>
          </div>
          <div class="col-8 col-lg-4">
            <label for="dataFineContratto"
              >*Inserisci Data fine contratto</label
            >
            <q-input
              id="dataFineContratto"
              v-model="data_fine"
              type="date"
              min="1970-01-01"
              max="2099-12-31"
            >
            </q-input>

            <p class="text--error" v-if="errorDataFine">
              La data di chiusura contratto non può essere inferiore alla data inizio
            </p>
          </div>
        </div>

        <div class="row justify-between q-mt-md q-mb-sm">
          <div class="col-auto">
            <q-btn color="negative" outline @click="close()">ANNULLA</q-btn>
          </div>
          <div class="col-auto">
            <q-btn
              :disable="errorDataFine || !this.data_fine"
              color="primary"
              outline
              @click="closeContract()"
              >CONFERMA CHIUSURA</q-btn
            >
          </div>
        </div>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

      
<script>
import moment from "moment";
import LmsBanner from "components/core/LmsBanner";
export default {
  name: "CloseContractModal",
  props: {
    openModal: Boolean,
    contractModal: Object,
  },
  components: { LmsBanner },
  data () {
    return {
      data_fine: null
    };
  },
  filters: {
    formatDate: function (date) {
      return date ? moment(date).format("DD/MM/YYYY") : null;
    },
  },
  computed: {
    open () {
      return this.openModal;
    },
    errorDataFine () {
      return this.data_fine !== null && new Date(this.data_fine) < new Date(this.contractModal.contratto.data_inizio)  ? true : false
    }
  },
  filters: {
    formatDate: function (date) {
      return date ? moment(date).format("DD/MM/YYYY") : null;
    },
  },
  methods: {
    async closeContract () {
      let obj = {
        modale: this.contractModal, 
        data_fine: this.data_fine
      }
      await this.$emit("closeContract", obj);
      this.data_fine = null
    },
    close () {
      this.data_fine = null
      this.$emit("closeContract", null);
    }
  }
};
</script>