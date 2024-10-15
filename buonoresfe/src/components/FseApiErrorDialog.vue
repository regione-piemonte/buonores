<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <q-dialog
    ref="dialog"
    position="bottom"
    seamless
    class="fse-api-error-dialog"
    @hide="onDialogHide"
  >
    <q-card
      class="bg-negative text-white fse-api-error-dialog-content"
      style="width: 800px"
    >
      <q-card-section class="q-pa-none">
        <!-- ERRORE SENZA DETTAGLI -->
        <!-- ------------------------------------------------------------------------------------------------------- -->
        <template v-if="!hasDetailList">
          <q-item>
            <q-item-section>
              [{{ errorCode }}] {{ errorTitle }}
              <template v-if="xRequestId">
                <div class="q-mt-xs text-caption">
                  ID richiesta: {{ xRequestId }}
                </div>
              </template>
            </q-item-section>

            <template v-if="closable">
              <q-item-section side>
                <q-btn
                  round
                  flat
                  dense
                  icon="close"
                  aria-label="chiudi finestra"
                  color="white"
                  @click="onCloseClick"
                />
              </q-item-section>
            </template>
          </q-item>
        </template>

        <!-- ERRORE CON DETTAGLI -->
        <!-- ------------------------------------------------------------------------------------------------------- -->
        <template v-else>
          <q-expansion-item default-opened expand-icon-class="text-white">
            <template #header>
              <q-item-section>
                [{{ errorCode }}] {{ errorTitle }}
                <template v-if="xRequestId">
                  <div class="q-mt-xs text-caption">
                    ID richiesta: {{ xRequestId }}
                  </div>
                </template>
              </q-item-section>

              <template v-if="closable">
                <q-item-section side>
                  <q-btn
                    round
                    flat
                    dense
                    icon="close"
                    aria-label="chiudi finestra"
                    color="white"
                    @click="onCloseClick"
                  />
                </q-item-section>
              </template>
            </template>

            <div class="q-px-md bg-red-5">
              <q-list class="q-px-none">
                <q-item
                  v-for="detail in errorDetailList"
                  :key="detail.chiave"
                  class="q-px-none text-caption"
                >
                  <q-item-section>
                    [{{ detail.chiave }}] {{ detail.valore }}
                  </q-item-section>
                </q-item>
              </q-list>
            </div>
          </q-expansion-item>
        </template>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script>
const noop = () => {};

export default {
  name: "FseApiErrorDialog",
  props: {
    error: { type: Error, required: false, default: () => null },
    closable: { type: Boolean, required: false, default: false },
    onClose: { type: Function, required: false, default: noop },
    canTryAgain: { type: Boolean, required: false, default: false },
    onTryAgain: { type: Function, required: false, default: noop }
  },
  data() {
    return {
      progressValue: 0
    };
  },
  computed: {
    errorStatusCode() {
      let code = null;

      if (!(this.error instanceof Error)) {
        code = this.error?.status;
      } else if (this.error?.response) {
        code = this.error?.response?.status;
      }

      return code;
    },
    errorCode() {
      return this.error?.response?.data?.code;
    },
    errorTitle() {
      return this.error?.response?.data?.title;
    },
    errorDetailList() {
      return this.error?.response?.data?.detail ?? [];
    },
    hasDetailList() {
      return this.errorDetailList.length > 0;
    },
    xRequestId() {
      return this.error?.config?.headers?.["X-Request-Id"];
    }
  },
  created() {},
  beforeDestroy() {
    console.log("beforeDestroy");
    // destroy the vue listeners, etc
    // this.$destroy();
    // remove the element from the DOM
    this.$el.parentNode.removeChild(this.$el);
  },
  methods: {
    onCloseClick() {
      this.onClose();
      this.onCancelClick();
    },
    // following method is REQUIRED
    // (don't change its name --> "show")
    show() {
      this.$refs.dialog.show();
    },

    // following method is REQUIRED
    // (don't change its name --> "hide")
    hide() {
      this.$refs.dialog.hide();
    },

    onDialogHide() {
      // required to be emitted
      // when QDialog emits "hide" event
      this.$emit("hide");
    },

    onOKClick() {
      // on OK, it is REQUIRED to
      // emit "ok" event (with optional payload)
      // before hiding the QDialog
      this.$emit("ok");
      // or with payload: this.$emit('ok', { ... })

      // then hiding dialog
      this.hide();
    },

    onCancelClick() {
      // we just need to hide dialog
      this.hide();
    }
  }
};
</script>

<style lang="scss">
.fse-api-error-dialog .q-dialog__inner {
  bottom: 16px;
}
</style>
