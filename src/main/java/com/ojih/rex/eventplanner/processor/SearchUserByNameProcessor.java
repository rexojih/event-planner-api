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

import static com.ojih.rex.eventplanner.constant.EventPlannerConstants.*;

@Component
@RequiredArgsConstructor
public class SearchUserByNameProcessor implements RequestProcessor {

    private final UserService userService;
    private final Mapper<UserDTO, User> userMapper;

    @Override
    public EventPlannerResponse process(EventPlannerRequest request) {
        List<User> users;
        if (STARTS_WITH.equals(request.getSearchType()))
            users = userService.getUserFromNameStartingWith(request.getName());
        else if (CONTAINING.equals(request.getSearchType()))
            users = userService.getUsersFromNameContaining(request.getName());
        else
            users = userService.getUsersFromName(request.getName());
        UserDTO[] userDTOs = new UserDTO[users.size()];
        return EventPlannerResponse.builder()
                .eventPlannerMessage(new EventPlannerMessage(SUCCESS, userMapper.toDTOs(users).toArray(userDTOs)))
                .build();
    }

    @Override
    public String processorType() {
        return SEARCH_USERS_BY_NAME;
    }
}
