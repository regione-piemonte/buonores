/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ModelAllegato } from './ModelAllegato';
import { ModelAteco } from './ModelAteco';
import { ModelContatti } from './ModelContatti';
import { ModelDomicilio } from './ModelDomicilio';
import { ModelIsee } from './ModelIsee';
import { ModelPersona } from './ModelPersona';
import { ModelProtocollo } from './ModelProtocollo';
import { ModelRichiestaContratto } from './ModelRichiestaContratto';
import { ModelVerifiche } from './ModelVerifiche';

export class ModelRichiesta {
  constructor(
    public protocollo: ModelProtocollo,
    public numero: string,
    public stato: string,
    public data_aggiornamento: Date,
    public note: string,
    public nota_interna: string,
    public note_richiedente: string,
    public studio_destinatario: string,
    public studio_destinatario_desc: string,
    public asl_destinatario: string,
    public asl_destinatario_desc: string,
    public delega_cod: string,
    public delega_desc: string,
    public contributo_tipo_desc: string,
    public domanda_stato_desc: string,
    public area_desc: string,
    public valutazione_multidimensionale: string,
    public richiedente: ModelPersona,
    public destinatario: ModelPersona,
    public domicilio_destinatario: ModelDomicilio,
    public isee: ModelIsee,
    public verifiche: ModelVerifiche,
    public allegati: Array<ModelAllegato>,
    public contratto: ModelRichiestaContratto,
    public rettificare: string[],
    public lavoro_destinatario: string,
    public attestazione_isee: boolean,
    public nessuna_incompatibilita: boolean,
    public punteggio_bisogno_sociale: number,
    public contatto: ModelContatti,
    public domanda_det_id: number,
    public dataDecesso: Date,
    public dataResidenzaExtraregione: Date
  ) {}
}
