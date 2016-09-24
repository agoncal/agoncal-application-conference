package org.agoncal.application.conference.venue.repository;

import org.agoncal.application.conference.venue.domain.Room;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@ApplicationScoped
public class RoomRepository {

    // ======================================
    // =             Attributes             =
    // ======================================

    private long sequence = 11L;

    private Map<String, Room> roomMap;

    // ======================================
    // =         Lifecycle methods          =
    // ======================================

    @PostConstruct
    private void intializeRoomMapWithDummyData() {
        roomMap = new HashMap<>();
        roomMap.put("room3", new Room("room3", "Room 3", 300, "theatre"));
        roomMap.put("room4", new Room("room4", "Room 4", 347, "theatre"));
        roomMap.put("room5", new Room("room5", "Room 5", 641, "theatre"));
        roomMap.put("room6", new Room("room6", "Room 6", 372, "theatre"));
        roomMap.put("room7", new Room("room7", "Room 7", 370, "theatre"));
        roomMap.put("room8", new Room("room8", "Room 8", 696, "theatre"));
        roomMap.put("room9", new Room("room9", "Room 9", 393, "theatre"));
        roomMap.put("room10", new Room("room10", "Room 10", 286, "theatre"));
        roomMap.put("bof1", new Room("bof1", "BOF 1", 70, "classroom"));
        roomMap.put("bof2", new Room("bof2", "BOF 2", 70, "classroom"));
        roomMap.put("a_hall", new Room("a_hall", "Exhibition floor", 1500, "special"));
    }

    // ======================================
    // =          Business methods          =
    // ======================================

    public Room create(Room room) {
        sequence++;
        room.setId("room" + sequence);
        roomMap.put("room" + sequence, room);
        return room;
    }

    public List<Room> findAllRooms() {
        return new ArrayList<>(roomMap.values());
    }

    public Room findById(String id) {
        Room room = roomMap.get(id);
        if (room != null)
            return room.clone();
        else
            return null;
    }

    public Room update(Room room) {
        if (room.getId() == null) {
            return create(room);
        }

        roomMap.put(room.getId(), room);
        return room;
    }

    public void delete(String id) {
        if (id != null) {
            roomMap.remove(id);
        }
    }
}
