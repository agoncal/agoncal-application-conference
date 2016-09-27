package org.agoncal.application.conference.commons.registry;

import javax.enterprise.inject.Produces;
import javax.ws.rs.core.UriBuilder;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
public class SinglePortRegistry implements Registry {

    @Produces @Attendee
    public UriBuilder getURIAttendee() {
        return UriBuilder.fromUri("http://localhost:8080/api/attendees");
    }

    @Produces @Rating
    public UriBuilder getURIRating() {
        return UriBuilder.fromUri("http://localhost:8080/api/ratings");
    }

    @Produces @Session
    public UriBuilder getURISession() {
        return UriBuilder.fromUri("http://localhost:8080/api/sessions");
    }

    @Produces @Speaker
    public UriBuilder getURISpeaker() {
        return UriBuilder.fromUri("http://localhost:8080/api/speakers");
    }

    @Produces @Talk
    public UriBuilder getURITalk() {
        return UriBuilder.fromUri("http://localhost:8080/conference-talk/api/talks");
    }

    @Produces @Attendee
    public UriBuilder getURIVenue() {
        return UriBuilder.fromUri("http://localhost:8080/conference-venue/api/rooms");
    }
}
