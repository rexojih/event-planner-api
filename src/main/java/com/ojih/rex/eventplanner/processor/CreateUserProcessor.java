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

import static com.ojih.rex.eventplanner.constant.EventPlannerConstants.CREATE_USER;
import static com.ojih.rex.eventplanner.constant.EventPlannerConstants.SUCCESS;

@Component
@RequiredArgsConstructor
public class CreateUserProcessor implements RequestProcessor {

    private final UserService userService;
    private final Mapper<UserDTO, User> userMapper;

    @Override
    public EventPlannerResponse process(EventPlannerRequest request) {
        User user = User.builder()
                .userName(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .location(request.getLocation())
                .build();

        User storedUser = userService.storeUser(user);
        return EventPlannerResponse.builder()
                .eventPlannerMessage(new EventPlannerMessage(SUCCESS, userMapper.toDTO(storedUser)))
                .build();
    }

    @Override
    public String processorType() {
        return CREATE_USER;
    }
}
