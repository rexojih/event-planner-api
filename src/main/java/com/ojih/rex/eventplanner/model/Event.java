package com.ojih.rex.eventplanner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
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
    @Column(
            nullable = false
    )
    private String title;
    @Column(
            nullable = false
    )
    private Date date;
    @Column(
            nullable = false
    )
    private Location location;
    private String description;
    @Column(
            nullable = false
    )
    private String category;
    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "host_id",
            referencedColumnName = "userId"
    )
    private User host;
    private Integer maxAttendees;
    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "event_attendees",
            joinColumns = @JoinColumn(
                    name = "event_id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id"
            )
    )
    private List<User> attendees;

    public Event(String title, Date date, Location location, String category) {
        this.title = title;
        this.date = date;
        this.location = location;
        this.category = category;
        this.host = host;
    }

    public Event(String title, Date date, Location location, String description, String category) {
        this.title = title;
        this.date = date;
        this.location = location;
        this.description = description;
        this.category = category;
        this.host = host;
    }

    public Event(String title, Date date, Location location, String category, Integer maxAttendees) {
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
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
        this.addAttendee(host);
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

    public void addAttendee(User user) {
        if (attendees == null) attendees = new ArrayList<>();
        if (attendees.size() < maxAttendees) attendees.add(user);
    }

    public void addAttendees(List<User> attendeesList) {
        if (attendees == null) attendees = new ArrayList<>();
        if (attendees.size() + attendeesList.size() <= maxAttendees) attendees.addAll(attendeesList);
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", maxAttendees=" + maxAttendees +
                '}';
    }
}
