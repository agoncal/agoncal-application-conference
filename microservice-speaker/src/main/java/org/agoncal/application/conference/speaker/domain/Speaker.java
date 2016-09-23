package org.agoncal.application.conference.speaker.domain;

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
@NamedQuery(name = Speaker.FIND_ALL, query = "SELECT s FROM Speaker s ORDER BY s.lastName DESC")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Speaker extends LinkableResource {

    // ======================================
    // =             Constants              =
    // ======================================

    public static final String FIND_ALL = "Speaker.findAll";

    // ======================================
    // =             Attributes             =
    // ======================================

    @Id
    private String id;
    private String lastName;
    private String firstName;
    @Column(length = 5000)
    private String bio;
    @Column(length = 3)
    private String language;
    private String twitter;
    private String avatarUrl;
    private String company;
    private String blog;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<AcceptedTalk> acceptedTalks;

    // ======================================
    // =            Constructors            =
    // ======================================

    public Speaker() {
    }

    public Speaker(String id, String lastName) {
        this.id = id;
        this.lastName = lastName;
    }

    public Speaker(String id, String lastName, String firstName, String bio, String language, String twitter, String avatarUrl, String company, String blog) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.bio = bio;
        this.language = language;
        this.twitter = twitter;
        this.avatarUrl = avatarUrl;
        this.company = company;
        this.blog = blog;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public List<AcceptedTalk> getAcceptedTalks() {
        return acceptedTalks;
    }

    public void setAcceptedTalks(List<AcceptedTalk> acceptedTalks) {
        this.acceptedTalks = acceptedTalks;
    }

    // ======================================
    // =   Methods hash, equals, toString   =
    // ======================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Speaker room = (Speaker) o;
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
            ", bio='" + bio + '\'' +
            ", language='" + language + '\'' +
            ", twitter='" + twitter + '\'' +
            ", avatarUrl='" + avatarUrl + '\'' +
            ", company='" + company + '\'' +
            ", blog='" + blog + '\'' +
            ", acceptedTalks='" + acceptedTalks + '\'' +
            '}';
    }
}
