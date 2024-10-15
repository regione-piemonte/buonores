<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div
    class="lms-card-item-primary no-margin bg-grey-3 q-py-md"
    :class="{
      'row q-col-gutter-x-md items-center': $q.screen.lt.sm,
      'q-col-gutter-y-md full-height': $q.screen.gt.xs
    }"
  >
    <slot :is-mobile="$q.screen.lt.sm" :is-desktop="$q.screen.gt.xs">
      <div class="col-auto" :class="{ 'text-center': $q.screen.gt.xs }">
        <slot name="icon" :is-mobile="$q.screen.lt.sm" :is-desktop="$q.screen.gt.xs" />
      </div>

      <div class="col-auto" :class="{ 'text-center': $q.screen.gt.xs }">
        <slot name="text" :is-mobile="$q.screen.lt.sm" :is-desktop="$q.screen.gt.xs" />
      </div>
    </slot>
  </div>
</template>

<script>
export default {
  name: "LmsCardItemPrimary",
  data() {
    return {};
  }
};
</script>

<style lang="sass"></style>
