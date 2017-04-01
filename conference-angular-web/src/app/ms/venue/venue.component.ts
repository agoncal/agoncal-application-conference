import { Component, OnInit } from '@angular/core';
import {VenueApi } from '../api/api';
import * as models from '../model/models';

@Component({
    selector: 'app-venue',
    templateUrl: './venue.component.html',
    styleUrls: ['./venue.component.css']
})
export class VenueComponent implements OnInit {
    rooms: models.Room[];
    selected: boolean = false;

    constructor(private roomService: VenueApi) { }

    ngOnInit() {
        this.roomService.allRooms()
            .toPromise()
            .then(rooms => {
                this.rooms = rooms.sort((a,b) => a.capacity > b.capacity ? -1 : 1);
            })
    }

    showRoom(room: models.Room) {

    }
}