import { Component, OnInit } from '@angular/core';
import { Speaker } from '../server/model/speaker';
import { SpeakerApi } from '../server/api/speaker.api';
import { URLSearchParams } from '@angular/http';

@Component({
  selector: 'conf-speaker',
  templateUrl: './speaker.component.html',
  styleUrls: ['./speaker.component.css']
})
export class SpeakerComponent implements OnInit {

  private speakers: Speaker[];
  private next: string;
  private prev: string;
  private hasLinks: boolean = false;

  constructor(private speakersService: SpeakerApi) {
  }

  ngOnInit() {
    this.speakersService.allSpeakers()
      .subscribe(speakers => {
        this.speakers = speakers;

        this.next = this.speakersService.links["next"] ? this.speakersService.links["next"] : undefined;
        this.prev = this.speakersService.links["prev"] ? this.speakersService.links["prev"] : undefined;

        this.hasLinks = (this.next != undefined || this.prev != undefined);
      });
  }

  gotoPrev() {
    let uri = new URL(this.speakersService.links["prev"]);
    let urlsParams = new URLSearchParams(uri.search);

    let nextPage = urlsParams.get("?page");

    this.speakersService.allSpeakers(+nextPage)
      .toPromise()
      .then(speakers => {
        this.speakers = speakers
      });

  }

  gotoNext() {
    let uri = new URL(this.speakersService.links["next"]);
    let urlsParams = new URLSearchParams(uri.search);

    let nextPage = urlsParams.get("?page");

    this.speakersService.allSpeakers(+nextPage)
      .toPromise()
      .then(speakers => {
        this.speakers = speakers
      });

  }

}
