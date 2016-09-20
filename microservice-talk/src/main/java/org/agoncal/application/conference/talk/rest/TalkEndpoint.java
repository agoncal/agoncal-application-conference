package org.agoncal.application.conference.talk.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.agoncal.application.conference.talk.domain.Talk;
import org.agoncal.application.conference.talk.repository.TalkRepository;

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
@Path("/talks")
@Api(description = "Talks REST Endpoint")
@RequestScoped
@Produces("application/json")
@Consumes("application/json")
public class TalkEndpoint {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @Inject
    private TalkRepository talkRepository;

    @Context
    private UriInfo uriInfo;

    // ======================================
    // =          Business methods          =
    // ======================================

    @POST
    @ApiOperation(value = "Adds a new talk to the conference")
    @ApiResponses(value = {
        @ApiResponse(code = 405, message = "Invalid input")}
    )
    public Response add(Talk talk) {
        Talk created = talkRepository.create(talk);
        return Response.created(getURIForSelf(talk)).entity(created).build();
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Finds a room by ID", response = Talk.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid input"),
        @ApiResponse(code = 404, message = "Room not found")}
    )
    public Response retrieve(@PathParam("id") String id, @Context Request request) {

        Talk talk = talkRepository.findById(id);

        if (talk == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        EntityTag etag = new EntityTag(Integer.toString(talk.hashCode()));
        Response.ResponseBuilder preconditions = request.evaluatePreconditions(etag);

        // cached resource did change -> serve updated content
        if (preconditions == null) {
            talk.addLink("self", getURIForSelf(talk));
            talk.addLink("collection", getURIForCollection());
            preconditions = Response.ok(talk).tag(etag);
        }

        return preconditions.build();
    }

    @GET
    @ApiOperation(value = "Finds all the talks", response = Talk.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Rooms not found")}
    )
    public Response allTalks() {
        List<Talk> allTalks = talkRepository.findAllTalks();

        if (allTalks == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        for (Talk talk : allTalks) {
            talk.addLink("self", getURIForSelf(talk));
        }
        return Response.ok(buildEntity(allTalks)).build();
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Deletes a talk")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid talk value")}
    )
    public Response remove(@PathParam("id") String id) {
        talkRepository.delete(id);
        return Response.noContent().build();
    }

    // ======================================
    // =           Private methods          =
    // ======================================

    private GenericEntity<List<Talk>> buildEntity(final List<Talk> talkList) {
        return new GenericEntity<List<Talk>>(talkList) {
        };
    }

    private URI getURIForSelf(Talk talk) {
        return uriInfo.getBaseUriBuilder().path(TalkEndpoint.class).path(talk.getId()).build();
    }

    private URI getURIForCollection() {
        return uriInfo.getBaseUriBuilder().path(TalkEndpoint.class).build();
    }
}
