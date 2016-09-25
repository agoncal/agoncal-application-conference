package org.agoncal.application.conference.commons.registry;

import javax.ws.rs.core.UriBuilder;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
public class Registry {

    public static UriBuilder getURIAttendee() {
        return UriBuilder.fromUri("http://localhost:8081/api/attendees");
    }

    public static UriBuilder getURIRating() {
        return UriBuilder.fromUri("http://localhost:8082/api/ratings");
    }

    public static UriBuilder getURISession() {
        return UriBuilder.fromUri("http://localhost:8083/api/sessions");
    }

    public static UriBuilder getURISpeaker() {
        return UriBuilder.fromUri("http://localhost:8084/api/speakers");
    }

    public static UriBuilder getURITalk() {
        return UriBuilder.fromUri("http://localhost:8084/api/talks");
    }

    public static UriBuilder getURIVenue() {
        return UriBuilder.fromUri("http://localhost:8085/api/rooms");
    }
}
