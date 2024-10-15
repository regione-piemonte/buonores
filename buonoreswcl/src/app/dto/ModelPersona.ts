/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ModelPersona {
  constructor(
    public cf: string,
    public cognome: string,
    public nome: string,
    public comune_nascita: string,
    public comune_residenza: string,
    public data_decesso: string,
    public data_nascita: string,
    public indirizzo_residenza: string,
    public provincia_nascita: string,
    public provincia_residenza: string,
    public stato_nascita: string
  ) {}
}
