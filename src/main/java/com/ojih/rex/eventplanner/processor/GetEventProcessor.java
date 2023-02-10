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

import static com.ojih.rex.eventplanner.constant.EventPlannerConstants.GET_EVENT;
import static com.ojih.rex.eventplanner.constant.EventPlannerConstants.SUCCESS;

@Component
@RequiredArgsConstructor
public class GetEventProcessor implements RequestProcessor {

    private final EventService eventService;
    private final Mapper<EventDTO, Event> eventMapper;

    @Override
    public EventPlannerResponse process(EventPlannerRequest request) {
        Event event = eventService.getEventFromId(request.getEventId());
        return EventPlannerResponse.builder()
                .eventPlannerMessage(new EventPlannerMessage(SUCCESS, eventMapper.toDTO(event)))
                .build();
    }

    @Override
    public String processorType() {
        return GET_EVENT;
    }
}
