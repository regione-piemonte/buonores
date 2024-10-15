<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <q-btn flat dense :round="$q.screen.lt.md" no-caps icon="help" class="lms-help-button" aria-label="Aiuto" >
    <q-tooltip anchor="top middle">
      Aiuto
    </q-tooltip>
    <div class="gt-sm q-pl-xs">Aiuto</div>
    <q-menu
        anchor="bottom left"
        auto-close
        class="lms-help-button__menu"
      >
        <q-list class="lms-help-button__menu__list" style="min-width: 120px">
<!--          <q-item clickable @click="$emit('click-contacts')">-->
<!--            <q-item-section>-->
<!--              <q-item-label>-->
<!--                Contatti-->
<!--              </q-item-label>-->
<!--            </q-item-section>-->
<!--          </q-item>-->

          <q-item clickable @click="$emit('click-faq')">
            <q-item-section>
              <q-item-label>
                FAQ
              </q-item-label>
            </q-item-section>
          </q-item>

          <q-item clickable @click="$emit('click-assistance')">
            <q-item-section>
              <q-item-label>
                Assistenza
              </q-item-label>
            </q-item-section>
          </q-item>

        </q-list>
      </q-menu>
  </q-btn>
</template>

<script>
export default {
  name: "LmsHelpButton",
  data() {
    return {
      isMenuVisible: false
    };
  },
  computed: {
    appList() {
      return this.$store.getters["getAppList"];
    },
    workingApp() {
      return this.$store.getters["getWorkingApp"];
    },
    isAssistance() {
      return this.workingApp?.albero_aiuti_visibile
    }

  },
  methods: {
    showMenu() {
      this.isMenuVisible = true;
    },
    hideMenu() {
      this.isMenuVisible = false;
    },
    updateMenu(val) {
      this.isMenuVisible = val;
    }
  }
};
</script>
