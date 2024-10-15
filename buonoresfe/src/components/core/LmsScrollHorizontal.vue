<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div class="lms-scroll-horizontal" :class="rootClasses">
    <!-- CONTROLLO SINISTRO -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <template v-if="controls">
      <div
        v-touch-repeat.mouse="scrollLeft"
        class="col-auto lms-scroll-horizontal-control lms-scroll-horizontal-control--left"
        @click="scrollLeft"
      >
        <slot
          name="control-left"
          :is-left-edge="isLeftEdge"
          :is-right-edge="isRightEdge"
        >
          <template v-if="!controlsFloating || !isLeftEdge">
            <q-btn
              round
              flat
              dense
              class="lms-scroll-horizontal-control__button"
              :disable="isLeftEdge"
            >
              <q-icon
                name="chevron_left"
                size="xl"
                class="lms-scroll-horizontal-control__icon"
              />
            </q-btn>
          </template>
        </slot>
      </div>
    </template>

    <!-- SCROLLER -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <div
      class="lms-scroll-horizontal-scroller-wrapper relative-position overflow-hidden"
      :class="wrapperClasses"
    >
      <div
        v-scroll="scrolled"
        v-mutation="mutation"
        v-touch-pan.horizontal.prevent.mouse="handlePan"
        ref="container"
        class="lms-scroll-horizontal-scroller scroll"
        :class="scrollClasses"
      >
        <slot />

        <q-resize-observer debounce="250" @resize="onContainerResize" />
      </div>
    </div>

    <!-- CONTROLLO DESTRO -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <template v-if="controls">
      <div
        v-touch-repeat.mouse="scrollRight"
        class="col-auto lms-scroll-horizontal-control lms-scroll-horizontal-control--right"
        @click="scrollRight"
      >
        <slot
          name="control-right"
          :is-left-edge="isLeftEdge"
          :is-right-edge="isRightEdge"
        >
          <template v-if="!controlsFloating || !isRightEdge">
            <q-btn
              round
              flat
              dense
              class="lms-scroll-horizontal-control__button"
              :disable="isRightEdge"
            >
              <q-icon
                name="chevron_right"
                size="xl"
                class="lms-scroll-horizontal-control__icon"
              />
            </q-btn>
          </template>
        </slot>
      </div>
    </template>
  </div>
</template>

<script>
import { scroll, dom } from "quasar";

const { width } = dom;

const {
  getHorizontalScrollPosition,
  setHorizontalScrollPosition,
  getScrollWidth
} = scroll;

const defaultScrollDelta = container => {
  if (!container || !container.children || !container.children[0]) return 100;
  return container.children[0].clientWidth;
};

export default {
  name: "LmsScrollHorizontal",
  props: {
    classes: { type: String, required: false, default: "" },
    scrollDelta: {
      type: Function,
      required: false,
      default: defaultScrollDelta
    },
    controls: { type: Boolean, required: false, default: false },
    controlsFloating: { type: Boolean, required: false, default: false },
    scrollDuration: { type: Number, required: false, default: 300 },
    scrollbar: { type: Boolean, required: false, default: false },
    draggable: { type: Boolean, required: false, default: false },
    edgeThreshold: { type: Number, required: false, default: 5 }
  },
  data() {
    return {
      x: 0,
      y: 0,
      containerWidth: 0,
      containerScrollWidth: 0
    };
  },
  computed: {
    rootClasses() {
      let result = [];

      if (this.controls) {
        result.push("lms-scroll-horizontal--controls row items-center");
      }

      if (this.controls && !this.controlsFloating) {
        result.push("lms-scroll-horizontal--controls-fixed");
      }

      if (this.controls && this.controlsFloating) {
        result.push("lms-scroll-horizontal--controls-floating");
      }

      if (this.draggable) result.push("lms-scroll-horizontal--draggable");

      if (this.scrollbar) {
        result.push("lms-scroll-horizontal--scrollbar");
      }

      return result;
    },
    wrapperClasses() {
      let result = [];
      if (this.controls && !this.controlsFloating) result.push("col");
      return result;
    },
    scrollClasses() {
      let result = [];
      return [...result, this.classes];
    },
    isLeftEdge() {
      return this.x <= this.edgeThreshold;
    },
    isRightEdge() {
      let max =
        this.containerScrollWidth - this.containerWidth - this.edgeThreshold;
      return this.x >= max;
    }
  },
  mounted() {
    this.updateContainerInfo();
  },
  methods: {
    handlePan(details) {
      if (!this.draggable) return;
      let el = this.$refs.container;
      let x = getHorizontalScrollPosition(el);

      // isFinal significa che si tratta della fine della gestione del pan
      // => è il momento giusto per fare eventuali aggiustamenti
      if (details.isFinal) {
        // Velocità px/ms
        let v = details.distance.x / details.duration;

        // Creiamo un delta proporzionale alla velocità di drag
        //
        // Se la velocità è bassa (< 1.5) non aggiungiamo nessun delta
        // In questo modo l'utente che ha scrollato parecchio e velocemente scorrerà più elementi
        // rispetto all'utente che ha scrollato poco anche se forte
        let delta = 0;
        if (v >= 2) delta = details.offset.x * v;

        x = x - delta;
        x = Math.max(0, x);
        x = Math.min(getScrollWidth(el), x);

        setHorizontalScrollPosition(el, x, this.scrollDuration);
        // console.log(details);
        // console.log({ v, delta });

        // // Aggiungiamo un'accelerazione al movimento
        // // basiamo l'accelerazione sulla forza usata dall'utente per scrollare
        // // cioè il rapporto tra distanza percorsa e durata dello scroll.
        // //
        // // Se l'utente percorre poca distanza in tanto tempo vuol dire che sta scrollando piano
        // // Se l'utente percorre molta distanza in poco tempo vuol dire che ha dato uno strappo allo scroll
        // // perché si vuole muovere velocemente
        // let a = Math.abs(details.offset.x) / Math.abs(details.duration);
        // a = Math.min(a, 7);
        // a = Math.max(a, 0);
        //
        // // Creiamo un delta a partire dall'accelerazione calcolata
        // // Se l'accelerazione è bassa (< 1.5) non aggiungiamo nessun delta
        // //
        // // Se invece l'accelerazione è elevata
        // // => il delta che aggiungiamo si basa sull'accelerazione e sulla distanza percorsa
        // //
        // // In questo modo l'utente che ha scrollato parecchio e velocemente scorrerà più elementi
        // // rispetto all'utente che ha scrollato poco anche se forte
        // let delta = a < 1.5 ? 0 : details.offset.x * a;
        // x = x - delta;
        // x = Math.max(0, x);
        // x = Math.min(getScrollWidth(el), x);
        //
        // let time = 300;
        // setHorizontalScrollPosition(el, x, time);
        // // console.log({ delta, a, time });
      } else {
        x = x - details.delta.x;
        x = Math.max(0, x);
        x = Math.min(getScrollWidth(el), x);
        setHorizontalScrollPosition(el, x);
      }
    },
    mutation() {
      this.updateContainerInfo();
    },
    onContainerResize() {
      this.updateContainerInfo();
    },
    updateContainerInfo() {
      this.containerWidth = width(this.$refs.container);
      this.containerScrollWidth = getScrollWidth(this.$refs.container);
    },
    scrolled(y, x) {
      this.y = y;
      this.x = x;
    },
    scrollLeft() {
      let el = this.$refs.container;
      let delta = -this.scrollDelta(el);

      let x = getHorizontalScrollPosition(el);
      x = Math.max(0, x + delta);
      setHorizontalScrollPosition(el, x, this.scrollDuration);
    },
    scrollRight() {
      let el = this.$refs.container;
      let delta = this.scrollDelta(el);

      let x = getHorizontalScrollPosition(el);
      x = Math.min(getScrollWidth(el), x + delta);
      setHorizontalScrollPosition(el, x, this.scrollDuration);
    }
  }
};
</script>

<style lang="sass">
// STILI SCROLL ORIZZONTALE
// ------------------------
.lms-scroll-horizontal--controls-floating
  position: relative

// STILI CONTROLLI
// ------------------
.lms-scroll-horizontal--controls-fixed .lms-scroll-horizontal-control--left
  padding-right: 8px

.lms-scroll-horizontal--controls-fixed .lms-scroll-horizontal-control--right
  padding-left: 8px

.lms-scroll-horizontal--controls-floating .lms-scroll-horizontal-control
  position: absolute
  top: 50%
  transform: translateY(-50%)
  z-index: 9

.lms-scroll-horizontal--controls-floating .lms-scroll-horizontal-control__button
  background-color: rgba(0, 0, 0, 0.2)

.lms-scroll-horizontal--controls-floating .lms-scroll-horizontal-control--left
  left: 20px

.lms-scroll-horizontal--controls-floating .lms-scroll-horizontal-control--right
  right: 20px

.lms-scroll-horizontal--controls-fixed .lms-scroll-horizontal-control__icon
  color: $primary
  font-weight: bold

.lms-scroll-horizontal--controls-floating .lms-scroll-horizontal-control__icon
  color: black

// STILI SCROLLER
// --------------
.lms-scroll-horizontal-scroller
  overflow-x: hidden

.lms-scroll-horizontal--scrollbar .lms-scroll-horizontal-scroller
  overflow-x: scroll

.lms-scroll-horizontal--draggable .lms-scroll-horizontal-scroller:hover
  cursor: grab

// STILI SCROLLBAR DELLO SCROLLER
.lms-scroll-horizontal-scroller
  scrollbar-width: thin
// scrollbar-color: $primary rgba(255, 255, 255, 0)

.lms-scroll-horizontal-scroller::-webkit-scrollbar-track
  background: $grey-3
  border-radius: 5px

.lms-scroll-horizontal-scroller::-webkit-scrollbar
  height: 12px

.lms-scroll-horizontal-scroller::-webkit-scrollbar-thumb
  background-color: $grey-7
  border-radius: 8px
  border: 3px solid $grey-2
</style>
