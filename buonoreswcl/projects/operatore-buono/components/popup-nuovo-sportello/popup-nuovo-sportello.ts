import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { BuonoresBOClient } from '@buonores-app/app/BuonoresBOClient';
import { Sportelli } from '@buonores-app/app/dto/Sportelli';
import { Sportello } from '@buonores-app/app/dto/Sportello';
import { AppToastService } from '@buonores-shared/toast/app-toast.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';


@Component({
  selector: 'app-popup-nuovo-sportello',
  templateUrl: './popup-nuovo-sportello.html',
  styleUrls: ['./popup-nuovo-sportello.css'],

})
export class PopupNuovoSportelloComponent implements OnInit {

  //Form e validazione
  nuovoSportelloForm: FormGroup;
  controlloDataInizio: boolean = false;
  controlloDataFine: boolean = false;
  dataInizioMin: string;
  dataInizioError: string;
  dataFineMin: string;

  //Sportello Cod
  ultimoSportello: Sportello = this.client.listaSportelliSalvato.sportelli[0];
  nuovoSportello: Sportello = new Sportello("", "", "", "", "", false);


  constructor(private client: BuonoresBOClient, private fb: FormBuilder, public activeModal: NgbActiveModal, public toastService: AppToastService) { }
  //ON INIT
  ngOnInit() {

    //Date minime DatePicker
    let data = new Date(this.ultimoSportello.dataFine);
    data.setDate(data.getDate() + 1)
    let anno = data.getFullYear();
    let mese = (data.getMonth() + 1).toString().padStart(2, '0');
    let giorno = data.getDate().toString().padStart(2, '0');
    this.dataInizioMin = anno + "-" + mese + "-" + giorno;

    data = new Date(this.ultimoSportello.dataFine);
    data.setDate(data.getDate())
    anno = data.getFullYear();
    mese = (data.getMonth() + 1).toString().padStart(2, '0');
    giorno = data.getDate().toString().padStart(2, '0');
    this.dataInizioError = giorno + "/" + mese + "/" + anno;

    data = new Date(this.ultimoSportello.dataFine);
    data.setDate(data.getDate() + 2)
    anno = data.getFullYear();
    mese = (data.getMonth() + 1).toString().padStart(2, '0');
    giorno = data.getDate().toString().padStart(2, '0');
    this.dataFineMin = anno + "-" + mese + "-" + giorno;

    //Creazione form
    this.nuovoSportelloForm = this.fb.group({
      anno: new FormControl("", [Validators.required]),
      dataInizio: new FormControl(this.dataInizioMin, [Validators.required]),
      dataFine: new FormControl("", [Validators.required]),
      descrizione: new FormControl("", [Validators.required]),
    });



  }

  //FORM GETTERS
  get dataInizio() {
    return this.nuovoSportelloForm.get('dataInizio');
  }

  get dataFine() {
    return this.nuovoSportelloForm.get('dataFine');
  }

  get anno() {
    return this.nuovoSportelloForm.get('anno');
  }

  get descrizione() {
    return this.nuovoSportelloForm.get('descrizione');
  }

  //METODI

  //Esegue le validazioni delle date inserite
  validazioneDate() {
    if (this.dataInizio.value) {
      this.anno.setValue(this.dataInizio.value.slice(0, 4));

      //Controllo Data inizio
      if (this.dataInizio.value <= this.ultimoSportello.dataFine) {
        this.controlloDataInizio = true;
      } else {
        this.controlloDataInizio = false;
      }

      let data = new Date(this.dataInizio.value);
      data.setDate(data.getDate() + 1)
      let anno = data.getFullYear();
      let mese = (data.getMonth() + 1).toString().padStart(2, '0');
      let giorno = data.getDate().toString().padStart(2, '0');
      this.dataFineMin = anno + "-" + mese + "-" + giorno;

      //Controllo Data fine
      if (this.dataFine.value) {
        if (this.dataFine.value <= this.dataInizio.value) {
          this.controlloDataFine = true;
        } else {
          this.controlloDataFine = false;
        }
      }
    }
  }

  //Assembla il codice del nuovo sportello
  creaCodSportello(): string {
    const mese = this.dataInizio.value.slice(5, 7);
    const partiCodSportello = this.ultimoSportello.codSportello.split("_");
    const contributo = partiCodSportello[2];
    const seqUltimo = partiCodSportello[3];
    const seqNuovo = parseInt(seqUltimo) + 1
    let zero = "0"
    if (seqNuovo > 9) {
      zero = "";
    }
    return (this.anno.value + "_" + mese + "_" + contributo + "_" + zero + seqNuovo);
  }

  //Assembla lo sportello e richiama il servizio di insert
  creaSportello() {
    //recupero dati sportello
    this.nuovoSportello.codSportello = this.creaCodSportello();
    this.nuovoSportello.anno = this.anno.value;
    this.nuovoSportello.descSportello = this.descrizione.value;
    this.nuovoSportello.dataInizio = this.dataInizio.value;
    this.nuovoSportello.dataFine = this.dataFine.value;

    //servizio insert sportello
    this.client.spinEmitter.emit(true);
    this.client.creaNuovoSportello(this.nuovoSportello).subscribe((response: boolean) => {
      if (response) {
        this.toastService.showSuccess({ text: 'Sportello creato correttamente' });
        this.client.getSportelli().subscribe(
          (sportelli: Sportelli) => {
            this.client.listaSportelliSalvato = sportelli;
            this.client.spinEmitter.emit(false);
          },
          err => {
            this.client.spinEmitter.emit(false);
          }
        );
      } else {
        this.client.spinEmitter.emit(false);
        this.toastService.showError({ text: 'Errore nelle creazione dello sportello' });
      }
      this.activeModal.close(true);
    },
      err => {
        this.client.spinEmitter.emit(false);
      })
  }
}
