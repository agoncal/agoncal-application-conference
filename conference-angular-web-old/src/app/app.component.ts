import { Component, ViewChild } from '@angular/core';
import * as ms from './ms';
import {MdSidenav} from "@angular/material";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Devoxx roxx !';

  @ViewChild("sidenav")
  sidenav: MdSidenav;

  openOrCloseSidenav() {
    if (this.sidenav.opened){
      this.sidenav.close();
    }else{
      this.sidenav.open();
    }
  }

  constructor() {


  }
}
