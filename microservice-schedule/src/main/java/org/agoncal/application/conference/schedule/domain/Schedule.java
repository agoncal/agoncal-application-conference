package org.agoncal.application.conference.schedule.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Entity
@NamedQueries({
    @NamedQuery(name = Schedule.FIND_ALL, query = "SELECT s FROM Schedule s ORDER BY s.id DESC"),
    @NamedQuery(name = Schedule.FIND_BY_DAY, query = "SELECT s FROM Schedule s WHERE s.day = :day ORDER BY s.id DESC"),
    @NamedQuery(name = Schedule.FIND_BY_DAY_AND_ROOM, query = "SELECT s FROM Schedule s WHERE s.day = :day AND s.room.id = :roomId ORDER BY s.id DESC")
})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Schedule {

    // ======================================
    // =             Constants              =
    // ======================================

    public static final String FIND_ALL = "Schedule.findAll";
    public static final String FIND_BY_DAY = "Schedule.findByDay";
    public static final String FIND_BY_DAY_AND_ROOM = "Schedule.findByDayAndRoom";

    // ======================================
    // =             Attributes             =
    // ======================================

    @Id
    private String id;
    @Transient
    private Map<String, URI> links;
    private Boolean notAllocated;
    private Boolean isaBreak;
    private Long fromTimeMillis;
    private String fromTime;
    private Long toTimeMillis;
    private String toTime;
    private String day;
    @ManyToOne
    private Room room;
    @ManyToOne
    private Talk talk;

    // ======================================
    // =            Constructors            =
    // ======================================

    public Schedule() {
    }

    public Schedule(String id, String day) {
        this.id = id;
        this.day = day;
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
        return "Schedule{" +
            "id='" + id + '\'' +
            ", links=" + links +
            ", notAllocated=" + notAllocated +
            ", isaBreak=" + isaBreak +
            ", fromTimeMillis=" + fromTimeMillis +
            ", fromTime='" + fromTime + '\'' +
            ", toTimeMillis=" + toTimeMillis +
            ", toTime='" + toTime + '\'' +
            ", day='" + day + '\'' +
            ", room=" + room +
            ", talk=" + talk +
            '}';
    }
}
