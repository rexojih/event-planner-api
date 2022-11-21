package com.ojih.rex.eventplanner.model;

import com.ojih.rex.eventplanner.model.dto.DTO;

public class EventPlannerResponseBody {

    String responseMessage;
    DTO [] data;

    public EventPlannerResponseBody(DTO... data) {
        this.data = data;
    }

    public EventPlannerResponseBody(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public EventPlannerResponseBody(String responseMessage,
                                    DTO... data) {
        this.responseMessage = responseMessage;
        this.data = data;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public DTO[] getData() {
        return data;
    }
}
