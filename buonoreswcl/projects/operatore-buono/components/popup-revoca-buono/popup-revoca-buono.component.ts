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
  templateUrl: './popup-revoca-buono.component.html',
  styleUrls: ['./popup-revoca-buono.component.css'],

})
export class PopuppopupRevocaBuonoComponent implements OnInit {


  dataSistema = new Date(new Date().getFullYear(), 0, 1);
  dataChiusura: Date;
  chiusuraForm: FormGroup;

  @Input() public notaPerRevoca: string;


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
      notaInterna: [''],
      notaEnte: ['']
    });
  }


  conferma() {

  }





  setValueDataCreazione() {
    this.dataChiusura = this.chiusuraForm.controls.dataChiusura.value;
  }

}
