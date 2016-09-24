package org.agoncal.application.conference.attendee.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.agoncal.application.conference.attendee.domain.Attendee;
import org.agoncal.application.conference.attendee.domain.Attendees;
import org.agoncal.application.conference.attendee.repository.AttendeeRepository;
import org.agoncal.application.conference.commons.rest.LinkableEndpoint;

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
    private AttendeeRepository attendeeRepository;

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
    public Response add(Attendee attendee) {
        Attendee created = attendeeRepository.create(attendee);
        return Response.created(getURIForSelf(attendee)).entity(created).build();
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Finds an attendee by ID", response = Attendee.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid input"),
        @ApiResponse(code = 404, message = "Attendee not found")}
    )
    public Response retrieve(@PathParam("id") String id, @Context Request request) {

        Attendee attendee = attendeeRepository.findById(id);

        if (attendee == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        EntityTag etag = new EntityTag(Integer.toString(attendee.hashCode()));
        Response.ResponseBuilder preconditions = request.evaluatePreconditions(etag);

        // cached resource did change -> serve updated content
        if (preconditions == null) {
            attendee.addSelfLink(getURIForSelf(attendee));
            attendee.addCollectionLink(getURIForCollection());
            preconditions = Response.ok(attendee).tag(etag);
        }

        return preconditions.build();
    }

    @GET
    @ApiOperation(value = "Finds all the attendees", response = Attendee.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Attendees not found")}
    )
    public Response allAttendees(@DefaultValue("1") @QueryParam("page") Integer pageNumber) {
        List<Attendee> allAttendees = attendeeRepository.findAllAttendees(pageNumber);

        if (allAttendees == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        for (Attendee attendee : allAttendees) {
            attendee.addSelfLink(getURIForSelf(attendee));
        }

        Attendees attendees = new Attendees(allAttendees);
        Integer last = attendeeRepository.getNumberOfPages();
        attendees.addSelfLink(getURIForPage(pageNumber));
        attendees.addFirst(getURIForPage(1));
        attendees.addLast(getURIForPage(last));
        attendees.addNext(getURIForPage(pageNumber < last ? pageNumber + 1 : last));
        attendees.addPrevious(getURIForPage(pageNumber == 1 ? 1 : pageNumber - 1));

        return Response.ok(buildEntities(attendees)).build();
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Deletes an attendee")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid attendee value")}
    )
    public Response remove(@PathParam("id") String id) {
        attendeeRepository.delete(id);
        return Response.noContent().build();
    }

    // ======================================
    // =           Private methods          =
    // ======================================

    private GenericEntity<Attendees> buildEntities(final Attendees talks) {
        return new GenericEntity<Attendees>(talks) {
        };
    }
}
