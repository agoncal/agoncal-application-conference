import { Component, OnInit } from '@angular/core';
import { Speaker } from '../server/model/speaker';
import { SpeakerApi } from '../server/api/speaker.api';
import { Params, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'conf-speaker-detail',
  templateUrl: './speaker-detail.component.html',
  styleUrls: ['./speaker-detail.component.css']
})
export class SpeakerDetailComponent implements OnInit {

  private speaker: Speaker;
  private id: string;

  constructor(private speakersService: SpeakerApi,
              private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.params.forEach((params: Params) => {
      this.id = params['id'];
    });

    this.speakersService.retrieve(this.id)
      .toPromise()
      .then(speaker => {
        this.speaker = speaker;
      })
      .catch(error => {
        console.log(`An error has occured ${error}`);
      });
  }

}
