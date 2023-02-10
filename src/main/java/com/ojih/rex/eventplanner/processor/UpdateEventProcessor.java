package com.ojih.rex.eventplanner.processor;

import com.ojih.rex.eventplanner.model.Event;
import com.ojih.rex.eventplanner.model.dto.EventDTO;
import com.ojih.rex.eventplanner.model.request.EventPlannerRequest;
import com.ojih.rex.eventplanner.model.response.EventPlannerMessage;
import com.ojih.rex.eventplanner.model.response.EventPlannerResponse;
import com.ojih.rex.eventplanner.service.EventService;
import com.ojih.rex.eventplanner.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;

import static com.ojih.rex.eventplanner.constant.EventPlannerConstants.UPDATE_EVENT;
import static com.ojih.rex.eventplanner.util.DateStringConverter.getDateFomDateString;

@Component
@RequiredArgsConstructor
public class UpdateEventProcessor implements RequestProcessor {

    private final EventService eventService;
    private final Mapper<EventDTO, Event> eventMapper;

    @Override
    public EventPlannerResponse process(EventPlannerRequest request) throws ParseException {
        Event updateEvent = Event.builder()
                .title(request.getTitle())
                .date(getDateFomDateString(request.getDate()))
                .location(request.getLocation())
                .description(request.getDescription())
                .category(request.getCategory())
                .maxAttendees(request.getMaxAttendees())
                .build();
        Event updatedEvent = eventService.updateEvent(request.getEventId(), updateEvent);
        return EventPlannerResponse.builder()
                .eventPlannerMessage(new EventPlannerMessage(eventMapper.toDTO(updatedEvent)))
                .build();
    }

    @Override
    public String processorType() {
        return UPDATE_EVENT;
    }
}
