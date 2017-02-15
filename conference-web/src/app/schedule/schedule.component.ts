import { Component, OnInit } from '@angular/core';
import { ScheduleApi } from '../server/api/ScheduleApi';
import { Session } from '../server/model/Session';

@Component({
    selector: 'conf-schedule',
    templateUrl: './schedule.component.html',
    styleUrls: ['./schedule.component.css']
})
export class ScheduleComponent implements OnInit {

    private _sessions: Session[];
    selected: boolean = false; // TODO is that useful ?

    constructor(private scheduleService: ScheduleApi) {
    }

    ngOnInit() {
        this.scheduleService.allSessions().subscribe((sessions: Session[]) => {
            this._sessions = sessions
        });
    }


    get sessions(): Session[] {
        return this._sessions;
    }
}
