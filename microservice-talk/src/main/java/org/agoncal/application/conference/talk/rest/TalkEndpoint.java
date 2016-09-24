package org.agoncal.application.conference.talk.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.agoncal.application.conference.commons.rest.LinkableEndpoint;
import org.agoncal.application.conference.talk.domain.Talk;
import org.agoncal.application.conference.talk.domain.Talks;
import org.agoncal.application.conference.talk.repository.TalkRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
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
public class TalkEndpoint extends LinkableEndpoint<Talk> {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @Inject
    private TalkRepository talkRepository;

    // ======================================
    // =            Constructors            =
    // ======================================

    public TalkEndpoint() {
        super(TalkEndpoint.class);
    }

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
        return Response.created(getURIForSelf(created)).entity(created).build();
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
            talk.addSelfLink(getURIForSelf(talk));
            talk.addCollectionLink(getURIForCollection());
            preconditions = Response.ok(talk).tag(etag);
        }

        return preconditions.build();
    }

    @GET
    @ApiOperation(value = "Finds all the talks", response = Talk.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Rooms not found")}
    )
    public Response allTalks(@DefaultValue("1") @QueryParam("page") Integer pageNumber) {
        List<Talk> allTalks = talkRepository.findAllTalks(pageNumber);

        if (allTalks == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        for (Talk talk : allTalks) {
            talk.addSelfLink(getURIForSelf(talk));
        }
        Talks talks = new Talks(allTalks);
        Integer last = talkRepository.getNumberOfPages();
        talks.addSelfLink(getURIForPage(pageNumber));
        talks.addFirst(getURIForPage(1));
        talks.addLast(getURIForPage(last));

        Integer next;
        if (pageNumber < last)
            next = pageNumber + 1;
        else
            next = last;
        talks.addNext(getURIForPage(next));

        Integer previous;
        if (pageNumber == 1)
            previous = 1;
        else
            previous = pageNumber - 1;
        talks.addPrevious(getURIForPage(previous));


        return Response.ok(buildEntities(talks)).build();


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


    public GenericEntity<Talks> buildEntities(final Talks talks) {
        return new GenericEntity<Talks>(talks) {
        };
    }
}
