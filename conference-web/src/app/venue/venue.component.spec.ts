/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { VenueComponent } from './venue.component';

describe('VenueComponent', () => {
  let component: VenueComponent;
  let fixture: ComponentFixture<VenueComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VenueComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VenueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
