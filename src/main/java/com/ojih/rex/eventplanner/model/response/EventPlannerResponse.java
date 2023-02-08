package com.ojih.rex.eventplanner.model.response;

import com.ojih.rex.eventplanner.model.dto.DTO;

public class EventPlannerResponse {

    String responseMessage;
    DTO[] data;

    public EventPlannerResponse(DTO... data) {
        this.data = data;
    }

    public EventPlannerResponse(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public EventPlannerResponse(String responseMessage,
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
