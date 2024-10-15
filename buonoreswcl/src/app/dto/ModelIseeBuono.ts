/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ModelIseeBuono {
  constructor(
    public isee_valore: number,
    public isee_data_rilascio: Date,
    public isee_scadenza: Date,
    public isee_verifica_esito_cod: string,
    public isee_verificato_in_data: Date,
    public isee_note: String,
    public isee_anno: String,
    public isee_conforme: boolean,
    public buono_id: number,
    public isee_id: number,
    public validita_inizio: string,
    public utente_creazione: String,
    public utente_modifica: String,
    public validita_fine: string
  ) {}
}
