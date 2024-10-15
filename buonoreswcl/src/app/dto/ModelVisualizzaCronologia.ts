/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ModelVisualizzaCronologia {
    constructor(
        public numeroDomanda: string,
        public statoDomanda: string,
        public note: string,
        public notaInterna: string,
        public utente: string,
        public dataInizioValidita: Date,
        public isRichiedente: boolean,
        public notaEnte: string
    ) {}
}