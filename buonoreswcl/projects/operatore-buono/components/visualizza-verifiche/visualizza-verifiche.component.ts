import { Input } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material';
import { BuonoresBOClient } from '@buonores-app/app/BuonoresBOClient';
import { ModelVisualizzaVerifiche } from '@buonores-app/app/dto/ModelVisualizzaVerifiche';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-visualizza-verifiche',
  templateUrl: './visualizza-verifiche.component.html',
  styleUrls: ['./visualizza-verifiche.component.css']
})
export class VisualizzaVerificheComponent implements OnInit {

  @Input() public numeroDomanda: string;


  constructor(private client: BuonoresBOClient, public activeModal: NgbActiveModal) { }

  displayedColumns: string[] = ['controllo', 'fonte', 'dataControllo', 'note', 'noteRichiesta', 'dataRichiesta'];
  //'esitoEnteGestore','dateEoreEsito','note'];
  listaVerifiche: MatTableDataSource<ModelVisualizzaVerifiche>;

  ngOnInit() {
    this.client.spinEmitter.emit(true);
    this.listaVerifiche = new MatTableDataSource<ModelVisualizzaVerifiche>();

    this.client.getverifiche(this.numeroDomanda).subscribe(
      (response: ModelVisualizzaVerifiche[]) => {
        this.listaVerifiche.data = response as any;
        this.client.spinEmitter.emit(false);
      }
    )
  }

  isControllo(controllo: string, element: ModelVisualizzaVerifiche): boolean {
    if (controllo == 'NESSUNA_INCOMPATIBILITA' && element.tipo == 'NESSUNA_INCOMPATIBILITA')
      return true;
    else if (controllo == 'ATECO' && element.tipo == 'ATECO')
      return true;
    else if (controllo == 'INCOMPATIBILITA_PER_CONTRATTO' && element.tipo == 'INCOMPATIBILITA_PER_CONTRATTO')
      return true;
    else if (controllo == 'ISEE' && element.tipo == 'ISEE') {
      return true;
    }
    else if (controllo == 'ISEE_CITTADINO' && element.tipo == 'ISEE_CITTADINO') {
      return true;
    }
    else if (controllo == 'CONFORMITA_PSOCIALE' && element.tipo == 'CONFORMITA_PSOCIALE') {
      return true;
    }
    // else if (controllo == 'PUNTEGGIO_PSOCIALE' && element.tipo == 'PUNTEGGIO_PSOCIALE') {
    //   return true;
    // }
    return false;

  }

  dateToLocale(data: string) {
    let dataTemp = new Date(data);
    return data
  }

}
