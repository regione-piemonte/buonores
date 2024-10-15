import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { forkJoin, Observable } from 'rxjs';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort, Sort } from '@angular/material/sort';
import { MatDialog } from '@angular/material';
import { AppToastService } from '@buonores-shared/toast/app-toast.service';
import { BuonoresBOClient } from '@buonores-app/app/BuonoresBOClient';
import { AZIONE, SECTION, STATO_DOMANDA } from '@buonores-app/constants/buonores-constants';
import { FiltriDomandeAperte } from '@buonores-app/app/dto/FiltriDomandeAperte';
import { StatiBuonores } from '@buonores-app/app/dto/StatiBuonores';
import { DomandeAperte } from '@buonores-app/app/dto/DomandeAperte';
import { CustomDialogComponent } from '@buonores-app/shared/custom-dialog/custom-dialog.component';
import { PresaInCaricoModel } from '@buonores-app/app/dto/PresaInCaricoModel';
import { VisualizzaCronologiaComponent } from '../visualizza-cronologia/visualizza-cronologia.component';
import { VisualizzaVerificheComponent } from '../visualizza-verifiche/visualizza-verifiche.component';
import { map, startWith } from 'rxjs/operators';
import { ModelVerifiche } from '@buonores-app/app/dto/ModelVerifiche';
import { PopupNuovaGraduatoriaComponent } from '../popup-nuova-graduatoria/popup-nuova-graduatoria';
import { PopupNuovoSportelloComponent } from '../popup-nuovo-sportello/popup-nuovo-sportello';



@Component({
  selector: 'app-istanze-aperte',
  templateUrl: './istanze-aperte.component.html',
  styleUrls: ['./istanze-aperte.component.css']
})
export class IstanzaAperteComponent implements OnInit, AfterViewInit, OnDestroy {

  tooltipRendicontazione: string = '';

  isChecked: boolean = false;
  isDisabled: boolean = true;

  searchForm: FormGroup;

  denomSelected: string;
  dataSistema = new Date();
  annoSelezionato: number = null;
  statoSelezionato: string = null;
  ricercaEffettuata: boolean = false;
  sortedData: DomandeAperte[];
  statiVerifiche: string[] = ['Richiesta verifica', 'Verifica in corso', 'Verifica effettuata'];
  esitiVerifiche: string[] = ['Positivo', 'Negativo'];
  displayedColumns: string[] = ['check', 'numeroDomanda', 'Destinatario', 'Richiedente', 'codStato', 'dataDomanda1', 'decessoResidenza', 'enteGestore', 'statoEsito', 'dataVerifica', 'azione'];
  dataListaRichieste: MatTableDataSource<DomandeAperte>;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;

  selezionaTutto: boolean = false;
  // Parametro per la renderizzazione del pulsante Home
  param: boolean = false;
  stati: StatiBuonores[];

  messaggeVerifica: string = "- Stato della verifica richiesta dall'operatore regionale.\n- Esito della verifica da parte dell'ente gestore.";

  constructor(private fb: FormBuilder, private router: Router, private dialog: MatDialog, private route: ActivatedRoute,
    private modalService: NgbModal, public client: BuonoresBOClient, public toastService: AppToastService) { }

  getProperty = (obj: DomandeAperte, path: string) => (
    path.split('.').reduce((o: { [x: string]: any; }, p: string | number) => o && o[p], obj)
  )

  checkUncheckAll() {
    let c: number = 0;
    this.dataListaRichieste.data.forEach(element => {
      if (element.checked) {
        c++;
      }
    })
    if (c < this.dataListaRichieste.data.length) {
      this.dataListaRichieste.data.forEach(element => {
        element.checked = true;
      })
    } else {
      this.dataListaRichieste.data.forEach(element => {
        element.checked = !element.checked;
      })
    }

  }

  ngOnInit() {
    if (this.client.listaStatiSalvato.length == 0) {
      this.client.spinEmitter.emit(true);
      this.searchForm = this.fb.group({
        codSportello: new FormControl(Validators.required),
        numeroDomanda: [],
        statoDomanda: [],
        destinatario: [],
        richiedente: [],
        formEnteGestore: [],
        statoVerifica: [],
        formVerificaEnteEffettuata: [],
        formPresenzaDiMisureNonCompatibili: [],
        dataVerificaDa: [],
        dataVerificaA: [],
        formDecessoResidenza: [],
      });
      forkJoin({
        statiDomanda: this.client.getStati(SECTION.RICERCA_DOMANDE_APERTE),
        listaSportelli: this.client.getSportelli(),
        listaEntiGestori: this.client.getEntiGestori()
      })
        .subscribe(({ statiDomanda, listaSportelli, listaEntiGestori }) => {

          this.client.listaStatiSalvato = statiDomanda;
          this.client.listaSportelliSalvato = listaSportelli;
          this.client.listaEntiGestori = listaEntiGestori;
          this.getDenominazioniEntiGestori();
          this.searchForm = this.fb.group({
            codSportello: [],
            numeroDomanda: [],
            statoDomanda: [],
            destinatario: [],
            richiedente: [],
            formEnteGestore: [],
            statoVerifica: [],
            formVerificaEnteEffettuata: [],
            formPresenzaDiMisureNonCompatibili: [],
            dataVerificaDa: [],
            dataVerificaA: [],
            formDecessoResidenza: [],
          });
          this.matAutocomplete(); //MATAUTOCOMPLETE
          this.client.spinEmitter.emit(false);
        },
          err => {
            this.client.spinEmitter.emit(false)
          }
        );
      this.dataListaRichieste = new MatTableDataSource<DomandeAperte>();
      this.dataListaRichieste.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
      this.dataListaRichieste.sort = this.sort;
    }
    else {
      if (this.client.ricercaDomAperte.length > 0) {
        this.ricercaEffettuata = true;
        this.dataListaRichieste = new MatTableDataSource<DomandeAperte>();
        this.dataListaRichieste.data = this.client.ricercaDomAperte as any;
        this.dataListaRichieste.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
        this.dataListaRichieste.sort = this.sort;
        this.dataListaRichieste.paginator = this.paginator;
        this.searchForm = this.fb.group({
          codSportello: [this.client.filtroDomandeAperte.codSportello],
          numeroDomanda: [this.client.filtroDomandeAperte.numeroDomanda],
          statoDomanda: [this.client.filtroDomandeAperte.statoDomanda],
          destinatario: [this.client.filtroDomandeAperte.destinatario],
          richiedente: [this.client.filtroDomandeAperte.richiedente],
          formEnteGestore: [this.client.filtroDomandeAperteEnte],
          statoVerifica: [this.client.filtroDomandeAperte.statoVerificaEnteGestore],
          formVerificaEnteEffettuata: [this.client.filtroDomandeAperte.verificaEnteGestore],
          formPresenzaDiMisureNonCompatibili: [this.client.filtroDomandeAperte.esitoVerificaEnteGestore],
          dataVerificaDa: [this.client.filtroDomandeAperte.dataVerificaDa],
          dataVerificaA: [this.client.filtroDomandeAperte.dataVerificaA],
          formDecessoResidenza: [this.client.filtroDomandeAperte.decessoResidenza],
        });
        this.matAutocomplete(); //MATAUTOCOMPLETE
      }
      else {
        this.dataListaRichieste = new MatTableDataSource<DomandeAperte>();
        this.searchForm = this.fb.group({
          codSportello: [],
          numeroDomanda: [],
          statoDomanda: [],
          destinatario: [],
          richiedente: [],
          formEnteGestore: [],
          statoVerifica: [],
          formVerificaEnteEffettuata: [],
          formPresenzaDiMisureNonCompatibili: [],
          dataVerificaDa: [],
          dataVerificaA: [],
          formDecessoResidenza: [],
        });
        this.matAutocomplete(); //MATAUTOCOMPLETE
        this.dataListaRichieste.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
        this.dataListaRichieste.sort = this.sort;
      }
    }
  }

  //------------------------------------------------------------------------------------------MATAUTOCOMPLETE
  matAutocomplete() {
    this.client.filteredEntiRegionali = this.searchForm.get('formEnteGestore').valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value || ''))
    );
  }
  // filtro gli enti da visualizzare in base a quello scelto
  private _filter(value: string): string[] {
    const filterValue = this._normalizeValue(value);
    return this.client.listaDenominazioneEntiGestori.filter(ente => this._normalizeValue(ente).includes(filterValue));
  }
  // elimino gli spazi dai nomi composti
  private _normalizeValue(value: string): string {
    return value.toLowerCase().replace(/\s/g, '');
  }

  // Ottengo solo le denominazioni degli EntiGestori
  getDenominazioniEntiGestori() {
    this.client.listaDenominazioneEntiGestori = this.client.listaEntiGestori.map(ente => ente.denominazioneEnte);
  }
  //----------------------------------------------------------------------------------------------------------

  ngAfterViewInit() {
    if (this.ricercaEffettuata) {
      if (this.client.paginaSalvata !== this.paginator.pageIndex ||
        this.client.righePerPagina !== this.paginator.pageSize ||
        this.client.ordinamentoSalvato !== this.sort.active ||
        this.client.versoSalvato !== this.sort.direction) {
        setTimeout(() => {
          this.paginator.pageSize = this.client.righePerPagina;
          this.paginator.pageIndex = this.client.paginaSalvata;
          this.sort.active = this.client.ordinamentoSalvato;
          this.sort.direction = this.client.versoSalvato;
          this.dataListaRichieste.paginator = this.paginator;
          this.dataListaRichieste.sort = this.sort;
        }, 0);
      }
    } else {
      this.dataListaRichieste.sort = this.sort;
      this.dataListaRichieste.paginator = this.paginator;
    }
  }

  checkAzione(azione: string): boolean {
    switch (azione) {
      case "verifiche":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_VisualizzaVerifiche);
      case "cronologia":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_VisualizzaCronologia);
      case "OP-PrendiInCarico":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_PrendiInCarico);
      case "OP-NuovoSportello":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_NuovoSportello);
      default:
        return false;
    }
  }

  avviaRicerca() {
    this.client.spinEmitter.emit(true);
    this.client.righePerPagina = 10;
    this.client.paginaSalvata = 0;
    this.client.ordinamentoSalvato = null;
    this.client.versoSalvato = null;
    const codSportello = this.searchForm.controls.codSportello.value && this.searchForm.controls.codSportello.value != "" ? this.searchForm.controls.codSportello.value : null;
    const numeroDomanda = this.searchForm.controls.numeroDomanda.value && this.searchForm.controls.numeroDomanda.value.toUpperCase();
    const statoDomanda = this.searchForm.controls.statoDomanda.value && this.searchForm.controls.statoDomanda.value != "" ? this.searchForm.controls.statoDomanda.value : null;
    const destinatario = this.searchForm.controls.destinatario.value && this.searchForm.controls.destinatario.value.toUpperCase();
    const richiedente = this.searchForm.controls.richiedente.value && this.searchForm.controls.richiedente.value.toUpperCase();
    //ENTE GESTORE
    const enteGestoreForm = this.searchForm.controls.formEnteGestore.value && this.searchForm.controls.formEnteGestore.value != "" ? this.searchForm.controls.formEnteGestore.value : null;
    this.client.filtroDomandeAperteEnte = enteGestoreForm;
    let enteGestore: number[] = [];
    if (enteGestoreForm !== null) {
      const enteSearch = this.searchEnteGestore(enteGestoreForm);
      // Controllo nel caso venga immessa una ricerca errata
      if (enteSearch === null) {
        enteGestore = null;
        this.dataListaRichieste.data = [];
        this.client.ricercaDomAperte = [];
        this.client.spinEmitter.emit(false);
        return;
      } else {
        enteGestore.push(enteSearch);
      }
    } else {
      enteGestore = null;
    }
    // Stato verifica
    let statoVerifica = this.searchForm.controls.statoVerifica.value && this.searchForm.controls.statoVerifica.value != "" ? this.searchForm.controls.statoVerifica.value : null;
    // Verifica ente effettuata
    let verificaEnteEffettuata = this.searchForm.controls.formVerificaEnteEffettuata.value && this.searchForm.controls.formVerificaEnteEffettuata.value != "" ? this.searchForm.controls.formVerificaEnteEffettuata.value : null;
    // Esito verifica ente, presenza di misure non compatibili
    let esitoVerificaEnteGestore = this.searchForm.controls.formPresenzaDiMisureNonCompatibili.value && this.searchForm.controls.formPresenzaDiMisureNonCompatibili.value != "" ? this.searchForm.controls.formPresenzaDiMisureNonCompatibili.value : null;
    // Ultima data verifica
    let dataVerificaDa = this.searchForm.controls.dataVerificaDa.value;
    let dataVerificaA = this.searchForm.controls.dataVerificaA.value;
    let dataItaliana = null;
    let dataFormattataDa = null;
    let dataFormattataA = null;
    if (dataVerificaDa != null && dataVerificaDa != '') {
      dataItaliana = new Date(dataVerificaDa);
      dataFormattataDa = dataItaliana.toLocaleDateString("it-IT");
    }
    if (dataVerificaA != null && dataVerificaA != '') {
      dataItaliana = new Date(dataVerificaA);
      dataFormattataA = dataItaliana.toLocaleDateString("it-IT");
    }
    // Decesso/Residenza
    let decessoResidenza = this.searchForm.controls.formDecessoResidenza.value && this.searchForm.controls.formDecessoResidenza.value != "" ? this.searchForm.controls.formDecessoResidenza.value : null;

    // Oggetto per ricerca
    const ricerca = new FiltriDomandeAperte(codSportello, numeroDomanda, statoDomanda, destinatario, richiedente, SECTION.RICERCA_DOMANDE_APERTE, enteGestore, statoVerifica, verificaEnteEffettuata, esitoVerificaEnteGestore, dataFormattataDa, dataFormattataA, decessoResidenza);
    // Filtro backup ricerca
    this.client.filtroDomandeAperte = new FiltriDomandeAperte(codSportello, numeroDomanda, statoDomanda, destinatario, richiedente, SECTION.RICERCA_DOMANDE_APERTE, enteGestore, statoVerifica, verificaEnteEffettuata, esitoVerificaEnteGestore, dataVerificaDa, dataVerificaA, decessoResidenza);;

    this.client.ricercaDomandeAperte(ricerca).subscribe((response: DomandeAperte[]) => {
      this.client.spinEmitter.emit(false);
      response.forEach(element => {
        element.checked = false;
      })
      this.dataListaRichieste.data = response as any;
      this.client.ricercaDomAperte = response as any;
      this.dataListaRichieste.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
      this.dataListaRichieste.sort = this.sort;
      this.dataListaRichieste.paginator = this.paginator;
      this.ricercaEffettuata = true;
      this.ngAfterViewInit();
      this.checkSelezione();
    },
      err => {
        this.client.spinEmitter.emit(false);
      });
  }

  searchEnteGestore(denominazione: string): number {
    const ente = this.client.listaEntiGestori.find((e) => e.denominazioneEnte === denominazione);
    // Controllo nel caso venga immessa una ricerca errata
    if (ente === undefined || ente === null) {
      return null;
    }
    return ente.idEnteGestore;
  }

  ngOnDestroy(): void {
    this.client.paginaSalvata = this.paginator.pageIndex;
    this.client.ordinamentoSalvato = this.sort.active;
    this.client.versoSalvato = this.sort.direction;
    this.client.righePerPagina = this.paginator.pageSize;
  }

  sortData(sort: Sort) {
    const data = this.dataListaRichieste.data.slice();
    if (!sort.active || sort.direction === '') {
      this.sortedData = data;
      return;
    }

    this.sortedData = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'numeroDomanda':
          return this.compare(a.numeroDomanda, b.numeroDomanda, isAsc);
        case 'Destinatario':
          return this.compare(a.cfDestinatario, b.cfDestinatario, isAsc);
        case 'Richiedente':
          return this.compare(a.cfRichiedente, b.cfRichiedente, isAsc);
        case 'codStato':
          return this.compare(a.stato.codStato, b.stato.codStato, isAsc);
        case 'dataDomanda1':
          // Fix nel caso una delle due date sua nulla
          let dataStringA = a.dataDomanda;
          let dataStringB = b.dataDomanda;
          if (a.dataDomanda === null) {
            if (isAsc !== true) {
              dataStringA = '01/01/1970';
            } else if (isAsc) {
              dataStringA = '20/12/2099';
            }
          }
          if (b.dataDomanda === null) {
            if (isAsc !== true) {
              dataStringB = '01/01/1970';
            } else if (isAsc) {
              dataStringB = '20/12/2099';
            }
          }
          let dateA = new Date(dataStringA.split("/").reverse().join("-"));
          let dateB = new Date(dataStringB.split("/").reverse().join("-"));

          return this.compare(dateA.getTime(), dateB.getTime(), isAsc);
        case 'enteGestore':
          return this.compare(a.enteGestore.denominazioneEnte, b.enteGestore.denominazioneEnte, isAsc);
        case 'dataVerifica':
          if (a.dataVerificaBuono === null) {
            a.dataVerificaBuono = "";
          }
          if (b.dataVerificaBuono === null) {
            b.dataVerificaBuono = "";
          }
          return this.compare(a.dataVerificaBuono, b.dataVerificaBuono, isAsc);
        case 'esitoVerifica':
          let esitoVerificaA = String(a.esitoVerificaBuono);
          let esitoVerificaB = String(b.esitoVerificaBuono);
          if (esitoVerificaA === 'null') {
            esitoVerificaA = '';
          }
          if (esitoVerificaB === 'null') {
            esitoVerificaB = '';
          }
          return this.compare(esitoVerificaA, esitoVerificaB, isAsc);
        default:
          return 0;
      }
    });
    this.dataListaRichieste.data = this.sortedData;
    this.client.ricercaDomAperte = this.sortedData;
  }

  compare(a: number | string, b: number | string, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }

  compareVerifiche(verifica: ModelVerifiche): string {
    if (verifica.verifica_eg_richiesta !== null) {
      return this.client.listaStatiVericaEnteGestore[0].replace(/\s+/g, "");
    } else if (verifica.verifica_eg_in_corso !== null) {
      return this.client.listaStatiVericaEnteGestore[1].replace(/\s+/g, "");
    } else if (verifica.verifica_eg_conclusa !== null) {
      return this.client.listaStatiVericaEnteGestore[2].replace(/\s+/g, "");
    } else {
      return "null";
    }
  }

  routeTo(from: string, content: DomandeAperte, path: string) {
    this.client.paginaSalvata = this.paginator.pageIndex
    this.client.ordinamentoSalvato = this.sort.active;
    this.client.versoSalvato = this.sort.direction;
    this.client.righePerPagina = this.paginator.pageSize;
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      skipLocationChange: true,
      state: {
        numerodomanda: content.numeroDomanda,
        paginaRitorno: from
      }
    };
    this.router.navigate([path], navigationExtras);
  }


  annullaRicerca() {
    this.searchForm.reset();
    // Creo il form
    this.searchForm = this.fb.group({
      codSportello: [],
      numeroDomanda: [],
      statoDomanda: [],
      destinatario: [],
      richiedente: [],
      formEnteGestore: [],
      statoVerifica: [],
      formVerificaEnteEffettuata: [],
      formPresenzaDiMisureNonCompatibili: [],
      dataVerificaDa: [],
      dataVerificaA: [],
      formDecessoResidenza: [],
    });
    // Reset impostazioni di default del paginator
    this.client.ricercaDomAperte = [];
    this.dataListaRichieste.data = [];
    this.ricercaEffettuata = false;
  }

  openCronologia(content: DomandeAperte) {

    const modalRef = this.modalService.open(VisualizzaCronologiaComponent, { size: 'xl' });
    modalRef.componentInstance.numeroDomanda = content.numeroDomanda;
  }

  openVerifiche(content: DomandeAperte) {

    const modalRef = this.modalService.open(VisualizzaVerificheComponent, { size: 'xl' });
    modalRef.componentInstance.numeroDomanda = content.numeroDomanda;
  }

  checkSelezione() {
    let c: number = 0;
    this.dataListaRichieste.data.forEach(element => {
      if (element.checked) {
        c++;
      }
    })
    if (c == this.dataListaRichieste.data.length) {
      this.selezionaTutto = true;
    } else {
      this.selezionaTutto = false;
    }
  }

  controlloSelezione() {
    let c: number = 0;
    this.dataListaRichieste.data.forEach(element => {
      if (element.checked) {
        c++;
      }
    })
    if (c == 0) {
      return true;
    }
    return false;
  }

  checkDisabled(element: DomandeAperte, azione: string): boolean {
    if (this.dataListaRichieste != null && this.dataListaRichieste != undefined) {
      switch (azione) {
        case 'PRESA-CARICO': {
          let c: number = 0;
          this.dataListaRichieste.data.forEach(value => {
            if (!value.checked) {
              c++;
            }
          })
          if (this.dataListaRichieste.data.length == c) {
            return true;
          }
          if (this.dataListaRichieste.data.some(elem => elem.checked)) {
            let filteredList = this.dataListaRichieste.data.filter(elem => elem.checked);
            filteredList = filteredList.filter(domanda => (domanda) && !(
              domanda.stato.codStato == STATO_DOMANDA.INVIATA || domanda.stato.codStato == STATO_DOMANDA.RETTIFICATA));
            return filteredList.length > 0;
          }
          else {
            let domandaSelected = element ? element : null;
            return (domandaSelected.stato.codStato == STATO_DOMANDA.INVIATA || domandaSelected.stato.codStato == STATO_DOMANDA.RETTIFICATA);
          }
        }
        default:
          return false;
      }
    }
  }

  presaInCarico(element: DomandeAperte) {
    let messaggio: string;
    if (element === null) {
      messaggio = `Le domande selezionate verranno prese in carico.`;
    }
    else {
      messaggio = `Le domande selezionate verranno prese in carico.`;
    }

    const dialogRef = this.dialog.open(CustomDialogComponent, {
      width: '650px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Conferma Presa in carico', messaggio: messaggio }
    });
    dialogRef.afterClosed().subscribe((result: boolean) => {
      if (result) {
        this.client.spinEmitter.emit(true);
        let modelPresaCarico: PresaInCaricoModel[] = [];
        if (this.dataListaRichieste.data.some(elem => elem.checked)) {
          const filteredList = this.dataListaRichieste.data.filter(elem => elem.checked);
          filteredList.forEach(elem => {
            let model: PresaInCaricoModel = new PresaInCaricoModel(elem.numeroDomanda, elem.stato.codStato);
            modelPresaCarico.push(model);
          });
        } else {
          let model: PresaInCaricoModel = new PresaInCaricoModel(element.numeroDomanda, element.stato.codStato);
          modelPresaCarico.push(model);
        }

        this.client.presaInCarico(modelPresaCarico).subscribe(res => {
          if (res) {
            this.client.spinEmitter.emit(false);
            this.avviaRicerca();
          } else {
            this.client.spinEmitter.emit(false);
          }
        })
      } else {
        this.client.spinEmitter.emit(false);
      }
    })
  }



  updateInput() {
    const input = document.getElementById('enteGestore') as HTMLInputElement;
    const div = document.getElementById('divEnteGestore') as HTMLDivElement;

    //Emulo la stringa caratteri a schermo
    const canvasContext = document.createElement('canvas').getContext('2d');
    canvasContext.font = window.getComputedStyle(input).getPropertyValue('font');

    const textLength = canvasContext.measureText(input.value).width;


    //Controllo lunghezza caratteri
    if (textLength > 260) {
      div.className = "col-md-6 divCol6";
    }
    else {
      div.className = "col-md-3 divCol3";
    }
  }

  nuovoSportello() {
    const modalRef = this.modalService.open(PopupNuovoSportelloComponent, { size: 'lg' });
  }
}
