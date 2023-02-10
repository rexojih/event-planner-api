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

import java.util.List;

import static com.ojih.rex.eventplanner.constant.EventPlannerConstants.*;

@Component
@RequiredArgsConstructor
public class GetEventsFromIdsProcessor implements RequestProcessor {

    private final EventService eventService;
    private final Mapper<EventDTO, Event> eventMapper;

    @Override
    public EventPlannerResponse process(EventPlannerRequest request) {
        List<Event> events;
        if (UPCOMING.equals(request.getStrategy()))
            events = eventService.getUpcomingEventsFromIds(request.getEventIds());
        else if (PAST.equals(request.getStrategy()))
            events = eventService.getPastEventsFromIds(request.getEventIds());
        else
            events = eventService.getEventsFromIds(request.getEventIds());
        EventDTO[] eventDTOs = new EventDTO[events.size()];
        return EventPlannerResponse.builder()
                .eventPlannerMessage(new EventPlannerMessage(SUCCESS, eventMapper.toDTOs(events).toArray(eventDTOs)))
                .build();
    }

    @Override
    public String processorType() {
        return GET_EVENTS_FROM_IDS;
    }
}
