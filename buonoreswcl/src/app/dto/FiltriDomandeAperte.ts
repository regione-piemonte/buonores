/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class FiltriDomandeAperte {
  constructor(
    public codSportello: string,
    public numeroDomanda: string,
    public statoDomanda: string,
    public destinatario: string,
    public richiedente: string,
    public menu: string,
    public entiGestori: number[],
    public statoVerificaEnteGestore: string,
    public verificaEnteGestore: string,
    public esitoVerificaEnteGestore: string,
    public dataVerificaDa: string,
    public dataVerificaA: string,
    public decessoResidenza: string
  ) { }
}

