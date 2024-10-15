import { animate, state, style, transition, trigger } from '@angular/animations';
import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, } from '@angular/forms';
import { MatDialog } from '@angular/material';
import { ActivatedRoute, Navigation, Router } from '@angular/router';
import { BuonoresBOClient } from '@buonores-app/app/BuonoresBOClient';
import { ModelDecorrenzaBuono } from '@buonores-app/app/dto/ModelDecorrenzaBuono';
import { ModelRichiesta } from '@buonores-app/app/dto/ModelRichiesta';
import { AZIONE } from '@buonores-app/constants/buonores-constants';
import { BuonoresErrorService } from '@buonores-app/shared/error/buonores-error.service';
import { AppToastService } from '@buonores-shared/toast/app-toast.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { forkJoin } from 'rxjs';


interface BuonoInformazioni {
  buonoStatoCod: String;
  validitaFine: String;
  rinunciaDataDecorrenza: String;
  revocaDataDecorrenza: String;
	notaRevoca: String;
}

interface VerificaInformazioni {
  nessunaIncompatibilita: Boolean;
  verificaNote: String;
  verificaData: String;
}

interface StoricoRendicontazioni {
  monthData: string;
  anno: number;
  mese: string;
  esito: string;
  note: string;
  dataDichiarazione: string;
 dataIstruttoria: string;
}

@Component({
  selector: 'stato-avanzamento-istruttoria',
  templateUrl: './stato-avanzamento-istruttoria.component.html',
  styleUrls: ['./stato-avanzamento-istruttoria.component.css'],
  providers: [DatePipe],
  animations: [
    trigger('rotatedState', [
      state('rotated', style({ transform: 'rotate(0)' })),
      state('default', style({ transform: 'rotate(-180deg)' })),
      transition('rotated => default', animate('100ms ease-out')),
      transition('default => rotated', animate('100ms ease-in'))
    ])
  ]
})



export class StatoAvanzamentoIstruttoriaComponent implements OnInit {
  errorMessage = {
    error: { descrizione: '' },
    message: '',
    name: '',
    status: '',
    statusText: '',
    url: '',
    date: Date
  }

  navigation: Navigation;
  numeroDomanda: string;
  statoBuono: string;
  paginaRitorno: string;
  buonoCod: string;

  // Dati domanda
  domanda: ModelRichiesta;

  // Filtro periodo
  periodoRendicontazioneForm: FormGroup;
  controlloDataFine: boolean = true;
  dateUguali: boolean = true;
  buonoattivo: boolean = true;
  // Cambia Decorrenza
  decorrenzaBuono: ModelDecorrenzaBuono = new ModelDecorrenzaBuono(new Date(), new Date());
  decorrenzaBuonoIni: ModelDecorrenzaBuono = new ModelDecorrenzaBuono(new Date(), new Date());
  cambiaDecorrenzaForm: FormGroup;

  // Informazioni istruttoria
  buonoInfo: BuonoInformazioni;
  verificaInfo: VerificaInformazioni;
  listaStoricoRendicontazioni: StoricoRendicontazioni[] = [];
  listaStoricoRendicontazioniFiltered: StoricoRendicontazioni[] = [];
  displayedColumns: string[] = ['Anno', 'Mese', 'Esito', 'DataDichiarazione','DataIstruttoria', 'Note'];
  dataSource = this.listaStoricoRendicontazioni;
  anni: number[] = [];
  selectedOption: number = null;
  noBoxSegnalazioni: boolean = false;

  constructor(private router: Router, private route: ActivatedRoute, private fb: FormBuilder, private dialog: MatDialog,
    public client: BuonoresBOClient, private modalService: NgbModal, public toastService: AppToastService, private buonoresError: BuonoresErrorService, public datePipe: DatePipe, public appToastService: AppToastService) {
    this.navigation = this.router.getCurrentNavigation();
    let domandaValues: string[] = [];
    this.route.fragment.subscribe((frag: string) => {
      domandaValues.push(frag);
    });
    this.statoBuono = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.statoBuono : domandaValues[0][0];
    this.buonoCod = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.buonoCod : domandaValues[0][0];
    this.numeroDomanda = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.numeroDomanda : domandaValues[0][0];
    this.paginaRitorno = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.paginaRitorno : domandaValues[0][0];
  }

  ngOnInit() {
    this.client.spinEmitter.emit(true);

    // Check buono attivo
    this.buonoattivo = this.statoBuono !== 'ATTIVO' ? false : true;

    //Servizi get
    forkJoin({
      decorrenza: this.client.getDecorrenzaBuono(this.numeroDomanda),
      decorrenzaIni: this.client.getDecorrenzaBuono(this.numeroDomanda),
      schedaDomanda: this.client.getRichiestaNumero(this.numeroDomanda),
      buonoInformazioniResponse: this.client.getBuonoInformazioni(this.numeroDomanda),
      verificaInformazioniResponse: this.client.getVerificaInformazioni(this.numeroDomanda),
      storicoRendicontazioni: this.client.getStoricoRendicontazioni(this.numeroDomanda, this.buonoCod)
    })
      .subscribe(({ decorrenza, decorrenzaIni, schedaDomanda, buonoInformazioniResponse, verificaInformazioniResponse, storicoRendicontazioni }) => {

        //Decorrenza
        if (decorrenza) {
          this.decorrenzaBuono = decorrenza
        }
        //DecorrenzaIni
        if (decorrenzaIni) {
          this.decorrenzaBuonoIni = decorrenzaIni;
        }
        //Dati domanda
        if (schedaDomanda) {
          this.domanda = schedaDomanda;
        }

        // Buono informazioni
        if (buonoInformazioniResponse) {
          this.buonoInfo = {
            buonoStatoCod: buonoInformazioniResponse.buonoStatoCod,
            validitaFine: buonoInformazioniResponse.validitaFine,
            rinunciaDataDecorrenza: buonoInformazioniResponse.rinunciaDataDecorrenza,
            revocaDataDecorrenza: buonoInformazioniResponse.revocaDataDecorrenza,
			notaRevoca: buonoInformazioniResponse.notaRevoca
          }
        }
        // Verifica Informazioni
        if (verificaInformazioniResponse) {
          this.verificaInfo = {
            nessunaIncompatibilita: verificaInformazioniResponse.nessunaIncompatibilita,
            verificaNote: verificaInformazioniResponse.verificaNote,
            verificaData: verificaInformazioniResponse.verificaData
          }
        }

        // Storico rendicontazioni
        if (storicoRendicontazioni) {
          this.listaStoricoRendicontazioni = storicoRendicontazioni;
          const dateArrayTradotto = this.listaStoricoRendicontazioni.map(obj => {
            return {
              ...obj,
              mese: this.traduciMeseInItaliano(obj.mese.replace(/\s/g, ''))
            };
          });
          this.listaStoricoRendicontazioni = dateArrayTradotto;

          // elimino i mesi prima della decorrenza del buono
   /*       const data = new Date(this.decorrenzaBuonoIni.decorrenzaInizio);
          const meseItaliano = data.toLocaleDateString('it-IT', { month: 'long' });
          let flag = false;
          let datiFiltrati: StoricoRendicontazioni[] = [];
          for (let i = 0; i < this.listaStoricoRendicontazioni.length; i++) {
            if(this.listaStoricoRendicontazioni[i].mese.toLocaleLowerCase() === meseItaliano) {
              flag = true;
              datiFiltrati.push(this.listaStoricoRendicontazioni[i]);
              continue;
            }
            if(flag) {
              datiFiltrati.push(this.listaStoricoRendicontazioni[i]);
            }
          }
          this.listaStoricoRendicontazioni = datiFiltrati;
*/
          this.listaStoricoRendicontazioniFiltered = this.listaStoricoRendicontazioni;

          // Ottengo gli anni delle rendicontazioni
          this.anni = [];
          for (const anno of this.listaStoricoRendicontazioni) {
            if (!this.anni.includes(anno.anno)) {
              this.anni.push(anno.anno);
            }
          }
        }

        // Chek box informazioni
        if(!this.verificaInfo && !this.domanda.dataDecesso && !this.domanda.dataResidenzaExtraregione) {
          this.noBoxSegnalazioni = true;
        }

        this.client.spinEmitter.emit(false);
      });
  }


  //--------------METODI------------
  //Controllo azione utente
  checkAzione(azione: string): boolean {
    switch (azione) {
      case "richiesta_rettifica_ente":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_RichiediRettificaEnte);
      case "revoca_buono":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_RevocaBuono);
      default:
        return false;
    }
  }

  //Fa il routing alla pagina precedente
  backButton() {
    switch (this.paginaRitorno) {
      case 'istruttoria':
        this.router.navigate(["/operatore-buono/istruttoria"], { relativeTo: this.route, skipLocationChange: true });
        break;
      case 'archivio-domande':
        this.router.navigate(["/operatore-buono/archivio-domande"], { relativeTo: this.route, skipLocationChange: true });
        break;
      case 'istanze-aperte':
        this.router.navigate(["/operatore-buono/istanze-aperte"], { relativeTo: this.route, skipLocationChange: true });
        break;
      case 'rendicontazione-op':
        this.router.navigate(["/operatore-buono/rendicontazione-op"], { relativeTo: this.route, skipLocationChange: true });
        break;
      case 'rendicontazione-ente':
        this.router.navigate(["/operatore-buono/rendicontazione-ente"], { relativeTo: this.route, skipLocationChange: true });
        break;
      default:
        this.router.navigate(['selezione-profilo-applicativo'], { skipLocationChange: true });
        break;
    }
  }

  // Converto i mesi inglesi in italiano
  traduciMeseInItaliano(meseInglese: string): string {
    const traduzioniMesi = {
      'January': 'Gennaio',
      'February': 'Febbraio',
      'March': 'Marzo',
      'April': 'Aprile',
      'May': 'Maggio',
      'June': 'Giugno',
      'July': 'Luglio',
      'August': 'Agosto',
      'September': 'Settembre',
      'October': 'Ottobre',
      'November': 'Novembre',
      'December': 'Dicembre'
    };

    // Verifica se il mese è presente nell'oggetto delle traduzioni
    if (traduzioniMesi[meseInglese]) {
      return traduzioniMesi[meseInglese];
    } else {
      return meseInglese; // Restituisci il mese inglese se non c'è una traduzione
    }
  }

  onNumberChange(type: string = '') {
    if(type !== '') {
      console.log('Tutti gli anni');//DEBUG
      this.listaStoricoRendicontazioniFiltered = this.listaStoricoRendicontazioni;
    } else {
      console.log('anno selezionato');//DEBUG
      this.listaStoricoRendicontazioniFiltered = this.listaStoricoRendicontazioni.filter((e) => e.anno === this.selectedOption);
    }
  }
}


