import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { AuthService } from '../auth.service';
import { Http, Headers, RequestOptionsArgs, Response } from '@angular/http';
import {Talk} from "../server/model/talk";
import {Session} from "../server/model/session";
import {ScheduleApi} from "../server/api/schedule.api";
import {TalkApi} from "../server/api/talk.api";
import {SpeakerApi} from "../server/api/speaker.api";

@Component({
  selector: 'conf-vote',
  templateUrl: './vote.component.html',
  styleUrls: ['./vote.component.css']
})
export class VoteComponent implements OnInit {

    private id: string;
    public session: Session;
    public talk: Talk;
    rating: any;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private scheduleService: ScheduleApi,
        private talkService: TalkApi,
        private speakerService: SpeakerApi,
        private authService: AuthService,
        private http: Http

    ) { }

  ngOnInit() {
      this.route.params.forEach((params: Params) => {
          this.id = params['id'];
      });

      this.getSession(this.id);
  }

    onSubmit() {

        var jwt = this.authService.jwt;
        let defaultHeaders: Headers = new Headers();
        defaultHeaders.append('Authorization', 'Bearer ' + jwt);

        let requestOptions: RequestOptionsArgs = {
            method: 'POST',
            headers: defaultHeaders
        };

        return this.http.request('http://conference.docker.localhost:90/conference-rating/api/ratings/uni_room9_tuesday_8_13h30_16h30',
            requestOptions)
            .subscribe((response:Response) => {
                console.log(response.json());
            })


    }

    private getSession(id: string) {
        this.scheduleService
            .retrieve(id)
            .flatMap((s: Session) => {
                this.session = s;
                return s ? this.talkService.retrieve(s.talk.id) : undefined;
            }).subscribe((t: Talk) => {
            this.talk = t;
            return t ? t.speakers : undefined;
        });
    }
}
