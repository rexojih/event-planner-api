package com.ojih.rex.eventplanner.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventPlannerErrorResponse {
    private EventPlannerErrorMessage eventPlannerErrorMessage;
}
