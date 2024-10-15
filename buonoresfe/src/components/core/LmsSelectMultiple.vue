<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <div>
    <q-field
      :label="label"
      :value="value"
      :for="labelFor"
      :bottom-slots="bottomSlots"
      :loading="loading"
      :lazy-rules="lazyRules"
      :rules="rules"
      :required="required"
      :no-error-icon="noErrorIcon"
      :disable="disable"
      class="cursor-pointer lms-select-multiple-field"
      :aria-label="label"
      :filled="filled"
      :dense="dense"
      tabindex="0"
    >
      <template v-slot:append>
        <q-icon name="keyboard_arrow_down" class="cursor-pointer" />
      </template>

      <template v-slot:control>
        <div class="self-center full-width no-outline">
          <q-chip
            v-for="option in selectedOptions"
            :key="option[optionValue]"
            removable  @remove="onInput(option)"  >
            {{option[optionLabel]}}
          </q-chip>
        </div>
      </template>
      <q-menu
        fit
        square
        class="q-pa-md"
        role="combobox"
      >

        <q-list class="lms-select-multiple">

          <div   v-for="(option, index) in options"
                 :key="option[optionValue]"
                 class="lms-checkbox-multiple q-pa-sm"
                 tabindex="0"
                 @click="onInput(option)"
                 @keyup.space.enter.prevent.stop="onInput(option)"

          >

            <input
              :value="option"
              type="checkbox"
              :id="id+index"
              name="options"
              v-model="options[index]"
              @focus="onFocusCheckbox"
              @blur="onBlurCheckbox"
              ref="optionCheckbox"
              tabindex="0"
              @keydown.down.prevent.stop="onKeyDown(index, $event)"
              @keydown.up.prevent.stop="onKeyUp(index, $event)"

            />

            <label :for="id+index" class="hidden-label"> {{ option[optionLabel] }}</label>

            <div class="row q-col-gutter-sm items-center"  >
              <div class="col-auto">
                <template v-if="!isSelectedOption(option)">
                  <q-icon
                    name="check_box_outline_blank"
                    size="sm"
                    aria-hidden="true"

                  />
                </template>
                <template v-else>
                  <q-icon
                    name="check_box"
                    size="sm"
                    color="primary"
                    aria-hidden="true"

                  />
                </template>
              </div>

              <div class="col" aria-hidden="true">

                {{ option[optionLabel] }}

              </div>
            </div>
          </div>
        </q-list>
      </q-menu>


    </q-field>
  </div>
</template>

<script>

export default {
  name: "LmsSelectMultiple",
  components: {  },
  props: {
    value: { type: [Array,String, Number, Object], required: false, default: null },
    options: { type: Array, required: false, default: () => [] },
    optionValue: { type: String, required: false, default: "value" },
    optionLabel: { type: String, required: false, default: "label" },
    label: { type: String, required: false, default: "" },
    required: { type: Boolean, required: false, default: false },
    name: { type: String, required: false, default: null },
    labelFor: { type: String, required: false, default: null },
    id: { type: String, required: false, default: null },
    bottomSlots: { type: Boolean, required: false, default: false },
    loading: { type: Boolean, required: false, default: false },
    lazyRules: { type: [Boolean, String], required: false, default: false },
    rules: { type: Array, required: false, default: () => [] },
    noErrorIcon: { type: Boolean, required: false, default: false },
    disable: { type: Boolean, required: false, default: false },
    filled: { type: Boolean, required: false, default: false },
    dense: { type: Boolean, required: false, default: false },
    emitValue: { type: Boolean, required: false, default: false },
  },
  data() {
    return {
      isOptionsShown:false,
      selectedOptions:[],
      isOpenMenu:false
    };
  },
  computed: {


  },
  created() {
    if(this.emitValue)
       this.selectedOptions = this.options?.filter(option => this.value.includes(option[this.optionValue])  )
    else
      this.selectedOptions = this.value
  },
  methods: {
    onInput(value) {

      if(this.selectedOptions.includes(value)){
        let index = this.selectedOptions.indexOf(value);
        this.selectedOptions.splice(index, 1)
      }
      else
        this.selectedOptions.push( value)
      let values=null
      if(this.emitValue)
        values= this.selectedOptions.map( option =>  option[this.optionValue])
      else{
        values= this.selectedOptions
      }
      this.$emit('input', values)
    },

    showOptions(){

      this.isOpenMenu = !this.isOpenMenu
    },

    onKeyDown(index, event){
     // event.target?.nextElementSibling?.focus()
      this.$refs['optionCheckbox'][index+1]?.focus()
    },
    onKeyUp(index, event){
      // event.target?.previousElementSibling?.focus()
      this.$refs['optionCheckbox'][index-1]?.focus()
    },
    onKeyCheckbox(event){
      event.target.parentElement?.focus()
    },
    onBlurCheckbox( event){
      let parent = event.target.parentElement
      parent.classList.remove('checkbox-focused')
    },
    onFocusCheckbox( event){
      console.log('onFocusCheckbox',event)
      let parent = event.target.parentElement
      parent.classList.add('checkbox-focused')

    },
    getOptionLabel(option) {
      // console.log(this.options.find())
    },
    isSelectedOption(option){
      return this.selectedOptions.find(o=> o[this.optionValue] === option[this.optionValue])
    }
  }
};
</script>

<style lang="scss">
.lms-checkbox-multiple {
  cursor: pointer;
  &:hover {
    background-color:  rgba(0, 0, 0, 0.1);
  }
  input, .hidden-label {
    position: absolute;
    z-index: -1;
    opacity: 0;
  }

  label {
    cursor: pointer;
    user-select: none;
    vertical-align: middle;
  }

  //&:focus {
  //  outline: 2px solid darkorange;
  //}
}
.hol-icon:before{
  color: #fff;
  margin-top: 8px;
  content: url(https://tst-niv-sansol.isan.csi.it/statics/la-mia-salute/icone/check_box_selected.svg);
}

.checkbox-focused{
  background-color: rgba(0, 0, 0, 0.1);
}
</style>
