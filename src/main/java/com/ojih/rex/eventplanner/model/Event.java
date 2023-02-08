package com.ojih.rex.eventplanner.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Event")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private Long hostId;
    private Integer maxAttendees;
    @ManyToMany(
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY
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

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
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
        if (attendees == null) {
            attendees = new ArrayList<>(Collections.singletonList(user));
        } else if (attendees.size() < maxAttendees) {
            ArrayList<User> updatedAttendees = new ArrayList<>(attendees);
            updatedAttendees.add(user);
            this.setAttendees(updatedAttendees);
        }
    }

    public void addAttendees(List<User> newAttendees) {
        if (attendees == null) {
            attendees = new ArrayList<>(newAttendees);
        } else if (attendees.size() + newAttendees.size() <= maxAttendees) {
            List<User> updatedAttendees = new ArrayList<>(attendees);
            updatedAttendees.addAll(newAttendees);
            this.setAttendees(updatedAttendees);
        }
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
