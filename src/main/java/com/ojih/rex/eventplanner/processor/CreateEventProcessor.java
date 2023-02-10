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

import static com.ojih.rex.eventplanner.constant.EventPlannerConstants.CREATE_EVENT;
import static com.ojih.rex.eventplanner.constant.EventPlannerConstants.SUCCESS;
import static com.ojih.rex.eventplanner.util.DateStringConverter.getDateFomDateString;

@Component
@RequiredArgsConstructor
public class CreateEventProcessor implements RequestProcessor {

    private final UserService userService;
    private final Mapper<EventDTO, Event> eventMapper;
    private final EventService eventService;

    @Override
    public EventPlannerResponse process(EventPlannerRequest request) throws ParseException {
        User host = userService.getUserFromId(request.getHostId());
        Event event = Event.builder()
                .title(request.getTitle())
                .date(getDateFomDateString(request.getDate()))
                .location(request.getLocation())
                .description(request.getDescription())
                .category(request.getCategory())
                .maxAttendees(request.getMaxAttendees())
                .build();
        Event storedEvent = eventService.storeEvent(host, event);
        return EventPlannerResponse.builder()
                .eventPlannerMessage(new EventPlannerMessage(SUCCESS, eventMapper.toDTO(storedEvent)))
                .build();
    }

    @Override
    public String processorType() {
        return CREATE_EVENT;
    }
}
