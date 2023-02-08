package com.ojih.rex.eventplanner.model.response;


import com.ojih.rex.eventplanner.model.dto.DTO;

public class EventPlannerMessage {

    private String responseMessage;
    private DTO[] data;

    public EventPlannerMessage(DTO... data) {
        this.data = data;
    }

    public EventPlannerMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public EventPlannerMessage(String responseMessage,
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
