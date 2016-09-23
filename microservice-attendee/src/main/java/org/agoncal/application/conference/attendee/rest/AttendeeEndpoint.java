package org.agoncal.application.conference.attendee.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.agoncal.application.conference.attendee.domain.Attendee;
import org.agoncal.application.conference.attendee.repository.AttendeeRepository;

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
@Path("/attendees")
@Api(description = "Attendees REST Endpoint")
@RequestScoped
@Produces("application/json")
@Consumes("application/json")
public class AttendeeEndpoint {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @Inject
    private AttendeeRepository speakerRepository;

    @Context
    private UriInfo uriInfo;

    // ======================================
    // =          Business methods          =
    // ======================================

    @POST
    @ApiOperation(value = "Adds a new attendee to the conference")
    @ApiResponses(value = {
        @ApiResponse(code = 405, message = "Invalid input")}
    )
    public Response add(Attendee speaker) {
        Attendee created = speakerRepository.create(speaker);
        return Response.created(getURIForSelf(speaker)).entity(created).build();
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Finds an attendee by ID", response = Attendee.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid input"),
        @ApiResponse(code = 404, message = "Attendee not found")}
    )
    public Response retrieve(@PathParam("id") String id, @Context Request request) {

        Attendee speaker = speakerRepository.findById(id);

        if (speaker == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        EntityTag etag = new EntityTag(Integer.toString(speaker.hashCode()));
        Response.ResponseBuilder preconditions = request.evaluatePreconditions(etag);

        // cached resource did change -> serve updated content
        if (preconditions == null) {
            speaker.addSelfLink(getURIForSelf(speaker));
            speaker.addCollectionLink(getURIForCollection());

            preconditions = Response.ok(speaker).tag(etag);
        }

        return preconditions.build();
    }

    @GET
    @ApiOperation(value = "Finds all the attendees", response = Attendee.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Attendees not found")}
    )
    public Response allSpeakers() {
        List<Attendee> allSpeakers = speakerRepository.findAllSpeakers();

        if (allSpeakers == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        for (Attendee speaker : allSpeakers) {
            speaker.addSelfLink(getURIForSelf(speaker));
        }
        return Response.ok(buildEntity(allSpeakers)).build();
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Deletes an attendee")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid attendee value")}
    )
    public Response remove(@PathParam("id") String id) {
        speakerRepository.delete(id);
        return Response.noContent().build();
    }

    // ======================================
    // =           Private methods          =
    // ======================================

    private GenericEntity<List<Attendee>> buildEntity(final List<Attendee> speakerList) {
        return new GenericEntity<List<Attendee>>(speakerList) {
        };
    }

    private URI getURIForSelf(Attendee speaker) {
        return uriInfo.getBaseUriBuilder().path(AttendeeEndpoint.class).path(speaker.getId()).build();
    }

    private URI getURIForCollection() {
        return uriInfo.getBaseUriBuilder().path(AttendeeEndpoint.class).build();
    }
}
