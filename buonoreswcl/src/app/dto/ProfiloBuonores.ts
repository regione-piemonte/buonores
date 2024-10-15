/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { AzioneBuonores } from "./AzioneBuonores";
import { ListaBuonores } from "./ListaBuonores";

export class ProfiloBuonores {
    constructor(
        public codProfilo: string,
        public descProfilo: string,
		public listaAzioni: AzioneBuonores[]
    ) {}
}
