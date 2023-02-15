package com.ojih.rex.eventplanner.model.response;


import com.ojih.rex.eventplanner.model.dto.DTO;
import lombok.Data;

@Data
public class EventPlannerMessage {

    private String message;
    private DTO[] data;

    public EventPlannerMessage(DTO... data) {
        this.data = data;
    }

    public EventPlannerMessage(String message) {
        this.message = message;
    }

    public EventPlannerMessage(String message,
                               DTO... data) {
        this.message = message;
        this.data = data;
    }
}
