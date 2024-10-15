<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div class="lms-the-guard-fcm">
    <slot />

    <!-- MODAL RICHIESTA PERMESSI -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <template v-if="messaging">
      <q-dialog v-model="isUiPermissionsVisible" position="bottom">
        <q-card class="lms-fcm-permission-dialog">
          <q-card-section>
            <h5 class="lms-fcm-permission-dialog__title">Notifiche Push</h5>
          </q-card-section>

          <q-card-section class="lms-fcm-permission-dialog__text">
            <span class="lms-fcm-permission-dialog__text__app_name">
              La mia salute
            </span>
            vorrebbe mandarti delle notifiche in caso di eventi importanti.
          </q-card-section>

          <q-card-section>
            <lms-buttons>
              <lms-button @click="onUiPermissionsGranted"> Ok </lms-button>
              <lms-button outline @click="hideUiPermission">
                No, grazie
              </lms-button>
            </lms-buttons>
          </q-card-section>
        </q-card>
      </q-dialog>
    </template>
  </div>
</template>

<script>
import firebase from "@firebase/app";
import "@firebase/messaging";
import { SessionStorage } from "quasar";

const STORAGE_KEY = "LMS_PWA_BANNER_FCM_DO_NOT_ASK";

export default {
  name: "TheGuardFcm",
  data() {
    return {
      messaging: null,
      isUiPermissionsVisible: false,
    };
  },
  computed: {
    user() {
      return this.$store.getters["getUser"];
    },
    notifyContacts() {
      return this.$store.getters["getNotifyContacts"];
    },
    fcmConfig() {
      return this.$store.getters["getFcmConfig"];
    },
    fcmVapidKey() {
      return this.$store.getters["getFcmVapidKey"];
    },
    fcmToken() {
      return this.$store.getters["getFcmToken"];
    },
    watchable() {
      return {
        fcmToken: this.fcmToken,
        user: this.user,
        notifyContacts: this.notifyContacts,
      };
    },
  },
  async created() {
    if (
      "serviceWorker" in navigator &&
      "Notification" in window &&
      window.Notification.permission !== "denied"
    ) {
      await this.init();

      // Se l'utente non ha già un token, è loggato ed ha i contatti impostati sul notificatore
      // => gli chiediamo i permessi per le notifiche
      this.$watch(() => this.watchable, {
        immediate: true,
        handler(val, oldVal) {
          console.debug("[FCM] watchable", { val, oldVal });
          let { fcmToken, user, notifyContacts } = val;

          if (!fcmToken && user && notifyContacts) {
            if (window.Notification.permission === "granted") {
              // Se l'utente ha già fornito il consenso
              // => prova ad ottenere il token FCM
              this.onPermissionGranted();
            } else {
              // Altrimenti (se non l'abbiamo fatto già prima)
              // chiediamo all'utente di attivare le notifiche push
              let isPermissionAsked = SessionStorage.getItem(STORAGE_KEY);
              if (!isPermissionAsked) {
                this.askPermissions();
              }
            }
          }
        },
      });
    }
  },
  methods: {
    init() {
      // Prendiamo il SW registrato
      return navigator.serviceWorker.ready.then((sw) => {
        const config = this.fcmConfig;
        const key = this.fcmVapidKey;

        // Inizializza l'app di firebase con la configurazione
        console.debug("[FCM] Initialize with config:", { config });

        // Aggiunto controllo per evitare ripetizioni di istanze
        if (firebase.messaging.isSupported() & !firebase.apps.length) {
          firebase.initializeApp(config);

          // Otteniamo un'istanza della FCM (Firebase Cloud Messaging)
          this.messaging = firebase.messaging();

          // Diciamo ad FCM qual è il SW (Service Worker) che gestirà le notifiche push in background
          console.debug("[FCM] FCM use this SW:", { sw });
          this.messaging.useServiceWorker(sw);
        }

        // Impostiamo le chiavi pubbliche per le notifiche push
        console.debug("[FCM] FCM use this public key:", { key });
        this.messaging.usePublicVapidKey(key);

        // Imposta un handler per le notifiche push che viene invocata quando:
        //  - un messaggio arriva in foreground
        //  - un utente clicca su una notifica creata da `setBackgroundMessageHandler` nel Service Worker.
        //    quindi si tratta di un Data Messages
        this.messaging.onMessage(this.onForegroundMessage);
      });
    },
    // NOTA: `payload` contiene solo i dati presenti nel campo `data` del messaggio push. La proprietà `notification`
    //       viene automaticamente rimossa da Firebase
    onForegroundMessage(payload) {
      console.debug("[FCM] Message foreground: ", payload);

      let actions = [];
      let url = payload?.notification?.click_action ?? null;

      // Se nella notifica è presente la call to action => aggiungiamo il bottone per invocare la call to action
      if (url) {
        actions.push({
          label: "vai",
          handler: () => {
            window.location.assign(url);
          },
        });
      }

      let options = {
        message: payload.notification.title,
        caption: payload.notification.body,
        multiLine: true,
        color: "info",
        position: "bottom-right",
        timeout: 5000,
        actions,
      };

      this.$q.notify(options);
    },
    onTokenError(err) {
      console.error("[FCM] Token error:", { err });
    },
    askPermissions() {
      console.debug("[FCM] Asking UI permission to user");
      this.isUiPermissionsVisible = true;
    },
    hideUiPermission() {
      this.isUiPermissionsVisible = false;
      SessionStorage.set(STORAGE_KEY, true);
    },
    onUiPermissionsGranted() {
      this.hideUiPermission();
      console.debug("[FCM] Asking permission to user");

      this.messaging
        .requestPermission()
        .then(this.onPermissionGranted)
        .catch(this.onPermissionDenied);
    },
    onPermissionDenied(err) {
      console.debug("[FCM] Permission denied: ", { err });
    },
    onPermissionGranted(result) {
      console.debug("[FCM] Permission granted");
      console.debug("[FCM] Listening to FCM Token refresh...");
      this.messaging.onTokenRefresh(this.onToken);

      console.debug("[FCM] FCM getting token");
      this.messaging.getToken().then(this.onToken).catch(this.onTokenError);
    },
    onToken(fcmToken) {
      console.debug("[FCM] Token retrieved", { fcmToken });
      if (fcmToken) {
        this.$store.dispatch("setFcmToken", { fcmToken });
      }
    },
  },
};
</script>

<style lang="sass">
.lms-fcm-permission-dialog__title
  margin: 0

.lms-fcm-permission-dialog__text__app_name
  font-weight: map-get($text-weights, 'bold')
</style>
