import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { IstanzaAperteComponent } from './istanze-aperte.component';



describe('IstanzaAperteComponent', () => {
  let component: IstanzaAperteComponent;
  let fixture: ComponentFixture<IstanzaAperteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IstanzaAperteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IstanzaAperteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
