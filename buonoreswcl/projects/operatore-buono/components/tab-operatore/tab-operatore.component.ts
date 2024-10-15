import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Navigation, NavigationExtras, Router } from '@angular/router';
import { BuonoresBOClient } from '@buonores-app/app/BuonoresBOClient';
import { AzioneBuonores } from '@buonores-app/app/dto/AzioneBuonores';
import { RuoloBuonores } from '@buonores-app/app/dto/RuoloBuonores';
import { UserInfoBuonores } from '@buonores-app/app/dto/UserInfoBuonores';
import { AZIONE } from '@buonores-app/constants/buonores-constants';
import { AppToastService } from '@buonores-shared/toast/app-toast.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from "rxjs";
import { Link } from '../../../../src/app/dto/Link';

@Component({
  selector: 'app-tab-operatore',
  templateUrl: './tab-operatore.component.html',
  styleUrls: ['./tab-operatore.component.css']
})
export class TabOperatoreComponent implements OnInit {
  // ------- New Dynamic
  public links: Link[] = [];

  // Costanti tab
  domandeAperte = {
    title: 'Domande Aperte',
    link: 'istanze-aperte',
    active: true
  }
  Archivio = {
    title: 'Archivio',
    link: 'archivio-domande',
    active: true
  }
  Istruttoria = {
    title: 'Buono Residenzialita\'',
    link: 'istruttoria',
    active: true
  }
  Graduatoria = {
    title: 'Graduatoria',
    link: 'graduatoria',
    active: true
  }
  RendicontazioneOp = {
    title: 'Buoni in Erogazione',
    link: 'rendicontazione-op',
    active: true
  }
  // verifichePostGraduatoria = {
  //   title: 'Verifiche post-Graduatoria',
  //   link: 'configuratore-prestazioni',
  //   active: true
  // }
  // inErogazione = {
  //   title: 'In Erogazione',
  //   link: 'cruscotto',
  //   active: true
  // }

  navigation: Navigation;
  cruscotto: boolean;
  active: number = 0;
  constructor(public route: ActivatedRoute, public client: BuonoresBOClient, private modalService: NgbModal, public toastService: AppToastService, private router: Router) {
    this.navigation = this.router.getCurrentNavigation();
    this.cruscotto = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.cruscotto : false;
  }

  ngOnInit(): void {
    if(this.client.azioni.some((azione) => azione.codAzione === AZIONE.OP_RicercaDomandeAperte)) {
      this.links.push(this.domandeAperte);
    }
    if (this.client.azioni.some((azione) => azione.codAzione === AZIONE.OP_Archivio)) {
    this.links.push(this.Archivio);
    }
    if (this.client.azioni.some((azione) => azione.codAzione === AZIONE.OP_Istruttoria)) {
      this.links.push(this.Istruttoria);
    }
    if (this.client.azioni.some((azione) => azione.codAzione === AZIONE.OP_Graduatoria)) {
      this.links.push(this.Graduatoria);
    }
    if (this.client.azioni.some((azione) => azione.codAzione === AZIONE.OP_Buono)) {
      this.links.push(this.RendicontazioneOp);
    }
    // if (this.listaAzioni.some((azione) => azione.codAzione === AZIONE.OP_VisualizzaVerifiche)) {
    //   this.links.push(this.verifichePostGraduatoria);
    // }
    // Aggiungere altri con relazione azioni-tabs (1-1)
  }





  // inserisciEnte() {
  //   this.router.navigate(['/nuovo-ente'], { relativeTo: this.route, skipLocationChange: true });
  // }



  // unisciEnte() {
  //   // this.modalService.open(ChiudiEnteComponent, { size: 'lg' });
  //   // Serve a passare eventuali dati alla modale
  //   const modalRef = this.modalService.open(UnisciEnteComponent, { size: 'lg', windowClass: 'my-class' });
  //   modalRef.result.then((result) => {
  //     if (result) {
  //       this.toastService.showSuccess({ text: 'Unione Avvenuta con successo' });
  //       this.onReloadPage();


  //     }
  //   }).catch((res) => { });

  // }

  onReloadPage() {

    let currentUrl = this.router.url;
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      skipLocationChange: true,

    };
    this.router.navigateByUrl('/', navigationExtras).then(() => {
      this.router.navigate([currentUrl], navigationExtras);
    });
  }

  // goToCruscotto(link: any) {
  //   if (link.link == 'cruscotto') {
  //     this.client.goToCruscotto = true;
  //     this.client.goToConfiguratore = false;
  //     this.client.nuovaPrestazione = false;
  //     this.client.goToNuovaPrestazione = false;
	//   this.client.goToArchivio = false;
  //   } else if (link.link == 'configuratore-prestazioni') {
  //     this.client.goToCruscotto = false;
  //     this.client.goToConfiguratore = true;
  //     this.client.nuovaPrestazione = false;
  //     this.client.goToNuovaPrestazione = false;
  //     this.client.goToArchivio = false;
  //   } else if (link.link == 'archivio-dati-rendicontazione'){
	//   this.client.goToCruscotto = false;
  //     this.client.goToConfiguratore = false;
  //     this.client.nuovaPrestazione = false;
  //     this.client.goToNuovaPrestazione = false;
  //     this.client.goToArchivio = true;
	// } else if (link.link == 'gestione-utenti'){
	//   this.client.goToCruscotto = true;
  //     this.client.goToConfiguratore = false;
  //     this.client.nuovaPrestazione = false;
  //     this.client.goToNuovaPrestazione = false;
  //     this.client.goToArchivio = false;
	// }
	// else{
  //     this.client.goToCruscotto = false;
  //     this.client.goToConfiguratore = false;
  //     this.client.nuovaPrestazione = false;
  //     this.client.goToNuovaPrestazione = false;
  //     this.client.goToArchivio = false;
  //   }
  // }

  // nuovaPrestazione1() {
  //   this.client.nuovaPrestazione = true;
  //   this.client.goToNuovaPrestazione = true;
  //   this.router.navigate(['../operatore-regionale/nuova-prestazione'], { relativeTo: this.route, skipLocationChange: true });
  // }


  // ------- OLD
  // links = [
  //   {
  //     title: 'Domande Aperte', link: 'istanze-aperte', active: true
  //   },
  //   {
  //     title: 'Archivio', link: 'archivio-domande', active: true
  //   },
  //   {
  //    title: 'Graduatoria', link: 'archivio-dati-rendicontazione', active: false, azione: 'Archivio'
  //     //  title: 'Gradatoria', link: 'archivio-dati-rendicontazione', active: true, azione:'Archivio'
  //   },
  //   {
  //     title: 'Verifiche post-Graduatoria', link: 'configuratore-prestazioni', active: false, azione: 'ConfiguratorePrestazioni'
  //   },
  //   {
  //     title: 'In Erogazione', link: 'cruscotto', active: false, azione: 'Cruscotto'
  //   },
  // ]
}
