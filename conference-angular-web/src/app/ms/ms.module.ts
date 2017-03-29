import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { VenueApi, ScheduleApi, AttendeeApi, TalkApi, SpeakerApi } from './api/api';
import { ScheduleComponent } from './schedule/schedule.component';
import { VenueComponent } from './venue/venue.component';
import { CommonModule } from '@angular/common'
import { MaterialModule } from '@angular/material';
import { AttendeeComponent } from './attendee/attendee.component';
import { ScheduledetailsComponent } from './scheduledetails/scheduledetails.component';
import { SpeakerDetailComponent } from './speaker-detail/speaker-detail.component';


@NgModule({
  imports: [CommonModule, MaterialModule, RouterModule],
  exports: [ScheduleComponent, VenueComponent, AttendeeComponent, ScheduledetailsComponent, SpeakerDetailComponent],
  declarations: [ScheduleComponent, VenueComponent, AttendeeComponent, ScheduledetailsComponent, SpeakerDetailComponent],
  providers: [
    VenueApi, ScheduleApi,
    AttendeeApi, TalkApi, SpeakerApi
    ],
})
export class MsModule { }
