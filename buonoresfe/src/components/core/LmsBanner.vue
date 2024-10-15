<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <q-banner v-bind="attrs" v-on="listeners" :class="classesAlert" class="lms-banner q-my-lg">
    <div class="row q-pa-xs q-mt-md items-start">
      <div class="col-auto q-pr-md classesIcon">
        <img :src='"../../../public/icone/alert/" + type + ".svg"' alt=""/>
      </div>
      <div class="text-body1 col">
        <slot/>
      </div>

    </div>


  </q-banner>
</template>

<script>

const VALID_TYPES = ['info', 'warning', 'positive', 'negative']
export default {
  name: "LmsBanner",
  props: {
    type: {type: String, default: ''}
  },
  computed: {
    attrs() {
      const {...attrs} = this.$attrs;
      return attrs;
    },
    listeners() {
      const {...listeners} = this.$listeners;
      return listeners;
    },
    classesAlert() {
      let result = [];
      if (this.type) result.push(`lms-banner--${this.type}`);
      return result
    }

  }
}
</script>