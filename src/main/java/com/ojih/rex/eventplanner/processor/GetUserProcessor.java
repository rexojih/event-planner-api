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

import static com.ojih.rex.eventplanner.constant.EventPlannerConstants.GET_USER;
import static com.ojih.rex.eventplanner.constant.EventPlannerConstants.SUCCESS;

@Component
@RequiredArgsConstructor
public class GetUserProcessor implements RequestProcessor {

    private final UserService userService;
    private final Mapper<UserDTO, User> userMapper;

    @Override
    public EventPlannerResponse process(EventPlannerRequest request) {
        User user = userService.getUserFromId(request.getUserId());
        return EventPlannerResponse.builder()
                .eventPlannerMessage(new EventPlannerMessage(SUCCESS, userMapper.toDTO(user)))
                .build();
    }

    @Override
    public String processorType() {
        return GET_USER;
    }
}
