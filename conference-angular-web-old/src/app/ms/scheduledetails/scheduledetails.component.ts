import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { DefaultApiSchedule, DefaultApiSpeaker, DefaultApiTalk } from '../api/api';
import * as models from '../model/models';
import { Observable } from 'rxjs/Observable';
import 'rxjs/Rx';

@Component({
  selector: 'app-scheduledetails',
  templateUrl: './scheduledetails.component.html',
  styleUrls: ['./scheduledetails.component.css']
})
export class ScheduledetailsComponent implements OnInit {

  private id: string;
  public session: models.Session;
  public talk: models.Talk;
  public speakers: models.Speaker[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private scheduleService: DefaultApiSchedule,
    private talkService: DefaultApiTalk,
    private speakerService: DefaultApiSpeaker

  ) { }

  ngOnInit() {

    this.route.params.forEach((params: Params) => {
      this.id = params['id'];
    });

    this.getSession(this.id);
  }



  private getSession(id: string) {
    this.scheduleService
      .retrieve(id)
      .flatMap((s: models.Session) => {
        this.session = s;
        return s ? this.talkService.retrieve(s.talk.id) : undefined;
      }).map((t: models.Talk) => {
        this.talk = t;
        return t ? t.speakers : undefined;
      })
      .subscribe((speakers: models.Speaker[]) => {
        speakers.forEach((s) => {
          this.speakerService.retrieve(s.id).subscribe((sp) => this.speakers.push(sp));
        });
      });
  }

  private getSession2(id: string) {
    this.scheduleService.retrieve(id)
      .toPromise()
      .then((s) => {
        this.session = s;
        return s ? this.talkService.retrieve(s.talk.id).toPromise() : undefined;
      })
      .then((t) => {
        this.talk = t;
        if (t && t.speakers) {
          t.speakers.forEach((s) => {
            this.speakerService.retrieve(s.id).toPromise().then((sp) => this.speakers.push(sp));
          });
        }
      });

  }



}
