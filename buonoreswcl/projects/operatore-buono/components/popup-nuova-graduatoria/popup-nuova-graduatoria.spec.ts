import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { PopupNuovaGraduatoriaComponent } from './popup-nuova-graduatoria';



describe('ChiudiEnteComponent', () => {
  let component: PopupNuovaGraduatoriaComponent;
  let fixture: ComponentFixture<PopupNuovaGraduatoriaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [PopupNuovaGraduatoriaComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PopupNuovaGraduatoriaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
