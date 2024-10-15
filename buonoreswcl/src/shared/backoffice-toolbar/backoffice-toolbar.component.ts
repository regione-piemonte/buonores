/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BuonoresBOClient } from '@buonores-app/app/BuonoresBOClient';
import { UserInfoBuonores } from '@buonores-app/app/dto/UserInfoBuonores';
import { PROJECT_CONSTANTS } from '@buonores-app/constants/buonores-constants';
import { environment } from '@buonores-app/environments/environment';
import { BuonoresErrorService } from '../error/buonores-error.service';

@Component({
  selector: 'app-backoffice-toolbar',
  templateUrl: './backoffice-toolbar.component.html',
  styleUrls: ['./backoffice-toolbar.component.css']
})
export class BackofficeToolbarComponent implements OnInit {

  titlePage: String = PROJECT_CONSTANTS.BACKOFFICE_TITLE;
  userLogged: UserInfoBuonores;

   constructor(public client: BuonoresBOClient, private router: Router, private BuonoresError: BuonoresErrorService, private route: ActivatedRoute) { }

  ngOnInit() {
    /* this.client.login().subscribe(
      (response: UserInfoBuonores ) => {
          this.userLogged = response;
          if (response.ruolo == ROLES.OPERATORE_REGIONALE || response.ruolo == ROLES.SUPERUSER){
            this.client.ruolo = response.ruolo;
            this.router.navigate( ['operatore-regionale'], { skipLocationChange: true } );
          }
          else  if (response.ruolo == ROLES.RESPONSABILE_ENTE){
            this.client.ruolo = response.ruolo;
            //prendo l'ente dalla map
            let map = new Map(Object.entries(this.userLogged.enteprofilo));
            for (let entry of map.entries()) {
                  this.router.navigate( ['responsabile-ente'], { skipLocationChange: true, state: { idEnte: entry[0]} } );
          }
          }
          else  if (response.ruolo == ROLES.RESPONSABILE_MULTIENTE){
            this.client.ruolo = response.ruolo;
            this.client.listaenti = this.userLogged.enteprofilo;
            this.router.navigate( ['responsabile-multiente'], { skipLocationChange: true, state: { idEnte: this.client.listaenti} } );
          }
          else{
            this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...Error, errorDesc : 'Errore nel ruolo' }))
          }
      },
      err => {}
    )*/
     this.client.selectprofiloazione().subscribe(
      (response: UserInfoBuonores ) => {
          this.userLogged = response;
		  this.client.verificaprofilo(response);
      },
      err => {}
    )
  }

  goToPaginaPersonale() {
    window.open(environment.profilo, "_blank");
  }

  goLogout() {
	 this.client.logout().subscribe(
      (response: UserInfoBuonores ) => {
	  window.open(environment.esci, "_self");
	 },
      err => {}
  )
  }


}
