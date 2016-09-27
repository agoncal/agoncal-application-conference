package org.agoncal.application.conference.schedule.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Entity
@Table(name = "sc_room")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Room {

    // ======================================
    // =             Attributes             =
    // ======================================

    @Id
    private String id;
    @Transient
    private Map<String, URI> links;

    // ======================================
    // =            Constructors            =
    // ======================================

    public Room() {
    }

    public Room(String id) {
        this.id = id;
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

    public void addLink(String rel, URI uri) {
        if (links == null)
            links = new HashMap<>();
        if (!links.containsKey(rel))
            links.put(rel, uri);
    }

    // ======================================
    // =   Methods hash, equals, toString   =
    // ======================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(id, room.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Speaker{" +
            "id='" + id + '\'' +
            '}';
    }
}
