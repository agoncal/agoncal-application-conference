package org.agoncal.application.conference.talk.domain;

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
public class Talks extends LinkableResources<Talk> {

    // ======================================
    // =            Constructors            =
    // ======================================

    public Talks(List<Talk> allTalks) {
        super(allTalks);
    }
}
