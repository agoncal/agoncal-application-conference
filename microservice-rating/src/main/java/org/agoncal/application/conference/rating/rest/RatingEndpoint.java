package org.agoncal.application.conference.rating.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.agoncal.application.conference.commons.rest.LinkableEndpoint;
import org.agoncal.application.conference.rating.domain.Rating;
import org.agoncal.application.conference.rating.repository.RatingRepository;

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
@Path("/ratings")
@Api(description = "Rating REST Endpoint")
@RequestScoped
@Produces("application/json")
@Consumes("application/json")
public class RatingEndpoint extends LinkableEndpoint<Rating> {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @Inject
    private RatingRepository ratingRepository;

    // ======================================
    // =            Constructors            =
    // ======================================

    public RatingEndpoint() {
        super(RatingEndpoint.class);
    }

    // ======================================
    // =          Business methods          =
    // ======================================

    @POST
    @ApiOperation(value = "Rates a session")
    @ApiResponses(value = {
        @ApiResponse(code = 405, message = "Invalid input")}
    )
    public Response rateSession(Rating rating) {
        Rating created = ratingRepository.rateSession(rating);
        return Response.created(getURIForSelf(created)).entity(created).build();
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Finds a rating by ID", response = Rating.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid input"),
        @ApiResponse(code = 404, message = "Rating not found")}
    )
    public Response retrieve(@PathParam("id") String id, @Context Request request) {

        Rating rating = ratingRepository.findById(id);

        if (rating == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        EntityTag etag = new EntityTag(Integer.toString(rating.hashCode()));
        Response.ResponseBuilder preconditions = request.evaluatePreconditions(etag);

        // cached resource did change -> serve updated content
        if (preconditions == null) {
            rating.addSelfLink(getURIForSelf(rating));
            rating.addCollectionLink(getURIForCollection());
            preconditions = Response.ok(rating).tag(etag);
        }

        return preconditions.build();
    }

    @GET
    @ApiOperation(value = "Finds all the ratings", response = Rating.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Ratings not found")}
    )
    public Response allRatings() {
        List<Rating> allRatings = ratingRepository.findAllRatings();

        if (allRatings == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        for (Rating rating : allRatings) {
            rating.addSelfLink(getURIForSelf(rating));
        }
        return Response.ok(buildEntity(allRatings)).build();
    }

    @GET
    @Path("/attendees/{attendeeId}")
    @ApiOperation(value = "Finds all the ratings for an attendee", response = Rating.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Ratings not found")}
    )
    public Response allRatingsByAttendee(@PathParam("attendeeId") String attendeeId) {
        List<Rating> allRatings = ratingRepository.findRatingsByAttendee(attendeeId);
        for (Rating rating : allRatings) {
            rating.addSelfLink(getURIForSelf(rating));
        }
        return Response.ok(buildEntity(allRatings)).build();
    }

    @GET
    @Path("/sessions/{sessionId}")
    @ApiOperation(value = "Finds all the ratings for a session", response = Rating.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Ratings not found")}
    )
    public Response allRatingsBySession(@PathParam("sessionId") String sessionId) {
        List<Rating> allRatings = ratingRepository.findRatingsBySession(sessionId);
        for (Rating rating : allRatings) {
            rating.addSelfLink(getURIForSelf(rating));
        }
        return Response.ok(buildEntity(allRatings)).build();
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Deletes a rating")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid rating value")}
    )
    public Response remove(@PathParam("id") String id) {
        ratingRepository.delete(id);
        return Response.noContent().build();
    }
}
