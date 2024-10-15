import { Input } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material';
import { BuonoresBOClient } from '@buonores-app/app/BuonoresBOClient';
import { ModelVisualizzaCronologia } from '@buonores-app/app/dto/ModelVisualizzaCronologia';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-visualizza-cronologia',
  templateUrl: './visualizza-cronologia.component.html',
  styleUrls: ['./visualizza-cronologia.component.css']
})
export class VisualizzaCronologiaComponent implements OnInit {

  @Input() public numeroDomanda: string;


  constructor(private client: BuonoresBOClient, public activeModal: NgbActiveModal) { }

  displayedColumns: string[] = ['dataOra', 'utente', 'statoDomanda', 'notaEnte', 'notaInterna', 'notaxEnte'];
  listaCronologia: MatTableDataSource<ModelVisualizzaCronologia[]>;

  ngOnInit() {
    this.client.spinEmitter.emit(true);
    this.listaCronologia = new MatTableDataSource<ModelVisualizzaCronologia[]>();

    this.client.getCronologiaArchivio(this.numeroDomanda).subscribe(
      (response: ModelVisualizzaCronologia[]) => {
        this.listaCronologia.data = response as any;
        this.client.spinEmitter.emit(false);
      }
    )
  }
}
