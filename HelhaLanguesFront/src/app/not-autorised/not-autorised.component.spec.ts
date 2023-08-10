import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotAutorisedComponent } from './not-autorised.component';

describe('NotAutorisedComponent', () => {
  let component: NotAutorisedComponent;
  let fixture: ComponentFixture<NotAutorisedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NotAutorisedComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NotAutorisedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
