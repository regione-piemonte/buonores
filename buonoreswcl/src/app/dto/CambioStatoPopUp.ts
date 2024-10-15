/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ModelDatiDaModificare } from "./ModelDatiDaModificare";

export class CambioStatoPopUp {
    public numerodomanda: string;
    public notaCittadino: string;
	public notaEnte : string;
    public notaInterna: string;
	public datidamodificare: ModelDatiDaModificare [] = [];
    public constructor() {}
}