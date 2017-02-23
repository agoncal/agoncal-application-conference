import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ScheduleComponent } from './ms/schedule/schedule.component';
import { VenueComponent } from './ms/venue/venue.component';
import { AppComponent } from './app.component';
import { AttendeeComponent } from './ms/attendee/attendee.component';
import { VoteComponent } from './vote/vote.component';
import { LoginComponent } from './login/login.component';
import { AuthGuardService } from './auth.guard.service';
import { ScheduledetailsComponent } from './ms/scheduledetails/scheduledetails.component';

const routes: Routes = [
  { path: 'rooms', component: VenueComponent },
  { path: 'sessions', component: ScheduleComponent },
  { path: 'sessions/:id', component: ScheduledetailsComponent },
  { path: '', component: ScheduleComponent },
  { path: 'login', component: LoginComponent },
  { path: 'attendees', component : AttendeeComponent},
  { path: 'ratings/:id', component: VoteComponent, canActivate: [AuthGuardService] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: []
})
export class Ngcli03RoutingModule { }
