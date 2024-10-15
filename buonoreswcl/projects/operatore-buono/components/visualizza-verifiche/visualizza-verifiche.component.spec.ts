import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VisualizzaVerificheComponent } from './visualizza-verifiche.component';

describe('VisualizzaCronologiaComponent', () => {
  let component: VisualizzaVerificheComponent;
  let fixture: ComponentFixture<VisualizzaVerificheComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VisualizzaVerificheComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VisualizzaVerificheComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
