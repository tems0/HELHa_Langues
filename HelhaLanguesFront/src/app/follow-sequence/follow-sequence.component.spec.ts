import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FollowSequenceComponent } from './follow-sequence.component';

describe('FollowSequenceComponent', () => {
  let component: FollowSequenceComponent;
  let fixture: ComponentFixture<FollowSequenceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FollowSequenceComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FollowSequenceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
