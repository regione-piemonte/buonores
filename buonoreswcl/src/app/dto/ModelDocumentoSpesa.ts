/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ModelDocumentoSpesaDettaglio } from "./ModelDocumentoSpesaDettaglio";

export class ModelDocumentoSpesa {
  constructor(
    public idDocSpesa: Number,
    public doc_tipo: String,
    public doc_numero: String,
    public periodoInizio: Date,
    public periodoFine: Date,
    public rsaCod: String ,
    public rsaCodArpe: String ,
    public rsaPIva: String ,
    public rsaNome: String ,
    public rsaIBAN: String ,
    public fascia: String ,
    public flagPrimoIngresso:boolean,
    public movUscitaData:Date,
    public movUscitaMotivazione: String ,
    public note: String ,
    public documentiSpesaDettaglio: ModelDocumentoSpesaDettaglio[]
  ) { }
}
