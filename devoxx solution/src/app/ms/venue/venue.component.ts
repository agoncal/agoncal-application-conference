import { Component, OnInit } from '@angular/core';
import { DefaultApiVenue } from '../api/api';
import * as models from '../model/models';

@Component({
    selector: 'app-venue',
    templateUrl: './venue.component.html',
    styleUrls: ['./venue.component.css']
})
export class VenueComponent implements OnInit {
    rooms: models.Room[];
    selected: boolean = false;

    constructor(private roomService: DefaultApiVenue) { }

    ngOnInit() {
        this.roomService.allRooms()
            .toPromise()
            .then(rooms => {
                this.rooms = rooms;
            })
    }

    showRoom(room: models.Room) {

    }
}