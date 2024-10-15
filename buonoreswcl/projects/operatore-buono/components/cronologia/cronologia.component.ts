import { animate, state, style, transition, trigger } from '@angular/animations';
import { HostListener, Input } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material';
import { ActivatedRoute, Navigation, Router } from '@angular/router';
import { BuonoresBOClient } from '@buonores-app/app/BuonoresBOClient';
import { ModelVisualizzaCronologia } from '@buonores-app/app/dto/ModelVisualizzaCronologia';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-cronologia',
  templateUrl: './cronologia.component.html',
  styleUrls: ['./cronologia.component.css'],
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
export class CronologiaComponent implements OnInit {
 navigation: Navigation;
numeroDomanda: string;
 displayedColumns: string[] = ['dataOra', 'utente', 'statoDomanda', 'notaEnte', 'notaInterna', 'notaxEnte'];
  listaCronologia: MatTableDataSource<ModelVisualizzaCronologia[]>;
 espansa:boolean;
  state: string = 'default';
  constructor( private client: BuonoresBOClient,private route: ActivatedRoute,private router: Router ) {
	this.navigation = this.router.getCurrentNavigation();
    let domandaValues: string[] = [];
    this.route.fragment.subscribe((frag: string) => {
      domandaValues.push(frag);
    });
    this.numeroDomanda = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.numerodomanda : domandaValues[0][0];
	 }

  ngOnInit() {
	this.client.spinEmitter.emit(true);
    this.espansa=false;
    this.listaCronologia = new MatTableDataSource<ModelVisualizzaCronologia[]>();

      this.client.getCronologiaArchivio(this.numeroDomanda).subscribe(
        (response: ModelVisualizzaCronologia[]) => {
        this.listaCronologia.data = response as any;
      this.client.spinEmitter.emit(false);
	}
      )}

apricronologia(){
    if (this.espansa)
    this.espansa=false;
    else
    this.espansa=true;
    this.state = (this.state === 'default' ? 'rotated' : 'default');
  }



 @HostListener('document:updateCronoEmitter')
  updateCrono(){
    this.ngOnInit();
    this.espansa=false;
    this.state='rotated';
    this.apricronologia();

  }
}
