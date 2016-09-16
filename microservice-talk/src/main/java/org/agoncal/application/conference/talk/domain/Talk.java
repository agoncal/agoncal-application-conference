package org.agoncal.application.conference.talk.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Entity
@NamedQuery(name = Talk.FIND_ALL, query = "SELECT t FROM Talk t ORDER BY t.title DESC")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Talk {

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
        Talk that = (Talk) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AcceptedTalk{" +
            "id='" + id + '\'' +
            ", title='" + title + '\'' +
            ", language='" + language + '\'' +
            '}';
    }
}
