import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DefaultApiVenue, DefaultApiSchedule, ContactService, DefaultApiAttendee, DefaultApiTalk, DefaultApiSpeaker } from './api/api';
import { ScheduleComponent } from './schedule/schedule.component';
import { VenueComponent } from './venue/venue.component';
import { CommonModule } from '@angular/common'
import { MaterialModule } from '@angular/material';
import { AttendeeComponent } from './attendee/attendee.component';
import { ScheduledetailsComponent } from './scheduledetails/scheduledetails.component';


@NgModule({
  imports: [CommonModule, MaterialModule.forRoot(), RouterModule],
  exports: [ScheduleComponent, VenueComponent, AttendeeComponent, ScheduledetailsComponent],
  declarations: [ScheduleComponent, VenueComponent, AttendeeComponent, ScheduledetailsComponent],
  providers: [
    ContactService, DefaultApiVenue, DefaultApiSchedule,
    DefaultApiAttendee, DefaultApiTalk, DefaultApiSpeaker],
})
export class MsModule { }
