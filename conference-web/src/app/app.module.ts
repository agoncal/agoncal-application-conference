// CORE DEPS
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
// MATERIAL DESIGN MODULES
import { MaterialModule } from '@angular/material';

import { AppComponent } from './app.component';
import { AuthService } from './auth.service';
import { VoteComponent } from './vote/vote.component'

@NgModule({
  declarations: [
    AppComponent,
    VoteComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    MaterialModule.forRoot(),
  ],
  providers: [AuthService],
  bootstrap: [AppComponent]
})
export class AppModule { }
