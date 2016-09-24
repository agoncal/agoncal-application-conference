package org.agoncal.application.conference.speaker.domain;

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
public class Speakers extends LinkableResources<Speaker> {

    // ======================================
    // =            Constructors            =
    // ======================================

    public Speakers(List<Speaker> speakers) {
        super(speakers);
    }
}
