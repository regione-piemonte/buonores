/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ModelVerificheEnte {
  constructor(
    public presenzaAltreIncompatibilita: boolean,
    public noteEnte: String,
    public dataVerifica: Date
  ) { }
}
