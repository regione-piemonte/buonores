<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div>
    <div class="q-page lms-page">
      <!-- NOTE E ALLEGATO CONTRODEDUZIONE -->
      <q-card class="q-mt-xl q-mb-xl">
        <q-card-section>
          <div class="col-12 q-pa-xs">
            <h2>Controdeduzioni</h2>
            <label for="controdeduzione"
              ><span
                :class="
                  $v.richiesta.note_richiedente.$invalid ? 'required-field' : ''
                "
                >*</span
              >Note del richiedente</label
            >
            <q-input
              v-model="richiesta.note_richiedente"
              filled
              for="controdeduzione"
              autogrow
            />
          </div>
          <div class="col-12 q-pa-xs">
            <div>Allegato</div>
            <q-file
              ref="CONTRODEDUZIONE"
              outlined
              clearable
              v-model="allegati.CONTRODEDUZIONE"
              label="Documento controdeduzione"
              class="q-mb-xl hidden"
              :accept="fileAccepted"
              :max-file-size="fileSize"
              :filter="checkFileSize"
              @rejected="onRejected"
            ></q-file>

            <q-btn
              v-if="allegati.CONTRODEDUZIONE == null"
              @click="caricaDocumento('CONTRODEDUZIONE')"
              color="primary"
              label="ALLEGA"
            ></q-btn>

            <div v-if="allegati.CONTRODEDUZIONE" class="q-my-lg">
              <q-btn
                icon="delete"
                class="q-mr-md"
                flat
                color="negative"
                @click="allegati.CONTRODEDUZIONE = null"
                :aria-label="
                  'elimina il documento' + allegati.CONTRODEDUZIONE.name
                "
              />{{ allegati.CONTRODEDUZIONE.name }}
            </div>
          </div>
        </q-card-section>
      </q-card>
      <!-- NOTE E ALLEGATO CONTRODEDUZIONE-->

      <!-- CARD DESTINATARIO BUONO -->
      <q-card class="q-mt-xl q-mb-xl">
        <!-- DATI DESTINATARIO -->
        <q-card-section>
          <div class="row q-mt-xl">
            <div class="col-12">
              <h2>Informazioni domanda</h2>
              <dl>
                <dt>Numero domanda</dt>
                <dd>
                  {{ richiesta.numero }}
                </dd>
              </dl>
            </div>
          </div>
          <div class="row q-my-xl justify-between items-center">
            <div class="col-auto">
              <h2>Dati destinatario</h2>
            </div>
          </div>
          <div class="row q-mb-xl q-col-gutter-xl">
            <div class="col-12">
              <dl>
                <dt>Codice fiscale</dt>
                <dd>
                  {{ richiesta.destinatario.cf }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Cognome</dt>
                <dd>
                  {{ richiesta.destinatario.cognome }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Nome</dt>
                <dd>
                  {{ richiesta.destinatario.nome }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Data di nascita</dt>
                <dd>
                  {{ richiesta.destinatario.data_nascita | formatDate }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Nato/a a</dt>
                <dd>
                  {{ richiesta.destinatario.comune_nascita }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Provincia</dt>
                <dd>
                  {{ richiesta.destinatario.provincia_nascita }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Stato nascita</dt>
                <dd>
                  {{ richiesta.destinatario.stato_nascita }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Indirizzo residenza</dt>
                <dd>
                  {{ richiesta.destinatario.indirizzo_residenza }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Comune residenza</dt>
                <dd>
                  {{ richiesta.destinatario.comune_residenza }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Provincia residenza</dt>
                <dd>
                  {{ richiesta.destinatario.provincia_residenza }}
                </dd>
              </dl>
            </div>
            <template>
              <div class="col-12 col-md-4">
                <dl>
                  <dt>Indirizzo domicilio</dt>
                  <dd>
                    {{ richiesta.domicilio_destinatario.indirizzo }}
                  </dd>
                </dl>
              </div>
              <div class="col-12 col-md-4">
                <dl>
                  <dt>Comune domicilio</dt>
                  <dd>
                    {{ richiesta.domicilio_destinatario.comune }}
                  </dd>
                </dl>
              </div>
              <div class="col-12 col-md-4">
                <dl>
                  <dt>Provincia domicilio</dt>
                  <dd>
                    {{ richiesta.domicilio_destinatario.provincia }}
                  </dd>
                </dl>
              </div>
            </template>
          </div>
          <div class="row q-mb-xl q-col-gutter-xl">
            <div class="col-12 col-lg-8">
              <dl>
                <dt>Asl</dt>
                <dd v-for="(item, i) of asl_options" :key="i">
                  <template v-if="item.codice == richiesta.asl_destinatario">
                    {{ item.etichetta }}
                  </template>
                </dd>
              </dl>
            </div>
            <div class="col-12 col-lg-8">
              <dl>
                <dt>Titolo studio</dt>
                <dd v-for="(item, i) of titolo_options" :key="i">
                  <template v-if="item.codice == richiesta.studio_destinatario">
                    {{ item.etichetta }}
                  </template>
                </dd>
              </dl>
            </div>
            <div class="col-12 col-lg-4" v-if="appropriateAge">
              <dl>
                <dt>Situazione lavorativa</dt>
                <dd>
                  {{
                    richiesta.lavoro_destinatario ? "Occupato" : "Non occupato"
                  }}
                </dd>
              </dl>
            </div>
          </div>
        </q-card-section>
        <!--FINE DATI DESTINATARIO -->

        <!-- I TUOI DATI -->
        <q-card-section>
          <h3>I tuoi dati</h3>
          <div class="row q-mb-xl q-col-gutter-xl">
            <div class="col-12">
              <dl>
                <dt>Codice fiscale</dt>
                <dd>
                  {{ richiesta.richiedente.cf }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Cognome</dt>
                <dd>
                  {{ richiesta.richiedente.cognome }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Nome</dt>
                <dd>
                  {{ richiesta.richiedente.nome }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Data di nascita</dt>
                <dd>
                  {{ richiesta.richiedente.data_nascita | formatDate }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Nato/a a</dt>
                <dd>
                  {{ richiesta.richiedente.comune_nascita }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Provincia</dt>
                <dd>
                  {{ richiesta.richiedente.provincia_nascita }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Stato nascita</dt>
                <dd>
                  {{ richiesta.richiedente.stato_nascita }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Indirizzo residenza</dt>
                <dd>
                  {{ richiesta.richiedente.indirizzo_residenza }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Comune residenza</dt>
                <dd>
                  {{ richiesta.richiedente.comune_residenza }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Provincia residenza</dt>
                <dd>
                  {{ richiesta.richiedente.provincia_residenza }}
                </dd>
              </dl>
            </div>
          </div>
          <div class="row q-mb-xl" v-if="richiesta.delega">
            <div class="col-12">
              <dl>
                <dt>Il richiedente opera in qualità di</dt>
                <dd>
                  <div v-for="(item, i) of rapporto_options" :key="i">
                    <template v-if="item.codice == richiesta.delega">
                      {{ item.etichetta }}
                    </template>
                  </div>
                </dd>
              </dl>
            </div>
          </div>
        </q-card-section>
        <!--FINE I TUOI DATI -->
        <!-- ALLEGATI -->
        <q-card-section>
          <div class="row q-mb-xl">
            <div>
              <!-- ALLEGATI-->
              <div v-for="(allegato, i) in richiesta.allegati" :key="i">
                <div v-if="allegato.tipo == 'DELEGA'">
                  Delega:
                  <a
                    :href="url + '/' + richiesta.numero + '/' + allegato.tipo"
                    target="_blank"
                  >
                    <strong>{{ allegato.filename }} </strong>
                  </a>
                </div>
                <div v-if="allegato.tipo == 'CARTA_IDENTITA'">
                  Carta identità:
                  <a
                    :href="url + '/' + richiesta.numero + '/' + allegato.tipo"
                    target="_blank"
                  >
                    <strong>{{ allegato.filename }} </strong>
                  </a>
                </div>
                <div v-if="allegato.tipo == 'PROCURA_SPECIALE'">
                  Procura speciale:
                  <a
                    :href="url + '/' + richiesta.numero + '/' + allegato.tipo"
                    target="_blank"
                  >
                    <strong>{{ allegato.filename }} </strong>
                  </a>
                </div>

                <div v-if="allegato.tipo == 'NOMINA_TUTORE'">
                  Nomina tutore:
                  <a
                    :href="url + '/' + richiesta.numero + '/' + allegato.tipo"
                    target="_blank"
                  >
                    <strong>{{ allegato.filename }} </strong>
                  </a>
                </div>
              </div>
            </div>
          </div>
        </q-card-section>
        <!--FINE ALLEGATI -->
      </q-card>
      <!--FINE CARD DESTINATARIO BUONO -->

      <!-- CARD COMPILAZIONE MODULO -->
      <q-card class="q-mt-xl q-mb-xl">
        <!-- COMPILAZIONE MODULO -->
        <q-card-section>
          <div class="row items-center justify-between">
            <div class="col-auto">
              <h3>Compilazione Modulo</h3>
            </div>
          </div>
        </q-card-section>
        <!--FINE  COMPILAZIONE MODULO -->

        <!-- ISEE -->
        <q-card-section>
          <h3>Attestazione I.S.E.E.</h3>
          <div class="row">
            <div class="col-11">
              Il destinatario è in possesso di un attestato I.S.E.E. socio -
              sanitario in corso di validità avente un valore inferiore a 50.000
              euro (65.000 euro per minori e disabili)
            </div>
          </div>
        </q-card-section>
        <!--FINE ISEE -->

        <!-- REQUISITI COMPATIBILITA'-->
        <q-card-section>
          <h3>Requisiti di compatibilità</h3>
          <div class="">
            <p>
              {{
                richiesta.valutazione_multidimensionale == "UVG"
                  ? "Il destinatario è persona di età pari o superiore a 65 anni, non autosufficiente"
                  : "Il destinatario è persona con disabilità, non autosufficiente"
              }}
            </p>
            <p>
              {{
                richiesta.nessuna_incompatibilita
                  ? "Il destinatario non è beneficiario di altre misure aventi natura di trasferimento monetario specificatamente destinato/a al sostegno della residenzialità"
                  : "Il destinatario è beneficiario di altre misure aventi natura di trasferimento monetario specificatamente destinato/a al sostegno della residenzialità, ed è disponibile a rinunciarvi in caso di assegnazione del buono"
              }}
            </p>
          </div>
        </q-card-section>
        <!--FINE REQUISITI COMPATIBILITA'-->

        <!-- PUNTEGGIO SOCIALE -->
        <q-card-section>
          <h3>Punteggio sociale</h3>
          <p class="">
            Il destinatario è già stato sottoposto a �??valutazione
            multidimensionale�?? presso le Unità di Valutazione competenti (U.V.G.
            - ANZIANI, o U.M.V.D. - DISABILI), e di essere in possesso di un
            punteggio sociale pari a:
          </p>

          <p class="text-font--xxl">
            <strong>{{ richiesta.punteggio_bisogno_sociale }}</strong>
          </p>
          <div v-for="(allegato, i) in richiesta.allegati" :key="i">
            <div
              v-if="
                allegato.tipo == 'VERBALE_UVG' &&
                richiesta.valutazione_multidimensionale === 'UVG'
              "
            >
              Lettera di comunicazione/Verbale UVG:
              <a
                :href="url + '/' + richiesta.numero + '/' + allegato.tipo"
                target="_blank"
              >
                <strong>{{ allegato.filename }} </strong>
              </a>
            </div>
            <div
              v-if="
                allegato.tipo == 'VERBALE_UMVD' &&
                richiesta.valutazione_multidimensionale === 'UMVD'
              "
            >
              Lettera di comunicazione/Verbale UMVD:
              <a
                :href="url + '/' + richiesta.numero + '/' + allegato.tipo"
                target="_blank"
              >
                <strong>{{ allegato.filename }} </strong>
              </a>
            </div>
          </div>
        </q-card-section>
        <!--FINE PUNTEGGIO SOCIALE -->

        <!-- DATI STRUTTURA -->
        <template v-if="richiesta.contratto.tipo != 'NESSUN_CONTRATTO'">
          <q-card-section>
            <h3>Inserimento in struttura</h3>
          </q-card-section>
          <q-card-section>
            <p>
              Il destinatario è inserito in una struttura residenziale a
              carattere socio-sanitario o socio-assistenziale a
              <strong>titolo privato</strong> e non tramite convenzione con il
              Servizio Sanitario Regionale (SSR).
            </p>

            <!-- DATI STRUTTURA -->
            <q-card-section
              v-if="richiesta.contratto && richiesta.contratto.struttura"
            >
              <div class="row q-my-xl justify-between items-center">
                <div class="col-auto">
                  <h2>Dati della struttura</h2>
                </div>
                <div class="col-auto"></div>
              </div>
              <div class="row q-mb-xl q-col-gutter-xl">
                <div class="col-12 col-md-4">
                  <dl>
                    <dt>Nome struttura</dt>
                    <dd>
                      {{ richiesta.contratto.struttura.nome }}
                    </dd>
                  </dl>
                </div>
                <div class="col-12 col-md-4">
                  <dl>
                    <dt>Indirizzo</dt>
                    <dd>
                      {{ richiesta.contratto.struttura.indirizzo }}
                    </dd>
                  </dl>
                </div>
                <div class="col-12 col-md-4">
                  <dl>
                    <dt>Comune</dt>
                    <dd>
                      {{ richiesta.contratto.struttura.comune }}
                    </dd>
                  </dl>
                </div>
              </div>
            </q-card-section>
            <!-- FINE DATI STRUTTURA -->
            <!--DATA INIZIO -->
            <div class="q-my-xl">
              <h3>Data di inizio</h3>
              <div class="row q-mb-xl q-col-gutter-xl">
                <div class="col-12 col-md-4">
                  <dl>
                    <dt>Data inizio contratto</dt>
                    <dd>
                      {{ richiesta.contratto.data_inizio | formatDate }}
                    </dd>
                  </dl>
                </div>
              </div>
            </div>
            <!--FINE DATA INIZIO-->
          </q-card-section>
          <!--FINE DATI STRUTTURA -->

          <!-- COPIA CONTRATTO RSA-->
          <q-card-section v-if="richiesta.contratto.tipo == 'CONTRATTO_RSA'">
            <template v-if="richiesta.contratto.tipo == 'CONTRATTO_RSA'">
              <div>
                <div v-for="(allegato, i) in richiesta.allegati" :key="i">
                  <div v-if="allegato.tipo == 'CONTRATTO_RSA'">
                    Contratto:
                    <a
                      :href="url + '/' + richiesta.numero + '/' + allegato.tipo"
                      target="_blank"
                    >
                      <strong>{{ allegato.filename }} </strong>
                    </a>
                  </div>
                </div>
              </div>
            </template>
          </q-card-section>
        </template>
        <template v-else>
          <q-card-section>
            <h3>Nessun contratto</h3>
            <p>
              Il destinatario non è attualmente inserito in una struttura ma
              <strong
                >è intenzionato ad effettuare l�??inserimento entro 30 giorni
                dalla comunicazione di avvenuta erogazione del buono.</strong
              >
            </p>
          </q-card-section>
        </template>
      </q-card>
      <!-- PULSANTE INVIO -->
      <div class="row button-action-stepper q-my-xxl">
        <div class="col-12 col-md second-block">
          <div class="row justify-end q-gutter-y-lg">
            <div class="col-auto">
              <q-btn
                class="q-ml-xl"
                color="primary"
                @click="inviaControdeduzione()"
                label="INVIA CONTRODEDUZIONE"
                :disable="isInvalidValid"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import moment from "moment";
import store from "src/store/index";
import {
  putRichiesta,
  getRichiesta,
  postAllegato,
  postCronologia,
} from "src/services/api";
import {
  required,
} from "vuelidate/lib/validators";
import { ACCEPTED_FILES_TYPES, MAX_FILE_SIZE } from "src/services/config";
export default {
  name: "PageCounterDeduction",
  components: {},
  data () {
    return {
      id: null,
      allegatiOk: true,
      url: window.location.origin + "/buonoresbff/api/v1/allegato",
      titolo_options: store.state.titoli_studio,
      rapporto_options: store.state.tipi_rapporto,
      asl_options: store.state.asl,
      contratto_options: store.state.tipi_contratto,
      fileSize: MAX_FILE_SIZE,
      fileAccepted: ACCEPTED_FILES_TYPES,
      precedente_NOMINA_TUTORE: null,
      precedente_PROCURA_SPECIALE: null,
      precedente_DELEGA: null,
      precedente_CARTA_IDENTITA: null,
      precedente_VERBALE_UVG: null,
      precedente_VERBALE_UMVD: null,
      precedente_DENUNCIA_INPS: null,
      precedente_CONTRATTO_LAVORO: null,
      precedente_CONTRATTO_LAVORO_COOP: null,
      optionsRichiestaValMulti: [
        {
          label:
            "Il destinatario è persona di età pari o superiore a 65 anni, non autosufficiente",
          value: "UVG",
        },
        {
          label:
            "Il destinatario è persona con disabilità, non autosufficiente",
          value: "UMVD",
        },
      ],
      richiesta: {
        note_richiedente: "",
        attestazione_isee: false,
        punteggio_bisogno_sociale: null,
        valutazione_multidimensionale: null,
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
        asl_destinatario: null,
        delega: null,
        attestazione_isee: false,
        punteggio_bisogno_sociale: null,
        contratto: {
          data_inizio: null,
          data_fine: null,
          tipo: null,
          struttura: null,
        },
        nessuna_incompatibilita: null,
      },
      allegati: {
        CONTRODEDUZIONE: null,
      },
    };
  },
  filters: {
    formatDate: function (date) {
      return date ? moment(date).format("DD/MM/YYYY") : null;
    },
  },
  validations: {
    richiesta: {
      note_richiedente: { required },
    },
  },
  methods: {
    checkAge (date) {
      return moment().diff(new Date(date), "years");
    },
    textToUpper (val) {
      this.richiesta.domicilio_destinatario.provincia = val.toUpperCase();
    },
    // PER ORA FA SOLO IL TOUCH DEL FORM
    submit () {
      this.$v.$touch();
      if (this.$v.$error) {
        this.$q.notify({
          type: "negative",
          message: `Ricontrolla i campi del form`,
        });
        return;
      }
    },

    caricaDocumento (type) {
      this.$refs[type].pickFiles();
    },
    async postAllegatoSingolo (type) {
      try {
        let { data } = await postAllegato(
          this.richiesta.numero,
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
      } catch (err) {
        this.allegatiOk = false;
        let message =
          err.response && err.response.data.detail
            ? err.response.data.detail[0].valore
            : "Problemi server, contattare assistenza";
        this.$q.notify({
          type: "negative",
          message: message,
        });
        await store.dispatch("setSpinner", false);
      }
    },
    checkFileSize (files) {
      return files.filter((file) => file.size < MAX_FILE_SIZE);
    },

    onRejected () {
      this.$q.notify({
        type: "negative",
        message: `File non idoneo, controlla tipo e grandezza`,
      });
    },
    async inviaControdeduzione (invia) {
      // PROVIAMO PRIMA SE GLI ALLEGATI VANNO BENE DIVERSAMENTE ANNULLIAMO ANCHE LA PUT
      await store.dispatch("setSpinner", true);

      for (let attribute in this.allegati) {
        try {
          if (this.allegati[attribute]) {
            await this.postAllegatoSingolo(attribute);
          }
        } catch (err) {
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

      if (this.allegatiOk) {
        try {
          let { data } = await putRichiesta(
            this.richiesta.numero,
            this.richiesta
          );
          await store.dispatch(
            "setRichiesta",
            JSON.parse(JSON.stringify(data))
          );

          try {
            await postCronologia(this.richiesta.numero, "CONTRODEDOTTA");
            await store.dispatch("setSpinner", false);
            this.$q.notify({
              type: "positive",
              message: "Controdeduzione inviata",
              timeout: 6000,
            });
            this.$router.push("/");
          } catch (err) {
            let message =
              err.response && err.response.data.detail
                ? err.response.data.detail[0].valore
                : "Problemi server, contattare assistenza";
            this.$q.notify({
              type: "negative",
              message: message,
            });
            await store.dispatch("setSpinner", false);
          }
        } catch (err) {
          let message =
            err.response && err.response.data.detail
              ? err.response.data.detail[0].valore
              : "Problemi server, contattare assistenza";
          this.$q.notify({
            type: "negative",
            message: message,
            timeout: 6000,
          });
          await store.dispatch("setSpinner", false);
        }
      }
    },
  },
  computed: {
    appropriateAge () {
      return (
        this.checkAge(this.richiesta.destinatario.data_nascita) < 66 &&
        this.checkAge(this.richiesta.destinatario.data_nascita) >= 18
      );
    },

    richiestaStore () {
      return store.getters["getRichiesta"];
    },
    isInvalidValid () {
      return this.$v ? this.$v.$invalid : null;
    },
  },
  async created () {
    // PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA
    await store.dispatch("setSpinner", true);
    // SERVE PER RIMETTERE IN CIMA IL COMPONENTE
    window.scrollTo(0, 0);
    // SE SIAMO IN EDIT PRENDIAMO ID NUMERO DALLA URL
    let id = this.$route.params.id ? this.$route.params.id : null;
    try {
      let { data } = await getRichiesta(id);
      this.richiesta = data;
      if (
        this.richiesta.stato != "PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA"
      ) {
        await store.dispatch("setSpinner", false);
        this.$q.notify({
          type: "negative",
          message:
            "La domanda deve essere in stato 'preavviso di diniego per non ammissibilità' per essere modificabile",
          timeout: 6000,
        });
        this.$router.push("/");
        return;
      }
      if (data["allegati"]) {
        let allegati = JSON.parse(JSON.stringify(data["allegati"]));
        for (let allegato of allegati) {
          this["precedente_" + allegato.tipo] = allegato;
        }
      }
      await store.dispatch("setRichiesta", JSON.parse(JSON.stringify(data)));
      await store.dispatch("setSpinner", false);
    } catch (err) {
      await store.dispatch("resetAllStep");
      await store.dispatch("setSpinner", false);
      let message =
        err.response && err.response.data.detail
          ? err.response.data.detail[0].valore
          : "Problemi server, contattare assistenza";
      this.$q.notify({
        type: "negative",
        message: message,
      });
      this.$router.push("/");
      console.error(err);
    }
  },
};
</script>

<style lang="sass">
.red
  color: red
</style>
