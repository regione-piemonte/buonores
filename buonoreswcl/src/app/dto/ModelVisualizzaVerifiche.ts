/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ModelVisualizzaVerifiche {
    constructor(
        public numeroDomanda: string,
        public tipo: string,
        public dataInizioValidita: Date,
        public misure: string,
        public fonte: string,
        public note: string,
        public noteRichiesta: string,
        public dataRichiesta: string
    ) {}
}
