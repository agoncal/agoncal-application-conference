import { Routes } from '@angular/router';
// APP COMPONENTS
import { VenueComponent } from './venue/venue.component';
import { LoginComponent } from './login/login.component';
import { VoteComponent } from './vote/vote.component';
import { ScheduleComponent } from './schedule/schedule.component';
import { AttendeeComponent } from './attendee/attendee.component';
import { AuthGuardService } from './auth-guard.service';
import { ScheduleDetailComponent } from './schedule-detail/schedule-detail.component';
import { SpeakerComponent } from './speaker/speaker.component';
import { SpeakerDetailComponent } from './speaker-detail/speaker-detail.component';

export const ROUTES: Routes = [
  {path: '', component: ScheduleComponent},
  {path: 'sessions', component: ScheduleComponent},
  {path: 'sessions/:id', component: ScheduleDetailComponent},
  {path: 'speakers', component: SpeakerComponent},
  {path: 'speakers/:id', component: SpeakerDetailComponent},
  {path: 'login', component: LoginComponent},
  {path: 'attendees', component: AttendeeComponent},
  {path: 'ratings/:id', component: VoteComponent, canActivate: [AuthGuardService]},
  {path: 'rooms', component: VenueComponent}
];
