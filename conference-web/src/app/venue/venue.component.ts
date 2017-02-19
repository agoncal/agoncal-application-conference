import {Component, OnInit} from "@angular/core";
import {Room} from "../server/model/room";
import {VenueApi} from "../server/api/venue.api";

@Component({
  selector: 'conf-venue',
  templateUrl: './venue.component.html',
  styleUrls: ['./venue.component.css']
})
export class VenueComponent implements OnInit {

  rooms: Room[];

  constructor(private roomService: VenueApi) {
  }

  ngOnInit() {
    this.roomService.allRooms()
      .subscribe(rooms => {
        this.rooms = rooms;
      })
  }

  showRoom(room: Room) {

  }
}
