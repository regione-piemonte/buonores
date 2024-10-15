/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ModelEnteGestore } from "./ModelEnteGestore";
import { ModelVerifiche } from "./ModelVerifiche";
import { StatiBuonores } from "./StatiBuonores";


export class DomandeAperte {
    constructor(
        public idDomanda: number,
        public numeroDomanda: string,
        public cfDestinatario: string,
        public nomeDestinatario: string,
        public cognomeDestinatario: string,
		    public cfRichiedente: string,
        public nomeRichiedente: string,
        public cognomeRichiedente: string,
        public dataDomanda: string,
        public stato: StatiBuonores,
        public enteGestore: ModelEnteGestore,
        public verifiche: ModelVerifiche,
        public dataVerificaBuono: string,
        public esitoVerificaBuono: boolean,
        public sportelloId: number,
        public sportelloCod: string,
        public sportelloDesc: string,
        public decorrenzaInizio: string,
        public decorrenzaFine: string,
        public dataDecesso: string,
        public dataCambioResidenza: string
    ){}
    checked: boolean;
}
