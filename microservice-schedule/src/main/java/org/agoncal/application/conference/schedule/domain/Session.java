package org.agoncal.application.conference.schedule.domain;

import org.agoncal.application.conference.commons.domain.LinkableResource;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;
import java.util.UUID;

import static javax.persistence.CascadeType.PERSIST;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Entity
@Table(name = "sc_session")
@NamedQueries({
    @NamedQuery(name = Session.FIND_ALL, query = "SELECT s FROM Session s ORDER BY s.id DESC"),
    @NamedQuery(name = Session.FIND_BY_DAY, query = "SELECT s FROM Session s WHERE s.day = :day ORDER BY s.id DESC"),
    @NamedQuery(name = Session.FIND_BY_DAY_AND_ROOM, query = "SELECT s FROM Session s WHERE s.day = :day AND s.room.id = :roomId ORDER BY s.id DESC"),
    @NamedQuery(name = Session.COUNT_ALL, query = "SELECT COUNT(s) FROM Session s")
})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Session extends LinkableResource {

    // ======================================
    // =             Constants              =
    // ======================================

    public static final String FIND_ALL = "Session.findAll";
    public static final String COUNT_ALL = "Session.countAll";
    public static final String FIND_BY_DAY = "Session.findByDay";
    public static final String FIND_BY_DAY_AND_ROOM = "Session.findByDayAndRoom";

    // ======================================
    // =             Attributes             =
    // ======================================

    @Id
    private String id;
    private Boolean notAllocated;
    private Boolean isaBreak;
    private Long fromTimeMillis;
    private String fromTime;
    private Long toTimeMillis;
    private String toTime;
    private String day;
    @ManyToOne(optional = false, cascade = PERSIST)
    private Room room;
    @ManyToOne(cascade = PERSIST)
    @XmlElement(nillable = true)
    private Talk talk;

    // ======================================
    // =            Constructors            =
    // ======================================

    public Session() {
    }

    public Session(Boolean notAllocated, Boolean isaBreak, Long fromTimeMillis, String fromTime, Long toTimeMillis, String toTime, String day, Room room, Talk talk) {
        this.notAllocated = notAllocated;
        this.isaBreak = isaBreak;
        this.fromTimeMillis = fromTimeMillis;
        this.fromTime = fromTime;
        this.toTimeMillis = toTimeMillis;
        this.toTime = toTime;
        this.day = day;
        this.room = room;
        this.talk = talk;
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

    public Boolean getNotAllocated() {
        return notAllocated;
    }

    public void setNotAllocated(Boolean notAllocated) {
        this.notAllocated = notAllocated;
    }

    public Boolean getIsaBreak() {
        return isaBreak;
    }

    public void setIsaBreak(Boolean isaBreak) {
        this.isaBreak = isaBreak;
    }

    public Long getFromTimeMillis() {
        return fromTimeMillis;
    }

    public void setFromTimeMillis(Long fromTimeMillis) {
        this.fromTimeMillis = fromTimeMillis;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public Long getToTimeMillis() {
        return toTimeMillis;
    }

    public void setToTimeMillis(Long toTimeMillis) {
        this.toTimeMillis = toTimeMillis;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Talk getTalk() {
        return talk;
    }

    public void setTalk(Talk talk) {
        this.talk = talk;
    }

    // ======================================
    // =   Methods hash, equals, toString   =
    // ======================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session that = (Session) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Session{" +
            "id='" + id + '\'' +
            ", links=" + getLinks() +
            ", notAllocated=" + notAllocated +
            ", isaBreak=" + isaBreak +
            ", fromTimeMillis=" + fromTimeMillis +
            ", fromTime='" + fromTime + '\'' +
            ", toTimeMillis=" + toTimeMillis +
            ", toTime='" + toTime + '\'' +
            ", day='" + day + '\'' +
            ", room=" + room +
            '}';
    }
}
