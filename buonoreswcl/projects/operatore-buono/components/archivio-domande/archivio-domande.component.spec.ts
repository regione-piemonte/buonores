import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArchivioDomandeComponent } from './archivio-domande.component';

describe('ArchivioDomandeComponent', () => {
  let component: ArchivioDomandeComponent;
  let fixture: ComponentFixture<ArchivioDomandeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ArchivioDomandeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArchivioDomandeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
