package org.agoncal.application.conference.commons.domain;

import java.io.Serializable;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 *         Marks objects that are identifiable by an ID of any type.
 */
public interface Identifiable<ID extends Serializable> {

    ID getId();
}
