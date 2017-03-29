import { Component, OnInit } from '@angular/core';
import { SpeakerApi } from '../api/api';
import { Params, ActivatedRoute } from '@angular/router';
import { Speaker } from '../model/Speaker';

@Component({
  selector: 'conf-speaker-detail',
  templateUrl: './speaker-detail.component.html',
  styleUrls: ['./speaker-detail.component.css']
})
export class SpeakerDetailComponent implements OnInit {

  public speaker: Speaker;

  constructor(private speakersService:SpeakerApi,
              private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      const id = params['id'];
      this.speakersService.retrieve(id).subscribe(speaker => this.speaker = speaker);
    });
  }

}
