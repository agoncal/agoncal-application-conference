package org.agoncal.application.conference.venue.repository;

import org.agoncal.application.conference.venue.resource.RoomResource;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.*;

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

    private Map<String, RoomResource> roomMap;

    // ======================================
    // =         Lifecycle methods          =
    // ======================================

    @PostConstruct
    private void intializeRoomMapWithDummyData() {
        roomMap = new HashMap<>();
        roomMap.put("room8", new RoomResource("room8", "Room 8", 696, "theatre"));
        roomMap.put("room5", new RoomResource("room5", "Room 5", 641, "theatre"));
        roomMap.put("room9", new RoomResource("room9", "Room 9", 393, "theatre"));
        roomMap.put("room6", new RoomResource("room6", "Room 6", 372, "theatre"));
        roomMap.put("room7", new RoomResource("room7", "Room 7", 370, "theatre"));
        roomMap.put("room4", new RoomResource("room4", "Room 4", 347, "theatre"));
        roomMap.put("room3", new RoomResource("room3", "Room 3", 300, "theatre"));
        roomMap.put("room10", new RoomResource("room10", "Room 10", 286, "theatre"));
        roomMap.put("bof1", new RoomResource("bof1", "BOF 1", 70, "classroom"));
        roomMap.put("bof2", new RoomResource("bof2", "BOF 2", 70, "classroom"));
        roomMap.put("a_hall", new RoomResource("a_hall", "Exhibition floor", 1500, "special"));
    }

    // ======================================
    // =          Business methods          =
    // ======================================

    public RoomResource create(RoomResource room) {
        sequence++;
        room.setId("room" + sequence);
        roomMap.put("room" + sequence, room);
        return room;
    }

    public List<RoomResource> getAllRooms() {
        return new ArrayList<>(roomMap.values());
    }

    public RoomResource findById(String id) {
        return roomMap.get(id);
    }

    public RoomResource update(RoomResource room) {
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
