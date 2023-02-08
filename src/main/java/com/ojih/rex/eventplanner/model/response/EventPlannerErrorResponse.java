package com.ojih.rex.eventplanner.model.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventPlannerErrorResponse {

    private final String status;
    private final String type;
    private final String exception;
    private final String message;
}
