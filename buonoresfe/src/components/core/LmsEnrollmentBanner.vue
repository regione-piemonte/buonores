<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div class="lms-enrollment-banner row q-col-gutter-y-md" :class="classes">
    <!-- IMMAGINE -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <div class="col-12 col-md col-lg-5 q-px-lg">
      <img
        src="/statics/la-mia-salute/immagini/arruolamento.svg"
        alt="Icona arruolamento"
      />
    </div>

    <!-- TESTO -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <div class="col-12 col-md q-gutter-y-sm">
      <div class="lms-enrollment-banner__title">
        Per utilizzare i servizi online della sanità piemontese è necessario
        aver aperto il Fascicolo Sanitario Elettronico
      </div>

      <div class="lms-enrollment-banner__text">
        <slot name="textBefore" />

        <slot name="text">
          <template v-if="!autoShowMore">
            <div class="lms-enrollment-banner__show_more">
              <lms-button
                flat
                dense
                no-caps
                :ripple="false"
                no-min-width
                rounded
                class="q-mb-sm"
                @click="showMore = !showMore"
              >
                Scopri di più
                <q-icon
                  name="keyboard_arrow_down"
                  size="sm"
                  class="lms-enrollment-banner__show_more__icon"
                />
              </lms-button>
            </div>
          </template>

          <q-slide-transition>
            <div
              v-show="autoShowMore || showMore"
              class="lms-enrollment-banner__text__more"
            >
              <p>
                Il Fascicolo Sanitario Elettronico (FSE) traccia la tua storia
                clinica rendendo disponibili tutte le informazioni e i documenti
                prodotti sul territorio regionale da medici e operatori sanitari
                anche di strutture diverse (ASL, Aziende Ospedaliere, medici di
                famiglia e pediatri �?�).
              </p>
              <p class="no-margin">
                Puoi inoltre arricchire il tuo fascicolo inserendo dati nel tuo
                taccuino digitale, e archiviando in cartelle documenti clinici
                in tuo possesso.
              </p>
            </div>
          </q-slide-transition>
        </slot>

        <slot name="textAfter" />
      </div>

      <!-- AZIONI -->
      <!-- ----------------------------------------------------------------------------------------------------------- -->
      <slot name="actions">
        <template v-if="activable || closable || dontAsk">
          <div class="col-12 q-mt-lg q-gutter-y-sm">
            <template v-if="activable">
              <lms-button block @click="$emit('click-activate')">
                Attiva il fascicolo sanitario
              </lms-button>
            </template>

            <template v-if="closable">
              <lms-button block outline @click="$emit('click-close')">
                Al momento non mi interessa
              </lms-button>
            </template>

            <template v-if="dontAsk">
              <q-checkbox
                :value="dontAskValue"
                @input="$emit('update:dontAskValue', $event)"
              >
                Non mostrarmi più questo messaggio
              </q-checkbox>
            </template>
          </div>
        </template>
      </slot>
    </div>
  </div>
</template>

<script>
export default {
  name: "LmsEnrollmentBanner",
  props: {
    activable: { type: Boolean, required: false, default: false },
    closable: { type: Boolean, required: false, default: false },
    dontAsk: { type: Boolean, required: false, default: false },
    dontAskValue: { type: Boolean, required: false, default: false }
  },
  data() {
    return {
      showMore: false
    };
  },
  computed: {
    autoShowMore() {
      return this.$q.screen.gt.md;
    },
    classes() {
      return {
        "lms-enrollment-banner--showing-more":
          this.autoShowMore || this.showMore
      };
    }
  }
};
</script>

<style lang="sass">
.lms-enrollment-banner__title
  font-size: map-get($body2, 'size')
  line-height: map-get($body2, 'line-height')
  letter-spacing: map-get($body2, 'letter-spacing')
  font-weight: map-get($text-weights, 'bold')

.lms-enrollment-banner__text
  font-size: map-get($body2, 'size')
  line-height: map-get($body2, 'line-height')
  letter-spacing: map-get($body2, 'letter-spacing')
  font-weight: map-get($body2, 'weight')

.lms-enrollment-banner__show_more
  text-align: center

.lms-enrollment-banner__show_more__icon
  transition: all .5s ease

.lms-enrollment-banner--showing-more
  .lms-enrollment-banner__show_more__icon
    transform: rotateZ(-180deg)

@media (min-width: $breakpoint-md-min)
  .lms-enrollment-banner__show_more
    text-align: initial
</style>
