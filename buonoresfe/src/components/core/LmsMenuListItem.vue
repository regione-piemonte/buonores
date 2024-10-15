<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div>
    <q-item
      v-bind="attrs"
      v-on="listeners"
      class="lms-menu-list-item"
      :class="classes"
    >
      <template v-if="icon_">
        <q-item-section side avatar>
          <q-icon :name="icon_"/>
        </q-item-section>
      </template>

      <q-item-section>
        <template v-if="title">
          <q-item-label class="lms-menu-list-item__title">
            {{ title }}
          </q-item-label>
        </template>

        <template v-if="caption">
          <q-item-label class="lms-menu-list-item__caption">
            {{ caption }}
          </q-item-label>
        </template>
      </q-item-section>

      <template v-if="locked">
        <q-item-section side avatar>
          <q-icon class="lms-menu-list-item__icon-lock" name="lock"/>
        </q-item-section>
      </template>
    </q-item>
    <template v-if="isMultiLevelMenu && !locked">
      <q-list dense>
        <div
          class="row items-center lms-menu-list-item--child q-px-sm bg-white"
          v-for="menu in filteredMenuList"
          :key="menu.id"
        >
          <div class="col-2 lms-menu-list-item--child-item relative-position">
          </div>
          <q-item
            tag="a"
            :href="menu.url"
            v-bind="attrs"
            v-on="listeners"
            :class="classes"
            class="lms-menu-list-item col"
          >
            <q-item-section>
              <q-item-label class="lms-menu-list-item__title">
                {{ menu.descrizione }}
              </q-item-label>
            </q-item-section>
          </q-item>
        </div>

      </q-list>
    </template>
  </div>

</template>

<script>
import {orderBy} from "src/services/utils";

export default {
  name: "LmsMenuListItem",
  inheritAttrs: false,
  props: {
    icon: {type: String, required: false, default: null},
    title: {type: String, required: false, default: ""},
    caption: {type: String, required: false, default: ""},
    locked: {type: Boolean, required: false, default: false},
    menuList: {type: Array, required: false, default: null},
  },
  computed: {
    listeners() {
      const {...listeners} = this.$listeners;
      return listeners;
    },
    attrs() {
      const {...attrs} = this.$attrs;
      if (!("href" in attrs)) attrs.overlay = true;
      if (!("tag" in attrs)) attrs.tag = "a";
      if (!("target" in attrs)) attrs.target = "_self";
      if (!("clickable" in attrs)) attrs.clickable = true;
      if (!("active" in attrs)) attrs.active = false;
      if (!("activeClass" in attrs))
        attrs.activeClass = "lms-menu-list-item--active";
      return attrs;
    },
    icon_() {
      if (!this.icon || !this.icon.startsWith("http")) return this.icon;
      return `img:${this.icon}`;
    },
    classes() {
      return {
        "lms-menu-list-item--locked": this.locked,
        "lms-menu-list-item--root": this.isMultiLevelMenu && !this.locked
      };
    },
    isMultiLevelMenu() {
      return this.filteredMenuList?.length > 0
    },
    filteredMenuList() {
      let isMobile = this.$q.platform.is.mobile;

      let menuListItems = this.menuList.filter(m => {
        if (isMobile) return m.visibile_menu_mobile;
        return m.visibile_menu_desktop;
      });


      return orderBy(menuListItems, ["posizione"])
    }

  }
};
</script>

<style lang="sass">
.lms-menu-list-item__title
  color: $primary
  font-weight: bold

.lms-menu-list-item__icon
  color: $primary

.lms-menu-list-item__icon-lock
  color: $grey-7
  font-size: 18px !important

.lms-menu-list-item--active
  background-color: $blue-1

  &.lms-menu-list-item--locked
    background-color: $grey-2

.lms-menu-list-item--locked
  .lms-menu-list-item__title
    color: $grey-8

.lms-menu-list-item--root
  position: relative
.lms-menu-list-item--child
  .lms-menu-list-item--child-item
    height: 48px
    &:before
      content: ""
      position: absolute
      top: -2px
      bottom: 50%
      width: 50%
      left: calc(50% - 4px)
      border-left: 2px solid $primary
      border-bottom: 2px solid $primary
  &:not(:last-child)
    .lms-menu-list-item--child-item
      &:after
        content: ""
        position: absolute
        top: -2px
        bottom: 0
        width: 2px
        right: auto
        left:  calc(50% - 4px)
        border-left: 2px solid $primary
  .lms-menu-list-item--active
    background-color: #FFFFFF !important
</style>
