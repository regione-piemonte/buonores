/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class BuonoresError {
    constructor(
        public errorDesc: string,
        public message: string,
        public name: string,
        public status: string,
        public statusText: string,
        public url: string,
        public date: Date,
    ) { }

    static toBuonoresError(json: any, errorDesc = "Si è verificato un errore nell'applicativo.") {
	if (json.error.detail!=null){
		errorDesc = json.error.detail;
	}
        return new BuonoresError(
            json.error.detail[0].valore || errorDesc,
            json.message || '',
            json.name || '',
            json.status || '',
            json.statusText,
            json.url,
            new Date()
        );
    }
}
