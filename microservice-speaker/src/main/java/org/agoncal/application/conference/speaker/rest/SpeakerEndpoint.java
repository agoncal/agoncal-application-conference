package org.agoncal.application.conference.speaker.rest;

import io.swagger.annotations.*;
import org.agoncal.application.conference.commons.constraints.NotEmpty;
import org.agoncal.application.conference.commons.domain.Links;
import org.agoncal.application.conference.commons.registry.TalkMicroService;
import org.agoncal.application.conference.commons.rest.LinkableEndpoint;
import org.agoncal.application.conference.speaker.domain.AcceptedTalk;
import org.agoncal.application.conference.speaker.domain.Speaker;
import org.agoncal.application.conference.speaker.domain.Speakers;
import org.agoncal.application.conference.speaker.repository.SpeakerRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

import static org.agoncal.application.conference.commons.domain.Links.SELF;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Path("/speakers")
@Api(description = "Speakers REST Endpoint", tags = {"Speaker"})
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SpeakerEndpoint extends LinkableEndpoint<Speaker> {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @Inject
    private SpeakerRepository speakerRepository;

    @Inject @TalkMicroService
    private UriBuilder uriTalk;

    // ======================================
    // =            Constructors            =
    // ======================================

    public SpeakerEndpoint() {
        super(SpeakerEndpoint.class);
    }

    // ======================================
    // =          Business methods          =
    // ======================================

    @POST
    @ApiOperation(value = "Adds a new speaker to the conference")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "The speaker is created"),
        @ApiResponse(code = 400, message = "Invalid input")}
    )
    public Response add(@ApiParam(value = "Speaker to be created", required = true) @NotNull Speaker speaker) {
        Speaker created = speakerRepository.create(speaker);
        return Response.created(getURIForSelf(speaker)).entity(created).build();
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Finds a speaker by ID", response = Speaker.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Speaker found"),
        @ApiResponse(code = 400, message = "Invalid input"),
        @ApiResponse(code = 404, message = "Speaker not found")}
    )
    public Response retrieve(@PathParam("id") String id, @DefaultValue("true") @QueryParam("expand") boolean expand, @Context Request request) {

        Speaker speaker = speakerRepository.findById(id);

        if (speaker == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        EntityTag etag = new EntityTag(Integer.toString(speaker.hashCode()));
        Response.ResponseBuilder preconditions = request.evaluatePreconditions(etag);

        // cached resource did change -> serve updated content
        if (preconditions == null) {
            speaker.addSelfLink(getURIForSelf(speaker));
            speaker.addCollectionLink(getURIForCollection());
            speaker.addLink(Links.SUMMARY, getURIBuilderForSelf(speaker).queryParam("expand", false).build());
            speaker.addSwaggerLink(getURIForSwagger());

            if (expand) {
                for (AcceptedTalk acceptedTalk : speaker.getAcceptedTalks()) {
                    acceptedTalk.addLink(SELF, uriTalk.clone().path(acceptedTalk.getId()).build());
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
        @ApiResponse(code = 200, message = "All speakers found"),
        @ApiResponse(code = 400, message = "Invalid input"),
        @ApiResponse(code = 404, message = "Speakers not found")}
    )
    public Response allSpeakers(@DefaultValue("1") @QueryParam("page") @Min(1) Integer pageNumber) {
        List<Speaker> allSpeakers = speakerRepository.findAllSpeakers(pageNumber);

        if (allSpeakers == null || allSpeakers.isEmpty())
            return Response.status(Response.Status.NOT_FOUND).build();

        for (Speaker speaker : allSpeakers) {
            speaker.addSelfLink(getURIForSelf(speaker));
            speaker.addLink(Links.SUMMARY, getURIBuilderForSelf(speaker).queryParam("expand", false).build());
            speaker.setBio(null);
            speaker.setAcceptedTalks(null);
        }

        Speakers speakers = new Speakers(allSpeakers);
        Integer last = speakerRepository.getNumberOfPages();
        speakers.addSelfLink(getURIForPage(pageNumber));
        speakers.addFirst(getURIForPage(1));
        speakers.addLast(getURIForPage(last));
        speakers.addNext(getURIForPage(pageNumber < last ? pageNumber + 1 : last));
        speakers.addPrevious(getURIForPage(pageNumber == 1 ? 1 : pageNumber - 1));
        speakers.addSwaggerLink(getURIForSwagger());

        return Response.ok(buildEntities(speakers)).build();
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Deletes a speaker")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Speaker has been deleted"),
        @ApiResponse(code = 405, message = "Invalid speaker value")}
    )
    public Response remove(@PathParam("id") @NotEmpty String id) {
        speakerRepository.delete(id);
        return Response.noContent().build();
    }

    // ======================================
    // =           Private methods          =
    // ======================================

    private GenericEntity<Speakers> buildEntities(final Speakers speakers) {
        return new GenericEntity<Speakers>(speakers) {
        };
    }
}
