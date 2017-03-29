import { Component, OnInit } from '@angular/core';
import { ScheduleApi } from '../api/api';
import * as models from '../model/models';
import { MdButton } from '@angular/material';

@Component({
  selector: 'app-schedule',
  templateUrl: './schedule.component.html',
  styleUrls: ['./schedule.component.css']
})
export class ScheduleComponent implements OnInit {

  sessions: models.Session[];
  selected: boolean = false;

  constructor(private scheduleService: ScheduleApi) { }

  ngOnInit() {

    this.scheduleService.allSessions()
      .toPromise()
      .then(sessions => {
        this.sessions = sessions;
      })
  }


}
