/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ModelAllegatoBuono {
  constructor(
    public allegatoFilename: string,
    public allegatoId: Number,
    public allegatoTipoCod:String,
    public allegatoTipoDesc:String,
    public allegatoTipo: string

  ) { }
}
