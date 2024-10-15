<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div class="lms-policy">
    <div class="relative-position">
      <template v-if="useIframe">
        <iframe
          :src="src"
          frameborder="0"
          class="lms-policy__frame"
          :class="iframeClasses"
          :style="iframeStyles"
          @load="stopLoading"
        >
        </iframe>
      </template>

      <template v-else>
        <div
          class="lms-policy__frame scroll q-px-md"
          :class="iframeClasses"
          :style="iframeStyles"
        >
          <slot />
        </div>
      </template>

      <!-- LOADING -->
      <!-- --------------------------------------------------------------------------------------------------------- -->
      <lms-inner-loading :showing="isLoading" class="bg-transparent" />
    </div>

    <!-- DOWNLOAD PDF -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <div v-if="srcPdf" class="q-pa-sm row items-center justify-end gutter-x-xs">
      <div class="col-auto">
        <q-icon name="picture_as_pdf" />
      </div>
      <div class="col-auto">
        <a :href="srcPdf" target="_blank">
          Scarica PDF
        </a>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "LmsPolicy",
  props: {
    src: { type: String, required: false, default: "" },
    srcPdf: { type: String, required: false, default: "" },
    iframeClasses: { type: Object, required: false, default: () => ({}) },
    iframeStyles: { type: Object, required: false, default: () => ({}) }
  },
  data() {
    return {
      isLoading: false
    };
  },
  computed: {
    useIframe() {
      return !this.$slots.default;
    }
  },
  created() {},
  methods: {
    stopLoading() {
      this.isLoading = false;
    }
  }
};
</script>

<style lang="sass">
.lms-policy__frame
  background-color: $grey-3
  width: 100%
  height: 50vh
  border: 1px solid $grey-5
  border-radius: 4px
</style>
