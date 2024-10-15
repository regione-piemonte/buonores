import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RendicontazioneOpComponent } from './rendicontazione-op.component';



describe('IstanzaAperteComponent', () => {
  let component: RendicontazioneOpComponent;
  let fixture: ComponentFixture<RendicontazioneOpComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RendicontazioneOpComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RendicontazioneOpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
