/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { EventEmitter, Injectable } from "@angular/core";
import { NavigationExtras, Router } from "@angular/router";
import { ConfigService } from "@buonores-app/app/config.service";
import { UserInfoBuonores } from "@buonores-app/app/dto/UserInfoBuonores";
import { AZIONE } from "@buonores-app/constants/buonores-constants";
import { BuonoresError } from "@buonores-app/shared/error/buonores-error.model";
import { BuonoresErrorService } from "@buonores-app/shared/error/buonores-error.service";
import { encodeAsHttpParams } from "@buonores-app/shared/util";
import { CookieService } from "ngx-cookie-service";
import { Observable, of, throwError } from "rxjs";
import { map } from "rxjs/internal/operators/map";
import { catchError } from 'rxjs/operators';
import * as uuid from 'uuid';
import { AzioneBuonores } from "./dto/AzioneBuonores";
import { CambioStatoPopUp } from "./dto/CambioStatoPopUp";
import { DomandeAperte } from "./dto/DomandeAperte";
import { Feedback } from "./dto/Feedback";
import { FiltriDomandeAperte } from "./dto/FiltriDomandeAperte";
import { FiltriRicercaBuoni } from "./dto/FiltriRicercaBuoni";
import { ListaBuonores } from "./dto/ListaBuonores";
import { ModelArea } from "./dto/ModelArea";
import { ModelAteco } from "./dto/ModelAteco";
import { ModelCriteriGraduatoria } from "./dto/ModelCriteriGraduatoria";
import { ModelDatiDaModificare } from "./dto/ModelDatiDaModificare";
import { ModelDescrizioneGraduatoria } from "./dto/ModelDescrizioneGraduatoria";
import { ModelDichiarazioneSpesa } from "./dto/ModelDichiarazioneSpesa";
import { ModelDomandeGraduatoria } from "./dto/ModelDomandeGraduatoria";
import { ModelEnteGestore } from "./dto/ModelEnteGestore";
import { ModelIsee } from "./dto/ModelIsee";
import { ModelMessaggio } from "./dto/ModelMessaggio";
import { ModelNuovaGraduatoria } from "./dto/ModelNuovaGraduatoria";
import { ModelParametriFinanziamento } from "./dto/ModelParametriFinanziamento";
import { ModelRicercaBuono } from "./dto/ModelRicercaBuono";
import { ModelRichiesta } from "./dto/ModelRichiesta";
import { ModelStatiBuono } from "./dto/ModelStatiBuono";
import { ModelVerifiche } from "./dto/ModelVerifiche";
import { ModelVisualizzaCronologia } from "./dto/ModelVisualizzaCronologia";
import { ModelVisualizzaVerifiche } from "./dto/ModelVisualizzaVerifiche";
import { ParametroBuonores } from "./dto/ParametroBuonores";
import { PresaInCaricoModel } from "./dto/PresaInCaricoModel";
import { ProfiloBuonores } from "./dto/ProfiloBuonores";
import { RuoloBuonores } from "./dto/RuoloBuonores";
import { Sportelli } from "./dto/Sportelli";
import { Sportello } from "./dto/Sportello";
import { StatiBuonores } from "./dto/StatiBuonores";
import { ModelVerificheEnte } from "./dto/ModelVerificheEnte";
import { ModelContrattoAllegati } from "./dto/ModelContrattoAllegati";
import { ModelDecorrenzaBuono } from "./dto/ModelDecorrenzaBuono";
import { GenericResponse } from "./dto/GenericResponse";
import { ModelDownloadAllegato } from "./dto/ModelDownloadAllegato";
import { ModelSpesaIntegrazione } from "./dto/ModelSpesaIntegrazione";
import { ModelIseeBuono} from "./dto/ModelIseeBuono";
import { ModelEsitiIsee } from "./dto/ModelEsitiIsee";

const enum PathApi {

    logout = '/loginController/logout',
    //Ricerca
    getStati = '/ricerca/stati',
    getSportelli = '/ricerca/sportelli',
    getsportellichiusi ='/graduatoria/getsportellichiusi',
    ricerca = '/ricerca',
    getEntiGestori = '/entigestori',
    //Presa in carico
    presaInCarico = '/richieste/presa_carico',
    selectprofilo = '/loginController/login',
    //richiesta
    getRichiestaNumero = '/richieste',
    //allegati
    getAllegatoDownload = '/allegato',
    //verifica ateco
    // getAteco = '/integrazione',
    //rettifica
    getallegatiRettifica = '/richieste/listaallegati',
    getcampiRettifica = '/richieste/listacampi',
    datiDaModificarePost = '/richieste/richiedi_rettifica',
    //AMMETTI, RESPINGI, AMMESSA_CON_RISERVA, AMMESSA, IN_PAGAMENTO, DINIEGO
    ammissibile = '/richieste/ammissibile',
    nonammissibile = '/richieste/nonammissibile',
    richiedirettificaente = '/entigestori/richiedirettifica',
    // salvaverificaente = '/entigestori/salvaverificaente',
    salvaverificaente = '/entigestori/salvaverificaentenew',
    //concludiverificaente = '/entigestori/concludiverificaente',
    concludiverificaente = '/entigestori/salvaconcludiverificaente',
    getverificheente = '/entigestori/getverificheentegestore',
    preavvisopernonammissibilita = '/richieste/preavvisopernonammissibilita',
    verificacontatto = '/richieste/verificacontatto',

    salvadomandaisee = '/richieste/salvadomandaisee',
    salvadomandanota = '/richieste/salvadomandanota',
    ammessaConRiserva = '/richieste/ammessaconriserva',
    ammessaConRiservaInPagamento = '/richieste/ammessaconriservainpagamento',
    ammessa = '/richieste/ammessa',
    inPagamento = '/richieste/inpagamento',
    diniego = '/richieste/diniego',

    //da RICHIESTA VERIFICA a VERIFICA IN CORSO
    updateToVerificaInCorso = '/entigestori/updateToVerificaInCorso',

    // Messaggi
    getMsgApplicativo = '/liste/getMessaggioApplicativo',
    getMsgInformativi = '/liste/getMessaggiInformativi',
    // getParametroPerInformativa = '/liste/getParametroPerInformativa',
    getMsgInformativiPerCod = '/liste/getMessaggiInformativipercodice',
    getParametro = '/liste/getParametro',

    // Ricerca Enti
    getListaStatoRendicontazione = '/entiGestoriAttivi/getListaStatoRendicontazione',
    getListaComuni = '/entiGestoriAttivi/getListaComuni',
    getListaTipoEnte = '/entiGestoriAttivi/getListaTipoEnte',
    getListaDenominazioni = '/entiGestoriAttivi/getListaDenominazioni',
    getListaDenominazioniWithComuniAssociati = '/entiGestoriAttivi/getListaDenominazioniWithComuniAssociati',
    searchSchedeEntiGestori = '/entiGestoriAttivi/searchSchedeEntiGestori',
    searchSchedeMultiEntiGestori = '/entiGestoriAttivi/searchSchedeMultiEntiGestori',
    getSchedaEnteGestore = '/entiGestoriAttivi/getSchedaEnteGestore',
    getCronologia = '/entiGestoriAttivi/getCronologia',
    getStorico = '/entiGestoriAttivi/getStorico',
    getStatiEnte = '/entiGestoriAttivi/getStatiEnte',
    getAnniEsercizio = '/entiGestoriAttivi/getAnniEsercizio',
    getCronologiaStato = '/datiEnte/getCronologiaStato',
    getLastStato = '/datiEnte/getLastStato',

    // Archivio Enti
    getListaStatoRendicontazioneArchivio = '/archivioDatiRendicontazione/getListaStatoRendicontazione',
    getListaComuniArchivio = '/archivioDatiRendicontazione/getListaComuni',
    getListaTipoEnteArchivio = '/archivioDatiRendicontazione/getListaTipoEnte',
    searchSchedeEntiGestoriArchivio = '/archivioDatiRendicontazione/searchArchivioRendicontazione',
    getCronologiaArchivio = '/richieste/cronologia',
    getVerifiche = '/richieste/verifiche',
    searchSchedeEntiGestoriRendicontazioneConclusa = '/archivioDatiRendicontazione/searchSchedeEntiGestoriRendicontazioneConclusa',
    getStatoRendicontazioneConclusa = '/archivioDatiRendicontazione/getStatoRendicontazioneConclusa',
    getAnniEsercizioArchivio = '/archivioDatiRendicontazione/getAnniEsercizio',

    //GRADUATORIA
    getultimosportellochiuso = '/graduatoria/getultimosportellochiuso',
    creaNuovaGraduatoria = '/graduatoria/creanuovagraduatoria',
    getCriteriGraduatoria = '/graduatoria/getgraduatoriaordinamento',
    getDomandeGraduatoria = '/graduatoria/getdomandegraduatoria',
    getParametriFinanziamento = '/graduatoria/getparametrifinanziamento',
    getDescrizioneGraduatoria = '/graduatoria/getgraduatoriadesc',
    pubblicazioneGraduatoria = '/graduatoria/pubblicagraduatoria',
    aggiornamentoGraduatoria = '/graduatoria/aggiornagraduatoria',
    simulazioneGraduatoria = '/graduatoria/simulagraduatoria',
    checkEsistenzaGraduatoria = '/graduatoria/checkesistenzagraduatoria',
    checkStatoGraduatoria = '/graduatoria/checkestatograduatoria',
    checkPubblicaGraduatoria = '/graduatoria/checkpubblicagraduatoria',
    getAree = '/graduatoria/getaree',

    //NUOVO SPORTELLO
    creaNuovoSportello = '/operatoreregionale/creanuovosportello',

    // Rendicontazione
    getInfoRendicontazioneOperatore = '/datiRendicontazione/getInfoRendicontazioneOperatore',
    confermaDati1 = '/datiRendicontazione/confermaDati1',
    richiestaRettifica1 = '/datiRendicontazione/richiestaRettifica1',
    confermaDati2 = '/datiRendicontazione/confermaDati2',
    richiestaRettifica2 = '/datiRendicontazione/richiestaRettifica2',
    inviatranche = '/datiRendicontazione/inviaTranche',
    getmodellipertranche = '/datiRendicontazione/getModelliTranche',
    gettranchepermodello = '/datiRendicontazione/getTranchePerModello',
    valida = '/datiRendicontazione/valida',
    storicizza = '/datiRendicontazione/storicizza',
    getmodelliassociati = "/datiRendicontazione/getModelliAssociati",
    getmodelli = "/datiRendicontazione/getModelli",
    getAllObbligo = "/datiRendicontazione/getAllObbligo",
    getVerificaModelliVuoto = "/datiRendicontazione/getVerificaModelliVuoto",
    // Stato avanzamento istruttoria
    getBuonoInformazioni = '/buonodettaglio/getbuonoinformazioni',
    getVerificaInformazioni = '/buonodettaglio/getverificainformazioni',
    getStoricoInformazioni = '/buonodettaglio/getstoricorendicontazioni',

    // Modello A1
    getVociModelloA1 = '/modelloA1/getVociModelloA1',
    getDatiModelloA1 = '/modelloA1/getDatiModelloA1',
    saveModelloA1 = '/modelloA1/saveModelloA1',
    esportaModelloA1 = '/modelloA1/esportaModelloA1',

    // Modello A2
    getCausali = '/modelloA2/getCausali',
    getCausaliStatiche = '/modelloA2/getCausaliStatiche',
    getTrasferimentiA2 = '/modelloA2/getTrasferimentiA2',
    saveModelloA2 = '/modelloA2/saveModelloA2',
    esportaModelloA2 = '/modelloA2/esportaModelloA2',

    // Modello D
    getTipoVoceD = '/modelloD/getTipoVoceD',
    getVoceModD = '/modelloD/getVociModD',
    getRendicontazioneModD = '/modelloD/getRendicontazioneModDByIdScheda',
    saveModelloD = '/modelloD/saveModelloD',
    esportaModelloD = '/modelloD/esportaModelloD',

    // MacroagBuonoresati
    getMacroagBuonoresatiTotali = '/macroagBuonoresati/getRendicontazioneTotaliMacroagBuonoresati',
    getMacroagBuonoresati = '/macroagBuonoresati/getMacroagBuonoresati',
    getSpesaMissione = '/macroagBuonoresati/getSpesaMissione',
    getRendicontazioneMacroagBuonoresati = '/macroagBuonoresati/getRendicontazioneMacroagBuonoresatiByIdScheda',
    isMacroagBuonoresatiCompiled = '/macroagBuonoresati/isRendicontazioneTotaliMacroagBuonoresatiCompiled',
    saveMacroagBuonoresati = '/macroagBuonoresati/saveMacroagBuonoresati',
    esportaMacroagBuonoresati = '/macroagBuonoresati/esportaMacroagBuonoresati',

    // Modello A
    getListaVociModA = '/modelloA/getListaVociModA',
    saveModelloA = '/modelloA/saveModelloA',
    esportaModelloA = '/modelloA/esportaModelloA',


    //Modello B1
    getPrestazioniModB1 = '/modellob1/getPrestazioni',
    getElencoLblModB1 = '/modellob1/getElencoLbl',
    saveModelloB1 = '/modellob1/saveModello',
    esportaModelloB1 = '/modellob1/esportaModelloB1',

    // ModelloB
    getMissioniModB = '/modelloB/getMissioniModB',
    getRendicontazioneModB = '/modelloB/getRendicontazioneModB',
    getRendicontazioneTotaliSpese = '/macroagBuonoresati/getRendicontazioneTotaliSpese',
    getRendicontazioneTotaliMacroagBuonoresati = '/macroagBuonoresati/getRendicontazioneTotaliMacroagBuonoresati',
    getProgrammiMissioneTotaliModB = '/modellob1/getProgrammiMissioneTotaliModB',
    saveModelloB = '/modelloB/saveModelloB',
    esportaModelloB = '/modelloB/esportaModelloB',
    esportaIstat = '/modelloB/esportaIstat',
    canActiveModB = '/modelloB/canActiveModB',

    // ModelloE
    getAttivitaSocioAssist = '/modelloE/getAttivitaSocioAssist',
    getRendicontazioneModE = '/modelloE/getRendicontazioneModE',
    saveModelloE = '/modelloE/saveModelloE',
    esportaModelloE = '/modelloE/esportaModelloE',

    // ModelloC
    getPrestazioniC = '/modelloC/getPrestazioniModC',
    getRendicontazioneModC = '/modelloC/getRendicontazioneModC',
    saveModelloC = '/modelloC/saveModelloC',
    esportaModelloC = '/modelloC/esportaModelloC',

    // ModelloF
    getConteggiF = '/modelloF/getConteggiModF',
    getRendicontazioneModF = '/modelloF/getRendicontazioneModF',
    saveModelloF = '/modelloF/saveModelloF',
    esportaModelloF = '/modelloF/esportaModelloF',

    // Modello All D
    getVociAllD = '/modelloAllD/getVociAllD',
    getRendicontazioneModAllD = '/modelloAllD/getRendicontazioneModAllD',
    compilabileModelloAllD = '/modelloAllD/compilabileModelloAllD',
    saveModelloAllD = '/modelloAllD/saveModelloAllD',
    canActiveModFnps = '/modelloAllD/canActiveModFnps',
    esportaModuloFnps = '/modelloAllD/esportaModuloFnps',

    //check
    saveMotivazioneCheck = '/datiRendicontazione/saveMotivazioneCheck',
    checkModelloA = '/modelloA/checkModelloA',
    checkModelloA1 = '/modelloA1/checkModelloA1',
    checkMacroagBuonoresati = '/macroagBuonoresati/checkMacroagBuonoresati',
    checkModelloB1 = '/modellob1/checkModelloB1',
    checkModelloB = '/modelloB/checkModelloB',
    checkModelloC = '/modelloC/checkModelloC',
    checkModelloF = '/modelloF/checkModelloF',


    //cruscotto
    searchSchedeEntiGestoriCruscotto = '/cruscotto/searchSchedeEntiGestori',
    getModelliCruscotto = "/cruscotto/getModelli",
    getInfoModello = '/cruscotto/getInfoModello',
    getMaxAnnoRendicontazione = '/cruscotto/getMaxAnnoRendicontazione',

    //Configuratore
    getPrestazioniReg1 = '/configuratore/getPrestazioni',
    getPrestazioneRegionale1 = '/configuratore/getPrestazioneRegionale1',
    getTipologie = '/configuratore/getTipologie',
    getStrutture = '/configuratore/getStrutture',
    getQuote = '/configuratore/getQuote',
    getPrestColl = '/configuratore/getPrestColl',
    getMacroagBuonoresatiConf = '/configuratore/getMacroagBuonoresati',
    getUtenzeConf = '/configuratore/getUtenze',
    getMissioneProgConf = '/configuratore/getMissioneProg',
    getSpeseConf = '/configuratore/getSpese',
    getPrestazioni2Conf = '/configuratore/getPrestazioni2',
    getPrestazioniMinConf = '/configuratore/getPrestazioniMin',
    savePrestazione = '/configuratore/savePrestazione',
    getIstatConf = '/configuratore/getIstatConf',
    getUtenzeIstatConf = '/configuratore/getUtenzeIstatConf',
    getNomenclatoreConf = '/configuratore/getNomenclatoreConf',
    savePrestazione2 = '/configuratore/savePrestazione2',
    modificaPrestazione2 = '/configuratore/modificaPrestazione2',
    modificaPrestazione = '/configuratore/modificaPrestazione',
    getPrestazioneRegionale2 = '/configuratore/getPrestazioneRegionale2',
    getUtenzeIstatTransConf = '/configuratore/getUtenzeIstatTransConf',

    //CreaAnno
    creaNuovoAnno = '/entiGestoriAttivi/creaNuovoAnno',
    //Concludi
    concludiRendicontazione = '/entiGestoriAttivi/concludiRendicontazione',
    ripristinaRendicontazione = '/entiGestoriAttivi/ripristinaRendicontazione',

    //BUONO
    getStatiBuono = '/buono/statibuono',
    ricercaBuoni = '/buono/ricerca',

    //Buono Dettaglio
    getAllegatiBuono = '/buonodettaglio/getallegati',
    scaricaAllegatoBuono = '/buono/allegato',
    scaricaRendicontazioneBuono = '/buono/rendicontazione',
    getContrattiBuono = '/buonodettaglio/getcontratti',
    getDecorrenzaBuono = '/buonodettaglio/getdecorrenza',
    updateDecorrenzaBuono = '/buonodettaglio/updatedecorrenza',
    getIntegrazioniSpesaFiles = '/buonodettaglio/integrazioni',

    //isee buono
    getIseeConforme = '/isee',
    getListaIseeConforme = '/isee/lista',
    getListaEsitiIsee = '/isee/listaesitiisee',
    salvaisee = '/isee',
}

@Injectable()
export class BuonoresBOClient {

    public myUUIDV4 = uuid.v4();
    public messaggioFeedback: string = null;
    public idFeedback: number = null;
    public feedback: Feedback = null;
    baseUrl = ConfigService.getBEServer();
    public spinEmitter: EventEmitter<boolean> = new EventEmitter();
    public buttonViewEmitter: EventEmitter<boolean> = new EventEmitter();
    public utente: Observable<UserInfoBuonores>;
    public listaenti: any[] = [];
    public listaEntiArc: any[] = [];
    public notSavedEvent = new CustomEvent('notSavedEvent', { bubbles: true });
    public changeTabEmitter = new CustomEvent('changeTabEmitter', { bubbles: true });
    public updateCronoEmitter = new CustomEvent('updateCronoEmitter', { bubbles: true });
    public utenteloggato: UserInfoBuonores;
    public azioni: AzioneBuonores[] = [];
    public profilo: ProfiloBuonores;
    public ruolo: RuoloBuonores;
    public listaSelezionata: ListaBuonores;
    public listaStatiSalvato: StatiBuonores[] = [];
    public listaSportelliSalvato: Sportelli;
    public ricercaDomAperte: DomandeAperte[] = [];
    public paginaSalvata: number = 0;
    public ordinamentoSalvato: string = null;
    public versoSalvato = null;
    public showFeedback: boolean;
    public filtroDomandeAperte: FiltriDomandeAperte;
    public righePerPagina: number = 10;

    //------Varibili per enti gestori
    public filtroDomandeAperteEnte: string;
    public listaEntiGestori: ModelEnteGestore[] = [];
    public listaDenominazioneEntiGestori: string[] = [];
    public listaIdEntiGestori: number[] = [];
    public listaStatiVericaEnteGestore: string[] = ['Si', 'No'];
    public listaEsitiVericaEnteGestore: string[] = ['Si', 'No'];
    public filteredEntiRegionali: Observable<string[]>;
    //-----------------------

    public listaDecessoResidenza: string[] = ['Decesso', 'Fuori Regione'];
    //-------Varibili isee
    public listaIseeConforme: string[] = ['Si', 'Trasmissione isee non espressa'];
	  public listaIseeVerificaConforme: ModelEsitiIsee[] = [];

    //------ArchivioComponent
    public listaStatiSalvatoArchivio: StatiBuonores[] = [];
    public listaSportelliSalvatoArchivio: Sportelli = new Sportelli([], new Sportello('', '', '', '', '', false)); public filtroDomandeArchivio: FiltriDomandeAperte;
    public ricercaDomArchivio: DomandeAperte[] = [];
    public paginaSalvataArchivio: number = 0;
    public ordinamentoSalvatoArchivio: string = null;
    public versoSalvatoArchivio = null;
    public righePerPaginaArchivio: number = 10;
    //-----------------------

    //------GraduatoriaComponent
    public ricercaDomGraduatoria: ModelDomandeGraduatoria[] = [];
    public paginaSalvataGraduatoria: number = 0;
    public ordinamentoSalvatoGraduatoria: string = null;
    public versoSalvatoGraduatoria = null;
    public righePerPaginaGraduatoria: number = 10;
    public listaCriteriOrdinamento: ModelCriteriGraduatoria[] = [];
    public parametriFinanziabili: ModelParametriFinanziamento[] = [];
    public importoTotaleMisura: number = null;
    public buonoMensile: number = null;
    public mesiPerDestinatario: number = null;
    public importoResiduoTotale: number = null;
    public soggettiFinanziabili: number = null;
    public soggettiFinanziati: number = null;
    public totaleImportoDestinatario: number = null;
    public graduatoriaDescrizione: ModelDescrizioneGraduatoria = new ModelDescrizioneGraduatoria(" ", " ", " ");
    public checkPubblicazioneGraduatoria: boolean = false;
    public checkStatoGraduatoria: boolean = false;
    public checkEsistenzaGraduatoria: boolean = false;
    //-----------------------

    //------BUONO
    public listaStatiBuonoSalvato: ModelStatiBuono[] = [];
    public listaRicercaBuonoSalvato: ModelRicercaBuono[] = [];
    public filtriRicercaBuoni: FiltriRicercaBuoni;
    //------Rendicontazione Operatore Regionale
    public righePerPaginaRendicontazione: number = 10;
    public paginaSalvataRendicontazione: number = 0;
    public ordinamentoSalvatoRendicontazione: string = null;
    public versoSalvatoRendicontazione = null;
    /*    public cronologia: CronologiaBuonores = new CronologiaBuonores();

       public operazione: string = "";
       public readOnly: boolean;
       public readOnlyII: boolean;
       public readOnlyIII: boolean;
       public mostrabottoniera: boolean;
       public nomemodello:string;
       public componinote:boolean;
       public statorendicontazione:string;

   // sezione azioni booleani
        public SalvaModelliI : boolean;
        public InviaI : boolean;
        public InviaII : boolean;
        public RichiestaRettificaI : boolean;
        public ConfermaDatiI : boolean;
        public RichiestaRettificaII : boolean;
        public ConfermaDatiII : boolean;
        public Valida : boolean;
        public Storicizza : boolean;
        public SalvaModelliII : boolean;
        public SalvaModelliIII : boolean;
        public CheckI: boolean;
        public CheckII: boolean;
        public Concludi: boolean;
        public inviaIIFatto : boolean = false;
        public inviaIFatto : boolean = false;

        public selectedLinkRend: any;
        public ricercaEnte: RicercaBuonoresOutput[]=[];
        public ricercaEnteArchivio: RicercaBuonoresOutput[]=[];
        public filtroEnte: RicercaBuonores;
        public filtroEnteArchivio: RicercaBuonores;
        public paginaSalvata: number = 0;
        public ordinamentoSalvato: string = null;
        public versoSalvato = null;
        public statiEnteSalvato: StatoEnte[]=[];
        public statiEnteSalvatoArchivio: StatoEnte[]=[];
        public listaStatiSalvato: StatoRendicontazioneBuonores[]=[];
        public statoRendicontazioneConslusaArchivio: StatoRendicontazioneBuonores;
        public listaStatiSalvatoArchivio: StatoRendicontazioneBuonores[]=[];
        public listaComuniSalvato: ComuneBuonores[]=[];
        public listaComuniSalvatoArchivio: ComuneBuonores[]=[];
        public listaTipoEntiSalvato: TipoEnteBuonores[]=[];
        public listaTipoEntiSalvatoArchivio: TipoEnteBuonores[]=[];
        public listaDenominazioniEntiSalvato: EnteBuonores[]=[];
        public listaDenominazioniEntiSalvatoArchivio: EnteBuonores[]=[];
        public anniEsercizioSalvato: number[]=[];
        public anniEsercizioSalvatoArchivio: number[]=[];
       public tooltipRendicontazione : string = null;
       public listaComuniinitial: ComuneBuonores[]=[];
       public listaComuniinitialArchivio: ComuneBuonores[]=[];
       public listaComuniinitialCruscotto: ComuneBuonores[]=[];
       public dettaglioPrestazione: boolean = false;
       public ricercaEnteCruscotto: RicercaBuonoresOutput[]=[];
        public filtroEnteCruscotto: RicercaBuonoresCruscotto;
        public paginaSalvataCruscotto: number = 0;
        public statiEnteSalvatoCruscotto: StatoEnte[]=[];
        public listaStatiSalvatoCruscotto: StatoRendicontazioneBuonores[]=[];
        public listaComuniSalvatoCruscotto: ComuneBuonores[]=[];
        public listaTipoEntiSalvatoCruscotto: TipoEnteBuonores[]=[];
        public listaDenominazioniEntiSalvatoCruscotto: EnteBuonores[]=[];
        public anniEsercizioSalvatoCruscotto: number[]=[];
        public modelliSalvatoCruscotto: ModelTabTrancheCruscotto[]=[];
        public statiRendicontazioneCruscotto: StatiCruscotto[]=[];
        public cruscotto: boolean = false;
        public goToCruscotto: boolean = false;
        public goToConfiguratore: boolean = false;
        public goToNuovaPrestazione: boolean = false;
        public nuovaPrestazione: boolean = false;
        public goToArchivio: boolean = false;*/
    constructor(private http: HttpClient, private BuonoresError: BuonoresErrorService, private cookieService: CookieService, private router: Router) { }

    getStati(tipoMenu: string): Observable<StatiBuonores[]> {
        return this.http.get(this.baseUrl + PathApi.getStati, { ...encodeAsHttpParams({ tipoMenu: tipoMenu }) }).pipe(
            map((response: any) => {
                return response as StatiBuonores[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.detail[0].valore }))
                )
            }
            )
        )
    }

    getSportelli(): Observable<Sportelli> {
        return this.http.get(this.baseUrl + PathApi.getSportelli).pipe(
            map((response: any) => {
                return response as Sportelli;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.detail[0].valore }))
                )
            }
            )
        )
    }

    getsportellichiusi(): Observable<Sportello[]> {
      return this.http.get(this.baseUrl + PathApi.getsportellichiusi).pipe(
          map((response: any) => {
              return response as Sportello[];
          }),
          catchError((error: HttpErrorResponse) => {
              if (!error.error || error.error == null || error.error.code == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                  return;
              }
              return throwError(
                  this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.detail[0].valore }))
              )
          }
          )
      )
  }

    ricercaDomandeAperte(filtri: FiltriDomandeAperte): Observable<DomandeAperte[]> {
        return this.http.post(this.baseUrl + PathApi.ricerca, filtri).pipe(
            map((response: any) => {
                return response as DomandeAperte[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.detail[0].valore }))
                )
            }
            )
        )
    }

    getEntiGestori(): Observable<ModelEnteGestore[]> {
        return this.http.get(this.baseUrl + PathApi.getEntiGestori).pipe(
            map((response: any) => {
                return response as ModelEnteGestore[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.detail[0].valore }))
                )
            }
            )
        )
    }

    presaInCarico(richieste: PresaInCaricoModel[]): Observable<boolean> {
        return this.http.post(this.baseUrl + PathApi.presaInCarico, richieste).pipe(
            map((response: any) => {
                return response as boolean;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.detail[0].valore }))
                )
            }
            )
        )
    }

    logout(): Observable<UserInfoBuonores> {
        return this.http.get(this.baseUrl + PathApi.logout
        ).pipe(
            map((response: any) => {
                this.utente = of(response as UserInfoBuonores);
                return response as UserInfoBuonores;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.detail[0].valore }))
                )
            }
            )
        );
    }

    selectprofiloazione(): Observable<UserInfoBuonores> {
        return this.http.get(this.baseUrl + PathApi.selectprofilo ).pipe(
            map((response: any) => {
                this.utente = of(response as UserInfoBuonores);
                return response as UserInfoBuonores;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.detail[0].valore }))
                )
            }
            )
        );
    }

    verificaprofilo(response: UserInfoBuonores) {

        this.utenteloggato = response;
        if (response.listaRuoli.length == 1) { //un solo ruolo
            if (response.listaRuoli[0].listaProfili.length == 1) {//un solo profilo
                this.ruolo = response.listaRuoli[0];
                this.profilo = this.ruolo.listaProfili[0];
                this.azioni = this.profilo.listaAzioni;
                this.redirecttab(this.azioni);
            }
            else {
                //aggiungere il routing alla selezione-profilo-applicativo
                this.router.navigate(['selezione-profilo-applicativo'], { skipLocationChange: true, state: { utente: response } });
            }
        }
        else {
            //aggiungere il routing alla selezione-profilo-applicativo
            this.router.navigate(['selezione-profilo-applicativo'], { skipLocationChange: true, state: { utente: response } });
        }
    }

    /*
    * Redirect a diverse applicazioni in base alle azioni di un utente
    */
    redirecttab(azioni: AzioneBuonores[]) {

        if (azioni.some(a => a.codAzione == AZIONE.OP_RicercaDomandeAperte)) {
            this.router.navigate(['operatore-buono'], { skipLocationChange: true });
        } else if (azioni.some(azione => azione.codAzione == AZIONE.OP_Istruttoria)) {
            this.router.navigate(['operatore-buono/istruttoria'], { skipLocationChange: true });
        } else if (azioni.some(azione => azione.codAzione == AZIONE.OP_Graduatoria)) {
            this.router.navigate(['operatore-buono/graduatoria'], { skipLocationChange: true });
        } else if (azioni.some(azione => azione.codAzione == AZIONE.OP_Archivio)) {
            this.router.navigate(['operatore-buono/archivio-domande'], { skipLocationChange: true });
        }
        // else if (azioni.get("Archivio")[1])
        // this.router.navigate( ['operatore-regionale/archivio-dati-rendicontazione'], { skipLocationChange: true } );
        // else if (azioni.get("ConfiguratorePrestazioni")[1])
        // this.router.navigate( ['operatore-regionale/configuratore-prestazioni'], { skipLocationChange: true} );
        // else if (azioni.get("Cruscotto")[1])
        // this.router.navigate( ['operatore-regionale/cruscotto'], { skipLocationChange: true } );
        else
            this.router.navigate(['/redirect-page'], { skipLocationChange: true });
    }

    getRichiestaNumero(numerorichiesta: string): Observable<ModelRichiesta> {

        return this.http.get(this.baseUrl + PathApi.getRichiestaNumero + '/' + numerorichiesta
        ).pipe(
            map((response: any) => {
                return response as ModelRichiesta;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.detail[0].valore }))
                )
            }
            )
        );
    }

    getAllegato(numerorichiesta: string, tipo: string): string {
        return this.baseUrl + PathApi.getAllegatoDownload + '/' + numerorichiesta + '/' + tipo;
    }

    // getAteco(numerorichiesta: string, piva: string): Observable<ModelAteco> {

    //     return this.http.get(this.baseUrl + PathApi.getAteco + '/' + piva + '/' + numerorichiesta
    //     ).pipe(
    //         map((response: any) => {
    //             return response as ModelAteco;
    //         }),
    //         catchError((error: HttpErrorResponse) => {
    //             if (!error.error || error.error == null || error.error.code == null) {
    //                 this.spinEmitter.emit(false);
    //                 this.router.navigate(['/redirect-page'], { skipLocationChange: true });
    //                 return;
    //             }
    //             return throwError(
    //                 this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.detail[0].valore }))
    //             )
    //         }
    //         )
    //     );
    // }

    getAllegatiRettifica(numerorichiesta: string): Observable<ModelDatiDaModificare[]> {
        return this.http.get(this.baseUrl + PathApi.getallegatiRettifica + '/' + numerorichiesta).pipe(
            map((response: any) => {
                return response as ModelDatiDaModificare[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.detail[0].valore }))
                )
            }
            )
        )
    }
    getCampiRettifica(numerorichiesta: string): Observable<ModelDatiDaModificare[]> {
        return this.http.get(this.baseUrl + PathApi.getcampiRettifica + '/' + numerorichiesta).pipe(
            map((response: any) => {
                return response as ModelDatiDaModificare[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.detail[0].valore }))
                )
            }
            )
        )
    }

    datiDaModificarePost(ricerca: CambioStatoPopUp): Observable<ModelRichiesta> {

        return this.http.post(this.baseUrl + PathApi.datiDaModificarePost, ricerca
        ).pipe(
            map((response: any) => {
                return response as ModelRichiesta;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.detail[0].valore }))
                )
            }
            )
        );
    }

    ammissibile(ricerca: CambioStatoPopUp): Observable<boolean> {
        return this.http.post(this.baseUrl + PathApi.ammissibile, ricerca).pipe(
            map((response: any) => {
                return response as boolean;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    nonammissibile(ricerca: CambioStatoPopUp): Observable<boolean> {
        return this.http.post(this.baseUrl + PathApi.nonammissibile, ricerca).pipe(
            map((response: any) => {
                return response as boolean;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    rettificaente(ricerca: CambioStatoPopUp): Observable<boolean> {
        return this.http.post(this.baseUrl + PathApi.richiedirettificaente, ricerca).pipe(
            map((response: any) => {
                return response as boolean;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    salvaverificaente(verifica: ModelVerificheEnte, numerorichiesta: string): Observable<boolean> {
        return this.http.post(this.baseUrl + PathApi.salvaverificaente + '/' + numerorichiesta, verifica).pipe(
            map((response: any) => {
                return response as boolean;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    concludiverificaente(verifica: ModelVerificheEnte, numerorichiesta: string): Observable<boolean> {
        return this.http.post(this.baseUrl + PathApi.concludiverificaente + '/' + numerorichiesta, verifica).pipe(
            map((response: any) => {
                return response as boolean;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    preavvisopernonammissibilita(ricerca: CambioStatoPopUp): Observable<boolean> {
        return this.http.post(this.baseUrl + PathApi.preavvisopernonammissibilita, ricerca).pipe(
            map((response: any) => {
                return response as boolean;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    verificacontatto(numerodomanda: string): Observable<ModelMessaggio> {
        return this.http.get(this.baseUrl + PathApi.verificacontatto + '/' + numerodomanda).pipe(
            map((response: any) => {
                return response as ModelMessaggio;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    // REST API, cambio stato domanda: AMMISSIBILE --> AMMESSA
    ammessa(cambioStato: CambioStatoPopUp): Observable<boolean> {
        return this.http.post(this.baseUrl + PathApi.ammessa, cambioStato).pipe(
            map((response: any) => {
                return response as boolean;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    // REST API, cambio stato domanda: AMMISSIBILE --> AMMESSA CON RISERVA
    ammessaConRiserva(cambioStato: CambioStatoPopUp): Observable<boolean> {
        return this.http.post(this.baseUrl + PathApi.ammessaConRiserva, cambioStato).pipe(
            map((response: any) => {
                return response as boolean;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    // REST API, cambio stato domanda: AMMESSA CON RISERVA --> AMMESSA CON RISERVA IN PAGAMENTO
    ammessaConRiservaInPagamento(cambioStato: CambioStatoPopUp): Observable<boolean> {
      return this.http.post(this.baseUrl + PathApi.ammessaConRiservaInPagamento, cambioStato).pipe(
        map((response: any) => {
            return response as boolean;
        }),
        catchError((error: HttpErrorResponse) => {
            if (!error.error || error.error == null || error.error.code == null) {
                this.spinEmitter.emit(false);
                this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                return;
            }
            return throwError(
                this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
            )
        }
        )
    );
}

    salvaDomandaIsee(numerodomanda: string, isee: ModelIsee): Observable<boolean> {
        return this.http.post(this.baseUrl + PathApi.salvadomandaisee + '/' + numerodomanda, isee).pipe(
            map((response: any) => {
                return response as boolean;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.detail[0].valore }))
                )
            }
            )
        )
    }

    salvaDomandaNota(nota: CambioStatoPopUp): Observable<boolean> {
        return this.http.post(this.baseUrl + PathApi.salvadomandanota, nota).pipe(
            map((response: any) => {
                return response as boolean;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.detail[0].valore }))
                )
            }
            )
        )
    }


    // REST API, cambio stato domanda: PERFEZIONATA --> IN_PAGAMENTO oppure AMMESSA --> IN_PAGAMENTO
    inPagamento(cambioStato: CambioStatoPopUp): Observable<boolean> {
        return this.http.post(this.baseUrl + PathApi.inPagamento, cambioStato).pipe(
            map((response: any) => {
                return response as boolean;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    // REST API, cambio stato domanda: PERFEZIONATA --> DINIEGO oppure AMMESSA --> DINIEGO
    diniego(cambioStato: CambioStatoPopUp): Observable<boolean> {
        return this.http.post(this.baseUrl + PathApi.diniego, cambioStato).pipe(
            map((response: any) => {
                return response as boolean;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }


    /*
    respingi(ricerca: CambioStatoPopUp): Observable<boolean> {
        return this.http.post(this.baseUrl + PathApi.respingi,ricerca).pipe(
            map((response: any) => {
                return response as boolean;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }
    */


    getCronologiaArchivio(numeroDomanda: string): Observable<ModelVisualizzaCronologia[]> {

        return this.http.get(this.baseUrl + PathApi.getCronologiaArchivio + '/' + numeroDomanda
        ).pipe(
            map((response: any) => {
                return response as ModelVisualizzaCronologia[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getverifiche(numeroDomanda: string): Observable<ModelVisualizzaVerifiche[]> {

        return this.http.get(this.baseUrl + PathApi.getVerifiche + '/' + numeroDomanda
        ).pipe(
            map((response: any) => {
                return response as ModelVisualizzaVerifiche[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }


    updateToVerificaInCorso(numeroDomande: string[]): Observable<boolean> {
        return this.http.post(this.baseUrl + PathApi.updateToVerificaInCorso, numeroDomande).pipe(
            map((response: any) => {
                return response as boolean;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.detail[0].valore }))
                )
            }
            )
        )
    }

    getUltimoSportelloChiuso(): Observable<Sportello> {
        return this.http.get(this.baseUrl + PathApi.getultimosportellochiuso).pipe(
            map((response: any) => {
                return response as Sportello;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
                )
            })
        );
    }

    getAree(): Observable<ModelArea[]> {
      return this.http.get(this.baseUrl + PathApi.getAree).pipe(
          map((response: any) => {
              return response as ModelArea[];
          }),
          catchError((error: HttpErrorResponse) => {
              if (!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                  return;
              }
              return throwError(
                  this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
              )
          })
      );
  }

    getCriteriGraduatoria(): Observable<ModelCriteriGraduatoria[]> {
        return this.http.get(this.baseUrl + PathApi.getCriteriGraduatoria).pipe(
            map((response: any) => {
                return response as ModelCriteriGraduatoria[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    creaNuovoSportello(newSportello: Sportello): Observable<boolean> {
        return this.http.post(this.baseUrl + PathApi.creaNuovoSportello, newSportello).pipe(
            map((response: any) => {
                return response as boolean;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getDomandeGraduatoria(sportelloCod: string): Observable<ModelDomandeGraduatoria[]> {
        return this.http.get(this.baseUrl + PathApi.getDomandeGraduatoria + '/' + sportelloCod).pipe(
            map((response: any) => {
                return response as ModelDomandeGraduatoria[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getParametriFinanziamento(sportelloCod: string): Observable<ModelParametriFinanziamento[]> {
        return this.http.get(this.baseUrl + PathApi.getParametriFinanziamento + '/' + sportelloCod).pipe(
            map((response: any) => {
                return response as ModelParametriFinanziamento[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getDescrizioneGraduatoria(sportelloCod: string): Observable<ModelDescrizioneGraduatoria> {
        return this.http.get(this.baseUrl + PathApi.getDescrizioneGraduatoria + '/' + sportelloCod).pipe(
            map((response: any) => {
                return response as ModelDescrizioneGraduatoria;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getCheckPubblicazioneGraduatoria(sportelloCod: string): Observable<boolean> {
        return this.http.get(this.baseUrl + PathApi.checkPubblicaGraduatoria + '/' + sportelloCod).pipe(
            map((response: any) => {
                return response as boolean;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.code == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    creaNuovaGraduatoria(nuovaGraduatoria: ModelNuovaGraduatoria): Observable<boolean> {
        return this.http.post(this.baseUrl + PathApi.creaNuovaGraduatoria, nuovaGraduatoria).pipe(
            map((response: any) => {
                return response as boolean;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    pubblicazioneGraduatoria(sportelloCod: string): Observable<boolean> {
      return this.http.get(this.baseUrl + PathApi.pubblicazioneGraduatoria + '/' + sportelloCod).pipe(
          map((response: any) => {
              return response as boolean;
          }),
          catchError((error: HttpErrorResponse) => {
              if (!error.error || error.error == null || error.error.code == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                  return;
              }
              return throwError(
                  this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
              )
          }
          )
      )
  }

  aggiornamentoGraduatoria(sportelloCod: string): Observable<boolean> {
    return this.http.get(this.baseUrl + PathApi.aggiornamentoGraduatoria + '/' + sportelloCod).pipe(
        map((response: any) => {
            return response as boolean;
        }),
        catchError((error: HttpErrorResponse) => {
            if (!error.error || error.error == null || error.error.code == null) {
                this.spinEmitter.emit(false);
                this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                return;
            }
            return throwError(
                this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
            )
        }
        )
    )
}

simulazioneGraduatoria(nuovaGraduatoria: ModelNuovaGraduatoria): Observable<boolean> {
  return this.http.post(this.baseUrl + PathApi.simulazioneGraduatoria, nuovaGraduatoria).pipe(
      map((response: any) => {
          return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
          if (!error.error || error.error == null || error.error.id == null) {
              this.spinEmitter.emit(false);
              this.router.navigate(['/redirect-page'], { skipLocationChange: true });
              return;
          }
          return throwError(
              this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
          )
      }
      )
  );
}

controlloEsistenzaGraduatoria(sportelloCod: string): Observable<boolean> {
  return this.http.get(this.baseUrl + PathApi.checkEsistenzaGraduatoria + '/' + sportelloCod).pipe(
      map((response: any) => {
          return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
          if (!error.error || error.error == null || error.error.code == null) {
              this.spinEmitter.emit(false);
              this.router.navigate(['/redirect-page'], { skipLocationChange: true });
              return;
          }
          return throwError(
              this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
          )
      }
      )
  )
}

controlloStatoGraduatoria(sportelloCod: string, stato: string): Observable<boolean> {
  return this.http.get(this.baseUrl + PathApi.checkStatoGraduatoria + '/' + sportelloCod + '/' + stato).pipe(
      map((response: any) => {
          return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
          if (!error.error || error.error == null || error.error.code == null) {
              this.spinEmitter.emit(false);
              this.router.navigate(['/redirect-page'], { skipLocationChange: true });
              return;
          }
          return throwError(
              this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
          )
      }
      )
  )
}

/*
   * API BUONO
  */
getStatiBuono(): Observable<ModelStatiBuono[]> {
    return this.http.get(this.baseUrl + PathApi.getStatiBuono).pipe(
      map((response: any) => {
        return response as ModelStatiBuono[];
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    )
  }

  getEsitiIsee(): Observable<ModelEsitiIsee[]> {
    return this.http.get(this.baseUrl + PathApi.getListaEsitiIsee).pipe(
        map((response: any) => {
            return response as ModelEsitiIsee[];
        }),
        catchError((error: HttpErrorResponse) => {
            if (!error.error || error.error == null || error.error.code == null) {
                this.spinEmitter.emit(false);
                this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                return;
            }
            return throwError(
                this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
            )
        }
        )
    )
}

  ricercaBuoni(filtri: FiltriRicercaBuoni): Observable<ModelRicercaBuono[]> {
    return this.http.post(this.baseUrl + PathApi.ricercaBuoni, filtri).pipe(
      map((response: any) => {
        return response as ModelRicercaBuono[];
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.detail[0].valore }))
        )
      }
      )
    )
  }


  getAllegatiBuono(buonoCod: String): Observable<ModelDichiarazioneSpesa[]> {
    return this.http.get(this.baseUrl + PathApi.getAllegatiBuono + '/' + buonoCod).pipe(
      map((response: any) => {
        return response as ModelDichiarazioneSpesa[];
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    )
  }

  scaricaAllegatoBuono(idAllegato: Number): string {
    return this.baseUrl + PathApi.scaricaAllegatoBuono + '/' + idAllegato;
  }

  scaricaRendicontazioneBuono(idAllegato: Number): string {
    return this.baseUrl + PathApi.scaricaRendicontazioneBuono + '/' + idAllegato;
  }

  getVerificheEnte(numeroDomanda: String): Observable<ModelVerificheEnte> {
    return this.http.get(this.baseUrl + PathApi.getverificheente + '/' + numeroDomanda).pipe(
      map((response: any) => {
        return response as ModelVerificheEnte;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    )
  }


  getContrattiBuono(buonoCod: String): Observable<ModelContrattoAllegati[]> {
    return this.http.get(this.baseUrl + PathApi.getContrattiBuono + '/' + buonoCod).pipe(
      map((response: any) => {
        return response as ModelContrattoAllegati[];
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    )
  }

  getDecorrenzaBuono(buonoCod: String): Observable<ModelDecorrenzaBuono> {
    return this.http.get(this.baseUrl + PathApi.getDecorrenzaBuono + '/' + buonoCod).pipe(
      map((response: any) => {
        return response as ModelDecorrenzaBuono;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    )
  }

  updateDecorrenzaBuono(buonoCod: String, decorrenza: ModelDecorrenzaBuono): Observable<ModelDecorrenzaBuono> {
    return this.http.post(this.baseUrl + PathApi.updateDecorrenzaBuono + '/' + buonoCod, decorrenza).pipe(
      map((response: any) => {
        return response as ModelDecorrenzaBuono;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    )
  }

  getIntegrazioniSpesaFiles(numerodDomanda: string, dateFiltro: any): Observable<ModelSpesaIntegrazione[]> {
    return this.http.post(this.baseUrl + PathApi.getIntegrazioniSpesaFiles + '/' + numerodDomanda, dateFiltro).pipe(
        map((response: any) => {
            return response as ModelSpesaIntegrazione[];
        }),
        catchError((error: HttpErrorResponse) => {
            if (!error.error || error.error == null || error.error.code == null) {
                this.spinEmitter.emit(false);
                this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                return;
            }
            return throwError(
                this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
            )
        }
        )
    )
}


  getBuonoInformazioni(numerorichiesta: string): Observable<any> {

    return this.http.get(this.baseUrl + PathApi.getBuonoInformazioni + '/' + numerorichiesta
    ).pipe(
        map((response: any) => {
            return response as any;
        }),
        catchError((error: HttpErrorResponse) => {
            if (!error.error || error.error == null || error.error.code == null) {
                this.spinEmitter.emit(false);
                this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                return;
            }
            return throwError(
                this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.detail[0].valore }))
            )
        }
        )
    );
}

getVerificaInformazioni(numerorichiesta: string): Observable<any> {

    return this.http.get(this.baseUrl + PathApi.getVerificaInformazioni + '/' + numerorichiesta
    ).pipe(
        map((response: any) => {
            return response as any;
        }),
        catchError((error: HttpErrorResponse) => {
            if (!error.error || error.error == null || error.error.code == null) {
                this.spinEmitter.emit(false);
                this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                return;
            }
            return throwError(
                this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.detail[0].valore }))
            )
        }
        )
    );
}

getStoricoRendicontazioni(numerorichiesta: string, numeroBuono: string): Observable<any> {

    return this.http.get(this.baseUrl + PathApi.getStoricoInformazioni + '/' + numeroBuono + '/' + numerorichiesta
    ).pipe(
        map((response: any) => {
            return response as any;
        }),
        catchError((error: HttpErrorResponse) => {
            if (!error.error || error.error == null || error.error.code == null) {
                this.spinEmitter.emit(false);
                this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                return;
            }
            return throwError(
                this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.detail[0].valore }))
            )
        }
        )
    );
}

getIseeConforme(numerorichiesta: string): Observable<ModelIseeBuono> {

  return this.http.get(this.baseUrl + PathApi.getIseeConforme + '/' + numerorichiesta
  ).pipe(
      map((response: any) => {
          return response as ModelIseeBuono;
      }),
       catchError((error: HttpErrorResponse) => {
          if (!error.error || error.error == null || error.error.code == null) {
              this.spinEmitter.emit(false);
              this.router.navigate(['/redirect-page'], { skipLocationChange: true });
              return;
          }
          return throwError(
              this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
          )
      }
      )
  )
}

getListaIseeConforme(numerorichiesta: string): Observable<ModelIseeBuono[]> {

  return this.http.get(this.baseUrl + PathApi.getListaIseeConforme + '/' + numerorichiesta
  ).pipe(
      map((response: any) => {
          return response as ModelIseeBuono[];
      }),
       catchError((error: HttpErrorResponse) => {
          if (!error.error || error.error == null || error.error.code == null) {
              this.spinEmitter.emit(false);
              this.router.navigate(['/redirect-page'], { skipLocationChange: true });
              return;
          }
          return throwError(
              this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.descrizione }))
          )
      }
      )
  )
}

salvaIsee(numerodomanda: string, isee: ModelIseeBuono): Observable<boolean> {
  return this.http.post(this.baseUrl + PathApi.salvaisee + '/' + numerodomanda, isee).pipe(
      map((response: any) => {
          return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
          if (!error.error || error.error == null || error.error.code == null) {
              this.spinEmitter.emit(false);
              this.router.navigate(['/redirect-page'], { skipLocationChange: true });
              return;
          }
          return throwError(
              this.BuonoresError.handleError(BuonoresError.toBuonoresError({ ...error, errorDesc: error.error.detail[0].valore }))
          )
      }
      )
  )
}



}
