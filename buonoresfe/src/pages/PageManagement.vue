<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <lms-page>

    <template v-if="!loading && richiesta?.destinatario">
      <BreadcrumbsCustom :name="richiesta.destinatario.nome + ' ' + richiesta.destinatario.cognome
        " :path1="'gestione'" :label1="'Gestione'" :id="idRichiesta"></BreadcrumbsCustom>
      <h2 class="q-mb-md q-mt-md" no-back>
        Buono residenzialità di {{ utenteRichiedente.nome }}
        {{ utenteRichiedente.cognome }}
      </h2>

      <!-- CARD DETTAGLI DOMANDA -->
      <div class="q-pt-xl">
        <q-card class="q-mt-xl q-mb-xl q-card--background">
          <q-card-section>
            <div class="row justify-between items-end">
              <div class="col-12 col-md-6">
                <h3 class="title-ico buono-res">
                  RICHIESTA BUONO RESIDENZIALITA'
                </h3>
                <dl :title="'Richiesta Buono' + richiesta.numero" class="info-grid">
                  <dt>Destinatario:</dt>
                  <dd>
                    {{
                      richiesta.destinatario.nome +
                      " " +
                    richiesta.destinatario.cognome
                    }}
                  </dd>
                </dl>
              </div>
              <div class="col-12 col-md-6">
                <dl :title="'Richiesta Buono' + richiesta.numero" class="info-grid">
                  <dt>Richiesta numero:</dt>
                  <dd>{{ richiesta.numero }}</dd>
                </dl>
              </div>
              <div v-if="richiesta.protocollo && richiesta.protocollo.numero" class="col-12 q-mt-md">
                <dl :title="'Richiesta Buono' + richiesta.numero" class="info-grid">
                  <dt>Protocollata:</dt>
                  <dd>
                    in data {{ richiesta.protocollo.data | formatDate }} -
                    numero {{ richiesta.protocollo.numero }}
                  </dd>
                </dl>
              </div>
            </div>
            <div class="row justify-between items-end q-mt-md">
              <div class="col-md-6">
                <dl :title="'Richiesta Buono' + richiesta.numero">
                  <dt>Stato:</dt>
                  <dd :class="'richiesta-stato richiesta-stato--' +
                    richiesta.stato.toLowerCase()
                    " v-for="(item, i) of statiLista" :key="i">
                    <template v-if="item.codice == richiesta.stato">{{
                      item.etichetta
                      }}</template>
                  </dd>
                </dl>
              </div>
              <div class="col-12 col-md">
                <dl :title="'Richiesta Buono' + richiesta.numero">
                  <dt>Data aggiornamento:</dt>
                  <dd>{{ richiesta.data_aggiornamento | formatDate }}</dd>
                </dl>
              </div>
            </div>
            <div class="q-px-sm" v-if="!isPresaVisione && isRevocato">
              <lms-banner type="negative">
                <h2>Attenzione</h2>
                <p class="q-mt-md">
                  Il buono relativo alla domanda n. {{ richiesta.numero }} è stato <strong>REVOCATO CON PRESA
                    VISIONE</strong>
                </p>
                <p v-if="buonoNote"><strong>Motivazione:</strong> {{ buonoNote }}</p>
                <p v-if="buonoDecorrenza"><strong>Decorrenza revoca:</strong> {{ buonoDecorrenza | formatDate }}</p>
              </lms-banner>
            </div>
          </q-card-section>
        </q-card>
      </div>
      <!-- FINE CARD DETTAGLI DOMANDA -->

      <!-- CARD ISEE ###-->
      <q-card class="q-mt-xl q-mb-xl">
        <q-card-section>
          <h2>Rinnovo <abbr title="Indicatore della Situazione Economica Equivalente">I.S.E.E.</abbr> socio -
            sanitario {{ currentYear }}</h2>
          <div class="row q-my-xl justify-between">
            <!-- // ### revocato -->
            <div class="col-auto col-lg-1">
              <q-toggle :disable="iseeVerified || isRevocato" v-model="iseeVerified" @input="postConformitaIsee"
                aria-labelledby="attestazioneIsee" id="attestazioneIseeToggle" name="attestazioneIseeToggle" />
            </div>
            <label class="col col-lg-10 q-mt-xs" id="attestazioneIsee" for="attestazioneIseeToggle">
              Seleziona se già in possesso di un'attestazione <abbr
                title="Indicatore della Situazione Economica Equivalente">I.S.E.E.</abbr>
              socio - sanitario {{ currentYear }}
            </label>
            <div class="col-1">
              <tooltip :titleBanner="iseeTooltip.title" :textBanner="iseeTooltip.text" />
            </div>
            <div class="q-mt-xs">
              <p>
                Per mantenere il diritto all'erogazione del buono l'ISEE deve essere conforme ai requisiti definiti dal
                bando, in caso contrario il buono viene revocato dal 1/1/{{ currentYear }}
              </p>
            </div>

          </div>
        </q-card-section>
      </q-card>
      <!-- FINE CARD ISEE -->

      <!-- CARD VERIFICA DATI CONTRATTO -->
      <q-card class="q-mt-xl q-mb-xl">
        <q-card-section>
          <div class="row justify-between items-end">
            <h2 class="col-12">
              Cosa devi fare per ottenere l'erogazione del Buono?
            </h2>
            <div class="row q-pa-xs q-mt-md items-start">
              <div class="col-auto q-pr-md">
                <img src="../../public/img/gestione/computer.svg" alt="" />
              </div>
              <div class="col-12 col-md">
                <h3>
                  VERIFICA LA CORRETTEZZA DEI DATI CONTRATTO PER LA STRUTTURA RESIDENZIALE
                </h3>
                <p>
                  Se hai cambiato struttura residenziale, hai modificato il contratto o ne hai stipulato un altro,
                  aggiorna i tuoi dati
                </p>
              </div>
              <div class="col-12">
                <lms-banner type="warning">
                  <h2>Attenzione</h2>
                  <p class="q-mt-md">
                    Per ottenere l�??erogazione del Buono è necessario che i dati dichiarati corrispondano a quelli
                    presenti
                    nei documenti
                  </p>
                </lms-banner>
              </div>
              <div class="col-12 q-mt-xl card-section--btn text-right">
                <!-- // ### revocato -->
                <q-btn :disable="isRevocato" @click="openApplication(richiesta.numero, 'daticontratto')" color="primary"
                  :aria-label="'vai ai dati contrattuali domanda numero: ' +
                    richiesta.numero
                    " label="VAI AI DATI CONTRATTUALI"></q-btn>
              </div>
            </div>
          </div>
        </q-card-section>
      </q-card>
      <!-- FINE CARD VERIFICA DATI CONTRATTO -->

      <!-- CARD RINUNCIA -->
      <q-card class="q-mt-xl q-mb-xl">
        <q-card-section v-if="cronologiaBuonoFlag">
          <div class="row q-pa-xs q-mt-md items-start">
            <div class="col-12 col-md flex-column-col">
              <div>
                <h3>Hai già rinuciato al buono</h3>
                <p>
                  Hai rinunciato al <b>Buono Residenzialità</b> con decorrenza
                  <b>{{ dataAggiornamento | formatDate }}</b>
                </p>
              </div>
            </div>
            <div class="col-auto q-pr-md">
              <img src="../../public/img/gestione/rinuncia.svg" alt="" />
            </div>
          </div>
        </q-card-section>
        <q-card-section v-else>
          <div class="row q-pa-xs q-mt-md items-start">
            <div class="col-12 col-md flex-column-col">
              <div>
                <h3>Rinuncia al buono</h3>
                <p>
                  Non hai più i requisiti per avere diritto all'erogazione del
                  Buono?
                </p>
              </div>
            </div>
            <div class="col-auto q-pr-md">
              <img src="../../public/img/gestione/rinuncia.svg" alt="" />
            </div>
            <div class="col-12 q-mt-xl text-right">
              <!-- // ### revocato -->
              <q-btn :disabled="isRevocato" @click="openModalRenounce(richiesta)" color="negative"
                :aria-label="'rinuncia alla buono numero: ' + richiesta.numero" label="RINUNCIA BUONO"></q-btn>
            </div>
          </div>
        </q-card-section>
      </q-card>
      <!-- FINE CARD RINUNCIA -->

      <!-- MODALE RINUNCIA -->
      <template v-if="openRenounceModal">
        <renunciation-modal :openModal="openRenounceModal" :applicationModal="applicationModal" typeModal="buono"
          @renounceApplication="renounceApplication($event)"></renunciation-modal>
      </template>
      <!-- FINE MODALE RINUNCIA -->
    </template>
    <!-- MODALE REVOCA -->
    <template v-if="isPresaVisione && isRevocato">
      <q-dialog v-model="isPresaVisione" :maximized="$q.screen.lt.md" persistent>
        <q-card style="max-width: 800px">
          <q-card-section>
            <lms-banner type="negative">
              <h2>Attenzione</h2>
              <p class="q-mt-md">
                Il buono relativo alla domanda n. {{ richiesta.numero }} è stato <strong>REVOCATO</strong>
              </p>
              <p v-if="buonoNote"><strong>Motivazione:</strong> {{ buonoNote }}</p>
              <p v-if="buonoDecorrenza"><strong>Decorrenza revoca:</strong> {{ buonoDecorrenza | formatDate }}</p>
            </lms-banner>
            <div class="row justify-between">
              <div class="col-auto q-mb-sm">

              </div>
              <div class="col-auto q-mb-sm">
                <q-btn color="primary" outline @click="revokedSeen('REVOCATO_PRESA_VISIONE')">HO PRESO VISIONE</q-btn>
              </div>
            </div>
          </q-card-section>
        </q-card>
      </q-dialog>

    </template>
    <!-- MODALE REVOCA -->
  </lms-page>

</template>

<script>
import moment from "moment"
import Tooltip from "components/core/Tooltip";
import store from "src/store/index"
import { getRichiesta, getCronologiaBuono, postCronologiaBuono, getConformitaIsee, postConformitaIsee } from "src/services/api"
import { snackbarError, notifySuccess } from "src/services/utils"
import LmsBanner from "components/core/LmsBanner"
import BreadcrumbsCustom from "src/components/reporting/BreadcrumbsCustom.vue"
import RenunciationModal from "components/RenunciationModal"


export default {
  name: "PagePersonalArea",
  components: {
    LmsBanner,
    RenunciationModal,
    BreadcrumbsCustom,
    Tooltip,
  },
  data () {
    return {
      application: null,
      idRichiesta: null,
      cronologiaBuonoFlag: null,
      isRevocato: false, //### revocato
      isPresaVisione: false,
      buonoNote: null,
      buonoDecorrenza: null,
      loading: false,
      renounceOpen: false,
      richiesta: null,
      url: window.location.origin + "/buonoresbff/api/v1/allegato",
      iseeVerified: false,
      currentYear: new Date().getFullYear().toString(),
      iseeTooltip: {
        title: "Attestazione I.S.E.E socio - sanitario",
        text: "L'I.S.E.E socio - sanitario è utile per l'accesso alle prestazioni sociosanitarie come l'assistenza domiciliare per le persone con disabilità e/o non autosufficienti, l'ospitalità alberghiera presso strutture residenziali e semiresidenziali per le persone che non possono essere assistite a domicilio. <br><br> Le persone disabili maggiorenni possono scegliere un nucleo più ristretto rispetto a quello ordinario. <br><br> Per esempio, una persona maggiorenne disabile non coniugata e senza figli, che vive con i genitori, in sede di calcolo ISEE può dichiarare solo i suoi redditi e patrimoni.",
      },
    }
  },
  filters: {
    formatDate: function (date) {
      return date ? moment(date).format("DD/MM/YYYY") : null
    },
  },
  methods: {
    async openApplication (id, route) {
      this.$router.push({ name: 'daticontratto', params: { id: id } })
    },
    openModalRenounce (application) {
      this.renounceOpen = true
      this.application = application
    },
    revokedSeen (stato) {
      this.isPresaVisione = false
      if (stato) {
        let payload = {
          stato: stato,
          decorrenza: moment(new Date()).format("YYYY-MM-DD")
        }
        this.settaStato(payload, stato)
      }
    },
    renounceApplication (value) {
      this.renounceOpen = false
      this.application = null
      if (value) {
        let payload = {
          stato: 'RINUNCIATO',
          decorrenza: value.date
        }
        this.settaStato(payload, payload.stato)
        this.application = null
      }
    },

    async settaStato (payload, stato) {
      await store.dispatch("setSpinner", true)
      this.loading = true
      try {
        await postCronologiaBuono(this.richiesta.numero, payload)
        this.loading = false
        await store.dispatch("setSpinner", false)
        this.$q.notify({
          type: "positive",
          message: stato == "RINUNCIATO" ? "Buono rinunciato" : "Presa visione eseguita correttamente",
        })
        this.$router.push("/")
      } catch (err) {
        await store.dispatch("setSpinner", false)
        this.loading = false
        snackbarError(err)
      }
    },

    async getRichiesta (id) {
      try {
        let { data } = await getRichiesta(id)
        this.richiesta = data
        await store.dispatch("setUtenteDestinatario", data.destinatario)
        this.loading = false
      } catch (err) {
        this.loading = false
        snackbarError(err)
        await store.dispatch("setSpinner", false)
      }
    },
    async getCronologiaBuono (id) {
      try {
        let { data } = await getCronologiaBuono(id)
        for (let item of data) {
          if (item.stato.stato == 'RINUNCIATO') {
            this.cronologiaBuonoFlag = true
            this.dataAggiornamento = item.stato.decorrenza
          }
          // ### revocato
          if (item.stato.stato == 'REVOCATO') {
            this.isRevocato = true
            this.isPresaVisione = true
            this.buonoNote = item.note
            this.buonoDecorrenza = item.stato.decorrenza
          }
          if (item.stato.stato == 'REVOCATO_PRESA_VISIONE') {
            this.isRevocato = true
            this.isPresaVisione = false
          }
        }
      } catch (err) {
        snackbarError(err)
        await store.dispatch("setSpinner", false)
      }
    },
    async getConformitaIsee (id) {
      try {
        let { data } = await getConformitaIsee(id)
        this.iseeVerified = data.find(({ anno }) => anno == this.currentYear) ? true : false
        this.loading = false
      } catch (err) {
        this.loading = false
        snackbarError(err)
        await store.dispatch("setSpinner", false)
      }
    },
    async postConformitaIsee () {
      await store.dispatch("setSpinner", true)
      this.loading = true
      try {
        await postConformitaIsee(this.richiesta.numero, { anno: this.currentYear })
        await store.dispatch("setSpinner", false)
        notifySuccess("Attestazione ISEE aggiornata");
        this.iseeVerified = true;
        this.loading = false
      } catch (err) {
        await store.dispatch("setSpinner", false)
        this.loading = false
        snackbarError(err)
      }
    },
  },
  computed: {
    utenteRichiedente () {
      return store.getters["getUtenteRichiedente"]
    },
    statiLista () {
      return store.state.stati_domanda
    },
    openRenounceModal () {
      return this.renounceOpen
    },
    applicationModal () {
      return this.application
    },
  },
  async created () {

    this.loading = true
    await store.dispatch("setSpinner", true)
    window.scrollTo(0, 0)

    this.idRichiesta = this.$route.params.id ? this.$route.params.id : null


    await this.getRichiesta(this.idRichiesta)
    await this.getCronologiaBuono(this.idRichiesta)
    await this.getConformitaIsee(this.idRichiesta)
    await store.dispatch("setSpinner", false)
  },
}
</script>

<style lang="sass"></style>
