import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DateAdapter, MAT_DATE_LOCALE, MatCardModule, MatRadioModule, MatTooltipModule } from '@angular/material';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { RouterModule } from '@angular/router';
import { CustomDateAdapter } from '@buonores-app/app/format-datepicker';
import { MaterialModule } from '@buonores-app/app/material.module';
import { ArchivioDomandeComponent } from '@buonores-operatore/components/archivio-domande/archivio-domande.component';
import { SharedComponentModule } from '@buonores-shared/shared-components.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { DataTablesModule } from 'angular-datatables';
import { ContainerOperatoreComponent } from './components/container-operatore.component';
import { GraduatoriaComponent } from './components/graduatoria/graduatoriacomponent';
import { IstanzaAperteComponent } from './components/istanze-aperte/istanze-aperte.component';
import { IstruttoriaComponent } from './components/istruttoria/istruttoria.component';
import { PopupCambistatoComponent } from './components/popup-cambistato/popup-cambistato.component';
import { PopupEnteComponent } from './components/popup-ente/popup-ente.component';
import { PopupNuovaGraduatoriaComponent } from './components/popup-nuova-graduatoria/popup-nuova-graduatoria';
import { PopupNuovoSportelloComponent } from './components/popup-nuovo-sportello/popup-nuovo-sportello';
import { PopuppopupRevocaBuonoComponent } from './components/popup-revoca-buono/popup-revoca-buono.component';
import { RendicontazioneOpComponent } from './components/rendicontazione-op/rendicontazione-op.component';
import { TabOperatoreComponent } from './components/tab-operatore/tab-operatore.component';
import { OperatoreBuonoRoutingModule } from './operatore-buono-routing.module';





@NgModule({
  declarations: [
    ContainerOperatoreComponent,
    TabOperatoreComponent,
    IstanzaAperteComponent,
    PopupCambistatoComponent,
    ArchivioDomandeComponent,
    IstruttoriaComponent,
    PopupEnteComponent,
    GraduatoriaComponent,
    PopupNuovaGraduatoriaComponent,
    PopupNuovoSportelloComponent,
    RendicontazioneOpComponent,
    PopuppopupRevocaBuonoComponent,
  ],
  imports: [
    CommonModule,
    OperatoreBuonoRoutingModule,
    NgbModule,
    FormsModule,
    ReactiveFormsModule,
    SharedComponentModule,
    DataTablesModule,
    MaterialModule,
    RouterModule,
    MatAutocompleteModule,
    MatTooltipModule,
    MatCardModule,
    MatRadioModule
  ],
  exports: [
    TabOperatoreComponent
  ],
  entryComponents: [
    PopupCambistatoComponent,
    PopupEnteComponent,
    PopupNuovaGraduatoriaComponent,
    PopupNuovoSportelloComponent,
    PopuppopupRevocaBuonoComponent
  ],
  providers: [
    { provide: MAT_DATE_LOCALE, useValue: 'en-GB' },
    { provide: DateAdapter, useClass: CustomDateAdapter }
  ],
})
export class OperatoreBuonoModule { }
