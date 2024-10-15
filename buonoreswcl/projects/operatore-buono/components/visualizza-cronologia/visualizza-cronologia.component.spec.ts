import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VisualizzaCronologiaComponent } from './visualizza-cronologia.component';

describe('VisualizzaCronologiaComponent', () => {
  let component: VisualizzaCronologiaComponent;
  let fixture: ComponentFixture<VisualizzaCronologiaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VisualizzaCronologiaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VisualizzaCronologiaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
