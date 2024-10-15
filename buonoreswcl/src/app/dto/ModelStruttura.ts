/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ModelStrutturaSedeLegale } from './ModelStrutturaSedeLegale';

export class ModelStruttura {
  constructor(
    public id: string,
    public id_arpe: string,
    public nome: string,
    public indirizzo: string,
    public comune: string,
    public provincia: string,
    public piva: string,
    public sede_legale: ModelStrutturaSedeLegale
  ) {}
}
