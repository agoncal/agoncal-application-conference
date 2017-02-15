import { Component, OnInit } from '@angular/core';
import { Room } from '../server/model/Room';
import { VenueApi } from '../server/api/VenueApi';

@Component({
  selector: 'conf-venue',
  templateUrl: './venue.component.html',
  styleUrls: ['./venue.component.css']
})
export class VenueComponent implements OnInit {
    rooms: Room[];
    selected: boolean = false;

  constructor(private roomService: VenueApi) { }

  ngOnInit() {
      this.roomService.allRooms()
          .toPromise()
          .then(rooms => {
              this.rooms = rooms;
          })
  }

}
