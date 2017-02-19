import {Component, OnInit} from "@angular/core";
import {ScheduleApi} from "../server/api/schedule.api";
import {Session} from "../server/model/Session";

@Component({
  selector: 'conf-schedule',
  templateUrl: './schedule.component.html',
  styleUrls: ['./schedule.component.css']
})
export class ScheduleComponent implements OnInit {

  private sessions: Session[];
  private selected: boolean = false; // TODO is that useful ?

  constructor(private scheduleService: ScheduleApi) {
  }

  ngOnInit() {
    this.scheduleService.allSessions()
      .subscribe(sessions => {
        this.sessions = sessions;
      });
  }
}
