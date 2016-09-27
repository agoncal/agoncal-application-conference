package org.agoncal.application.conference.commons.registry;

import javax.ws.rs.core.UriBuilder;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
public interface Registry {

    UriBuilder getURIAttendee();

    UriBuilder getURIRating();

    UriBuilder getURISession();

    UriBuilder getURISpeaker();

    UriBuilder getURITalk();

    UriBuilder getURIVenue();
}
