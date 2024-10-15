import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatTable } from '@angular/material';
import { ActivatedRoute, Navigation, NavigationExtras, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BehaviorSubject, forkJoin, Subject } from 'rxjs';
import { BuonoresBOClient } from '@buonores-app/app/BuonoresBOClient';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ALLEGATI, AZIONE, CONTRATTO, DATIENTE_INFO, DOC, ERRORS, MSG, PATTERN, SECTION, STATO_DOMANDA, STATO_ENTE, STATO_ENTE_DESC_LUNGA, TIPO_FEEDBACK, VALUTAZIONE } from '@buonores-app/constants/buonores-constants';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { GenericResponseWarnErrBuonores } from '@buonores-app/app/dto/GenericResponseWarnErrBuonores';
import { BuonoresErrorService } from '@buonores-app/shared/error/buonores-error.service';
import { BuonoresError } from '@buonores-app/shared/error/buonores-error.model';
import { formatDate } from '@angular/common';
import { AppToastService } from '@buonores-shared/toast/app-toast.service';
import { ModelRichiesta } from '@buonores-app/app/dto/ModelRichiesta';
import { ModelDownloadAllegato } from '@buonores-app/app/dto/ModelDownloadAllegato';
import { ModelAteco } from '@buonores-app/app/dto/ModelAteco';
import { Feedback } from '@buonores-app/app/dto/Feedback';
import { ModelDatiDaModificare } from '@buonores-app/app/dto/ModelDatiDaModificare';
import { IDropdownSettings } from 'ng-multiselect-dropdown';
import { PopupCambistatoComponent } from '../popup-cambistato/popup-cambistato.component';
import { CustomDialogComponent } from '@buonores-app/shared/custom-dialog/custom-dialog.component';
import { PresaInCaricoModel } from '@buonores-app/app/dto/PresaInCaricoModel';


@Component({
  selector: 'app-dettaglio-archivio',
  templateUrl: './dettaglio-archivio.component.html',
  styleUrls: ['./dettaglio-archivio.component.css'],
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
export class DettaglioArchivioComponent implements OnInit {

  state: string = 'default';
  dataModifica =  new Date(new Date().getFullYear(), 0, 1);
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
disabilitaForm :boolean;
disabilitaAllegati: boolean;
disabilitaPresaInCarico :boolean;
verbale: string;
cartaidentita: string;
delega: string;
tipoallegato:string;
procuraspeciale:string;
denunciainps:string;
contratto:string;
letteraincarico:string;
urlVerbale:string;
urlDelega:string;
urlContratto:string;
urlCartaIdentita:string;
urlLetteraIncarico:string;
urlProcuraSpeciale:string;

allegatiRettifica :ModelDatiDaModificare[]=[];
campiRettifica :ModelDatiDaModificare[]=[];
selectedAllegatiItems :ModelDatiDaModificare[] = [];
selectedCampiItems :ModelDatiDaModificare[] = [];
selectedAllItems :ModelDatiDaModificare[] = [];
dropdownSettings = {};
dropdownSettings1 = {};

  constructor(private router: Router, private route: ActivatedRoute, private fb: FormBuilder,private dialog: MatDialog,
    public client: BuonoresBOClient, private modalService: NgbModal, public toastService: AppToastService, private buonoresError: BuonoresErrorService) {

    this.navigation = this.router.getCurrentNavigation();
    let domandaValues: string[] = [];
    this.route.fragment.subscribe((frag: string) => {
      domandaValues.push(frag);
    });
    this.numerodomanda = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.numerodomanda : domandaValues[0][0];
	 }

  ngOnInit() {
	this.showFeedback=this.client.showFeedback;
    this.dataSistema.setHours(0,0,0,0);

    this.client.spinEmitter.emit(true);
	this.showMessage();
      forkJoin({
        schedaDomanda: this.client.getRichiestaNumero(this.numerodomanda),
		listaAllegati: this.client.getAllegatiRettifica(this.numerodomanda),
		campiRettifica: this.client.getCampiRettifica(this.numerodomanda),
      })
        .subscribe(({schedaDomanda,listaAllegati,campiRettifica }) => {
           this.client.azioni;
          this.domanda = schedaDomanda;
		  this.allegatiRettifica = listaAllegati;
         this.campiRettifica = campiRettifica;
		this.delega = null;
		this.abilitabottoni();
		this.cartaidentita = null;
		this.verbale = null;
		this.procuraspeciale = null;
		this.denunciainps = null;
		this.contratto = null;
		this.letteraincarico = null;
		if (this.domanda.valutazione_multidimensionale==VALUTAZIONE.UMVD){
		if (this.domanda.allegati.findIndex(element => element.tipo == ALLEGATI.VERBALE_UMVD)!=-1){
		this.verbale =  this.domanda.allegati.filter(element => element.tipo == ALLEGATI.VERBALE_UMVD)[0].filename;
		this.tipoallegato =  this.domanda.allegati.filter(element => element.tipo == ALLEGATI.VERBALE_UMVD)[0].tipo;
		this.urlVerbale = this.client.getAllegato(this.domanda.numero,this.tipoallegato);
		}
		}
		else if (this.domanda.valutazione_multidimensionale==VALUTAZIONE.UVG){
	    if (this.domanda.allegati.findIndex(element => element.tipo == ALLEGATI.VERBALE_UVG)!=-1){
		this.verbale = this.domanda.allegati.filter(element => element.tipo == ALLEGATI.VERBALE_UVG)[0].filename;
		this.tipoallegato =  this.domanda.allegati.filter(element => element.tipo == ALLEGATI.VERBALE_UVG)[0].tipo;
		this.urlVerbale = this.client.getAllegato(this.domanda.numero,this.tipoallegato);
		}
		}
		if (this.domanda.allegati.findIndex(element => element.tipo == ALLEGATI.DELEGA)!=-1){
		this.delega = this.domanda.allegati.filter(element => element.tipo == ALLEGATI.DELEGA)[0].filename;
		this.urlDelega = this.client.getAllegato(this.domanda.numero,ALLEGATI.DELEGA);
		}
		if (this.domanda.allegati.findIndex(element => element.tipo == ALLEGATI.CARTA_IDENTITA)!=-1){
		this.cartaidentita = this.domanda.allegati.filter(element => element.tipo == ALLEGATI.CARTA_IDENTITA)[0].filename;
		this.urlCartaIdentita = this.client.getAllegato(this.domanda.numero,ALLEGATI.CARTA_IDENTITA);
		}
		if (this.domanda.allegati.findIndex(element => element.tipo == ALLEGATI.PROCURA_SPECIALE)!=-1){
		this.procuraspeciale = this.domanda.allegati.filter(element => element.tipo == ALLEGATI.PROCURA_SPECIALE)[0].filename;
		this.urlProcuraSpeciale =this.client.getAllegato(this.domanda.numero,ALLEGATI.PROCURA_SPECIALE);
		}
		if (this.domanda.allegati.findIndex(element => element.tipo == ALLEGATI.LETTERA_INCARICO)!=-1){
		this.letteraincarico = this.domanda.allegati.filter(element => element.tipo == ALLEGATI.LETTERA_INCARICO)[0].filename;
		this.urlLetteraIncarico = this.client.getAllegato(this.domanda.numero,ALLEGATI.LETTERA_INCARICO);
		}
		if (this.domanda.allegati.findIndex(element => element.tipo == ALLEGATI.CONTRATTO_RSA)!=-1){
		this.contratto = this.domanda.allegati.filter(element => element.tipo == ALLEGATI.CONTRATTO_RSA)[0].filename;
		this.urlContratto = this.client.getAllegato(this.domanda.numero,ALLEGATI.CONTRATTO_RSA);
		}


		this.dropdownSettings =  {
	      singleSelection: false,
	      idField: 'codice',
	      textField: 'descrizione',
	      selectAllText: 'Seleziona tutti',
	      unSelectAllText: 'Deseleziona tutti',
	      itemsShowLimit: 0,
	      allowSearchFilter: false
	    };
      this.dropdownSettings1 =  {
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
	this.client.feedback=null;
	this.client.showFeedback=false;
      }

	checkAzione(azione:string){
		if (azione=="scarica_allegato")
	    return this.client.azioni.some(a => a.codAzione == AZIONE.OP_ScaricaAllegati);
		if (azione=="verifica_isee")
	    return this.client.azioni.some(a => a.codAzione == AZIONE.OP_VerificaISEE);
        if (azione=="presa_in_carico")
	    return this.client.azioni.some(a => a.codAzione == AZIONE.OP_PrendiInCarico);
         if (azione=="richiesta_rettifica")
	    return this.client.azioni.some(a => a.codAzione == AZIONE.OP_RichiediRettifica);
	}


	/*scaricaallegato(tipo:string){
		if (tipo=='VERBALE')
		tipo=this.tipoallegato;
    this.client.spinEmitter.emit(true);
    this.client.getAllegato(this.domanda.numero,tipo).subscribe(
    (docToDownload: ModelDownloadAllegato) => {
			//	const linkSource = 'data:application/pdf/zip;base64,' + ' ' + docToDownload.allegato;
				const downloadLink = document.createElement("a");
				const fileName = docToDownload.fileName;
				downloadLink.href = 'http://localhost:8080/buonoresbo/api/v1/allegato/20230216_DOM_5437/VERBALE_UMVD';
				downloadLink.download = fileName;
				downloadLink.target ='_blank';
				downloadLink.click();
				downloadLink.remove();
				this.client.spinEmitter.emit(false);
			},
			err => {
				this.client.spinEmitter.emit(false);
			}
		);
	}*/
verificaisee(){
	}

	abilitabottoni(){
		if (this.domanda != null && this.domanda !=undefined && this.domanda.stato===STATO_DOMANDA.PRESA_IN_CARICO)
	    this.disabilitaForm = false;
		else
		this.disabilitaForm = true;
		if (this.domanda != null && this.domanda !=undefined && this.domanda.stato===STATO_DOMANDA.INVIATA)
	    this.disabilitaAllegati = true;
		else
		this.disabilitaAllegati = false;
		if (this.domanda != null && this.domanda !=undefined && (this.domanda.stato===STATO_DOMANDA.INVIATA || this.domanda.stato===STATO_DOMANDA.RETTIFICATA))
	    this.disabilitaPresaInCarico = false;
		else
		this.disabilitaPresaInCarico = true;
	}

	// verificaateco(){
	// 	this.client.spinEmitter.emit(true);
	// 	let piva = null;
	// 	if (this.domanda.contratto.tipo==CONTRATTO.COOPERATIVA)
	// 		piva = this.domanda.contratto.agenzia.cf;
	// 	else
	// 		piva = this.domanda.contratto.piva_assitente_familiare;
  //   this.client.getAteco(this.domanda.numero,piva).subscribe(
  //   (ateco: ModelAteco) => {
	// 			this.domanda.ateco.ateco_cod = ateco.ateco_cod;
	// 			this.domanda.ateco.ateco_desc = ateco.ateco_desc;
	// 			this.domanda.ateco.ateco_verificato_in_data = ateco.ateco_verificato_in_data;
	// 			this.client.spinEmitter.emit(false);
	// 		},
	// 		err => {
	// 			this.client.spinEmitter.emit(false);
	// 		}
	// 	);
	// }

richiedirettifica(){
	this.selectedAllItems = [];
	for (let entry of this.selectedCampiItems){
		entry.id =  this.campiRettifica.filter(element => element.codice == entry.codice)[0].id;
		entry.nome_tabella =  this.campiRettifica.filter(element => element.codice == entry.codice)[0].nome_tabella;
		this.selectedAllItems.push(entry);
	}
	for (let entry of this.selectedAllegatiItems){
		entry.id =  this.allegatiRettifica.filter(element => element.codice == entry.codice)[0].id;
		entry.nome_tabella =  this.allegatiRettifica.filter(element => element.codice == entry.codice)[0].nome_tabella;
		this.selectedAllItems.push(entry);
	}

    const modalRef = this.modalService.open(PopupCambistatoComponent, { size: 'lg' });
    modalRef.componentInstance.numerodomanda = this.numerodomanda;
    modalRef.componentInstance.selectedAllItems = this.selectedAllItems;
    modalRef.componentInstance.descStato = 'RICHIESTA VERIFICA CITTADINO';
    modalRef.result.then((result)  => {
      if(result){
	        this.domanda.stato = STATO_DOMANDA.DA_RETTIFICARE;
			this.domanda.domanda_stato_desc = STATO_DOMANDA.DA_RETTIFICARE_DESC
			this.selectedCampiItems = [];
			this.selectedAllegatiItems = [];
			this.selectedAllItems = [];
			this.client.ricercaDomAperte =[];
			this.abilitabottoni();
			this.toastService.showSuccess({ text: 'Richiesta Rettifica inviata correttamente' });
      }
    }).catch((res) => {});


}


  presaInCarico(){
    let message;
   let titolo;
      message = `Sei sicuro di voler prendere in carico la domanda selezionata`;
      titolo = `Presa in carico`;
    const dialogRef = this.dialog.open(CustomDialogComponent, {
      data: {
        messaggio: message,
		titolo:titolo,
        conferma: 'CONFERMA',
        annulla: 'ANNULLA'
      },
      disableClose: true
   });
    dialogRef.afterClosed().subscribe((result : boolean)=>{
      if(result){
        this.client.spinEmitter.emit(true);
        let modelPresaCarico : PresaInCaricoModel[] = [];
          let model : PresaInCaricoModel = new PresaInCaricoModel(this.domanda.numero,this.domanda.stato);
          modelPresaCarico.push(model);
        this.client.presaInCarico(modelPresaCarico).subscribe(res=>{
          if(res){
			this.domanda.stato = STATO_DOMANDA.PRESA_IN_CARICO;
			 this.domanda.domanda_stato_desc = STATO_DOMANDA.PRESA_IN_CARICO_DESC
			this.abilitabottoni();
			 this.client.ricercaDomAperte =[];
            this.client.spinEmitter.emit(false);
          }else{
            this.client.spinEmitter.emit(false);
          }
        })
      }else{
        this.client.spinEmitter.emit(false);
      }
    })
  }

  backButton() {
	this.router.navigate(["/operatore-buono/archivio-domande"], { relativeTo: this.route, skipLocationChange: true });
  }

  showMessage(){
     if(this.client.feedback !== null && this.client.feedback !== undefined && this.client.showFeedback){
      this.showMessageEvent(this.client.feedback);
      this.feedback = null;

    }
  }

  showMessageEvent(feedback: Feedback){
    this.showFeedbackSubject.next(feedback.showFeedback);
    this.tipoFeedback = feedback.tipoFeedback;
    this.messaggioFeedback = feedback.messaggioFeedback;
   // this.ngOnInit();
  }

  onCloseFeedback(){
    this.showFeedbackSubject.next(false);
  }

  relaod(msgFeedback: string){
	this.client.showFeedback=true;
	//this.client.messaggioFeedback=msgFeedback;
	this.client.feedback = new Feedback(true, TIPO_FEEDBACK.SUCCESS, msgFeedback)
	//this.ngOnInit();
  }


}
