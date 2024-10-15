/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ModelVerifiche } from "./ModelVerifiche";
// MODIFICHE PROD 1.4.0-002
export class ModelDomandeGraduatoria {
    constructor(
        public posizioneGraduatoria: number,
        public numeroDomanda: string,
        // public nomeDestinatario: string,
        // public cognomeDestinatario: string,
        // public cfDestinatario: string,
        public destinatarioNome: string,
        public destinatarioCognome: string,
        public destinatarioCF: string,
        public punteggioSociale: number,
        // public isee: number | string,
        public isee: number,
        public dataInvioDomanda: string,
        public statoDomanda: string,
        // public areaInterna: string,
        public importoTotale: number,
        public areaInterna: string,
        public verifiche: ModelVerifiche
    ) { }
    checked: boolean;
}
