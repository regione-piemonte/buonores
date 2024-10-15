<!--
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 -->

<template>
  <footer v-bind="attrs" v-on="listeners" class="lms-layout-footer" id="footer">
    
      <div class="lms-layout-footer__help">
        <div class="container">
        <div class="row q-col-gutter-xl justify-center items-center">
          <div class="col-12 col-md-auto text-center lms-layout-footer__logo">
            <img
              src="../../../public/img/pre-footer/coesione-italia.svg"
              alt="Cofinanziato dal fondo coesione italia 21-27 Regione Piemonte"
            />
          </div>
          <div class="col-12 col-md-auto text-center lms-layout-footer__logo">
            <img
            src="../../../public/img/pre-footer/unione-europea.svg"
              alt="Cofinanziato dall'unione europea"
            />
          </div>
          <div class="col-12 col-md-auto text-center lms-layout-footer__logo">
            <img
              src="../../../public/img/pre-footer/ministro-interno.svg"
              alt="Ministro dell'Interno"
            />
          </div>
          <div class="col-12 col-md-auto text-center lms-layout-footer__logo">
            <img
              src="../../../public/img/pre-footer/regione-piemonte.svg"
              alt="Regione Piemonte"
            />
          </div>
        </div>
      </div>
      </div>

      <!-- LINKS -->
      <!-- ----------------------------------------------------------------------------------------------------------- -->
      <div class="q-py-lg">
        <div class="container">
          <div
            class="lms-layout-footer__links row justify-between items-center q-col-gutter-y-xl no-margin"
          >
          
            <div class="col-12 col-sm-auto">
              <a :href="urlPiemonteTu">
                <img src="../../../public/img/piemonte-tu.svg" alt="Logo Piemonte Tu" class="lms-layout-footer__logo-piemonteTu" />
              </a>
              <!--        <div class="text-h6 text-bold">-->
              <!--          Sistemapiemonte-->
              <!--        </div>-->
              <!--        <div class="text-caption">-->
              <!--          Un servizio a cura della regione Piemonte-->
              <!--        </div>-->
              <!--        <div class="text-caption">-->
              <!--          P.Iva 02843860012 - CF 80087670016-->
              <!--        </div>-->
            </div>

            <div class="col-12 col-sm-auto">
              <img
                src="../../../public/img/csiPiemonte.svg"
                alt="Logo CSI"
                class="lms-layout-footer__logo-csi"
              />
            </div>


          </div>
          <div class="q-px-sm q-py-lg">
            <q-separator dark  />
          </div>
          <div class="row justify-between lms-layout-footer__policy-links">
            <div class="col-auto col-lg-6">
              <ul class="">
                <li>
                  <a
                    :href="urls.privacyPolicy()"
                    target="_blank"
                    class="col-12 col-md-auto pis-footer__external-links q-py-sm">
                    Note legali e privacy
                  </a>
                </li>
                <li>
                  <a
                    :href="urls.cookiePolicy()"
                    target="_blank"
                    class="col-12 col-md-auto pis-footer__external-links q-py-sm">
                    Cookie policy
                  </a>
                </li>
              </ul>
              <!--            <a-->
              <!--              href="#"-->
              <!--              class="col-12 col-md-auto pis-footer__external-links q-py-sm"-->
              <!--              >Accessibilità</a-->
              <!--            >-->
            </div>
            <div class="col-auto col-lg-6 text-copyright">
              Un servizio a cura della Regione Piemonte <br/>
              <abbr title="Partita Iva">P.Iva</abbr> 02843860012 - <abbr title="codice fiscale">CF</abbr> 80087670016
            </div>
          </div>
          
      </div>
    </div>
    <!-- RATING -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <!-- <template v-if="rating">
      <div class="lms-layout-footer__rating row justify-center q-col-gutter-x-md">
        <div class="col-auto">
          <q-icon name="chat_bubble_outline" size="md" />
        </div>

        <div class="col-auto cursor-pointer" @click="$emit('click-rating')">
          <div class="text-h6 text-bold" style="line-height: 1">
            Valuta questo servizio
          </div>
          <div class="text-body1 q-mt-xs">
            Aiutaci a migliorare
          </div>
        </div>
      </div>
    </template> -->

    <!-- HELP -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->


  </footer>
</template>

<script>
import * as urls from "src/services/urls";
export default {
  name: "LmsLayoutFooter",
  inheritAttrs: false,
  props: {
    helpContacts: { type: Boolean, required: false, default: false },
    helpContactsLabel: { type: String, required: false, default: "Contatti" },
    helpFaq: { type: Boolean, required: false, default: false },
    helpFaqLabel: { type: String, required: false, default: "FAQ" }
  },
  data() {
    return {
      urls

    };
  },
  computed: {
    listeners() {
      const { ...listeners } = this.$listeners;
      return listeners;
    },
    attrs() {
      const { ...attrs } = this.$attrs;
      // if (!("elevated" in attrs)) attrs.elevated = true;
      return attrs;
    },
    user() {
      return this.$store.getters['getUser']
    },
    urlPiemonteTu() {
      return this.user
        ? 'https://www.piemontetu.it/api/auth/login?redirect=true'
        : 'https://www.piemontetu.it/';
    }
  }
};
</script>

<style lang="sass">
  .lms-layout-footer__rating
    background-color: $secondary
    color: #FFF
    padding: 32px 16px

  .lms-layout-footer__help
    padding: 3rem
    background-color: $primary

  .lms-layout-footer__links
    color: #FFF
    padding: 0 16px 32px 16px
    text-align: center

  .lms-layout-footer__policy-links
    color: #FFF
    padding: 8px 16px
    a
      color: #ffffff
      text-decoration: none

  .lms-layout-footer__help__links
    color: #FFFFFF
    text-align: center
</style>
