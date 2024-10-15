/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ModelRicercaBuono {
  constructor(
      public buonoCod: string,
      public buonoStatoCod: string,
      public numeroDomanda: string,
      public richiedenteCf: string,
      public destinatarioCf: string,
      public nomeRichiedente: string,
      public cognomeRichiedente: string,
      public nomeDestinatario: string,
      public cognomeDestinatario: string,
      public decorrenzaInizio: string,
      public decorrenzaFine: string,
      public dataDecesso: string,
      public dataCambioResidenza: string,
      public iseeConforme: string,
      public iseeVerificaConforme: string,
  ) {}
  checked: boolean;
}
