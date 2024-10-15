import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { IstruttoriaComponent } from './istruttoria.component';




describe('Istruttoria', () => {
  let component: IstruttoriaComponent;
  let fixture: ComponentFixture<IstruttoriaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IstruttoriaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IstruttoriaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
