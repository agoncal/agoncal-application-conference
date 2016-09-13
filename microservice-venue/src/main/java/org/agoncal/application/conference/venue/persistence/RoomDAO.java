package org.agoncal.application.conference.venue.persistence;

import org.agoncal.application.conference.venue.model.Room;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.*;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@ApplicationScoped
public class RoomDAO {

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
        roomMap.put("room8", new Room("room8", "Room 8", 696, "theatre"));
        roomMap.put("room5", new Room("room5", "Room 5", 641, "theatre"));
        roomMap.put("room9", new Room("room9", "Room 9", 393, "theatre"));
        roomMap.put("room6", new Room("room6", "Room 6", 372, "theatre"));
        roomMap.put("room7", new Room("room7", "Room 7", 370, "theatre"));
        roomMap.put("room4", new Room("room4", "Room 4", 347, "theatre"));
        roomMap.put("room3", new Room("room3", "Room 3", 300, "theatre"));
        roomMap.put("room10", new Room("room10", "Room 10", 286, "theatre"));
        roomMap.put("bof1", new Room("bof1", "BOF 1", 70, "classroom"));
        roomMap.put("bof2", new Room("bof2", "BOF 2", 70, "classroom"));
        roomMap.put("a_hall", new Room("a_hall", "Exhibition floor", 1500, "special"));
    }

    // ======================================
    // =          Business methods          =
    // ======================================

    public Room addRoom(Room room) {
        sequence++;
        room.setId("room" + sequence);
        roomMap.put("room" + sequence, room);
        return room;
    }

    public List<Room> getAllRooms() {
        return new ArrayList<>(roomMap.values());
    }

    public Optional<Room> findById(String id) {
        return Optional.ofNullable(roomMap.get(id));
    }

    public Room updateRoom(Room room) {
        if (room.getId() == null) {
            return addRoom(room);
        }

        roomMap.put(room.getId(), room);
        return room;
    }

    public void deleteRoom(String roomId) {
        if (roomId != null) {
            roomMap.remove(roomId);
        }
    }
}
