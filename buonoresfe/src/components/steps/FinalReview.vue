<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div>
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
          <div class="col-auto">
            <q-btn
              v-if="!modifica_dati"
              @click="modifica_dati = true"
              flat
              color="primary"
              label="MODIFICA"
              aria-label="modifica i dati del destinatario"
            />
            <q-btn
              v-if="modifica_dati"
              @click="salvaDestinatario()"
              flat
              color="primary"
              label="SALVA"
              aria-label="se il pulsante non è disabilitato si può procedere con il salvataggio dei dati modificati"
              :disable="isInvalidValid"
            />
          </div>
        </div>
        <div
          class="row q-mb-xl q-col-gutter-xl"
          :class="modifica_dati ? 'text-overlay--active' : ' '"
        >
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
          <template v-if="modifica_dati">
            <div class="col-12 col-md-9">
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
            <div class="col-12 col-md-4">
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
            <div class="col-12 col-md-4">
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
                    !$v.richiesta.domicilio_destinatario.provincia.maxLength ||
                    !$v.richiesta.domicilio_destinatario.provincia.minLength
                  "
                >
                  Inserire solo codice provincia a due lettere (esempio: TO)
                </div>
              </div>
            </div>
          </template>
          <template v-else>
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
        <div class="row q-col-gutter-xl" v-if="modifica_dati">
          <div class="col-12 col-lg-8">
            <lms-select
              id="asl-select"
              v-model="richiesta.asl_destinatario"
              :options="asl_options"
              :rules="[(val) => val !== null || 'Inserire Asl di appartenenza']"
              bottom-slots
              label="*Asl"
              label-for="asl-select"
              lazy-rules
              name="destinatario-asl-app"
              no-error-icon
              required
            />
          </div>
          <div class="col-12 col-lg-8">
            <lms-select
              id="studio-select"
              v-model="richiesta.studio_destinatario"
              :options="titolo_options"
              :rules="[(val) => val !== null || 'Inserire titolo di studio']"
              bottom-slots
              label="*Titolo studio"
              label-for="studio-select"
              lazy-rules
              name="destinatario-studio-app"
              no-error-icon
              required
            />
          </div>
          <div class="col-12 col-lg-4" v-if="appropriateAge">
            <div>
              <span
                :class="
                  $v.richiesta.lavoro_destinatario.$invalid
                    ? 'required-field'
                    : ''
                "
                >*</span
              >
              Lavorativamente occupato
            </div>
            <div class="q-gutter-sm">
              <q-radio
                v-model="richiesta.lavoro_destinatario"
                :val="true"
                label="si"
              />
              <q-radio
                v-model="richiesta.lavoro_destinatario"
                :val="false"
                label="no"
              />
            </div>
          </div>
        </div>
        <div class="row q-mb-xl q-col-gutter-xl" v-else>
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
        <div
          class="row q-mb-xl q-col-gutter-xl"
          :class="modifica_dati ? 'text-overlay--active' : ''"
        >
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
          <div v-if="modifica_dati">
            <div class="">
              <label for="relazione-select">
                *Il richiedente opera in qualità di
              </label>

              <lms-select
                id="relazione-select"
                v-model="richiesta.delega"
                :options="rapporto_options"
                :rules="[(val) => val !== null || 'Inserire relazione']"
                bottom-slots
                label-for="relazione-select"
                lazy-rules
                name="destinatario-relazione-app"
                no-error-icon
                required
              />
            </div>
          </div>
          <div class="col-12" v-else>
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
        <template v-if="modifica_dati">
          <div class="row q-mb-xl">
            <!-- ALLEGATI VALIDI-->
            <div v-if="allegatiValidi && allegatiValidi.length > 0">
              <div v-if="precedente_CARTA_IDENTITA && cartaIdentitaAllowed">
                <q-btn
                  icon="delete"
                  class="q-mr-md"
                  flat
                  color="negative"
                  @click="precedente_CARTA_IDENTITA = null"
                  :aria-label="
                    'elimina il documento ' + precedente_CARTA_IDENTITA.name
                  "
                />
                <a
                  :href="
                    url +
                    '/' +
                    richiesta.numero +
                    '/' +
                    precedente_CARTA_IDENTITA.tipo
                  "
                  target="_blank"
                >
                  <strong>{{ precedente_CARTA_IDENTITA.filename }} </strong>
                </a>
              </div>
              <div v-if="precedente_PROCURA_SPECIALE && procuraAllowed">
                <q-btn
                  icon="delete"
                  class="q-mr-md"
                  flat
                  color="negative"
                  @click="precedente_PROCURA_SPECIALE = null"
                  :aria-label="
                    'elimina il documento ' + precedente_PROCURA_SPECIALE.name
                  "
                />
                <a
                  :href="
                    url +
                    '/' +
                    richiesta.numero +
                    '/' +
                    precedente_PROCURA_SPECIALE.tipo
                  "
                  target="_blank"
                >
                  <strong>{{ precedente_PROCURA_SPECIALE.filename }} </strong>
                </a>
              </div>
              <div v-if="precedente_NOMINA_TUTORE && nominaTutoreAllowed">
                <q-btn
                  icon="delete"
                  class="q-mr-md"
                  flat
                  color="negative"
                  @click="precedente_NOMINA_TUTORE = null"
                  :aria-label="
                    'elimina il documento ' + precedente_NOMINA_TUTORE.name
                  "
                />
                <a
                  :href="
                    url +
                    '/' +
                    richiesta.numero +
                    '/' +
                    precedente_NOMINA_TUTORE.tipo
                  "
                  target="_blank"
                >
                  <strong>{{ precedente_NOMINA_TUTORE.filename }} </strong>
                </a>
              </div>
              <template
                v-if="
                  cartaIdentitaAllowed || procuraAllowed || nominaTutoreAllowed
                "
              >
                <div class="row q-mb-xl q-gutter-y-lg">
                  <div
                    class="col-12 q-pa-xs"
                    v-if="!precedente_CARTA_IDENTITA && cartaIdentitaAllowed"
                  >
                    <div>
                      <template v-if="cartaIdentitaRequired"
                        ><span
                          :class="
                            $v.allegati.CARTA_IDENTITA.$invalid
                              ? 'required-field'
                              : ''
                          "
                          >*</span
                        ></template
                      >Carta d'identità del delegante
                    </div>
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
                  <div
                    class="col-12 q-pa-xs"
                    v-if="!precedente_PROCURA_SPECIALE && procuraAllowed"
                  >
                    <div>
                      <template v-if="procuraRequired"
                        ><span
                          :class="
                            $v.allegati.CARTA_IDENTITA.$invalid
                              ? 'required-field'
                              : ''
                          "
                          >*</span
                        ></template
                      >Procura speciale
                    </div>
                    <q-file
                      ref="PROCURA_SPECIALE"
                      outlined
                      clearable
                      v-model="allegati.PROCURA_SPECIALE"
                      label="Documento procura"
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
                    ></q-btn>

                    <div v-if="allegati.PROCURA_SPECIALE" class="q-my-lg">
                      <q-btn
                        icon="delete"
                        class="q-mr-md"
                        flat
                        color="negative"
                        @click="allegati.PROCURA_SPECIALE = null"
                        :aria-label="
                          'elimina il documento' +
                          allegati.PROCURA_SPECIALE.name
                        "
                      />{{ allegati.PROCURA_SPECIALE.name }}
                    </div>
                  </div>
                  <div
                    class="col-12 q-pa-xs"
                    v-if="!precedente_NOMINA_TUTORE && nominaTutoreAllowed"
                  >
                    <div>
                      <template v-if="nominaTutoreRequired"
                        ><span
                          :class="
                            $v.allegati.NOMINA_TUTORE.$invalid
                              ? 'required-field'
                              : ''
                          "
                          >*</span
                        ></template
                      >Nomina tutore
                    </div>
                    <q-file
                      ref="NOMINA_TUTORE"
                      outlined
                      clearable
                      v-model="allegati.NOMINA_TUTORE"
                      label="Documento procura"
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
            </div>
            <div v-else>
              <!-- ALLEGATI NON VALIDI-->
              <template
                v-if="
                  cartaIdentitaAllowed || procuraAllowed || nominaTutoreAllowed
                "
              >
                <div class="row q-mb-xl q-gutter-y-lg">
                  <div
                    class="col-12 q-pa-xs"
                    v-if="!precedente_CARTA_IDENTITA && cartaIdentitaAllowed"
                  >
                    <div>
                      <template v-if="cartaIdentitaRequired"
                        ><span
                          :class="
                            $v.allegati.CARTA_IDENTITA.$invalid
                              ? 'required-field'
                              : ''
                          "
                          >*</span
                        ></template
                      >Carta d'identità del delegante
                    </div>
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
                  <div
                    class="col-12 q-pa-xs"
                    v-if="!precedente_PROCURA_SPECIALE && procuraAllowed"
                  >
                    <div>
                      <template v-if="procuraRequired"
                        ><span
                          :class="
                            $v.allegati.PROCURA_SPECIALE.$invalid
                              ? 'required-field'
                              : ''
                          "
                          >*</span
                        ></template
                      >Procura speciale
                    </div>
                    <q-file
                      ref="PROCURA_SPECIALE"
                      outlined
                      clearable
                      v-model="allegati.PROCURA_SPECIALE"
                      label="Documento procura"
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
                    ></q-btn>

                    <div v-if="allegati.PROCURA_SPECIALE" class="q-my-lg">
                      <q-btn
                        icon="delete"
                        class="q-mr-md"
                        flat
                        color="negative"
                        @click="allegati.PROCURA_SPECIALE = null"
                        :aria-label="
                          'elimina il documento' +
                          allegati.PROCURA_SPECIALE.name
                        "
                      />{{ allegati.PROCURA_SPECIALE.name }}
                    </div>
                  </div>
                  <div
                    class="col-12 q-pa-xs"
                    v-if="!precedente_NOMINA_TUTORE && nominaTutoreAllowed"
                  >
                    <div>
                      <template v-if="nominaTutoreRequired"
                        ><span
                          :class="
                            $v.allegati.NOMINA_TUTORE.$invalid
                              ? 'required-field'
                              : ''
                          "
                          >*</span
                        ></template
                      >Nomina tutore
                    </div>
                    <q-file
                      ref="NOMINA_TUTORE"
                      outlined
                      clearable
                      v-model="allegati.NOMINA_TUTORE"
                      label="Documento procura"
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
            </div>
          </div>
        </template>
        <template v-else>
          <div v-for="(allegato, i) in richiesta.allegati" :key="i">
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
        </template>
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
          <div class="col-auto">
            <q-btn
              v-if="!modifica_modulo"
              @click="editApplication(id)"
              flat
              color="primary"
              label="MODIFICA"
              aria-label="torna allo step 3 per modificare i dati della domanda"
              :disable="modifica_dati"
            />
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
        <div>
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
        <p>
          Il destinatario è già stato sottoposto a �??valutazione
          multidimensionale�?? presso le Unità di Valutazione competenti (U.V.G. -
          ANZIANI, o U.M.V.D. - DISABILI), e di essere in possesso di un
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
            Il destinatario è inserito in una struttura residenziale a carattere
            socio-sanitario o socio-assistenziale a
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

          <!--DATA INIZIO E FINE CONTRATTO-->
          <div class="q-my-xl">
            <h3>Data di inizio e fine contratto</h3>
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
          <!--FINE DATA INIZIO E FINE CONTRATTO-->
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
              >è intenzionato ad effettuare l�??inserimento entro 30 giorni dalla comunicazione di avvenuta erogazione del buono.</strong
            >
          </p>
        </q-card-section>
      </template>
    </q-card>
  </div>
</template>

<script>
import moment from "moment";
import store from "src/store/index";
import LmsSelect from "components/core/LmsSelect";
import {
  putRichiesta,
  getRichiesta,
  postAllegato,
  postCronologia,
} from "src/services/api";
import {
  required,
  requiredIf,
  minLength,
  maxLength,
} from "vuelidate/lib/validators";
import { ACCEPTED_FILES_TYPES, MAX_FILE_SIZE } from "src/services/config";
export default {
  name: "FinalReview",
  props: ["saveCurrentPage"],
  components: { LmsSelect },
  data() {
    return {
      id: null,
      allegatiOk: true,
      url: window.location.origin + "/buonoresbff/api/v1/allegato",
      titolo_options: store.state.titoli_studio,
      rapporto_options: store.state.tipi_rapporto,
      asl_options: store.state.asl,
      contratto_options: store.state.tipi_contratto,
      modifica_dati: false,
      modifica_modulo: false,
      fileSize: MAX_FILE_SIZE,
      fileAccepted: ACCEPTED_FILES_TYPES,
      precedente_NOMINA_TUTORE: null,
      precedente_PROCURA_SPECIALE: null,
      precedente_CARTA_IDENTITA: null,
      precedente_VERBALE_UVG: null,
      precedente_VERBALE_UMVD: null,
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
          tipo: null,
          struttura: null,
        },
        nessuna_incompatibilita: null,
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
  validations: {
    richiesta: {
      asl_destinatario: { required },
      studio_destinatario: { required },
      lavoro_destinatario: {
        required: requiredIf(function () {
          return this.appropriateAge;
        }),
      },
      domicilio_destinatario: {
        indirizzo: { required },
        comune: { required },
        provincia: {
          required,
          maxLength: maxLength(2),
          minLength: minLength(2),
        },
      },
    },
    allegati: {
      CARTA_IDENTITA: {
        required: requiredIf(function () {
          return (
            this.cartaIdentitaRequired &&
            this.modifica_dati &&
            !this.precedente_CARTA_IDENTITA
          );
        }),
      },
      PROCURA_SPECIALE: {
        required: requiredIf(function () {
          return (
            this.procuraRequired &&
            this.modifica_dati &&
            !this.precedente_PROCURA_SPECIALE
          );
        }),
      },
      NOMINA_TUTORE: {
        required: requiredIf(function () {
          return (
            this.nominaTutoreRequired &&
            this.modifica_dati &&
            !this.precedente_NOMINA_TUTORE
          );
        }),
      },
    },
  },
  methods: {
    checkAge(date) {
      return moment().diff(new Date(date), "years");
    },
    textToUpper(val) {
      this.richiesta.domicilio_destinatario.provincia = val.toUpperCase();
    },
    // PER ORA FA SOLO IL TOUCH DEL FORM
    submit() {
      this.$v.$touch();
      if (this.$v.$error) {
        this.$q.notify({
          type: "negative",
          message: `Ricontrolla i campi del form`,
        });
        return;
      }
    },
    async salvaDestinatario() {
      this.saveStepForm(false);
    },
    async editApplication(id) {
      await store.dispatch("bozzaAllStep");
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
    checkFileSize(files) {
      return files.filter((file) => file.size < MAX_FILE_SIZE);
    },

    onRejected() {
      this.$q.notify({
        type: "negative",
        message: `File non idoneo, controlla tipo e grandezza`,
      });
    },
    async saveStepForm(invia) {


      // PROVIAMO PRIMA SE GLI ALLEGATI VANNO BENE DIVERSAMENTE ANNULLIAMO ANCHE LA PUT
      if (invia) {
        await store.dispatch("setSpinner", true);
      }

      for (let attribute in this.allegati) {
        console.log(attribute);
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
          await store.dispatch("setStepperForm", this.stepperForm);
          await store.dispatch(
            "setRichiesta",
            JSON.parse(JSON.stringify(data))
          );

          if (invia) {
            try {
              await postCronologia(this.richiesta.numero, "INVIATA");
              await store.dispatch("setSpinner", false);
              this.$emit("modal", {
                isSaved: true,
                messageError: false,
                messageText: "",
              });
            } catch (err) {
              this.$emit("modal", {
                isSaved: false,
                messageError: true,
                messageText: err.response.data.detail[0].valore,
              });
              await store.dispatch("setSpinner", false);
            }
          } else {
            let { data } = await getRichiesta(this.richiesta.numero);
            this.richiesta = data;
            this.$q.notify({
              type: "positive",
              message: "Modulo salvato",
              timeout: 6000,
            });
            this.modifica_dati = false;
          }
        } catch (err) {
          this.$emit("modal", {
            isSaved: false,
            messageError: true,
            messageText: err.response.data.detail[0].valore,
          });
          await store.dispatch("setSpinner", false);
        }
      }
    },
  },
  computed: {
    dati() {
      return this.modifica_dati ? this.modifica_dati : null;
    },
    appropriateAge() {
      return (
        this.checkAge(this.richiesta.destinatario.data_nascita) < 66 &&
        this.checkAge(this.richiesta.destinatario.data_nascita) >= 18
      );
    },
    allegatiValidi() {
      return this.richiesta.allegati
        ? this.richiesta.allegati.filter(
            (item) =>
              item.tipo == "DELEGA" ||
              item.tipo == "CARTA_IDENTITA" ||
              item.tipo == "PROCURA_SPECIALE" ||
              item.tipo == "NOMINA_TUTORE"
          )
        : null;
    },
    richiestaStore() {
      return store.getters["getRichiesta"];
    },
    isInvalidValid() {
      return this.$v ? this.$v.$invalid : null;
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

    cartaIdentitaRequired() {
      return this.cartaIdentitaAllowed && this.rapportoFiltrato
        ? this.rapportoFiltrato.filter(
            (tipo) => tipo.allegato_tipo_cod == "CARTA_IDENTITA"
          )[0].allegato_obbligatorio
        : null;
    },
    cartaIdentitaAllowed() {
      return this.rapportoFiltrato
        ? this.rapportoFiltrato.filter(
            (tipo) => tipo.allegato_tipo_cod == "CARTA_IDENTITA"
          )[0]
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
  },
  watch: {
    dati() {
      store.dispatch("setNextStep", {
        bool: this.modifica_dati,
        step: 3,
      });
    },
    saveCurrentPage: function () {
      this.saveStepForm(true);
    },
  },
  async created() {
    // SERVE PER RIMETTERE IN CIMA IL COMPONENTE
    window.scrollTo(0, 0);
    // SE SIAMO IN EDIT PRENDIAMO ID NUMERO DALLA URL
    let id = this.$route.params.id
      ? this.$route.params.id
      : this.richiestaStore.numero
      ? this.richiestaStore.numero
      : null;
    // MAGARI USIAMO SOLO THIS ID PULENDO IL CODICE
    this.id = id;

    try {
      let { data } = await getRichiesta(id);
      this.richiesta = data;
      if (data["allegati"]) {
        let allegati = JSON.parse(JSON.stringify(data["allegati"]));
        for (let allegato of allegati) {
          this["precedente_" + allegato.tipo] = allegato;
        }
      }
      await store.dispatch("setRichiesta", JSON.parse(JSON.stringify(data)));
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

<style lang="sass"></style>
