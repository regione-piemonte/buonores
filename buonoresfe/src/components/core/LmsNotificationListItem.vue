<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div
      class="lms-notification-list-item"
      :class="classes"
      v-on="listeners"
      @click="onClick"
  >
    <!-- Aggiungiamo il div necessario per dare l'effetto "hover" -->
    <template v-if="clickable">
      <div class="q-focus-helper"></div>
    </template>

    <div class="lms-notification-list-item__header row justify-between">
      <div class="col">
        <div class="text-subtitle2 text-weight-bold">
          {{ title }}
        </div>

        <template v-if="senderName && senderName !== ''">
          <div>
            <q-badge
                color="info"
                text-color="black"
                class="q-px-xs q-py-none no-border-radius text-caption"
            >
              {{ senderName }}
            </q-badge>
          </div>
        </template>
      </div>

      <template v-if="removable">
        <div class="col-auto">
          <q-btn round flat dense icon="close" @click.stop="onRemove"  aria-label="rimuovi"/>
        </div>
      </template>
    </div>

    <div class="lms-notification-list-item__body text-body2 relative-position" :inner-html.prop="body | truncate(120)" >

    </div>

    <template v-if="datetime">
      <div class="lms-notification-list-item__footer text-caption text-right">
        {{ datetime | datetime }}
      </div>
    </template>

    <!-- DIALOG -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <q-dialog v-model="isRemoveDialogVisible" persistent>
      <q-card>
        <q-card-section class="text-h6">
          Rimuovi notifica
        </q-card-section>

        <q-card-section>
          Stai per rimuovere la notifica <strong>"{{ title }}"</strong>.
          <br />
          Vuoi proseguire?
        </q-card-section>

        <q-card-actions align="right">
          <q-btn v-close-popup outline label="No, annulla" color="primary" />
          <q-btn
              v-close-popup
              outline
              label="Sì, rimuovi"
              color="negative"
              @click="onRemoveConfirmed"
          />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
export default {
  name: "LmsNotificationListItem",
  props: {
    id: { type: String, required: true },
    title: { type: String, required: true },
    sender: { type: String, required: false, default: null },
    datetime: { type: [String, Date], required: false, default: null },
    read: { type: Boolean, required: false, default: false },
    clickable: { type: Boolean, required: false, default: false },
    removable: { type: Boolean, required: false, default: false },
    body: { type: String, required: true },
  },
  data() {
    return {
      isRemoveDialogVisible: false
    };
  },
  computed: {
    listeners() {
      const { click, ...listeners } = this.$listeners;
      return listeners;
    },
    appList() {
      return this.$store.getters["getAppList"];
    },
    classes() {
      let result = [];

      if (this.read) {
        result.push("lms-notification-list-item--read");
      }

      if (this.clickable) {
        result.push("lms-notification-list-item--clickable");
        result.push("cursor-pointer");
        result.push("q-hoverable");
        result.push("q-focusable");
        result.push("q-link");
        result.push("relative-position");
      }

      return result;
    },
    senderName(){
      let senderApp =  this.appList?.find(app => app.notifiche_codice === this.sender )

      return senderApp?.descrizione ?? ''
    }
  },
  methods: {
    onClick() {
      if (!this.clickable) return;

      let eventName = "click";
      let url = "/la-mia-salute/profilo-utente/#/notifiche/" + this.id;

      if (eventName in this.$listeners) return this.$emit(eventName, url);

      window.location.assign(url);
    },
    onRemove() {
      this.isRemoveDialogVisible = true;
    },
    onRemoveConfirmed() {
      this.$emit("remove");
    }
  }
};
</script>

<style lang="sass">
.lms-notification-list-item
  padding: map-get($space-sm, 'y') map-get($space-md, 'x')
  background-color: #E3F2FD

.lms-notification-list-item--read
  background-color: transparent

.lms-notification-list-item__body,
.lms-notification-list-item__footer
  margin-top: map-get($space-sm, 'y')
</style>
