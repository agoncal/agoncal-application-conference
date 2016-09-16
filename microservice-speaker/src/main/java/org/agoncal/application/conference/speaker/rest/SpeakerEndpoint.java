package org.agoncal.application.conference.speaker.rest;

import io.swagger.annotations.Api;
import org.agoncal.application.conference.speaker.domain.Speaker;
import org.agoncal.application.conference.speaker.repository.SpeakerRepository;
import org.agoncal.application.conference.speaker.resource.SpeakerResource;

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
@Path("/rooms")
@Api(description = "Rooms REST Endpoint")
@RequestScoped
@Produces("application/json")
public class SpeakerEndpoint {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @Inject
    private SpeakerRepository roomDAO;

    @Context
    UriInfo uriInfo;

    // ======================================
    // =          Business methods          =
    // ======================================

    @POST
    @Consumes("application/json")
    public Response add(SpeakerResource room) {
        Speaker created = roomDAO.create(room);
        // Speaker2SpeakerResource
        return Response.created(URI.create("/" + created.getId()))
            .entity(created)
            .build();
    }

    @GET
    @Path("/{id}")
    public Response retrieve(@PathParam("id") String roomId) {

        Speaker room = roomDAO.findById(roomId);
        // Speaker2SpeakerResource
        SpeakerResource speakerResource = new SpeakerResource();

        if (room != null) {
            speakerResource.addLink("self", uriInfo.getAbsolutePath().resolve(room.getId()));
            return Response.ok(room).build();
        } else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    public Response allSpeakers() {
        List<Speaker> allSpeakers = roomDAO.getAllSpeakers();
        // for (SpeakerResource room : allSpeakers) {
        //     room.addLink("self", uriInfo.getAbsolutePath().resolve(room.getId()));
        // }
        // GenericEntity<List<SpeakerResource>> entity = buildEntity(allSpeakers);
        // return Response.ok(entity).build();
        return null;
    }

    @DELETE
    @Path("/{id}")
    public Response remove(@PathParam("id") String id) {
        roomDAO.delete(id);
        return Response.noContent().build();
    }

    // ======================================
    // =           Private methods          =
    // ======================================

    private GenericEntity<List<SpeakerResource>> buildEntity(final List<SpeakerResource> roomList) {
        return new GenericEntity<List<SpeakerResource>>(roomList) {
        };
    }
}
