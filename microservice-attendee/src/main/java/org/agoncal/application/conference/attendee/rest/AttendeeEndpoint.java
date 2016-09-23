package org.agoncal.application.conference.attendee.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.agoncal.application.conference.attendee.domain.Attendee;
import org.agoncal.application.conference.attendee.repository.AttendeeRepository;
import org.agoncal.application.conference.commons.rest.LinkableEndpoint;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
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
public class AttendeeEndpoint extends LinkableEndpoint<Attendee> {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @Inject
    private AttendeeRepository speakerRepository;

    // ======================================
    // =            Constructors            =
    // ======================================

    public AttendeeEndpoint() {
        super(AttendeeEndpoint.class);
    }

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
}
