import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ArchivioDomandeComponent } from './components/archivio-domande/archivio-domande.component';
import { GraduatoriaComponent } from './components/graduatoria/graduatoriacomponent';
import { IstanzaAperteComponent } from './components/istanze-aperte/istanze-aperte.component';
import { IstruttoriaComponent } from './components/istruttoria/istruttoria.component';
import { RendicontazioneOpComponent } from './components/rendicontazione-op/rendicontazione-op.component';

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'istanze-aperte'
        // redirectTo: 'gestione-utenti'
      },
      {
        path: 'istanze-aperte',
        component: IstanzaAperteComponent
      },
      {
        path: 'archivio-domande',
        component: ArchivioDomandeComponent
      },
      {
        path: 'istruttoria',
        component: IstruttoriaComponent
      },
      {
        path: 'graduatoria',
        component: GraduatoriaComponent
      },
      {
        path: 'rendicontazione-op',
        component: RendicontazioneOpComponent
      },
      // {
      //   path: 'archivio-graduatorie',
      //   component: ArchivioGraduatorieComponent
      // }
      //     {
      //       path: 'dati-ente',
      //       component: DatiEnteComponent
      //     },
      //     {
      //       path: 'gestione-utenti',
      //       component: ContainerGestioneUtentiComponent,
      //       children: [
      //         {
      //           path: '',
      //           pathMatch: 'full',
      //           redirectTo: 'utenti'
      //         },
      //         {
      //           path: 'utenti',
      //           component: UtentiComponent
      //         },
      //         {
      //           path: 'profili',
      //           component: ProfiliComponent,
      //         },
      //         {
      //           path: 'liste',
      //           component: ListeComponent
      //         }
      //       ]
      //     },
      //   {
      //       path: 'dati-ente-archivio',
      //       component: DatiEnteArchivioComponent
      //     },
      //     {
      //       path: 'dettaglio-prestazione',
      //       component: DettaglioPrestazioneComponent
      //     },
      // 	{
      //       path: 'dettaglio-prestazione-archivio',
      //       component: DettaglioPrestazioneArchivioComponent
      //     },
      //     {
      //       path: 'dati-rendicontazione',
      //       component: DatiRendicontazioneComponent,
      //       children: [
      //         {
      //           path: '',
      //           pathMatch: 'full',
      //           redirectTo: 'modello-a'
      //         },
      //         {
      //           path: 'modello-a',
      //           component: ModelloAComponent
      //         },
      //         {
      //           path: 'modello-a1',
      //           component: ModelloA1Component
      //         },
      //         {
      //           path: 'modello-a2',
      //           component: ModelloA2Component
      //         },
      //         {
      //           path: 'modello-d',
      //           component: ModelloDComponent
      //         },
      //         {
      //           path: 'macroaggregati',
      //           component: MacroaggregatiComponent
      //         },
      //         {
      //           path: 'modello-b',
      //           component: ModelloBComponent
      //         },
      //         {
      //           path: 'modello-b1',
      //           component: ModelloB1Component
      //         },
      //         {
      //           path: 'modello-e',
      //           component: ModelloEComponent
      //         },
      //         {
      //           path: 'modello-c',
      //           component: ModelloCComponent
      //         },
      //         {
      //           path: 'modello-f',
      //           component: ModelloFComponent
      //         },
      //       ]
      //     },
      //   {
      //           path: 'all-d',
      //           component: ModelloAllDComponent
      //     },
      //     {
      //       path: 'archivio-dati-rendicontazione',
      //       component: ArchivioDatiRendicontazioneComponent
      //     },
      //     {
      //       path: 'configuratore-prestazioni',
      //       component: ConfiguratorePrestazioniComponent,
      //     },
      //     {
      //       path: 'cruscotto',
      //       component: CruscottoComponent,
      //     },
      //     {
      //       path: 'nuova-prestazione',
      //       component: NuovaPrestazioneComponent
      //     },
      //  {
      //       path: 'dati-anagrafici-archivio',
      //       component: DatiAnagraficiArchivioComponent
      //     }


    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OperatoreBuonoRoutingModule { }
