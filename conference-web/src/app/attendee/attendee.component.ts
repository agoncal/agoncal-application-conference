import { Component, OnInit } from '@angular/core';
import { URLSearchParams } from '@angular/http';
import { AttendeeApi } from '../server/api/attendee.api';
import { Attendee } from '../server/model/attendee';

@Component({
  selector: 'conf-attendee',
  templateUrl: './attendee.component.html',
  styleUrls: ['./attendee.component.css']
})
export class AttendeeComponent implements OnInit {

    private attendees: Attendee[];
    private next: string;
    private prev: string;
    private hasLinks: boolean = false;

  constructor(private attendeesService: AttendeeApi) { }

  ngOnInit() {
    this.attendeesService.allAttendees()
      .subscribe(attendees => {
        this.attendees = attendees;

        this.next = this.attendeesService.links["next"] ? this.attendeesService.links["next"] : undefined;
        this.prev = this.attendeesService.links["prev"] ? this.attendeesService.links["prev"] : undefined;

        this.hasLinks = (this.next != undefined || this.prev != undefined);
      });
  }

  gotoPrev() {
    let uri = new URL(this.attendeesService.links["prev"]);
    let urlsParams = new URLSearchParams(uri.search);

    let nextPage = urlsParams.get("?page");

    this.attendeesService.allAttendees(+nextPage)
      .toPromise()
      .then(attendees => {
        this.attendees = attendees
      });

  }

  gotoNext() {
    let uri = new URL(this.attendeesService.links["next"]);
    let urlsParams = new URLSearchParams(uri.search);

    let nextPage = urlsParams.get("?page");

    this.attendeesService.allAttendees(+nextPage)
      .toPromise()
      .then(attendees => {
        this.attendees = attendees
      });

  }

}
