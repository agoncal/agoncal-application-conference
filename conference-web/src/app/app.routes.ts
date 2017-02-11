import { Routes } from '@angular/router';

// APP COMPONENTS
import { VenueComponent } from './venue/venue.component';
import { LoginComponent } from './login/login.component';
import { VoteComponent } from './vote/vote.component';
import { ScheduleComponent } from './schedule/schedule.component';
import { AttendeeComponent } from './attendee/attendee.component';
import { AuthGuardService } from './auth-guard.service';
import { ScheduleDetailComponent } from './schedule-detail/schedule-detail.component';

export const ROUTES: Routes = [
    { path: 'rooms', component: VenueComponent },
    { path: 'sessions', component: ScheduleComponent },
    { path: 'sessions/:id', component: ScheduleDetailComponent },
    { path: '', component: ScheduleComponent },
    { path: 'login', component: LoginComponent },
    { path: 'attendees', component : AttendeeComponent},
    { path: 'ratings/:id', component: VoteComponent, canActivate: [AuthGuardService] }
];
