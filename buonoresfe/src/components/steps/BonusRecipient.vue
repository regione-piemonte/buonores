<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div>
    <template>
      <p class="text--error">
        Tutti i campi contrassegnati con * sono obbligatori
      </p>
      <!-- CARD RADIO -->
      <q-card class="q-mt-xl q-mb-xl" v-if="richiestaStore">
        <q-card-section>
          <h3 id="destinatario-options-desc">
            <span :class="$v.stepperForm.destinatario_radio.$invalid
                        ? 'required-field'
                        : ''"
              >*</span
            >Per chi stai richiedendo il buono?
          </h3>
          <div>
            <q-option-group
              aria-describedby="destinatario-options-desc"
              ref="radio"
              :options="destinatario_options"
              type="radio"
              v-model="stepperForm.destinatario_radio"
              inline
            />
          </div>
        </q-card-section>
      </q-card>
      <!-- CARD DESTINATARIO -->
      <q-card
        class="q-mt-xl q-mb-xl"
        v-if="
          stepperForm.destinatario_radio &&
          stepperForm.destinatario_radio == 'other'
        "
      >
        <q-card-section>
          <h3>Dati destinatario</h3>
          <!-- CODICE FISCALE DA VERIFICARE -->
          <label for="codiceFiscale">
            <span :class="!richiesta.destinatario.cf ? 'required-field' : ''"
              >*</span
            >Inserisci codice
          </label>
          <CheckCF
            :codice_fiscale="destinatarioCF"
            :type="'destinatario'"
            @verificaCF="verificaCF($event)"
          />
          <!-- DATI SE CORRISPONDE SARANNO NASCOSTI -->
          <template v-if="stepperForm.show_destinatario">
            <div class="row q-mb-xl">
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
                  <dt>Stato</dt>
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
                  <dt>Provincia</dt>
                  <dd>
                    {{ richiesta.destinatario.provincia_residenza }}
                  </dd>
                </dl>
              </div>
            </div>
            <h2 id="domicilio-options-desc" class="card-title--label">
              <span
                :class="
                  $v.stepperForm.domicilio_radio.$invalid
                    ? 'required-field'
                    : ''
                "
                >*</span
              >Il domicilio coincide con la residenza?
            </h2>
            <div class="q-gutter-sm">
              <q-option-group
                aria-describedby="domicilio-options-desc"
                ref="radio"
                :options="domicilio_options"
                type="radio"
                v-model="stepperForm.domicilio_radio"
                inline
              />
            </div>
            <div
              class="row q-mb-xl justify-between"
              v-if="stepperForm.domicilio_radio == 'false'"
            >
              <div class="col-12 q-mt-xl">
                <label for="indirizzo_destinatarioIndirizzo">
                  <span
                    :class="
                      $v.richiesta.domicilio_destinatario.indirizzo.$invalid
                        ? 'required-field'
                        : ''
                    "
                    >*</span
                  >Indirizzo domicilio
                </label>
                <q-input
                  id="indirizzo_destinatarioIndirizzo"
                  v-model="richiesta.domicilio_destinatario.indirizzo"
                  outlined
                ></q-input>
              </div>
              <div class="col-12 col-md-6 q-mt-xl">
                <label for="indirizzo_destinatarioComune">
                  <span
                    :class="
                      $v.richiesta.domicilio_destinatario.comune.$invalid
                        ? 'required-field'
                        : ''
                    "
                    >*</span
                  >Comune domicilio
                </label>
                <q-input
                  id="indirizzo_destinatarioComune"
                  v-model="richiesta.domicilio_destinatario.comune"
                  outlined
                ></q-input>
              </div>
              <div class="col-12 col-md-4 q-mt-xl">
                <div v-if="richiesta.domicilio_destinatario">
                  <label for="domicilio_destinatarioProvincia">
                  <span
                    :class="
                      $v.richiesta.domicilio_destinatario.provincia.$invalid
                        ? 'required-field'
                        : ''
                    "
                    >*</span
                  >Provincia domicilio
                </label>
                  <q-input
                    id="domicilio_destinatarioProvincia"
                    :value="richiesta.domicilio_destinatario.provincia"
                    @input="textToUpper"
                    outlined
                  ></q-input>
                  <div
                    class="text--error"
                    v-if="
                      !$v.richiesta.domicilio_destinatario.provincia
                        .maxLength ||
                      !$v.richiesta.domicilio_destinatario.provincia.minLength
                    "
                  >
                    Inserire solo codice provincia a due lettere (esempio: TO)
                  </div>
                </div>
              </div>
            </div>
            <div class="row q-mb-xl">
              <div class="col-12 col-lg-8 q-mr-sm">
                <label for="asl-select">
                  <span
                    :class="
                      $v.richiesta.asl_destinatario.$invalid
                        ? 'required-field'
                        : ''
                    "
                    >*</span
                  >Asl
                </label>
                <div>
                  <lms-select
                    id="asl-select"
                    v-model="richiesta.asl_destinatario"
                    :options="asl_options"
                    :rules="[
                      (val) => val !== null || 'Inserire Asl di appartenenza',
                    ]"
                    bottom-slots
                    lazy-rules
                    name="destinatario-asl-app"
                    no-error-icon
                    required
                  />
                </div>
              </div>
            </div>
            <div class="row q-mb-xl q-gutter-y-lg">
              <div class="col-12 col-lg-8 q-mr-sm">
                <label for="studio-select">
                  <span
                    :class="
                      $v.richiesta.studio_destinatario.$invalid
                        ? 'required-field'
                        : ''
                    "
                    >*</span
                  >Titolo studio
                </label>
                <lms-select
                  id="studio-select"
                  v-model="richiesta.studio_destinatario"
                  :options="titolo_options"
                  :rules="[
                    (val) => val !== null || 'Inserire titolo di studio',
                  ]"
                  bottom-slots
                  lazy-rules
                  name="destinatario-studio-app"
                  no-error-icon
                  required
                />
              </div>
              <div class="col-12 q-mt-lg">
                <template v-if="appropriateAge">
                  <h2 id="lavoro-options-desc" class="card-title--label">
                    <span
                      :class="
                        $v.richiesta.lavoro_destinatario.$invalid
                          ? 'required-field'
                          : ''
                      "
                      >*</span
                    >Lavorativamente occupato
                  </h2>
                  <div class="q-gutter-sm">
                    <q-option-group
                      aria-describedby="lavoro-options-desc"
                      ref="radio"
                      :options="lavoro_options"
                      type="radio"
                      v-model="richiesta.lavoro_destinatario"
                      inline
                    />
                  </div>
                </template>
              </div>
            </div>
          </template>
        </q-card-section>
      </q-card>
      <!-- CARD RICHIEDENTE -->
      <q-card class="q-mt-xl q-mb-xl" v-if="stepperForm.destinatario_radio">
        <q-card-section>
          <h3>I tuoi dati</h3>
          <div class="row q-mb-xl">
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
                <dt>Comune nascita</dt>
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
                <dt>Stato</dt>
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
                <dt>Provincia</dt>
                <dd>
                  {{ richiesta.richiedente.provincia_residenza }}
                </dd>
              </dl>
            </div>
          </div>
          <template
            v-if="
              stepperForm.destinatario_radio &&
              stepperForm.destinatario_radio == 'me'
            "
          >
            <div class="row q-mb-xl">
              <div class="col-12 col-lg-8 q-mr-sm">
                <label for="asl-select">
                  <span
                    :class="
                      $v.richiesta.asl_destinatario.$invalid
                        ? 'required-field'
                        : ''
                    "
                    >*</span
                  >Asl
                </label>
                <lms-select
                  id="asl-select"
                  v-model="richiesta.asl_destinatario"
                  :options="asl_options"
                  :rules="[
                    (val) => val !== null || 'Inserire Asl di appartenenza',
                  ]"
                  bottom-slots
                  label-for="asl-select"
                  lazy-rules
                  name="destinatario-asl-app"
                  no-error-icon
                  required
                />
              </div>
            </div>
          </template>
          <!-- SE IL RICHIEDENTE COINCIDE CON IL DESTINATARIO SERVE TITOLO E LAVORO -->
          <template
            v-if="
              stepperForm.destinatario_radio &&
              stepperForm.destinatario_radio == 'me'
            "
          >
            <div class="row q-mb-xl q-gutter-y-lg">
              <div class="col-12 col-md-8">
                <label for="studio-select">
                  <span
                    :class="
                      $v.richiesta.studio_destinatario.$invalid
                        ? 'required-field'
                        : ''
                    "
                    >*</span
                  >Titolo studio
                </label>
                <lms-select
                  id="studio-select"
                  v-model="richiesta.studio_destinatario"
                  :options="titolo_options"
                  :rules="[
                    (val) => val !== null || 'Inserire titolo di studio',
                  ]"
                  bottom-slots
                  lazy-rules
                  name="destinatario-studio-app"
                  no-error-icon
                  required
                />
              </div>
              <div class="col-12 col-md-5">
                <template v-if="appropriateAge">
                  <h2 id="lavoro-options-desc" class="card-title--label">
                    <span
                      :class="
                        $v.richiesta.lavoro_destinatario.$invalid
                          ? 'required-field'
                          : ''
                      "
                      >*</span
                    >Lavorativamente occupato
                  </h2>
                  <div class="q-gutter-sm">
                    <q-option-group
                      aria-describedby="lavoro-options-desc"
                      ref="radio"
                      :options="lavoro_options"
                      type="radio"
                      v-model="richiesta.lavoro_destinatario"
                      inline
                    />
                  </div>
                </template>
              </div>
            </div>
          </template>
          <template
            v-if="
              stepperForm.destinatario_radio &&
              stepperForm.destinatario_radio == 'other'
            "
          >
            <div class="row q-mb-xl">
              <div class="col-12 col-lg-8 q-mr-sm">
                <label for="relazione-select">
                  <span
                    :class="
                      $v.richiesta.delega.$invalid ? 'required-field' : ''
                    "
                    >*</span
                  >Richiedente opera in qualità di
                </label>
                <lms-select
                  id="relazione-select"
                  v-model="richiesta.delega"
                  :options="rapporto_options"
                  :rules="[(val) => val !== null || 'Inserire relazione']"
                  bottom-slots
                  lazy-rules
                  name="destinatario-relazione-app"
                  no-error-icon
                  required
                />
              </div>
            </div>
            <div class="row q-mb-xl">
              <div class="col-12 col-lg-8 q-mr-sm border-left--attach">
                <p>
                  Allegare la procura speciale (e la carta d'identità del
                  delegante) se il richiedente opera in qualità di:
                </p>
                <ul>
                  <li>
                    componente nucleo famigliare convivente ai fini anagrafici;
                  </li>
                  <li>coniuge o parte dell'unione civile non conviventi;</li>
                  <li>
                    parente in linea diretta di 1° grado (genitori-figli) non
                    convivente;
                  </li>
                  <li>curatela;</li>
                  <li>amministrazione di sostegno;</li>
                  <li>altro.</li>
                </ul>
                <div>
                  <a
                    class="btn--flat q-mt-lg"
                    href="doc/mod_procura_speciale.pdf"
                    target="_blank"
                  >
                    SCARICA MODELLO DOCUMENTO DI PROCURA SPECIALE
                  </a>
                  <p class="q-mt-xl">
                    Nel caso di Tutela legale allegare il documento di domina
                    del tutore legale.
                  </p>
                </div>
              </div>
            </div>
            <div class="row q-mb-xl q-gutter-y-lg">
              <div class="col-12 col-lg-6 q-pa-xs" v-if="cartaIdentitaAllowed">
                <h2>
                  <template v-if="cartaIdentitaRequired"
                    ><span
                      :class="!allegati.CARTA_IDENTITA ? 'required-field' : ''"
                      >*</span
                    ></template
                  >Carta d'identità del delegante
                </h2>
                <q-file
                  ref="CARTA_IDENTITA"
                  outlined
                  clearable
                  v-model="allegati.CARTA_IDENTITA"
                  label="Documento delegante"
                  class="q-mb-xl hidden"
                  :accept="fileAccepted"
                  :max-file-size="fileSize"
                  :filter="checkFileSize"
                  @rejected="onRejected"
                ></q-file>

                <q-btn
                  v-if="allegati.CARTA_IDENTITA == null"
                  @click="caricaDocumento('CARTA_IDENTITA')"
                  color="primary"
                  label="ALLEGA"
                  aria-label="carica il documento di carta d'identità"
                ></q-btn>

                <div v-if="allegati.CARTA_IDENTITA" class="q-my-lg">
                  <q-btn
                    icon="delete"
                    class="q-mr-md"
                    flat
                    color="negative"
                    @click="allegati.CARTA_IDENTITA = null"
                    :aria-label="
                      'elimina il documento' + allegati.CARTA_IDENTITA.name
                    "
                  />{{ allegati.CARTA_IDENTITA.name }}
                </div>
              </div>
              <div class="col-12 col-lg-4 q-pa-xs" v-if="procuraAllowed">
                <h2>
                  <template v-if="procuraRequired"
                    ><span class="sr-only"
                      >Visualizza il documento caricato per</span
                    >
                    <span
                      :class="
                        !allegati.PROCURA_SPECIALE ? 'required-field' : ''
                      "
                      >*</span
                    ></template
                  >Procura speciale
                </h2>
                <q-file
                  ref="PROCURA_SPECIALE"
                  outlined
                  clearable
                  v-model="allegati.PROCURA_SPECIALE"
                  label="Documento procura speciale"
                  class="q-mb-xl hidden"
                  :accept="fileAccepted"
                  :max-file-size="fileSize"
                  :filter="checkFileSize"
                  @rejected="onRejected"
                ></q-file>

                <q-btn
                  v-if="allegati.PROCURA_SPECIALE == null"
                  @click="caricaDocumento('PROCURA_SPECIALE')"
                  color="primary"
                  label="ALLEGA"
                  aria-label="carica la procura speciale"
                ></q-btn>

                <div v-if="allegati.PROCURA_SPECIALE" class="q-my-lg">
                  <q-btn
                    icon="delete"
                    class="q-mr-md"
                    flat
                    color="negative"
                    @click="allegati.PROCURA_SPECIALE = null"
                    :aria-label="
                      'elimina il documento' + allegati.PROCURA_SPECIALE.name
                    "
                  />{{ allegati.PROCURA_SPECIALE.name }}
                </div>
              </div>

              <div class="col-12 col-lg-4 q-pa-xs" v-if="nominaTutoreAllowed">
                <h2>
                  <template v-if="nominaTutoreRequired"
                    ><span class="sr-only"
                      >Visualizza il documento caricato per</span
                    >
                    <span
                      :class="!allegati.NOMINA_TUTORE ? 'required-field' : ''"
                      >*</span
                    ></template
                  >Nomina tutore
                </h2>
                <q-file
                  ref="NOMINA_TUTORE"
                  outlined
                  clearable
                  v-model="allegati.NOMINA_TUTORE"
                  label="Documento nomina tutore"
                  class="q-mb-xl hidden"
                  :accept="fileAccepted"
                  :max-file-size="fileSize"
                  :filter="checkFileSize"
                  @rejected="onRejected"
                ></q-file>

                <q-btn
                  v-if="allegati.NOMINA_TUTORE == null"
                  @click="caricaDocumento('NOMINA_TUTORE')"
                  color="primary"
                  label="ALLEGA"
                  aria-label="carica la nomina tutore"
                ></q-btn>

                <div v-if="allegati.NOMINA_TUTORE" class="q-my-lg">
                  <q-btn
                    icon="delete"
                    class="q-mr-md"
                    flat
                    color="negative"
                    @click="allegati.NOMINA_TUTORE = null"
                    :aria-label="
                      'elimina il documento' + allegati.NOMINA_TUTORE.name
                    "
                  />{{ allegati.NOMINA_TUTORE.name }}
                </div>
              </div>
            </div>
          </template>
        </q-card-section>
      </q-card>
    </template>
  </div>
</template>

<script>
import { postRichiesta, postAllegato } from "src/services/api";
import { ACCEPTED_FILES_TYPES, MAX_FILE_SIZE } from "src/services/config";
import moment from "moment";
import store from "src/store/index";
import LmsSelect from "components/core/LmsSelect";
import CheckCF from "components/CheckCF";
import {
  required,
  requiredIf,
  minLength,
  maxLength,
} from "vuelidate/lib/validators";

export default {
  name: "BonusRecipient",
  props: ["saveCurrentPage"],
  components: { LmsSelect, CheckCF },
  data() {
    return {
      destinatarioCF: "",
      modificaCF: false,
      fileSize: MAX_FILE_SIZE,
      fileAccepted: ACCEPTED_FILES_TYPES,
      numero: null,
      titolo_options: store.state.titoli_studio,
      rapporto_options: store.state.tipi_rapporto,
      asl_options: store.state.asl,
      destinatario_options: [
        {
          label: "Per me",
          value: "me",
        },
        {
          label: "Per un'altra persona",
          value: "other",
        },
      ],
      domicilio_options: [
        {
          label: "Si",
          value: "true",
        },
        {
          label: "No",
          value: "false",
        },
      ],

      lavoro_options: [
        {
          label: "Si",
          value: true,
        },
        {
          label: "No",
          value: false,
        },
      ],
      stepperForm: {
        domicilio_radio: null,
        show_destinatario: null,
        destinatario_radio: null,
      },
      richiesta: {
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
        note: "",
        studio_destinatario: null,
        lavoro_destinatario: null,
        domicilio_destinatario: {
          indirizzo: null,
          comune: null,
          provincia: "",
        },
        asl_destinatario: null,
        delega: null,
      },
      allegati: {
        CARTA_IDENTITA: null,
        PROCURA_SPECIALE: null,
        NOMINA_TUTORE: null,
      },
    };
  },
  filters: {
    formatDate: function (date) {
      return date ? moment(date).format("DD/MM/YYYY") : null;
    },
  },
  computed: {
    // SERVE PER CAPIRE SE L'UTENTE POTRA' ANDARE AVANTI NEGLI STEP, FORM DEVE ESSERE VALIDO
    utenteRichiedente() {
      return store.getters["getUtenteRichiedente"];
    },
    isInvalidValid() {
      return this.$v ? this.$v.$invalid : null;
    },
    richiestaStore() {
      return store.getters["getRichiesta"];
    },
    destinatarioInput() {
      return this.stepperForm.destinatario_radio;
    },
    domicilioInput() {
      return this.stepperForm.domicilio_radio;
    },
    rapportoFiltrato() {
      return this.rapporto_options &&
        this.rapporto_options.filter(
          (tipo) => tipo.codice === this.richiesta.delega
        )[0]
        ? this.rapporto_options.filter(
            (tipo) => tipo.codice === this.richiesta.delega
          )[0]["allegato"]
        : null;
    },

    cartaIdentitaAllowed() {
      return this.rapportoFiltrato
        ? this.rapportoFiltrato.filter(
            (tipo) => tipo.allegato_tipo_cod == "CARTA_IDENTITA"
          )[0]
        : null;
    },
    cartaIdentitaRequired() {
      return this.cartaIdentitaAllowed && this.rapportoFiltrato
        ? this.rapportoFiltrato.filter(
            (tipo) => tipo.allegato_tipo_cod == "CARTA_IDENTITA"
          )[0].allegato_obbligatorio
        : null;
    },
    procuraAllowed() {
      return this.rapportoFiltrato
        ? this.rapportoFiltrato.filter(
            (tipo) => tipo.allegato_tipo_cod == "PROCURA_SPECIALE"
          )[0]
        : null;
    },
    procuraRequired() {
      return this.procuraAllowed && this.rapportoFiltrato
        ? this.rapportoFiltrato.filter(
            (tipo) => tipo.allegato_tipo_cod == "PROCURA_SPECIALE"
          )[0].allegato_obbligatorio
        : null;
    },
    nominaTutoreAllowed() {
      return this.rapportoFiltrato
        ? this.rapportoFiltrato.filter(
            (tipo) => tipo.allegato_tipo_cod == "NOMINA_TUTORE"
          )[0]
        : null;
    },
    nominaTutoreRequired() {
      return this.nominaTutoreAllowed && this.rapportoFiltrato
        ? this.rapportoFiltrato.filter(
            (tipo) => tipo.allegato_tipo_cod == "NOMINA_TUTORE"
          )[0].allegato_obbligatorio
        : null;
    },

    // CONTROLLO SE E' IN ETA' LAVORATIVA ALTRIMENTI NON LO CHIEDIAMO
    appropriateAge() {
      return (
        this.checkAge(this.richiesta.destinatario.data_nascita) < 66 &&
        this.checkAge(this.richiesta.destinatario.data_nascita) >= 18
      );
    },
  },
  validations: {
    stepperForm: {
      destinatario_radio: { required },
      domicilio_radio: {
        required: requiredIf(function () {
          return this.stepperForm.destinatario_radio === "other";
        }),
      },
    },

    richiesta: {
      asl_destinatario: { required },
      studio_destinatario: { required },
      lavoro_destinatario: {
        required: requiredIf(function () {
          return this.appropriateAge;
        }),
      },
      domicilio_destinatario: {
        indirizzo: {
          required: requiredIf(function () {
            return this.stepperForm.domicilio_radio == "false";
          }),
        },
        comune: {
          required: requiredIf(function () {
            return this.stepperForm.domicilio_radio == "false";
          }),
        },
        provincia: {
          required: requiredIf(function () {
            return this.stepperForm.domicilio_radio == "false";
          }),
          maxLength: maxLength(2),
          minLength: minLength(2),
        },
      },
      delega: {
        required: requiredIf(function () {
          return this.stepperForm.destinatario_radio === "other";
        }),
      },
    },
    allegati: {
      CARTA_IDENTITA: {
        required: requiredIf(function () {
          return this.cartaIdentitaRequired;
        }),
      },
      PROCURA_SPECIALE: {
        required: requiredIf(function () {
          return this.procuraRequired;
        }),
      },
      NOMINA_TUTORE: {
        required: requiredIf(function () {
          return this.nominaTutoreRequired;
        }),
      },
    },
  },
  methods: {
    textToUpper(val) {
      this.richiesta.domicilio_destinatario.provincia = val.toUpperCase();
    },
    // FORMATO DATA YYYY-MM-DD
    checkAge(date) {
      return moment().diff(new Date(date), "years");
    },
    checkFileSize(files) {
      return files.filter((file) => file.size < MAX_FILE_SIZE);
    },

    onRejected() {
      this.$q.notify({
        type: "negative",
        message: `File non idoneo, controlla tipo e grandezza`,
      });
    },
    async saveStepForm() {
      await store.dispatch("setSpinner", true);

      // SE SCELGO 'ME' SALVO I DATI DEL RICHIEDENTE NEI CAMPI DESTINATARIO
      if (this.stepperForm.destinatario_radio === "me") {
        this.richiesta.destinatario = this.richiesta.richiedente;
      }
      // POPOLO ANCHE DOMICILIO DESTINATARIO IN OGNI CASO SE DIVERSO DA 'NO'
      if (this.stepperForm.domicilio_radio == null) {
        this.richiesta.domicilio_destinatario = {
          indirizzo: this.richiesta.richiedente.indirizzo_residenza,
          comune: this.richiesta.richiedente.comune_residenza,
          provincia: this.richiesta.richiedente.provincia_residenza,
        };
      } else if (this.stepperForm.domicilio_radio == "true") {
        this.richiesta.domicilio_destinatario = {
          indirizzo: this.richiesta.destinatario.indirizzo_residenza,
          comune: this.richiesta.destinatario.comune_residenza,
          provincia:
            this.richiesta.destinatario.provincia_residenza.toUpperCase(),
        };
      }

      // CHIAMATA POST

      try {
        let { data } = await postRichiesta(this.richiesta);
        this.numero = data.numero;
        this.numero = JSON.parse(JSON.stringify(this.numero));
        await store.dispatch("setStepperForm", this.stepperForm);
        await store.dispatch("setRichiesta", JSON.parse(JSON.stringify(data)));
        for (let attribute in this.allegati) {
          try {
            if (this.allegati[attribute]) {
              await this.postAllegatoSingolo(attribute);
            }
          } catch (err) {
            let message = err.response.data.detail
              ? err.response.data.detail[0].valore
              : "Problemi server, contattare assistenza";
            this.$q.notify({
              type: "negative",
              message: message,
            });
          }
        }
        await this.$store.dispatch("setNextStep", {
          bool: true,
          step: 2,
        });
        await this.$store.dispatch("setStep", 2);
        await store.dispatch("setSpinner", false);
      } catch (err) {
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
      }
    },

    resetDomicilio() {
      this.richiesta.domicilio_destinatario = {
        indirizzo: null,
        comune: null,
        provincia: "",
      };
    },

    resetForm() {
      this.stepperForm.show_destinatario = null;
      this.stepperForm.domicilio_radio = null;
      this.richiesta.studio_destinatario = null;
      this.richiesta.lavoro_destinatario = null;
      this.richiesta.delega = null;
      this.richiesta.asl_destinatario = null;
      // DA VEDERE GLI ALLEGATI COME LI VOGLIONO
      this.allegati = {
        CARTA_IDENTITA: null,
        PROCURA_SPECIALE: null,
        NOMINA_TUTORE: null,
      };
      this.resetDomicilio();
    },

    resetFormCheckCf() {
      this.stepperForm.show_destinatario = null;
      this.stepperForm.domicilio_radio = null;
      this.richiesta.studio_destinatario = null;
      this.richiesta.lavoro_destinatario = null;
      this.richiesta.asl_destinatario = null;
      this.richiesta.destinatario = {
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
      };
      this.resetDomicilio();
    },

    async postAllegatoSingolo(type) {
      try {
        let { data } = await postAllegato(
          this.numero,
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
        console.error(err);
      }
    },
    async verificaCF(obj) {
      this.resetFormCheckCf();
      if (obj.error) {
        this.stepperForm.show_destinatario = null;
      }
      if (obj.payload) {
        this.richiesta.destinatario = JSON.parse(JSON.stringify(obj.payload));
        this.stepperForm.show_destinatario = true;
      }
    },
    // POPOLEREMO VERAMENTE IL DESTINATARIO PER ORA SOLO MOCK
    caricaDocumento(type) {
      this.$refs[type].pickFiles();
    },
  },
  watch: {
    // RESETTO IL FORM QUANDO FACCIO SWITCH PER EVITARE DATI SPORCHI
    destinatarioInput() {
      store.dispatch("setNextStep", {
        bool: this.$v.$invalid,
        step: 1,
      });
      this.resetForm();
    },
    // RESETTO DOMICILIO SE C'E' SELEZIONA NO
    domicilioInput() {
      this.resetDomicilio();
    },
    // SE C'E' UN CAMBIAMENTO FA SCATTARE UN METODO NELLO STORE ABILITA E DISABILITA IL PULSANTE AVANTI SU STEPPER
    isInvalidValid() {
      store.dispatch("setNextStep", {
        bool: this.$v.$invalid,
        step: 1,
      });
    },
    saveCurrentPage: function (newVal, oldVal) {
      this.saveStepForm();
    },
  },
  async created() {
    // SERVE PER RIMETTERE IN CIMA IL COMPONENTE
    window.scrollTo(0, 0);
    this.richiesta.richiedente = JSON.parse(
      JSON.stringify(store.getters["getUtenteRichiedente"])
    );
  },
};
</script>

<style lang="scss">
label {
  display: block;
}
</style>
