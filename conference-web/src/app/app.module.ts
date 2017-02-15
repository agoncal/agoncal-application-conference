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
import { ScheduleDetailComponent } from './schedule-detail/schedule-detail.component';
import { VenueComponent } from './venue/venue.component';
import { VoteComponent } from './vote/vote.component';
import { SpeakerComponent } from './speaker/speaker.component';
import { SpeakerDetailComponent } from './speaker-detail/speaker-detail.component';
// Services
import { AuthService } from './auth.service';
import { AuthGuardService } from './auth-guard.service';
// Server APIs
import { ScheduleApi } from './server/api/schedule.api';
import { SpeakerApi } from './server/api/speaker.api';
import { AttendeeApi } from './server/api/attendee.api';
import { RatingApi } from './server/api/rating.api';
import { TalkApi } from './server/api/talk.api';
import { VenueApi } from './server/api/venue.api';
import { NoatPipe } from './shared/noat/noat.pipe';
import 'hammerjs';

@NgModule({
  declarations: [
    AppComponent,
    AttendeeComponent,
    LoginComponent,
    ScheduleComponent,
    ScheduleDetailComponent,
    SpeakerComponent,
    SpeakerDetailComponent,
    VenueComponent,
    VoteComponent,
    SpeakerComponent,
    SpeakerDetailComponent,
    NoatPipe
  ],
  imports: [
    BrowserModule,
    FormsModule,
    MdIconModule,
    HttpModule,
    MaterialModule.forRoot(),
    RouterModule.forRoot(ROUTES, {useHash: true})
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
export class AppModule {
}
