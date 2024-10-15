/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class  ModelPersonaSintesi {
  constructor(
    public cf: string,
    public nome: string,
    public cognome: string,
    public data_nascita: Date,
    public stato_nascita: string,
    public comune_nascita: string,
    public provincia_nascita: string,
  ) { }
}
