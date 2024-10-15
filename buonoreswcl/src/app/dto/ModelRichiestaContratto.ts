/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ModelStruttura } from "./ModelStruttura";

export class ModelRichiestaContratto {
	constructor(
		public tipo: string,
		public data_inizio: Date,
		public struttura: ModelStruttura,
		public titolo_privato: boolean,
		public incompatibilita_per_contratto: boolean
	) { }
}
