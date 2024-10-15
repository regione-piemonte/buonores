<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div>
    <div class="row q-mb-xl items-end q-gutter-md">
      <!-- CODICE FISCALE DA VERIFICARE -->
      <div class="col-8 col-md-6">
        <q-input id="codiceFiscale" :value="CF" @input="textToUpper" label="Codice fiscale"  outlined ></q-input> 
      </div>
      <div class="col">
        <!-- BTN CONFERMA DESKTOP -->
        <q-btn
          v-if="!$q.screen.lt.md"
          @click="verificaCF()"
          flat
          color="primary"
          label="Conferma"
          :disable="erroreCF || !CF"
          
        />

        <!-- BTN CONFERMA MOBILE-->
        <q-btn
          v-else
          @click="verificaCF()"
          color="primary"
          icon="check"
          aria-label="Conferma"
          :disable="erroreCF || !CF"
        />
      </div>
      <div class="col-8">
        <div v-if="erroreCF" class="error">
        <p class="text--error">{{ erroreCF }}</p>
      </div>
      <div v-if="erroreApi" class="error">
        <p class="text--error">{{ erroreApi }}</p>
      </div>
      </div>
    </div>
    <template v-if="spinnerCF">
      <div class="q-pa-md flex flex-center">
        <q-spinner color="primary" size="3em" />
      </div>
    </template>
  </div>
</template>

<script>
import { getAnagrafica } from "src/services/api";
import store from "src/store/index";
export default {
  name: "CheckCF",
  props: {
    codice_fiscale: String,
    type: String,
    destinatario: String
  },
  data() {
    return {
      CF: this.codice_fiscale,
      recipient: this.destinatario,
      errore: "",
      spinnerCF: false,
      tipo: this.type,
    };
  },
  computed: {
    erroreCF() {
      return this.CF ? this.validaCF(this.CF) : null;
    },
    erroreApi() {
      return this.errore ? this.errore : null;
    },
  },
  methods: {
    textToUpper(val) {
      this.CF = val.toUpperCase();
    },
    async verificaCF() {
      this.errore = "";

      if (this.CF == this.recipient) {
        this.errore = "Il codice fiscale indicato corrisponde al destinatario";
        let obj = {
          payload: null,
          type: this.tipo,
          error: true,
        };
        this.$emit("verificaCF", obj);
        return;
      }

      if (this.CF == store.getters["getUtenteRichiedente"].cf) {
        this.errore = "Il codice fiscale indicato corrisponde al richiedente ";
        let obj = {
          payload: null,
          type: this.tipo,
          error: true,
        };
        this.$emit("verificaCF", obj);
        return;
      } else {
        try {
          this.spinnerCF = true;
          let { data } = await getAnagrafica(this.CF);
          let deceduto = data.hasOwnProperty("data_decesso");
          if (deceduto) {
            this.spinnerCF = false;
            this.errore =
              "Il codice inserito risulta appartenere ad una persona deceduta";
            let obj = {
              payload: false,
              type: this.tipo,
              error: true,
            };
            this.$emit("verificaCF", obj);
          } else {
            let payload = JSON.parse(JSON.stringify(data));
            let obj = {
              payload: payload,
              type: this.tipo,
              error: false,
            };
            this.$emit("verificaCF", obj);
            this.spinnerCF = false;
          }
        } catch (err) {
          this.errore =
            "Utente non presente in anagrafica, usa un altro codice fiscale";
          let obj = {
            payload: false,
            type: this.tipo,
            error: true,
          };
          this.$emit("verificaCF", obj);
          this.spinnerCF = false;
        }
      }
    },
    // CONTROLLO CONFORMITA'
    validaCF(cf) {
      this.errore = ""
      let validi, i, s, set1, set2, setpari, setdisp;
      if (cf == "") return "";
      cf = cf.toUpperCase();
      if (cf.length != 16 && cf.length != 11)
        return (
          "La lunghezza del Codice Fiscale non è\n" +
          "corretta: il Codice Fiscale dovrebbe essere lungo\n" +
          "16 caratteri oppure 11 caratteri.\n"
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
          return "Il codice fiscale può contenere solo numeri.";
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
          return "Il codice fiscale non è corretto: il codice di controllo non corrisponde.\n.";
      }
    },
  },
};
</script>
