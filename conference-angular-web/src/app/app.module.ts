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
import { AuthService } from './auth.service';
import { BASE_PATH } from 'app/ms/variables';
import { environment } from '../environments/environment';

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
  providers: [
    AuthGuardService,
    AuthService,
    { provide: BASE_PATH, useValue: environment.basePath }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
