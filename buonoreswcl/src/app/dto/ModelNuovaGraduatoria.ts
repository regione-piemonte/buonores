/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ModelNuovaGraduatoria {
  constructor(
    public codSportello: string,
    public descrizione: string,
    public importoGenerale: number,
    public importoA1: number,
    public importoA2: number,
    public importoMensile: number,
    public numeroMesi: number
  ) { }
}
