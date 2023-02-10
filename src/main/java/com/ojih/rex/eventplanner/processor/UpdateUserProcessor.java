package com.ojih.rex.eventplanner.processor;

import com.ojih.rex.eventplanner.model.User;
import com.ojih.rex.eventplanner.model.dto.UserDTO;
import com.ojih.rex.eventplanner.model.request.EventPlannerRequest;
import com.ojih.rex.eventplanner.model.response.EventPlannerMessage;
import com.ojih.rex.eventplanner.model.response.EventPlannerResponse;
import com.ojih.rex.eventplanner.service.UserService;
import com.ojih.rex.eventplanner.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.ojih.rex.eventplanner.constant.EventPlannerConstants.SUCCESS;
import static com.ojih.rex.eventplanner.constant.EventPlannerConstants.UPDATE_USER;

@Component
@RequiredArgsConstructor
public class UpdateUserProcessor implements RequestProcessor {

    private final UserService userService;
    private final Mapper<UserDTO, User> userMapper;

    @Override
    public EventPlannerResponse process(EventPlannerRequest request) {
        User updateUser = User.builder()
                .userName(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getNewPassword())
                .build();
        User updatedUser = userService.updateUser(request.getUserId(), updateUser, request.getOriginalPassword());
        return EventPlannerResponse.builder()
                .eventPlannerMessage(new EventPlannerMessage(SUCCESS, userMapper.toDTO(updatedUser)))
                .build();
    }

    @Override
    public String processorType() {
        return UPDATE_USER;
    }
}
