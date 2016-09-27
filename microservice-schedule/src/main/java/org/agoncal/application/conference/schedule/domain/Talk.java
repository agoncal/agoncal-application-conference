package org.agoncal.application.conference.schedule.domain;

import org.agoncal.application.conference.commons.domain.LinkableResource;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.*;

import static javax.persistence.CascadeType.PERSIST;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Entity
@Table(name = "sc_talk")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Talk extends LinkableResource {

    // ======================================
    // =             Attributes             =
    // ======================================

    @Id
    private String id;
    private String title;
    private String talkType;
    private String track;
    @ManyToMany(fetch = FetchType.EAGER, cascade = PERSIST)
    private List<Speaker> speakers;

    // ======================================
    // =            Constructors            =
    // ======================================

    public Talk() {
    }

    public Talk(String id, String title, String talkType, String track, Speaker... speakers) {
        this.id = id;
        this.title = title;
        this.talkType = talkType;
        this.track = track;
        this.speakers = Arrays.asList(speakers);
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
        Talk room = (Talk) o;
        return Objects.equals(id, room.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Talk{" +
            "id='" + id + '\'' +
            ", title='" + title + '\'' +
            ", talkType='" + talkType + '\'' +
            ", track='" + track + '\'' +
            '}';
    }
}
