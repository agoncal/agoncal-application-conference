import { Component, OnInit } from '@angular/core';
import { Http, Headers, RequestOptionsArgs, Response, URLSearchParams } from '@angular/http';
import { AttendeeApi } from '../server/api/AttendeeApi';
import { Attendee } from '../server/model/Attendee';

@Component({
  selector: 'conf-attendee',
  templateUrl: './attendee.component.html',
  styleUrls: ['./attendee.component.css']
})
export class AttendeeComponent implements OnInit {

    attendees: Attendee[];
    next: string;
    prev: string;
    hasLinks: boolean = false;

  constructor(private attendeesService: AttendeeApi) { }

  ngOnInit() {
    this.attendeesService.allAttendees()
      .toPromise()
      .then(attendees => {
        this.attendees = attendees;

        this.next = this.attendeesService.links["next"] ? this.attendeesService.links["next"] : undefined;
        this.prev = this.attendeesService.links["prev"] ? this.attendeesService.links["prev"] : undefined;

        this.hasLinks = (this.next != undefined || this.prev != undefined);
      })
      .catch(error => {
        console.log(`An error has occured ${error}`);
      });

  }

  gotoPrev() {
    var uri = new URL(this.attendeesService.links["prev"]);
    var urlsParams = new URLSearchParams(uri.search);

    var nextPage = urlsParams.get("?page");

    this.attendeesService.allAttendees(+nextPage)
      .toPromise()
      .then(attendees => {
        this.attendees = attendees
      });

  }

  gotoNext() {
    var uri = new URL(this.attendeesService.links["next"]);
    var urlsParams = new URLSearchParams(uri.search);

    var nextPage = urlsParams.get("?page");

    this.attendeesService.allAttendees(+nextPage)
      .toPromise()
      .then(attendees => {
        this.attendees = attendees
      });

  }

}
