package org.agoncal.application.conference.schedule.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.agoncal.application.conference.commons.rest.LinkableEndpoint;
import org.agoncal.application.conference.schedule.domain.Schedule;
import org.agoncal.application.conference.schedule.repository.ScheduleRepository;

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
@Path("/schedules")
@Api(description = "Schedule REST Endpoint")
@RequestScoped
@Produces("application/json")
@Consumes("application/json")
public class ScheduleEndpoint extends LinkableEndpoint<Schedule> {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @Inject
    private ScheduleRepository scheduleRepository;

    // ======================================
    // =            Constructors            =
    // ======================================

    public ScheduleEndpoint() {
        super(ScheduleEndpoint.class);
    }

    // ======================================
    // =          Business methods          =
    // ======================================

    @POST
    @ApiOperation(value = "Adds a new schedule to the conference")
    @ApiResponses(value = {
        @ApiResponse(code = 405, message = "Invalid input")}
    )
    public Response add(Schedule schedule) {
        Schedule created = scheduleRepository.create(schedule);
        return Response.created(getURIForSelf(schedule)).entity(created).build();
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Finds a schedule by ID", response = Schedule.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid input"),
        @ApiResponse(code = 404, message = "Schedule not found")}
    )
    public Response retrieve(@PathParam("id") String id, @Context Request request) {

        Schedule schedule = scheduleRepository.findById(id);

        if (schedule == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        EntityTag etag = new EntityTag(Integer.toString(schedule.hashCode()));
        Response.ResponseBuilder preconditions = request.evaluatePreconditions(etag);

        // cached resource did change -> serve updated content
        if (preconditions == null) {
            schedule.addSelfLink(getURIForSelf(schedule));
            schedule.addCollectionLink(getURIForCollection());
            preconditions = Response.ok(schedule).tag(etag);
        }

        return preconditions.build();
    }

    @GET
    @ApiOperation(value = "Finds all the schedules", response = Schedule.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Schedules not found")}
    )
    public Response allSchedules() {
        List<Schedule> allSchedules = scheduleRepository.findAllSchedules();

        if (allSchedules == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        for (Schedule schedule : allSchedules) {
            schedule.addSelfLink(getURIForSelf(schedule));
        }
        return Response.ok(buildEntity(allSchedules)).build();
    }

    @GET
    @Path("/monday")
    @ApiOperation(value = "Finds all the schedules for Monday", response = Schedule.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Schedules not found")}
    )
    public Response allSchedulesMonday() {
        return allSchedulesByDay("monday");
    }

    @GET
    @Path("/monday/{roomId}")
    @ApiOperation(value = "Finds all the schedules for Monday on a specific room", response = Schedule.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Schedules not found")}
    )
    public Response allSchedulesMondayByRoom(@PathParam("roomId") String roomId) {
        return allSchedulesByDayAndRoom("monday", roomId);
    }

    @GET
    @Path("/tuesday")
    @ApiOperation(value = "Finds all the schedules for Tuesday", response = Schedule.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Schedules not found")}
    )
    public Response allSchedulesTuesday() {
        return allSchedulesByDay("tuesday");
    }

    @GET
    @Path("/tuesday/{roomId}")
    @ApiOperation(value = "Finds all the schedules for Tuesday on a specific room", response = Schedule.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Schedules not found")}
    )
    public Response allSchedulesTuesdayByRoom(@PathParam("roomId") String roomId) {
        return allSchedulesByDayAndRoom("tuesday", roomId);
    }

    @GET
    @Path("/wednesday")
    @ApiOperation(value = "Finds all the schedules for Wednesday", response = Schedule.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Schedules not found")}
    )
    public Response allSchedulesWednesday() {
        return allSchedulesByDay("wednesday");
    }

    @GET
    @Path("/wednesday/{roomId}")
    @ApiOperation(value = "Finds all the schedules for Wednesday on a specific room", response = Schedule.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Schedules not found")}
    )
    public Response allSchedulesWednesdayByRoom(@PathParam("roomId") String roomId) {
        return allSchedulesByDayAndRoom("wednesday", roomId);
    }

    @GET
    @Path("/thursday")
    @ApiOperation(value = "Finds all the schedules for Thursday", response = Schedule.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Schedules not found")}
    )
    public Response allSchedulesThursday() {
        return allSchedulesByDay("thursday");
    }

    @GET
    @Path("/thursday/{roomId}")
    @ApiOperation(value = "Finds all the schedules for Thursday on a specific room", response = Schedule.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Schedules not found")}
    )
    public Response allSchedulesThursdayByRoom(@PathParam("roomId") String roomId) {
        return allSchedulesByDayAndRoom("thursday", roomId);
    }

    @GET
    @Path("/friday")
    @ApiOperation(value = "Finds all the schedules for Friday", response = Schedule.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Schedules not found")}
    )
    public Response allSchedulesFriday() {
        return allSchedulesByDay("friday");
    }

    @GET
    @Path("/friday/{roomId}")
    @ApiOperation(value = "Finds all the schedules for Friday on a specific room", response = Schedule.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Schedules not found")}
    )
    public Response allSchedulesFridayByRoom(@PathParam("roomId") String roomId) {
        return allSchedulesByDayAndRoom("friday", roomId);
    }

    private Response allSchedulesByDay(String day) {
        List<Schedule> allSchedules = scheduleRepository.findAllSchedulesByDay(day);
        for (Schedule schedule : allSchedules) {
            schedule.addSelfLink(getURIForSelf(schedule));
        }
        return Response.ok(buildEntity(allSchedules)).build();
    }

    private Response allSchedulesByDayAndRoom(String day, String roomId) {
        List<Schedule> allSchedules = scheduleRepository.findAllSchedulesByDayAndRoom(day, roomId);
        for (Schedule schedule : allSchedules) {
            schedule.addSelfLink(getURIForSelf(schedule));
        }
        return Response.ok(buildEntity(allSchedules)).build();
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Deletes a schedule")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid schedule value")}
    )
    public Response remove(@PathParam("id") String id) {
        scheduleRepository.delete(id);
        return Response.noContent().build();
    }
}
