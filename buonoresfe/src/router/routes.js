/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */


// PAGINE VERTICALE
import store from "src/store"
import {
  getSportelli
} from "src/services/api"
const bffPath = 'buonoresbff/api/v1'


async function check (to, from, next) {
  if (!store.state.sportelli) {
      next('/')
  } else {
    next()
  }
}

export const PERSONAL_AREA = {
  path: "",
  name: "personalarea",
  component: () => import("pages/PagePersonalArea")
}


export const BONUS_NEW = {
  path: "/richiesta",
  name: "richiesta",
  beforeEnter: check,
  component: () => import("pages/PageVoucherDom.vue")
}

export const BONUS_EDIT = {
  path: "/richiestaedit/:id",
  name: "richiestaedit",
  beforeEnter: check,
  component: () => import("pages/PageVoucherDom.vue")
}


export const RECTIFICATION = {
  path: "/rettifica/:id",
  name: "rettifica",
  beforeEnter: check,
  component: () => import("pages/PageRectification.vue")
}

export const COUNTER_DEDUCTION = {
  path: "/controdeduzione/:id",
  name: "controdeduzione",
  beforeEnter: check,
  component: () => import("pages/PageCounterDeduction.vue")
}

export const REFINEMENT = {
  path: "/perfezionamento/:id",
  name: "perfezionamento",
  beforeEnter: check,
  component: () => import("pages/PageRefinement.vue")
}

export const MANAGEMENT = {
  path: "/gestione/:id",
  name: "gestione",
  beforeEnter: check,
  component: () => import("pages/PageManagement.vue")
}

export const VOUCHERDATA = {
  path: "/daticontratto/:id",
  name: "daticontratto",
  beforeEnter: check,
  component: () => import("pages/PageContractData.vue")
}

export const NEWCONTRACT = {
  path: "/nuovocontratto/:id",
  name: "nuovocontratto",
  beforeEnter: check,
  component: () => import("src/pages/PageContractDataNewContract.vue")
}

// AIUTO

export const HELP_CONTACTS = {
  path: "contatti",
  name: "helpContacts",
  component: () => import("pages/PageHelpContacts.vue")
}


export const SERVICE_RATING = {
  path: "valutazione",
  name: "serviceRating",
  component: () => import("pages/PageServiceRating.vue")
}

// NOT FOUND

export const ERROR_404 = {
  path: "*",
  name: "error404",
  component: () => import("pages/PageError404.vue")
}


// QUASAR LO CREA AUTOMATICAMENTE E DENTRO CI SONO HEADER, FOOTER E ROUTER VIEW
export const LAYOUT_APP = {
  path: "/",
  component: () => import("layouts/LayoutApp.vue"),
  children: [
    PERSONAL_AREA,
    HELP_CONTACTS,
    SERVICE_RATING,
    BONUS_NEW,
    BONUS_EDIT,
    RECTIFICATION,
    COUNTER_DEDUCTION,
    REFINEMENT,
    MANAGEMENT,
    VOUCHERDATA,
    NEWCONTRACT
  ]
}


const routes = [LAYOUT_APP]

// Always leave this as last one
if (process.env.MODE !== "ssr") {
  routes.push(ERROR_404)
}

export default routes
