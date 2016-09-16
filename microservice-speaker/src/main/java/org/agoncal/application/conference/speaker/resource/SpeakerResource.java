package org.agoncal.application.conference.speaker.resource;

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
public class SpeakerResource {

    // ======================================
    // =             Attributes             =
    // ======================================

    private String id;
    private Map<String, URI> links;
    private String email;
    private String name;
    private String firstName;
    private String bio;
    private String language;
    private String twitter;
    private String avatarUrl;
    private String company;
    private String blog;
    private String qualifications;

    // ======================================
    // =            Constructors            =
    // ======================================

    public SpeakerResource() {
    }

    public SpeakerResource(String id, String name) {
        this.id = id;
        this.name = name;
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


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
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
        SpeakerResource room = (SpeakerResource) o;
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
            ", email='" + email + '\'' +
            ", name='" + name + '\'' +
            ", firstName='" + firstName + '\'' +
            ", links=" + links +
            ", bio='" + bio + '\'' +
            ", language='" + language + '\'' +
            ", twitter='" + twitter + '\'' +
            ", avatarUrl='" + avatarUrl + '\'' +
            ", company='" + company + '\'' +
            ", blog='" + blog + '\'' +
            ", qualifications='" + qualifications + '\'' +
            '}';
    }
}
