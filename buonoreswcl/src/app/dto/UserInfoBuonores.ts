/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ModelEnteGestore } from "./ModelEnteGestore";
import { ProfiloBuonores } from "./ProfiloBuonores";
import { RuoloBuonores } from "./RuoloBuonores";


export class UserInfoBuonores {
    constructor(
        public codFisc: string,
        public cognome: string,
        public nome: string,
		public listaRuoli: RuoloBuonores[],
    public listaEntiGestore: ModelEnteGestore[]
    ){}
}
