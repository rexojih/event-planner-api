package com.ojih.rex.eventplanner.processor;

import com.ojih.rex.eventplanner.model.request.EventPlannerRequest;
import com.ojih.rex.eventplanner.model.response.EventPlannerResponse;

import java.text.ParseException;

public interface RequestProcessor {

    EventPlannerResponse process(EventPlannerRequest request) throws ParseException;
    String processorType();
}
