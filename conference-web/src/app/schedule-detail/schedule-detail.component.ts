import {Component, OnInit} from "@angular/core";
import {Speaker} from "../server/model/speaker";
import {Talk} from "../server/model/talk";
import {Session} from "../server/model/session";
import {ScheduleApi} from "../server/api/schedule.api";
import {TalkApi} from "../server/api/talk.api";
import {SpeakerApi} from "../server/api/speaker.api";
import {ActivatedRoute, Params} from "@angular/router";

@Component({
  selector: 'conf-schedule-detail',
  templateUrl: './schedule-detail.component.html',
  styleUrls: ['./schedule-detail.component.css']
})
export class ScheduleDetailComponent implements OnInit {

  public session: Session;
  public talk: Talk;
  public speakers: Speaker[] = [];

  constructor(private route: ActivatedRoute,
              private scheduleService: ScheduleApi,
              private talkService: TalkApi,
              private speakerService: SpeakerApi) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      const id = params['id'];
      this.getSession(id);
    });
  }


  private getSession(id: string) {
    this.scheduleService
      .retrieve(id)
      .flatMap((s: Session) => {
        this.session = s;
        return s ? this.talkService.retrieve(s.talk.id) : undefined;
      }).map((t: Talk) => {
      this.talk = t;
      return t ? t.speakers : undefined;
    })
      .subscribe((speakers: Speaker[]) => {
        speakers.forEach((s) => {
          this.speakerService.retrieve(s.id).subscribe((sp) => this.speakers.push(sp));
        });
      });
  }
}
