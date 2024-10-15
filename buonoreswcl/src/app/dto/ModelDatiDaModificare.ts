/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ModelDatiDaModificare {
    constructor(
        public codice: string,
        public descizione: string,
		public id:number,
		public nome_tabella:string
    ) {}
}
