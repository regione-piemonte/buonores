import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { BuonoresBOClient } from '@buonores-app/app/BuonoresBOClient';
import { BuonoresErrorService } from '@buonores-app/shared/error/buonores-error.service';
import { CambioStatoPopUp } from '@buonores-app/app/dto/CambioStatoPopUp';
import { BuonoresError } from '@buonores-app/shared/error/buonores-error.model';
import { ModelRichiesta } from '@buonores-app/app/dto/ModelRichiesta';
import { ModelDatiDaModificare } from '@buonores-app/app/dto/ModelDatiDaModificare';
import { STATO_DOMANDA } from '@buonores-app/constants/buonores-constants';

const enum PathApi { }


@Component({
  selector: 'app-popup-cambistato',
  templateUrl: './popup-cambistato.component.html',
  styleUrls: ['./popup-cambistato.component.css'],

})
export class PopupCambistatoComponent implements OnInit {


  dataSistema = new Date(new Date().getFullYear(), 0, 1);
  dataChiusura: Date;
  chiusuraForm: FormGroup;
  nascondinotacittadino: boolean = false;

  @Input() public numerodomanda: string;
  @Input() public selectedAllItems: ModelDatiDaModificare[];
  @Input() public descStato: string;
  @Input() public stato: STATO_DOMANDA;

  constructor(public client: BuonoresBOClient, public activeModal: NgbActiveModal,
    private fb: FormBuilder, private buonoresError: BuonoresErrorService, private router: Router, private route: ActivatedRoute) { }

  errorMessage = {
    error: { detail: '' },
    message: '',
    name: '',
    status: '',
    statusText: '',
    url: '',
    date: Date
  }

  ngOnInit() {
    this.chiusuraForm = this.fb.group({
      notaCittadino: [''],
      notaInterna: [''],
      notaEnte: ['']
    });
    if (this.stato !== STATO_DOMANDA.AMMISSIBILE && this.stato !== STATO_DOMANDA.NON_AMMISSIBILE) {
      this.nascondinotacittadino = false;
      this.chiusuraForm.get('notaCittadino').setValidators([
        Validators.required
      ]);
    }
    else {
      this.nascondinotacittadino = true;
    }
  }


  conferma() {
    this.client.spinEmitter.emit(true);
    if (this.chiusuraForm.valid) {
      let chiuso: CambioStatoPopUp = new CambioStatoPopUp();
      chiuso.numerodomanda = this.numerodomanda;
      chiuso.notaEnte = this.chiusuraForm.controls.notaEnte.value;
      chiuso.notaInterna = this.chiusuraForm.controls.notaInterna.value;
      chiuso.notaCittadino = this.chiusuraForm.controls.notaCittadino.value;
      chiuso.datidamodificare = this.selectedAllItems;
      switch (this.stato) {
        case STATO_DOMANDA.DA_RETTIFICARE:
          this.rettifica(chiuso);
          break;
        case STATO_DOMANDA.AMMISSIBILE:
          this.ammissibile(chiuso);
          break;
        case STATO_DOMANDA.NON_AMMISSIBILE:
          this.nonammissibile(chiuso);
          break;
        case STATO_DOMANDA.PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA:
          this.preavvisopernonammissibilita(chiuso);
          break;
        case STATO_DOMANDA.AMMESSA:
          this.ammessa(chiuso);
          break;
        case STATO_DOMANDA.AMMESSA_CON_RISERVA:
          this.ammessaConRiserva(chiuso);
          break;
        case STATO_DOMANDA.AMMESSA_CON_RISERVA_IN_PAGAMENTO:
          this.ammessaConRiservaInPagamento(chiuso);
          break;
        case STATO_DOMANDA.IN_PAGAMENTO:
          this.inPagamento(chiuso);
          break;
        case STATO_DOMANDA.DINIEGO:
          this.diniego(chiuso);
          break;
        default:
          break;
      }
    } else {
      this.errorMessage.error.detail = "Nota al cittadino obbligatoria";
      this.buonoresError.handleWarning(BuonoresError.toBuonoresError({ ...this.errorMessage, errorDesc: this.errorMessage.error.detail }))

      this.client.spinEmitter.emit(false);
    }
  }

  rettifica(chiuso: CambioStatoPopUp) {
    this.client.datiDaModificarePost(chiuso).subscribe(
      (domanda: ModelRichiesta) => {
        this.activeModal.close(true);
        this.client.spinEmitter.emit(false);
      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }


  ammissibile(chiuso: CambioStatoPopUp) {

    this.client.ammissibile(chiuso).subscribe(
      () => {
        this.activeModal.close(true);
        this.client.spinEmitter.emit(false);
      },
      err => {
        this.activeModal.close(false);
        this.client.spinEmitter.emit(false);
      }
    );
  }

  nonammissibile(chiuso: CambioStatoPopUp) {

    this.client.nonammissibile(chiuso).subscribe(
      () => {
        this.activeModal.close(true);
        this.client.spinEmitter.emit(false);
      },
      err => {
        this.activeModal.close(false);
        this.client.spinEmitter.emit(false);
      }
    );
  }



  preavvisopernonammissibilita(chiuso: CambioStatoPopUp) {

    this.client.preavvisopernonammissibilita(chiuso).subscribe(
      () => {
        this.activeModal.close(true);
        this.client.spinEmitter.emit(false);
      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }


  ammessa(cambioStato: CambioStatoPopUp) {

    this.client.ammessa(cambioStato).subscribe(
      () => {
        this.activeModal.close(true);
        this.client.spinEmitter.emit(false);
      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }

  ammessaConRiserva(cambioStato: CambioStatoPopUp) {

    this.client.ammessaConRiserva(cambioStato).subscribe(
      () => {
        this.activeModal.close(true);
        this.client.spinEmitter.emit(false);
      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }

  ammessaConRiservaInPagamento(cambioStato: CambioStatoPopUp) {

    this.client.ammessaConRiservaInPagamento(cambioStato).subscribe(
      () => {
        this.activeModal.close(true);
        this.client.spinEmitter.emit(false);
      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }

  inPagamento(cambioStato: CambioStatoPopUp) {

    this.client.inPagamento(cambioStato).subscribe(
      () => {
        this.activeModal.close(true);
        this.client.spinEmitter.emit(false);
      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }

  diniego(cambioStato: CambioStatoPopUp) {

    this.client.diniego(cambioStato).subscribe(
      () => {
        this.activeModal.close(true);
        this.client.spinEmitter.emit(false);
      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }


  /*
  respingi(chiuso: CambioStatoPopUp) {

    this.client.respingi(chiuso).subscribe(
      () => {
        this.activeModal.close(true);
        this.client.spinEmitter.emit(false);
      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }
  */

  setValueDataCreazione() {
    this.dataChiusura = this.chiusuraForm.controls.dataChiusura.value;
  }

}
