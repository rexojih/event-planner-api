package com.ojih.rex.eventplanner.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Event")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @SequenceGenerator(
            name = "event_sequence",
            sequenceName = "event_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "event_sequence"
    )
    private Long eventId;
    private String title;
    private Date date;
    private String location;
    private String description;
    private String category;
    @OneToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id"
    )
    private User host;
    private Integer maxAttendees;
    @ManyToMany(
            cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "user_event_map",
            joinColumns = @JoinColumn(
                    name = "event_id",
                    referencedColumnName = "eventId"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "userId"
            )
    )
    private List<User> attendees;

    public Event(String title, Date date, String location, String category, User host) {
        this.title = title;
        this.date = date;
        this.location = location;
        this.category = category;
        this.host = host;
    }

    public Event(String title, Date date, String location, String description, String category, User host) {
        this.title = title;
        this.date = date;
        this.location = location;
        this.description = description;
        this.category = category;
        this.host = host;
    }

    public Event(String title, Date date, String location, String category, User host, Integer maxAttendees) {
        this.title = title;
        this.date = date;
        this.location = location;
        this.category = category;
        this.host = host;
        this.maxAttendees = maxAttendees;
    }



    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public Integer getMaxAttendees() {
        return maxAttendees;
    }

    public void setMaxAttendees(Integer maxAttendees) {
        this.maxAttendees = maxAttendees;
    }

    public List<User> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<User> attendees) {
        this.attendees = attendees;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
