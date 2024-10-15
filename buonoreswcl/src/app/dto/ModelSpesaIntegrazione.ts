/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ModelAllegatoBuono } from "./ModelAllegatoBuono";

export class ModelSpesaIntegrazione {
    constructor(
        public dicSpesaIntegrazioneId: number,
        public dicSpesaIntegrazioneData: String,
        public dicSpesaIntegrazioneNote: String,
        public dicSpesaIntegrazioneCode: String,
        public periodoInizio: String,
        public periodoFine: String,
        public dicSpesaId: number,
        public allegati: ModelAllegatoBuono[]
    ) { }
}
