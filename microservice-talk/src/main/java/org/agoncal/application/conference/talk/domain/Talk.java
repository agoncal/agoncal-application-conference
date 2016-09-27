package org.agoncal.application.conference.talk.domain;

import org.agoncal.application.conference.commons.domain.LinkableResource;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Entity
@Table(name = "t_talk")
@NamedQueries({
    @NamedQuery(name = Talk.FIND_ALL, query = "SELECT t FROM Talk t ORDER BY t.title DESC"),
    @NamedQuery(name = Talk.COUNT_ALL, query = "SELECT COUNT(t) FROM Talk t")
})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Talk extends LinkableResource {

    // ======================================
    // =             Constants              =
    // ======================================

    public static final String FIND_ALL = "Talk.findAll";
    public static final String COUNT_ALL = "Talk.countAll";

    // ======================================
    // =             Attributes             =
    // ======================================

    @Id
    private String id;
    private String title;
    private String language;
    private String talkType;
    private String track;
    @Column(length = 5000)
    private String summary;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Speaker> speakers;

    // ======================================
    // =            Constructors            =
    // ======================================

    public Talk() {
    }

    public Talk(String id, String title, String language) {
        this.id = id;
        this.title = title;
        this.language = language;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTalkType() {
        return talkType;
    }

    public void setTalkType(String talkType) {
        this.talkType = talkType;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Speaker> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(List<Speaker> speakers) {
        this.speakers = speakers;
    }

    // ======================================
    // =   Methods hash, equals, toString   =
    // ======================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Talk that = (Talk) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Talk{" +
            "id='" + id + '\'' +
            ", links=" + getLinks() +
            ", title='" + title + '\'' +
            ", language='" + language + '\'' +
            ", talkType='" + talkType + '\'' +
            ", track='" + track + '\'' +
            ", summary='" + summary + '\'' +
            ", speakers=" + speakers +
            '}';
    }
}
