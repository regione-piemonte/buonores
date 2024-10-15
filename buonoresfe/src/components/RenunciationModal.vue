<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <q-dialog v-model="open" :maximized="$q.screen.lt.md" persistent>
    <q-card style="max-width: 800px" v-if="applicationModal">
      <q-card-section>
        <lms-banner type="warning">
          <h2 class="text-center">
            DICHIARAZIONE SOSTITUTIVA DI ATTO DI NOTORIETAâ?? RINUNCIA â??BUONO
            RESIDENZIALITÃ?â?? ai sensi del D.P.R. 445/2000 art. 38
          </h2>
          <p>
            Il/a sottoscritto/a
            <strong
              >{{ applicationModal.richiedente.nome }}
              {{ applicationModal.richiedente.cognome }}</strong
            >
            nato/a a
            <strong>{{ applicationModal.richiedente.comune_nascita }}</strong>
            il
            <strong>{{
              applicationModal.richiedente.data_nascita | formatDate
            }}</strong
            >, residente in
            <strong>{{
              applicationModal.richiedente.indirizzo_residenza
            }}</strong
            >,
            <strong>{{ applicationModal.richiedente.comune_residenza }}</strong>
            (<strong>{{
              applicationModal.richiedente.provincia_residenza
            }}</strong
            >) CF:
            <strong>{{ applicationModal.richiedente.cf }}</strong> consapevole
            che chiunque rilasci dichiarazioni mendaci Ã¨ punito ai sensi del
            codice penale e delle leggi speciali in materia, ai sensi e per gli
            effetti degli artt. 75 e 76 D.P.R. nÂ°. 445/2000 e ss. mm. e ii.
          </p>

          <h2 class="text-center">DICHIARA</h2>
          <p v-if="typeModal == 'buono'">
            di voler rinunciare allâ??erogazione del â??Buono ResidenzialitÃ â?? (D.G.R. n. 27-6320 del 22/12/2022) assegnato in favore del dichiarante ovvero in nome e per conto di  <strong
              >{{ applicationModal.destinatario.nome }}
              {{ applicationModal.destinatario.cognome }}</strong
            > residente in
            <strong>{{
              applicationModal.destinatario.indirizzo_residenza
            }}</strong
            >,
            <strong>{{
              applicationModal.destinatario.comune_residenza
            }}</strong>
            (<strong>{{
              applicationModal.destinatario.provincia_residenza
            }}</strong
            >).

          </p>
          <p v-else>
            di voler rinunciare alla domanda inserita nÂ°<strong>{{
              applicationModal.numero
            }}</strong>
            aggiornata il
            <strong>{{
              applicationModal.data_aggiornamento | formatDate
            }}</strong>
            relativa al â??Buono ResidenzialitÃ â?? (D.G.R. nÂ°. 51-6810 del 28/04/2023) sulla piattaforma dedicata e presentata in favore del
            dichiarante ovvero in nome e per conto di 
            <strong
              >{{ applicationModal.destinatario.nome }}
              {{ applicationModal.destinatario.cognome }}</strong
            > 
            residente in
            <strong>{{
              applicationModal.destinatario.indirizzo_residenza
            }}</strong
            >,
            <strong>{{
              applicationModal.destinatario.comune_residenza
            }}</strong>
            (<strong>{{
              applicationModal.destinatario.provincia_residenza
            }}</strong
            >), in virtÃ¹ della procura speciale sottoscritta dal destinatario e
            dal richiedente ed inoltrata in sede di presentazione della domanda.
          </p>
          <h2 class="text-center">PERTANTO</h2>
          <p>
            <q-checkbox
              v-model="accept"
              :label="typeModal == 'buono'? 'autorizza Regione Piemonte â?? Direzione Welfare â?? a procedere alla revoca dellâ??assegnazione del â??Buonoâ??.' : 'autorizza Regione Piemonte â?? Direzione SanitÃ  e Welfare â?? a procedere alla dichiarazione di non ammissibilitÃ  della domanda stessa.' "
            />
            <template v-if="typeModal == 'buono'">
              <div class="col-8 col-lg-4 q-mt-sm">
                <label for="dataRinuncia"
                  >*Tale rinuncia decorre da</label
                >
                <q-input
                  id="dataRinuncia"
                  v-model="renounce_date"
                  type="date"
                  :min="minDate"
                  :max="maxDate"
                >
                </q-input></div
            ></template>
          </p>
        </lms-banner>

        <div class="row justify-between">
          <div class="col-auto q-mb-sm">
            <q-btn color="negative" outline @click="close()">ANNULLA</q-btn>
          </div>
          <div class="col-auto q-mb-sm">
            <q-btn
              :disable="!accept || !renounce_date"
              color="primary"
              outline
              @click="renounce()"
              v-if="typeModal == 'buono'"
              >RINUNCIA AL BUONO</q-btn
            >
            <q-btn
              :disable="!accept"
              color="primary"
              outline
              @click="renounce()"
              v-else
              >RINUNCIA ALLA DOMANDA</q-btn
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
  name: "RenunciationModal",
  props: {
    openModal: Boolean,
    applicationModal: Object,
    typeModal: String,
  },
  components: { LmsBanner },
  data () {
    return {
      accept: false,
      renounce_date: null
    };
  },
  computed: {
    open () {
      return this.openModal;
    },
    minDate () {
      return moment(new Date()).format("YYYY-MM-DD")
    },
    maxDate () {
      return moment(new Date().setFullYear(new Date().getFullYear() + 2)).format("YYYY-MM-DD")
    }
  },
  filters: {
    formatDate: function (date) {
      return date ? moment(date).format("DD/MM/YYYY") : null;
    },
  },
  methods: {
    renounce () {
      if (this.typeModal == 'buono') {
        this.$emit("renounceApplication", { obj: this.applicationModal, date: this.renounce_date });
      } else {
        this.$emit("renounceApplication", this.applicationModal);
      }

    },
    close () {
      this.$emit("renounceApplication", null);
    }
  },
};
</script>