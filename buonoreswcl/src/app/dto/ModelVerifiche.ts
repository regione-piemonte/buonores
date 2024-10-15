/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ModelVerifiche {
  constructor(
    public verifica_eg_richiesta: boolean,
    public verifica_eg_in_corso: boolean,
    public verifica_eg_conclusa: boolean,
    public verifica_eg_punteggio_sociale: string,
    public verifica_eg_incompatibilita: boolean,
    public note_ente_gestore: string
  ) {}
}
