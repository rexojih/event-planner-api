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
public class SearchEventsByTitleProcessor implements RequestProcessor {

    private final EventService eventService;
    private final Mapper<EventDTO, Event> eventMapper;

    @Override
    public EventPlannerResponse process(EventPlannerRequest request) {
        List<Event> events;
        if (STARTS_WITH.equals(request.getSearchType())) {
            if (UPCOMING.equals(request.getStrategy()))
                events = eventService.getUpcomingEventsFromTitleStartingWith(request.getTitle());
            else if (PAST.equals(request.getStrategy()))
                events = eventService.getPastEventsFromTitleStartingWith(request.getTitle());
            else
                events = eventService.getEventsFromTitleStartingWith(request.getTitle());
        } else if (CONTAINING.equals(request.getSearchType())) {
            if (UPCOMING.equals(request.getStrategy()))
                events = eventService.getUpcomingEventsFromTitleContaining(request.getTitle());
            else if (PAST.equals(request.getStrategy()))
                events = eventService.getPastEventsFromTitleContaining(request.getTitle());
            else
                events = eventService.getEventsFromTitleContaining(request.getTitle());
        } else {
            if (UPCOMING.equals(request.getStrategy()))
                events = eventService.getUpcomingEventsFromTitle(request.getTitle());
            else if (PAST.equals(request.getStrategy()))
                events = eventService.getPastEventsFromTitle(request.getTitle());
            else
                events = eventService.getEventsFromTitle(request.getTitle());
        }
        EventDTO[] eventDTOs = new EventDTO[events.size()];
        return EventPlannerResponse.builder()
                .eventPlannerMessage(new EventPlannerMessage(SUCCESS, eventMapper.toDTOs(events).toArray(eventDTOs)))
                .build();
    }

    @Override
    public String processorType() {
        return SEARCH_EVENTS_BY_TITLE;
    }
}
