/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ModelIsee {
  constructor(
    public isee_valore: number,
    public isee_data_rilascio: Date,
    public isee_scadenza: Date,
    public isee_verificato_conforme: boolean,
    public isee_verificato_in_data: Date
  ) {}
}
