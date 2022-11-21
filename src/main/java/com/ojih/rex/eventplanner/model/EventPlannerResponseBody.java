package com.ojih.rex.eventplanner.model;

import com.ojih.rex.eventplanner.model.dto.DTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EventPlannerResponseBody {

    String responseMessage;
    DTO [] data;

    public EventPlannerResponseBody(DTO... data) {
        this.data = data;
    }

    public EventPlannerResponseBody(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public DTO[] getData() {
        return data;
    }
}
