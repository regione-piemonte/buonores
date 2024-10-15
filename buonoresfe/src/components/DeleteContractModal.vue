<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <q-dialog v-model="open" :maximized="$q.screen.lt.md" persistent>
    <q-card style="max-width: 800px" v-if="contractModal">
      <q-card-section>
        <lms-banner type="warning">
          <h2>SEI SICURO DI VOLER ELIMINARE IL CONTRATTO</h2>
          <p>
            Se confermi il contratto sarà eliminato
            definitivamente. Procedere?
          </p>
        </lms-banner>

        <div class="row justify-between q-mt-md q-mb-sm">
          <div class="col-auto">
            <q-btn color="negative" outline @click="close()">ANNULLA</q-btn>
          </div>
          <div class="col-auto">
            <q-btn color="primary" outline @click="deleteContract()"
              >CONFERMA ELIMINAZIONE</q-btn
            >
          </div>
        </div>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

      
<script>
import LmsBanner from "components/core/LmsBanner";
export default {
  name: "DeleteContractModal",
  props: {
    openModal: Boolean,
    contractModal: Object,
  },
  components: { LmsBanner },
  data () {
    return {
    };
  },
  computed: {
    open () {
      return this.openModal;
    },
  },
  methods: {
    async deleteContract () {
      let obj = {
        modale: this.contractModal,
      }
      await this.$emit("deleteContract", obj);
    },
    close () {
      this.$emit("deleteContract", null);
    }
  }
};
</script>