import { DownloadB64ServiceService } from './../../../../src/app/DownloadB64Service.service';
import {
  animate,
  state,
  style,
  transition,
  trigger,
} from '@angular/animations';
import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { MatDialog, MatTableDataSource } from '@angular/material';
import { ActivatedRoute, Navigation, Router } from '@angular/router';
import { BuonoresBOClient } from '@buonores-app/app/BuonoresBOClient';
import { ModelContratto } from '@buonores-app/app/dto/ModelContratto';
import { ModelContrattoAllegati } from '@buonores-app/app/dto/ModelContrattoAllegati';
import { ModelDecorrenzaBuono } from '@buonores-app/app/dto/ModelDecorrenzaBuono';
import { ModelDichiarazioneSpesa } from '@buonores-app/app/dto/ModelDichiarazioneSpesa';
import { ModelDocumentoSpesa } from '@buonores-app/app/dto/ModelDocumentoSpesa';
import { ModelIseeBuono } from '@buonores-app/app/dto/ModelIseeBuono';
import { ModelRichiesta } from '@buonores-app/app/dto/ModelRichiesta';
import { ModelSpesaIntegrazione } from '@buonores-app/app/dto/ModelSpesaIntegrazione';
import { AZIONE } from '@buonores-app/constants/buonores-constants';
import { CustomDialogComponent } from '@buonores-app/shared/custom-dialog/custom-dialog.component';
import { BuonoresErrorService } from '@buonores-app/shared/error/buonores-error.service';
import { AppToastService } from '@buonores-shared/toast/app-toast.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-buono-dettaglio',
  templateUrl: './buono-dettaglio.component.html',
  styleUrls: ['./buono-dettaglio.component.css'],
  providers: [DatePipe],
  animations: [
    trigger('rotatedState', [
      state('rotated', style({ transform: 'rotate(0)' })),
      state('default', style({ transform: 'rotate(-180deg)' })),
      transition('rotated => default', animate('100ms ease-out')),
      transition('default => rotated', animate('100ms ease-in')),
    ]),
  ],
})
export class BuonoDettaglioComponent implements OnInit {
  errorMessage = {
    error: { descrizione: '' },
    message: '',
    name: '',
    status: '',
    statusText: '',
    url: '',
    date: Date,
  };

  navigation: Navigation;
  numeroDomanda: string;
  statoBuono: string;
  dataRinuncia: string;
  dataRevoca: string;
  paginaRitorno: string;

  // Dati domanda
  domanda: ModelRichiesta;

  //isee
  isee: ModelIseeBuono;
  iseeprecedente: ModelIseeBuono;
  displayedColumns: string[] = [
    'iseeAnno',
    'conforme',
    'validatoConforme',
    'iseeValore',
    'nota',
    'dataScadenza',
    'dataRilascio',
    'verificatoIndata',
    'operatore',
  ];
  listaIsee: MatTableDataSource<ModelIseeBuono[]>;
  espansa: boolean;
  state: string = 'default';

  // Documenti spesa
  dichiarazioniSpesa: ModelDichiarazioneSpesa[];
  documentiSpesaFiltrati: ModelDocumentoSpesa[];

  // Contratti
  contrattiAllegatiBuono: ModelContrattoAllegati[];
  contrattiAllegatiFiltrati: ModelContrattoAllegati[];

  // Spesa integrazioni
  listaSpesaIntegrazioni: ModelSpesaIntegrazione[] = [];

  // Filtro periodo
  periodoRendicontazioneForm: FormGroup;
  controlloDataFine: boolean = true;
  dateUguali: boolean = true;
  buonoattivo: boolean = true;
  buonoattivorinunciato: boolean = false;
  // Cambia Decorrenza
  decorrenzaBuono: ModelDecorrenzaBuono = new ModelDecorrenzaBuono(
    new Date(),
    new Date()
  );
  decorrenzaBuonoIni: ModelDecorrenzaBuono = new ModelDecorrenzaBuono(
    new Date(),
    new Date()
  );
  cambiaDecorrenzaForm: FormGroup;

  annocorrente: number;
  conformeOption: string = null;
  valoreisee: string;
  datascadenzaIsee: Date;
  dataRilascio: Date;
  note: String;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private dialog: MatDialog,
    public client: BuonoresBOClient,
    private modalService: NgbModal,
    public toastService: AppToastService,
    private buonoresError: BuonoresErrorService,
    public datePipe: DatePipe,
    public appToastService: AppToastService,
    private downloadB64: DownloadB64ServiceService
  ) {
    this.navigation = this.router.getCurrentNavigation();
    let domandaValues: string[] = [];
    this.route.fragment.subscribe((frag: string) => {
      domandaValues.push(frag);
    });
    this.statoBuono =
      this.navigation.extras && this.navigation.extras.state
        ? this.navigation.extras.state.statoBuono
        : domandaValues[0][0];
    this.numeroDomanda =
      this.navigation.extras && this.navigation.extras.state
        ? this.navigation.extras.state.numeroDomanda
        : domandaValues[0][0];
    this.paginaRitorno =
      this.navigation.extras && this.navigation.extras.state
        ? this.navigation.extras.state.paginaRitorno
        : domandaValues[0][0];
  }

  ngOnInit() {
    this.client.spinEmitter.emit(true);
    this.annocorrente = (new Date()).getFullYear();
    if (this.statoBuono !== 'ATTIVO') {
      this.buonoattivo = false;
    }
    //Creazione form
    this.periodoRendicontazioneForm = this.fb.group({
      meseInizio: new FormControl(this.getCurrentYearMonth(), [
        Validators.required,
      ]),
      meseFine: new FormControl(this.getCurrentYearMonth(), [
        Validators.required,
      ]),
    });

    this.cambiaDecorrenzaForm = this.fb.group({
      decorrenzaInizio: new FormControl(this.getCurrentYearMonth(), [
        Validators.required,
      ]),
      decorrenzaFine: new FormControl(
        { value: this.getCurrentYearMonth(), disabled: true },
        [Validators.required]
      ),
    });
    this.espansa=false;
    this.listaIsee = new MatTableDataSource<ModelIseeBuono[]>();

    //Servizi get
    forkJoin({
      allegati: this.client.getAllegatiBuono(this.numeroDomanda),
      contratti: this.client.getContrattiBuono(this.numeroDomanda),
      decorrenza: this.client.getDecorrenzaBuono(this.numeroDomanda),
      decorrenzaIni: this.client.getDecorrenzaBuono(this.numeroDomanda),
      schedaDomanda: this.client.getRichiestaNumero(this.numeroDomanda),
      buonoInformazioniResponse: this.client.getBuonoInformazioni(this.numeroDomanda),
      isee: this.client.getIseeConforme(this.numeroDomanda),
      iseeprec: this.client.getIseeConforme(this.numeroDomanda),
      listaIsee: this.client.getListaIseeConforme(this.numeroDomanda),
    }).subscribe(({ allegati, contratti, decorrenza, decorrenzaIni, schedaDomanda, buonoInformazioniResponse, isee, iseeprec, listaIsee}) => {
        //Allegati
        if (allegati) {
          this.dichiarazioniSpesa = allegati;

          this.documentiSpesaFiltrati = this.dichiarazioniSpesa.reduce(
            (
              acc: ModelDocumentoSpesa[],
              dichiarazioneSpesa: ModelDichiarazioneSpesa
            ) => acc.concat(dichiarazioneSpesa.documentiSpesa),
            []
          );
        }

        //Contratti
        if (contratti) {
          this.contrattiAllegatiBuono = contratti;
        }

        //Decorrenza
        if (decorrenza) {
          this.decorrenzaBuono = decorrenza;
          this.cambiaDecorrenzaForm = this.fb.group({
            decorrenzaInizio: new FormControl(
              this.datePipe.transform(
                this.decorrenzaBuono.decorrenzaInizio,
                'yyyy-MM'
              ),
              [Validators.required]
            ),
            decorrenzaFine: new FormControl(
              {
                value: this.datePipe.transform(
                  this.decorrenzaBuono.decorrenzaFine,
                  'yyyy-MM'
                ),
                disabled: true,
              },
              [Validators.required]
            ),
          });
        }

        //DecorrenzaIni
        if (decorrenzaIni) {
          this.decorrenzaBuonoIni = decorrenzaIni;
        }

        //Dati domanda
        if (schedaDomanda) {
          this.domanda = schedaDomanda;
        }

        if (buonoInformazioniResponse) {
          this.dataRinuncia = buonoInformazioniResponse.rinunciaDataDecorrenza;
          this.dataRevoca = buonoInformazioniResponse.revocaDataDecorrenza;
        }

        //isee
          this.iseeprecedente = iseeprec;
        if (isee) {
          this.isee = isee;
          if (this.isee.isee_valore!=null)
          this.valoreisee = this.transform(this.isee.isee_valore);
          else
          this.valoreisee = null;
          this.datascadenzaIsee = this.isee.isee_scadenza;
          this.dataRilascio = this.isee.isee_data_rilascio;
          this.note = this.isee.isee_note;
          this.conformeOption = this.isee.isee_verifica_esito_cod;
        }else{
        this.note = null;
        this.valoreisee = null;
        this.dataRilascio = null;
        this.conformeOption = null;
        }

        if ((this.statoBuono === 'ATTIVO' || this.statoBuono === 'RINUNCIATO')
        && this.conformeOption !=='NON CONFORME')
        this.buonoattivorinunciato = true;

        this.listaIsee.data = listaIsee as any;
        //Filtro di default
        this.filtraPerPeriodo();
        this.client.spinEmitter.emit(false);
      }
    );
  }

  //--------------FORM GETTERS------------
  get meseInizio() {
    return this.periodoRendicontazioneForm.get('meseInizio');
  }

  get meseFine() {
    return this.periodoRendicontazioneForm.get('meseFine');
  }

  get decorrenzaInizio() {
    return this.cambiaDecorrenzaForm.get('decorrenzaInizio');
  }

  get decorrenzaFine() {
    return this.cambiaDecorrenzaForm.get('decorrenzaFine');
  }

  isNumber(value: string | number): boolean {
    return ((value != null) &&
      (value !== '') &&
      !isNaN(Number(value.toString().replace(',', '.'))));
  }


 transform(value: number,
    currencySign: string = '',
    decimalLength: number = 2,
    chunkDelimiter: string = '.',
    decimalDelimiter: string = ',',
    chunkLength: number = 3): string {
    let result = '\\d(?=(\\d{' + chunkLength + '})+' + (decimalLength > 0 ? '\\D' : '$') + ')';
    if (this.isNumber(value)) {
      if (value != null && value != undefined) {
        let num = value.toFixed(Math.max(0, ~~decimalLength));
        return currencySign + (decimalDelimiter ? num.replace('.', decimalDelimiter) : num).replace(new RegExp(result, 'g'), '$&' + chunkDelimiter);
      }
    }
    return undefined;
  }
  changeKey(valore: any) {
    if (valore == "" || valore == null) {
      this.valoreisee = null;
    }
    else {
      if (valore.indexOf('.') !== -1) {
        if (valore.indexOf(',') !== -1 && (valore.slice(valore.indexOf(',') + 1, valore.length)).length != 2)
          valore = [valore, '0'].join('');
        this.valoreisee = valore;
      }
      else this.valoreisee = this.transform(parseFloat(valore.toString().replace(',', '.')));
    }

    if(this.valoreisee == null && this.conformeOption == 'CONFORME') {
      this.toastService.showError({ text: 'Inserire il valore dell\'isee per proseguire' });
    }
  }

  //--------------METODI------------
  //Controllo azione utente
  checkAzione(azione: string): boolean {
    switch (azione) {
      case 'richiesta_rettifica_ente':
        return this.client.azioni.some(
          (a) => a.codAzione == AZIONE.OP_RichiediRettificaEnte
        );
      case 'revoca_buono':
        return this.client.azioni.some(
          (a) => a.codAzione == AZIONE.OP_RevocaBuono
        );
      default:
        return false;
    }
  }

  //Fa il routing alla pagina precedente
  backButton() {
    	//controllo se ho salvato isee
	// if (this.controllasalvataggioisee()) { // TODO aggiunto in DOM - TBV   if (this.isee && this.controllasalvataggioisee()) { il 05/02/2024
  //   this.toastService.showError({ text: 'Per procedere e\' necessario salvare i valori dell\'isee' });
  // }
  // else {
    switch (this.paginaRitorno) {
      case 'istruttoria':
        this.router.navigate(['/operatore-buono/istruttoria'], {
          relativeTo: this.route,
          skipLocationChange: true,
        });
        break;
      case 'archivio-domande':
        this.router.navigate(['/operatore-buono/archivio-domande'], {
          relativeTo: this.route,
          skipLocationChange: true,
        });
        break;
      case 'istanze-aperte':
        this.router.navigate(['/operatore-buono/istanze-aperte'], {
          relativeTo: this.route,
          skipLocationChange: true,
        });
        break;
      case 'rendicontazione-op':
        this.router.navigate(['/operatore-buono/rendicontazione-op'], {
          relativeTo: this.route,
          skipLocationChange: true,
        });
        break;
      case 'rendicontazione-ente':
        this.router.navigate(['/operatore-buono/rendicontazione-ente'], {
          relativeTo: this.route,
          skipLocationChange: true,
        });
        break;
      default:
        this.router.navigate(['selezione-profilo-applicativo'], {
          skipLocationChange: true,
        });
        break;
    }
  // }
  }

  //Prende mese e anno corrente
  getCurrentYearMonth(): string {
    const currentDate = new Date();
    return this.datePipe.transform(currentDate, 'yyyy-MM') || '';
  }

  //Esegue le validazioni delle date inserite
  validazioneDate() {
    if (this.meseInizio.value) {
      if (this.meseFine.value) {
        if (this.meseFine.value < this.meseInizio.value) {
          this.controlloDataFine = true;
        } else {
          this.controlloDataFine = false;
        }
      }
    }
  }

  // Filtra le liste in base al periodo selezionato
  filtraPerPeriodo() {
    //Parsing del periodo di filtro
    let meseFine = new Date(this.meseFine.value);
    meseFine.setDate(1);
    meseFine.setMonth(meseFine.getMonth() + 1);
    meseFine.setDate(meseFine.getDate() - 1);
    meseFine.setHours(23, 59, 59, 999);

    let meseInizio = new Date(this.meseInizio.value);
    meseInizio.setDate(1);
    meseInizio.setHours(0, 0, 0, 0);

    // Valorizzazione liste filtrate
    this.documentiSpesaFiltrati = this.dichiarazioniSpesa.reduce(
      (
        acc: ModelDocumentoSpesa[],
        dichiarazioneSpesa: ModelDichiarazioneSpesa
      ) => acc.concat(dichiarazioneSpesa.documentiSpesa),
      []
    );
    this.contrattiAllegatiFiltrati = this.contrattiAllegatiBuono;

    //Filtro delle liste
    this.documentiSpesaFiltrati = this.documentiSpesaFiltrati.filter(
      (documentoSpesa) =>
        new Date(documentoSpesa.periodoInizio) <= meseFine &&
        (documentoSpesa.periodoFine === null ||
          new Date(documentoSpesa.periodoFine) >= meseInizio)
    );

    //   this.contrattiAllegatiFiltrati = this.contrattiAllegatiFiltrati.filter(contrattoAllegato => (
    //     (new Date(contrattoAllegato.contratto.data_inizio) <= meseFine) && (contrattoAllegato.contratto.data_fine === null || (new Date(contrattoAllegato.contratto.data_fine) >= meseInizio))))

    // Parte del filtro di spesa integrazioni
    const payload = {
      meseInizio: this.formatTimestamp(meseInizio),
      meseFine: this.formatTimestamp(meseFine),
    };
    // console.log('payload'); //DEBUG
    // console.log(payload); //DEBUG
    this.client.spinEmitter.emit(true);
    this.client
      .getIntegrazioniSpesaFiles(this.numeroDomanda, payload)
      .subscribe(
        (data: ModelSpesaIntegrazione[]) => {
          this.listaSpesaIntegrazioni = data;
          // console.log('SpesaIntegrazioni'); //DEBUG
          // console.log(this.listaSpesaIntegrazioni); //DEBUG
          this.client.spinEmitter.emit(false);
        },
        (error) => {
          this.client.spinEmitter.emit(false);
          console.error(
            'Si è verificato un errore durante la chiamata:',
            error
          );
        }
      );

    this.client.spinEmitter.emit(false);
  }

  formatTimestamp(date: Date) {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const seconds = date.getSeconds().toString().padStart(2, '0');
    const milliseconds = date.getMilliseconds().toString().padStart(3, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}.${milliseconds}`;
  }

  salvaDecorrenza() {
    this.client.spinEmitter.emit(true);
    this.calcolaDifferenzaMesi();
    const dataInizio = new Date(this.decorrenzaInizio.value);
    const dataFine = new Date(this.decorrenzaFine.value);
    const nuovaDecorrenza = new ModelDecorrenzaBuono(dataInizio, dataFine);
    this.client
      .updateDecorrenzaBuono(this.numeroDomanda, nuovaDecorrenza)
      .subscribe(
        (decorrenza: ModelDecorrenzaBuono) => {
          if (decorrenza) {
            this.decorrenzaBuono = decorrenza;
            this.cambiaDecorrenzaForm = this.fb.group({
              decorrenzaInizio: new FormControl(
                this.datePipe.transform(
                  this.decorrenzaBuono.decorrenzaInizio,
                  'yyyy-MM'
                ),
                [Validators.required]
              ),
              decorrenzaFine: new FormControl(
                {
                  value: this.datePipe.transform(
                    this.decorrenzaBuono.decorrenzaFine,
                    'yyyy-MM'
                  ),
                  disabled: true,
                },
                [Validators.required]
              ),
            });
            this.dateUguali = true;
            this.client.listaStatiBuonoSalvato = [];
            this.appToastService.showSuccess({
              text: 'Decorrenza aggiornata con successo',
            });
          }
          this.client.spinEmitter.emit(false);
        },
        (err) => {
          this.client.spinEmitter.emit(false);
        }
      );
  }

  calcolaDifferenzaMesi() {
    if (
      this.datePipe.transform(
        this.decorrenzaBuonoIni.decorrenzaInizio,
        'yyyy-MM'
      ) != this.decorrenzaInizio.value
    ) {
      this.dateUguali = false;
    } else {
      this.dateUguali = true;
    }
    const data1Date = new Date(new Date(this.decorrenzaBuono.decorrenzaInizio));
    const data2Date = new Date(this.decorrenzaInizio.value);
    const data3Date = new Date(new Date(this.decorrenzaBuono.decorrenzaFine));

    const anniData1 = data1Date.getFullYear();
    const mesiData1 = data1Date.getMonth();
    const anniData2 = data2Date.getFullYear();
    const mesiData2 = data2Date.getMonth();
    const anniData3 = data3Date.getFullYear();
    const mesiData3 = data3Date.getMonth();

    const differenzaMesi =
      (anniData2 - anniData1) * 12 + (mesiData2 - mesiData1);

    const risultatoDate = new Date(anniData3, mesiData3 + differenzaMesi);

    this.decorrenzaFine.setValue(
      this.datePipe.transform(risultatoDate, 'yyyy-MM')
    );
  }

  //Richiama il servizio per scaricare un allegato
  scaricaAllegato(idAllegato: Number) {
    return this.client.scaricaAllegatoBuono(idAllegato);
  }

  scaricaRendicontazione(idAllegato: Number) {
    return this.client.scaricaRendicontazioneBuono(idAllegato);
  }

  //Rimuove tutti i '_' da una stringa
  replace_(string: String) {
    return string.replace(/_/g, ' ');
  }

  //Italianizza gli importi
  convertiNumeroInItaliano(importo: string): string {
    const pivaAssistenteFamiliare = parseFloat(importo);
    const numeroItaliano = Number(pivaAssistenteFamiliare).toLocaleString(
      'it-IT'
    );
    return numeroItaliano;
  }

  getDataInvio(idRend: number): Date {
    for (let i = 0; i < this.dichiarazioniSpesa.length; i++) {
      let documenti = this.dichiarazioniSpesa[i].documentiSpesa;
      for (let j = 0; j < documenti.length; j++) {
        if (documenti[j].idDocSpesa === idRend) {
          if (this.dichiarazioniSpesa[i].statoCod === 'INVIATA')
            return this.dichiarazioniSpesa[i].dataInvio;
          else return null;
        }
      }
    }
    return null;
  }

  getStatoDichiarazione(idRend: number): String {
    for (let i = 0; i < this.dichiarazioniSpesa.length; i++) {
      let documenti = this.dichiarazioniSpesa[i].documentiSpesa;
      for (let j = 0; j < documenti.length; j++) {
        if (documenti[j].idDocSpesa === idRend) {
          return this.dichiarazioniSpesa[i].statoDesc;
        }
      }
    }
    return null;
  }

  getDataFineContratto(contratto: ModelContratto): String {
    return contratto.data_fine
      ? 'al ' + this.datePipe.transform(contratto.data_fine, 'dd/MM/yyyy')
      : '';
  }

  downloadFile() {
    const base64 = '';
    const mimeType = '';
    const fileName = '';
    this.downloadB64.getFile(base64, mimeType, fileName);
  }

  parsingFloat(el) {
    if (el == '' || el == '-') el = null;
    el = el ? parseFloat(el.toString().replaceAll('.', '').replace(',', '.')) : el;
    return el;
  }

  salvaNonConforme() {
    if (this.conformeOption == 'NON CONFORME') {
      let message = '';
      let titolo = '';
      message = `Sei sicuro di voler confermare la non conformita' dell'isee ?`;
      titolo = `Salvataggio isee non conforme`;
      const dialogRef = this.dialog.open(CustomDialogComponent, {
        data: {
          messaggio: message,
          titolo: titolo,
          conferma: 'CONFERMA',
          annulla: 'ANNULLA'
        },
        disableClose: true
      });
      dialogRef.afterClosed().subscribe((result: boolean) => {
        if (result) {
          this.salvaIsee();
        } else {
          this.client.spinEmitter.emit(false);
        }
      })
    }
    else
      this.salvaIsee();
  }



  salvaIsee() {
    if (!this.controllasalvataggioisee()) {
      this.toastService.showError({ text: 'Nessun dato da salvare. Dati isee invariati' });
    }
    else if ((this.conformeOption == 'RESPINTO' || this.conformeOption == 'NON CONFORME')
      && this.note == null) {
      this.toastService.showError({ text: 'Inserire la nota per proseguire' });
    }
    else {
      this.client.spinEmitter.emit(true);

      this.isee = new ModelIseeBuono(this.parsingFloat(this.valoreisee),
        this.dataRilascio, this.datascadenzaIsee, this.conformeOption, null,
        this.note, this.annocorrente.toString(), null, null, null, null, null, null, null);

      this.client.salvaIsee(this.domanda.numero, this.isee).subscribe(
        () => {
          this.iseeprecedente = this.isee;
          this.client.listaRicercaBuonoSalvato = [];
          this.buonoattivorinunciato = false;
          // if ((this.statoBuono === 'ATTIVO' || this.statoBuono === 'CREATO')
          // && this.conformeOption !=='NON CONFORME')
          //   this.buonoattivorinunciato = true;
          this.toastService.showSuccess({ text: 'Isee salvato correttamente' });
          this.updateCrono();
          this.client.spinEmitter.emit(false);
        },
        err => {
          this.client.spinEmitter.emit(false);
        }
      );
    }
  }

 controllasalvataggioisee(): boolean {

    //se isee non salvato non faccio andare avanti
    let datarilascio = null;
    let datarilascioprec = null;
    let datascadenza = null;
    let datascadenzaprec = null;
	if (this.iseeprecedente===null){
		return this.parsingFloat(this.valoreisee) != null ||
      this.conformeOption != null ||
      datarilascio != null ||
      datascadenza != null ||
		this.note != null;
	}
    if (this.dataRilascio != null && this.dataRilascio.toString() != '')
      datarilascio = new Date(this.dataRilascio).getTime();
    if (this.iseeprecedente.isee_data_rilascio != null && this.iseeprecedente.isee_data_rilascio.toString() != '')
      datarilascioprec = new Date(this.iseeprecedente.isee_data_rilascio).getTime();
    if (this.datascadenzaIsee != null && this.datascadenzaIsee.toString() != '')
      datascadenza = new Date(this.datascadenzaIsee).getTime();
    if (this.iseeprecedente.isee_scadenza != null && this.iseeprecedente.isee_scadenza.toString() != '')
      datascadenzaprec = new Date(this.iseeprecedente.isee_scadenza).getTime();
    return this.parsingFloat(this.valoreisee) != this.iseeprecedente.isee_valore ||
      this.conformeOption != this.iseeprecedente.isee_verifica_esito_cod ||
      datarilascio != datarilascioprec ||
      datascadenza != datascadenzaprec ||
		this.note != this.iseeprecedente.isee_note;
  }

apriIsee(){
    if (this.espansa)
    this.espansa=false;
    else
    this.espansa=true;
    this.state = (this.state === 'default' ? 'rotated' : 'default');
  }


  updateCrono(){
	this.client.spinEmitter.emit(true);
    this.espansa=false;
   this.listaIsee = new MatTableDataSource<ModelIseeBuono[]>();

      this.client.getListaIseeConforme(this.numeroDomanda).subscribe(
        (response: ModelIseeBuono[]) => {
        this.listaIsee.data = response as any;
      this.client.spinEmitter.emit(false);
	}
      )
    this.espansa=false;
    this.state='rotated';
    this.apriIsee();

  }



}
