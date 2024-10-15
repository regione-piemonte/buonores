/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ModelCriteriGraduatoria {
    constructor(
        public ordinamentoId: number,
        public ordinamentoCriterio: string,
        public ordinamentoTabella: string,
        public ordinamentoCampo: string,
        public ordinamentoPosizione: number,
        public ordinamentoDirezione: string
    ) { }
}