import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { PopupNuovoSportelloComponent } from './popup-nuovo-sportello';



describe('ChiudiEnteComponent', () => {
  let component: PopupNuovoSportelloComponent;
  let fixture: ComponentFixture<PopupNuovoSportelloComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [PopupNuovoSportelloComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PopupNuovoSportelloComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
