import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatPaginator, MatTableDataSource, PageEvent } from '@angular/material';
import { MatSort, Sort } from '@angular/material/sort';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { BuonoresBOClient } from '@buonores-app/app/BuonoresBOClient';
import { DomandeAperte } from '@buonores-app/app/dto/DomandeAperte';
import { FiltriDomandeAperte } from '@buonores-app/app/dto/FiltriDomandeAperte';
import { SECTION } from '@buonores-app/constants/buonores-constants';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { forkJoin } from 'rxjs';
import { VisualizzaCronologiaComponent } from '../visualizza-cronologia/visualizza-cronologia.component';
import { VisualizzaVerificheComponent } from '../visualizza-verifiche/visualizza-verifiche.component';


@Component({
  selector: 'app-archivio-domande',
  templateUrl: './archivio-domande.component.html',
  styleUrls: ['./archivio-domande.component.css']
})
export class ArchivioDomandeComponent implements OnInit, OnDestroy {
  searchForm: FormGroup;  //FORM
  ricercaEffettuata: boolean = false; // RICERCA
  // MAT PAGINATOR
  displayedColumns: string[] = ['numeroDomanda', 'cfDestinatario', 'cfRichiedente', 'codStato', 'dataDomanda1', 'azione'];
  dataSource: MatTableDataSource<DomandeAperte>;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  sortedData: DomandeAperte[];
  selezionaTutto: boolean = false;
  righePerPagina: number;

  constructor(private fb: FormBuilder, public client: BuonoresBOClient,
              private route: ActivatedRoute, private router: Router,
              private modalService: NgbModal
  ) { }



  ngOnInit() {
    // Se non sono ancora stati presi gli stati dal db
    if (this.client.listaStatiSalvatoArchivio.length == 0) {
      this.client.spinEmitter.emit(true); //Spinner loading....
      // Creo il form
      this.searchForm = this.fb.group({
        codSportello: new FormControl(Validators.required),
        numeroDomanda: [],
        statoDomanda: [],
        destinatario: [],
        richiedente: [],
      });

      //-----------------------------------------Init liste in Client
      forkJoin({
        // Ottengo gli Observe
        statiDomanda: this.client.getStati(SECTION.RICERCA_ARCHIVIO),
        listaSportelli: this.client.getSportelli(),
      }).subscribe(({ statiDomanda, listaSportelli }) => {
        this.client.listaStatiSalvatoArchivio = statiDomanda;
        this.client.listaSportelliSalvatoArchivio = listaSportelli;
        this.client.spinEmitter.emit(false); //Sprinner stop....
        // Aggiorno il form
        this.searchForm = this.fb.group({
          codSportello: [this.client.listaSportelliSalvatoArchivio.sportelloCorrente.codSportello, [Validators.required]],
          numeroDomanda: [],
          statoDomanda: [],
          destinatario: [],
          richiedente: [],
        });
      },
      err => {
        this.client.spinEmitter.emit(false)
      }
      );
      //------------------------------------------Table and paginator
      this.dataSource = new MatTableDataSource<DomandeAperte>();
      this.dataSource.sort = this.sort;
      this.dataSource.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
    } else {
      // Nel caso in cui siano gi� state effettuate delle ricerche
      if (this.client.ricercaDomArchivio.length > 0) {
        this.ricercaEffettuata = true;
        // Aggiorno il form
        this.searchForm = this.fb.group({
          codSportello: [this.client.filtroDomandeArchivio.codSportello, [Validators.required]],
          numeroDomanda: [this.client.filtroDomandeArchivio.numeroDomanda],
          statoDomanda: [this.client.filtroDomandeArchivio.statoDomanda],
          destinatario: [this.client.filtroDomandeArchivio.destinatario],
          richiedente: [this.client.filtroDomandeArchivio.richiedente],
        });
        // Set dello stato precedente del paginator
        this.dataSource = new MatTableDataSource<DomandeAperte>();
        this.dataSource.data = this.client.ricercaDomArchivio as DomandeAperte[];
        this.sort.active = this.client.ordinamentoSalvatoArchivio;
        this.sort.direction = this.client.versoSalvatoArchivio;
        this.dataSource.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
        this.dataSource.sort = this.sort;
        this.paginator.pageSize = this.client.righePerPaginaArchivio;
        this.paginator.pageIndex = this.client.paginaSalvataArchivio;
        this.dataSource.paginator = this.paginator;
      } else { // Nel caso in cui non siano ancora state effettuate ricerche
        // Creo il form
        this.searchForm = this.fb.group({
          codSportello: [this.client.listaSportelliSalvatoArchivio.sportelloCorrente.codSportello, [Validators.required]],
          numeroDomanda: [],
          statoDomanda: [],
          destinatario: [],
          richiedente: [],
        });
        this.dataSource = new MatTableDataSource<DomandeAperte>();
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
      }
    }
  }

  // Funzione per ottenere le diverse propriet� di un oggetto
  getProperty = (obj, path) => (
    path.split('.').reduce((o, p) => o && o[p], obj)
  )

  // Avvio la ricerca ed ottengo i dati
  avviaRicerca() {
    this.client.spinEmitter.emit(true);
    // Costruisco l'oggetto per la ricerca delle domande
    const codSportello = this.searchForm.controls.codSportello.value && this.searchForm.controls.codSportello.value != "" ? this.searchForm.controls.codSportello.value : null;
    const numeroDomanda = this.searchForm.controls.numeroDomanda.value && this.searchForm.controls.numeroDomanda.value.toUpperCase();
    const statoDomanda = this.searchForm.controls.statoDomanda.value && this.searchForm.controls.statoDomanda.value != "" ? this.searchForm.controls.statoDomanda.value : null;
    const destinatario = this.searchForm.controls.destinatario.value && this.searchForm.controls.destinatario.value.toUpperCase();
    const richiedente = this.searchForm.controls.richiedente.value && this.searchForm.controls.richiedente.value.toUpperCase();
    const ricerca = new FiltriDomandeAperte(codSportello, numeroDomanda, statoDomanda, destinatario, richiedente, SECTION.RICERCA_ARCHIVIO, null, null, null, null, null, null, null);
    this.client.filtroDomandeArchivio = ricerca;
    //Effettuo la chiamata
    this.client.ricercaDomandeAperte(ricerca).subscribe((response: DomandeAperte[]) => {
      this.client.spinEmitter.emit(false);
      response.forEach(element => {
        element.checked = false;
      })
    this.client.ricercaDomArchivio = response as DomandeAperte[];
    // Reset impostazioni di default del paginator
    this.dataSource.data = response as DomandeAperte[];  // Inizializzo il MatTablePaginator con i dati ricevuti
    this.resetImpPaginator();
    this.ricercaEffettuata = true;
    },
    err => {
      this.client.spinEmitter.emit(false);
    });
  }

  // Paginator arguments
  // - pageSize: il numero di elementi da visualizzare in una singola pagina.
  // - pageIndex: l'indice della pagina attualmente selezionata.
  // -pageSizeChange: un evento emesso quando la dimensione della pagina selezionata viene modificata.
  // MatSort arguments
  // - active: il nome della colonna correntemente selezionata per la visualizzazione in ordine.
  // - direction: la direzione di ordinamento attuale, "asc" o "desc".
  // MatTableDatasource arguments
  // - paginator: un'istanza di MatPaginator che viene utilizzata per visualizzare i dati in pagine.
  // - sort: un'istanza di MatSort che viene utilizzata per ordinare i dati nella tabella.

  ngOnDestroy(): void {
    this.client.paginaSalvataArchivio = this.paginator.pageIndex;
    this.client.ordinamentoSalvatoArchivio = this.sort.active;
    this.client.versoSalvatoArchivio = this.sort.direction;
    this.client.righePerPaginaArchivio = this.paginator.pageSize;
  }

  // Annullare la ricerca
  annullaRicerca() {
    this.searchForm.reset();  // Azzero il form
     // Creo il form
     this.searchForm = this.fb.group({
      codSportello: [this.client.listaSportelliSalvatoArchivio.sportelloCorrente.codSportello, [Validators.required]],
      numeroDomanda: [],
      statoDomanda: [],
      destinatario: [],
      richiedente: [],
    });
    // Reset impostazioni di default del paginator
    this.client.ricercaDomArchivio = [];
    this.dataSource.data = [];  // Elimino i dati dal MatTablePaginator
    this.resetImpPaginator();
    this.ricercaEffettuata = false;
  }

  resetImpPaginator() {
    this.paginator.pageSize = 10;
    this.client.righePerPaginaArchivio = this.paginator.pageSize;
    this.paginator.pageIndex = 0;
    this.client.paginaSalvataArchivio = this.paginator.pageIndex;
    this.paginator.firstPage();
    this.sort.active = '';
    this.sort.direction = '';
    this.dataSource.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
    this.dataSource.sort = this.sort; // Allows the user to sort the data in the table by clicking on the column headers
    this.dataSource.paginator = this.paginator; // Setto la proprieta' del paginatorSource a quella del paginatorComponent
  }


  sortData(sort: Sort) {
    const data = this.dataSource.data.slice();
    if (!sort.active || sort.direction === '') {
      this.sortedData = data;
      return;
    }

  this.sortedData = data.sort((a, b) => {
    const isAsc = sort.direction === 'asc';
    switch (sort.active) {
      case 'numeroDomanda':
        return this.compare(a.numeroDomanda, b.numeroDomanda, isAsc);
      case 'cfDestinatario':
        return this.compare(a.cfDestinatario, b.cfDestinatario, isAsc);
      case 'cfRichiedente':
        return this.compare(a.cfRichiedente, b.cfRichiedente, isAsc);
      case 'codStato':
        return this.compare(a.stato.codStato, b.stato.codStato, isAsc);
      case 'dataVerifica':
        // Fix nel caso una delle due date sua nulla
        let dataStringA = a.dataDomanda;
        let dataStringB = b.dataDomanda;
        if(a.dataDomanda === null) {
          if(isAsc !== true) {
            dataStringA = '01/01/1970';
          } else if(isAsc) {
            dataStringA = '20/12/2099';
          }
        }
        if(b.dataDomanda === null) {
          if(isAsc !== true) {
            dataStringB = '01/01/1970';
          } else if(isAsc) {
            dataStringB = '20/12/2099';
          }
        }
        // Confronto le date, le date arrivano come stringhe
        let dateA = new Date(dataStringA.split("/").reverse().join("-"));
        let dateB = new Date(dataStringB.split("/").reverse().join("-"));

        return this.compare(dateA.getTime(), dateB.getTime(), isAsc);
      default:
        return 0;
    }
  });
  this.dataSource.data = this.sortedData;
  this.client.ricercaDomArchivio = this.sortedData;
  }

  compare(a: number | string, b: number | string, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }

  //--------------------------------------------AZIONI
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
  // Open cronologia per ogni la domanda nella ricerca
  openCronologia(content: DomandeAperte) {
    const modalRef = this.modalService.open(VisualizzaCronologiaComponent, { size: 'xl' });
    modalRef.componentInstance.numeroDomanda = content.numeroDomanda;
  }

  // Open veriche per ogni la domanda nella ricerca
  openVerifiche(content: DomandeAperte) {
    const modalRef = this.modalService.open(VisualizzaVerificheComponent, { size: 'xl' });
    modalRef.componentInstance.numeroDomanda = content.numeroDomanda;
  }
  //--------------------------------------------------

}

