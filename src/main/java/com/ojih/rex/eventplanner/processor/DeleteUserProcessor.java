package com.ojih.rex.eventplanner.processor;

import com.ojih.rex.eventplanner.model.request.EventPlannerRequest;
import com.ojih.rex.eventplanner.model.response.EventPlannerMessage;
import com.ojih.rex.eventplanner.model.response.EventPlannerResponse;
import com.ojih.rex.eventplanner.service.EventService;
import com.ojih.rex.eventplanner.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ojih.rex.eventplanner.constant.EventPlannerConstants.DELETE_USER;

@Component
@RequiredArgsConstructor
public class DeleteUserProcessor implements RequestProcessor {

    private final UserService userService;
    private final EventService eventService;

    @Override
    public EventPlannerResponse process(EventPlannerRequest request) {
        List<Long> hostedEventIds = userService.removeUser(request.getUserId());
        if (!hostedEventIds.isEmpty())
            eventService.removeEvents(hostedEventIds);
        return EventPlannerResponse.builder()
                .eventPlannerMessage(new EventPlannerMessage("User "
                        + request.getUserId() + " and their Event(s) "
                        + hostedEventIds + " removed."))
                .build();
    }

    @Override
    public String processorType() {
        return DELETE_USER;
    }
}
