/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { Sportello } from "./Sportello";

export class Sportelli {
    constructor(
        public sportelli: Sportello[],
        public sportelloCorrente: Sportello,
    ) {}
}