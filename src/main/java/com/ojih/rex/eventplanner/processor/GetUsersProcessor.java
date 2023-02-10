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

import java.util.List;

import static com.ojih.rex.eventplanner.constant.EventPlannerConstants.GET_USERS;
import static com.ojih.rex.eventplanner.constant.EventPlannerConstants.SUCCESS;

@Component
@RequiredArgsConstructor
public class GetUsersProcessor implements RequestProcessor {

    private final UserService userService;
    private final Mapper<UserDTO, User> userMapper;

    @Override
    public EventPlannerResponse process(EventPlannerRequest request) {
        List<User> users = userService.getUsers();
        UserDTO[] userDTOs = new UserDTO[users.size()];
        return EventPlannerResponse.builder()
                .eventPlannerMessage(new EventPlannerMessage(SUCCESS, userMapper.toDTOs(users).toArray(userDTOs)))
                .build();
    }

    @Override
    public String processorType() {
        return GET_USERS;
    }
}
