/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ModelParametriFinanziamento {
  constructor(
      public importoMensile: number,
      public mesi: number,
      public soggettiFinanziati: number,
      public importoTotale: number,
      public importoResiduo: number,
      public importoDistribuibile: number,
      public soggettiFinanziabili: number,
      public aree: string
  ) { }
}
