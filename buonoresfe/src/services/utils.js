/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { Notify, Dialog, uid } from "quasar"
import FseApiErrorDialog from "components/FseApiErrorDialog"
import router from "src/router"
import { APPLICATION_CODE, IS_DEV, IS_TEST } from "src/services/config"

export const ISO_DATE_FULL_YEAR = "YYYY"
export const ISO_DATE_MONTH = "MM"
export const ISO_DATE_MONTH_DAY = "DD"
export const ISO_TIME_HOUR = "HH"
export const ISO_TIME_MINUTE = "mm"
export const ISO_TIME_SECOND = "ss"
export const ISO_TIME_SEC_FRAC = "SSS"
export const ISO_TIME_OFFSET = "Z"

export const ISO_FULL_DATE = `${ISO_DATE_FULL_YEAR}-${ISO_DATE_MONTH}-${ISO_DATE_MONTH_DAY}`
export const ISO_PARTIAL_TIME = `${ISO_TIME_HOUR}:${ISO_TIME_MINUTE}:${ISO_TIME_SECOND}`
export const ISO_FULL_TIME = `${ISO_PARTIAL_TIME}${ISO_TIME_OFFSET}`
export const ISO_DATE_TIME = `${ISO_FULL_DATE}T${ISO_FULL_TIME}`

export const DEFAULT_FORMAT_DATE = "DD MMM YYYY"
export const DEFAULT_FORMAT_TIME = "HH:mm"
export const DEFAULT_FORMAT_DATETIME = "DD MMM YYYY HH:mm"
export const MOBILE_PHONE_PREFIX_ITALY = "0039"
export const DATETIME_MESSAGES_FORMAT = 'DD/MM/YYYY HH:mm'

// API UTILS
// ---------------------------------------------------------------------------------------------------------------------
/**
 * Estende il Notify di Quasar per mostrare all'utente un messaggio di errore.
 *
 * Di solito questo metodo viene usato per notificare errori nelle chiamate AJAX, per questo motivo di default
 * non svanisce mai e mostra un bottone di chiusura della notifica.
 *
 * Inoltre è possibile aggiungere facilmente un altro bottone di "riprova" con una callback relativa per riprovare
 * la chiamata.
 *
 * @param {Object} options
 */
export const apiErrorNotify = (options = {}) => {
  const noop = () => { }

  // Impostiamo alcuni defaults
  options.color = options.color || "negative"
  options.message = options.message || ""
  options.actions = options.actions || []
  options.timeout = options.timeout || 0
  if (!("closable" in options)) options.closable = true
  if (!("canTryAgain" in options)) options.canTryAgain = false
  if (!("progress" in options)) options.progress = true
  options.onClose = options.onClose || noop
  options.onTryAgain = options.onTryAgain || noop

  if (options.error) {
    let error = options.error
    let code = null

    if (!(error instanceof Error)) {
      code = error.status
    } else if (error.response) {
      code = error.response.status
      options.caption = error?.response?.data?.title
    }

    if (code) options.message = `[${code}] ${options.message}`
  }

  if (options.canTryAgain) {
    options.actions.push({
      label: "Riprova",
      color: "white",
      handler: options.onTryAgain
    })
  }

  if (options.closable) {
    options.actions.push({
      label: options.canTryAgain ? "Chiudi" : undefined,
      icon: options.canTryAgain ? undefined : "close",
      dense: !options.canTryAgain,
      round: !options.canTryAgain,
      color: "white",
      handler: options.onClose
    })
  }

  Notify.create(options)
}

export const apiErrorNotifyDialog = (options = {}) => {
  const noop = () => { }

  // Impostiamo alcuni defaults
  options.error = options.error || options.err || null
  options.component = options.component || FseApiErrorDialog
  options.parent = options.parent || this
  if (!("closable" in options)) options.closable = true
  if (!("canTryAgain" in options)) options.canTryAgain = false
  options.onClose = options.onClose || noop
  options.onTryAgain = options.onTryAgain || noop

  return Dialog.create(options)
}




export const apiInterceptorRequestId = http => {
  http.interceptors.request.use(config => {
    config.headers["X-Request-Id"] = uid() //
    config.headers["X-Codice-Servizio"] = "BUONORES" //
    return config
  })
}


export const login = (loginUrl, landingUrl = window.location.href) => {
  let encodedPublicUrl = encodeURIComponent(landingUrl)
  let loginRedirectUrl = `${loginUrl}?landingUrl=${encodedPublicUrl}`

  try {
    location.assign(loginRedirectUrl)
  } catch (e) {
    window.open(loginRedirectUrl, "_self")
  }
}

export const logout = logoutUrl => {
  try {
    location.assign(logoutUrl)
  } catch (e) {
    window.open(logoutUrl, "_self")
  }
}

export const orderBy = (arr, props, orders) => {
  return [...arr].sort((a, b) =>
    props.reduce((acc, prop, i) => {
      if (acc === 0) {
        const [p1, p2] =
          orders && orders[i] === "desc"
            ? [b[prop], a[prop]]
            : [a[prop], b[prop]]
        acc = p1 > p2 ? 1 : p1 < p2 ? -1 : 0
      }
      return acc
    }, 0)
  )
}

export const notifySuccess = message => {
  Notify.create({
    type: "positive",
    message
  })
}

export const notifyError = message => {
  Notify.create({
    color: "negative",
    message
  })
}

export const snackbarError = err => {
  let message =
    err.response && err.response.data.detail
      ? err.response.data.detail[0].valore
      : "Problemi server, contattare assistenza";
  Notify.create({
    type: "negative",
    message: message,
  });
}

export const uniqueElementsBy = (arr, fn) => {
  return arr.reduce((acc, v) => {
    if (!acc.some(x => fn(v, x))) acc.push(v)
    return acc
  }, [])
}

export const toBase64 = file =>
  new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })


//@see https://www.30secondsofcode.org/js/s/is-empty
export const isEmpty = val => val == null || !(Object.keys(val) || val).length

//@see https://www.30secondsofcode.org/js/s/deep-clone
export const deepClone = obj => {
  if (obj === null) return null
  let clone = Object.assign({}, obj)
  Object.keys(clone).forEach(
    key =>
    (clone[key] =
      typeof obj[key] === 'object' ? deepClone(obj[key]) : obj[key])
  )
  if (Array.isArray(obj)) {
    clone.length = obj.length
    return Array.from(clone)
  }
  return clone
}




