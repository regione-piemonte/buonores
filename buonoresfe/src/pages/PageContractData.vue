<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div v-if="idRichiesta && !loading">
    <div class="q-page lms-page">
      <BreadcrumbsCustom
        :name="utenteDestinatario?.nome + ' ' + utenteDestinatario?.cognome"
        :path1="'gestione'"
        :label1="'Gestione'"
        :path2="'daticontratto'"
        :label2="'Dati del contratto'"
        :title="'Dati del contratto'"
        :id="idRichiesta"
      ></BreadcrumbsCustom>
      <template v-if="contrattiList && contrattiList.length > 0">
        <q-card class="q-mt-xl q-mb-xl">
          <!-- CARD ELENCO CONTRATTI -->
          <q-card-section>
            <div class="row">
              <h3 class="col-12">Fornitore e contratto</h3>
            </div>
            <lms-banner type="info">
              <h2>
                Informazione su Fornitore e contratto
              </h2>
              <p>
                Verifica che i dati inseriti corrispondano alla situazione
                attuale del destinatario del buono
              </p>
            </lms-banner>
            <!-- CICLO CONTRATTI -->

            <div
              v-for="(item, i) of contrattiList"
              :key="i"
              class="border-left--primary"
            >
              <div>
                Data Inizio
                <strong>{{ item.contratto.data_inizio | formatDate }} </strong>
                Data fine
                <strong>
                  <template v-if="item.contratto.data_fine">
                    {{ item.contratto.data_fine | formatDate }}
                  </template>
                  <template v-else>-</template></strong
                >
              </div>
              <div class="row q-mb-sm" v-if="item.contratto.struttura">
                <div class="col-md-9 col-sm-12">
                  Struttura:
                  <strong
                    >{{ item.contratto.struttura?.nome }}
                    </strong
                  >
                </div>
                <div
                  class="col-md-9 col-sm-12"
                  v-if=" item.contratto.struttura?.indirizzo"
                >
                  Indirizzo:
                  <strong>{{ item.contratto.struttura?.indirizzo }} {{ item.contratto.struttura?.comune }} ({{ item.contratto.struttura?.provincia }})</strong>
                </div>
                <div
                  class="col-md-9 col-sm-12"
                  v-if="item.contratto.struttura?.piva"
                >
                  P.IVA:
                  <strong>{{ item.contratto.struttura?.piva}}</strong>
                </div>
                <div
                  class="col-12"
                  v-if="item.allegati && item.allegati.length > 0"
                >
                  <div v-for="(allegato, i) of item.allegati" :key="i">
                    <div class="row q-mb-sm q-col-gutter-xl">
                      <div class="col-12">
                        <dl>
                          <dd>
                            <a
                              :href="url + '/' + allegato.allegato_id"
                              target="_blank"
                              class="text--info position-relative"
                            >
                              {{ allegato.filename }}
                            </a>
                          </dd>
                        </dl>
                      </div>
                      <div class="col-12 col-md-4">
                        <dl>
                          <dd>
                            {{ allegato.importo }}
                          </dd>
                        </dl>
                      </div>
                      <div class="col-12 col-md-4">
                        <dl>
                          <dd>
                            {{ allegato.data | formatDate }}
                          </dd>
                        </dl>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="col-12" v-else>
                  <h3>Non sono presenti allegati</h3>
                </div>
                
                <div class="col-12 q-mb-lg">
                  <q-btn
                    @click="openModalDeleting(item)"
                    outline
                    color="red"
                    :aria-label="'elimina il contratto numero '+ item.contratto.id"
                    label="ELIMINA CONTRATTO"
                  ></q-btn>
                </div>
                <!-- ### NASCOSTo MOMENTANEAMENTE -->
                <!-- <div class="col-12 q-mb-lg" v-if="!item.contratto.data_fine">
                  <q-btn
                    @click="openModalClosing(item)"
                    outline
                    class="text--error"
                    :aria-label="getChiusuraContrattoAriaLabel(item)"
                    label="COMUNICA CHIUSURA CONTRATTO"
                  ></q-btn> 
                </div> -->
              </div>
            </div>
          </q-card-section>
          <!-- FINE CARD ELENCO CONTRATTI -->

          <!-- CARD NUOVO CONTRATTO -->
          <q-card-section>
            <div class="row">
              <h3 class="col-12">Contratto aggiuntivo</h3>
            </div>
            <lms-banner type="info">
              <p>
                Se hai
                <strong
                  >un altro contratto attivo compila ed invia il modulo apposito</strong
                >
              </p>
            </lms-banner>
            <div class="row q-mb-sm">
              <div class="col-12">
                <q-btn
                  @click="openApplication(idRichiesta, 'nuovocontratto')"
                  outline
                  color="primary"
                  :aria-label="'aggiungi contratto: '"
                  label="AGGIUNGI CONTRATTO"
                ></q-btn>
              </div>
            </div>
          </q-card-section>
          <!-- FINE  CARD NUOVO CONTRATTO -->
        </q-card>
      </template>

      <template v-else>
        <q-card class="q-mt-xl q-mb-xl">
          <!-- CARD ELENCO CONTRATTI -->
          <q-card-section>
            <div class="row">
              <h3 class="col-12">Fornitore e contratto</h3>
            </div>
            <lms-banner type="negative">
              <p>
                Non hai nessun contratto attivo. <br />
                Inserisci un contratto della durata di almeno 12 mesi per avere
                diritto all'erogazione del Buono
              </p>
            </lms-banner>
            Fornitore:
            <strong>NESSUNO</strong>
            <div class="row q-mb-sm q-mt-sm">
              <div class="col-12">
                <q-btn
                  @click="openApplication(idRichiesta, 'nuovocontratto')"
                  color="primary"
                  :aria-label="'aggiungi contratto: '"
                  label="AGGIUNGI CONTRATTO"
                ></q-btn>
              </div>
            </div>
          </q-card-section>
        </q-card>
      </template>
    </div>
    <!-- MODALE CHIUSURA -->
    <template v-if="openModalClose">
      <close-contract-modal
        :openModal="openModalClose"
        :contractModal="contractModal"
        @closeContract="closeContract($event)"
      ></close-contract-modal>
    </template>
    <!-- FINE MODALE CHIUSURA -->

    <!-- MODALE ELIMINAZIONE -->
    <template v-if="openModalDelete">
      <delete-contract-modal
        :openModal="openModalDelete"
        :contractModal="contractModal"
        @deleteContract="deleteContract($event)"
      ></delete-contract-modal>
    </template>
    <!-- FINE MODALE ELIMINAZIONE -->
  </div>
</template>
<script>
import moment from "moment";
import store from "src/store/index";
import LmsBanner from "components/core/LmsBanner";
import BreadcrumbsCustom from 'src/components/reporting/BreadcrumbsCustom.vue';
import CloseContractModal from "components/CloseContractModal";
import DeleteContractModal from "components/DeleteContractModal";
import { snackbarError } from "src/services/utils";
import {
  getContratti, putContratti, deleteContratti
} from "src/services/api";


export default {
  name: "PageContractData",
  components: { BreadcrumbsCustom, LmsBanner, CloseContractModal, DeleteContractModal },
  data () {
    return {
      application: null,
      closeContractOpen: false,
      deleteContractOpen: false,
      idRichiesta: null,
      loading: false,
      contratti: [],
      url: window.location.origin + "/buonoresbff/api/v1/allegato-buono",
    }
  },
  filters: {
    formatDate: function (date) {
      return date ? moment(date).format("DD/MM/YYYY") : null;
    },
  },
  methods: {
    getChiusuraContrattoAriaLabel (item) {
      const id = item.contratto.id
      const dataInizio = moment(item.contratto.data_inizio).lang('it').format('DD MMMM YYYY');
      return `comunica chiusura contratto: ${id} - data inizio: ${dataInizio}`;
    },
    async openApplication (id, route) {
      this.$router.push({ name: route, params: { id: id } });
    },
    openModalClosing (application) {
      this.closeContractOpen = true;
      this.application = application
    },
    openModalDeleting (application) {
      this.deleteContractOpen = true;
      this.application = application
    },
    async getContrattiList (id) {
      try {
        let { data } = await getContratti(id);
        this.contratti = data;
        this.loading = false;
        await store.dispatch("setSpinner", false);
      } catch (err) {
        this.loading = false;
        snackbarError(err);
        await store.dispatch("setSpinner", false);
      }
    },
    async closeContract (obj) {
      this.closeContractOpen = false;
      this.application = null;
      if (obj) {
        this.loading = true;
        await store.dispatch("setSpinner", true);
        obj.modale.contratto.data_fine = obj.data_fine
        try {
          let { data } = await putContratti(
            this.idRichiesta,
            obj.modale.contratto.id,
            obj.modale.contratto
          );
          this.$q.notify({
            type: "positive",
            message: "Contratto chiuso",
          });
        } catch (err) {
          await store.dispatch("setSpinner", false);
          snackbarError(err);
        }
        await this.getContrattiList(this.idRichiesta)
      } else {
        this.loading = false;
        await store.dispatch("setSpinner", false);
      }
    },
    async deleteContract (obj) {
      this.deleteContractOpen = false;
      this.application = null;
      if (obj) {
        this.loading = true;
        await store.dispatch("setSpinner", true);
        try {
          let { data } = await deleteContratti(
            this.idRichiesta,
            obj.modale.contratto.id,
            obj.modale.contratto
          );
          this.$q.notify({
            type: "positive",
            message: "Contratto eliminato",
          });
        } catch (err) {
          await store.dispatch("setSpinner", false);
          snackbarError(err);
        }
        await this.getContrattiList(this.idRichiesta)
      } else {
        this.loading = false;
        await store.dispatch("setSpinner", false);
      }
    },
  },
  computed: {
    utenteDestinatario () {
      return store.getters["getUtenteDestinatario"];
    },
    openModalClose () {
      return this.closeContractOpen;
    },
    openModalDelete () {
      return this.deleteContractOpen;
    },
    contractModal () {
      return this.application ? this.application : null;
    },
    contrattiList () {
      return this.contratti ? this.contratti : []
    }

  },
  async created () {
    this.loading = true;
    await store.dispatch("setSpinner", true);
    window.scrollTo(0, 0);
    let id = this.$route.params.id
      ? this.$route.params.id
      : null;
    if (this.utenteDestinatario == null) {
      await store.dispatch("setSpinner", false);
      this.$router.push("/");
    } else {
      this.idRichiesta = id;
      await this.getContrattiList(this.idRichiesta)
      this.loading = false;
      await store.dispatch("setSpinner", false);
    }
  }
};
</script>
