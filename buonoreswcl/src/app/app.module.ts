/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { DateAdapter, MAT_DATE_LOCALE } from '@angular/material/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule, Routes } from "@angular/router";
import { BuonoresBOClient } from "@buonores-app/app/BuonoresBOClient";
import { MomentDateFormatter } from "@buonores-app/app/MomentDateFormatter";
import { RequestInterceptor } from '@buonores-app/interceptors/request-interceptor';
import { CustomDialogComponent } from '@buonores-app/shared/custom-dialog/custom-dialog.component';
import { TwoDigitDecimalNumberDirective } from '@buonores-app/shared/directive/two-digit-decimal-number.directive';
import { BuonoresErrorService } from '@buonores-app/shared/error/buonores-error.service';
import { FeedbackComponent } from '@buonores-app/shared/feedback-component/feedback.component';
import { BuonoDettaglioComponent } from '@buonores-operatore/components/buono-dettaglio/buono-dettaglio.component';
import { CronologiaComponent } from '@buonores-operatore/components/cronologia/cronologia.component';
import { DatiIstanzaEnteComponent } from '@buonores-operatore/components/dati-istanza-ente/dati-istanza-ente.component';
import { DatiIstanzaComponent } from '@buonores-operatore/components/dati-istanza/dati-istanza.component';
import { DettaglioArchivioComponent } from '@buonores-operatore/components/dettaglio-archivio/dettaglio-archivio.component';
import { StatoAvanzamentoIstruttoriaComponent } from '@buonores-operatore/components/stato-avanzamento-istruttoria/stato-avanzamento-istruttoria.component';
import { VisualizzaCronologiaComponent } from '@buonores-operatore/components/visualizza-cronologia/visualizza-cronologia.component';
import { VisualizzaVerificheComponent } from '@buonores-operatore/components/visualizza-verifiche/visualizza-verifiche.component';
import { RedirectPageComponent } from '@buonores-shared/redirect-page/redirect-page.component';
import { ToastsContainerComponent } from '@buonores-shared/toast/toasts-container.component';
import { NgbDateParserFormatter, NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { DataTablesModule } from 'angular-datatables';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { CookieService } from "ngx-cookie-service";
import { NgxLoadingModule } from 'ngx-loading';
import { OperatoreBuonoModule } from 'projects/operatore-buono/operatore-buono.module';
import { BackofficeToolbarComponent } from '../shared/backoffice-toolbar/backoffice-toolbar.component';
import { FooterComponent } from '../shared/footer/footer.component';
import { ProjectToolbarComponent } from '../shared/project-toolbar/project-toolbar.component';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CurrencyFormat } from './currencyFormatter';
import { CustomDateAdapter } from './format-datepicker';
import { MaterialModule } from './material.module';
import { SelezioneProfiloApplicativoComponent } from './pages/selezione-profilo-applicativo/selezione-profilo-applicativo.component';
import { MatCardModule, MatRadioModule } from '@angular/material';
const routes: Routes = [];


@NgModule({
  declarations: [
    AppComponent,
    BackofficeToolbarComponent,
    ProjectToolbarComponent,
    FooterComponent,
    ToastsContainerComponent,
    RedirectPageComponent,
    SelezioneProfiloApplicativoComponent,
    DatiIstanzaComponent,
    DettaglioArchivioComponent,
    CurrencyFormat,
    TwoDigitDecimalNumberDirective,
    CustomDialogComponent,
    FeedbackComponent,
    VisualizzaCronologiaComponent,
    VisualizzaVerificheComponent,
    CronologiaComponent,
    DatiIstanzaEnteComponent,
    BuonoDettaglioComponent,
    StatoAvanzamentoIstruttoriaComponent
  ],
  entryComponents: [
    CustomDialogComponent,
    VisualizzaCronologiaComponent,
    VisualizzaVerificheComponent,
    CronologiaComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    DataTablesModule,
    NgbModule,
    AppRoutingModule,
    NgxLoadingModule.forRoot({}),
    BrowserAnimationsModule,
    MaterialModule,
    RouterModule,
    OperatoreBuonoModule,
    AppRoutingModule,
    NgMultiSelectDropDownModule,
    NgMultiSelectDropDownModule,
    MatCardModule,
    MatRadioModule
  ],
  providers: [BuonoresBOClient,
    {
      provide: NgbDateParserFormatter,
      useClass: MomentDateFormatter
    },
    CookieService,
    BuonoresErrorService,
    { provide: MAT_DATE_LOCALE, useValue: 'en-GB' },
    { provide: DateAdapter, useClass: CustomDateAdapter },
    {provide: HTTP_INTERCEPTORS, useClass: RequestInterceptor, multi:true}
  ],
  bootstrap: [AppComponent]
})

export class AppModule { }

