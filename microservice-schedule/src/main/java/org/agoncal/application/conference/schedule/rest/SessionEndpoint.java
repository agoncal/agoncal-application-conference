package org.agoncal.application.conference.schedule.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.agoncal.application.conference.commons.rest.LinkableEndpoint;
import org.agoncal.application.conference.schedule.domain.Session;
import org.agoncal.application.conference.schedule.domain.Sessions;
import org.agoncal.application.conference.schedule.repository.SessionRepository;

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
@Path("/sessions")
@Api(description = "Session REST Endpoint")
@RequestScoped
@Produces("application/json")
@Consumes("application/json")
public class SessionEndpoint extends LinkableEndpoint<Session> {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @Inject
    private SessionRepository sessionRepository;

    // ======================================
    // =            Constructors            =
    // ======================================

    public SessionEndpoint() {
        super(SessionEndpoint.class);
    }

    // ======================================
    // =          Business methods          =
    // ======================================

    @POST
    @ApiOperation(value = "Adds a new session to the conference")
    @ApiResponses(value = {
        @ApiResponse(code = 405, message = "Invalid input")}
    )
    public Response add(Session session) {
        Session created = sessionRepository.create(session);
        return Response.created(getURIForSelf(session)).entity(created).build();
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Finds a session by ID", response = Session.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid input"),
        @ApiResponse(code = 404, message = "Session not found")}
    )
    public Response retrieve(@PathParam("id") String id, @Context Request request) {

        Session session = sessionRepository.findById(id);

        if (session == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        EntityTag etag = new EntityTag(Integer.toString(session.hashCode()));
        Response.ResponseBuilder preconditions = request.evaluatePreconditions(etag);

        // cached resource did change -> serve updated content
        if (preconditions == null) {
            session.addSelfLink(getURIForSelf(session));
            session.addCollectionLink(getURIForCollection());
            session.addLink(session.getDay(), getUriBuilderForRoot().path(session.getDay()).build());
            for (int i = 3; i < 11; i++) {
                session.addLink("room" + i, getUriBuilderForRoot().path(session.getDay()).path("room" + i).build());
            }
            preconditions = Response.ok(session).tag(etag);
        }

        return preconditions.build();
    }

    @GET
    @ApiOperation(value = "Finds all the sessions", response = Session.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Sessions not found")}
    )
    public Response allSessions(@DefaultValue("1") @QueryParam("page") Integer pageNumber) {
        List<Session> allSessions = sessionRepository.findAllSessions(pageNumber);

        if (allSessions == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        for (Session session : allSessions) {
            session.addSelfLink(getURIForSelf(session));
        }

        Sessions sessions = new Sessions(allSessions);
        Integer last = sessionRepository.getNumberOfPages();
        sessions.addSelfLink(getURIForPage(pageNumber));
        sessions.addFirst(getURIForPage(1));
        sessions.addLast(getURIForPage(last));
        sessions.addNext(getURIForPage(pageNumber < last ? pageNumber + 1 : last));
        sessions.addPrevious(getURIForPage(pageNumber == 1 ? 1 : pageNumber - 1));
        sessions.addLink("monday", getUriBuilderForRoot().path("monday").build());
        sessions.addLink("tuesday", getUriBuilderForRoot().path("tuesday").build());
        sessions.addLink("wednesday", getUriBuilderForRoot().path("wednesday").build());
        sessions.addLink("thursday", getUriBuilderForRoot().path("thursday").build());
        sessions.addLink("friday", getUriBuilderForRoot().path("friday").build());

        return Response.ok(buildEntities(sessions)).build();
    }

    @GET
    @Path("/monday")
    @ApiOperation(value = "Finds all the sessions for Monday", response = Session.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Sessions not found")}
    )
    public Response allSessionsMonday() {
        return allSessionsByDay("monday");
    }

    @GET
    @Path("/monday/{roomId}")
    @ApiOperation(value = "Finds all the sessions for Monday on a specific room", response = Session.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Sessions not found")}
    )
    public Response allSessionsMondayByRoom(@PathParam("roomId") String roomId) {
        return allSessionsByDayAndRoom("monday", roomId);
    }

    @GET
    @Path("/tuesday")
    @ApiOperation(value = "Finds all the sessions for Tuesday", response = Session.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Sessions not found")}
    )
    public Response allSessionsTuesday() {
        return allSessionsByDay("tuesday");
    }

    @GET
    @Path("/tuesday/{roomId}")
    @ApiOperation(value = "Finds all the sessions for Tuesday on a specific room", response = Session.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Sessions not found")}
    )
    public Response allSessionsTuesdayByRoom(@PathParam("roomId") String roomId) {
        return allSessionsByDayAndRoom("tuesday", roomId);
    }

    @GET
    @Path("/wednesday")
    @ApiOperation(value = "Finds all the sessions for Wednesday", response = Session.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Sessions not found")}
    )
    public Response allSessionsWednesday() {
        return allSessionsByDay("wednesday");
    }

    @GET
    @Path("/wednesday/{roomId}")
    @ApiOperation(value = "Finds all the sessions for Wednesday on a specific room", response = Session.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Sessions not found")}
    )
    public Response allSessionsWednesdayByRoom(@PathParam("roomId") String roomId) {
        return allSessionsByDayAndRoom("wednesday", roomId);
    }

    @GET
    @Path("/thursday")
    @ApiOperation(value = "Finds all the sessions for Thursday", response = Session.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Sessions not found")}
    )
    public Response allSessionsThursday() {
        return allSessionsByDay("thursday");
    }

    @GET
    @Path("/thursday/{roomId}")
    @ApiOperation(value = "Finds all the sessions for Thursday on a specific room", response = Session.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Sessions not found")}
    )
    public Response allSessionsThursdayByRoom(@PathParam("roomId") String roomId) {
        return allSessionsByDayAndRoom("thursday", roomId);
    }

    @GET
    @Path("/friday")
    @ApiOperation(value = "Finds all the sessions for Friday", response = Session.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Sessions not found")}
    )
    public Response allSessionsFriday() {
        return allSessionsByDay("friday");
    }

    @GET
    @Path("/friday/{roomId}")
    @ApiOperation(value = "Finds all the sessions for Friday on a specific room", response = Session.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Sessions not found")}
    )
    public Response allSessionsFridayByRoom(@PathParam("roomId") String roomId) {
        return allSessionsByDayAndRoom("friday", roomId);
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Deletes a session")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid session value")}
    )
    public Response remove(@PathParam("id") String id) {
        sessionRepository.delete(id);
        return Response.noContent().build();
    }

    // ======================================
    // =           Private methods          =
    // ======================================

    private Response allSessionsByDay(String day) {
        List<Session> allSessions = sessionRepository.findAllSessionsByDay(day);
        for (Session session : allSessions) {
            session.addSelfLink(getURIForSelf(session));
            session.addCollectionLink(getURIForCollection());
            session.addLink(day, getUriBuilderForRoot().path(day).build());
        }

        Sessions sessions = new Sessions(allSessions);
        sessions.addSelfLink(getUriBuilderForRoot().path(day).build());
        return Response.ok(buildEntities(sessions)).build();
    }

    private Response allSessionsByDayAndRoom(String day, String roomId) {
        List<Session> allSessions = sessionRepository.findAllSessionsByDayAndRoom(day, roomId);
        for (Session session : allSessions) {
            session.addSelfLink(getURIForSelf(session));
            session.addCollectionLink(getURIForCollection());
            session.addLink(day, getUriBuilderForRoot().path(day).path(roomId).build());
        }

        Sessions sessions = new Sessions(allSessions);
        sessions.addSelfLink(getUriBuilderForRoot().path(day).path(roomId).build());
        return Response.ok(buildEntities(sessions)).build();
    }

    private GenericEntity<Sessions> buildEntities(final Sessions talks) {
        return new GenericEntity<Sessions>(talks) {
        };
    }
}
