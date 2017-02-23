import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { MaterialModule } from '@angular/material';
import { AppComponent } from './app.component';
import { ConferenceRoutingModule } from './app.routes';
import { RouterModule, Routes } from '@angular/router';
import { MsModule } from './ms/ms.module';
import { LoginComponent } from './login/login.component';
import { VoteComponent } from './vote/vote.component';
import { AuthGuardService } from './auth.guard.service';
import { AuthService } from './auth.service'

import 'hammerjs';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    VoteComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    RouterModule,
    ConferenceRoutingModule,
    MaterialModule,
    MsModule
  ],
  providers: [AuthGuardService, AuthService],
  bootstrap: [AppComponent]
})
export class AppModule { }
