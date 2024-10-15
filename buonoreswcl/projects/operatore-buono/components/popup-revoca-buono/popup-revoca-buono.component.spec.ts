import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { PopuppopupRevocaBuonoComponent } from './popup-revoca-buono.component';

describe('PopuppopupRevocaBuonoComponent', () => {
  let component: PopuppopupRevocaBuonoComponent;
  let fixture: ComponentFixture<PopuppopupRevocaBuonoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PopuppopupRevocaBuonoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PopuppopupRevocaBuonoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
