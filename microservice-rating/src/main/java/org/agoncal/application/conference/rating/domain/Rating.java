package org.agoncal.application.conference.rating.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Entity
@NamedQueries({
    @NamedQuery(name = Rating.FIND_ALL, query = "SELECT r FROM Rating r ORDER BY r.rating DESC"),
    @NamedQuery(name = Rating.FIND_BY_SESSION, query = "SELECT r FROM Rating r WHERE r.sessionId = :sessionId ORDER BY r.rating DESC"),
    @NamedQuery(name = Rating.FIND_BY_ATTENDEE, query = "SELECT r FROM Rating r WHERE r.attendeeId = :attendeeId ORDER BY r.rating DESC")
})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Rating {

    // ======================================
    // =             Constants              =
    // ======================================

    public static final String FIND_ALL = "Rating.findAll";
    public static final String FIND_BY_SESSION = "Rating.findBySession";
    public static final String FIND_BY_ATTENDEE = "Rating.findByAttendee";

    // ======================================
    // =             Attributes             =
    // ======================================

    @Id
    private String id;
    @Transient
    private Map<String, URI> links;
    private String talkId;
    private String attendeeId;
    private Integer rating;

    // ======================================
    // =            Constructors            =
    // ======================================

    public Rating() {
    }

    public Rating(String talkId, String attendeeId, Integer rating) {
        this.talkId = talkId;
        this.attendeeId = attendeeId;
        this.rating = rating;
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

    public String getTalkId() {
        return talkId;
    }

    public void setTalkId(String talkId) {
        this.talkId = talkId;
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void addLink(String rel, URI uri) {
        links = new HashMap<>();
        links.put(rel, uri);
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
            ", links=" + links +
            ", talkId='" + talkId + '\'' +
            ", attendeeId='" + attendeeId + '\'' +
            ", rating=" + rating +
            '}';
    }
}
