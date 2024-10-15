<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <lms-page>
    <template v-if="loading">
      <div class="container">
        <div class="row">
          <div class="col-12 col-lg-8 offset-lg-2">
            <spinner></spinner>
          </div>
        </div>
      </div>
    </template>
    <template v-else>
      <lms-page-title v-if="utenteRichiedente" class="q-mb-lg" no-back>Area personale di
        {{
      richiestaUtente(utenteRichiedente.nome, utenteRichiedente.cognome)
    }}</lms-page-title>
      <template v-if="checkApi">
        <div class="q-pt-xl">
          <lms-banner type="info" v-if="listaRichieste.length == 0 && sportelliAttivi && sportelliAttivi.length > 0">
            <h2>Non hai nessuna richiesta</h2>
            <p>
              Per effettuare una domanda clicca
              <strong>"Nuova richiesta"</strong>
            </p>
          </lms-banner>

          <h3 v-if="listaRichieste.length">RICHIESTE ATTIVE</h3>
          <div v-for="(richiesta, i) in listaRichieste" :key="i">
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
                  <div class="col-md-6">
                    <dl :title="'Richiesta Buono' + richiesta.numero">
                      <dt>Data aggiornamento:</dt>
                      <dd>{{ richiesta.data_aggiornamento | formatDate }}</dd>
                    </dl>
                  </div>
                  <div class="col-12" v-if="richiesta.stato == 'DINIEGO'">
                    <lms-banner type="negative">
                      <h2>Motivazione</h2>
                      <p style="word-break: break-all">{{ richiesta.note }}</p>
                    </lms-banner>
                  </div>
                  <div class="col-12 q-mt-xl  card-section--btn"
                    v-if="richiesta.stato == 'IN_PAGAMENTO' && richiesta.esistebuono">

                    <q-btn @click="openApplication(richiesta.numero, 'gestione')" color="primary" :aria-label="'gestione domanda numero: ' + richiesta.numero
      " label="GESTISCI IL BUONO"></q-btn>

                  </div>
                  <div class="col-12 q-mt-xl card-section--btn" v-if="richiesta.stato == 'AMMESSA_RISERVA_IN_PAGAMENTO' ||
      richiesta.stato == 'PREAVVISO_DINIEGO_IN_PAGAMENTO'
      ">
                    <q-btn @click="
      openApplication(richiesta.numero, 'perfezionamento')
      " color="primary" :aria-label="'perfeziona domanda numero: ' + richiesta.numero
      " label="PERFEZIONA DOMANDA"></q-btn>
                  </div>
                  <div class="col-12 q-mt-xl card-section--btn" v-if="richiesta.stato == 'BOZZA'">
                    <q-btn @click="
      openApplication(richiesta.numero, 'richiestaedit')
      " color="primary" :aria-label="'modifica la bozza numero: ' + richiesta.numero
      " label="VAI ALLA BOZZA"></q-btn>

                    <q-btn @click="settaStato('ANNULLATA', richiesta)" :aria-label="'elimina la bozza numero: ' + richiesta.numero
      " color="negative" label="ELIMINA"></q-btn>
                  </div>
                  <div class="col-12 q-mt-xl card-section--btn" v-if="richiesta.stato ==
      'PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA'
      ">
                    <q-btn @click="
      openApplication(richiesta.numero, 'controdeduzione')
      " color="primary" :aria-label="'controdeduzione domanda numero: ' + richiesta.numero
      " label="CONTRODEDUZIONE DOMANDA"></q-btn>
                  </div>
                  <div class="col-12 q-mt-xl card-section--btn"
                    v-if="richiesta.stato !== 'BOZZA' && richiesta.stato !== 'DINIEGO' && richiesta.stato !== 'RINUNCIATA'">
                    <q-btn v-if="richiesta.stato == 'DA_RETTIFICARE' ||
      richiesta.stato == 'IN_RETTIFICA'
      " @click="retifyApplication(richiesta, 'IN_RETTIFICA')" color="primary" :aria-label="'rettifica la domanda numero: ' + richiesta.numero
      " label="RETTIFICA DOMANDA"></q-btn>

                    <q-btn v-if="richiesta.stato !== 'IN_PAGAMENTO' && !richiesta.esistebuono"
                      @click="openModalRenounce(richiesta)" color="negative" :aria-label="'rinuncia alla domanda numero: ' + richiesta.numero
      " label="RINUNCIA DOMANDA"></q-btn>
                  </div>
                  <div class="col-12 q-mt-xl card-section--btn" v-if="richiesta.stato != 'BOZZA'">
                    <a class="btn--flat" :href="url + '/' + richiesta.numero + '/DOMANDA'" target="_blank" :aria-label="'scarica la richiesta numero ' + richiesta.numero
      ">
                      SCARICA RICHIESTA
                    </a>
                  </div>
                </div>
              </q-card-section>
            </q-card>
          </div>
        </div>
        <template v-if="nuovaRichiestaBtn.length == 0">
          <div class="col-12 q-pt-xxl text-right"
            v-if="sportelliAttivi && sportelliAttivi.length > 0 && utenteRichiedente">
            <q-btn @click="newApplication()" color="primary">
              Nuova richiesta
            </q-btn>
          </div>
        </template>
        <template v-else>
          <lms-banner type="warning">
            <h2>Attenzione</h2>
            <p>
              Non è possibile fare nuove richieste finchè ci sono domande in
              stato "BOZZA"
            </p>
          </lms-banner>
        </template>

        <template v-if="!sportelliAttivi">
          <lms-banner type="warning">
            <h2>Servizio temporaneamente non attivo</h2>
            <p v-if="sportelliNonAttivi">
              Sarà possibile inviare una richiesta di Buono residenzialità a
              partire dal
              <strong v-for="(sportello, i) in sportelliNonAttivi" :key="i">{{
      sportello[0].data_fine
    }}</strong>
            </p>
            <p v-else>Si prega di riprovare più tardi.</p>
          </lms-banner>
        </template>
        <div class="q-pt-xxxl q-pb-xxxl" v-if="contatti">
          <q-card>
            <q-card-section>
              <div class="row justify-between items-center">
                <div class="col">
                  <h3>I tuoi contatti</h3>
                </div>
                <div class="col text-right">
                  <a color="primary"
                    class="q-btn q-btn-item non-selectable no-outline q-btn--flat q-btn--rectangle text-primary q-btn--actionable q-focusable q-hoverable q-btn--wrap"
                    aria-label="modifica i tuoi contatti tornando al portale di Scelta sociale" :href="linkPortale" flat
                    target="_blank">
                    MODIFICA
                  </a>
                </div>
              </div>
              <div class="q-pt-xl">
                <p>
                  I contatti elencati di seguito saranno utilizzati per tutte le
                  comunicazioni riguardanti il bonus residenzialità, verifica la
                  correttezza ed eventualmente aggiornali/modificali se ritieni
                  opportuno.
                </p>
              </div>
              <div class="row justify-between q-pt-xl items-start q-gutter-y-lg">
                <div class="col-12 col-md">
                  <dl title="numero di telefono di contatto">
                    <dt>
                      <q-icon name="phone" color="primary" aria-hidden="true" />
                      Telefono
                    </dt>
                    <dd>{{ contatti.sms }}</dd>
                  </dl>
                </div>
                <div class="col-12 col-md-5">
                  <dl>
                    <dt>
                      <q-icon name="mail" color="primary" aria-hidden="true" />
                      Email
                    </dt>
                    <dd class="wrap-text">
                      {{ contatti.email }}
                    </dd>
                  </dl>
                </div>
              </div>
            </q-card-section>
          </q-card>
        </div>
      </template>
      <template v-else><lms-banner type="negative">
          <h2>Attenzione</h2>
          <p>Problemi server, contattare assistenza o riprovare più tardi</p>
        </lms-banner></template>
      <div class="q-pt-xxl q-pb-xxl">
        <q-card>
          <q-card-section class="expansion-bar">
            <q-list>
              <q-expansion-item dense dense-toggle expand-separator
                label="Cosa devo fare per compilare e inviare una richiesta?">
                <p class="q-mt-xl">
                  Per procedere con la compilazione della richiesta di buono
                  residenzialità occorre essere in possesso di una attestazione
                  ISEE socio-sanitario in corso di validità e di un verbale di
                  valutazione U.V.G./U.M.V.D. o lettera di comunicazione del
                  punteggio sociale attribuito.
                </p>
              </q-expansion-item>
            </q-list>
          </q-card-section>
        </q-card>
      </div>
      <renunciation-modal :openModal="openModal" :applicationModal="applicationModal"
        @renounceApplication="renounceApplication($event)"></renunciation-modal>
    </template>
  </lms-page>
</template>

<script>
import moment from "moment";
import store from "src/store/index";
import { IS_PROD } from "src/services/config";
import { getRichieste, postCronologia } from "src/services/api";
import LmsBanner from "components/core/LmsBanner";
import Spinner from "components/core/Spinner";
import RenunciationModal from "components/RenunciationModal";
export default {
  name: "PagePersonalArea",
  components: {
    LmsBanner,
    Spinner,
    RenunciationModal,
  },
  data () {
    return {
      renounceOpen: false,
      application: null,
      url: window.location.origin + "/buonoresbff/api/v1/allegato",
      loading: false,
      tab: "richieste",
      richieste: [],
      today: null,
      checkApi: true,
    };
  },
  computed: {
    openModal () {
      return this.renounceOpen;
    },
    applicationModal () {
      return this.application;
    },
    linkPortale () {
      return IS_PROD
        ? "https://www.sceltasociale.it/#/profilo"
        : "https://tst-www-sceltasociale.portali.csi.it/#/profilo";
    },
    sportelliAttivi () {
      return store.getters["getSportelli"]
        ? store.getters["getSportelli"].filter(
          (sportello) => sportello.in_corso == true
        )
        : null;
    },
    listaRichieste () {
      return this.richieste
        ? this.richieste.filter((item) => item.stato != "ANNULLATA")
        : null;
    },
    nuovaRichiestaBtn () {
      return this.richieste
        ? this.richieste.filter((item) => item.stato == "BOZZA")
        : null;
    },
    sportelliNonAttivi () {
      return store.getters["getSportelli"]
        ? store.getters["getSportelli"].filter(
          (sportello) =>
            sportello.in_corso == false &&
            this.controlloDate(sportello.data_inizio)
        )
        : null;
    },
    utenteRichiedente () {
      return store.getters["getUtenteRichiedente"];
    },
    contatti () {
      return store.getters["getContatti"];
    },
    taxCode () {
      return store.getters["getTaxCode"];
    },
    sportelliScelta () {
      return store.getters["getSportelli"];
    },
    statiLista () {
      return store.state.stati_domanda;
    },
    canCreateConversations () {
      return isUserEnabled();
    },
    hasMoreConversations () {
      return (
        this.conversations.length < this.conversationListTotalCount &&
        this.conversationListTotalCount > 0
      );
    },
    conversationList () {
      let conversations = this.conversations;
      if (!this.canCreateConversations) {
        conversations = conversations?.map((conversation) => {
          conversation.sola_lettura = true;
          return conversation;
        });
      }
      return conversations;
    },
  },
  filters: {
    formatDate: function (date) {
      return date ? moment(date).format("DD/MM/YYYY") : null;
    },
  },
  methods: {

    richiestaUtente (nome, cognome) {
      let utente = nome + " " + cognome;
      return utente;
    },

    async newApplication () {
      // RESETTO STORE PRIMA DI UNA NUOVA RICHIESTA
      let stepperForm = {
        stepperForm: {
          domicilio_radio: null,
          destinatario_radio: null,
          show_destinatario: false,
        },
      };
      let richiesta = {
        richiedente: {
          cf: null,
          nome: null,
          cognome: null,
          data_nascita: null,
          stato_nascita: null,
          comune_nascita: null,
          provincia_nascita: null,
          indirizzo_residenza: null,
          comune_residenza: null,
          provincia_residenza: null,
        },
        destinatario: {
          cf: null,
          nome: null,
          cognome: null,
          data_nascita: null,
          stato_nascita: null,
          comune_nascita: null,
          provincia_nascita: null,
          indirizzo_residenza: null,
          comune_residenza: null,
          provincia_residenza: null,
        },
        note: null,
        studio_destinatario: null,
        lavoro_destinatario: null,
        domicilio_destinatario: {
          indirizzo: null,
          comune: null,
          provincia: "",
        },
        delega: null,
      };
      await store.dispatch("resetAllStep");
      await store.commit("SET_PRIVACY", false);
      await store.commit("SET_AGREMENT", false);
      await store.dispatch("setStepperForm", stepperForm);
      await store.dispatch("setRichiesta", richiesta);
      this.$router.push("richiesta");
    },
    async openApplication (id, route) {
      await store.dispatch("bozzaAllStep");
      this.$router.push({ name: route, params: { id: id } });
    },

    async retifyApplication (richiesta, stato) {
      await store.dispatch("bozzaAllStep");
      await this.settaStato(stato, richiesta);
      this.$router.push({
        name: "rettifica",
        params: { id: richiesta.numero },
      });
    },
    formatDate () {
      var today = new Date();
      var dd = today.getDate();
      var mm = today.getMonth() + 1;
      var yyyy = today.getFullYear();
      if (dd < 10) {
        dd = "0" + dd;
      }
      if (mm < 10) {
        mm = "0" + mm;
      }
      return (today = yyyy + "-" + mm + "-" + dd);
    },
    controlloDate (date) {
      var d1 = new Date(this.today); //yyyy-mm-dd
      var d2 = new Date(date); //yyyy-mm-dd

      if (d1 < d2) {
        return true;
      } else {
        return false;
      }
    },
    async settaStato (stato, richiesta) {
      if (stato != richiesta.stato) {
        this.loading = true;
        try {
          let { data } = await postCronologia(richiesta.numero, stato);
          try {
            let { data } = await getRichieste();
            this.richieste = data;
            this.loading = false;
          } catch (err) {
            this.loading = false;
            let message = err.response && err.response.data.detail
              ? err.response.data.detail[0].valore
              : "Problemi server, contattare assistenza";
            this.$q.notify({
              type: "negative",
              message: message,
            });
          }
        } catch (err) {
          this.loading = false;
          let message =
            err.response && err.response.data.detail
              ? err.response.data.detail[0].valore
              : "Problemi server, contattare assistenza";
          this.$q.notify({
            type: "negative",
            message: message,
          });
        }
      }
    },
    openModalRenounce (application) {
      this.renounceOpen = true;
      this.application = application
    },
    renounceApplication (value) {
      this.renounceOpen = false;
      this.application = null;
      if (value) {
        this.settaStato('RINUNCIATA', value)
        this.application = null;
      }
    },
  },
  async created () {
    this.loading = true;
    this.today = this.formatDate();
    try {
      let { data } = await getRichieste();
      this.richieste = data;
      this.loading = false;
    } catch (err) {
      this.checkApi = false;
      this.loading = false;
    }
  },
};
</script>

<style lang="sass">
.q-tab-panels
  background: transparent
.q-tab-panel
  padding: 0
.q-item__label
  font-weight: 700
.expansion-bar
  padding: 0
</style>
