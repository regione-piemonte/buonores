<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div>
    <!-- DICHIARAZIONE -->
    <div class="q-mt-xl q-mb-xl">
      <p>
        <strong
          >Consapevole delle sanzioni penali richiamate dall'art. 76 del D.P.R.
          445/2000 nel caso di dichiarazioni non veritiere e falsità in atti con
          riferimento a quanto dichiarato DICHIARA che, alla data della
          presentazione della domanda:</strong
        >
      </p>
      <p class="text--error">
        Tutti i campi contrassegnati con * sono obbligatori
      </p>
    </div>
    <!-- CARD ISEE -->
    <q-card class="q-mt-xl q-mb-xl">
      <q-card-section>
        <h2>
          Attestazione
          <abbr title="Indicatore della Situazione Economica Equivalente"
            >I.S.E.E.</abbr
          >
          socio - sanitario
        </h2>
        <div class="row q-my-xl justify-between">
          <div class="col-auto col-lg-1">
            <q-toggle
              v-model="richiesta.attestazione_isee"
              aria-labelledby="attestazioneIsee"
              id="attestazioneIseeToggle"
              name="attestazioneIseeToggle"
            />
          </div>
          <label
            class="col col-lg-10"
            id="attestazioneIsee"
            for="attestazioneIseeToggle"
          >
            <span
              :class="
                $v.richiesta.attestazione_isee.$invalid ? 'required-field' : ''
              "
              >*</span
            >

            Il destinatario è in possesso, alla data dell�??invio della domanda,
            di un attestato
            <abbr title="Indicatore della Situazione Economica Equivalente"
              >I.S.E.E.</abbr
            >
            socio - sanitario (no I.S.E.E. residenziale) in corso di validità
            avente un valore inferiore
            <strong>50.000 euro (65.000 euro per minori e disabili)</strong>
          </label>
          <div class="col-1">
            <tooltip
              :titleBanner="iseeTooltip.title"
              :textBanner="iseeTooltip.text"
            />
          </div>
        </div>
      </q-card-section>
    </q-card>
    <!-- FINE CARD ISEE -->
    <!-- REQUISITI COMPATIBILITA'-->
    <q-card class="q-mt-xl">
      <q-card-section>
        <h2>
          Requisiti compatibilità
        </h2>
        <div class="row q-my-xl justify-between">
          <div class="col-auto col-lg-1">
            <q-toggle
              v-model="richiesta.nessuna_incompatibilita"
              aria-labelledby="richiestaNoInc"
              id="richiestaNoIncToggle"
              name="richiestaNoIncToggle"
            />
          </div>
          <label
            class="col col-lg-10"
            id="richiestaNoInc"
            for="richiestaNoIncToggle"
          >
            <span
              :class="
                $v.richiesta.nessuna_incompatibilita.$invalid ? 'required-field' : ''
              "
              >*</span
            >
            Il destinatario non è beneficiario di altre misure aventi natura di trasferimento monetario specificatamente destinato/a al sostegno della residenzialità
          </label>
          <div class="col-1">
            <!-- <tooltip
              :titleBanner="iseeTooltip.title"
              :textBanner="iseeTooltip.text"
            /> -->
          </div>
        </div>
     
        
      </q-card-section>
      <!-- ELENCO MISURE INCOMPATIBILI -->
      <q-list>
        <q-expansion-item
          dense
          dense-toggle
          expand-separator
          label="ELENCO MISURE INCOMPATIBILI con il Buono Residenzialità"
          header-class="bg-white text-black font-weight:bold"
        >
          <q-card class="border-left--primary">
            <q-card-section>
              <ul class="lower-roman">
                <li>
                  il contributo pubblico percepito dal destinatario del Buono
                  Residenzialità che risieda presso una struttura residenziale
                  in regime di �??convenzionamento�?? con il Sistema sanitario
                  regionale, nel quale il SSR riconosce i costi relativi alla
                  quota sanitaria, pari al 50% della retta mensile;
                </li>
                <li>
                  altri contributi pubblici rispondenti alla medesima finalità,
                  direttamente percepiti dall�??ospite o erogati alla struttura
                  ospitante dagli Enti gestori delle funzioni
                  socio-assistenziali o dai Comuni di residenza (contributi a
                  parziale o totale copertura della quota sociale), oppure
                  dall�??INPS nell�??ambito della misura �??Long Term Care�??.
                </li>
              </ul>
            </q-card-section>
          </q-card>
        </q-expansion-item>
      </q-list>
      <!-- ELENCO MISURE COMPATIBILI -->
      <q-list class="q-my-xl">
        <q-expansion-item
          dense
          dense-toggle
          expand-separator
          label="ELENCO MISURE COMPATIBILI con il Buono Residenzialità"
          header-class="bg-white text-black font-weight:bold"
        >
          <q-card class="border-left--primary">
            <q-card-section>
              <ul class="lower-roman">
                <li>
                  l�??assenza temporanea dalla struttura dovuta a ricoveri
                  ospedalieri o rientri temporanei al domicilio, nel caso in cui
                  l�??importo mensile fatturato �?? in relazione ai giorni di
                  effettiva presenza in struttura �?? sia almeno pari al valore
                  del Buono (600 euro).
                </li>
              </ul>
            </q-card-section>
          </q-card>
        </q-expansion-item>
      </q-list>
      <!-- CARD TIPOLOGIE -->
      <q-card-section class="q-my-xl">
        <strong>
          <span
            :class="
              $v.richiesta.valutazione_multidimensionale.$invalid
                ? 'required-field'
                : ''
            "
            >*</span
          >
          Seleziona tipologia persona
        </strong>
        <div class="q-my-lg">
          <q-radio
            dense
            v-model="richiesta.valutazione_multidimensionale"
            val="UVG"
            label="Il destinatario è persona di età pari o superiore a 65 anni, non autosufficiente con valutazione multidimensionale U.V.G. - ANZIANI"
          />
        </div>
        <q-radio
          dense
          v-model="richiesta.valutazione_multidimensionale"
          val="UMVD"
          label="Il destinatario è persona con disabilità, non autosufficiente con valutazione multidimensionale U.M.V.D. �?? DISABILI"
        />
      </q-card-section>
    </q-card>
    <!-- FINE REQUISITI COMPATIBILITA'-->
    <!-- CARD PUNTEGGIO SOCIALE -->
    <q-card class="q-mt-xl q-mb-xl">
      <q-card-section>
        <h2>Punteggio bisogno sociale</h2>
        <div>
          Questo dato attesta il grado di <strong>"bisogno sociale"</strong>, e
          viene assegnato a seguito di
          <strong>"valutazione multidimensionale"</strong> (<strong
            ><abbr title="Unità Valutativa Geriatrica">U.V.G.</abbr> -
            ANZIANI</strong
          >
          o
          <strong
            ><abbr
              title="Unità multidisciplinare di valutazione della disabilità"
              >U.M.V.D.</abbr
            >
            DISABILI</strong
          >), effettuata presso la ASL di residenza (la visita per la
          valutazione dev'essere richiesta con impegnativa dal Medico di
          Famiglia). Il punteggio sociale assegnato può raggiungere un massimo
          di <strong>14 punti</strong>, così suddivisi:
          <ol>
            <li>condizione abitativa: (max 2 punti);</li>
            <li>condizione economica: (max 4 punti);</li>
            <li>condizione famigliare: (max 4 punti);</li>
            <li>condizione assistenziale: (max 4 punti).</li>
          </ol>

          <span
            :class="
              $v.richiesta.punteggio_bisogno_sociale.$invalid
                ? 'required-field'
                : ''
            "
            >*</span
          >Il destinatario è già stato sottoposto a "<strong
            >valutazione multidimensionale</strong
          >" presso le Unità di Valutazione competenti (<abbr
            title="Unità Valutativa Geriatrica"
            >U.V.G.</abbr
          >
          - ANZIANI, o
          <abbr title="Unità multidisciplinare di valutazione della disabilità"
            >U.M.V.D.</abbr
          >
          - DISABILI), ed è in possesso di un punteggio sociale pari a:
        </div>
        <div class="q-mt-xl">
          <div class="row">
            <div class="col-10 col-md-8 col-lg-6">
              <q-input
                outlined
                type="number"
                v-model="richiesta.punteggio_bisogno_sociale"
                @blur="$v.richiesta.punteggio_bisogno_sociale.$touch"
                @keyup.enter="submit"
                :error="$v.richiesta.punteggio_bisogno_sociale.$error"
                :step="1"
                label="Inserisci il punteggio sociale"
              >
                <template v-slot:error>
                  Punteggio bisogno sociale deve essere minimo
                  {{
                    $v.richiesta.punteggio_bisogno_sociale.$params.between.min
                  }}, massimo
                  {{
                    $v.richiesta.punteggio_bisogno_sociale.$params.between.max
                  }}
                  e deve essere una cifra senza decimali
                </template>
              </q-input>
            </div>
          </div>
        </div>
        <div class="row q-my-xl">
          <div
            class="col-12 q-pa-xs"
            v-if="richiesta.valutazione_multidimensionale"
          >
            <q-file
              ref="VERBALE_UVG"
              outlined
              clearable
              v-model="allegati.VERBALE_UVG"
              class="q-mb-xl hidden"
              :accept="fileAccepted"
              :max-file-size="fileSize"
              :filter="checkFileSize"
              @rejected="onRejected"
            ></q-file>

            <q-file
              ref="VERBALE_UMVD"
              outlined
              clearable
              v-model="allegati.VERBALE_UMVD"
              class="q-mb-xl hidden"
              :accept="fileAccepted"
              :max-file-size="fileSize"
              :filter="checkFileSize"
              @rejected="onRejected"
            ></q-file>

            <div v-if="richiesta.valutazione_multidimensionale === 'UVG'">
              <!-- PRECEDENTE FILE UVG -->
              <div v-if="precedente_VERBALE_UVG">
                <q-btn
                  icon="delete"
                  class="q-mr-md"
                  flat
                  color="negative"
                  title="elimina il documento"
                  @click="precedente_VERBALE_UVG = null"
                  :aria-label="
                    'elimina il documento ' + precedente_VERBALE_UVG.filename
                  "
                />
                <a
                  :href="
                    url +
                    '/' +
                    richiesta.numero +
                    '/' +
                    precedente_VERBALE_UVG.tipo
                  "
                  target="_blank"
                >
                  <strong>{{ precedente_VERBALE_UVG.filename }} </strong>
                </a>
              </div>

              <div v-if="!precedente_VERBALE_UVG && !allegati.VERBALE_UVG">
                <h2 class="q-my-xl required-field">
                  *Allega il Verbale o Lettera di Comunicazione della
                  valutazione UVG
                </h2>
                <h3 class="title-card--alert q-mb-xl">
                  ATTENZIONE: <u>NON ALLEGARE</u> il verbale di invalidità
                  civile rilasciato dall'<abbr
                    title="Istituto nazionale di previdenza sociale"
                    >INPS</abbr
                  >
                </h3>
                <q-btn
                  @click="caricaDocumento('VERBALE_UVG')"
                  color="primary"
                  aria-label="allega il verbale o lettera di comunicazione della valutazione uvg"
                  label="*ALLEGA"
                ></q-btn>
              </div>
            </div>

            <div
              v-if="richiesta.valutazione_multidimensionale === 'UMVD'"
              class="q-my-xl"
            >
              <div v-if="precedente_VERBALE_UMVD">
                <q-btn
                  icon="delete"
                  class="q-mr-md"
                  flat
                  title="elimina il documento"
                  color="negative"
                  @click="precedente_VERBALE_UMVD = null"
                  :aria-label="
                    'elimina il documento ' + precedente_VERBALE_UMVD.filename
                  "
                />
                <a
                  :href="
                    url +
                    '/' +
                    richiesta.numero +
                    '/' +
                    precedente_VERBALE_UMVD.tipo
                  "
                  target="_blank"
                >
                  <strong>{{ precedente_VERBALE_UMVD.filename }} </strong>
                </a>
              </div>
              <div v-if="!precedente_VERBALE_UMVD && !allegati.VERBALE_UMVD">
                <h2 class="q-my-xl required-field">
                  *Allega il Verbale o Lettera di Comunicazione della
                  valutazione UMVD
                </h2>
                <h3 class="title-card--alert q-mb-xl">
                  ATTENZIONE: <u>NON ALLEGARE</u> il verbale di invalidità
                  civile rilasciato dall'<abbr
                    title="Istituto nazionale di previdenza sociale"
                    >INPS</abbr
                  >
                </h3>
                <q-btn
                  @click="caricaDocumento('VERBALE_UMVD')"
                  color="primary"
                  aria-label="allega il verbale o lettera di comunicazione della valutazione umvd"
                  label="*ALLEGA"
                ></q-btn>
              </div>
            </div>
          </div>
        </div>

        <div v-if="allegati.VERBALE_UVG" class="q-my-lg">
          <strong>Lettera di comunicazione/Verbale UVG:</strong
          ><q-btn
            icon="delete"
            class="q-mr-md"
            flat
            color="negative"
            title="elimina il documento"
            @click="allegati.VERBALE_UVG = null"
            :aria-label="'elimina il documento ' + allegati.VERBALE_UVG.name"
          />{{ allegati.VERBALE_UVG.name }}
        </div>
        <div v-if="allegati.VERBALE_UMVD">
          <strong>Lettera di comunicazione/Verbale UMVD:</strong>
          <q-btn
            icon="delete"
            class="q-mr-md"
            flat
            color="negative"
            title="elimina il documento"
            @click="allegati.VERBALE_UMVD = null"
            :aria-label="'elimina il documento ' + allegati.VERBALE_UMVD.name"
          />{{ allegati.VERBALE_UMVD.name }}
        </div>
      </q-card-section>
    </q-card>
    <!-- FINE CARD PUNTEGGIO SOCIALE -->
    <!-- CARD DATI STRUTTURA -->
    <q-card class="q-mt-xl q-mb-xl">
      <q-card-section>
        <h3>Inserimento dati struttura</h3>
      </q-card-section>
      <div class="q-pt-lg">
        <lms-banner type="info">
          <p>
            L�??assegnazione del buono è subordinata all�??adesione della struttura
          </p>
        </lms-banner>
      </div>
      <q-card-section>
        <p>
          <strong
            ><span
              :class="
                $v.richiesta.contratto.tipo.$invalid ? 'required-field' : ''
              "
              >*</span
            >
            Seleziona struttura ospitante</strong
          >
        </p>
        <q-radio
          dense
          v-model="richiesta.contratto.tipo"
          @keyup.enter="submit"
          val="CONTRATTO_RSA"
          label="Il destinatario è in possesso di un regolare contratto con una struttura aderente alla misura"
        />
        <!-- CONTRATTO RSA -->
        <template v-if="richiesta.contratto.tipo === 'CONTRATTO_RSA'">
          <div class="border-left--primary">
            <!-- CARD TITOLO PRIVATO -->
            <div class="row q-my-xl justify-between">
              <div class="col-auto col-lg-1">
                <q-toggle
                  v-model="richiesta.contratto.titolo_privato"
                  aria-labelledby="titoloPrivato"
                  id="titoloPrivato"
                  name="titoloPrivato"
                />
              </div>
              <label
                class="col col-lg-11"
                id="titoloPrivato"
                for="titoloPrivato"
              >
                <span
                  :class="
                    !richiesta.contratto.titolo_privato ? 'required-field' : ''
                  "
                  >*</span
                >Il destinatario è inserito in una struttura residenziale a
                carattere socio-sanitario o socio-assistenziale a
                <strong>titolo privato</strong> e non tramite convenzione con il
                Servizio Sanitario Regionale (SSR).
              </label>
            </div>
            <!-- OBBLIGATORIO CONFERMARE TITOLO PRIVATO -->
            <template
              v-if="richiesta.contratto && richiesta.contratto.titolo_privato"
            >
              <!-- SELEZIONE STRUTTURA -->
              <q-card-section class="q-my-xl">
                <div class="row">
                  <div class="col-11">
                    <strong>
                      <span
                        :class="
                          $v.richiesta.contratto.struttura.$invalid
                            ? 'required-field'
                            : ''
                        "
                        >*</span
                      >
                      Seleziona la struttura ospitante cercando per comune e poi
                      per denominazione
                    </strong>
                  </div>
                  <div class="col-1">
                    <tooltip
                      :titleBanner="strutturaTooltip.title"
                      :textBanner="strutturaTooltip.text"
                    />
                  </div>
                </div>
                <div class="q-my-lg">
                  <div class="row q-gutter-y-lg q-gutter-x-lg date-start-end">
                    <div class="col-12 col-lg-4">
                      <SelectSuggest
                        :options="optionsComuni"
                        :field="'comune'"
                        :label="'Seleziona nome comune'"
                        @setValue="setValue($event)"
                      ></SelectSuggest>
                    </div>
                    <template
                      v-if="
                        comuneSelect &&
                        optionsStrutture !== null &&
                        optionsStrutture.length > 0 &&
                        spinner == false
                      "
                    >
                      <div class="col-12 col-lg-7">
                        <SelectSuggest
                          :options="optionsStrutture"
                          :field="'richiesta'"
                          :subField="'contratto'"
                          :subFieldChild="'struttura'"
                          :label="'Seleziona nome struttura'"
                          :column="'nome'"
                          @setValue="setValue($event)"
                        ></SelectSuggest>
                      </div>
                    </template>
                    <template v-if="spinner">
                      <div class="q-pa-md flex flex-center">
                        <q-spinner color="primary" size="3em" />
                      </div>
                    </template>
                    <template
                      v-if="
                        optionsStrutture !== null &&
                        optionsStrutture.length == 0
                      "
                    >
                      <div class="col-12 col-lg-7">
                        <strong class="required-field"
                          >Questo comune non ha strutture, selezionane un
                          altro</strong
                        >
                      </div>
                    </template>
                  </div>

                  <template
                    v-if="
                      richiesta.contratto.struttura &&
                      richiesta.contratto.struttura.id
                    "
                  >
                    <q-card-section class="q-my-xl">
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
                  </template>
                </div>
              </q-card-section>
              <!-- FINE SELEZIONE STRUTTURA -->
              <!-- DATA INIZIO CONTRATTO -->
              <h2>Data inizio del contratto</h2>
              <div class="row q-gutter-y-lg q-gutter-x-lg date-start-end">
                <div class="col-8 col-lg-4">
                  <label for="dataInizioContratto"
                    ><span
                      :class="
                        $v.richiesta.contratto.data_inizio.$invalid
                          ? 'required-field'
                          : ''
                      "
                      >*</span
                    >
                    Data inizio</label
                  >
                  <q-input
                    id="dataInizioContratto"
                    v-model="richiesta.contratto.data_inizio"
                    type="date"
                    @blur="$v.richiesta.contratto.data_inizio.$touch"
                    :error="$v.richiesta.contratto.data_inizio.$error"
                    min="1970-01-01"
                    max="2050-12-31"
                  >
                    <template v-slot:error>
                      <template
                        v-if="!$v.richiesta.contratto.data_inizio.required"
                      >
                        Data inizio è obbligatoria
                      </template>

                      <template
                        v-if="
                          !$v.richiesta.contratto.data_inizio.maxValue ||
                          !$v.richiesta.contratto.data_inizio.minValue
                        "
                      >
                        Data inizio deve essere compresa tra il 1 gennaio 1970 e
                        il 31 dicembre 2050
                      </template>
                    </template>
                  </q-input>
                </div>
              </div>
              <!--FINE DATA INIZIO-->
              <template>
                <div class="row q-my-xl q-gutter-y-lg">
                  <div class="col-12">
                    <h3 class="flex">
                      <span
                        :class="
                          $v.allegati.CONTRATTO_RSA.$invalid
                            ? 'required-field'
                            : ''
                        "
                        >*</span
                      >Copia contratto ospitalità
                      <tooltip
                        :titleBanner="copiaContrattoRSATooltip.title"
                        :textBanner="copiaContrattoRSATooltip.text"
                      />
                    </h3>
                    <!-- PRECEDENTE FILE CONTRATTO LAVORO -->
                    <div v-if="precedente_CONTRATTO_RSA">
                      <q-btn
                        icon="delete"
                        class="q-mr-md"
                        flat
                        color="negative"
                        title="elimina il documento"
                        @click="precedente_CONTRATTO_RSA = null"
                        :aria-label="
                          'elimina il documento ' +
                          precedente_CONTRATTO_RSA.name
                        "
                      />
                      <a
                        :href="
                          url +
                          '/' +
                          richiesta.numero +
                          '/' +
                          precedente_CONTRATTO_RSA.tipo
                        "
                        target="_blank"
                      >
                        <strong
                          >{{ precedente_CONTRATTO_RSA.filename }}
                        </strong>
                      </a>
                    </div>
                    <div
                      v-if="
                        !precedente_CONTRATTO_RSA && !allegati.CONTRATTO_RSA
                      "
                    >
                      <q-file
                        ref="CONTRATTO_RSA"
                        outlined
                        clearable
                        v-model="allegati.CONTRATTO_RSA"
                        label="Documento contratto"
                        class="q-mb-xl hidden"
                        :accept="fileAccepted"
                        :max-file-size="fileSize"
                        :filter="checkFileSize"
                        @rejected="onRejected"
                      ></q-file>

                      <q-btn
                        @click="caricaDocumento('CONTRATTO_RSA')"
                        color="primary"
                        label="ALLEGA"
                        aria-label="allega il contratto di lavoro"
                      ></q-btn>
                    </div>

                    <div class="q-my-lg" v-if="allegati.CONTRATTO_RSA">
                      <q-btn
                        icon="delete"
                        class="q-mr-md"
                        flat
                        color="negative"
                        title="elimina il documento"
                        @click="allegati.CONTRATTO_RSA = null"
                        :aria-label="
                          'elimina il documento ' + allegati.CONTRATTO_RSA.name
                        "
                      />
                      {{ allegati.CONTRATTO_RSA.name }}
                    </div>
                  </div>
                </div>
              </template>
            </template>
          </div>
        </template>
        <div>
          <!-- NESSUN CONTRATTO -->
          <q-radio
            dense
            v-model="richiesta.contratto.tipo"
            val="NESSUN_CONTRATTO"
            label="il destinatario non è attualmente inserito in una struttura ma è intenzionato ad effettuare l�??inserimento entro 30 giorni dalla comunicazione di avvenuta erogazione del buono."
          />
        </div>
      </q-card-section>
    </q-card>
    <!-- FINE CARD DATI STRUTTURA -->
  </div>
</template>

<script>
import moment, { invalid } from "moment";
import store from "src/store/index";
import CheckCF from "components/CheckCF";
import Tooltip from "components/core/Tooltip";
import LmsBanner from "components/core/LmsBanner";
import SelectSuggest from "components/steps/formComponents/SelectSuggest";
import {
  putRichiesta,
  getRichiesta,
  postAllegato,
  getStrutture,
} from "src/services/api";
import {
  required,
  requiredIf,
  between,
  integer,
} from "vuelidate/lib/validators";
import { ACCEPTED_FILES_TYPES, MAX_FILE_SIZE } from "src/services/config";
const mustBeTrue = (value) => value == true;

export default {
  name: "FillInTheForm",
  props: ["saveCurrentPage", "saveDraft"],
  components: { Tooltip, LmsBanner, SelectSuggest },

  data() {
    return {
      spinner: false,
      comune: null,
      legalAge: false,

      iseeTooltip: {
        title: "Attestazione I.S.E.E socio - sanitario",
        text: "L'I.S.E.E socio - sanitario è utile per l'accesso alle prestazioni sociosanitarie come l'assistenza domiciliare per le persone con disabilità e/o non autosufficienti, l'ospitalità alberghiera presso strutture residenziali e semiresidenziali per le persone che non possono essere assistite a domicilio. <br><br> Le persone disabili maggiorenni possono scegliere un nucleo più ristretto rispetto a quello ordinario. <br><br> Per esempio, una persona maggiorenne disabile non coniugata e senza figli, che vive con i genitori, in sede di calcolo ISEE può dichiarare solo i suoi redditi e patrimoni.",
      },
      punteggioSocialeTooltip: {
        title: "Punteggio Sociale",
        text: "Si tratta del valore indicato nella scheda di valutazione del bisogno sociale, effettuato a cura ente dell'ente gestore delle funzioni socio-assistenziali. <br> <br> La valutazione tiene conto dei seguenti elementi, e può raggiungere un punteggio massimo di 14 punti, così suddivisi: <ul><li>condizione abitativa:  (max 2 punti)</li> <li>condizione economica:  (max 4 punti)</li> <li>condizione famigliare:  (max 4 punti)</li><li>condizione assistenziale: (max 4 punti)</li></ul>",
      },
      copiaContrattoRSATooltip: {
        title: "Copia contratto ospitalità",
        text: "Allegare contributo, convenzione, lettera o altro documento fornito dalla struttura attestante l�??effettivo inserimento residenziale a titolo definitivo.",
      },
      strutturaTooltip: {
        title: "Selezione struttura",
        text: "Nel menu sono proposte soltanto strutture che hanno già aderito alla misura; l�??elenco delle strutture aderenti è in continuo aggiornamento. <br> <br> Se la struttura individuata non è presente, verificare nei prossimi giorni se è comparsa nella lista delle aderenti.",
      },
      allegatiOk: true,
      isMounted: false,
      optionsComuni: store.state.comuni,
      optionsStrutture: null,
      url: window.location.origin + "/buonoresbff/api/v1/allegato",
      fileSize: MAX_FILE_SIZE,
      fileAccepted: ACCEPTED_FILES_TYPES,
      precedente_VERBALE_UVG: null,
      precedente_VERBALE_UMVD: null,
      precedente_CONTRATTO_RSA: null,
      rapporto_options: store.state.tipi_rapporto,
      richiesta: {
        attestazione_isee: false,
        punteggio_bisogno_sociale: null,
        valutazione_multidimensionale: null,
        contratto: {
          data_inizio: null,
          tipo: null,
          struttura: null,
        },
        nessuna_incompatibilita: false,
      },
      allegati: {
        VERBALE_UVG: null,
        VERBALE_UMVD: null,
        CONTRATTO_RSA: null,
      },
    };
  },
  filters: {
    formatDate: function (date) {
      return moment(date).format("DD/MM/YYYY");
    },
  },
  computed: {
    comuneSelect() {
      return this.comune;
    },
    validaCFError() {
      return this.validaCF(this.richiesta.contratto.agenzia.cf)
        ? this.validaCF(this.richiesta.contratto.agenzia.cf)
        : null;
    },
    // PERCEPISCE CAMBIAMENTI SUL RADIO DEL PUNTEGGIO SOCIALE
    radioPunteggio() {
      return this.richiesta.valutazione_multidimensionale;
    },
    // PERCEPISCE CAMBIAMENTI SUL RADIO DEL CONTRATTO
    radioContratto() {
      return this.richiesta.contratto.tipo
        ? this.richiesta.contratto.tipo
        : null;
    },
    // PERCEPISCE CAMBIAMENTI SUL RADIO DEL TITOLO PRIVATO
    radioTitoloPrivato() {
      return this.richiesta.contratto.titolo_privato;
    },
    isInvalidValid() {
      return this.$v ? this.$v.$invalid : null;
    },
    invalidFields() {
      return this.$v
        ? Object.keys(this.$v.$params).filter(
            (fieldName) => this.$v[fieldName].$invalid
          )
        : null;
    },
    richiestaStore() {
      return store.getters["getRichiesta"];
    },
    richiestaCurrent() {
      return this.richiesta ? this.richiesta : null;
    },
    allegatiCurrent() {
      return this.allegati ? this.allegati : null;
    },
  },
  validations: {
    richiesta: {
      attestazione_isee: {
        mustBeTrue,
      },
      nessuna_incompatibilita: { mustBeTrue },
      valutazione_multidimensionale: { required },
      punteggio_bisogno_sociale: {
        required,
        between: between(7, 14),
        integer,
      },
      contratto: {
        tipo: { required },
        data_inizio: {
          required: requiredIf(function () {
            return this.richiesta.contratto.tipo !== "NESSUN_CONTRATTO";
          }),
          maxValue(val) {
            return this.richiesta.contratto.tipo !== "NESSUN_CONTRATTO"
              ? new Date("2050-12-31") > new Date(val)
              : true;
          },
          minValue(val) {
            return this.richiesta.contratto.tipo !== "NESSUN_CONTRATTO"
              ? new Date("2050-12-31") > new Date(val)
              : true;
          },
        },
        struttura: {
          id: {
            required: requiredIf(function () {
              return this.richiesta.contratto.tipo !== "NESSUN_CONTRATTO";
            }),
          },
        },
        titolo_privato: {
          required: requiredIf(function () {
            return this.richiesta.contratto.tipo !== "NESSUN_CONTRATTO";
          }),
        },
      },
    },
    allegati: {
      VERBALE_UVG: {
        required: requiredIf(function () {
          return (
            this.richiesta.valutazione_multidimensionale == "UVG" &&
            this.precedente_VERBALE_UVG == null
          );
        }),
      },
      VERBALE_UMVD: {
        required: requiredIf(function () {
          return (
            this.richiesta.valutazione_multidimensionale == "UMVD" &&
            this.precedente_VERBALE_UMVD == null
          );
        }),
      },
      CONTRATTO_RSA: {
        required: requiredIf(function () {
          return (
            this.richiesta.contratto.tipo == "CONTRATTO_RSA" &&
            this.precedente_CONTRATTO_RSA == null
          );
        }),
      },
    },
  },
  methods: {
    setValue(obj) {
      if (obj.subFieldChild) {
        this[obj.field][obj.subField][obj.subFieldChild] = obj.model;
      } else if (!obj.subFieldChild && obj.subField) {
        this[obj.field][obj.subField] = obj.model;
      } else {
        this[obj.field] = obj.model;
      }
    },
    // FA IL TOUCH DEL FORM
    submit() {
      this.$v.$touch();
      if (this.$v.$error) {
        this.$q.notify({
          type: "negative",
          message: "Ricontrolla i campi del form",
        });
        return;
      }
    },
    checkAge(date) {
      return moment().diff(new Date(date), "years");
    },
    resetFormContratto() {
      this.richiesta.contratto = {
        tipo: this.richiesta.contratto.tipo,
        data_inizio: null,
        struttura: null,
        titolo_privato: this.richiesta.contratto.titolo_privato,
      };

      this.allegati.CONTRATTO_RSA = null;
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
    async saveStepForm(avanti) {
      if (avanti) {
        await store.dispatch("setSpinner", true);
      }
      // PROVIAMO PRIMA SE GLI ALLEGATI VANNO BENE DIVERSAMENTE ANNULLIAMO ANCHE LA PUT
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
        if (this.richiesta.contratto.tipo == "NESSUN_CONTRATTO") {
          this.richiesta.contratto['incompatibilita_per_contratto'] = true;
        }
        try {
          let { data } = await putRichiesta(
            this.richiesta.numero,
            this.richiesta
          );
          await store.dispatch(
            "setRichiesta",
            JSON.parse(JSON.stringify(data))
          );

          if (avanti) {
            await store.dispatch("setSpinner", false);
            await store.dispatch("setStep", 3);
          } else {
            this.$q.notify({
              type: "positive",
              message: "La bozza è stata salvata correttamente",
              timeout: 6000,
            });
            this.$emit("sbloccaBozza", false);
          }
        } catch (err) {
          await store.dispatch("setSpinner", false);
          await store.dispatch("setNextStep", {
            bool: true,
            step: 2,
          });

          await store.dispatch("setStep", 2);
          let message =
            err.response && err.response.data.detail
              ? err.response.data.detail[0].valore
              : "Problemi server, contattare assistenza";
          this.$q.notify({
            type: "negative",
            message: message,
            timeout: 6000,
          });
          if (avanti) {
            this.$router.push("/");
          }
          console.error(err);
        }
      } else {
        this.allegatiOk = true;
        await store.dispatch("setSpinner", false);
        this.$q.notify({
          type: "negative",
          message: "Ci sono problemi con gli allegati",
        });
      }
    },

    caricaDocumento(type) {
      this.$refs[type].pickFiles();
    },

    async postAllegatoSingolo(type) {

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
    validaCF(cf) {
      if (cf == null) return;
      let validi, i, s, set1, set2, setpari, setdisp;
      if (cf == "") return "";
      cf = cf.toUpperCase();
      if (cf.length != 16 && cf.length != 11)
        return (
          "La lunghezza del Codice Fiscale non è\n" +
          "corretta: il Codice Fiscale dovrebbe essere lungo\n" +
          "esattamente 16 caratteri e la P.IVA o il codice fiscale provvisorio, 11 caratteri.\n"
        );
      if (cf.length == 16) {
        validi = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (i = 0; i < 16; i++) {
          if (validi.indexOf(cf.charAt(i)) == -1)
            return (
              "Il codice fiscale contiene un carattere non valido `" +
              cf.charAt(i) +
              "'.\nI caratteri validi sono le lettere e le cifre.\n"
            );
        }
        set1 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        set2 = "ABCDEFGHIJABCDEFGHIJKLMNOPQRSTUVWXYZ";
        setpari = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        setdisp = "BAKPLCQDREVOSFTGUHMINJWZYX";
        s = 0;
        for (i = 1; i <= 13; i += 2)
          s += setpari.indexOf(set2.charAt(set1.indexOf(cf.charAt(i))));
        for (i = 0; i <= 14; i += 2)
          s += setdisp.indexOf(set2.charAt(set1.indexOf(cf.charAt(i))));
        if (s % 26 != cf.charCodeAt(15) - "A".charCodeAt(0))
          return "Il codice fiscale non è corretto: il codice di controllo non corrisponde.\n";
      }
      if (cf.length == 11) {
        if (!/^[0-9]{11}$/.test(cf))
          return "Il codice fiscale provvisorio o P.IVA possono solo numeri.";
        let s = 0;
        for (let i = 0; i < 11; i++) {
          let n = cf.charCodeAt(i) - "0".charCodeAt(0);
          if ((i & 1) === 1) {
            n *= 2;
            if (n > 9) n -= 9;
          }
          s += n;
        }
        if (s % 10 !== 0)
          return "Il codice fiscale provvisorio non è corretto: il codice di controllo non corrisponde.\n.";
      }
    },
  },
  watch: {
    async comuneSelect() {
      if (this.isMounted) {
        this.richiesta.contratto.struttura = null;
        if (this.comune) {
          this.spinner = true;
          try {
            let { data } = await getStrutture(this.comune);
            let value = JSON.parse(JSON.stringify(data));
            this.optionsStrutture = value;
            this.spinner = false;
          } catch (err) {
            await store.dispatch("setSpinner", false);
            this.spinner = false;
          }
        }
      }
    },
    // RESETTO GLI ALLEGATI SE CAMBIA
    radioPunteggio() {
      this.allegati.VERBALE_UVG = null;
      this.allegati.VERBALE_UMVD = null;
    },
    // RESETTO  SOLO IL FORM CONTRATTO
    radioContratto() {
      if (this.isMounted) {
        this.richiesta.contratto.titolo_privato = false;
        if (this.richiesta.contratto.tipo === "NESSUN_CONTRATTO") {
          this.richiesta.contratto.incompatibilita_per_contratto = true;
        }
      }
    },

    radioTitoloPrivato() {
      if (this.isMounted) {
        this.resetFormContratto();
      }
    },
    isInvalidValid() {
      store.dispatch("setNextStep", {
        bool: this.$v.$invalid,
        step: 2,
      });
    },
    saveCurrentPage: function () {
      this.saveStepForm(true);
    },
    saveDraft: function () {
      if (this.$v.richiesta.$anyError) {
        this.$q.notify({
          type: "negative",
          message: `Non è possibile salvare in bozza con errori in pagina`,
        });
      } else {
        this.saveStepForm(false);
        this.$emit("sbloccaBozza", false);
      }
    },
    richiestaCurrent: {
      handler() {
        if (this.isMounted) {
          this.$emit("sbloccaBozza", true);
        }
      },
      deep: true,
    },
    allegatiCurrent: {
      handler() {
        if (this.isMounted) {
          this.$emit("sbloccaBozza", true);
        }
      },
      deep: true,
    },
  },
  async created() {
    await store.dispatch("setSpinner", false);
    // SERVE PER RIMETTERE IN CIMA IL COMPONENTE
    window.scrollTo(0, 0);
    // SE SIAMO IN EDIT PRENDIAMO ID NUMERO DALLA URL
    let id = this.$route.params.id
      ? this.$route.params.id
      : this.richiestaStore.numero
      ? this.richiestaStore.numero
      : null;
    try {
      let { data } = await getRichiesta(id);
      if (data.contratto.data_inizio)
        data.contratto.data_inizio = data.contratto.data_inizio.substring(
          0,
          10
        );
      // SE CI SONO ALLEGATI PREPOPOLIAMO PER LA EDIT
      if (data["allegati"]) {
        let allegati = JSON.parse(JSON.stringify(data["allegati"]));
        for (let allegato of allegati) {
          this["precedente_" + allegato.tipo] = allegato;
        }
      }

      // PREVALORIZZA A FALSE ISEE ANZICHè NULL
      if (!data.hasOwnProperty("attestazione_isee")) {
        data.attestazione_isee = false;
      }

      if (!data.hasOwnProperty("nessuna_incompatibilita")) {
        data.nessuna_incompatibilita = false;
      }


      if (!data["contratto"].hasOwnProperty("titolo_privato")) {
        data.contratto.titolo_privato = false;
      }

      this.richiesta = JSON.parse(JSON.stringify(data));

      await store.dispatch("setRichiesta", JSON.parse(JSON.stringify(data)));
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
      console.error(err);
    }
  },
  async updated() {
    // PROVVISORIO
    setTimeout(() => (this.isMounted = true), 1000);
    if (this.isInvalidValid) {
      store.dispatch("setNextStep", {
        bool: this.$v.$invalid,
        step: 2,
      });
    }
  },
};
</script>

<style lang="sass"></style>
