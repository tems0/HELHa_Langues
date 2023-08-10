import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModifySequenceComponent } from './modify-sequence.component';

describe('ModifySequenceComponent', () => {
  let component: ModifySequenceComponent;
  let fixture: ComponentFixture<ModifySequenceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModifySequenceComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModifySequenceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
