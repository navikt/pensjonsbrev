import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BrevFeltComponent } from './brev-felt.component';

describe('BrevFeltComponent', () => {
  let component: BrevFeltComponent;
  let fixture: ComponentFixture<BrevFeltComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BrevFeltComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BrevFeltComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
