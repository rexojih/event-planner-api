package com.ojih.rex.eventplanner.processor;

import com.ojih.rex.eventplanner.model.Event;
import com.ojih.rex.eventplanner.model.User;
import com.ojih.rex.eventplanner.model.dto.EventDTO;
import com.ojih.rex.eventplanner.model.request.EventPlannerRequest;
import com.ojih.rex.eventplanner.model.response.EventPlannerMessage;
import com.ojih.rex.eventplanner.model.response.EventPlannerResponse;
import com.ojih.rex.eventplanner.service.EventService;
import com.ojih.rex.eventplanner.service.UserService;
import com.ojih.rex.eventplanner.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;

import static com.ojih.rex.eventplanner.constant.EventPlannerConstants.ADD_EVENT_ATTENDEE;

@Component
@RequiredArgsConstructor
public class AddEventAttendeeProcessor implements RequestProcessor {

    private final UserService userService;
    private final EventService eventService;
    private final Mapper<EventDTO, Event> eventMapper;

    @Override
    public EventPlannerResponse process(EventPlannerRequest request) throws ParseException {
        User attendee = userService.getUserFromId(request.getUserId());
        Event updatedEvent = eventService.addEventAttendee(request.getEventId(), attendee);
        return EventPlannerResponse.builder()
                .eventPlannerMessage(new EventPlannerMessage(eventMapper.toDTO(updatedEvent)))
                .build();
    }

    @Override
    public String processorType() {
        return ADD_EVENT_ATTENDEE;
    }
}
