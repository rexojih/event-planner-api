package com.ojih.rex.eventplanner.model.event;

import com.ojih.rex.eventplanner.model.Location;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class EventDTO implements Serializable {

    private Long eventId;
    private String title;
    private Date date;
    private Location location;
    private String description;
    private String category;
    private Long hostId;
    private List<Long> attendeeIds;

    public EventDTO(Long eventId, String title, Date date, Location location, String description, String category, Long hostId, List<Long> attendeeIds) {
        this.eventId = eventId;
        this.title = title;
        this.date = date;
        this.location = location;
        this.description = description;
        this.category = category;
        this.hostId = hostId;
        this.attendeeIds = attendeeIds;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    public List<Long> getAttendeeIds() {
        return attendeeIds;
    }

    public void setAttendeeIds(List<Long> attendeeIds) {
        this.attendeeIds = attendeeIds;
    }
}
