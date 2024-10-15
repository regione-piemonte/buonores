import { Component,Input, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { BuonoresBOClient } from '@buonores-app/app/BuonoresBOClient';
import { ModelCriteriGraduatoria } from '@buonores-app/app/dto/ModelCriteriGraduatoria';
import { ModelNuovaGraduatoria } from '@buonores-app/app/dto/ModelNuovaGraduatoria';
import { Sportello } from '@buonores-app/app/dto/Sportello';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { forkJoin } from 'rxjs';
import { DecimalPipe } from '@angular/common';
import { ModelArea } from '@buonores-app/app/dto/ModelArea';

@Component({
  selector: 'app-popup-nuova-graduatoria',
  templateUrl: './popup-nuova-graduatoria.html',
  styleUrls: ['./popup-nuova-graduatoria.css'],

})
export class PopupNuovaGraduatoriaComponent implements OnInit {

  @Input() public tipo: string;

  //Ultimo sportello
  ultimoSportello: Sportello;

  //Aree graduatoria
  aree: ModelArea[];

  //Form
  nuovaGraduatoriaForm: FormGroup;

  //Tabella Criteri
  displayedColumnsCriteri: string[] = ['ordinamentoCriterio', 'ordinamentoDirezione'];
  criteriGraduatoria: ModelCriteriGraduatoria[] = [];

  //Nuova graduatoria
  nuovaGraduatoria: ModelNuovaGraduatoria = new ModelNuovaGraduatoria("", "", 0, 0, 0, 0, 0);

  constructor(public client: BuonoresBOClient, private fb: FormBuilder, public activeModal: NgbActiveModal) { }
  //ON INIT
  ngOnInit() {
    this.client.spinEmitter.emit(true);
    //Ultimo sportello

    forkJoin({
      ultimoSportello: this.client.getUltimoSportelloChiuso(),
      listaCriteriOrdinamento: this.client.getCriteriGraduatoria(),
      aree: this.client.getAree(),
    }).subscribe(({ ultimoSportello, listaCriteriOrdinamento, aree }) => {
      if (ultimoSportello) {
        this.ultimoSportello = ultimoSportello as any;
        this.nuovaGraduatoriaForm.controls.codSportello.setValue(this.ultimoSportello.codSportello);
      }
      if (listaCriteriOrdinamento) {
        this.client.listaCriteriOrdinamento = listaCriteriOrdinamento as ModelCriteriGraduatoria[];
      }
      if (aree) {
        this.aree = aree as ModelArea[];
      }
      this.client.spinEmitter.emit(false);
    },
    err => {
      this.client.spinEmitter.emit(false)
    });

    if(this.tipo === 'Nuova') {
      //Creazione graduatoria form
      this.nuovaGraduatoriaForm = this.fb.group({
        codSportello: new FormControl("", [Validators.required]),
        descrizione: new FormControl("", [Validators.required]),
        importoGenerale: new FormControl(null, { validators: [Validators.required, Validators.pattern(/^[0-9.]+(\,\d{1,2})?$/)], updateOn: 'blur' }),
        importoA1: new FormControl(null, { validators: [Validators.required, Validators.pattern(/^[0-9.]+(\,\d{1,2})?$/)], updateOn: 'blur' }),
        importoA2: new FormControl(null, { validators: [Validators.required, Validators.pattern(/^[0-9.]+(\,\d{1,2})?$/)], updateOn: 'blur' }),
        importoMensile: new FormControl(null, { validators: [Validators.required, Validators.pattern(/^[0-9.]+(\,\d{1,2})?$/)], updateOn: 'blur' }),
        // importoMensile: [],
        numeroMesi: [null, [Validators.required, Validators.pattern(/^[0-9]+$/)]],
      });
    } else if(this.tipo === 'Simula') {
      //Simulazione graduatoria form
      this.nuovaGraduatoriaForm = this.fb.group({
        codSportello: new FormControl(this.ultimoSportello, [Validators.required]),
        descrizione: new FormControl(this.client.graduatoriaDescrizione.descrizioneGraduatoria, [Validators.required]),
        importoGenerale: new FormControl(this.convertiNumeroInItaliano(this.client.parametriFinanziabili[0].importoTotale), { validators: [Validators.required, Validators.pattern(/^[0-9.]+(\,\d{1,2})?$/)], updateOn: 'blur' }),
        importoA1: new FormControl(this.convertiNumeroInItaliano(this.client.parametriFinanziabili[1].importoTotale), { validators: [Validators.required, Validators.pattern(/^[0-9.]+(\,\d{1,2})?$/)], updateOn: 'blur' }),
        importoA2: new FormControl(this.convertiNumeroInItaliano(this.client.parametriFinanziabili[2].importoTotale), { validators: [Validators.required, Validators.pattern(/^[0-9.]+(\,\d{1,2})?$/)], updateOn: 'blur' }),
        importoMensile: new FormControl(this.convertiNumeroInItaliano(this.client.buonoMensile), { validators: [Validators.required, Validators.pattern(/^[0-9.]+(\,\d{1,2})?$/)], updateOn: 'blur' }),
        // importoMensile: [],
        numeroMesi: [this.client.mesiPerDestinatario, [Validators.required, Validators.pattern(/^[0-9]+$/)]],
      });
    }
  }

  //FORM GETTERS
  get codSportello() {
    return this.nuovaGraduatoriaForm.get('codSportello');
  }

  get descrizione() {
    return this.nuovaGraduatoriaForm.get('descrizione');
  }

  get importoGenerale() {
    return this.nuovaGraduatoriaForm.get('importoGenerale');
  }

  get importoA1() {
    return this.nuovaGraduatoriaForm.get('importoA1');
  }

  get importoA2() {
    return this.nuovaGraduatoriaForm.get('importoA2');
  }

  get importoMensile() {
    return this.nuovaGraduatoriaForm.get('importoMensile');
  }

  get numeroMesi() {
    return this.nuovaGraduatoriaForm.get('numeroMesi');
  }

  //METODI
  convertiNumeroInItaliano(numero: number): string {
    const numeroItaliano = Number(numero).toLocaleString('it-IT');
    return numeroItaliano;
  }

  formatta(formControl: string) {
    let input = this.nuovaGraduatoriaForm.get(formControl).value
    if (input) {
      input = input.replace(/\./g, "");
      input = input.replace(",", ".");
    }

    const output = parseFloat(input).toLocaleString('it-IT')
    if (output != "NaN") {
      this.nuovaGraduatoriaForm.get(formControl).setValue(output)
    } else {
      this.nuovaGraduatoriaForm.get(formControl).setValue("")
    }
  }

  stringToNumber(input: string): number {
    if (input) {
      input = input.replace(/\./g, "");
      input = input.replace(",", ".");
      return parseFloat(input);
    } else {
      return null;
    }
  }

  conferma() {
    this.client.spinEmitter.emit(true);
    this.nuovaGraduatoria.codSportello = this.nuovaGraduatoriaForm.controls.codSportello.value
    this.nuovaGraduatoria.descrizione = this.nuovaGraduatoriaForm.controls.descrizione.value
    this.nuovaGraduatoria.importoGenerale = this.stringToNumber(this.nuovaGraduatoriaForm.controls.importoGenerale.value)
    this.nuovaGraduatoria.importoA1 = this.stringToNumber(this.nuovaGraduatoriaForm.controls.importoA1.value)
    this.nuovaGraduatoria.importoA2 = this.stringToNumber(this.nuovaGraduatoriaForm.controls.importoA2.value)
    this.nuovaGraduatoria.importoMensile = this.stringToNumber(this.nuovaGraduatoriaForm.controls.importoMensile.value)
    this.nuovaGraduatoria.numeroMesi = this.nuovaGraduatoriaForm.controls.numeroMesi.value
    if (this.tipo === 'Nuova') {
      this.client.creaNuovaGraduatoria(this.nuovaGraduatoria).subscribe((response: boolean) => {
        this.client.spinEmitter.emit(false);
        if (response) {
          this.activeModal.close(true);
        } else {
          this.activeModal.close(false);
        }
      });
    } else if (this.tipo === 'Simula') {
      this.client.simulazioneGraduatoria(this.nuovaGraduatoria).subscribe((response: boolean) => {
        this.client.spinEmitter.emit(false);
        if (response) {
          this.activeModal.close(true);
        } else {
          this.activeModal.close(false);
        }
      });
    }

  }

}
