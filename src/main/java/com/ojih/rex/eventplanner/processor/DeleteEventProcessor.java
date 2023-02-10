package com.ojih.rex.eventplanner.processor;

import com.ojih.rex.eventplanner.model.request.EventPlannerRequest;
import com.ojih.rex.eventplanner.model.response.EventPlannerMessage;
import com.ojih.rex.eventplanner.model.response.EventPlannerResponse;
import com.ojih.rex.eventplanner.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;

import static com.ojih.rex.eventplanner.constant.EventPlannerConstants.DELETE_EVENT;

@Component
@RequiredArgsConstructor
public class DeleteEventProcessor implements RequestProcessor {

    private final EventService eventService;

    @Override
    public EventPlannerResponse process(EventPlannerRequest request) throws ParseException {
        eventService.removeEvent(request.getEventId());
        return EventPlannerResponse.builder()
                .eventPlannerMessage(new EventPlannerMessage("Event " + request + " removed."))
                .build();
    }

    @Override
    public String processorType() {
        return DELETE_EVENT;
    }
}
