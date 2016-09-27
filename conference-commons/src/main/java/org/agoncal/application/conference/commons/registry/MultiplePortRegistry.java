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
public class MultiplePortRegistry implements Registry {

    @Produces @Attendee
    public UriBuilder getURIAttendee() {
        return UriBuilder.fromUri("http://localhost:8081/api/attendees");
    }

    @Produces @Rating
    public UriBuilder getURIRating() {
        return UriBuilder.fromUri("http://localhost:8082/api/ratings");
    }

    @Produces @Session
    public UriBuilder getURISession() {
        return UriBuilder.fromUri("http://localhost:8083/api/sessions");
    }

    @Produces @Speaker
    public UriBuilder getURISpeaker() {
        return UriBuilder.fromUri("http://localhost:8084/api/speakers");
    }

    @Produces @Talk
    public UriBuilder getURITalk() {
        return UriBuilder.fromUri("http://localhost:8084/api/talks");
    }

    @Produces @Attendee
    public UriBuilder getURIVenue() {
        return UriBuilder.fromUri("http://localhost:8085/api/rooms");
    }
}
