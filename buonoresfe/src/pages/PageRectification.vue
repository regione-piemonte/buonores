<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div>
    <div class="q-page lms-page">
      <!-- CARD DESTINATARIO BUONO  DISABILITATO IN RETTIFICA -->
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
          <!-- NOTE -->
          <div class="row q-mt-xl" v-if="richiesta.note">
            <div class="col-12">
              <lms-banner type="warning">
                <h2>Note di compilazione</h2>
                <p>
                  {{ richiesta.note }}
                </p>
              </lms-banner>
            </div>
          </div>
          <div class="row q-my-xl justify-between items-center">
            <div class="col-auto">
              <h2>Dati destinatario</h2>
            </div>
          </div>
          <div class="row q-mb-xl q-col-gutter-xl text-overlay--active">
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
            <!-- DATI DOMICILIO -->
            <template v-if="check('domicilio_destinatario')">
              <div class="col-12 col-md-9">
                <q-input
                  id="indirizzo_destinatarioIndirizzo"
                  v-model="richiesta.domicilio_destinatario.indirizzo"
                  label="*Indirizzo domicilio"
                  outlined
                ></q-input>
              </div>
              <div class="col-12 col-md-4">
                <q-input
                  id="indirizzo_destinatarioComune"
                  v-model="richiesta.domicilio_destinatario.comune"
                  label="*Comune domicilio"
                  outlined
                ></q-input>
              </div>
              <div class="col-12 col-md-4">
                <div v-if="richiesta.domicilio_destinatario">
                  <q-input
                    id="domicilio_destinatarioProvincia"
                    :value="richiesta.domicilio_destinatario.provincia"
                    @input="textToUpper"
                    label="*Provincia domicilio"
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
          <!-- ASL -->
          <div class="row q-col-gutter-xl q-mb-xl">
            <div class="col-12 col-lg-8" v-if="check('asl_destinatario')">
              <lms-select
                id="asl-select"
                v-model="richiesta.asl_destinatario"
                :options="asl_options"
                :rules="[
                  (val) => val !== null || 'Inserire Asl di appartenenza',
                ]"
                bottom-slots
                label="*Asl"
                label-for="asl-select"
                lazy-rules
                name="destinatario-asl-app"
                no-error-icon
                required
              />
            </div>
            <div class="col-12 col-lg-8 text-overlay--active" v-else>
              <dl>
                <dt>Asl</dt>
                <dd v-for="(item, i) of asl_options" :key="i">
                  <template v-if="item.codice == richiesta.asl_destinatario">
                    {{ item.etichetta }}
                  </template>
                </dd>
              </dl>
            </div>

            <!-- STUDIO DESTINATARIO -->
            <div
              class="col-12 col-lg-8 q-mb-xl"
              v-if="check('studio_destinatario')"
            >
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
            <div class="col-12 col-lg-8 text-overlay--active" v-else>
              <dl>
                <dt>Titolo studio</dt>
                <dd v-for="(item, i) of titolo_options" :key="i">
                  <template v-if="item.codice == richiesta.studio_destinatario">
                    {{ item.etichetta }}
                  </template>
                </dd>
              </dl>
            </div>
            <!-- LAVORO DESTINATARIO -->
            <div class="col-12 col-lg-4" v-if="check('lavoro_destinatario')">
              <div>*Lavorativamente occupato</div>
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
            <div class="col-12 col-lg-4 text-overlay--active" v-else>
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

        <!-- I TUOI DATI SEMPRE DISABILITATO IN RETTIFICA -->
        <q-card-section>
          <h3>I tuoi dati</h3>
          <div class="row q-mb-xl q-col-gutter-xl text-overlay--active">
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
          <!-- RICHIEDENTE OPERA IN QUALITA' DI -->
          <div class="row q-mb-xl" v-if="richiesta.delega">
            <div v-if="check('delega')">
              <div>
                <strong>*Richiedente opera in qualità di</strong>
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
            <div v-else class="text-overlay--active">
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
            <div v-if="allegatiValidi && allegatiValidi.length > 0">
              <div v-if="precedente_DELEGA && delegaAllowed">
                <q-btn
                  v-if="check('delega') || check('DELEGA')"
                  icon="delete"
                  class="q-mr-md"
                  flat
                  color="negative"
                  @click="precedente_DELEGA = null"
                  :aria-label="'elimina il documento ' + precedente_DELEGA.name"
                />
                <a
                  :href="
                    url + '/' + richiesta.numero + '/' + precedente_DELEGA.tipo
                  "
                  target="_blank"
                >
                  <strong>{{ precedente_DELEGA.filename }} </strong>
                </a>
              </div>
              <div v-if="precedente_CARTA_IDENTITA && cartaIdentitaAllowed">
                <q-btn
                  v-if="check('delega') || check('CARTA_IDENTITA')"
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
                  v-if="check('delega') || check('PROCURA_SPECIALE')"
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
                  v-if="check('delega') || check('NOMINA_TUTORE')"
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
                  delegaAllowed ||
                  cartaIdentitaAllowed ||
                  procuraAllowed ||
                  nominaTutoreAllowed
                "
              >
                <div class="row q-mb-xl q-gutter-y-lg">
                  <div
                    class="col-12 q-pa-xs"
                    v-if="!precedente_DELEGA && delegaAllowed"
                  >
                    <div>
                      <template v-if="delegaRequired">*</template>Delega
                    </div>

                    <q-file
                      ref="DELEGA"
                      outlined
                      clearable
                      v-model="allegati.DELEGA"
                      label="Documento delega"
                      class="q-mb-xl hidden"
                      :accept="fileAccepted"
                      :max-file-size="fileSize"
                      :filter="checkFileSize"
                      @rejected="onRejected"
                    ></q-file>
                    <q-btn
                      v-if="allegati.DELEGA == null"
                      @click="caricaDocumento('DELEGA')"
                      color="primary"
                      label="ALLEGA"
                    ></q-btn>
                    <div v-if="allegati.DELEGA" class="q-my-lg">
                      <q-btn
                        icon="delete"
                        class="q-mr-md"
                        flat
                        color="negative"
                        @click="allegati.DELEGA = null"
                        :aria-label="
                          'elimina il documento' + allegati.DELEGA.name
                        "
                      />
                      {{ allegati.DELEGA.name }}
                    </div>
                  </div>
                  <div
                    class="col-12 q-pa-xs"
                    v-if="!precedente_CARTA_IDENTITA && cartaIdentitaAllowed"
                  >
                    <div>
                      <template v-if="cartaIdentitaRequired">*</template>Carta
                      d'identità del delegante
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
                      <template v-if="procuraRequired">*</template>Procura
                      speciale
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
                      <template v-if="nominaTutoreRequired">*</template>Nomina
                      tutore
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
              <!-- ALLEGATI-->
              <template v-if="check('delega')">
                <div class="row q-mb-xl q-gutter-y-lg">
                  <div
                    class="col-12 q-pa-xs"
                    v-if="!precedente_DELEGA && delegaAllowed"
                  >
                    <div>
                      <template v-if="delegaRequired">*</template>Delega
                    </div>

                    <q-file
                      ref="DELEGA"
                      outlined
                      clearable
                      v-model="allegati.DELEGA"
                      label="Documento delega"
                      class="q-mb-xl hidden"
                      :accept="fileAccepted"
                      :max-file-size="fileSize"
                      :filter="checkFileSize"
                      @rejected="onRejected"
                    ></q-file>
                    <q-btn
                      v-if="allegati.DELEGA == null"
                      @click="caricaDocumento('DELEGA')"
                      color="primary"
                      label="ALLEGA"
                    ></q-btn>
                    <div v-if="allegati.DELEGA" class="q-my-lg">
                      <q-btn
                        icon="delete"
                        class="q-mr-md"
                        flat
                        color="negative"
                        @click="allegati.DELEGA = null"
                        :aria-label="
                          'elimina il documento' + allegati.DELEGA.name
                        "
                      />
                      {{ allegati.DELEGA.name }}
                    </div>
                  </div>
                  <div
                    class="col-12 q-pa-xs"
                    v-if="!precedente_CARTA_IDENTITA && cartaIdentitaAllowed"
                  >
                    <div>
                      <template v-if="cartaIdentitaRequired">*</template>Carta
                      d'identità del delegante
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
                      <template v-if="procuraRequired">*</template>Procura
                      speciale
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
                      <template v-if="nominaTutoreRequired">*</template>Nomina
                      tutore
                    </div>
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

        <!-- ISEE RIMARRA' SEMPRE BLOCCATO IN RETTIFICA-->
        <q-card-section>
          <h3>Attestazione I.S.E.E.</h3>
          <div class="row rectification-disabled">
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
          <!-- CARD TIPOLOGIE -->
          <q-card-section class="q-my-xl">
            <strong>*Seleziona tipologia persona</strong>
            <div class="q-my-lg">
              <q-radio
                :disable="!check('valutazione_multidimensionale')"
                dense
                v-model="richiesta.valutazione_multidimensionale"
                val="UVG"
                label="Il destinatario è persona di età pari o superiore a 65 anni, non autosufficiente con valutazione multidimensionale U.V.G. - ANZIANI"
              />
            </div>
            <q-radio
              :disable="!check('valutazione_multidimensionale')"
              dense
              v-model="richiesta.valutazione_multidimensionale"
              val="UMVD"
              label="Il destinatario è persona con disabilità, non autosufficiente con valutazione multidimensionale U.M.V.D. �?? DISABILI"
            />
          </q-card-section>
        </q-card-section>
        <!--FINE REQUISITI COMPATIBILITA'-->

        <!-- PUNTEGGIO SOCIALE -->
        <q-card-section>
          <h3>Punteggio sociale</h3>
          <div class="row">
            <div class="col-10 col-md-8 col-lg-6">
              <q-input
                :disable="!check('punteggio_bisogno_sociale')"
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
        </q-card-section>
        <!--FINE PUNTEGGIO SOCIALE -->

        <!-- ALLEGATI PUNTEGGIO -->
        <template
          v-if="
            check('valutazione_multidimensionale') ||
            check('VERBALE_UVG') ||
            check('VERBALE_UMVD')
          "
        >
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
                  <label for="prec-verbale-uvg">Allegato verbale UVG: </label>
                  <q-btn
                    v-if="
                      check('valutazione_multidimensionale') ||
                      check('VERBALE_UVG')
                    "
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
                    id="prec-verbale-uvg"
                  >
                    <strong>{{ precedente_VERBALE_UVG.filename }} </strong>
                  </a>
                </div>

                <div v-if="!precedente_VERBALE_UVG && !allegati.VERBALE_UVG">
                  <h2 class="q-my-xl">
                    Allega il Verbale o Lettera di Comunicazione della
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
                  <label for="prec-verbale-umvd">Allegato verbale UMVD: </label>
                  <q-btn
                    v-if="
                      check('valutazione_multidimensionale') ||
                      check('VERBALE_UMVD')
                    "
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
                    id="prec-verbale-umvd"
                  >
                    <strong>{{ precedente_VERBALE_UMVD.filename }} </strong>
                  </a>
                </div>
                <div v-if="!precedente_VERBALE_UMVD && !allegati.VERBALE_UMVD">
                  <h2>
                    Allega il Verbale o Lettera di Comunicazione della
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
            <q-card-section>
              <strong>Lettera di comunicazione/Verbale UVG: </strong
              ><q-btn
                icon="delete"
                class="q-mr-md"
                flat
                color="negative"
                title="elimina il documento"
                @click="allegati.VERBALE_UVG = null"
                :aria-label="'elimina il documento ' + allegati.VERBALE_UVG.name"
              />{{ allegati.VERBALE_UVG.name }}
            </q-card-section>
          </div>
          <div v-if="allegati.VERBALE_UMVD">
            <q-card-section>
              <strong>Lettera di comunicazione/Verbale UMVD: </strong>
              <q-btn
                icon="delete"
                class="q-mr-md"
                flat
                color="negative"
                title="elimina il documento"
                @click="allegati.VERBALE_UMVD = null"
                :aria-label="'elimina il documento ' + allegati.VERBALE_UMVD.name"
              />{{ allegati.VERBALE_UMVD.name }}
            </q-card-section>
          </div>
        </template>
        <template v-else>
          <div v-for="(allegato, i) in richiesta.allegati" :key="i">
            <div
              v-if="
                allegato.tipo == 'VERBALE_UVG' &&
                richiesta.valutazione_multidimensionale === 'UVG'
              "
            >
              <strong>Lettera di comunicazione/Verbale UVG:</strong>
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
              <strong>Lettera di comunicazione/Verbale UMVD:</strong>
              <a
                :href="url + '/' + richiesta.numero + '/' + allegato.tipo"
                target="_blank"
              >
                <strong>{{ allegato.filename }} </strong>
              </a>
            </div>
          </div>
        </template>
        <!-- FINE ALLEGATI PUNTEGGIO -->
      </q-card>
      <!-- CARD DATI STRUTTURA -->
      <q-card class="q-mt-xl q-mb-xl">
        <q-card-section>
          <h3>Inserimento dati struttura</h3>
        </q-card-section>
        <div class="q-pt-lg">
          <lms-banner type="info">
            <p>
              L�??assegnazione del buono è subordinata all�??adesione della
              struttura
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
            disable
            dense
            v-model="richiesta.contratto.tipo"
            @keyup.enter="submit"
            val="CONTRATTO_RSA"
            label="Il destinatario è in possesso di un regolare contratto con una struttura aderente alla misura"
          />
          <!-- CONTRATTO LAVORO DOMESTICO -->
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
                    disable
                  />
                </div>
                <label
                  class="col col-lg-11"
                  id="titoloPrivato"
                  for="titoloPrivato"
                >
                  <span
                    :class="
                      !richiesta.contratto.titolo_privato
                        ? 'required-field'
                        : ''
                    "
                    >*</span
                  >Il destinatario è inserito in una struttura residenziale a
                  carattere socio-sanitario o socio-assistenziale a
                  <strong>titolo privato</strong> e non tramite convenzione con
                  il Servizio Sanitario Regionale (SSR).
                </label>
              </div>
              <!-- OBBLIGATORIO CONFERMARE TITOLO PRIVATO -->
              <template
                v-if="richiesta.contratto && richiesta.contratto.titolo_privato"
              >
                <!-- SELEZIONE STRUTTURA -->
                <q-card-section
                  class="q-my-xl"
                  :class="
                    !check('contratto.struttura') ? 'text-overlay--active' : ''
                  "
                >
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
                  <div class="q-my-lg">
                    <div class="row q-gutter-y-lg q-gutter-x-lg date-start-end">
                      <div class="col-4 col-lg-4">
                        <SelectSuggest
                          :options="optionsComuni"
                          :field="'comune'"
                          :label="'Seleziona nome comune'"
                          @setValue="setValue($event)"
                          v-if="check('contratto.struttura')"
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
                        <div class="col-7 col-lg-7">
                          <SelectSuggest
                            :options="optionsStrutture"
                            :field="'richiesta'"
                            :subField="'contratto'"
                            :subFieldChild="'struttura'"
                            :label="'Seleziona nome struttura'"
                            :column="'nome'"
                            @setValue="setValue($event)"
                            v-if="check('contratto.struttura')"
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
                        <div class="col-8 col-lg-8">
                          <strong class="required-field"
                            >Questo comune non ha strutture, selezionane un
                            altro</strong
                          >
                        </div>
                      </template>
                    </div>

                    <template v-if="richiesta.contratto.struttura">
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
                      :disable="
                        !check('contratto.data_inizio') &&
                        !check('contratto.struttura')
                      "
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
                          Data inizio deve essere compresa tra il 1 gennaio 1970
                          e il 31 dicembre 2050
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
                          v-if="
                            check('contratto.tipo') || check('CONTRATTO_RSA')
                          "
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
                            'elimina il documento ' +
                            allegati.CONTRATTO_RSA.name
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
              disable
              dense
              v-model="richiesta.contratto.tipo"
              val="NESSUN_CONTRATTO"
              label="il destinatario non è attualmente inserito in una struttura ma è intenzionato ad effettuare l�??inserimento entro 30 giorni dalla comunicazione di avvenuta erogazione del buono."
            />
          </div>
        </q-card-section>
      </q-card>
      <!-- FINE CARD DATI STRUTTURA -->
      <!-- PULSANTE INVIO -->
      <div class="row button-action-stepper q-my-xxl">
        <div class="col-12 col-md second-block">
          <div class="row justify-end q-gutter-y-lg">
            <div class="col-auto">
              <q-btn
                class="q-mr-xl"
                color="primary"
                @click="rettificaDomanda('IN_RETTIFICA')"
                label="SALVA"
                :disable="isInvalidValid"
              />
            </div>
            <div class="col-auto">
              <q-btn
                class="q-ml-xl"
                color="primary"
                @click="rettificaDomanda('RETTIFICATA')"
                label="INVIA RETTIFICA"
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
import LmsSelect from "components/core/LmsSelect";
import Tooltip from "components/core/Tooltip";
import LmsBanner from "components/core/LmsBanner";
import SelectSuggest from "components/steps/formComponents/SelectSuggest";
import {
  putRichiesta,
  getRichiesta,
  postAllegato,
  postCronologia,
  getStrutture
} from "src/services/api";
import {
  required,
  requiredIf,
  minLength,
  maxLength,
  between,
  integer,
} from "vuelidate/lib/validators";
import { ACCEPTED_FILES_TYPES, MAX_FILE_SIZE } from "src/services/config";
export default {
  name: "PageRectification",
  components: { LmsSelect, Tooltip, LmsBanner, SelectSuggest },
  data () {
    return {
      id: null,
      allegatiOk: true,
      spinner: false,
      comune: null,
      legalAge: false,
      isMounted: false,
      optionsComuni: store.state.comuni,
      optionsStrutture: null,
      url: window.location.origin + "/buonoresbff/api/v1/allegato",
      titolo_options: store.state.titoli_studio,
      rapporto_options: store.state.tipi_rapporto,
      asl_options: store.state.asl,
      contratto_options: store.state.tipi_contratto,
      modifica_modulo: false,
      fileSize: MAX_FILE_SIZE,
      fileAccepted: ACCEPTED_FILES_TYPES,
      precedente_NOMINA_TUTORE: null,
      precedente_PROCURA_SPECIALE: null,
      precedente_DELEGA: null,
      precedente_CARTA_IDENTITA: null,
      precedente_VERBALE_UVG: null,
      precedente_VERBALE_UMVD: null,
      precedente_CONTRATTO_RSA: null,

      copiaContrattoRSATooltip: {
        title: "Copia contratto ospitalità",
        text: "Allegare contributo, convenzione, lettera o altro documento fornito dalla struttura attestante l�??effettivo inserimento residenziale a titolo definitivo.",
      },
      rettificare: [],
      dataContrattoDeterminato:
        "La data di fine di contratto è da indicare solo se è prevista",
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
        DELEGA: null,
        CARTA_IDENTITA: null,
        PROCURA_SPECIALE: null,
        NOMINA_TUTORE: null,
        VERBALE_UVG: null,
        VERBALE_UMVD: null,
        CONTRATTO_RSA: null,
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
      domicilio_destinatario: {
        indirizzo: { required },
        comune: { required },
        provincia: {
          required,
          maxLength: maxLength(2),
          minLength: minLength(2),
        },
      },
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
          maxValue (val) {
            return this.richiesta.contratto.tipo !== "NESSUN_CONTRATTO"
              ? new Date("2050-12-31") > new Date(val)
              : true;
          },
          minValue (val) {
            return this.richiesta.contratto.tipo !== "NESSUN_CONTRATTO"
              ? new Date("2050-12-31") > new Date(val)
              : true;
          },
        },
        struttura: {
          required: requiredIf(function () {
            return this.richiesta.contratto.tipo !== "NESSUN_CONTRATTO";
          }),
        },
        titolo_privato: {
          required: requiredIf(function () {
            return this.richiesta.contratto.tipo !== "NESSUN_CONTRATTO";
          }),
        },
      },
    },
    allegati: {
      DELEGA: {
        required: requiredIf(function () {
          return this.delegaRequired && !this.precedente_DELEGA;
        }),
      },
      CARTA_IDENTITA: {
        required: requiredIf(function () {
          return this.cartaIdentitaRequired && !this.precedente_CARTA_IDENTITA;
        }),
      },
      PROCURA_SPECIALE: {
        required: requiredIf(function () {
          return this.procuraRequired && !this.precedente_PROCURA_SPECIALE;
        }),
      },
      NOMINA_TUTORE: {
        required: requiredIf(function () {
          return this.nominaTutoreRequired && !this.precedente_NOMINA_TUTORE;
        }),
      },
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
    // CONTROLLA SE IL CAMPO/ALLEGATO PUO' ESSERE SBLOCCATO
    check (param) {
      return this.rettificare.indexOf(param) > -1;
    },
    textToUpper (val) {
      this.richiesta.domicilio_destinatario.provincia = val.toUpperCase();
    },
    setValue (obj) {
      if (obj.subFieldChild) {
        this[obj.field][obj.subField][obj.subFieldChild] = obj.model;
      } else if (!obj.subFieldChild && obj.subField) {
        this[obj.field][obj.subField] = obj.model;
      } else {
        this[obj.field] = obj.model;
      }
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

    checkAge (date) {
      return moment().diff(new Date(date), "years");
    },

    async editApplication (id) {
      await store.dispatch("bozzaAllStep");
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
    resetFormContratto () {
      this.richiesta.contratto = {
        tipo: this.richiesta.contratto.tipo,
        data_inizio: null,
        data_fine: null,
        struttura: null,
        titolo_privato: this.richiesta.contratto.titolo_privato,
      };

      this.allegati.CONTRATTO_RSA = null;
    },

    async rettificaDomanda (rettifica) {
      if (rettifica == "RETTIFICATA") {
        await store.dispatch("setSpinner", true);
      }
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
            if (rettifica != this.richiesta.stato) {
              await postCronologia(this.richiesta.numero, "RETTIFICATA");
            }
            await store.dispatch("setSpinner", false);
      

            this.$q.notify({
              type: "positive",
              message:
                rettifica == "RETTIFICATA"
                  ? "Domanda inviata"
                  : "Domanda salvata",
              timeout: 6000,
            });
            if (rettifica == "RETTIFICATA") {
              this.$router.push("/");
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
            await store.dispatch("setSpinner", false);
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
          if (rettifica == "RETTIFICATA") {
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
    validaCF (cf) {
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
        for (i = 0;i < 16;i++) {
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
        for (i = 1;i <= 13;i += 2)
          s += setpari.indexOf(set2.charAt(set1.indexOf(cf.charAt(i))));
        for (i = 0;i <= 14;i += 2)
          s += setdisp.indexOf(set2.charAt(set1.indexOf(cf.charAt(i))));
        if (s % 26 != cf.charCodeAt(15) - "A".charCodeAt(0))
          return "Il codice fiscale non è corretto: il codice di controllo non corrisponde.\n";
      }
      if (cf.length == 11) {
        if (!/^[0-9]{11}$/.test(cf))
          return "Il codice fiscale provvisorio o P.IVA possono solo numeri.";
        let s = 0;
        for (let i = 0;i < 11;i++) {
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
  computed: {
    comuneSelect () {
      return this.comune;
    },
    allegatiValidi () {
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
    richiestaStore () {
      return store.getters["getRichiesta"];
    },
    isInvalidValid () {
      return this.$v ? this.$v.$invalid : null;
    },
    invalidFields () {
      return this.$v
        ? Object.keys(this.$v.$params).filter(
          (fieldName) => this.$v[fieldName].$invalid
        )
        : null;
    },
    rapportoFiltrato () {
      return this.rapporto_options &&
        this.rapporto_options.filter(
          (tipo) => tipo.codice === this.richiesta.delega
        )[0]
        ? this.rapporto_options.filter(
          (tipo) => tipo.codice === this.richiesta.delega
        )[0]["allegato"]
        : null;
    },
    delegaAllowed () {
      return this.rapportoFiltrato
        ? this.rapportoFiltrato.filter(
          (tipo) => tipo.allegato_tipo_cod == "DELEGA"
        )[0]
        : null;
    },
    delegaRequired () {
      return this.delegaAllowed && this.rapportoFiltrato
        ? this.rapportoFiltrato.filter(
          (tipo) => tipo.allegato_tipo_cod == "DELEGA"
        )[0].allegato_obbligatorio
        : null;
    },
    cartaIdentitaRequired () {
      return this.cartaIdentitaAllowed && this.rapportoFiltrato
        ? this.rapportoFiltrato.filter(
          (tipo) => tipo.allegato_tipo_cod == "CARTA_IDENTITA"
        )[0].allegato_obbligatorio
        : null;
    },
    cartaIdentitaAllowed () {
      return this.rapportoFiltrato
        ? this.rapportoFiltrato.filter(
          (tipo) => tipo.allegato_tipo_cod == "CARTA_IDENTITA"
        )[0]
        : null;
    },
    procuraAllowed () {
      return this.rapportoFiltrato
        ? this.rapportoFiltrato.filter(
          (tipo) => tipo.allegato_tipo_cod == "PROCURA_SPECIALE"
        )[0]
        : null;
    },
    procuraRequired () {
      return this.procuraAllowed && this.rapportoFiltrato
        ? this.rapportoFiltrato.filter(
          (tipo) => tipo.allegato_tipo_cod == "PROCURA_SPECIALE"
        )[0].allegato_obbligatorio
        : null;
    },

    nominaTutoreAllowed () {
      return this.rapportoFiltrato
        ? this.rapportoFiltrato.filter(
          (tipo) => tipo.allegato_tipo_cod == "NOMINA_TUTORE"
        )[0]
        : null;
    },
    nominaTutoreRequired () {
      return this.nominaTutoreAllowed && this.rapportoFiltrato
        ? this.rapportoFiltrato.filter(
          (tipo) => tipo.allegato_tipo_cod == "NOMINA_TUTORE"
        )[0].allegato_obbligatorio
        : null;
    },
    radioPunteggio () {
      return this.richiesta.valutazione_multidimensionale;
    },
    validaCFError () {
      return this.validaCF(this.richiesta.contratto.agenzia.cf)
        ? this.validaCF(this.richiesta.contratto.agenzia.cf)
        : null;
    },
  },
  watch: {
    async comuneSelect () {
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
    radioPunteggio () {
      this.allegati.VERBALE_UVG = null;
      this.allegati.VERBALE_UMVD = null;
    },
    // RESETTO  SOLO IL FORM CONTRATTO
    radioContratto () {
      if (this.isMounted) {
        this.richiesta.contratto.titolo_privato = false;
        if (this.richiesta.contratto.tipo === "NESSUN_CONTRATTO") {
          this.richiesta.contratto.incompatibilita_per_contratto = true;
        }
      }
    },

    radioTitoloPrivato () {
      if (this.isMounted) {
        this.resetFormContratto();
      }
    },
    isInvalidValid () {
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
      handler () {
        if (this.isMounted) {
          this.$emit("sbloccaBozza", true);
        }
      },
      deep: true,
    },
    allegatiCurrent: {
      handler () {
        if (this.isMounted) {
          this.$emit("sbloccaBozza", true);
        }
      },
      deep: true,
    },
  },
  async created () {

    // SERVE PER RIMETTERE IN CIMA IL COMPONENTE
    window.scrollTo(0, 0);
    // SE SIAMO IN EDIT PRENDIAMO ID NUMERO DALLA URL
    let id = this.$route.params.id ? this.$route.params.id : null;
    try {
      await store.dispatch("setSpinner", true);
      let { data } = await getRichiesta(id);
      this.richiesta = data;
      if (
        this.richiesta.stato != "DA_RETTIFICARE" &&
        this.richiesta.stato != "IN_RETTIFICA"
      ) {
        await store.dispatch("setSpinner", false);
        this.$q.notify({
          type: "negative",
          message:
            "La domanda deve essere in stato 'da rettificare' per essere modificabile",
          timeout: 6000,
        });
        this.$router.push("/");
        return;
      }
      if (data.contratto.data_inizio)
        data.contratto.data_inizio = data.contratto.data_inizio.substring(
          0,
          10
        );
      if (data.contratto.data_fine)
        data.contratto.data_fine = data.contratto.data_fine.substring(0, 10);
      // SE CI SONO ALLEGATI PREPOPOLIAMO PER LA EDIT
      if (data["allegati"]) {
        let allegati = JSON.parse(JSON.stringify(data["allegati"]));
        for (let allegato of allegati) {
          this["precedente_" + allegato.tipo] = allegato;
        }
      }

      // VERIFICA L'ELENCO DEI CAMPI DA RETTIFICARE
      if (data["rettificare"]) {
        this.rettificare = data["rettificare"];
      }
      // PREVALORIZZA A FALSE ISEE ANZICHE' NULL
      if (!data.hasOwnProperty("attestazione_isee")) {
        data.attestazione_isee = false;
      }

      if (!data["contratto"].hasOwnProperty("titolo_privato")) {
        data.contratto.titolo_privato = false;
      }

      this.richiesta = JSON.parse(JSON.stringify(data));
      await store.dispatch("setSpinner", false);
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
  async updated () {
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
