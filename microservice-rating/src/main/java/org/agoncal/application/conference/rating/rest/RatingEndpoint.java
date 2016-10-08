package org.agoncal.application.conference.rating.rest;

import io.jsonwebtoken.Jwts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.agoncal.application.conference.commons.constraints.NotEmpty;
import org.agoncal.application.conference.commons.registry.AttendeeMicroService;
import org.agoncal.application.conference.commons.registry.SessionMicroService;
import org.agoncal.application.conference.commons.rest.LinkableEndpoint;
import org.agoncal.application.conference.commons.security.JWTTokenNeeded;
import org.agoncal.application.conference.commons.security.KeyGenerator;
import org.agoncal.application.conference.rating.domain.Rating;
import org.agoncal.application.conference.rating.domain.Ratings;
import org.agoncal.application.conference.rating.repository.RatingRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Path("/ratings")
@Api(description = "Rating REST Endpoint")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RatingEndpoint extends LinkableEndpoint<Rating> {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @Inject
    private RatingRepository ratingRepository;

    @Inject
    private Logger logger;

    @Inject
    private KeyGenerator keyGenerator;

    @Inject
    @AttendeeMicroService
    private UriBuilder uriAttendee;

    @Inject
    @SessionMicroService
    private UriBuilder uriSession;

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
    @Path("{sessionId}")
    @Consumes(APPLICATION_FORM_URLENCODED)
    @JWTTokenNeeded
    @ApiOperation(value = "Allows an attendee to rate a talk")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid input"),
        @ApiResponse(code = 401, message = "Needs to authenticate first")
    })
    public Response rate(@PathParam("sessionId") @NotEmpty String sessionId,
                         @FormParam("mark") @Min(1) @Max(5) Integer mark,
                         @Context HttpHeaders headers) {

        // Gets the login from the token
        String token = headers.getHeaderString(HttpHeaders.AUTHORIZATION).substring("Bearer".length()).trim();
        String login = Jwts.parser().setSigningKey(keyGenerator.generateKey()).parseClaimsJws(token).getBody().getSubject();

        Rating rating = new Rating(sessionId, login, mark);
        logger.info("#### rating : " + rating);

        Rating created = ratingRepository.rateSession(rating);
        return Response.created(getURIForSelf(created)).entity(created).build();
    }


    @GET
    @Path("/{id}")
    @ApiOperation(value = "Finds a rating by ID", response = Rating.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid input"),
        @ApiResponse(code = 404, message = "Rating not found")
    })
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
            rating.setAttendeeId(uriAttendee.clone().path(rating.getAttendeeId()).build().toString());
            rating.setSessionId(uriSession.clone().path(rating.getSessionId()).build().toString());
            preconditions = Response.ok(rating).tag(etag);
        }

        return preconditions.build();
    }

    @GET
    @ApiOperation(value = "Finds all the ratings", response = Rating.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Ratings not found")}
    )
    public Response allRatings(@DefaultValue("1") @QueryParam("page") @Min(1) Integer pageNumber) {
        List<Rating> allRatings = ratingRepository.findAllRatings(pageNumber);

        if (allRatings == null || allRatings.isEmpty())
            return Response.status(Response.Status.NOT_FOUND).build();

        for (Rating rating : allRatings) {
            rating.addSelfLink(getURIForSelf(rating));
            rating.setAttendeeId(uriAttendee.clone().path(rating.getAttendeeId()).build().toString());
            rating.setSessionId(uriSession.clone().path(rating.getSessionId()).build().toString());
        }

        Ratings ratings = new Ratings(allRatings);
        Integer last = ratingRepository.getNumberOfPages();
        ratings.addSelfLink(getURIForPage(pageNumber));
        ratings.addFirst(getURIForPage(1));
        ratings.addLast(getURIForPage(last));
        ratings.addNext(getURIForPage(pageNumber < last ? pageNumber + 1 : last));
        ratings.addPrevious(getURIForPage(pageNumber == 1 ? 1 : pageNumber - 1));

        return Response.ok(buildEntities(ratings)).build();
    }

    @GET
    @Path("/attendees/{attendeeId}")
    @ApiOperation(value = "Finds all the ratings for an attendee", response = Rating.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid input"),
        @ApiResponse(code = 404, message = "Ratings not found")}
    )
    public Response allRatingsByAttendee(@PathParam("attendeeId") @NotEmpty String attendeeId) {
        List<Rating> allRatings = ratingRepository.findRatingsByAttendee(attendeeId);

        if (allRatings == null || allRatings.isEmpty())
            return Response.status(Response.Status.NOT_FOUND).build();

        for (Rating rating : allRatings) {
            rating.addSelfLink(getURIForSelf(rating));
        }
        return Response.ok(buildEntity(allRatings)).build();
    }

    @GET
    @Path("/sessions/{sessionId}")
    @ApiOperation(value = "Finds all the ratings for a session", response = Rating.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid input"),
        @ApiResponse(code = 404, message = "Ratings not found")}
    )
    public Response allRatingsBySession(@PathParam("sessionId") @NotEmpty String sessionId) {
        List<Rating> allRatings = ratingRepository.findRatingsBySession(sessionId);

        if (allRatings == null || allRatings.isEmpty())
            return Response.status(Response.Status.NOT_FOUND).build();

        for (Rating rating : allRatings) {
            rating.addSelfLink(getURIForSelf(rating));
        }
        return Response.ok(buildEntity(allRatings)).build();
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Deletes a rating")
    @ApiResponses(value = {
        @ApiResponse(code = 405, message = "Invalid rating input")}
    )
    public Response remove(@PathParam("id") @NotEmpty String id) {
        ratingRepository.delete(id);
        return Response.noContent().build();
    }

    // ======================================
    // =           Private methods          =
    // ======================================

    private GenericEntity<Ratings> buildEntities(final Ratings ratings) {
        return new GenericEntity<Ratings>(ratings) {
        };
    }
}
