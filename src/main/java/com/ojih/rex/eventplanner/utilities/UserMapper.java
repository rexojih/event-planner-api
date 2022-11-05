package com.ojih.rex.eventplanner.utilities;

import com.ojih.rex.eventplanner.model.event.Event;
import com.ojih.rex.eventplanner.model.user.User;
import com.ojih.rex.eventplanner.model.user.UserDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper implements Mapper<UserDTO, User> {

    @Override
    public UserDTO toDto(User user) {
        List<Long> eventIds = new ArrayList<>();

        for (Event event : user.getEvents()) {
            eventIds.add(event.getEventId());
        }

        return UserDTO.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastNight(user.getLastName())
                .email(user.getEmail())
                .eventIds(eventIds)
                .build();
    }

    @Override
    public List<UserDTO> toDtos(List<User> users) {
        List<UserDTO> eventDTOS = new ArrayList<>();
        for (User user : users) {
            eventDTOS.add(this.toDto(user));
        }
        return eventDTOS;
    }
}
