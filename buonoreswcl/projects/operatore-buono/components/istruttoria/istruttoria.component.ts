import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { forkJoin } from 'rxjs';
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
import { VisualizzaVerificheComponent } from '../visualizza-verifiche/visualizza-verifiche.component';
import { ModelVerifiche } from '@buonores-app/app/dto/ModelVerifiche';
import { VisualizzaCronologiaComponent } from '../visualizza-cronologia/visualizza-cronologia.component';

@Component({
  selector: 'app-istruttoria',
  templateUrl: './istruttoria.component.html',
  styleUrls: ['./istruttoria.component.css']
})
export class IstruttoriaComponent implements OnInit, AfterViewInit, OnDestroy {

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
  displayedColumns: string[] = ['numeroDomanda', 'Destinatario', 'Richiedente', 'codStato', 'dataDomanda1', 'decessoResidenza', 'enteGestore', 'statoEsitoDataVerifica', "dateDecorrenzaBuono", 'azione'];
  // displayedColumns: string[] = ['numeroDomanda', 'cfDestinatario', 'cfRichiedente', 'codStato', 'dataDomanda1', 'enteGestore', "verificaEnte", 'dataVerifica', 'esitoVerifica', "dataDecorrenzaBuono", "dataFineBuono", 'azione'];
  dataListaRichieste: MatTableDataSource<DomandeAperte>;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;



  selezionaTutto: boolean = false;
  disabilitaAvviaVerifica: boolean = true;
  // Parametro per la renderizzazione del pulsante Home
  param: boolean = false;
  stati: StatiBuonores[];
  constructor(private fb: FormBuilder, private router: Router, private dialog: MatDialog, private route: ActivatedRoute,
    private modalService: NgbModal, public client: BuonoresBOClient, public toastService: AppToastService) { }

  getProperty = (obj, path) => (
    path.split('.').reduce((o, p) => o && o[p], obj)
  )

  checkUncheckAll() {
    let c: number = 0;
    this.dataListaRichieste.data.forEach(element => {
      if (!element.checked && element.dataVerificaBuono)
        c = 1;
    })
    if (c > 0) {
      this.dataListaRichieste.data.forEach(element => {
        if (element.dataVerificaBuono)
          element.checked = true;
      })
    } else {
      this.dataListaRichieste.data.forEach(element => {
        if (element.dataVerificaBuono)
          element.checked = false;
      })
    }
    this.checkDisabled();
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
        statoVerifica: [],
        formStatoVerificaEnteGestore: [],
        formVerificaEnteEffettuata: [],
        dataVerificaA: [],
        formDecessoResidenza: [],
      });
      forkJoin({
        statiDomanda: this.client.getStati(SECTION.RICERCA_DOMANDE_APERTE),
        listaSportelli: this.client.getSportelli(),
      })
        .subscribe(({ statiDomanda, listaSportelli }) => {

          this.client.listaStatiSalvato = statiDomanda;
          this.client.listaSportelliSalvato = listaSportelli;

          this.searchForm = this.fb.group({
           // codSportello: [this.client.listaSportelliSalvato.sportelloCorrente.codSportello, [Validators.required]],
            codSportello: [],
            numeroDomanda: [],
            statoDomanda: ['IN_PAGAMENTO'],
            destinatario: [],
            richiedente: [],
            statoVerifica: [],
            formStatoVerificaEnteGestore: [],
            formVerificaEnteEffettuata: [],
            dataVerificaA: [],
            formDecessoResidenza: [],
          });
          this.client.spinEmitter.emit(false);
        },
          err => {
            this.client.spinEmitter.emit(false)
          }
        );
      this.dataListaRichieste = new MatTableDataSource<DomandeAperte>();
      this.dataListaRichieste.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
      this.dataListaRichieste.sort = this.sort;
      this.getIdEntiGestori();
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
          statoVerifica: [this.client.filtroDomandeAperte.statoVerificaEnteGestore],
          formStatoVerificaEnteGestore: [this.client.filtroDomandeAperte.verificaEnteGestore],
          formVerificaEnteEffettuata: [this.client.filtroDomandeAperte.verificaEnteGestore],
          dataVerificaA: [this.client.filtroDomandeAperte.dataVerificaA],
          formDecessoResidenza: [this.client.filtroDomandeAperte.decessoResidenza],
        });
        this.checkSelezione();
      }
      else {
        this.dataListaRichieste = new MatTableDataSource<DomandeAperte>();
        this.searchForm = this.fb.group({
          codSportello: [],
          numeroDomanda: [],
          statoDomanda: ['IN_PAGAMENTO'],
          destinatario: [],
          richiedente: [],
          statoVerifica: [],
          formStatoVerificaEnteGestore: [],
          formVerificaEnteEffettuata: [],
          dataVerificaA: [],
          formDecessoResidenza: [],
        });
        this.dataListaRichieste.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
        this.dataListaRichieste.sort = this.sort;
      }
      this.getIdEntiGestori();
    }
  }



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

  // Ottengo solo gli Id degli EntiGestori
  getIdEntiGestori() {
    this.client.listaIdEntiGestori = this.client.utenteloggato.listaEntiGestore.map(ente => ente.idEnteGestore);
    if (this.client.listaIdEntiGestori.length == 0)
      this.client.listaIdEntiGestori.push(0);
  }

  avviaRicerca() {
    this.client.spinEmitter.emit(true);
    this.client.righePerPagina = 10;
    this.client.paginaSalvata = 0;
    this.client.ordinamentoSalvato = null;
    this.client.versoSalvato = null;
    let codSportello = this.searchForm.controls.codSportello.value && this.searchForm.controls.codSportello.value != "" ? this.searchForm.controls.codSportello.value : null;
    if (codSportello === "") {
      codSportello = null;
    }
    const numeroDomanda = this.searchForm.controls.numeroDomanda.value && this.searchForm.controls.numeroDomanda.value.toUpperCase();
    const statoDomanda = this.searchForm.controls.statoDomanda.value && this.searchForm.controls.statoDomanda.value != "" ? this.searchForm.controls.statoDomanda.value : null;
    const destinatario = this.searchForm.controls.destinatario.value && this.searchForm.controls.destinatario.value.toUpperCase();

    const richiedente = this.searchForm.controls.richiedente.value && this.searchForm.controls.richiedente.value;
    let statoVerifica = this.searchForm.controls.statoVerifica.value && this.searchForm.controls.statoVerifica.value != "" ? this.searchForm.controls.statoVerifica.value : null;
    let verificaEnteEffettuata = this.searchForm.controls.formStatoVerificaEnteGestore.value && this.searchForm.controls.formStatoVerificaEnteGestore.value != "" ? this.searchForm.controls.formStatoVerificaEnteGestore.value : null;

    let esitoVerificaEnteGestore = null;
    let dataVerificaA = this.searchForm.controls.dataVerificaA.value;
    let dataItaliana = null;
    let dataFormattata = null;
    if (dataVerificaA != null && dataVerificaA != '') {
      dataItaliana = new Date(dataVerificaA);
      dataFormattata = dataItaliana.toLocaleDateString("it-IT");
    }
    let decessoResidenza = this.searchForm.controls.formDecessoResidenza.value && this.searchForm.controls.formDecessoResidenza.value != "" ? this.searchForm.controls.formDecessoResidenza.value : null;
    const ricerca = new FiltriDomandeAperte(codSportello, numeroDomanda, statoDomanda, destinatario, richiedente, SECTION.RICERCA_DOMANDE_APERTE, this.client.listaIdEntiGestori, statoVerifica, verificaEnteEffettuata, esitoVerificaEnteGestore, null, dataFormattata, decessoResidenza);
    this.client.filtroDomandeAperte = new FiltriDomandeAperte(codSportello, numeroDomanda, statoDomanda, destinatario, richiedente, SECTION.RICERCA_DOMANDE_APERTE, this.client.listaIdEntiGestori, statoVerifica, verificaEnteEffettuata, esitoVerificaEnteGestore, null, dataVerificaA, decessoResidenza);
    this.client.ricercaDomandeAperte(ricerca).subscribe((response: DomandeAperte[]) => {
      response.forEach(element => {
        element.checked = false;
      })
      this.client.spinEmitter.emit(false);
      this.dataListaRichieste.data = response as any;
      this.client.ricercaDomAperte = response as any;
      this.dataListaRichieste.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
      this.dataListaRichieste.sort = this.sort;
      this.dataListaRichieste.paginator = this.paginator;
      this.ricercaEffettuata = true;
      this.ngAfterViewInit();
      this.checkSelezione();

      // Imposta un ordinamento di default se necessario
      let initialSort: Sort = this.dataListaRichieste.sort;
      initialSort.direction = 'asc';
      initialSort.active = 'Destinatario';
      this.sortData(initialSort);
    },
    err => {
      this.client.spinEmitter.emit(false);
    });
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
          return this.compareString(a.cognomeDestinatario, b.cognomeDestinatario, isAsc);
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

  compareString(a: string, b: string, isAsc: boolean) {
    let rigthA = '';
    let rigthB = '';
    rigthA = a.replace(/\s+/g, '');
    rigthB = b.replace(/\s+/g, '');
    return (rigthA < rigthB ? -1 : 1) * (isAsc ? 1 : -1);
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
      statoDomanda: ['IN_PAGAMENTO'],
      destinatario: [],
      richiedente: [],
      statoVerifica: [],
      formStatoVerificaEnteGestore: [],
      ultimaDataVerifica: [],
      formDecessoResidenza: [],
    });
    // Reset impostazioni di default del paginator
    this.client.ricercaDomAperte = [];
    this.dataListaRichieste.data = [];
    this.ricercaEffettuata = false;
  }

  openVerifiche(content: DomandeAperte) {

    const modalRef = this.modalService.open(VisualizzaVerificheComponent, { size: 'xl' });
    modalRef.componentInstance.numeroDomanda = content.numeroDomanda;
  }

  checkSelezione() {
    let c: number = 0;
    this.dataListaRichieste.data.forEach(element => {
      if (!element.checked && element.dataVerificaBuono)
        c = 1;
    })
    if (c > 0) {
      this.dataListaRichieste.data.forEach(element => {
        if (element.dataVerificaBuono)
          this.selezionaTutto = false;
      })
    } else {
      this.dataListaRichieste.data.forEach(element => {
        if (element.dataVerificaBuono)
          this.selezionaTutto = true;
      })
    }
    this.checkDisabled();
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

  checkDisabled() {
    if (this.dataListaRichieste != null && this.dataListaRichieste != undefined) {
      let controllo = false
      this.dataListaRichieste.data.forEach(element => {
        if (element.checked && element.dataVerificaBuono) {
          controllo = true;
          this.disabilitaAvviaVerifica = false;
        }
      })
      if (!controllo) {
        this.disabilitaAvviaVerifica = true;
      }
      if(this.dataListaRichieste.data.some(element => element.stato.codStato == STATO_DOMANDA.RINUNCIATA && element.checked)){
        this.disabilitaAvviaVerifica = true;
      }
    } else {
      this.disabilitaAvviaVerifica = true;
    }
  }

  avviaVerifica() {
    const dialogRef = this.dialog.open(CustomDialogComponent, {
      width: '650px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Conferma Avvia verifica', messaggio: 'Le domande selezionate verranno prese in carico' }
    });
    dialogRef.afterClosed().subscribe((result: boolean) => {
      if (result) {
        this.client.spinEmitter.emit(true);
        let idDomandeTest: string[] = [];

        if (this.dataListaRichieste.data.some(elem => elem.checked)) {
          const filteredList = this.dataListaRichieste.data.filter(elem => elem.checked);
          filteredList.forEach(elem => {
            idDomandeTest.push(elem.numeroDomanda);
          });
          this.client.updateToVerificaInCorso(idDomandeTest).subscribe(res => {
            if (res) {
              this.avviaRicerca();
            } else {
              this.client.spinEmitter.emit(false);
            }
          },
            err => {
              this.client.spinEmitter.emit(false);
            });

        } else {
          this.client.spinEmitter.emit(false);
        }
      }
    })
  }

  checkAzione(azione: string): boolean {
    switch (azione) {
      case "verifiche":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_VisualizzaVerifiche);
      case "avvia_verifica":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_Avvia_Verifica_Ente);
      default:
        return false;
    }
  }
}
