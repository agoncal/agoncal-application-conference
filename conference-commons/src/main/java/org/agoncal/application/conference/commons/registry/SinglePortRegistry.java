package org.agoncal.application.conference.commons.registry;

import javax.enterprise.inject.Produces;
import javax.ws.rs.core.UriBuilder;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
public class SinglePortRegistry implements Registry {

    @Produces @AttendeeMicroService
    public UriBuilder getURIAttendee() {
        return UriBuilder.fromUri("http://localhost:8080/api/attendees");
    }

    @Produces @RatingMicroService
    public UriBuilder getURIRating() {
        return UriBuilder.fromUri("http://localhost:8080/api/ratings");
    }

    @Produces @SessionMicroService
    public UriBuilder getURISession() {
        return UriBuilder.fromUri("http://localhost:8080/api/sessions");
    }

    @Produces @SpeakerMicroService
    public UriBuilder getURISpeaker() {
        return UriBuilder.fromUri("http://localhost:8080/api/speakers");
    }

    @Produces @TalkMicroService
    public UriBuilder getURITalk() {
        return UriBuilder.fromUri("http://localhost:8080/conference-talk/api/talks");
    }

    @Produces @VenueMicroService
    public UriBuilder getURIVenue() {
        return UriBuilder.fromUri("http://localhost:8080/conference-venue/api/rooms");
    }
}
