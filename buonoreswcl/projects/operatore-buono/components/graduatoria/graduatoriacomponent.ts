import { EventEmitter } from '@angular/core';
import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatPaginator, MatSort, MatTableDataSource, Sort } from '@angular/material';
import { BuonoresBOClient } from '@buonores-app/app/BuonoresBOClient';
import { ModelDescrizioneGraduatoria } from '@buonores-app/app/dto/ModelDescrizioneGraduatoria';
import { ModelDomandeGraduatoria } from '@buonores-app/app/dto/ModelDomandeGraduatoria';
import { ModelParametriFinanziamento } from '@buonores-app/app/dto/ModelParametriFinanziamento';
import { ModelVerifiche } from '@buonores-app/app/dto/ModelVerifiche';
import { Sportello } from '@buonores-app/app/dto/Sportello';
import { AZIONE } from '@buonores-app/constants/buonores-constants';
import { ExcelService } from '@buonores-app/services/excel-service';
import { CustomDialogComponent } from '@buonores-app/shared/custom-dialog/custom-dialog.component';
import { AppToastService } from '@buonores-shared/toast/app-toast.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PopupNuovaGraduatoriaComponent } from '../popup-nuova-graduatoria/popup-nuova-graduatoria';


@Component({
  selector: 'app-graduatoria',
  templateUrl: './graduatoria.component.html',
  styleUrls: ['./graduatoria.component.css'],
})
export class GraduatoriaComponent implements OnInit, OnDestroy, AfterViewInit {

  // MATPAGINATOR
  //displayedColumns: string[] = ['check', 'posizioneGraduatoria', 'numeroDomanda', 'destinatario', 'punteggioSociale', 'isee', 'dataInvioDomanda', 'statoDomanda', 'statoVerificaEnte', 'areaInterna', 'importoTotale'];
  displayedColumns: string[] = [ 'posizioneGraduatoria', 'numeroDomanda', 'destinatario', 'punteggioSociale', 'isee', 'dataInvioDomanda', 'statoDomanda', 'statoVerificaEnte', 'areaInterna', 'importoTotale'];
  listaDomandeGraduatoria: MatTableDataSource<ModelDomandeGraduatoria>;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  // listaDomandeGraduatoria: ModelDomandeGraduatoria[] = [];
  selezionaTutto: boolean;
  sortedData: ModelDomandeGraduatoria[];
  //Ultimo sportello
  ultimoSportello: Sportello = null;
  //Tabella Criteri
  displayedColumnsCriteri: string[] = ['AREE', 'DOMANDE IN GRADUATORIA', 'DOMANDE FINANZIABILI', 'IMPORTO RESIDUO/AREE','IMPORTO RESIDUO AREA NON INTERNA'];
  // Check Stati Graduatoria
  checkStatoGraduatoriaProvvisoria: boolean = null;
  checkStatoGraduatoriaAggiornamento: boolean = null;
  checkStatoGraduatoriaPubblicazione: boolean = null;

  importoTotaleRicevuto: number = 0;
  listaSportelli :Sportello[]=[];
  sportelloSelezionato:Sportello=null;
  constructor(private modalService: NgbModal, public client: BuonoresBOClient, public appToastService: AppToastService, private dialog: MatDialog, private excelService:ExcelService) {
  }

  ngAfterViewInit(): void {
    this.client.spinEmitter.emit(true);
    this.client.getUltimoSportelloChiuso().subscribe(
      (response) => {
        this.checkStatoGraduatoria('PROVVISORIA');
        this.checkStatoGraduatoria('PUBBLICATA');
        this.checkStatoGraduatoria('DA_AGGIORNARE');

      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }

  ngOnDestroy(): void {
    this.client.paginaSalvataGraduatoria = this.paginator.pageIndex;
    this.client.ordinamentoSalvatoGraduatoria = this.sort.active;
    this.client.versoSalvatoGraduatoria = this.sort.direction;
    this.client.righePerPaginaGraduatoria = this.paginator.pageSize;
  }

  ngOnInit(): void {
    this.client.spinEmitter.emit(true);
    this.caricaSportelli();
    this.client.getUltimoSportelloChiuso().subscribe(
      (response) => {
        if (response) {
          this.ultimoSportello = response as Sportello;
          this.client.controlloEsistenzaGraduatoria(this.ultimoSportello.codSportello).subscribe(
            (response) => {
              this.client.checkEsistenzaGraduatoria = response as boolean;
              if (this.client.checkEsistenzaGraduatoria) {
                if (this.client.ricercaDomGraduatoria.length > 0) {
                  this.getDomandeGraduatoria();
                  this.getParametriFinanziamento();
                  this.getDescrizioneGraduatoria();
                  this.checkStatoGraduatoria('PROVVISORIA');
                  this.checkStatoGraduatoria('PUBBLICATA');
                  this.checkStatoGraduatoria('DA_AGGIORNARE');
                  this.checkPubblicazioneGraduatoria();
                  this.ricreaPaginator();

                  // FIX CHEKBOX table header
                  // this.checkSelezione();
                } else {
                  // --------------------------

                  this.client.getUltimoSportelloChiuso().subscribe(
                    (response) => {
                      if (response) {
                        this.ultimoSportello = response as Sportello;
                        this.getDomandeGraduatoria();
                        this.getParametriFinanziamento();
                        this.getDescrizioneGraduatoria();
                        this.checkStatoGraduatoria('PROVVISORIA');
                        this.checkStatoGraduatoria('PUBBLICATA');
                        this.checkStatoGraduatoria('DA_AGGIORNARE');
                        this.checkPubblicazioneGraduatoria();

                        this.client.spinEmitter.emit(false);
                      }
                    },
                    err => {
                      this.client.spinEmitter.emit(false);
                    }
                  );

                  this.ricreaPaginator();

                }
              }
              this.client.spinEmitter.emit(false);
            },
            err => {
              this.client.spinEmitter.emit(false);
            }
          );
          this.client.spinEmitter.emit(false);
        }
      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }


  onSportelloChange( ){
  this.ultimoSportello=this.sportelloSelezionato;
   this.client.spinEmitter.emit(true);
   this.client.controlloEsistenzaGraduatoria( this.ultimoSportello.codSportello).subscribe(
    (response) => {

      this.client.checkEsistenzaGraduatoria = response as boolean;
      if (this.client.checkEsistenzaGraduatoria) {
          this.getDomandeGraduatoria();
          this.getParametriFinanziamento();
          this.getDescrizioneGraduatoria();
          this.checkStatoGraduatoria('PROVVISORIA');
          this.checkStatoGraduatoria('PUBBLICATA');
          this.checkStatoGraduatoria('DA_AGGIORNARE');
          this.checkPubblicazioneGraduatoria();
          this.ricreaPaginator();

          // FIX CHEKBOX table header
          // this.checkSelezione();

      }
      this.client.spinEmitter.emit(false);
    },
    err => {
      this.client.spinEmitter.emit(false);
    }
  );


  }
  caricaSportelli():void{
    this.client.getsportellichiusi().subscribe(
      (res) =>{
        this.listaSportelli=res;
        if(this.listaSportelli && this.listaSportelli.length>0){
          this.sportelloSelezionato=this.listaSportelli[0];
        }
      },
      (err) =>{
        this.listaSportelli=null
      }
    )
  }

  // -----------------------------------------------------------------CHECK
  checkAzioni(azione: string): boolean {
    if (azione == AZIONE.OP_NuovaGraduatoria) {
      return this.client.azioni.some(a => a.codAzione == AZIONE.OP_NuovaGraduatoria);
    }
    if (azione == AZIONE.OP_PubblicaGraduatoria) {
      return this.client.azioni.some(a => a.codAzione == AZIONE.OP_PubblicaGraduatoria);
    }
    if (azione == AZIONE.OP_AggiornaGraduatoria) {
      return this.client.azioni.some(a => a.codAzione == AZIONE.OP_AggiornaGraduatoria);
    }
    if (azione == AZIONE.OP_SimulaGraduatoria) {
      return this.client.azioni.some(a => a.codAzione == AZIONE.OP_SimulaGraduatoria);
    }
    return false;
  }

  // -----------------------------------------------------------------CHECKBOX
  // checkSelezione() {
  //   let c: number = 0;
  //   this.listaDomandeGraduatoria.data.forEach(element => {
  //     if (element.checked) {
  //       c++;
  //     }
  //   })
  //   if (c == this.listaDomandeGraduatoria.data.length) {
  //     this.selezionaTutto = true;
  //   } else {
  //     this.selezionaTutto = false;
  //   }
  // }

  // checkUncheckAll() {
  //   let c: number = 0;
  //   this.listaDomandeGraduatoria.data.forEach(element => {
  //     if (element.checked) {
  //       c++;
  //     }
  //   })
  //   if (c < this.listaDomandeGraduatoria.data.length) {
  //     this.listaDomandeGraduatoria.data.forEach(element => {
  //       element.checked = true;
  //     })
  //   } else {
  //     this.listaDomandeGraduatoria.data.forEach(element => {
  //       element.checked = !element.checked;
  //     })
  //   }
  // }

  // -----------------------------------------------------------------OPERAZIONI_GRADUATORIA
  nuovaGraduatoria() {
    this.client.spinEmitter.emit(true);
    this.client.controlloEsistenzaGraduatoria(this.ultimoSportello.codSportello).subscribe(
      (response) => {
        this.client.checkEsistenzaGraduatoria = response as boolean;
        if (this.client.checkEsistenzaGraduatoria) {
          this.appToastService.showError({ text: "Graduatoria gi\u00E0 esistente" });
          this.getDomandeGraduatoria();
          this.getParametriFinanziamento();
          this.getDescrizioneGraduatoria();
          this.checkStatoGraduatoria('PROVVISORIA');
          this.checkStatoGraduatoria('PUBBLICATA');
          this.checkStatoGraduatoria('DA_AGGIORNARE');
          this.ricreaPaginator();
          this.client.spinEmitter.emit(false);
        } else {
          const modalRef = this.modalService.open(PopupNuovaGraduatoriaComponent, { size: 'lg' });
          modalRef.componentInstance.tipo = 'Nuova';
          modalRef.result.then((result) => {

            // Riprendo i dati della graduatoria
            if (result) {
              this.ricreaPaginator();
              this.azzeraDatiFinanziamento();
              this.client.getUltimoSportelloChiuso().subscribe(
                (response) => {
                  if (response) {
                    this.ultimoSportello = response as Sportello;
                    this.getDomandeGraduatoria();
                    this.getParametriFinanziamento();
                    this.getDescrizioneGraduatoria();
                    this.checkEsistenzaGraduatoria();
                    this.checkStatoGraduatoria('PROVVISORIA');
                    this.checkStatoGraduatoria('PUBBLICATA');
                    this.checkStatoGraduatoria('DA_AGGIORNARE');
                    this.checkPubblicazioneGraduatoria();
                  }
                },
                err => {
                }
              );
              this.appToastService.showSuccess({ text: 'Graduatoria creata con successo' });
            } else {
              this.appToastService.showError({ text: "Graduatoria gi\u00E0 esistente" });
            }
          }).catch((res) => { });
          this.client.spinEmitter.emit(false);
        }
      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );


  }

  pubblicaGraduatoria() {
    this.client.controlloStatoGraduatoria(this.ultimoSportello.codSportello, 'PUBBLICATA').subscribe(
      (response) => {
        this.client.checkStatoGraduatoria = response as boolean;
        this.checkStatoGraduatoriaPubblicazione = this.client.checkStatoGraduatoria;
        if (!this.checkStatoGraduatoriaPubblicazione) {
          this.client.controlloStatoGraduatoria(this.ultimoSportello.codSportello, 'DA_AGGIORNARE').subscribe(
            (response) => {
              this.client.checkStatoGraduatoria = response as boolean;
              this.checkStatoGraduatoriaAggiornamento = this.client.checkStatoGraduatoria;
              if (!this.checkStatoGraduatoriaAggiornamento && this.checkStatoGraduatoriaProvvisoria) {
                this.client.getCheckPubblicazioneGraduatoria(this.ultimoSportello.codSportello).subscribe(
                  (response) => {
                    if (response) {
                      this.gestioneModale();
                    } else {
                      this.getDomandeGraduatoria();
                      this.getParametriFinanziamento();
                      this.getDescrizioneGraduatoria();
                      this.checkStatoGraduatoria('PROVVISORIA');
                      this.checkStatoGraduatoria('PUBBLICATA');
                      this.checkStatoGraduatoria('DA_AGGIORNARE');
                      this.client.spinEmitter.emit(false);
                      this.appToastService.showError({ text: 'Non \u00E9 possibile pubblicare la graduatoria, ci sono domande in stato: INVIATA, PRESA IN CARICO, DA RETTIFICARE, IN RETTIFICA, RETTIFICATA, PREAVVISO DI DINIEGO PER NON AMMISSIBILITA’, AMMISSIBILE, NON AMMISSIBILE, CONTRODEDOTTA.' });
                    }
                  },
                  err => {
                    this.client.spinEmitter.emit(false);
                  }
                );
              } else {
                this.getDomandeGraduatoria();
                this.getParametriFinanziamento();
                this.getDescrizioneGraduatoria();
                this.checkStatoGraduatoria('PROVVISORIA');
                this.checkStatoGraduatoria('PUBBLICATA');
                this.checkStatoGraduatoria('DA_AGGIORNARE');
                this.client.spinEmitter.emit(false);
                this.appToastService.showError({ text: "Non e' possibile pubblicare la gradutoria se non e' in stato: PROVVISORIA" });
              }
            },
            err => {
              this.client.spinEmitter.emit(false);
            }
          );
        } else {
          this.getDomandeGraduatoria();
          this.getParametriFinanziamento();
          this.getDescrizioneGraduatoria();
          this.checkStatoGraduatoria('PROVVISORIA');
          this.checkStatoGraduatoria('PUBBLICATA');
          this.checkStatoGraduatoria('DA_AGGIORNARE');
          this.client.spinEmitter.emit(false);
          this.appToastService.showError({ text: "Non puoi PUBBLICARE una graduatoria gia' PUBBLICATA" });
        }
      },
      err => {
        this.client.spinEmitter.emit(false);
      }

    );
  }

  gestioneModale() {
    const dialogRef = this.dialog.open(CustomDialogComponent, {
      width: '650px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Conferma Pubblicazione Graduatoria', messaggio: 'Pubblicazione graduatoria' }
    });
    dialogRef.afterClosed().subscribe((result: boolean) => {
      if (this.checkStatoGraduatoriaProvvisoria) {
        if (result) {
          this.azzeraDatiFinanziamento();
          this.client.spinEmitter.emit(true);
          this.client.pubblicazioneGraduatoria(this.ultimoSportello.codSportello).subscribe(
            (response) => {
              this.getDomandeGraduatoria();
              this.getParametriFinanziamento();
              this.getDescrizioneGraduatoria();
              this.checkStatoGraduatoria('PROVVISORIA');
              this.checkStatoGraduatoria('PUBBLICATA');
              this.checkStatoGraduatoria('DA_AGGIORNARE');
              this.client.spinEmitter.emit(false);
            },
            err => {
              this.client.spinEmitter.emit(false);
            }
          );
          this.appToastService.showSuccess({ text: 'Graduatoria pubblicata con successo' });
        } else {

          this.client.spinEmitter.emit(false);
        }
      } else {
        this.getDomandeGraduatoria();
        this.getParametriFinanziamento();
        this.getDescrizioneGraduatoria();
        this.checkStatoGraduatoria('PROVVISORIA');
        this.checkStatoGraduatoria('PUBBLICATA');
        this.checkStatoGraduatoria('DA_AGGIORNARE');
        this.client.spinEmitter.emit(false);
        this.appToastService.showError({ text: "Non e' possibile pubblicare la gradutoria se non e' in stato: PROVVISORIA" });
      }
    });
  }

  aggiornaGraduatoria() {
    this.client.controlloStatoGraduatoria(this.ultimoSportello.codSportello, 'DA_AGGIORNARE').subscribe(
      (response) => {
        this.client.checkStatoGraduatoria = response as boolean;
        this.checkStatoGraduatoriaAggiornamento = this.client.checkStatoGraduatoria;
        if (this.checkStatoGraduatoriaAggiornamento) {
          const dialogRef = this.dialog.open(CustomDialogComponent, {
            width: '650px',
            disableClose: true,
            autoFocus: true,
            data: { titolo: 'Conferma Aggiornamento Graduatoria', messaggio: 'Aggiornamento graduatoria' }
          });
          dialogRef.afterClosed().subscribe((result: boolean) => {
            if (result) {
              this.azzeraDatiFinanziamento();
              this.client.spinEmitter.emit(true);
              this.client.aggiornamentoGraduatoria(this.ultimoSportello.codSportello).subscribe(
                (response) => {
                  this.getDomandeGraduatoria();
                  this.getParametriFinanziamento();
                  this.getDescrizioneGraduatoria();
                  this.checkPubblicazioneGraduatoria();
                  this.checkStatoGraduatoria('PROVVISORIA');
                  this.checkStatoGraduatoria('PUBBLICATA');
                  this.checkStatoGraduatoria('DA_AGGIORNARE');
                  this.client.spinEmitter.emit(false);
                },
                err => {
                  this.client.spinEmitter.emit(false);
                }
              );
              this.appToastService.showSuccess({ text: 'Graduatoria aggiornata con successo' });
            } else {
              this.client.spinEmitter.emit(false);
            }
          });
        } else {
          this.appToastService.showError({ text: "Non e' possibile aggiornare la gradutoria se non e' in stato: DA_AGGIORNARE" });
          this.getDomandeGraduatoria();
          this.getParametriFinanziamento();
          this.getDescrizioneGraduatoria();
          this.checkPubblicazioneGraduatoria();
          this.checkStatoGraduatoria('PROVVISORIA');
          this.checkStatoGraduatoria('PUBBLICATA');
          this.checkStatoGraduatoria('DA_AGGIORNARE');
        }
      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }

  simulaGraduatoria() {
    this.client.spinEmitter.emit(true);
    this.client.controlloStatoGraduatoria(this.ultimoSportello.codSportello, 'PUBBLICATA').subscribe(
      (response) => {
        this.client.checkStatoGraduatoria = response as boolean;
        this.checkStatoGraduatoriaPubblicazione = this.client.checkStatoGraduatoria;
        if (!this.checkStatoGraduatoriaPubblicazione) {
          this.client.controlloStatoGraduatoria(this.ultimoSportello.codSportello, 'DA_AGGIORNARE').subscribe(
            (response) => {
              this.client.checkStatoGraduatoria = response as boolean;
              this.checkStatoGraduatoriaAggiornamento = this.client.checkStatoGraduatoria;
              if (!this.checkStatoGraduatoriaAggiornamento) {
                const modalRef = this.modalService.open(PopupNuovaGraduatoriaComponent, { size: 'lg' });
                modalRef.componentInstance.tipo = 'Simula';
                modalRef.result.then((result) => {
                  if (result) {
                    this.ricreaPaginator();
                    this.azzeraDatiFinanziamento();
                    this.client.getUltimoSportelloChiuso().subscribe(
                      (response) => {
                        if (response) {
                          this.ultimoSportello = response as Sportello;
                          this.getDomandeGraduatoria();
                          this.getParametriFinanziamento();
                          this.getDescrizioneGraduatoria();
                          this.checkEsistenzaGraduatoria();
                          this.checkStatoGraduatoria('PROVVISORIA');
                          this.checkStatoGraduatoria('PUBBLICATA');
                          this.checkStatoGraduatoria('DA_AGGIORNARE');
                          this.checkPubblicazioneGraduatoria();
                          this.client.spinEmitter.emit(false);
                        }
                      },
                      err => {
                        this.client.spinEmitter.emit(false);
                      }
                    );
                    this.appToastService.showSuccess({ text: 'Graduatoria simulata con successo' });
                  } else {
                    this.appToastService.showError({ text: "Stato gradutoria errato. Richiesto: PROVVISORIA" });
                  }
                }).catch((res) => { });
              } else {
                this.appToastService.showError({ text: "Non e' possibile simulare la gradutoria se e' in stato: DA_AGGIORNARE" });
                this.getDomandeGraduatoria();
                this.getParametriFinanziamento();
                this.getDescrizioneGraduatoria();
                this.checkPubblicazioneGraduatoria();
                this.checkStatoGraduatoria('PROVVISORIA');
                this.checkStatoGraduatoria('PUBBLICATA');
                this.checkStatoGraduatoria('DA_AGGIORNARE');
              }
            },
            err => {
              this.client.spinEmitter.emit(false);
            }
          );
        } else {
          this.appToastService.showError({ text: "Non e' possibile simulare la gradutoria se e' in stato: PUBBLICATA" });
          this.getDomandeGraduatoria();
          this.getParametriFinanziamento();
          this.getDescrizioneGraduatoria();
          this.checkPubblicazioneGraduatoria();
          this.checkStatoGraduatoria('PROVVISORIA');
          this.checkStatoGraduatoria('PUBBLICATA');
          this.checkStatoGraduatoria('DA_AGGIORNARE');
        }
      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }

  getDomandeGraduatoria() {
    this.client.getDomandeGraduatoria(this.ultimoSportello.codSportello).subscribe(
      (response) => {
        this.client.ricercaDomGraduatoria = response as ModelDomandeGraduatoria[];
        this.listaDomandeGraduatoria.data = this.client.ricercaDomGraduatoria.slice() as ModelDomandeGraduatoria[];
      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }

  azzeraDatiFinanziamento() {
    // Azzero i dati della graduatoria
    this.client.parametriFinanziabili = null;
    this.client.soggettiFinanziabili = null;
    this.client.soggettiFinanziati = null;
    this.client.importoResiduoTotale = null;
    this.client.importoTotaleMisura = null;
    this.client.mesiPerDestinatario = null;
    this.client.buonoMensile = null;
    this.client.totaleImportoDestinatario = null;
  }

  getParametriFinanziamento() {
    this.importoTotaleRicevuto = 0;
    this.azzeraDatiFinanziamento();
    this.client.getParametriFinanziamento(this.ultimoSportello.codSportello).subscribe(
      (response) => {
        this.client.parametriFinanziabili = response as ModelParametriFinanziamento[];
        this.client.parametriFinanziabili.forEach((element) => {
          this.client.soggettiFinanziabili += element.soggettiFinanziabili;
          this.client.soggettiFinanziati += element.soggettiFinanziati;
          this.client.importoResiduoTotale += element.importoResiduo;
          this.client.importoTotaleMisura += element.importoTotale;
          if (element.aree != null) {
            this.importoTotaleRicevuto += element.importoDistribuibile;
          }
        });
        this.client.mesiPerDestinatario = this.client.parametriFinanziabili[0].mesi;
        this.client.buonoMensile = this.client.parametriFinanziabili[0].importoMensile;
        this.client.totaleImportoDestinatario = this.client.mesiPerDestinatario * this.client.buonoMensile;

      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }

  getDescrizioneGraduatoria() {
    this.client.getDescrizioneGraduatoria(this.ultimoSportello.codSportello).subscribe(
      (response) => {
        this.client.graduatoriaDescrizione = response as ModelDescrizioneGraduatoria;
        this.client.spinEmitter.emit(false);
      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }

  checkPubblicazioneGraduatoria() {
    this.client.getCheckPubblicazioneGraduatoria(this.ultimoSportello.codSportello).subscribe(
      (response) => {
        this.client.checkPubblicazioneGraduatoria = response as boolean;
        this.client.spinEmitter.emit(false);
      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }

  checkStatoGraduatoria(stato: string) {
    this.client.controlloStatoGraduatoria(this.ultimoSportello.codSportello, stato).subscribe(
      (response) => {
        this.client.checkStatoGraduatoria = response as boolean;
        switch (stato) {
          case 'PROVVISORIA':
            this.checkStatoGraduatoriaProvvisoria = this.client.checkStatoGraduatoria;
            break;
          case 'DA_AGGIORNARE':
            this.checkStatoGraduatoriaAggiornamento = this.client.checkStatoGraduatoria;
            break;
          case 'PUBBLICATA':
            this.checkStatoGraduatoriaPubblicazione = this.client.checkStatoGraduatoria;
            break;
          default:
            break;
        }
        this.client.checkStatoGraduatoria = response as boolean;
        this.client.spinEmitter.emit(false);
      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }

  getUltimoSportelloChiuso() {
    this.client.getUltimoSportelloChiuso().subscribe(
      (response) => {
        if (response) {
          this.ultimoSportello = response as Sportello;
          this.client.controlloEsistenzaGraduatoria(this.ultimoSportello.codSportello).subscribe(
            (response) => {
              this.client.checkEsistenzaGraduatoria = response as boolean;
            },
            err => {
              this.client.spinEmitter.emit(false);
            }
          );
        }
      }
    );
  }

  checkEsistenzaGraduatoria() {
    this.client.controlloEsistenzaGraduatoria(this.ultimoSportello.codSportello).subscribe(
      (response) => {
        this.client.checkEsistenzaGraduatoria = response as boolean;
      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }

  // -----------------------------------------------------------------UTILS

  ricreaPaginator() {
    this.listaDomandeGraduatoria = new MatTableDataSource<ModelDomandeGraduatoria>();
    this.listaDomandeGraduatoria.data = this.client.ricercaDomGraduatoria.slice() as ModelDomandeGraduatoria[];
    this.sort.active = this.client.ordinamentoSalvatoGraduatoria;
    this.sort.direction = this.client.versoSalvatoGraduatoria;
    this.listaDomandeGraduatoria.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
    this.listaDomandeGraduatoria.sort = this.sort;
    this.paginator.pageSize = this.client.righePerPaginaGraduatoria;
    this.paginator.pageIndex = this.client.paginaSalvataGraduatoria;
    this.listaDomandeGraduatoria.paginator = this.paginator;
  }

  convertiNumeroInItaliano(numero: number): string {
    const numeroItaliano = Number(numero).toLocaleString('it-IT');
    return numeroItaliano;
  }

  /*
  * Ottengo il nome di ogni proprietà di un oggetto
  */
  getProperty = (obj: ModelDomandeGraduatoria, path: string) => (
    path.split('.').reduce((o: { [x: string]: any; }, p: string | number) => o && o[p], obj)
  )


  sortData(sort: Sort) {
    const data = this.listaDomandeGraduatoria.data.slice();
    if (!sort.active || sort.direction === '') {
      this.sortedData = data;
      return;
    }

    this.sortedData = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'posizioneGraduatoria':
          return this.compare(a.posizioneGraduatoria, b.posizioneGraduatoria, isAsc);
        case 'numeroDomanda':
          return this.compare(a.numeroDomanda, b.numeroDomanda, isAsc);
        case 'destinatario':
          return this.compare(a.destinatarioCF, b.destinatarioCF, isAsc);
        case 'punteggioSociale':
          return this.compare(a.punteggioSociale, b.punteggioSociale, isAsc);
        case 'isee':
          return this.compare(a.isee, b.isee, isAsc);
        case 'areaInterna':
          return this.compare(a.areaInterna, b.areaInterna, isAsc);
        case 'statoDomanda':
          return this.compare(a.statoDomanda, b.statoDomanda, isAsc);
        case 'importoTotale':
          let ordinamento = 0;
          if(a.importoTotale > b.importoTotale) {
            ordinamento = 1 * (isAsc ? 1 : -1);
          } else if(a.importoTotale < b.importoTotale) {
            ordinamento = -1 * (isAsc ? 1 : -1);
          } else {
            if(a.posizioneGraduatoria < b.posizioneGraduatoria) {
              ordinamento = -1;
            } else {
              ordinamento = 1;
            }
          }
          return ordinamento;
          // return this.compare(a.importoTotale, b.importoTotale, isAsc);
        case 'dataInvioDomanda':
          // Fix nel caso una delle due date sua nulla
          let dataStringA = a.dataInvioDomanda;
          let dataStringB = b.dataInvioDomanda;
          if (a.dataInvioDomanda === null) {
            if (isAsc !== true) {
              dataStringA = '01/01/1970';
            } else if (isAsc) {
              dataStringA = '20/12/2099';
            }
          }
          if (b.dataInvioDomanda === null) {
            if (isAsc !== true) {
              dataStringB = '01/01/1970';
            } else if (isAsc) {
              dataStringB = '20/12/2099';
            }
          }
          let dateA = new Date(dataStringA.split("/").reverse().join("-"));
          let dateB = new Date(dataStringB.split("/").reverse().join("-"));

          return this.compare(dateA.getTime(), dateB.getTime(), isAsc);
        case 'statoVerificaEnte':
          const verificaA = this.compareVerifiche(a.verifiche);
          const verificaB = this.compareVerifiche(b.verifiche);
          return this.compare(verificaA, verificaB, isAsc);
        default:
          return 0;
      }
    });
    this.listaDomandeGraduatoria.data = this.sortedData;
    this.client.ricercaDomGraduatoria = this.sortedData;
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

  max(valore1: number, valore2: number): number {
    return Math.max(valore1, valore2);
  }

  //INIZIO PULSANTE SCARICA GRADUATORIA COME EXCEL
  scaricaGraduatoria(){
    let nomeFIle='';
    let tabellaGraduatoria = [];

    //STATO PUBBLICATE
    if(this.checkStatoGraduatoriaPubblicazione==true){
      nomeFIle='Scarico-Pubblicate';
    }
    //STATO DA AGGIORNARE
    if(this.checkStatoGraduatoriaAggiornamento==true){
      nomeFIle='Scarico-Da-Aggiornare';
    }
    //STATO PROVVISORIO
    if(this.checkStatoGraduatoriaProvvisoria==true){
      nomeFIle='Scarico-Provvisorie';
    }
    nomeFIle=nomeFIle+'_'+this.client.graduatoriaDescrizione.descrizioneGraduatoria.trim();

    var intestazione = new Array();
    intestazione.push( 'Posizione graduatoria' );
    intestazione.push( 'Numero Domanda' );
    intestazione.push( 'Destinatario' );
    intestazione.push( 'Punteggio sociale' );
    intestazione.push( 'Isee' );
    intestazione.push( 'Data invio Domanda' );
    intestazione.push( 'Stato Domanda' );
    intestazione.push( 'Stato verifica ente' );
    intestazione.push( 'Area interna' );
    intestazione.push( 'Importo totale' );
    tabellaGraduatoria.push(intestazione);

        this.listaDomandeGraduatoria.data.forEach(element => {

          var datiDinamici = new Array();
          datiDinamici.push( element.posizioneGraduatoria );
          datiDinamici.push( element.numeroDomanda );
          datiDinamici.push( element.destinatarioNome+" "+element.destinatarioCognome+" "+element.destinatarioCF );
          datiDinamici.push( element.punteggioSociale );
          datiDinamici.push( element.isee );
          datiDinamici.push( element.dataInvioDomanda );
          datiDinamici.push( element.statoDomanda );
          if( element.verifiche!=null){
            if(element.verifiche.verifica_eg_richiesta==true){
              datiDinamici.push( 'Richiesta verifica' );
            }
            else if(element.verifiche.verifica_eg_in_corso==true){
              datiDinamici.push( 'Verifica in corso' );
            }
            else if(element.verifiche.verifica_eg_conclusa==true){
              datiDinamici.push( 'Verifica effettuata' );
            }
            else{
              datiDinamici.push( '' );
            }
          }
          if(element.areaInterna!=null){
            datiDinamici.push( element.areaInterna );
          }
          else{
            datiDinamici.push( '' );
          }
          datiDinamici.push( element.importoTotale );

          tabellaGraduatoria.push(datiDinamici);

        })

        this.exportAsXLSX(tabellaGraduatoria, nomeFIle);
  }

  exportAsXLSX(tabellaGraduatoria, nomeFIle):void {
    this.excelService.exportAsExcelFile(tabellaGraduatoria, nomeFIle);
  }
  //FINE PULSANTE SCARICA GRADUATORIA COME EXCEL

}
