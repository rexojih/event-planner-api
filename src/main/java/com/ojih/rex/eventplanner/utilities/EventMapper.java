package com.ojih.rex.eventplanner.utilities;

import com.ojih.rex.eventplanner.model.User;
import com.ojih.rex.eventplanner.model.event.Event;
import com.ojih.rex.eventplanner.model.event.EventDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EventMapper implements Mapper<EventDTO, Event> {

    public EventDTO toDto(Event event) {
        List<Long> attendeeIds = new ArrayList<>();
        for (User attendee : event.getAttendees()) {
            attendeeIds.add(attendee.getUserId());
        }
        return new EventDTO(event.getEventId(), event.getTitle(), event.getDate(), event.getLocation(), event.getDescription(), event.getCategory(), event.getHost().getUserId(), attendeeIds);
    }

    public List<EventDTO> toDtos(List<Event> events) {
        List<EventDTO> eventDtos = new ArrayList<>();
        for (Event event : events) {
            eventDtos.add(this.toDto(event));
        }
        return eventDtos;
    }
}
