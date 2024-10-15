/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { Component, OnInit } from '@angular/core';
import { BuonoresBOClient } from '@buonores-app/app/BuonoresBOClient';
import { ParametroBuonores } from '@buonores-app/app/dto/ParametroBuonores';
import { MENU } from '@buonores-app/constants/buonores-constants';


@Component({
  selector: 'app-project-toolbar',
  templateUrl: './project-toolbar.component.html',
  styleUrls: ['./project-toolbar.component.css']
})
export class ProjectToolbarComponent implements OnInit {


  projectTitle: String = 'Buono Residenzialita\'';
  parametriperhelp:	ParametroBuonores[];
  constructor(private client: BuonoresBOClient) { }

  ngOnInit() {
    //  this.client.getParametroPerInformativa(MENU.HELP).subscribe((msg: ParametroBuonores[]) => {
    //    this.parametriperhelp = msg;
   //   });
  }

  goToParametro(url:string){
    window.open(url, "_blank");
  }

}
