package org.agoncal.application.conference.venue.rest;

import io.swagger.annotations.Api;
import org.agoncal.application.conference.venue.resource.Room;
import org.agoncal.application.conference.venue.repository.RoomRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Path("/rooms")
@Api(description = "Rooms REST Endpoint")
@RequestScoped
@Produces("application/json")
public class RoomEndpoint {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @Inject
    private RoomRepository roomDAO;

    @Context
    UriInfo uriInfo;

    // ======================================
    // =          Business methods          =
    // ======================================

    @POST
    @Consumes("application/json")
    public Response add(Room room) {
        Room created = roomDAO.addRoom(room);
        return Response.created(URI.create("/" + created.getId()))
            .entity(created)
            .build();
    }

    @GET
    @Path("/{id}")
    public Response retrieve(@PathParam("id") String roomId) {

        Room room = roomDAO.findById(roomId);

        if (room != null) {
            room.addLink("self", uriInfo.getAbsolutePath().resolve(room.getId()));
            return Response.ok(room).build();
        } else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    public Response allRooms() {
        List<Room> allRooms = roomDAO.getAllRooms();
        for (Room room : allRooms) {
            room.addLink("self", uriInfo.getAbsolutePath().resolve(room.getId()));
        }
        GenericEntity<List<Room>> entity = buildEntity(allRooms);
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("/{id}")
    public Response remove(@PathParam("id") String roomId) {
        roomDAO.deleteRoom(roomId);
        return Response.noContent().build();
    }

    // ======================================
    // =           Private methods          =
    // ======================================

    private GenericEntity<List<Room>> buildEntity(final List<Room> roomList) {
        return new GenericEntity<List<Room>>(roomList) {
        };
    }
}
