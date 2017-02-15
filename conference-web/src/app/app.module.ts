// CORE DEPS
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule } from '@angular/router';
// MATERIAL DESIGN MODULES
import { MaterialModule } from '@angular/material';
import {MdIconModule} from '@angular/material/icon';

import { ROUTES } from './app.routes';

import { AppComponent } from './app.component';
import { AuthService } from './auth.service';
import { VoteComponent } from './vote/vote.component';
import { VenueComponent } from './venue/venue.component';
import { ScheduleComponent } from './schedule/schedule.component';
import { AttendeeComponent } from './attendee/attendee.component';
import { ScheduleDetailComponent } from './schedule-detail/schedule-detail.component'
import { AuthGuardService } from './auth-guard.service';
import { LoginComponent } from './login/login.component';
import { ScheduleApi } from './server/api/ScheduleApi';
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
      ScheduleApi
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
