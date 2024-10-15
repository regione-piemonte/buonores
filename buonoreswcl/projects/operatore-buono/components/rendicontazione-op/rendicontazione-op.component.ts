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
import { VisualizzaVerificheComponent } from '../visualizza-verifiche/visualizza-verifiche.component';
import { map, startWith } from 'rxjs/operators';
import { ModelVerifiche } from '@buonores-app/app/dto/ModelVerifiche';
import { PopuppopupRevocaBuonoComponent } from '../popup-revoca-buono/popup-revoca-buono.component';
import { ModelRicercaBuono } from '@buonores-app/app/dto/ModelRicercaBuono';
import { FiltriRicercaBuoni } from '@buonores-app/app/dto/FiltriRicercaBuoni';



@Component({
  selector: 'app-rendicontazione-op',
  templateUrl: './rendicontazione-op.component.html',
  styleUrls: ['./rendicontazione-op.component.css']
})
export class RendicontazioneOpComponent implements OnInit, AfterViewInit, OnDestroy {

  tooltipRendicontazione: string = '';

  isChecked: boolean = false;
  isDisabled: boolean = true;

  searchForm: FormGroup;

  denomSelected: string;
  dataSistema = new Date();
  annoSelezionato: number = null;
  statoSelezionato: string = null;
  ricercaEffettuata: boolean = false;
  sortedData: ModelRicercaBuono[];
  displayedColumns: string[] = ['numeroDomanda', 'destinatario', 'richiedente', 'statoBuono', 'dateDecorrenzaBuono', 'decessoResidenza','iseeConforme','iseeVerificaConforme', 'azione'];
  dataListaRichieste: MatTableDataSource<ModelRicercaBuono>;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;

  selezionaTutto: boolean = false;
  // Parametro per la renderizzazione del pulsante Home
  param: boolean = false;

  constructor(private fb: FormBuilder, private router: Router, private dialog: MatDialog, private route: ActivatedRoute,
    private modalService: NgbModal, public client: BuonoresBOClient, public toastService: AppToastService) { }

  ngOnDestroy(): void {
    this.client.paginaSalvataRendicontazione = this.paginator.pageIndex !== undefined ? this.paginator.pageIndex : 1;
    this.client.ordinamentoSalvatoRendicontazione = this.sort.active !== undefined ? this.sort.active : 'asc';
    this.client.versoSalvatoRendicontazione = this.sort.direction !== undefined ? this.sort.direction : 1;
    this.client.righePerPaginaRendicontazione = this.paginator.pageSize !== undefined ? this.paginator.pageSize : 1;
  }

  ngAfterViewInit() {
    if (this.ricercaEffettuata) {
      if (this.client.paginaSalvataRendicontazione !== this.paginator.pageIndex ||
        this.client.righePerPaginaRendicontazione !== this.paginator.pageSize ||
        this.client.ordinamentoSalvatoRendicontazione !== this.sort.active ||
        this.client.versoSalvatoRendicontazione !== this.sort.direction) {
        setTimeout(() => {
          this.paginator.pageSize = this.client.righePerPaginaRendicontazione;
          this.paginator.pageIndex = this.client.paginaSalvataRendicontazione;
          this.sort.active = this.client.ordinamentoSalvatoRendicontazione;
          this.sort.direction = this.client.versoSalvatoRendicontazione;
          this.dataListaRichieste.paginator = this.paginator;
          this.dataListaRichieste.sort = this.sort;
        }, 0);
      }
    } else {
      this.dataListaRichieste.sort = this.sort;
      this.dataListaRichieste.paginator = this.paginator;
    }
  }

  ngOnInit() {
    if (this.client.listaStatiBuonoSalvato.length == 0) {
      this.client.spinEmitter.emit(true);
      this.searchForm = this.fb.group({
        statiBuono: [],
        numeroBuono: [],
        destinatario: [],
        richiedente: [],
        numeroDomanda: [],
        formDecessoResidenza: [],
        iseeConforme: [],
		    iseeVerificaConforme: [],
      });
      forkJoin({
        statiBuono: this.client.getStatiBuono(),
        esitiIsee: this.client.getEsitiIsee(),
      })
        .subscribe(({ statiBuono,esitiIsee }) => {

          this.client.listaStatiBuonoSalvato = statiBuono;
          this.client.listaIseeVerificaConforme = esitiIsee;
          this.searchForm = this.fb.group({
            statiBuono: [],
            numeroBuono: [],
            destinatario: [],
            richiedente: [],
            numeroDomanda: [],
            formDecessoResidenza: [],
            iseeConforme: [],
			      iseeVerificaConforme: [],
          });
          this.client.spinEmitter.emit(false);
        },
          err => {
            this.client.spinEmitter.emit(false)
          }
        );
      this.dataListaRichieste = new MatTableDataSource<ModelRicercaBuono>();
      this.dataListaRichieste.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
      this.dataListaRichieste.sort = this.sort;
    }
    else {
      if (this.client.listaRicercaBuonoSalvato.length > 0) {
        this.ricercaEffettuata = true;
        this.dataListaRichieste = new MatTableDataSource<ModelRicercaBuono>();
        this.dataListaRichieste.data = this.client.listaRicercaBuonoSalvato as any;
        this.dataListaRichieste.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
        this.dataListaRichieste.sort = this.sort;
        this.dataListaRichieste.paginator = this.paginator;
        // Form per riinserire i filtri di ricerca se si cambia tab nell'applicativo
        this.searchForm = this.fb.group({
          statiBuono: [this.client.filtriRicercaBuoni.statoBuono],
          numeroBuono: [this.client.filtriRicercaBuoni.numeroBuono],
          destinatario: [this.client.filtriRicercaBuoni.destinatario],
          richiedente: [this.client.filtriRicercaBuoni.richiedente],
          numeroDomanda: [this.client.filtriRicercaBuoni.numeroDomanda],
          formDecessoResidenza: [this.client.filtriRicercaBuoni.decessoResidenza],
          iseeConforme: [this.client.filtriRicercaBuoni.iseeConforme],
          iseeVerificaConforme: [this.client.filtriRicercaBuoni.iseeVerificaConforme],
        });
        // Fix per checkbox selezionatutto
        this.checkSelezione();
      }
      else {
        this.dataListaRichieste = new MatTableDataSource<ModelRicercaBuono>();
        this.searchForm = this.fb.group({
          statiBuono: [],
          numeroBuono: [],
          destinatario: [],
          richiedente: [],
          numeroDomanda: [],
          formDecessoResidenza: [],
          iseeConforme: [],
			    iseeVerificaConforme: [],
        });
        this.dataListaRichieste.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
        this.dataListaRichieste.sort = this.sort;
      }
    }
  }
  //---------------------------------------------------------------------------------------------------------- RICERCA
  avviaRicerca() {
    this.client.spinEmitter.emit(true);
    this.client.righePerPaginaRendicontazione = 10;
    this.client.paginaSalvataRendicontazione = 0;
    this.client.ordinamentoSalvatoRendicontazione = null;
    this.client.versoSalvatoRendicontazione = null;
    const statoBuono = this.searchForm.controls.statiBuono.value && this.searchForm.controls.statiBuono.value != "" ? this.searchForm.controls.statiBuono.value : null;
    // const numeroBuono = this.searchForm.controls.numeroBuono.value && this.searchForm.controls.numeroBuono.value.toUpperCase();
    const numeroBuono = null;
    const numeroDomanda = this.searchForm.controls.numeroDomanda.value && this.searchForm.controls.numeroDomanda.value.toUpperCase();
    const destinatario = this.searchForm.controls.destinatario.value && this.searchForm.controls.destinatario.value.toUpperCase();
    const richiedente = this.searchForm.controls.richiedente.value && this.searchForm.controls.richiedente.value.toUpperCase();
    let decessoResidenza = this.searchForm.controls.formDecessoResidenza.value && this.searchForm.controls.formDecessoResidenza.value != "" ? this.searchForm.controls.formDecessoResidenza.value : null;

  	let iseeConforme = this.searchForm.controls.iseeConforme.value && this.searchForm.controls.iseeConforme.value != "" ? this.searchForm.controls.iseeConforme.value : null;
	  let iseeVerificaConforme = this.searchForm.controls.iseeVerificaConforme.value && this.searchForm.controls.iseeVerificaConforme.value != "" ? this.searchForm.controls.iseeVerificaConforme.value : null;
    const ricerca = new FiltriRicercaBuoni(statoBuono, numeroBuono, richiedente, destinatario, numeroDomanda, decessoResidenza, iseeConforme, iseeVerificaConforme);
    this.client.filtriRicercaBuoni = ricerca;
    this.client.ricercaBuoni(ricerca).subscribe((response: ModelRicercaBuono[]) => {
      this.client.spinEmitter.emit(false);
      response.forEach(element => {
        element.checked = false;
      })
      this.dataListaRichieste.data = response as any;
      this.client.listaRicercaBuonoSalvato = response as any;
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

  annullaRicerca() {
    this.searchForm.reset();
    // Creo il form
    this.searchForm = this.fb.group({
      statiBuono: [],
      numeroBuono: [],
      destinatario: [],
      richiedente: [],
      numeroDomanda: [],
      formDecessoResidenza: [],
      iseeConforme: [],
      iseeVerificaConforme: [],
    });
    // Reset impostazioni di default del paginator
    this.client.listaRicercaBuonoSalvato = [];
    this.dataListaRichieste.data = [];
    this.ricercaEffettuata = false;
  }

  //---------------------------------------------------------------------------------------------------------- ACTIONS

  openVerifiche(content: DomandeAperte) {
    // const modalRef = this.modalService.open(VisualizzaVerificheComponent, { size: 'xl' });
    // modalRef.componentInstance.numeroDomanda = content.numeroDomanda;
  }

  richiestaRevoca(notaPerRevoca: string) {
    const modalRef = this.modalService.open(PopuppopupRevocaBuonoComponent, { size: 'lg' });
    modalRef.componentInstance.notaPerRevoca = notaPerRevoca;
  }

  //---------------------------------------------------------------------------------------------------------- UTILS
  convertiNumeroInItaliano(numero: number): string {
    const numeroItaliano = Number(numero).toLocaleString('it-IT');
    return numeroItaliano;
  }

  getProperty = (obj: ModelRicercaBuono, path: string) => (
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

  checkAzione(azione: string): boolean {
    switch (azione) {
      case "verifiche":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_VisualizzaVerifiche);
      case "OP-RevocaBuono":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_RevocaBuono);
      case "OP-BuonoDettaglio":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_BuonoDettaglio);
      default:
        return false;
    }
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
        case 'numeroBuono':
          return this.compare(a.buonoCod, b.buonoCod, isAsc);
        case 'numeroDomanda':
          return this.compare(a.numeroDomanda, b.numeroDomanda, isAsc);
        case 'statoBuono':
          return this.compare(a.buonoStatoCod, b.buonoStatoCod, isAsc);
        case 'destinatario':
          return this.compare(a.nomeDestinatario, b.nomeDestinatario, isAsc);
        case 'richiedente':
          return this.compare(a.nomeRichiedente, b.nomeRichiedente, isAsc);
        default:
          return 0;
      }
    });
    this.dataListaRichieste.data = this.sortedData;
    this.client.listaRicercaBuonoSalvato = this.sortedData;
  }

  compare(a: number | string, b: number | string, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }

  routeTo(from: string, content: ModelRicercaBuono, path: string) {
    this.client.paginaSalvataRendicontazione = this.paginator.pageIndex
    this.client.ordinamentoSalvatoRendicontazione = this.sort.active;
    this.client.versoSalvatoRendicontazione = this.sort.direction;
    this.client.righePerPaginaRendicontazione = this.paginator.pageSize;
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      skipLocationChange: true,
      state: {
        buonoCod: content.buonoCod,
        numeroDomanda: content.numeroDomanda,
 		    statoBuono: content.buonoStatoCod,
        paginaRitorno: from
      }
    };
    this.router.navigate([path], navigationExtras);
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

  checkDisabled(element: ModelRicercaBuono): boolean {
    if (this.dataListaRichieste != null && this.dataListaRichieste != undefined) {
      let c: number = 0;
      this.dataListaRichieste.data.forEach(value => {
        if (!value.checked) {
          c++;
        }
      })
      if (this.dataListaRichieste.data.length <= c) {
        return true;
      }
    }
  }
}
