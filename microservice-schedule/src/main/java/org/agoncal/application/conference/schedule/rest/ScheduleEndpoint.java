package org.agoncal.application.conference.schedule.rest;

import io.swagger.annotations.Api;
import org.agoncal.application.conference.schedule.domain.Schedule;
import org.agoncal.application.conference.schedule.repository.ScheduleRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
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
public class ScheduleEndpoint {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @Inject
    private ScheduleRepository scheduleRepository;

    @Context
    private UriInfo uriInfo;

    // ======================================
    // =          Business methods          =
    // ======================================

    @POST
    public Response add(Schedule schedule) {
        Schedule created = scheduleRepository.create(schedule);
        return Response.created(URI.create("/" + created.getId())).entity(created).build();
    }

    @GET
    @Path("/{id}")
    public Response retrieve(@PathParam("id") String id) {

        Schedule talk = scheduleRepository.findById(id);

        if (talk != null) {
            talk.addLink("self", uriInfo.getAbsolutePathBuilder().path(talk.getId()).build());
            return Response.ok(talk).build();
        } else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    public Response allSchedules() {
        List<Schedule> allSchedules = scheduleRepository.getAllSchedules();
        for (Schedule schedule : allSchedules) {
            schedule.addLink("self", uriInfo.getAbsolutePathBuilder().path(schedule.getId()).build());
        }
        GenericEntity<List<Schedule>> entity = buildEntity(allSchedules);
        return Response.ok(entity).build();
    }

    @GET
    @Path("/monday")
    public Response allSchedulesMonday() {
        return allSchedulesByDay("monday");
    }

    @GET
    @Path("/monday/{roomId}")
    public Response allSchedulesMondayByRoom(@PathParam("roomId") String roomId) {
        return allSchedulesByDayAndRoom("monday", roomId);
    }

    @GET
    @Path("/tuesday")
    public Response allSchedulesTuesday() {
        return allSchedulesByDay("tuesday");
    }

    @GET
    @Path("/tuesday/{roomId}")
    public Response allSchedulesTuesdayByRoom(@PathParam("roomId") String roomId) {
        return allSchedulesByDayAndRoom("tuesday", roomId);
    }

    @GET
    @Path("/wednesday")
    public Response allSchedulesWednesday() {
        return allSchedulesByDay("wednesday");
    }

    @GET
    @Path("/wednesday/{roomId}")
    public Response allSchedulesWednesdayByRoom(@PathParam("roomId") String roomId) {
        return allSchedulesByDayAndRoom("wednesday", roomId);
    }

    @GET
    @Path("/thursday")
    public Response allSchedulesThursday() {
        return allSchedulesByDay("thursday");
    }

    @GET
    @Path("/thursday/{roomId}")
    public Response allSchedulesThursdayByRoom(@PathParam("roomId") String roomId) {
        return allSchedulesByDayAndRoom("thursday", roomId);
    }

    @GET
    @Path("/friday")
    public Response allSchedulesFriday() {
        return allSchedulesByDay("friday");
    }

    @GET
    @Path("/friday/{roomId}")
    public Response allSchedulesFridayByRoom(@PathParam("roomId") String roomId) {
        return allSchedulesByDayAndRoom("friday", roomId);
    }

    private Response allSchedulesByDay(String day) {
        List<Schedule> allSchedules = scheduleRepository.getAllSchedulesByDay(day);
        for (Schedule schedule : allSchedules) {
            schedule.addLink("self", uriInfo.getAbsolutePathBuilder().path(schedule.getId()).build());
        }
        GenericEntity<List<Schedule>> entity = buildEntity(allSchedules);
        return Response.ok(entity).build();
    }

    private Response allSchedulesByDayAndRoom(String day, String roomId) {
        List<Schedule> allSchedules = scheduleRepository.getAllSchedulesByDayAndRoom(day, roomId);
        for (Schedule schedule : allSchedules) {
            schedule.addLink("self", uriInfo.getAbsolutePathBuilder().path(schedule.getId()).build());
        }
        GenericEntity<List<Schedule>> entity = buildEntity(allSchedules);
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("/{id}")
    public Response remove(@PathParam("id") String id) {
        scheduleRepository.delete(id);
        return Response.noContent().build();
    }

    // ======================================
    // =           Private methods          =
    // ======================================

    private GenericEntity<List<Schedule>> buildEntity(final List<Schedule> scheduleList) {
        return new GenericEntity<List<Schedule>>(scheduleList) {
        };
    }
}
