package com.ojih.rex.eventplanner.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventPlannerResponse {
    private EventPlannerMessage eventPlannerMessage;
    private EventPlannerErrorMessage eventPlannerErrorMessage;
}
