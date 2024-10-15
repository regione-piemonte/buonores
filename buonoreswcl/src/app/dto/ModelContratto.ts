/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ModelStruttura } from "./ModelStruttura";

export class ModelContratto {
  constructor(
    public id: number,
    public data_inizio: Date,
    public data_fine: Date,
    public tipo: String,
    public tipo_fornitore: String,
    public data_invio_a_bandi: Date,
    public fornitore: ModelStruttura,
  ) { }
}
