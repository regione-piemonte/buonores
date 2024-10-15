<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

 <template>
  <div class="q-pt-md q-pb-md q-gutter-sm">
    <q-breadcrumbs>
      <q-breadcrumbs-el icon="home" to="/" />
      <q-breadcrumbs-el
        :label="name"
        :to="'/' + path1 + '/' + id"
        v-if="name"
      />
      <q-breadcrumbs-el
        :label="label1"
        :to="'/' + path1 + '/' + id"
        v-if="label1"
      />
      <q-breadcrumbs-el
        :label="label2"
        :to="'/' + path2 + '/' + id"
        v-if="label2"
      />
      <q-breadcrumbs-el
        :label="label3"
        :to="'/' + path3 + '/' + id"
        v-if="label3"
      />
    </q-breadcrumbs>
    <h2 v-if="title">{{ title }}</h2>
  </div>
</template>

<script>
export default {
  name: "BreadcrumbsCustom",
  components: {},
  props: {
    label1: { type: String },
    label2: { type: String },
    label3: { type: String },
    path1: { type: String },
    path2: { type: String },
    path3: { type: String },
    title: { type: String },
    name: { type: String },
    id: { type: String,  required: true }
  },
  data () {
    return {};
  }
};
</script>