import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { MsModule } from './ms';
import { MaterialModule } from '@angular/material';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { Ngcli03RoutingModule } from './app-routing.module';
import { VoteComponent } from './vote/vote.component';
import { AuthGuardService } from './auth.guard.service';
import { AuthService } from './auth.service'
import { RouterModule } from '@angular/router';
import 'hammerjs';

@NgModule({
  declarations: [
    AppComponent,
    VoteComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    RouterModule,
    Ngcli03RoutingModule,
    MaterialModule.forRoot(),
    MsModule
  ],
  providers: [AuthGuardService, AuthService],
  bootstrap: [AppComponent]
})
export class AppModule { }
