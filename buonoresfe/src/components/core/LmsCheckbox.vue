<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <q-item
    class="lms-checkbox-multiple"
    tabindex="0"
    clickable
    tag="label"
    @keyup.space.enter.prevent.stop="onKeyEnter"
    @keydown.down.prevent.stop="onKeyDown"
    @keydown.up.prevent.stop="onKeyUp"
  >
    <input
      :value="optionValue"
      type="checkbox"
      :id="id"
      :name="name"
      @input="onInput"
      v-model="selectedCheckbox"

  @keydown.down.up.prevent.stop="onKeyCheckbox"
    />

    <div class="row q-col-gutter-sm items-center">
      <div class="col-auto">
        <template v-if="!selectedCheckbox">
          <q-icon
            name="check_box_outline_blank"
            :size="size"
            aria-hidden="true"
          />
        </template>
        <template v-else>
          <q-icon
            name="check_box"
            :size="size"
            :color="color"
            aria-hidden="true"
          />
        </template>
      </div>

      <div class="col">
        <label :for="id">
          <slot>
            {{ label }}
          </slot>
        </label>
      </div>
    </div>
  </q-item>
<!--     @keyup.space.enter.prevent.stop="onKeyEnter"
    @keydown.down.prevent.stop="onKeyDown"
    @keydown.up.prevent.stop="onKeyUp"-->
</template>

<script>
export default {
  name: "LmsCheckboxMultiple",
  props: {
    optionValue: { type: [String, Boolean], required: false, default: null },
    label: { type: String, required: false, default: null },
    id: { type: String, required: false, default: null },
    name: { type: String, required: false, default: null },
    size: { type: String, required: false, default: "xs" },
    color: { type: String, required: false, default: "primary" }
  },
  data() {
    return {
      selectedCheckbox:null
    };
  },
  computed: {},
  created() {},
  methods: {
    onInput(event) {

      let val = !this.selectedCheckbox ? event?.target?.value : null
      this.$emit("input",val, event);
    },

    onKeyEnter(event) {
      this.onInput(event);
      // this.$emit("input", !this.value, event);
    },
    onKeyDown(event){
      event.target?.nextElementSibling?.focus()
    },
    onKeyUp(event){
      event.target?.previousElementSibling?.focus()
    },
    onKeyCheckbox(event){
      event.target.parentElement?.focus()
    },

  }
};
</script>

<style lang="scss">
.lms-checkbox {
  cursor: pointer;

  input {
    position: absolute;
    z-index: -1;
    opacity: 0;
  }

  label {
    cursor: pointer;
    user-select: none;
    vertical-align: middle;
  }

  &:focus {
    outline: 2px solid darkorange;
  }
}
</style>
