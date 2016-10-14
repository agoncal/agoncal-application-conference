package org.agoncal.application.conference.rating.domain;

import org.agoncal.application.conference.commons.domain.LinkableResource;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Entity
@Table(name = "r_rating")
@NamedQueries({
    @NamedQuery(name = Rating.FIND_ALL, query = "SELECT r FROM Rating r ORDER BY r.mark DESC"),
    @NamedQuery(name = Rating.COUNT_ALL, query = "SELECT COUNT(r) FROM Rating r"),
    @NamedQuery(name = Rating.FIND_BY_SESSION, query = "SELECT r FROM Rating r WHERE r.sessionId = :sessionId ORDER BY r.mark DESC"),
    @NamedQuery(name = Rating.FIND_BY_ATTENDEE, query = "SELECT r FROM Rating r WHERE r.attendeeId = :attendeeId ORDER BY r.mark DESC")
})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Rating extends LinkableResource {

    // ======================================
    // =             Constants              =
    // ======================================

    public static final String FIND_ALL = "Rating.findAll";
    public static final String COUNT_ALL = "Rating.countAll";
    public static final String FIND_BY_SESSION = "Rating.findBySession";
    public static final String FIND_BY_ATTENDEE = "Rating.findByAttendee";

    // ======================================
    // =             Attributes             =
    // ======================================

    @Id
    private String id;
    private String sessionId;
    private String attendeeId;
    private Integer mark;

    // ======================================
    // =            Constructors            =
    // ======================================

    public Rating() {
    }

    public Rating(String sessionId, String attendeeId, Integer mark) {
        this.sessionId = sessionId;
        this.attendeeId = attendeeId;
        this.mark = mark;
    }

    // ======================================
    // =         Lifecycle methods          =
    // ======================================

    @PrePersist
    private void setUUID() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }

    // ======================================
    // =          Getters & Setters         =
    // ======================================

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    // ======================================
    // =   Methods hash, equals, toString   =
    // ======================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating that = (Rating) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Rating{" +
            "id='" + id + '\'' +
            ", links=" + getLinks() +
            ", sessionId='" + sessionId + '\'' +
            ", attendeeId='" + attendeeId + '\'' +
            ", rating=" + mark +
            '}';
    }
}
