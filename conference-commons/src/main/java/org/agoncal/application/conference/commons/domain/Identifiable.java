package org.agoncal.application.conference.commons.domain;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 *         Marks resources that are identifiable by an ID of any type.
 */
public interface Identifiable {

    String getId();

    void setId(String id);
}
