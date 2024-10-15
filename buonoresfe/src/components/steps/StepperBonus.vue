<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div>
    <template v-if="loading">
      <Spinner></Spinner>
    </template>
    <template v-else>
      <h1>{{ title }}</h1>
      <!-- STEPPER DINAMICO CICLA UN LISTA NELLO STORE -->
      <q-stepper
        v-model="step"
        ref="stepper"
        animated
        active-color="primary"
        alternative-labels
        :contracted="$q.screen.lt.lg"
      >
        <template v-for="(step, i) of stepperList">
          <q-step
            :key="i"
            :name="i"
            :prefix="i + 1"
            :title="step.label"
            :done="step > i"
          >
            <em class="step">
              <span class="sr-only">step {{ i + 1 }}</span>
              <span class="sr-only"> di {{ stepperList.length }}</span>
            </em>
            <!-- IN BASE ALL'INDICE MOSTRIAMO I COMPONENTI, IL VALORE 'triggerAvanti' FA SCATTARE IL METODO SUL PULSANTE AVANTI COSI' SALVIAMO NELLO STORE OGNI FASE-->
            <PrivacyAgreement
              v-if="i === 0"
              :saveCurrentPage="triggerAvanti"
            ></PrivacyAgreement>
            <BonusRecipient
              v-if="i === 1"
              :saveCurrentPage="triggerAvanti"
            ></BonusRecipient>
            <FillInTheForm
              v-if="i === 2"
              :saveCurrentPage="triggerAvanti"
              :saveDraft="triggerBozza"
              @sbloccaBozza="sbloccaBozza"
            ></FillInTheForm>
            <FinalReview
              v-if="i === 3"
              :saveCurrentPage="triggerAvanti"
              @modal="modal"
            >
            </FinalReview>
          </q-step>
        </template>
        <!-- INCREMENTIAMO E DIMINUIAMO INDICE PER LA NAVIGAZIONE -->
        <template v-slot:navigation>
          <q-stepper-navigation
            class="row q-pa-sm q-gutter button-action-stepper q-my-xxl"
          >
            <div class="col-12 col-md-auto first-block">
              <q-btn
                flat
                color="primary"
                label="ANNULLA"
                @click="annulla()"
                aria-label="apri la finestra per annullare la domanda"
              />
            </div>
            <div class="col-12 col-md second-block">
              <div class="row justify-end q-gutter-y-lg">
                <div class="col-auto">
                  <q-btn
                    v-if="step == 2"
                    color="primary"
                    label="SALVA IN BOZZA"
                    aria-label="se il pulsante non è disabilitato si può procedere al salvataggio in bozza"
                    @click="salvaBozza()"
                    :disable="!is_draft_changed"
                  />
                </div>
                <div class="col-auto">
                  <q-btn
                    v-if="step < 3"
                    color="primary"
                    @click="avanti()"
                    aria-label="se il pulsante non è disabilitato si può procedere allo step successivo"
                    label="AVANTI"
                    :disable="!is_step_ok(step)"
                  />
                  <q-btn
                    v-if="step === 3"
                    @click="salva()"
                    color="primary"
                    aria-label="invia la domanda"
                    label="INVIA"
                    :disable="!is_step_ok(step)"
                  />
                </div>
              </div>
            </div>
          </q-stepper-navigation>
        </template>
      </q-stepper>

      <!-- MODALE CONFERMA MAGARI FORSE DIVENTA COMPONENTE -->
      <q-dialog v-model="isCancelled" :maximized="$q.screen.lt.md" persistent>
        <q-card style="max-width: 800px">
          <q-card-section>
            <lms-banner type="warning">
              <h2>Sei sicuro di voler annullare la domanda?</h2>
              <p>
                Se confermi l'annullamento, i dati inseriti in questa pagina
                andranno persi e sarai reindirizzato alla tua area personale
              </p>
            </lms-banner>

            <div class="row justify-between">
              <div class="col-auto">
                <q-btn color="negative" outline @click="goHome()"
                  >ANNULLA</q-btn
                >
              </div>
              <div class="col-auto">
                <q-btn color="primary" outline @click="isCancelled = false"
                  >TORNA ALLA DOMANDA</q-btn
                >
              </div>
            </div>
          </q-card-section>
        </q-card>
      </q-dialog>
      <!-- MODALE CONFERMA MAGARI FORSE DIVENTA COMPONENTE -->
      <q-dialog v-model="isSaved" :maximized="$q.screen.lt.md" persistent>
        <q-card style="max-width: 800px">
          <q-card-section>
            <lms-banner type="positive">
              <h2>
                La richiesta di buono residenzialità è stata inviata con
                successo
              </h2>
              <p>
                Le informazioni relative alla tua domanda saranno disponibili
                nella tua area personale e riceverai notifiche via e-mail per
                ogni evento collegato
              </p>
            </lms-banner>
            <div class="row justify-end">
              <div class="col-auto">
                <q-btn v-close-popup color="primary" @click="$router.push('/')"
                  >VAI ALL'AREA PERSONALE</q-btn
                >
              </div>
            </div>
          </q-card-section>
        </q-card>
      </q-dialog>
      <!-- MODALE CONFERMA MAGARI FORSE DIVENTA COMPONENTE -->
      <q-dialog v-model="messageError" :maximized="$q.screen.lt.md" persistent>
        <q-card style="max-width: 800px">
          <q-card-section>
            <lms-banner type="negative">
              <h2>La richiesta di buono residenzialità non è stata inviata</h2>
              <p>Messaggio errore: {{ messageText }}</p>
            </lms-banner>
            <div class="row justify-end">
              <div class="col-auto">
                <q-btn v-close-popup color="primary" @click="$router.push('/')"
                  >VAI ALL'AREA PERSONALE</q-btn
                >
              </div>
            </div>
          </q-card-section>
        </q-card>
      </q-dialog>
    </template>
  </div>
</template>

<script>
import store from "src/store/index";
import { getSportelli, getRichiesta, getRichieste } from "src/services/api";
import Spinner from "components/core/Spinner.vue";
import PrivacyAgreement from "components/steps/PrivacyAgreement";
import BonusRecipient from "components/steps/BonusRecipient";
import FillInTheForm from "components/steps/FillInTheForm";
import FinalReview from "components/steps/FinalReview";
import LmsBanner from "components/core/LmsBanner";
export default {
  name: "StepperBonus",
  components: {
    PrivacyAgreement,
    BonusRecipient,
    FillInTheForm,
    FinalReview,
    Spinner,
    LmsBanner,
  },
  data() {
    return {
      messageText: null,
      messageError: null,
      isSaved: false,
      loading: false,
      fase: 0,
      triggerAvanti: 0,
      triggerBozza: 0,
      isCancelled: false,
      is_draft_changed: false,
    };
  },
  computed: {
    title() {
      return store.getters["getStepperList"][this.step].label;
    },
    stepperList() {
      return store.getters["getStepperList"];
    },
    spinnerStepper() {
      return store.getters["getSpinnerStepper"];
    },
    step() {
      return store.getters["getStep"];
    },
  },
  methods: {
    async goHome() {
      // DA RIVEDERE
      this.$router.push("/");
    },
    // VERIFICA SE IL PULSANTE AVANTI PUO' ESSERE SBLOCCATO
    is_step_ok(step) {
      switch (step) {
        case 0:
          return store.state.privacy_toogle && store.state.consenso_toogle;
        case 1:
          return store.state.step_1;
        case 2:
          return store.state.step_2;
        default:
          return store.state.step_3;
      }
    },
    async avanti() {
      window.scrollTo(0, 0);
      await this.triggerAvanti++;
    },
    async salvaBozza() {
      this.is_draft_changed = false;
      await this.triggerBozza++;
    },
    async sbloccaBozza(value) {
      this.is_draft_changed = value;
    },
    async modal(value) {
      this.isSaved = value.isSaved;
      this.messageError = value.messageError;
      this.messageText = value.messageText;
    },
    async salva() {
      // CONTRIALLIAMO SE CI SONO SPORTELLI APERTI

      try {
        let { data } = await getSportelli();
        let sportelli = JSON.parse(JSON.stringify(data));
        store.dispatch("setSportelli", { sportelli });
        let attivi = sportelli.filter(
          (sportello) => sportello.in_corso == true
        );
        if (attivi) {
          await this.triggerAvanti++;
          // PUT DENTRO FINAL REVIEW
        } else {
          this.$q.notify({
            type: "negative",
            message: `Non è possibile salvare non ci sono sportelli attivi, torna alla homepage per vedere quando sarà possibile fare domanda`,
          });
        }
      } catch (err) {
        console.error(err);
      }
    },
    annulla() {
      this.isCancelled = true;
    },
    chiudi() {
      this.$router.push("/");
    },
  },
  async created() {
    this.loading = true;
    window.scrollTo(0, 0);

    let id = this.$route.params.id ? this.$route.params.id : null;
    if (id) {
      try {
        let { data } = await getRichiesta(id);
        let richiesta = data;
        if (richiesta.stato != "BOZZA") {
          this.loading = false;
          this.$q.notify({
            type: "negative",
            message:
              "La domanda deve essere in stato bozza per essere modificabile",
            timeout: 6000,
          });
          this.$router.push("/");
        } else {
          await store.dispatch("bozzaAllStep");
          this.loading = false;
        }
      } catch (err) {
        this.loading = false;
        let message =
          err.response && err.response.data.detail
            ? err.response.data.detail[0].valore
            : "La domanda non risulta disponibile";
        this.$q.notify({
          type: "negative",
          message: message,
        });
        this.$router.push("/");
      }
    } else {
      try {
        let { data } = await getRichieste();
        let richieste = data.filter((item) => item.stato == "BOZZA");
        if (richieste.length > 0) {
          this.loading = false;
          this.$q.notify({
            type: "negative",
            message:
              "Ci sono altre richieste aperte in corso, concludi prima di aprirne una nuova",
            timeout: 6000,
          });
          this.$router.push("/");
        } else {
          this.loading = false;
        }
      } catch (err) {
        this.loading = false;
      }
      await store.dispatch("resetAllStep");
      await store.dispatch("resetAllState");
    }

    this.loading = false;

    // POSSIAMO RIPARTIRE DA DOVE A SALVATO LE BOZZE OPPURE DA 0
    this.fase = store.state.step;
  },
};
</script>

<style lang="sass">
.q-stepper--horizontal .q-stepper__step-inner
  padding: 0px

.q-stepper
  box-shadow: none
  background: transparent

.q-stepper--horizontal .q-stepper__line:after
  height: 3px
  background: #1976d2

.q-stepper--horizontal .q-stepper__line:before
  height: 3px
  background-color: #1976d2

.q-stepper__tab
  color: #1976d2

.q-stepper--horizontal > .q-stepper__nav
  padding: 0
</style>
