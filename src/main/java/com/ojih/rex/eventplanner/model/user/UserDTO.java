package com.ojih.rex.eventplanner.model.user;

import lombok.Builder;

import java.util.List;

@Builder
public class UserDTO {

    private Long userId;
    private String userName;
    private String firstName;
    private String lastNight;
    private String email;
    private List<Long> eventIds;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastNight() {
        return lastNight;
    }

    public void setLastNight(String lastNight) {
        this.lastNight = lastNight;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Long> getEventIds() {
        return eventIds;
    }

    public void setEventIds(List<Long> events) {
        this.eventIds = events;
    }
}
