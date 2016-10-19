package org.agoncal.application.conference.attendee.rest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.agoncal.application.conference.attendee.domain.Attendee;
import org.agoncal.application.conference.attendee.domain.Attendees;
import org.agoncal.application.conference.attendee.repository.AttendeeRepository;
import org.agoncal.application.conference.commons.constraints.NotEmpty;
import org.agoncal.application.conference.commons.rest.LinkableEndpoint;
import org.agoncal.application.conference.commons.jwt.KeyGenerator;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Path("/attendees")
@Api(description = "Attendees REST Endpoint")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AttendeeEndpoint extends LinkableEndpoint<Attendee> {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @Inject
    private Logger logger;

    @Inject
    private KeyGenerator keyGenerator;

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
    @Path("/login")
    @Consumes(APPLICATION_FORM_URLENCODED)
    @ApiOperation(value = "Logs an attendee with a user and password")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid input"),
        @ApiResponse(code = 401, message = "Invalid login/password")
    })
    public Response authenticateUser(@FormParam("login") @NotEmpty String login,
                                     @FormParam("password") @NotEmpty String password) {

        try {

            // Authenticate the user using the credentials provided
            Attendee attendee = attendeeRepository.findByLoginPassWord(login, password);
            if (attendee == null) {
                logger.warning("Invalid user/password");
                throw new SecurityException("Invalid user/password");
            }

            // Issue a token for the user
            String token = issueToken(login);

            // Return the token on the response
            return Response.ok().header(AUTHORIZATION, "Bearer " + token).build();

        } catch (Exception e) {
            logger.warning("UNAUTHORIZED " + e.toString());
            return Response.status(UNAUTHORIZED).build();
        }
    }

    @POST
    @ApiOperation(value = "Adds a new attendee to the conference")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid input")}
    )
    public Response add(@NotNull Attendee attendee) {
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
        @ApiResponse(code = 400, message = "Invalid input"),
        @ApiResponse(code = 404, message = "Attendees not found")}
    )
    public Response allAttendees(@DefaultValue("1") @QueryParam("page") @Min(1) Integer pageNumber) {
        List<Attendee> allAttendees = attendeeRepository.findAllAttendees(pageNumber);

        if (allAttendees == null || allAttendees.isEmpty())
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
        @ApiResponse(code = 405, message = "Invalid input")}
    )
    public Response remove(@PathParam("id") @NotEmpty String id) {
        attendeeRepository.delete(id);
        return Response.noContent().build();
    }

    // ======================================
    // =           Private methods          =
    // ======================================

    private GenericEntity<Attendees> buildEntities(final Attendees attendees) {
        return new GenericEntity<Attendees>(attendees) {
        };
    }

    private String issueToken(String login) {
        Key key = keyGenerator.generateKey();
        String jwtToken = Jwts.builder()
            .setSubject(login)
            .setIssuer(getUriInfo().getAbsolutePath().toString())
            .setIssuedAt(new Date())
            .setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
            .signWith(SignatureAlgorithm.HS512, key)
            .compact();
        logger.info("#### generating token for a key : " + jwtToken);
        return jwtToken;

    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
