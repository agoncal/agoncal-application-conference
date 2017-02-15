// CORE DEPS
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule } from '@angular/router';
// MATERIAL DESIGN MODULES
import { MaterialModule } from '@angular/material';
import { MdIconModule } from '@angular/material/icon';

import { ROUTES } from './app.routes';

// Components
import { AppComponent } from './app.component';
import { AttendeeComponent } from './attendee/attendee.component';
import { LoginComponent } from './login/login.component';
import { ScheduleComponent } from './schedule/schedule.component';
import { ScheduleDetailComponent } from './schedule-detail/schedule-detail.component'
import { VenueComponent } from './venue/venue.component';
import { VoteComponent } from './vote/vote.component';
// Services
import { AuthService } from './auth.service';
import { AuthGuardService } from './auth-guard.service';
// Server APIs
import { ScheduleApi } from './server/api/ScheduleApi';
import { AttendeeApi } from './server/api/AttendeeApi';
import { RatingApi } from './server/api/RatingApi';
import { SpeakerApi } from './server/api/SpeakerApi';
import { TalkApi } from './server/api/TalkApi';
import { VenueApi } from './server/api/VenueApi';
import 'hammerjs';

@NgModule({
  declarations: [
    AppComponent,
    AttendeeComponent,
    LoginComponent,
    ScheduleComponent,
    ScheduleDetailComponent,
    VenueComponent,
    VoteComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    MdIconModule,
    HttpModule,
    MaterialModule.forRoot(),
    RouterModule.forRoot(ROUTES,{useHash: true})
  ],
  providers: [
      AuthGuardService,
      AuthService,
      AttendeeApi,
      RatingApi,
      ScheduleApi,
      SpeakerApi,
      TalkApi,
      VenueApi
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
