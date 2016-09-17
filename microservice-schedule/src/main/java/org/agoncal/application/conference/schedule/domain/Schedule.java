package org.agoncal.application.conference.schedule.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.*;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Entity
@NamedQuery(name = Schedule.FIND_ALL, query = "SELECT s FROM Schedule s ORDER BY s.title DESC")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Schedule {

    // ======================================
    // =             Constants              =
    // ======================================

    public static final String FIND_ALL = "Talk.findAll";

    // ======================================
    // =             Attributes             =
    // ======================================

    @Id
    private String id;
    @Transient
    private Map<String, URI> links;
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

    public Schedule() {
    }

    public Schedule(String id, String title, String language) {
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

    public void addLink(String title, URI uri) {
        links = new HashMap<>();
        links.put(title, uri);
    }

    // ======================================
    // =   Methods hash, equals, toString   =
    // ======================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule that = (Schedule) o;
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
            ", links=" + links +
            ", title='" + title + '\'' +
            ", language='" + language + '\'' +
            ", talkType='" + talkType + '\'' +
            ", track='" + track + '\'' +
            ", summary='" + summary + '\'' +
            ", speakers=" + speakers +
            '}';
    }
}
