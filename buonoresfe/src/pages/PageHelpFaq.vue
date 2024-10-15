<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div class="q-mt-lg q-gutter-y-md">
    <lms-faq-list-item
      v-for="(faq, index) in faqList"
      :key="faq.title"
      :title="faq.title"
      :number="index + 1"
    >
      <div class="text-body1" v-html="faq.text"></div>
    </lms-faq-list-item>
  </div>
</template>

<script>
import LmsFaqListItem from "src/components/core/LmsFaqListItem";

const FAQ_LIST = [
  { title: `Prima`, text: `testo random 1` },
  { title: `Seconda`, text: `testo random 2` },
  { title: `Terza`, text: `testo random 3` },
  { title: `Quarta`, text: `testo random 4` },
  { title: `Quinta`, text: `testo random 5` },
  { title: `Sesta`, text: `testo random 6` },
  { title: `Settima`, text: `testo random 7` },
  { title: `Ottava`, text: `testo random 8` },
  { title: `Nona`, text: `testo random 9` },
  { title: `Decima`, text: `testo random 10` }
];

export default {
  name: "PageHelpFaq",
  components: { LmsFaqListItem },
  props: {},
  data() {
    return {
      faqList: FAQ_LIST
    };
  },
  computed: {},
  created() {},
  methods: {}
};
</script>

<style scoped lang="sass"></style>
