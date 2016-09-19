package org.agoncal.application.conference.venue.rest;

import io.swagger.annotations.Api;
import org.agoncal.application.conference.venue.domain.Room;
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
@Consumes("application/json")
public class RoomEndpoint {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @Inject
    private RoomRepository roomRepository;

    @Context
    private UriInfo uriInfo;

    // ======================================
    // =          Business methods          =
    // ======================================

    @POST
    public Response add(Room room) {
        Room created = roomRepository.create(room);
        return Response.created(URI.create("/" + created.getId())).entity(created).build();
    }

    @GET
    @Path("/{id}")
    public Response retrieve(@PathParam("id") String id) {

        Room room = roomRepository.findById(id);

        if (room != null) {
            room.addLink("self", getURIForSelf(room));
            return Response.ok(room).build();
        } else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    public Response allRooms() {
        List<Room> allRooms = roomRepository.getAllRooms();
        for (Room room : allRooms) {
            room.addLink("self", getURIForSelf(room));
        }
        GenericEntity<List<Room>> entity = buildEntity(allRooms);
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("/{id}")
    public Response remove(@PathParam("id") String id) {
        roomRepository.delete(id);
        return Response.noContent().build();
    }

    // ======================================
    // =           Private methods          =
    // ======================================

    private GenericEntity<List<Room>> buildEntity(final List<Room> roomList) {
        return new GenericEntity<List<Room>>(roomList) {
        };
    }

    private URI getURIForSelf(Room room) {
        return uriInfo.getAbsolutePathBuilder().path(RoomEndpoint.class).path(room.getId()).build();
    }
}
