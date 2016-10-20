package org.agoncal.application.conference.commons.registry;

import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.ws.rs.core.UriBuilder;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Alternative
public class TraefikRegistry implements Registry {

    @Produces @AttendeeMicroService
    public UriBuilder getURIAttendee() {
        return UriBuilder.fromUri("http://conference.docker.localhost:90/conference-attendee/api/attendees");
    }

    @Produces @RatingMicroService
    public UriBuilder getURIRating() {
        return UriBuilder.fromUri("http://conference.docker.localhost:90/conference-rating/api/ratings");
    }

    @Produces @SessionMicroService
    public UriBuilder getURISession() {
        return UriBuilder.fromUri("http://conference.docker.localhost:90/conference-schedule/api/sessions");
    }

    @Produces @SpeakerMicroService
    public UriBuilder getURISpeaker() {
        return UriBuilder.fromUri("http://conference.docker.localhost:90/conference-speaker/api/speakers");
    }

    @Produces @TalkMicroService
    public UriBuilder getURITalk() {
        return UriBuilder.fromUri("http://conference.docker.localhost:90/conference-talk/api/talks");
    }

    @Produces @VenueMicroService
    public UriBuilder getURIVenue() {
        return UriBuilder.fromUri("http://conference.docker.localhost:90/conference-venue/api/rooms");
    }
}
