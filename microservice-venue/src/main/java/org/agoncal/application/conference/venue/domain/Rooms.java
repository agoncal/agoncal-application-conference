package org.agoncal.application.conference.venue.domain;

import org.agoncal.application.conference.commons.domain.LinkableResources;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Rooms extends LinkableResources<Room> {

    // ======================================
    // =            Constructors            =
    // ======================================

    public Rooms(List<Room> rooms) {
        super(rooms);
    }
}
