/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class FiltriRicercaBuoni {
  constructor(
      public statoBuono: string,
      public numeroBuono: string,
      public richiedente: string,
      public destinatario: string,
      public numeroDomanda: string,
      public decessoResidenza: string,
      public iseeConforme: string,
	    public iseeVerificaConforme: string
  ) { }
}
