package org.agoncal.application.conference.venue.rest;

import io.swagger.annotations.*;
import org.agoncal.application.conference.commons.constraints.NotEmpty;
import org.agoncal.application.conference.commons.rest.LinkableEndpoint;
import org.agoncal.application.conference.venue.domain.Room;
import org.agoncal.application.conference.venue.domain.Rooms;
import org.agoncal.application.conference.venue.repository.RoomRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Path("/rooms")
@Api(description = "Rooms REST Endpoint", tags = {"Venue"})
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomEndpoint extends LinkableEndpoint<Room> {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @Inject
    private RoomRepository roomRepository;

    // ======================================
    // =            Constructors            =
    // ======================================

    public RoomEndpoint() {
        super(RoomEndpoint.class);
    }

    // ======================================
    // =          Business methods          =
    // ======================================

    @POST
    @ApiOperation(value = "Adds a new room to the venue")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "The room is created"),
        @ApiResponse(code = 400, message = "Invalid input")}
    )
    public Response add(@ApiParam(value = "Room to be created", required = true) @NotNull Room room) {
        Room created = roomRepository.create(room);
        return Response.created(getURIForSelf(room)).entity(created).build();
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Finds a room by ID", response = Room.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Room found"),
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
            room.addSelfLink(getURIForSelf(room));
            room.addCollectionLink(getURIForCollection());
            room.addSwaggerLink(getURIForSwagger());
            preconditions = Response.ok(room).tag(etag);
        }

        return preconditions.build();
    }

    @GET
    @ApiOperation(value = "Finds all the rooms", response = Room.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "All rooms found"),
        @ApiResponse(code = 404, message = "Rooms not found")}
    )
    public Response allRooms() {
        List<Room> allRooms = roomRepository.findAllRooms();

        if (allRooms == null || allRooms.isEmpty())
            return Response.status(Response.Status.NOT_FOUND).build();

        for (Room room : allRooms) {
            room.addSelfLink(getURIForSelf(room));
        }
        Rooms rooms = new Rooms(allRooms);
        rooms.addSwaggerLink(getURIForSwagger());

        return Response.ok(buildEntities(rooms)).build();
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Deletes a room")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Room has been deleted"),
        @ApiResponse(code = 405, message = "Invalid room value")}
    )
    public Response remove(@PathParam("id") @NotEmpty String id) {
        roomRepository.delete(id);
        return Response.noContent().build();
    }

    @PUT
    @ApiOperation(value = "Update an existing room")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Rooms updated"),
        @ApiResponse(code = 405, message = "Invalid values")}
    )
    public Response update(@ApiParam(value = "Room to be updated", required = true) @NotNull Room room) {
        roomRepository.update(room);
        room.addSelfLink(getURIForSelf(room));
        room.addCollectionLink(getURIForCollection());
        room.addSwaggerLink(getURIForSwagger());
        return Response.ok(room).build();
    }

    // ======================================
    // =           Private methods          =
    // ======================================

    private GenericEntity<Rooms> buildEntities(final Rooms rooms) {
        return new GenericEntity<Rooms>(rooms) {
        };
    }
}
