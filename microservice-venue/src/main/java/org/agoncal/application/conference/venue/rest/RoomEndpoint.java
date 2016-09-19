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
        return Response.created(getURIForSelf(room)).entity(created).build();
    }

    @GET
    @Path("/{id}")
    public Response retrieve(@PathParam("id") String id, @Context Request request) {

        Room room = roomRepository.findById(id);

        if (room == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        EntityTag etag = new EntityTag(Integer.toString(room.hashCode()));
        Response.ResponseBuilder preconditions = request.evaluatePreconditions(etag);

        // cached resource did change -> serve updated content
        if (preconditions == null) {
            room.addLink("self", getURIForSelf(room));
            room.addLink("collection", getURIForCollection());
            preconditions = Response.ok(room).tag(etag);
        }

        return preconditions.build();
    }

    @GET
    public Response allRooms() {
        List<Room> allRooms = roomRepository.findAllRooms();
        for (Room room : allRooms) {
            room.addLink("self", getURIForSelf(room));
        }
        return Response.ok(buildEntity(allRooms)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response remove(@PathParam("id") String id) {
        roomRepository.delete(id);
        return Response.noContent().build();
    }

    @PUT
    public Response update(Room room) {
        roomRepository.update(room);
        room.addLink("self", getURIForSelf(room));
        room.addLink("collection", getURIForCollection());
        return Response.ok(room).build();
    }

    // ======================================
    // =           Private methods          =
    // ======================================

    private GenericEntity<List<Room>> buildEntity(final List<Room> roomList) {
        return new GenericEntity<List<Room>>(roomList) {
        };
    }

    private URI getURIForSelf(Room room) {
        return uriInfo.getBaseUriBuilder().path(RoomEndpoint.class).path(room.getId()).build();
    }

    private URI getURIForCollection() {
        return uriInfo.getBaseUriBuilder().path(RoomEndpoint.class).build();
    }
}
