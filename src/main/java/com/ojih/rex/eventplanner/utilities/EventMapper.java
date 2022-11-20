package com.ojih.rex.eventplanner.utilities;

import com.ojih.rex.eventplanner.model.event.Event;
import com.ojih.rex.eventplanner.model.event.EventDTO;
import com.ojih.rex.eventplanner.model.user.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EventMapper implements Mapper<EventDTO, Event> {

    @Override
    public EventDTO toDto(Event event) {
        List<Long> attendeeIds = new ArrayList<>();

        if (event.getAttendees() != null) {
            for (User attendee : event.getAttendees()) {
                attendeeIds.add(attendee.getUserId());
            }
        }

        return EventDTO.builder()
                .eventId(event.getEventId())
                .title(event.getTitle())
                .date(event.getDate())
                .location(event.getLocation())
                .description(event.getDescription())
                .category(event.getCategory())
                .hostId(event.getHostId())
                .maxAttendees(event.getMaxAttendees())
                .attendeeIds(attendeeIds)
                .build();
    }

    @Override
    public List<EventDTO> toDtos(List<Event> events) {
        List<EventDTO> eventDtos = new ArrayList<>();
        for (Event event : events) {
            eventDtos.add(this.toDto(event));
        }
        return eventDtos;
    }
}
