/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { AzioneBuonores } from "./AzioneBuonores";
import { ListaBuonores } from "./ListaBuonores";
import { ProfiloBuonores } from "./ProfiloBuonores";

export class RuoloBuonores {
    constructor(
        public codRuolo: string,
        public descRuolo: string,
		public listaProfili: ProfiloBuonores[],
    ) {}
}
