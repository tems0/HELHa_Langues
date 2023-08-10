import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListeSequencesComponent } from './liste-sequences.component';

describe('ListeSequencesComponent', () => {
  let component: ListeSequencesComponent;
  let fixture: ComponentFixture<ListeSequencesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListeSequencesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListeSequencesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
