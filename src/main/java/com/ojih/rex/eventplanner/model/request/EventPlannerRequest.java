package com.ojih.rex.eventplanner.model.request;

import com.ojih.rex.eventplanner.model.Location;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EventPlannerRequest {
    private Long userId;
    private String username;
    private String password;
    private String newPassword;
    private String originalPassword;
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private Long eventId;
    private List<Long> eventIds;
    private Long hostId;
    private String title;
    private String date;
    private Location location;
    private String description;
    private String category;
    private String strategy;
    private int maxAttendees;
    private String searchType;
}
