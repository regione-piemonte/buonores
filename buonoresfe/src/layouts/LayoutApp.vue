<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <q-layout
    view="hHh lpr fff"
    class="lms-layout-app"
    id="app-scelsoc"
    ref="routerView"
  >
    <div aria-label="Navigazione nascosta per screen reader" role="navigation">
      <a href="#main-content" role="link" class="skip-link"
        >Vai al contenuto principale</a
      >
      <a href="#footer" class="skip-link" role="link">Vai al footer</a>
    </div>
    <!-- HEADER -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <lms-layout-header :menu="!$c.IS_ALPHA_CERT" @click-menu="toggleMenu">
      <template #right v-if="utenteRichiedente">
        <!-- Il button notifiche è nascosto -->
        <!-- <lms-layout-header-notification-button
          :unseen-count="messageUnseenList.length"
          class="q-mr-sm"
          @click="toggleNotifications"
        /> -->

        <lms-layout-header-profile-button
          :name="utenteRichiedente.nome"
          :surname="utenteRichiedente.cognome"
          :tax-code="utenteRichiedente.cf"
        />
      </template>

      <!-- TOOLBAR DEL VERTICALE -->
      <!-- --------------------------------------------------------------------------------------------------------- -->
      <template #after>
        <div class="lms-layout-app-toolbar">
          <div class="container">
            <lms-layout-app-toolbar>
              <q-btn
                flat
                href=""
                @click.prevent="goHome"
                class="lms-layout-header__toolbar__title"
                aria-label="Torna alla homepage del servizio buono residenzialità"
              >
                Buono residenzialità
              </q-btn>
              <template #right>
                <!-- <div
                  class="row justify-end"
                  :class="$q.screen.lt.md ? 'q-gutter-x-sm' : 'q-gutter-x-md'"
                >
                  <lms-help-button
                    class="col"
                    @click-contacts="goToHelpContacts"
                    @click-faq="goToHelpFaq"
                    @click-assistance="goToAssistance"
                  />
                </div> -->
              </template>
            </lms-layout-app-toolbar>
          </div>
        </div>
      </template>
    </lms-layout-header>

    <!-- MENU -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <!-- <q-drawer
      v-model="isMenuVisible"
      overlay
      behavior="mobile"
      class="lms-menu-drawer"
    >
      <q-scroll-area class="fit">
        <q-list class="lms-menu-list">
          <div class="q-pa-md"></div>

          <lms-menu-list-item
            v-for="item in appList"
            :key="item.url"
            :href="item.url"
            :menu-list="item.menu"
            :title="item.descrizione"
            :icon="item.icona_url"
            :locked="!item.pubblico && !user"
            :active="isActive(item)"
          />
        </q-list>
      </q-scroll-area>
    </q-drawer> -->

    <!-- LISTA NOTIFICHE -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <q-drawer
      v-model="areNotificationsVisible"
      overlay
      behavior="mobile"
      side="right"
      class="lms-notification-drawer"
    >
      <q-scroll-area class="fit">
        <q-list class="lms-notification-list">
          <!-- HEADER -->
          <!-- ------ -->
          <slot name="header">
            <q-item class="lms-notification-list__header">
              <q-item-section>
                <q-item-label class="text-subtitle2 text-weight-bold">
                  Notifiche
                </q-item-label>
              </q-item-section>
              <q-item-section avatar>
                <q-btn
                  flat
                  round
                  dense
                  icon="settings"
                  color="primary"
                  @click="onClickSettings"
                />
              </q-item-section>
            </q-item>
          </slot>
          <q-separator />

          <!-- LISTA -->
          <!-- ----- -->
          <div class="lms-notification-list__body">
            <!-- PROFILO NON ATTIVO SU NOTIFICATORE -->
            <!-- ---------------------------------- -->
            <template v-if="!notifyContacts">
              <q-item
                clickable
                class="lms-notification-list-item-contacts-activation"
                @click="onNotificationActivate"
              >
                <q-item-section>
                  <q-item-label class="text-body1">
                    <slot name="text">
                      Attiva le notifiche via email, SMS e push
                    </slot>
                  </q-item-label>
                  <q-item-label class="text-body-2 text-grey-8">
                    <slot name="caption">
                      Clicca qui per inserire i contatti nel tuo profilo
                      personale ed attivare il servizio di notifiche.
                    </slot>
                  </q-item-label>
                </q-item-section>
              </q-item>
            </template>

            <!-- NO MESSAGGI -->
            <!-- ----------- -->
            <template v-if="!messageList.length">
              <div class="lms-notification-list-empty">
                <slot>Nessun messaggio ricevuto</slot>
              </div>
            </template>

            <!-- ELENCO NOTIFICHE -->
            <!-- ---------------- -->
            <lms-notification-list-item
              v-for="notification in messageList"
              :key="notification.id"
              :id="notification.id"
              :title="notification.mex.title"
              :sender="notification.sender"
              :datetime="notification.timestamp"
              :read="!!notification.read_at"
              :body="notification.mex.body"
              clickable
              removable
              @remove="onNotificationRemove(notification)"
            >
            </lms-notification-list-item>
          </div>

          <slot name="footer">
            <template v-if="messageList.length > 0">
              <div class="lms-notification-list__footer q-pa-md text-center">
                <lms-button @click="onClickShowAll">Vedi tutte</lms-button>
              </div>
            </template>
          </slot>
        </q-list>
      </q-scroll-area>
    </q-drawer>

    <!-- PAGINE -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <q-page-container class="page-main" id="main-content">
      <template v-if="spinner">
        <div class="container">
          <div class="row">
            <div class="col-12 col-lg-8 offset-lg-2">
              <spinner></spinner>
            </div>
          </div>
        </div>
      </template>
      <template v-if="loading == false">
        <router-view />
      </template>
    </q-page-container>

    <!-- FOOTER -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <lms-layout-footer
      rating
      help-faq
      @click-help-contacts="goToHelpContacts"
      @click-help-faq="goToHelpFaq"
      @click-rating="goToRating"
      @click-help-assistance="goToAssistance"
    />
  </q-layout>
</template>

<script>
import store from "src/store/index";
import { IS_PROD, IS_TEST, IS_DEV } from "src/services/config";
import {
  getNotifyMessages,
  getNotifyMessagesUnseen,
  updateMessagesAsSeen,
  deleteMessage,
  getSportelli,
  getUser,
  getContatti,
  getPreferenze,
  getAnagrafica,
  getNotifyContacts,
  getDecodifiche,
} from "src/services/api";
import { HELP_CONTACTS, SERVICE_RATING } from "src/router/routes";
import LmsLayoutAppToolbar from "src/components/core/LmsLayoutAppToolbar";
import LmsLayoutFooter from "src/components/core/LmsLayoutFooter";
import LmsLayoutHeader from "src/components/core/LmsLayoutHeader";
import Spinner from "src/components/core/Spinner.vue";
import LmsLayoutHeaderProfileButton from "src/components/core/LmsLayoutHeaderProfileButton";
//import LmsLayoutHeaderNotificationButton from "src/components/core/LmsLayoutHeaderNotificationButton";
// import LmsHelpButton from "src/components/core/LmsHelpButton";
// import LmsMenuListItem from "src/components/core/LmsMenuListItem";
import LmsNotificationListItem from "src/components/core/LmsNotificationListItem";
import { apiErrorNotifyDialog } from "src/services/utils";
import {
  appAssistanceForm,
  appAssistanceTree,
  appDetailFaq,
} from "../services/urls";

export default {
  name: "LayoutApp",
  components: {
    LmsLayoutFooter,
    LmsNotificationListItem,
    // LmsMenuListItem,
    // LmsHelpButton,
    LmsLayoutAppToolbar,
    //LmsLayoutHeaderNotificationButton,
    LmsLayoutHeaderProfileButton,
    LmsLayoutHeader,
    Spinner,
  },
  data() {
    return {
      loading: null,
      SERVICE_RATING,
      isMenuVisible: false,
      areNotificationsVisible: false,
      messageList: [],
      messageUnseenList: [],
      sportelli: null,
      richieste: null,
    };
  },
  computed: {
    user() {
      return store.getters["getUser"];
    },
    utenteRichiedente() {
      return store.getters["getUtenteRichiedente"];
    },
    sportelliStore() {
      return store.getters["getSportelli"];
    },
    spinner() {
      return store.getters["getSpinner"];
    },
    appList() {
      let appList = store.getters["getAppList"];
      return appList?.filter((item) => this.mustShowInMenu(item));
    },
    workingApp() {
      return store.getters["getWorkingApp"];
    },
    notifyContacts() {
      return store.getters["getNotifyContacts"];
    },
    workingAppName() {
      return this.workingApp?.descrizione;
    },
    isUserMinor() {
      return store.getters["isUserMinor"];
    },
  },
  watch: {
    $route: function () {
      this.$nextTick(function () {
        setTimeout(() => {
          const focusTarget = this.$refs.routerView.$el;
          focusTarget.setAttribute("tabindex", "-1");
          focusTarget.focus();
          // Rimuovere tabIndex: https://axesslab.com/skip-links/#update-3-a-comment-from-gov-uk
          focusTarget.removeAttribute("tabindex");
        }, 0);
      });
    },
  },

  methods: {
    async goHome() {
      if (this.$route.name !== "personalarea") {
        this.$router.push("/");
      }
    },
    async decodificheLoop() {
      let arrayDecodifiche = [
        "asl",
        "tipi-rapporto",
        "titoli-studio",
        "tipi-contratto",
        "stati-domanda",
        "comuni",
      ];

      for (const item of arrayDecodifiche) {
        try {
          let { data } = await getDecodifiche(item);
          let name = item.replace(/-/g, "_");
          let value = JSON.parse(JSON.stringify(data));
          let obj = {
            name: name,
            value: value,
          };
          store.dispatch("setOptions", obj);
        } catch (err) {
          let message = err.response.data.detail
            ? err.response.data.detail[0].valore
            : "Problemi server, contattare assistenza";
          this.$q.notify({
            type: "negative",
            message: message,
          });
          await store.dispatch("setSpinner", false);
          this.loading = false;
        }
      }
    },
    goPortal() {
      if (IS_PROD) {
        // PRODUZIONE
        window.location.href =
          "https://www.sceltasociale.it/api/auth/bff/login";
      } else if (IS_TEST) {
        // TEST E DEV
        window.location.href =
          "https://tst-www-sceltasociale.portali.csi.it/api/auth/bff/login";
      }
    },

    onClickTitle() {
      this.$router.push({ name: "personalarea" });
    },
    toggleMenu() {
      this.isMenuVisible = !this.isMenuVisible;
    },
    toggleNotifications() {
      this.areNotificationsVisible = !this.areNotificationsVisible;
      this.setMessageListAsSeen();
    },
    isActive(app) {
      return app.codice === this.workingApp?.codice;
    },
    async onNotificationRemove(notification) {
      let taxCode = this.user.cf;
      let id = notification.id;

      try {
        await deleteMessage(taxCode, id);
        this.messageList = this.messageList.filter(
          (n) => n.id !== notification.id
        );
      } catch (error) {
        let message = "Non è stato possibile rimuovere la notifica";
        apiErrorNotifyDialog({ error, message });
      }
    },
    async setMessageListAsSeen() {
      // Facciamo qualcosa solo se ci sono delle notifiche "non viste"
      if (this.messageUnseenList.length <= 0) return;

      try {
        let taxCode = this.user.cf;
        await updateMessagesAsSeen(taxCode, this.messageUnseenList);
        this.messageUnseenList = [];
      } catch (err) {
        console.error(err);
      }
    },
    goToHelpContacts() {
      this.$router.push(HELP_CONTACTS);
    },
    goToHelpFaq() {
      let url = appDetailFaq();
      window.open(url);
    },
    goToAssistance() {
      let isAssistance = this.workingApp?.albero_aiuti_visibile;
      let appCode = this.workingApp?.portale_codice ?? "";
      let url = isAssistance
        ? appAssistanceTree(appCode)
        : appAssistanceForm(appCode);
      window.location.assign(url);
    },
    onClickSettings() {
      let url = "/la-mia-salute/profilo-utente/#/preferenze";
      window.location.assign(url);
    },
    onClickShowAll() {
      let url = "/la-mia-salute/profilo-utente/#/notifiche";
      window.location.assign(url);
    },
    onNotificationActivate() {
      let url = "/la-mia-salute/profilo-utente/#/contatti/crea";
      window.location.assign(url);
    },
    mustShowInMenu(item) {
      if (this.$q.platform.is.mobile) return item.visibile_menu_mobile;
      if (this.$q.platform.is.desktop) return item.visibile_menu_desktop;
      return true;
    },
    goToRating() {
      this.$router.push(SERVICE_RATING);
    },
  },
  async created() {
    await store.dispatch("setSpinner", true);
    this.loading = true;
    if (!this.sportelliStore) {
      try {
        let { data } = await getSportelli();
        let sportelli = JSON.parse(JSON.stringify(data));
        store.dispatch("setSportelli", { sportelli });
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
        this.loading = false;
      }
    }

    if (!this.utenteRichiedente) {
      try {
        let contacts = null;
        try {
          let { data } = await getContatti();
          contacts = JSON.parse(JSON.stringify(data));
          if (!contacts || !contacts.user_id || !contacts["email"]) {
            this.goPortal();
          }
          await store.dispatch("setContatti", contacts);
        } catch (err) {
          this.goPortal();
          await store.dispatch("setSpinner", false);
          this.loading = false;
        }
        // CHIAMATA ANAGRAFICA SE CONTATTI NON VA IN DEV METTIAMO IL CF DI VALENTE
        try {
          let anagrafica = null;
          if (IS_TEST || IS_DEV) {
            anagrafica = {
              cf: contacts.user_id,
              nome: "UTENTE",
              cognome: "DI TEST",
              data_nascita: "1970-01-01",
              stato_nascita: "Italia",
              comune_nascita: "TO",
              provincia_nascita: "TO",
              indirizzo_residenza: "VIA DELLE TROFIE",
              comune_residenza: "TORINO",
              provincia_residenza: "TO",
            };
          } else {
            try {
              let { data } = await getAnagrafica(contacts.user_id);
              anagrafica = JSON.parse(JSON.stringify(data));
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
              this.loading = false;
            }
          }
          await store.dispatch("setUtenteRichiedente", anagrafica);
          await store.dispatch("setSpinner", false);
          this.loading = false;
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
          this.loading = false;
        }
        // CHIAMATA PREFERENZE
        let preferences = null;
        try {
          let { data } = await getPreferenze();
          preferences = JSON.parse(JSON.stringify(data));
          if (!preferences || preferences.indexOf("email") == -1) {
            this.goPortal();
          }
          await store.dispatch("setPreferenze", preferences);
        } catch (err) {
          await store.dispatch("setSpinner", false);
          this.goPortal();
          this.loading = false;
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
        this.loading = false;
      }
    }

    await this.decodificheLoop();
  },
  mounted() {
    this.$nextTick(() => {
      document.body.addEventListener(
        "keyup",
        function (e) {
          if (e.key === "Tab") {
            const focused = Array.from(
              document.getElementsByClassName("keyFocus")
            );
            focused.concat(document.getElementsByTagName("a"));
            new Promise((resolve) => {
              focused.forEach(function (element) {
                element.classList.remove("keyFocus");
              });
              resolve();
            })
              .then(() => {
                if (
                  document.activeElement.classList.contains(
                    "v-btn--disabled"
                  ) ||
                  document.activeElement.classList.contains(
                    "cursor-pointer-off"
                  ) ||
                  document.activeElement.classList.contains("disabled-card h3")
                ) {
                  document.activeElement.classList.remove("keyFocus");
                } else {
                  if (
                    document.activeElement.offsetParent.classList.value ===
                      "v-select__slot" ||
                    document.activeElement.offsetParent.classList.value ===
                      "v-input--selection-controls__input" ||
                    document.activeElement.offsetParent.classList.value ===
                      "v-toolbar__title"
                  ) {
                    document.activeElement.offsetParent.classList.add(
                      "keyFocus"
                    );
                  } else {
                    document.activeElement.classList.add("keyFocus");
                  }
                }
              })
              .catch((error) => {
                console.log(error);
              });
          }
          if (e.key === "Escape") {
            document.activeElement.classList.remove("keyFocus");
          }
        },
        false
      );
    });
  },
};
</script>

<style lang="sass">
.lms-menu-list__logo
  width: 100%
  max-width: 250px
  height: auto

.lms-notification-list__body > .lms-notification-list-item:not(:last-of-type)
  border-bottom: 1px solid rgba(0, 0, 0, .12)

.lms-notification-list-empty
  text-align: center
  padding: map-get($space-lg, 'y') map-get($space-lg, 'x')
  color: $lms-text-faded-color

.lms-notification-list-item-contacts-activation
  background-color: $blue-2
  padding: map-get($space-md, 'y') map-get($space-md, 'x')
</style>
