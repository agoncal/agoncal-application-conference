import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Router, Params} from "@angular/router";
import {ScheduleApi, SpeakerApi, TalkApi, RatingApi} from "../ms/api/api";
import * as models from "../ms/model/models";
import {AuthService} from "../auth.service";
import "rxjs/Rx";
import {Http, Headers, RequestOptionsArgs, Response} from "@angular/http";

@Component({
  selector: 'app-vote',
  templateUrl: './vote.component.html',
  styleUrls: ['./vote.component.css']
})
export class VoteComponent implements OnInit {

  private id: string;
  public session: models.Session;
  public talk: models.Talk;
  public speakers : models.Speaker[];

  constructor(private route: ActivatedRoute,
              private router: Router,
              private scheduleService: ScheduleApi,
              private talkService: TalkApi,
              private speakerService: SpeakerApi,
              private authService: AuthService,
              private ratingService : RatingApi,
              private http: Http) {
  }

  rating: any;

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

    this.ratingService.rate("id", 5, requestOptions)
      .subscribe((value:any) => {
        console.log("success :)");
      }, (error:any) => {
        console.error(error);
      });

    // return this.http.request('http://conference.docker.localhost:90/conference-rating/api/ratings/uni_room9_tuesday_8_13h30_16h30',
    //   requestOptions)
    //   .subscribe((response: Response) => {
    //     console.log(response.json());
    //   })

  }

  private getSession(id: string) {
    this.scheduleService
      .retrieve(id)
      .flatMap((s: models.Session) => {
        this.session = s;
        return s ? this.talkService.retrieve(s.talk.id) : undefined;
      }).subscribe((t: models.Talk) => {
      this.talk = t;
      console.log(t);
      this.speakers = t ? t.speakers : undefined;
    });
  }

}
