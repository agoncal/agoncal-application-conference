package org.agoncal.application.conference.speaker.rest;

import io.swagger.annotations.Api;
import org.agoncal.application.conference.speaker.domain.AcceptedTalk;
import org.agoncal.application.conference.speaker.domain.Speaker;
import org.agoncal.application.conference.speaker.repository.SpeakerRepository;

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
@Path("/speakers")
@Api(description = "Rooms REST Endpoint")
@RequestScoped
@Produces("application/json")
@Consumes("application/json")
public class SpeakerEndpoint {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @Inject
    private SpeakerRepository speakerRepository;

    @Context
    UriInfo uriInfo;

    // ======================================
    // =          Business methods          =
    // ======================================

    @POST
    public Response add(Speaker speaker) {
        Speaker created = speakerRepository.create(speaker);
        // Speaker2SpeakerResource
        return Response.created(URI.create("/" + created.getId()))
            .entity(created)
            .build();
    }

    @GET
    @Path("/{id}")
    public Response retrieve(@PathParam("id") String id, @DefaultValue("false") @QueryParam("expand") boolean expand) {

        Speaker speaker = speakerRepository.findById(id);

        if (speaker != null) {
            speaker.addLink("self", uriInfo.getAbsolutePath().resolve(speaker.getId()));
            if (expand) {
                for (AcceptedTalk acceptedTalk : speaker.getAcceptedTalks()) {
                    acceptedTalk.addLink("self", uriInfo.getAbsolutePath().resolve(acceptedTalk.getId()));
                }
            } else {
                speaker.setBio(null);
                speaker.setAcceptedTalks(null);
            }
            return Response.ok(speaker).build();
        } else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    public Response allSpeakers() {
        List<Speaker> allSpeakers = speakerRepository.getAllSpeakers();
        for (Speaker speaker : allSpeakers) {
            speaker.addLink("self", uriInfo.getAbsolutePath().resolve(speaker.getId()));
        }
        GenericEntity<List<Speaker>> entity = buildEntity(allSpeakers);
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("/{id}")
    public Response remove(@PathParam("id") String id) {
        speakerRepository.delete(id);
        return Response.noContent().build();
    }

    // ======================================
    // =           Private methods          =
    // ======================================

    private GenericEntity<List<Speaker>> buildEntity(final List<Speaker> speakerList) {
        return new GenericEntity<List<Speaker>>(speakerList) {
        };
    }
}
