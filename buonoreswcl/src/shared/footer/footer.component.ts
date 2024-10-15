/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { Component, OnInit } from '@angular/core';
import { FOOTER_CONSTANTS } from '../../constants/buonores-constants';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit {

  portal = null;

  assistenza: String = '';
  help: String = '';
  write: String = '';
  sistema: String = '';
  servizio: String = '';
  pIva: String = '';
  form: String = '';
  link: String='';


  constructor() { }

  ngOnInit() {

    this.assistenza = FOOTER_CONSTANTS.ASSISTENZA;
    this.help = FOOTER_CONSTANTS.HELP;
    this.write = FOOTER_CONSTANTS.WRITE;
    this.form = FOOTER_CONSTANTS.FORM;
    this.sistema = FOOTER_CONSTANTS.SISTEMA;
    this.servizio = FOOTER_CONSTANTS.SERVIZIO;
    this.pIva = FOOTER_CONSTANTS.P_IVA;
    this.link= FOOTER_CONSTANTS.LINK;
  }
}
