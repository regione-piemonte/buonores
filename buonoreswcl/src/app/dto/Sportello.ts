/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class Sportello {
    constructor(
        public codSportello: string,
        public descSportello: string,
        public dataInizio: string,
        public dataFine: string,
        public anno: string,
        public corrente: boolean
    ) {}
}
