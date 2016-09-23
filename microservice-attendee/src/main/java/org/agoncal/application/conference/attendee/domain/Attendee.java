package org.agoncal.application.conference.attendee.domain;

import org.agoncal.application.conference.commons.domain.LinkableResource;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
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
@NamedQuery(name = Attendee.FIND_ALL, query = "SELECT a FROM Attendee a ORDER BY a.lastName DESC")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Attendee extends LinkableResource {

    // ======================================
    // =             Constants              =
    // ======================================

    public static final String FIND_ALL = "Attendee.findAll";

    // ======================================
    // =             Attributes             =
    // ======================================

    @Id
    private String id;
    private String lastName;
    private String firstName;
    private String twitter;
    private String avatarUrl;
    private String company;

    // ======================================
    // =            Constructors            =
    // ======================================

    public Attendee() {
    }

    public Attendee(String id, String lastName) {
        this.id = id;
        this.lastName = lastName;
    }

    public Attendee(String id, String lastName, String firstName, String twitter, String avatarUrl, String company) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.twitter = twitter;
        this.avatarUrl = avatarUrl;
        this.company = company;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    // ======================================
    // =   Methods hash, equals, toString   =
    // ======================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attendee room = (Attendee) o;
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
            ", links='" + getLinks() + '\'' +
            ", lastName='" + lastName + '\'' +
            ", firstName='" + firstName + '\'' +
            ", twitter='" + twitter + '\'' +
            ", avatarUrl='" + avatarUrl + '\'' +
            ", company='" + company + '\'' +
            '}';
    }
}
