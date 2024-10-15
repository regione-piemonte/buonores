import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BuonoDettaglioComponent } from './buono-dettaglio.component';


describe('BuonoDettaglioComponent', () => {
  let component: BuonoDettaglioComponent;
  let fixture: ComponentFixture<BuonoDettaglioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BuonoDettaglioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BuonoDettaglioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
