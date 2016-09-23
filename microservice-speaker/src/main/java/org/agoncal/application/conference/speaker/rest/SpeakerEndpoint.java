package org.agoncal.application.conference.speaker.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.agoncal.application.conference.speaker.domain.AcceptedTalk;
import org.agoncal.application.conference.speaker.domain.Speaker;
import org.agoncal.application.conference.speaker.repository.SpeakerRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Path("/speakers")
@Api(description = "Speakers REST Endpoint")
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
    private UriInfo uriInfo;

    // ======================================
    // =          Business methods          =
    // ======================================

    @POST
    @ApiOperation(value = "Adds a new speacker to the conference")
    @ApiResponses(value = {
        @ApiResponse(code = 405, message = "Invalid input")}
    )
    public Response add(Speaker speaker) {
        Speaker created = speakerRepository.create(speaker);
        return Response.created(getURIForSelf(speaker)).entity(created).build();
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Finds a room by ID", response = Speaker.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid input"),
        @ApiResponse(code = 404, message = "Speaker not found")}
    )
    public Response retrieve(@PathParam("id") String id, @DefaultValue("false") @QueryParam("expand") boolean expand, @Context Request request) {

        Speaker speaker = speakerRepository.findById(id);

        if (speaker == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        EntityTag etag = new EntityTag(Integer.toString(speaker.hashCode()));
        Response.ResponseBuilder preconditions = request.evaluatePreconditions(etag);

        // cached resource did change -> serve updated content
        if (preconditions == null) {
            speaker.addSelfLink(getURIForSelf(speaker));
            speaker.addCollectionLink(getURIForCollection());
            if (expand) {
                for (AcceptedTalk acceptedTalk : speaker.getAcceptedTalks()) {
                    acceptedTalk.addLink("self", uriInfo.getAbsolutePath().resolve(acceptedTalk.getId()));
                }
            } else {
                speaker.setBio(null);
                speaker.setAcceptedTalks(null);
            }

            preconditions = Response.ok(speaker).tag(etag);
        }

        return preconditions.build();
    }

    @GET
    @ApiOperation(value = "Finds all the speakers", response = Speaker.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Speakers not found")}
    )
    public Response allSpeakers() {
        List<Speaker> allSpeakers = speakerRepository.findAllSpeakers();

        if (allSpeakers == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        for (Speaker speaker : allSpeakers) {
            speaker.addSelfLink(getURIForSelf(speaker));
        }
        return Response.ok(buildEntity(allSpeakers)).build();
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Deletes a speaker")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid speaker value")}
    )
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

    private URI getURIForSelf(Speaker speaker) {
        return uriInfo.getBaseUriBuilder().path(SpeakerEndpoint.class).path(speaker.getId()).build();
    }

    private URI getURIForCollection() {
        return uriInfo.getBaseUriBuilder().path(SpeakerEndpoint.class).build();
    }
}
