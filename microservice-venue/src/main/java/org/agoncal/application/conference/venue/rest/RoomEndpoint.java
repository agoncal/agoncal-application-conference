package org.agoncal.application.conference.venue.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    @ApiOperation(value = "Adds a new room to the venue")
    @ApiResponses(value = {
        @ApiResponse(code = 405, message = "Invalid input")}
    )
    public Response add(Room room) {
        Room created = roomRepository.create(room);
        return Response.created(getURIForSelf(room)).entity(created).build();
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Finds a room by ID", response = Room.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid input"),
        @ApiResponse(code = 404, message = "Room not found")}
    )
    public Response retrieve(@PathParam("id") String id, @Context Request request) {

        Room room = roomRepository.findById(id);

        if (room == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        EntityTag etag = new EntityTag(Integer.toString(room.hashCode()));
        Response.ResponseBuilder preconditions = request.evaluatePreconditions(etag);

        // cached resource did change -> serve updated content
        if (preconditions == null) {
            room.addSelf(getURIForSelf(room));
            room.addCollection(getURIForCollection());
            preconditions = Response.ok(room).tag(etag);
        }

        return preconditions.build();
    }

    @GET
    @ApiOperation(value = "Finds all the rooms", response = Room.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Rooms not found")}
    )
    public Response allRooms() {
        List<Room> allRooms = roomRepository.findAllRooms();

        if (allRooms == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        for (Room room : allRooms) {
            room.addSelf(getURIForSelf(room));
        }
        return Response.ok(buildEntity(allRooms)).build();
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Deletes a room")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid room value")}
    )
    public Response remove(@PathParam("id") String id) {
        roomRepository.delete(id);
        return Response.noContent().build();
    }

    @PUT
    @ApiOperation(value = "Update an existing room")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Pet not found"),
        @ApiResponse(code = 405, message = "Validation exception")}
    )
    public Response update(Room room) {
        roomRepository.update(room);
        room.addSelf(getURIForSelf(room));
        room.addCollection(getURIForCollection());
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
