/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RedirectPageComponent } from '@buonores-shared/redirect-page/redirect-page.component';
import { ContainerOperatoreComponent } from '@buonores-operatore/components/container-operatore.component';
import { SelezioneProfiloApplicativoComponent } from './pages/selezione-profilo-applicativo/selezione-profilo-applicativo.component';
import { DatiIstanzaComponent } from '@buonores-operatore/components/dati-istanza/dati-istanza.component';
import { ArchivioDomandeComponent } from '../../projects/operatore-buono/components/archivio-domande/archivio-domande.component';
import { DettaglioArchivioComponent } from '@buonores-operatore/components/dettaglio-archivio/dettaglio-archivio.component';
import { DatiIstanzaEnteComponent } from '@buonores-operatore/components/dati-istanza-ente/dati-istanza-ente.component';
import { BuonoDettaglioComponent } from '@buonores-operatore/components/buono-dettaglio/buono-dettaglio.component';
import { StatoAvanzamentoIstruttoriaComponent } from '@buonores-operatore/components/stato-avanzamento-istruttoria/stato-avanzamento-istruttoria.component';


const routes: Routes = [
    {
        path: '',
        children: [
            {
                path: 'operatore-buono',
                component: ContainerOperatoreComponent,
                loadChildren: () => import('@buonores-operatore/operatore-buono.module').then(m => m.OperatoreBuonoModule),
            },
            {
                path: 'redirect-page',
                component: RedirectPageComponent
            },
			      {
                path: 'selezione-profilo-applicativo',
                component: SelezioneProfiloApplicativoComponent
            },
            {
                path: 'dati-istanza',
                component: DatiIstanzaComponent
            },
 			      {
                path: 'dati-istanza-ente',
                component: DatiIstanzaEnteComponent
            },
            {
                path: 'dettaglio-archivio',
                component: DettaglioArchivioComponent
            },
            {
              path: 'buono-dettaglio',
              component: BuonoDettaglioComponent
            },
            {
              path: 'stato-avanzamento-istruttoria',
              component: StatoAvanzamentoIstruttoriaComponent
             }

        ]
    }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
