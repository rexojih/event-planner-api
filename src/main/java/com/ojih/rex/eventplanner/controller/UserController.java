package com.ojih.rex.eventplanner.controller;

import com.ojih.rex.eventplanner.annotation.DataQuality;
import com.ojih.rex.eventplanner.model.Event;
import com.ojih.rex.eventplanner.model.User;
import com.ojih.rex.eventplanner.model.dto.EventDTO;
import com.ojih.rex.eventplanner.model.dto.UserDTO;
import com.ojih.rex.eventplanner.model.request.EventPlannerRequest;
import com.ojih.rex.eventplanner.model.response.EventPlannerResponse;
import com.ojih.rex.eventplanner.service.EventService;
import com.ojih.rex.eventplanner.service.UserService;
import com.ojih.rex.eventplanner.util.Mapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.ojih.rex.eventplanner.constant.EventPlannerConstants.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final EventService eventService;
    private final Mapper<UserDTO, User> userMapper;
    private final Mapper<EventDTO, Event> eventMapper;

    @Autowired
    public UserController(UserService userService,
                          EventService eventService,
                          @Qualifier("userMapper") Mapper<UserDTO, User> userMapper,
                          @Qualifier("eventMapper") Mapper<EventDTO, Event> eventMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.eventMapper = eventMapper;
        this.eventService = eventService;
    }

    @GetMapping("/get")
    @ResponseBody
    @SneakyThrows
    public EventPlannerResponse getUser(@RequestParam(value = "id") Long userId,
                                        @RequestHeader(required = false) Map<String, String> requestHeaders) {
        User user = userService.getUserFromId(userId);
        return new EventPlannerResponse(SUCCESS, userMapper.toDTO(user));
    }

    @GetMapping("/all")
    @ResponseBody
    public EventPlannerResponse getUsers() {
        List<User> users = userService.getUsers();
        UserDTO[] userDTOs = new UserDTO[users.size()];
        return new EventPlannerResponse(SUCCESS, userMapper.toDTOs(users).toArray(userDTOs));
    }

    @GetMapping("/events")
    @ResponseBody
    @SneakyThrows
    public EventPlannerResponse getUserEvents(@RequestParam(value = "id", required = false) Long userId,
                                              @RequestHeader(required = false) Map<String, String> requestHeaders) {
        User user = userService.getUserFromId(userId);
        List<Event> events = user.getEvents();
        EventDTO[] eventDTOs = new EventDTO[events.size()];
        return new EventPlannerResponse(SUCCESS, eventMapper.toDTOs(events).toArray(eventDTOs));
    }

    @PostMapping("/authenticate")
    @ResponseBody
    @SneakyThrows
    public EventPlannerResponse authenticateUser(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                                 @RequestBody EventPlannerRequest request) {
        User user = userService.authenticateUser(request.getUsername(), request.getPassword());
        return new EventPlannerResponse(SUCCESS, userMapper.toDTO(user));
    }

    @PostMapping("/search")
    @ResponseBody
    public EventPlannerResponse searchUserByName(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                                 @RequestBody(required = false) EventPlannerRequest request) {
        List<User> users;
        if (STARTS_WITH.equals(request.getSearchType()))
            users = userService.getUserFromNameStartingWith(request.getName());
        else if (CONTAINING.equals(request.getSearchType()))
            users = userService.getUsersFromNameContaining(request.getName());
        else
            users = userService.getUsersFromName(request.getName());
        UserDTO[] userDTOs = new UserDTO[users.size()];
        return new EventPlannerResponse(SUCCESS, userMapper.toDTOs(users).toArray(userDTOs));
    }

    @PostMapping("/add")
    @ResponseBody
    @SneakyThrows
    @DataQuality(requestType = "createUser")
    @ResponseStatus(HttpStatus.CREATED)
    public EventPlannerResponse createUser(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                           @RequestBody EventPlannerRequest request) {
        User user = User.builder()
                .userName(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .location(request.getLocation())
                .build();

        User storedUser = userService.storeUser(user);
        return new EventPlannerResponse(SUCCESS, userMapper.toDTO(storedUser));
    }

    @PutMapping("/update")
    @ResponseBody
    @SneakyThrows
    @DataQuality(requestType = "updateUser")
    public EventPlannerResponse updateUser(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                           @RequestBody EventPlannerRequest request) {
        User updateUser = User.builder()
                .userName(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getNewPassword())
                .build();
        User updatedUser = userService.updateUser(request.getUserId(), updateUser, request.getOriginalPassword());
        return new EventPlannerResponse(SUCCESS, userMapper.toDTO(updatedUser));
    }

    @DeleteMapping("/delete")
    @ResponseBody
    @SneakyThrows
    public EventPlannerResponse deleteEvent(@RequestParam(value = "id", required = false) Long userId,
                                            @RequestHeader(required = false) Map<String, String> requestHeaders) {
        List<Long> hostedEventIds = userService.removeUser(userId);
        if (!hostedEventIds.isEmpty())
            eventService.removeEvents(hostedEventIds);
        return new EventPlannerResponse("User " + userId + " and their Event(s) " + hostedEventIds + " removed.");
    }
}
