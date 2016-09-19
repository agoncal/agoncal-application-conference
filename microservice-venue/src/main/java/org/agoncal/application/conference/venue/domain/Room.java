package org.agoncal.application.conference.venue.domain;

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
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Room {

    // ======================================
    // =             Attributes             =
    // ======================================

    private String id;
    private Map<String, URI> links;
    private String name;
    private Integer capacity;
    private String setup;

    // ======================================
    // =            Constructors            =
    // ======================================

    public Room() {
    }

    public Room(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Room(String id, String name, Integer capacity, String setup) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.setup = setup;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getSetup() {
        return setup;
    }

    public void setSetup(String setup) {
        this.setup = setup;
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
        Room room = (Room) o;
        return Objects.equals(id, room.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Room{" +
            "id='" + id + '\'' +
            ", links=" + links +
            ", name='" + name + '\'' +
            ", capacity=" + capacity +
            ", setup='" + setup + '\'' +
            '}';
    }
}
