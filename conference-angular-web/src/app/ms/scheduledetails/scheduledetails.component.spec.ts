import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ScheduledetailsComponent } from './scheduledetails.component';

describe('ScheduledetailsComponent', () => {
  let component: ScheduledetailsComponent;
  let fixture: ComponentFixture<ScheduledetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ScheduledetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ScheduledetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
