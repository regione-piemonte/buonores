import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog, MatTableDataSource } from '@angular/material';
import { ActivatedRoute, Navigation, Router } from '@angular/router';
import { BuonoresBOClient } from '@buonores-app/app/BuonoresBOClient';
import { Feedback } from '@buonores-app/app/dto/Feedback';
import { ModelRichiesta } from '@buonores-app/app/dto/ModelRichiesta';
import { ALLEGATI, AZIONE, DATIENTE_INFO, STATO_DOMANDA, TIPO_FEEDBACK, VALUTAZIONE } from '@buonores-app/constants/buonores-constants';
import { CustomDialogComponent } from '@buonores-app/shared/custom-dialog/custom-dialog.component';
import { BuonoresErrorService } from '@buonores-app/shared/error/buonores-error.service';
import { AppToastService } from '@buonores-shared/toast/app-toast.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BehaviorSubject, Subject, forkJoin } from 'rxjs';
//import { CronologiaComponent } from '../cronologia/cronologia.component';
import { ModelVerifiche } from '@buonores-app/app/dto/ModelVerifiche';
import { ModelVerificheEnte } from '@buonores-app/app/dto/ModelVerificheEnte';
import { ValidaDati } from '@buonores-app/shared/controlli/validadati';
import { ModelVisualizzaVerifiche } from '@buonores-app/app/dto/ModelVisualizzaVerifiche';
import { HostListener, Input } from '@angular/core';


@Component({
  selector: 'app-dati-istanza-ente',
  templateUrl: './dati-istanza-ente.component.html',
  styleUrls: ['./dati-istanza-ente.component.css'],
  animations: [
    // Each unique animation requires its own trigger. The first argument of the trigger function is the name
    trigger('rotatedState', [
      state('rotated', style({ transform: 'rotate(0)' })),
      state('default', style({ transform: 'rotate(-180deg)' })),
      transition('rotated => default', animate('100ms ease-out')),
      transition('default => rotated', animate('100ms ease-in')),
    ]),
  ],
})
export class DatiIstanzaEnteComponent implements OnInit {

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
    date: Date,
  };
  feedback: Feedback;
  numerodomanda: string;
  showFeedbackSubject: Subject<boolean> = new BehaviorSubject<boolean>(false);
  showFeedback: boolean;
  tipoFeedback: string;
  messaggioFeedback: string;
  domanda: ModelRichiesta;
  disabilitaAllegati: boolean;
  disabilitaAvviaVerifica: boolean;
  disabilitaConcludiVerifica: boolean;
  disabilitaSalvaVerifica: boolean;
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
  conformeOption: boolean = null;
  valoreisee: string;
  verificheEnteGestore: ModelVerificheEnte = new ModelVerificheEnte(false,'',new Date());
  listaVerifiche: MatTableDataSource<ModelVisualizzaVerifiche>;
  displayedColumns: string[] = ['controllo','fonte','dataControllo','note','noteRichiesta','dataRichiesta'];
  isTableVisible: boolean = false;
  espansa: boolean = false;

  numeroDomande: string[] = [];

  valida: ValidaDati = new ValidaDati();
  paginaRitorno: string;
  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private dialog: MatDialog,
    public client: BuonoresBOClient,
    private modalService: NgbModal,
    public toastService: AppToastService,
    private buonoresError: BuonoresErrorService
  ) {
    this.navigation = this.router.getCurrentNavigation();
    let domandaValues: string[] = [];
    this.route.fragment.subscribe((frag: string) => {
      domandaValues.push(frag);
    });
    this.numerodomanda =
      this.navigation.extras && this.navigation.extras.state
        ? this.navigation.extras.state.numerodomanda
        : domandaValues[0][0];
    this.paginaRitorno =
      this.navigation.extras && this.navigation.extras.state
        ? this.navigation.extras.state.paginaRitorno
        : domandaValues[0][0];
  }

  ngOnInit() {
    this.showFeedback = this.client.showFeedback;
    this.dataSistema.setHours(0, 0, 0, 0);

    this.client.spinEmitter.emit(true);
    this.showMessage();
    this.listaVerifiche = new MatTableDataSource<ModelVisualizzaVerifiche>();
    forkJoin({
      schedaDomanda: this.client.getRichiestaNumero(this.numerodomanda),
      verificheCronologia: this.client.getverifiche(this.numerodomanda)
    }).subscribe(
      ({ schedaDomanda, verificheCronologia }) => {
        this.client.azioni;
        this.domanda = schedaDomanda;
        this.valoreisee = this.transform(this.domanda.isee.isee_valore);
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
          if (this.domanda.allegati.findIndex(
            (element) => element.tipo == ALLEGATI.VERBALE_UMVD) != -1) {
            this.verbale = this.domanda.allegati.filter(
              (element) => element.tipo == ALLEGATI.VERBALE_UMVD
            )[0].filename;
            this.tipoallegato = this.domanda.allegati.filter(
              (element) => element.tipo == ALLEGATI.VERBALE_UMVD
            )[0].tipo;
            this.urlVerbale = this.client.getAllegato(
              this.domanda.numero,
              this.tipoallegato
            );
          }
        } else if (
          this.domanda.valutazione_multidimensionale == VALUTAZIONE.UVG
        ) {
          if (
            this.domanda.allegati.findIndex(
              (element) => element.tipo == ALLEGATI.VERBALE_UVG
            ) != -1
          ) {
            this.verbale = this.domanda.allegati.filter(
              (element) => element.tipo == ALLEGATI.VERBALE_UVG
            )[0].filename;
            this.tipoallegato = this.domanda.allegati.filter(
              (element) => element.tipo == ALLEGATI.VERBALE_UVG
            )[0].tipo;
            this.urlVerbale = this.client.getAllegato(
              this.domanda.numero,
              this.tipoallegato
            );
          }
        }
        if (
          this.domanda.allegati.findIndex(
            (element) => element.tipo == ALLEGATI.DELEGA
          ) != -1
        ) {
          this.delega = this.domanda.allegati.filter(
            (element) => element.tipo == ALLEGATI.DELEGA
          )[0].filename;
          this.urlDelega = this.client.getAllegato(
            this.domanda.numero,
            ALLEGATI.DELEGA
          );
        }
        if (
          this.domanda.allegati.findIndex(
            (element) => element.tipo == ALLEGATI.NOMINA_TUTORE
          ) != -1
        ) {
          this.tutela = this.domanda.allegati.filter(
            (element) => element.tipo == ALLEGATI.NOMINA_TUTORE
          )[0].filename;
          this.urlTutela = this.client.getAllegato(
            this.domanda.numero,
            ALLEGATI.NOMINA_TUTORE
          );
        }
        if (
          this.domanda.allegati.findIndex(
            (element) => element.tipo == ALLEGATI.CARTA_IDENTITA
          ) != -1
        ) {
          this.cartaidentita = this.domanda.allegati.filter(
            (element) => element.tipo == ALLEGATI.CARTA_IDENTITA
          )[0].filename;
          this.urlCartaIdentita = this.client.getAllegato(
            this.domanda.numero,
            ALLEGATI.CARTA_IDENTITA
          );
        }
        if (
          this.domanda.allegati.findIndex(
            (element) => element.tipo == ALLEGATI.PROCURA_SPECIALE
          ) != -1
        ) {
          this.procuraspeciale = this.domanda.allegati.filter(
            (element) => element.tipo == ALLEGATI.PROCURA_SPECIALE
          )[0].filename;
          this.urlProcuraSpeciale = this.client.getAllegato(
            this.domanda.numero,
            ALLEGATI.PROCURA_SPECIALE
          );
        }
        if (
          this.domanda.allegati.findIndex(
            (element) => element.tipo == ALLEGATI.LETTERA_INCARICO
          ) != -1
        ) {
          this.letteraincarico = this.domanda.allegati.filter(
            (element) => element.tipo == ALLEGATI.LETTERA_INCARICO
          )[0].filename;
          this.urlLetteraIncarico = this.client.getAllegato(
            this.domanda.numero,
            ALLEGATI.LETTERA_INCARICO
          );
        }
        if (
          this.domanda.allegati.findIndex(
            (element) => element.tipo == ALLEGATI.CONTRATTO_RSA
          ) != -1
        ) {
          this.contratto = this.domanda.allegati.filter(
            (element) => element.tipo == ALLEGATI.CONTRATTO_RSA
          )[0].filename;
          this.urlContratto = this.client.getAllegato(
            this.domanda.numero,
            ALLEGATI.CONTRATTO_RSA
          );
        }
        if (
          this.domanda.allegati.findIndex(
            (element) => element.tipo == ALLEGATI.CONTRODEDUZIONE
          ) != -1
        ) {
          this.controdeduzione = this.domanda.allegati.filter(
            (element) => element.tipo == ALLEGATI.CONTRODEDUZIONE
          )[0].filename;
          this.urlControdeduzione = this.client.getAllegato(
            this.domanda.numero,
            ALLEGATI.CONTRODEDUZIONE
          );
        }

        // Verifiche domanda
        this.listaVerifiche.data = verificheCronologia as any;

        this.client.spinEmitter.emit(false);
      },
      (err) => {
        this.client.spinEmitter.emit(false);
      }
    );
    this.getVerificheEnte();
    this.client.feedback = null;
    this.client.showFeedback = false;
  }

  onChangeConforme(valore: string) {
    if (valore == 'Si') {
      if (this.conformeOption == null) this.conformeOption = true;
      else if (this.conformeOption == false) this.conformeOption = true;
      else if (this.conformeOption == true) this.conformeOption = null;
    } else if (valore == 'No') {
      if (this.conformeOption == null) this.conformeOption = false;
      else if (this.conformeOption == true) this.conformeOption = false;
      else if (this.conformeOption == false) this.conformeOption = null;
    }
  }

  checkAzione(azione: string) {
    switch (azione) {
      case 'scarica_allegato':
        return this.client.azioni.some(
          (a) => a.codAzione == AZIONE.OP_ScaricaAllegati
        );
      case 'salva_verifica_ente':
        return this.client.azioni.some(
          (a) => a.codAzione == AZIONE.OP_Salva_Verifica_Ente
        );
      case 'avvia_verifica_ente':
        return this.client.azioni.some(
          (a) => a.codAzione == AZIONE.OP_Avvia_Verifica_Ente
        );
      case 'concludi_verifica_ente':
        return this.client.azioni.some(
          (a) => a.codAzione == AZIONE.OP_Concludi_Verifica_Ente
        );
      case 'verifiche':
        return this.client.azioni.some(
          (a) => a.codAzione == AZIONE.OP_VisualizzaVerifiche
        );
      default:
        return false;
    }
  }

  abilitabottoni() {
    if (
      this.domanda != null &&
      this.domanda != undefined &&
      this.domanda.stato === STATO_DOMANDA.INVIATA
    )
      this.disabilitaAllegati = true;
    else this.disabilitaAllegati = false;

    //Avvia Verifica
    if (
      this.checkStato() &&
      this.domanda != undefined &&
      this.domanda.verifiche.verifica_eg_richiesta &&
      !(
        this.domanda.verifiche.verifica_eg_in_corso ||
        this.domanda.verifiche.verifica_eg_conclusa
      )
    ) {
      this.disabilitaAvviaVerifica = false;
    } else {
      this.disabilitaAvviaVerifica = true;
    }

    //Concludi Verifica
    if (
      this.checkStato() &&
      this.domanda != undefined &&
      this.domanda.verifiche.verifica_eg_in_corso &&
      !(
        this.domanda.verifiche.verifica_eg_richiesta ||
        this.domanda.verifiche.verifica_eg_conclusa
      )
    ) {
      this.disabilitaConcludiVerifica = false;
    } else {
      this.disabilitaConcludiVerifica = true;
    }

    //Salva Verifica
    if (
      this.checkStato() &&
      this.domanda != undefined &&
      !(
        this.domanda.verifiche.verifica_eg_richiesta ||
        this.domanda.verifiche.verifica_eg_in_corso
      )
    ) {
      this.disabilitaSalvaVerifica = false;
    } else {
      this.disabilitaSalvaVerifica = true;
    }
  }

  salvaVerificaEnte() {
    this.client.spinEmitter.emit(true);
    if (this.conformeOption == true)
      this.domanda.verifiche.verifica_eg_incompatibilita = true;
    else if (this.conformeOption == false)
      this.domanda.verifiche.verifica_eg_incompatibilita = false;
    else this.domanda.verifiche.verifica_eg_incompatibilita = null;
    let verifica: ModelVerificheEnte = new ModelVerificheEnte(
      this.conformeOption,
      this.verificheEnteGestore.noteEnte,
      new Date()
    );
    this.client.salvaverificaente(verifica, this.numerodomanda).subscribe(
      (res) => {
        if (res) {
          this.domanda.verifiche.verifica_eg_conclusa = true;
          this.domanda.verifiche.verifica_eg_in_corso = null;
          this.domanda.verifiche.verifica_eg_richiesta = null;
          this.verificheEnteGestore.noteEnte = null;
          this.toastService.showSuccess({ text: 'Verifica salvata' });
          this.numeroDomande = [];
          this.client.ricercaDomAperte = [];
          this.abilitabottoni();
          this.disabilitaSalvaVerifica = true;
          this.client.spinEmitter.emit(false);
        } else {
          this.numeroDomande = [];
          this.client.spinEmitter.emit(false);
        }
      },
      (err) => {
        this.numeroDomande = [];
        this.client.spinEmitter.emit(false);
      }
    );
  }

  avviaVerificaEnte() {
    this.client.spinEmitter.emit(true);
    this.numeroDomande.push(this.numerodomanda);
    this.client.updateToVerificaInCorso(this.numeroDomande).subscribe(
      (res) => {
        if (res) {
          this.domanda.verifiche.verifica_eg_conclusa = null;
          this.domanda.verifiche.verifica_eg_in_corso = true;
          this.domanda.verifiche.verifica_eg_richiesta = null;
          this.toastService.showSuccess({ text: 'Verifica in corso' });
          this.numeroDomande = [];
          this.client.ricercaDomAperte = [];
          this.abilitabottoni();
          this.client.spinEmitter.emit(false);
        } else {
          this.numeroDomande = [];
          this.client.spinEmitter.emit(false);
        }
      },
      (err) => {
        this.numeroDomande = [];
        this.client.spinEmitter.emit(false);
      }
    );
  }

  concludiVerificaEnte() {
    let message = '';
    let titolo = '';
    message = `Sei sicuro di voler concludere la verifica della domanda`;
    titolo = `Concludi Verifica Ente`;
    const dialogRef = this.dialog.open(CustomDialogComponent, {
      data: {
        messaggio: message,
        titolo: titolo,
        conferma: 'CONFERMA',
        annulla: 'ANNULLA',
      },
      disableClose: true,
    });
    dialogRef.afterClosed().subscribe((result: boolean) => {
      if (result) {
        this.client.spinEmitter.emit(true);
        this.domanda.verifiche.verifica_eg_incompatibilita = this.conformeOption;
        let verifica: ModelVerificheEnte = new ModelVerificheEnte(this.conformeOption,this.verificheEnteGestore.noteEnte,new Date());
        this.client.concludiverificaente(verifica, this.numerodomanda).subscribe((res) => {
            if (res) {
              this.domanda.verifiche.verifica_eg_conclusa = true;
              this.domanda.verifiche.verifica_eg_in_corso = null;
              this.domanda.verifiche.verifica_eg_richiesta = null;
              this.verificheEnteGestore.noteEnte = null;
              this.toastService.showSuccess({ text: 'Verifica conclusa' });
              this.client.ricercaDomAperte = [];
              this.abilitabottoni();
              this.disabilitaSalvaVerifica = true;
              this.client.spinEmitter.emit(false);
            } else {
              this.client.spinEmitter.emit(false);
            }
          },
          (err) => {
            this.client.spinEmitter.emit(false);
          }
        );
      } else {
        this.client.spinEmitter.emit(false);
      }
    });
  }

  backButton() {
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
      default:
        this.router.navigate(['selezione-profilo-applicativo'], {
          skipLocationChange: true,
        });
        break;
    }
  }

  showMessage() {
    if (
      this.client.feedback !== null &&
      this.client.feedback !== undefined &&
      this.client.showFeedback
    ) {
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
    this.client.feedback = new Feedback(
      true,
      TIPO_FEEDBACK.SUCCESS,
      msgFeedback
    );
    //this.ngOnInit();
  }

  isNumber(value: string | number): boolean {
    return (
      value != null &&
      value !== '' &&
      !isNaN(Number(value.toString().replace(',', '.')))
    );
  }

  transform(
    value: number,
    currencySign: string = '',
    decimalLength: number = 2,
    chunkDelimiter: string = '.',
    decimalDelimiter: string = ',',
    chunkLength: number = 3
  ): string {
    let result =
      '\\d(?=(\\d{' +
      chunkLength +
      '})+' +
      (decimalLength > 0 ? '\\D' : '$') +
      ')';
    if (this.isNumber(value)) {
      if (value != null && value != undefined) {
        let num = value.toFixed(Math.max(0, ~~decimalLength));
        return (
          currencySign +
          (decimalDelimiter ? num.replace('.', decimalDelimiter) : num).replace(
            new RegExp(result, 'g'),
            '$&' + chunkDelimiter
          )
        );
      }
    }
    return undefined;
  }

  checkStato() {
    return (
      this.domanda != null &&
      this.domanda.stato != STATO_DOMANDA.RINUNCIATA &&
      this.domanda.stato != STATO_DOMANDA.DINIEGO &&
      this.domanda.stato != STATO_DOMANDA.ANNULLATA
    );
  }

  getVerificheEnte() {
    this.client.getVerificheEnte(this.numerodomanda).subscribe(
      (response: ModelVerificheEnte) => {
        this.verificheEnteGestore = response;
        this.conformeOption =
          this.verificheEnteGestore.presenzaAltreIncompatibilita;
      },
      (err) => {
        this.client.spinEmitter.emit(false);
      }
    );
  }

  isControllo(controllo: string, element: ModelVisualizzaVerifiche): boolean {
    if (
      controllo == 'NESSUNA_INCOMPATIBILITA' &&
      element.tipo == 'NESSUNA_INCOMPATIBILITA'
    )
      return true;
    else if (controllo == 'ATECO' && element.tipo == 'ATECO') return true;
    else if (
      controllo == 'INCOMPATIBILITA_PER_CONTRATTO' &&
      element.tipo == 'INCOMPATIBILITA_PER_CONTRATTO'
    )
      return true;
    else if (controllo == 'ISEE' && element.tipo == 'ISEE') {
      return true;
    } else if (
      controllo == 'ISEE_CITTADINO' &&
      element.tipo == 'ISEE_CITTADINO'
    ) {
      return true;
    } else if (
      controllo == 'CONFORMITA_PSOCIALE' &&
      element.tipo == 'CONFORMITA_PSOCIALE'
    ) {
      return true;
    }
    // else if (controllo == 'PUNTEGGIO_PSOCIALE' && element.tipo == 'PUNTEGGIO_PSOCIALE') {
    //   return true;
    // }
    return false;
  }

  toggleTable() {
    if (this.espansa)
      this.espansa = false;
    else
      this.espansa = true;
    this.state = (this.state === 'default' ? 'rotated' : 'default');
  }

  @HostListener('document:updateCronoEmitter')
  updateCrono() {
    this.ngOnInit();
    this.espansa = false;
    this.state = 'rotated';
    this.toggleTable();
  }

  dateToLocale(data: string) {
    let dataTemp = new Date(data);
    return data;
  }
}
