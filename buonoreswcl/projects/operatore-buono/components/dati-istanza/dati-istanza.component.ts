import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material';
import { ActivatedRoute, Navigation, Router } from '@angular/router';
import { BuonoresBOClient } from '@buonores-app/app/BuonoresBOClient';
import { CambioStatoPopUp } from '@buonores-app/app/dto/CambioStatoPopUp';
import { Feedback } from '@buonores-app/app/dto/Feedback';
import { ModelDatiDaModificare } from '@buonores-app/app/dto/ModelDatiDaModificare';
import { ModelIsee } from '@buonores-app/app/dto/ModelIsee';
import { ModelMessaggio } from '@buonores-app/app/dto/ModelMessaggio';
import { ModelRichiesta } from '@buonores-app/app/dto/ModelRichiesta';
import { PresaInCaricoModel } from '@buonores-app/app/dto/PresaInCaricoModel';
import { ALLEGATI, AZIONE, DATIENTE_INFO, STATO_DOMANDA, TIPO_FEEDBACK, VALUTAZIONE } from '@buonores-app/constants/buonores-constants';
import { ValidaDati } from '@buonores-app/shared/controlli/validadati';
import { CustomDialogComponent } from '@buonores-app/shared/custom-dialog/custom-dialog.component';
import { BuonoresErrorService } from '@buonores-app/shared/error/buonores-error.service';
import { AppToastService } from '@buonores-shared/toast/app-toast.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BehaviorSubject, Subject, forkJoin } from 'rxjs';
import { CronologiaComponent } from '../cronologia/cronologia.component';
import { PopupCambistatoComponent } from '../popup-cambistato/popup-cambistato.component';
import { PopupEnteComponent } from '../popup-ente/popup-ente.component';
import { PopuppopupRevocaBuonoComponent } from '../popup-revoca-buono/popup-revoca-buono.component';
import { ModelVerificheEnte } from '@buonores-app/app/dto/ModelVerificheEnte';

@Component({
  selector: 'app-dati-istanza',
  templateUrl: './dati-istanza.component.html',
  styleUrls: ['./dati-istanza.component.css'],
  animations: [
    // Each unique animation requires its own trigger. The first argument of the trigger function is the name
    trigger('rotatedState', [
      state('rotated', style({ transform: 'rotate(0)' })),
      state('default', style({ transform: 'rotate(-180deg)' })),
      transition('rotated => default', animate('100ms ease-out')),
      transition('default => rotated', animate('100ms ease-in'))
    ])
  ]
})
export class DatiIstanzaComponent implements OnInit {

  state: string = 'default';
  dataModifica = new Date(new Date().getFullYear(), 0, 1);
  dataSistema = new Date();
  dataModificata: Date = null;
  navigation: Navigation;

  warn: string = '';
  popoverParamTitle: string = DATIENTE_INFO.INFO_PARAM_TITLE;
  popoverParamBody: string = '';

  errorMessage = {
    error: { descrizione: '' },
    message: '',
    name: '',
    status: '',
    statusText: '',
    url: '',
    date: Date
  }
  feedback: Feedback;
  numerodomanda: string;
  showFeedbackSubject: Subject<boolean> = new BehaviorSubject<boolean>(false);
  showFeedback: boolean;
  tipoFeedback: string;
  messaggioFeedback: string;
  domanda: ModelRichiesta;
  disabilitaForm: boolean;
  disabilitaAllegati: boolean;
  disabilitaPresaInCarico: boolean;
  disabilitaAmmissibile: boolean;
  disabilitaPreavvisopernonammissibilita: boolean;
  disabilitaAmmessa: boolean;
  disabilitaNonAmmissibile: boolean;
  disabilitaAmmessaConRiserva: boolean;
  disabilitaAmmessaConRiservaInPagamento: boolean;
  disabilitaInPagamento: boolean;
  disabilitaDiniego: boolean;
  disabilitaRevocaBuono: boolean;
  disabilitaRettificaEnte: boolean;
  verbale: string;
  cartaidentita: string;
  delega: string;
  tutela: string;
  tipoallegato: string;
  procuraspeciale: string;
  denunciainps: string;
  contratto: string;
  letteraincarico: string;
  controdeduzione: string;
  urlVerbale: string;
  urlDelega: string;
  urlTutela: string;
  urlContratto: string;
  urlControdeduzione: string;
  urlCartaIdentita: string;
  urlLetteraIncarico: string;
  urlProcuraSpeciale: string;
  contattoesiste: ModelMessaggio;
  allegatiRettifica: ModelDatiDaModificare[] = [];
  campiRettifica: ModelDatiDaModificare[] = [];
  selectedAllegatiItems: ModelDatiDaModificare[] = [];
  selectedCampiItems: ModelDatiDaModificare[] = [];
  selectedAllItems: ModelDatiDaModificare[] = [];
  verificheEnteGestore: ModelVerificheEnte = new ModelVerificheEnte(false, '', new Date());
  dropdownSettings = {};
  dropdownSettings1 = {};
  conformeOption: boolean = null;
  valoreisee: string;
  iseeprecedente: ModelIsee;

  valida: ValidaDati = new ValidaDati();
  @ViewChild(CronologiaComponent, { static: false }) cronologiaMod: CronologiaComponent;
  paginaRitorno: string;
  constructor(private router: Router, private route: ActivatedRoute, private fb: FormBuilder, private dialog: MatDialog,
    public client: BuonoresBOClient, private modalService: NgbModal, public toastService: AppToastService, private buonoresError: BuonoresErrorService) {

    this.navigation = this.router.getCurrentNavigation();
    let domandaValues: string[] = [];
    this.route.fragment.subscribe((frag: string) => {
      domandaValues.push(frag);
    });
    this.numerodomanda = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.numerodomanda : domandaValues[0][0];
    this.paginaRitorno = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.paginaRitorno : domandaValues[0][0];
  }

  ngOnInit() {
    this.showFeedback = this.client.showFeedback;
    this.dataSistema.setHours(0, 0, 0, 0);

    this.client.spinEmitter.emit(true);
    this.showMessage();
    forkJoin({
      schedaDomanda: this.client.getRichiestaNumero(this.numerodomanda),
      schedaDomandaprec: this.client.getRichiestaNumero(this.numerodomanda),
      listaAllegati: this.client.getAllegatiRettifica(this.numerodomanda),
      campiRettifica: this.client.getCampiRettifica(this.numerodomanda),
      contattoesiste: this.client.verificacontatto(this.numerodomanda)
    })
      .subscribe(({ schedaDomanda, schedaDomandaprec, listaAllegati, campiRettifica, contattoesiste }) => {
        this.client.azioni;
        this.domanda = schedaDomanda;
        this.iseeprecedente = schedaDomandaprec.isee;
        if (this.domanda.isee.isee_verificato_conforme == true) {
          this.conformeOption = true;
        }
        else if (this.domanda.isee.isee_verificato_conforme == false) {
          this.conformeOption = false;
        }
        this.valoreisee = this.transform(this.domanda.isee.isee_valore);
        this.allegatiRettifica = listaAllegati;
        this.campiRettifica = campiRettifica;
        this.contattoesiste = contattoesiste;
        this.delega = null;
        this.abilitabottoni();
        this.cartaidentita = null;
        this.verbale = null;
        this.procuraspeciale = null;
        this.denunciainps = null;
        this.contratto = null;
        this.letteraincarico = null;
        this.tutela = null;
        if (this.domanda.valutazione_multidimensionale == VALUTAZIONE.UMVD) {
          if (this.domanda.allegati.findIndex(element => element.tipo == ALLEGATI.VERBALE_UMVD) != -1) {
            this.verbale = this.domanda.allegati.filter(element => element.tipo == ALLEGATI.VERBALE_UMVD)[0].filename;
            this.tipoallegato = this.domanda.allegati.filter(element => element.tipo == ALLEGATI.VERBALE_UMVD)[0].tipo;
            this.urlVerbale = this.client.getAllegato(this.domanda.numero, this.tipoallegato);
          }
        }
        else if (this.domanda.valutazione_multidimensionale == VALUTAZIONE.UVG) {
          if (this.domanda.allegati.findIndex(element => element.tipo == ALLEGATI.VERBALE_UVG) != -1) {
            this.verbale = this.domanda.allegati.filter(element => element.tipo == ALLEGATI.VERBALE_UVG)[0].filename;
            this.tipoallegato = this.domanda.allegati.filter(element => element.tipo == ALLEGATI.VERBALE_UVG)[0].tipo;
            this.urlVerbale = this.client.getAllegato(this.domanda.numero, this.tipoallegato);
          }
        }
        if (this.domanda.allegati.findIndex(element => element.tipo == ALLEGATI.DELEGA) != -1) {
          this.delega = this.domanda.allegati.filter(element => element.tipo == ALLEGATI.DELEGA)[0].filename;
          this.urlDelega = this.client.getAllegato(this.domanda.numero, ALLEGATI.DELEGA);
        }
        if (this.domanda.allegati.findIndex(element => element.tipo == ALLEGATI.NOMINA_TUTORE) != -1) {
          this.tutela = this.domanda.allegati.filter(element => element.tipo == ALLEGATI.NOMINA_TUTORE)[0].filename;
          this.urlTutela = this.client.getAllegato(this.domanda.numero, ALLEGATI.NOMINA_TUTORE);
        }
        if (this.domanda.allegati.findIndex(element => element.tipo == ALLEGATI.CARTA_IDENTITA) != -1) {
          this.cartaidentita = this.domanda.allegati.filter(element => element.tipo == ALLEGATI.CARTA_IDENTITA)[0].filename;
          this.urlCartaIdentita = this.client.getAllegato(this.domanda.numero, ALLEGATI.CARTA_IDENTITA);
        }
        if (this.domanda.allegati.findIndex(element => element.tipo == ALLEGATI.PROCURA_SPECIALE) != -1) {
          this.procuraspeciale = this.domanda.allegati.filter(element => element.tipo == ALLEGATI.PROCURA_SPECIALE)[0].filename;
          this.urlProcuraSpeciale = this.client.getAllegato(this.domanda.numero, ALLEGATI.PROCURA_SPECIALE);
        }
        if (this.domanda.allegati.findIndex(element => element.tipo == ALLEGATI.LETTERA_INCARICO) != -1) {
          this.letteraincarico = this.domanda.allegati.filter(element => element.tipo == ALLEGATI.LETTERA_INCARICO)[0].filename;
          this.urlLetteraIncarico = this.client.getAllegato(this.domanda.numero, ALLEGATI.LETTERA_INCARICO);
        }
        if (this.domanda.allegati.findIndex(element => element.tipo == ALLEGATI.CONTRATTO_RSA) != -1) {
          this.contratto = this.domanda.allegati.filter(element => element.tipo == ALLEGATI.CONTRATTO_RSA)[0].filename;
          this.urlContratto = this.client.getAllegato(this.domanda.numero, ALLEGATI.CONTRATTO_RSA);
        }
        if (this.domanda.allegati.findIndex(element => element.tipo == ALLEGATI.CONTRODEDUZIONE) != -1) {
          this.controdeduzione = this.domanda.allegati.filter(element => element.tipo == ALLEGATI.CONTRODEDUZIONE)[0].filename;
          this.urlControdeduzione = this.client.getAllegato(this.domanda.numero, ALLEGATI.CONTRODEDUZIONE);
        }

        this.dropdownSettings = {
          singleSelection: false,
          idField: 'codice',
          textField: 'descrizione',
          selectAllText: 'Seleziona tutti',
          unSelectAllText: 'Deseleziona tutti',
          itemsShowLimit: 0,
          allowSearchFilter: false
        };
        this.dropdownSettings1 = {
          singleSelection: false,
          idField: 'codice',
          textField: 'descrizione',
          selectAllText: 'Seleziona tutti',
          unSelectAllText: 'Deseleziona tutti',
          itemsShowLimit: 0,
          allowSearchFilter: false
        };

        this.client.spinEmitter.emit(false);
      },
        err => {
          this.client.spinEmitter.emit(false);
        });
    this.getVerificheEnte();
    this.client.feedback = null;
    this.client.showFeedback = false;
  }



  checkAzione(azione: string): boolean {

    switch (azione) {
      case "scarica_allegato":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_ScaricaAllegati);
      case "verifica_isee":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_VerificaISEE);
      case "presa_in_carico":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_PrendiInCarico);
      case "richiesta_rettifica":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_RichiediRettifica);
      case "ammissibile_domanda":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_AmmissibileDomanda);
      case "non_ammissibile_domanda":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_NonAmmissibileDomanda);
      case "preavvisopernonammissibilita_domanda":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_PreavvisopernonammissibilitaDomanda);
      case "ammessa_domanda":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_AmmettiDomanda);
      case "ammessa_con_riserva_domanda":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_AmmessaConRiserva);
      case "ammessa_con_riserva_in_pagamento_domanda":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_AmmessaConRiservaInPagamento);
      case "in_pagamento":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_InPagamento);
      case "diniego":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_Diniego);
      case "richiesta_rettifica_ente":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_RichiediRettificaEnte);
      case "cronologia":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_VisualizzaCronologia);
      case "OP-RevocaBuono":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_RevocaBuono);
      default:
        return false;
    }

  }


  onChangeConforme(valore: string) {
    if (valore == "Si") {
      if (this.conformeOption == null)
        this.conformeOption = true;
      else if (this.conformeOption == false)
        this.conformeOption = true;
      else if (this.conformeOption == true)
        this.conformeOption = null;
    }
    else if (valore == "No") {
      if (this.conformeOption == null)
        this.conformeOption = false;
      else if (this.conformeOption == true)
        this.conformeOption = false;
      else if (this.conformeOption == false)
        this.conformeOption = null;
    }
  }

  salvaisee() {
    this.client.spinEmitter.emit(true);
    if (this.conformeOption == true)
      this.domanda.isee.isee_verificato_conforme = true;
    else if (this.conformeOption == false)
      this.domanda.isee.isee_verificato_conforme = false;
    else
      this.domanda.isee.isee_verificato_conforme = null;
    this.domanda.isee.isee_valore = this.parsingFloat(this.valoreisee);
    this.client.salvaDomandaIsee(this.domanda.numero, this.domanda.isee).subscribe(
      () => {
        this.client.spinEmitter.emit(false);
        if (this.conformeOption == true)
          this.disabilitaAmmissibile = false;
        else
          this.disabilitaAmmissibile = true;
        this.iseeprecedente = this.domanda.isee;
        this.toastService.showSuccess({ text: 'Isee salvato correttamente' });
      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }

  salvanota() {
    this.client.spinEmitter.emit(true);
    let nota: CambioStatoPopUp = new CambioStatoPopUp();
    nota.numerodomanda = this.domanda.numero;
    nota.notaInterna = this.domanda.nota_interna;
    this.client.salvaDomandaNota(nota).subscribe(
      () => {
        document.dispatchEvent(this.client.updateCronoEmitter);
        this.client.spinEmitter.emit(false);
        this.toastService.showSuccess({ text: 'Nota salvata correttamente' });
      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }


  abilitabottoni() {

    //verifica incompatibilita
    let incompatibilita = false;
    if (this.domanda.contratto.incompatibilita_per_contratto || !this.domanda.nessuna_incompatibilita) {
      incompatibilita = true;
    }
    //FORM
    if (this.domanda != null && this.domanda != undefined && this.domanda.stato === STATO_DOMANDA.PRESA_IN_CARICO)
      this.disabilitaForm = false;
    else
      this.disabilitaForm = true;

    //ALLEGATI
    if (this.domanda != null && this.domanda != undefined && this.domanda.stato === STATO_DOMANDA.INVIATA)
      this.disabilitaAllegati = true;
    else
      this.disabilitaAllegati = false;

    //PRESA IN CARICO
    if (this.domanda != null && this.domanda != undefined && (this.domanda.stato === STATO_DOMANDA.INVIATA
	    || this.domanda.stato === STATO_DOMANDA.RETTIFICATA || this.domanda.stato === STATO_DOMANDA.CONTRODEDOTTA))
      this.disabilitaPresaInCarico = false;
    else
      this.disabilitaPresaInCarico = true;

    //AMMISSIBILE
    if (this.domanda != null && this.domanda != undefined && this.domanda.stato === STATO_DOMANDA.PRESA_IN_CARICO && this.conformeOption == true) {
      this.disabilitaAmmissibile = false;
    } else {
      this.disabilitaAmmissibile = true;
    }

    //PREAVVISO PER NON AMMISSIBILITA
    if (this.domanda != null && this.domanda != undefined && (this.domanda.stato === STATO_DOMANDA.PRESA_IN_CARICO || this.domanda.stato === STATO_DOMANDA.AMMISSIBILE)) {
      this.disabilitaPreavvisopernonammissibilita = false;
    } else {
      this.disabilitaPreavvisopernonammissibilita = true;
    }

    //Non ammissibile da controdedotta
    if (this.domanda != null && this.domanda != undefined && (this.domanda.stato === STATO_DOMANDA.CONTRODEDOTTA)) {
      this.disabilitaNonAmmissibile = false;
    } else {
      this.disabilitaNonAmmissibile = true;
    }

    //AMMESSA o ammessa da controdedotta
    if (this.domanda != null && this.domanda != undefined && (this.domanda.stato === STATO_DOMANDA.CONTRODEDOTTA || this.domanda.stato === STATO_DOMANDA.AMMISSIBILE) && !incompatibilita && this.conformeOption == true) {
      this.disabilitaAmmessa = false;
    } else {
      this.disabilitaAmmessa = true;
    }

    //AMMESSA CON RISERVA o ammessa riserva da controdedotta
    if (this.domanda != null && this.domanda != undefined && (this.domanda.stato === STATO_DOMANDA.CONTRODEDOTTA || this.domanda.stato === STATO_DOMANDA.AMMISSIBILE) && incompatibilita && this.conformeOption == true) {
      this.disabilitaAmmessaConRiserva = false;
    } else {
      this.disabilitaAmmessaConRiserva = true;
    }

    //Rettifica ente
    if (this.domanda != null && this.domanda != undefined && !(this.domanda.verifiche.verifica_eg_richiesta || this.domanda.verifiche.verifica_eg_in_corso) && this.domanda.stato != STATO_DOMANDA.RINUNCIATA
      && this.domanda.stato != STATO_DOMANDA.ANNULLATA && this.domanda.stato != STATO_DOMANDA.DINIEGO) {
      this.disabilitaRettificaEnte = false;
    } else {
      this.disabilitaRettificaEnte = true;
    }

    //IN PAGAMENTO
    if (this.domanda != null && this.domanda != undefined && this.domanda.stato === STATO_DOMANDA.PERFEZIONATA_IN_PAGAMENTO) {
      this.disabilitaInPagamento = false;
    } else {
      this.disabilitaInPagamento = true;
    }

    //REVOCA BUONO
    if (this.domanda != null && this.domanda != undefined && this.domanda.stato === STATO_DOMANDA.IN_PAGAMENTO) {
      this.disabilitaRevocaBuono = false;
    } else {
      this.disabilitaRevocaBuono = true;
    }

        //AMMESSA CON RISERVA IN PAGAMENTO NON VA MAI MOSTRATO
    //  if (this.domanda != null && this.domanda != undefined && this.domanda.stato === STATO_DOMANDA.AMMESSA_CON_RISERVA) {
   //     this.disabilitaAmmessaConRiservaInPagamento = false;
   //   } else {
    this.disabilitaAmmessaConRiservaInPagamento = true;
    //  }
      //DINIEGO
      if (this.domanda != null && this.domanda != undefined &&
        (this.domanda.stato === STATO_DOMANDA.PERFEZIONATA_IN_PAGAMENTO ||
          this.domanda.stato === STATO_DOMANDA.CONTRODEDOTTA ||
          this.domanda.stato === STATO_DOMANDA.NON_AMMISSIBILE )) {
        this.disabilitaDiniego = false;
      } else {
        this.disabilitaDiniego = true;
      }
  }


  // verificaateco() {
  //   this.client.spinEmitter.emit(true);
  //   let piva = null;
  //   if (this.domanda.contratto.tipo == CONTRATTO.COOPERATIVA)
  //     piva = this.domanda.contratto.agenzia.cf;
  //   else
  //     piva = this.domanda.contratto.piva_assitente_familiare;
  //   this.client.getAteco(this.domanda.numero, piva).subscribe(
  //     (ateco: ModelAteco) => {
  //       this.domanda.ateco.ateco_cod = ateco.ateco_cod;
  //       this.domanda.ateco.ateco_desc = ateco.ateco_desc;
  //       this.domanda.ateco.ateco_verificato_in_data = ateco.ateco_verificato_in_data;
  //       this.client.spinEmitter.emit(false);
  //     },
  //     err => {
  //       this.client.spinEmitter.emit(false);
  //     }
  //   );
  // }

  controllacontatto(campo: string) {
    //se non esiste contatto devi aprire popup con messaggil se continuare
    if (this.contattoesiste.descrizione !== 'contattoesiste') {
      let message;
      let titolo;
      message = this.contattoesiste.descrizione;
      titolo = `Contatto`;
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
          switch (campo) {
            case 'richiedirettifica':
              this.richiedirettifica();
              break;
            case 'preavvisopernonammissibilita':
              this.preavvisopernonammissibilita();
              break;
            case 'ammessa':
              this.ammessa();
              break;
            case 'ammessaConRiserva':
              this.ammessaConRiserva();
              break;
            case 'ammessaConRiservaInPagamento':
              this.ammessaConRiservaInPagamento();
              break;
            case 'inPagamento':
              this.inPagamento();
              break;
            case 'diniego':
              this.diniego();
              break;
            default:
              break;
          }
        }
      })
    }
    else {
      switch (campo) {
        case 'richiedirettifica':
          this.richiedirettifica();
          break;
        case 'preavvisopernonammissibilita':
          this.preavvisopernonammissibilita();
          break;
        case 'ammessa':
          this.ammessa();
          break;
        case 'ammessaConRiserva':
          this.ammessaConRiserva();
          break;
        case 'ammessaConRiservaInPagamento':
          this.ammessaConRiservaInPagamento();
          break;
        case 'inPagamento':
          this.inPagamento();
          break;
        case 'diniego':
          this.diniego();
          break;
        default:
          break;
      }
    }
  }

  richiedirettifica() {
    if (this.controllasalvataggioisee()) {
      this.toastService.showError({ text: 'Per procedere e\' necessario salvare i valori dell\'isee' });
    }
    else {
      this.selectedAllItems = [];
      for (let entry of this.selectedCampiItems) {
        entry.id = this.campiRettifica.filter(element => element.codice == entry.codice)[0].id;
        entry.nome_tabella = this.campiRettifica.filter(element => element.codice == entry.codice)[0].nome_tabella;
        this.selectedAllItems.push(entry);
      }
      for (let entry of this.selectedAllegatiItems) {
        entry.id = this.allegatiRettifica.filter(element => element.codice == entry.codice)[0].id;
        entry.nome_tabella = this.allegatiRettifica.filter(element => element.codice == entry.codice)[0].nome_tabella;
        this.selectedAllItems.push(entry);
      }
      const modalRef = this.modalService.open(PopupCambistatoComponent, { size: 'lg' });
      modalRef.componentInstance.numerodomanda = this.numerodomanda;
      modalRef.componentInstance.selectedAllItems = this.selectedAllItems;
      modalRef.componentInstance.descStato = 'RICHIESTA VERIFICA CITTADINO';
      modalRef.componentInstance.stato = STATO_DOMANDA.DA_RETTIFICARE;
      modalRef.result.then((result) => {
        if (result) {
          this.domanda.stato = STATO_DOMANDA.DA_RETTIFICARE;
          this.domanda.domanda_stato_desc = STATO_DOMANDA.DA_RETTIFICARE_DESC
          this.selectedCampiItems = [];
          this.selectedAllegatiItems = [];
          this.selectedAllItems = [];
          this.client.ricercaDomAperte = [];
          this.abilitabottoni();
          document.dispatchEvent(this.client.updateCronoEmitter);
          this.toastService.showSuccess({ text: 'Richiesta di rettifica inviata correttamente' });
        }
      }).catch((res) => { });
    }
  }


  presaInCarico() {
    if (this.controllasalvataggioisee()) {
      this.toastService.showError({ text: 'Per procedere e\' necessario salvare i valori dell\'isee' });
    }
    else {
      let message;
      let titolo;
      message = `Sei sicuro di voler prendere in carico la domanda selezionata`;
      titolo = `Presa in carico`;
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
          this.client.spinEmitter.emit(true);
          let modelPresaCarico: PresaInCaricoModel[] = [];
          let model: PresaInCaricoModel = new PresaInCaricoModel(this.domanda.numero, this.domanda.stato);
          modelPresaCarico.push(model);
          this.client.presaInCarico(modelPresaCarico).subscribe(res => {
            if (res) {
              this.domanda.stato = STATO_DOMANDA.PRESA_IN_CARICO;
              this.domanda.domanda_stato_desc = STATO_DOMANDA.PRESA_IN_CARICO_DESC
              this.abilitabottoni();
              this.client.ricercaDomAperte = [];
              this.toastService.showSuccess({ text: 'Domanda passata in stato presa in carico' });
              this.client.spinEmitter.emit(false);
            } else {
              this.client.spinEmitter.emit(false);
            }
          })
        } else {
          this.client.spinEmitter.emit(false);
        }
      })
    }
  }

  ammissibile() {
    //se isee non salvato non faccio andare avanti
    if (this.controllasalvataggioisee()) {
      this.toastService.showError({ text: 'Per procedere e\' necessario salvare i valori dell\'isee' });
    }
    else {
      //richiamo la modal di conferma
      const modalRef = this.modalService.open(PopupCambistatoComponent, { size: 'lg' });
      modalRef.componentInstance.numerodomanda = this.numerodomanda;
      modalRef.componentInstance.selectedAllItems = this.selectedAllItems;
      modalRef.componentInstance.descStato = 'DOMANDA AMMISSIBILE';
      modalRef.componentInstance.stato = STATO_DOMANDA.AMMISSIBILE;
      modalRef.result.then((result) => {
        if (result) {
          this.client.spinEmitter.emit(true);
          this.domanda.stato = STATO_DOMANDA.AMMISSIBILE;
          this.domanda.domanda_stato_desc = STATO_DOMANDA.AMMISSIBILE;
          this.abilitabottoni();

          //forza la ricerca nel caso tornassi in istanze-aperte
          this.client.ricercaDomAperte = [];
          document.dispatchEvent(this.client.updateCronoEmitter);

          this.toastService.showSuccess({ text: 'Domanda passata in stato ammissibile' });
          this.client.spinEmitter.emit(false);
        }
      }).catch((res) => { });
    }
  }

  nonammissibile() {
    //se isee non salvato non faccio andare avanti
    if (this.controllasalvataggioisee()) {
      this.toastService.showError({ text: 'Per procedere e\' necessario salvare i valori dell\'isee' });
    }
    else {
      //richiamo la modal di conferma
      const modalRef = this.modalService.open(PopupCambistatoComponent, { size: 'lg' });
      modalRef.componentInstance.numerodomanda = this.numerodomanda;
      modalRef.componentInstance.selectedAllItems = this.selectedAllItems;
      modalRef.componentInstance.descStato = 'DOMANDA NON AMMISSIBILE';
      modalRef.componentInstance.stato = STATO_DOMANDA.NON_AMMISSIBILE;
      modalRef.result.then((result) => {
        if (result) {
          this.client.spinEmitter.emit(true);
          this.domanda.stato = STATO_DOMANDA.NON_AMMISSIBILE;
          this.domanda.domanda_stato_desc = STATO_DOMANDA.NON_AMMISSIBILE;
          this.abilitabottoni();

          //forza la ricerca nel caso tornassi in istanze-aperte
          this.client.ricercaDomAperte = [];
          document.dispatchEvent(this.client.updateCronoEmitter);

          this.toastService.showSuccess({ text: 'Domanda passata in stato non ammissibile' });
          this.client.spinEmitter.emit(false);
        }
      }).catch((res) => { });
    }
  }

  // Open modal ammessa button
  ammessa() {
    if (this.controllasalvataggioisee()) {
      this.toastService.showError({ text: 'Per procedere e\' necessario salvare i valori dell\'isee' });
    }
    else {
      //richiamo la modal di conferma
      const modalRef = this.modalService.open(PopupCambistatoComponent, { size: 'lg' });
      modalRef.componentInstance.numerodomanda = this.numerodomanda;
      modalRef.componentInstance.selectedAllItems = this.selectedAllItems;
      modalRef.componentInstance.descStato = 'DOMANDA AMMESSA';
      modalRef.componentInstance.stato = STATO_DOMANDA.AMMESSA;
      modalRef.result.then((result) => {
        if (result) {
          this.client.spinEmitter.emit(true);
          this.domanda.stato = STATO_DOMANDA.AMMESSA;
          this.domanda.domanda_stato_desc = STATO_DOMANDA.AMMESSA;
          this.abilitabottoni();

          //forza la ricerca nel caso tornassi in istanze-aperte
          this.client.ricercaDomAperte = [];
          document.dispatchEvent(this.client.updateCronoEmitter);

          this.toastService.showSuccess({ text: 'Domanda passata in stato ammessa' });
          this.client.spinEmitter.emit(false);
        }
      }).catch((res) => { });
    }
  }
  // Open modal ammessa con riserva button
  ammessaConRiserva() {
    if (this.controllasalvataggioisee()) {
      this.toastService.showError({ text: 'Per procedere e\' necessario salvare i valori dell\'isee' });
    }
    else {
      //richiamo la modal di conferma
      const modalRef = this.modalService.open(PopupCambistatoComponent, { size: 'lg' });
      modalRef.componentInstance.numerodomanda = this.numerodomanda;
      modalRef.componentInstance.selectedAllItems = this.selectedAllItems;
      modalRef.componentInstance.descStato = 'DOMANDA AMMESSA CON RISERVA';
      modalRef.componentInstance.stato = STATO_DOMANDA.AMMESSA_CON_RISERVA;
      modalRef.result.then((result) => {
        if (result) {
          this.client.spinEmitter.emit(true);
          this.domanda.stato = STATO_DOMANDA.AMMESSA_CON_RISERVA;
          this.domanda.domanda_stato_desc = STATO_DOMANDA.AMMESSA_CON_RISERVA;
          this.abilitabottoni();

          //forza la ricerca nel caso tornassi in istanze-aperte
          this.client.ricercaDomAperte = [];
          document.dispatchEvent(this.client.updateCronoEmitter);

          this.toastService.showSuccess({ text: 'Domanda passata in stato ammessa con riserva' });
          this.client.spinEmitter.emit(false);
        }
      }).catch((res) => { });
    }
  }

  // Open modal ammessa in pagamento con riserva button
  ammessaConRiservaInPagamento() {
    if (this.controllasalvataggioisee()) {
      this.toastService.showError({ text: 'Per procedere e\' necessario salvare i valori dell\'isee' });
    }
    else {
      //richiamo la modal di conferma
      const modalRef = this.modalService.open(PopupCambistatoComponent, { size: 'lg' });
      modalRef.componentInstance.numerodomanda = this.numerodomanda;
      modalRef.componentInstance.selectedAllItems = this.selectedAllItems;
      modalRef.componentInstance.descStato = 'DOMANDA AMMESSA CON RISERVA IN PAGAMENTO';
      modalRef.componentInstance.stato = STATO_DOMANDA.AMMESSA_CON_RISERVA_IN_PAGAMENTO;
      modalRef.result.then((result) => {
        if (result) {
          this.client.spinEmitter.emit(true);
          this.domanda.stato = STATO_DOMANDA.AMMESSA_CON_RISERVA_IN_PAGAMENTO;
          this.domanda.domanda_stato_desc = STATO_DOMANDA.AMMESSA_CON_RISERVA_IN_PAGAMENTO;
          this.abilitabottoni();

          //forza la ricerca nel caso tornassi in istanze-aperte
          this.client.ricercaDomAperte = [];
          document.dispatchEvent(this.client.updateCronoEmitter);

          this.toastService.showSuccess({ text: 'Domanda passata in stato ammessa con riserva in pagamento' });
          this.client.spinEmitter.emit(false);
        }
      }).catch((res) => { });
    }
  }

  richiedirettificaente() {
    if (this.controllasalvataggioisee()) {
      this.toastService.showError({ text: 'Per procedere e\' necessario salvare i valori dell\'isee' });
    }
    else {
      //richiamo la modal di conferma
      const modalRef = this.modalService.open(PopupEnteComponent, { size: 'lg' });
      modalRef.componentInstance.numerodomanda = this.numerodomanda;
      modalRef.componentInstance.descRichiesta = 'RICHIESTA VERIFICA ENTE GESTORE';
      modalRef.result.then((result) => {
        if (result) {
          this.client.spinEmitter.emit(true);
          this.domanda.verifiche.verifica_eg_richiesta = true;
          this.domanda.verifiche.verifica_eg_conclusa = null;
          this.domanda.verifiche.verifica_eg_in_corso = null;
          this.abilitabottoni();
          document.dispatchEvent(this.client.updateCronoEmitter);

          this.toastService.showSuccess({ text: 'Richiesta per l\'ente gestore effettuata con successo' });
          this.client.spinEmitter.emit(false);
        }
      }).catch((res) => { });
    }
  }

  // Open modal inPagamento button
  inPagamento() {
    if (this.controllasalvataggioisee()) {
      this.toastService.showError({ text: 'Per procedere e\' necessario salvare i valori dell\'isee' });
    }
    else {
      //richiamo la modal di conferma
      const modalRef = this.modalService.open(PopupCambistatoComponent, { size: 'lg' });
      modalRef.componentInstance.numerodomanda = this.numerodomanda;
      modalRef.componentInstance.selectedAllItems = this.selectedAllItems;
      modalRef.componentInstance.descStato = 'DOMANDA IN PAGAMENTO';
      modalRef.componentInstance.stato = STATO_DOMANDA.IN_PAGAMENTO;
      modalRef.result.then((result) => {
        if (result) {
          this.client.spinEmitter.emit(true);
          this.domanda.stato = STATO_DOMANDA.IN_PAGAMENTO;
          this.domanda.domanda_stato_desc = STATO_DOMANDA.IN_PAGAMENTO;
          this.abilitabottoni();

          //forza la ricerca nel caso tornassi in istanze-aperte
          this.client.ricercaDomAperte = [];
          document.dispatchEvent(this.client.updateCronoEmitter);

          this.toastService.showSuccess({ text: 'Domanda passata in stato di pagamento' });
          this.client.spinEmitter.emit(false);
        }
      }).catch((res) => { });
    }
  }

  // Open modal diniego button
  diniego() {
    if (this.controllasalvataggioisee()) {
      this.toastService.showError({ text: 'Per procedere e\' necessario salvare i valori dell\'isee' });
    }
    else {
      //richiamo la modal di conferma
      const modalRef = this.modalService.open(PopupCambistatoComponent, { size: 'lg' });
      modalRef.componentInstance.numerodomanda = this.numerodomanda;
      modalRef.componentInstance.selectedAllItems = this.selectedAllItems;
      modalRef.componentInstance.descStato = 'DOMANDA IN DINIEGO';
      modalRef.componentInstance.stato = STATO_DOMANDA.DINIEGO;

      modalRef.result.then((result) => {
        if (result) {
          this.client.spinEmitter.emit(true);
          this.domanda.stato = STATO_DOMANDA.DINIEGO;
          this.domanda.domanda_stato_desc = STATO_DOMANDA.DINIEGO;
          this.abilitabottoni();

          //forza la ricerca nel caso tornassi in istanze-aperte
          this.client.ricercaDomAperte = [];
          document.dispatchEvent(this.client.updateCronoEmitter);
          this.toastService.showSuccess({ text: 'Domanda passata in stato di diniego' });
          this.client.spinEmitter.emit(false);
        }
      }).catch((res) => { });
    }
  }


  preavvisopernonammissibilita() {
    if (this.controllasalvataggioisee()) {
      this.toastService.showError({ text: 'Per procedere e\' necessario salvare i valori dell\'isee' });
    }
    else {
      //richiamo la modal di conferma
      const modalRef = this.modalService.open(PopupCambistatoComponent, { size: 'lg' });
      modalRef.componentInstance.numerodomanda = this.numerodomanda;
      modalRef.componentInstance.descStato = 'PREAVVISO DI DINIEGO PER NON AMMISSIBILITA\' DOMANDA';
      modalRef.componentInstance.stato = STATO_DOMANDA.PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA;
      modalRef.result.then((result) => {
        if (result) {
          this.client.spinEmitter.emit(true);
          this.domanda.stato = STATO_DOMANDA.PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA;
          this.domanda.domanda_stato_desc = STATO_DOMANDA.PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA;
          this.abilitabottoni();

          //forza la ricerca nel caso tornassi in istanze-aperte

          this.client.ricercaDomAperte = [];
          document.dispatchEvent(this.client.updateCronoEmitter);
          this.toastService.showSuccess({ text: 'Domanda passata in stato preavviso di diniego per non ammissibilita\'' });
          this.client.spinEmitter.emit(false);
        }
      }).catch((res) => { });
    }
  }

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
      default:
        this.router.navigate(['selezione-profilo-applicativo'], { skipLocationChange: true });
        break;
    }
  }

  showMessage() {
    if (this.client.feedback !== null && this.client.feedback !== undefined && this.client.showFeedback) {
      this.showMessageEvent(this.client.feedback);
      this.feedback = null;

    }
  }

  showMessageEvent(feedback: Feedback) {
    this.showFeedbackSubject.next(feedback.showFeedback);
    this.tipoFeedback = feedback.tipoFeedback;
    this.messaggioFeedback = feedback.messaggioFeedback;
    // this.ngOnInit();
  }

  onCloseFeedback() {
    this.showFeedbackSubject.next(false);
  }

  relaod(msgFeedback: string) {
    this.client.showFeedback = true;
    //this.client.messaggioFeedback=msgFeedback;
    this.client.feedback = new Feedback(true, TIPO_FEEDBACK.SUCCESS, msgFeedback)
    //this.ngOnInit();
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
  }

  parsingFloat(el) {
    if (el == '' || el == '-') el = null;
    el = el ? parseFloat(el.toString().replaceAll('.', '').replace(',', '.')) : el;
    return el;
  }

  controllasalvataggioisee(): boolean {
    //se isee non salvato non faccio andare avanti
    let datarilascio = null;
    let datarilascioprec = null;
    let datascadenza = null;
    let datascadenzaprec = null;
    if (this.domanda.isee.isee_data_rilascio != null)
      datarilascio = new Date(this.domanda.isee.isee_data_rilascio).getTime();
    if (this.iseeprecedente.isee_data_rilascio != null)
      datarilascioprec = new Date(this.iseeprecedente.isee_data_rilascio).getTime();
    if (this.domanda.isee.isee_scadenza != null)
      datascadenza = new Date(this.domanda.isee.isee_scadenza).getTime();
    if (this.iseeprecedente.isee_scadenza != null)
      datascadenzaprec = new Date(this.iseeprecedente.isee_scadenza).getTime();
    return this.parsingFloat(this.valoreisee) != this.iseeprecedente.isee_valore ||
      this.conformeOption != this.iseeprecedente.isee_verificato_conforme ||
      datarilascio != datarilascioprec ||
      datascadenza != datascadenzaprec;
  }

  richiestaRevoca(notaPerRevoca: string) {
    const modalRef = this.modalService.open(PopuppopupRevocaBuonoComponent, { size: 'md' });
    modalRef.componentInstance.notaPerRevoca = notaPerRevoca;
  }


  getVerificheEnte() {
    this.client.getVerificheEnte(this.numerodomanda).subscribe(
      (response: ModelVerificheEnte) => {
        this.verificheEnteGestore = response;
      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }
}

