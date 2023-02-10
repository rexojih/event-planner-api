package com.ojih.rex.eventplanner.controller;

import com.ojih.rex.eventplanner.annotation.DataQuality;
import com.ojih.rex.eventplanner.handler.RequestHandler;
import com.ojih.rex.eventplanner.model.request.EventPlannerRequest;
import com.ojih.rex.eventplanner.model.response.EventPlannerResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.ojih.rex.eventplanner.constant.EventPlannerConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final RequestHandler handler;

    @GetMapping("/get")
    @ResponseBody
    @SneakyThrows
    public EventPlannerResponse getUser(@RequestParam(value = "id") Long userId,
                                        @RequestHeader(required = false) Map<String, String> requestHeaders) {
        return handler.handleRequest(EventPlannerRequest.builder()
                .userId(userId)
                .build(), GET_USER);
    }

    @GetMapping("/all")
    @ResponseBody
    @SneakyThrows
    public EventPlannerResponse getUsers() {
        return handler.handleRequest(null, GET_USERS);
    }

    @GetMapping("/events")
    @ResponseBody
    @SneakyThrows
    public EventPlannerResponse getUserEvents(@RequestParam(value = "id", required = false) Long userId,
                                              @RequestHeader(required = false) Map<String, String> requestHeaders) {
        return handler.handleRequest(EventPlannerRequest.builder()
                .userId(userId)
                .build(), GET_USER_EVENTS);
    }

    @PostMapping("/authenticate")
    @ResponseBody
    @SneakyThrows
    @DataQuality(requestType = AUTHENTICATE_USER)
    public EventPlannerResponse authenticateUser(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                                 @RequestBody EventPlannerRequest request) {
        return handler.handleRequest(request, AUTHENTICATE_USER);
    }

    @PostMapping("/search")
    @ResponseBody
    @SneakyThrows
    public EventPlannerResponse searchUsersByName(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                                  @RequestBody(required = false) EventPlannerRequest request) {
        return handler.handleRequest(request, SEARCH_USERS_BY_NAME);
    }

    @PostMapping("/add")
    @ResponseBody
    @SneakyThrows
    @DataQuality(requestType = CREATE_USER)
    @ResponseStatus(HttpStatus.CREATED)
    public EventPlannerResponse createUser(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                           @RequestBody EventPlannerRequest request) {
        return handler.handleRequest(request, CREATE_USER);
    }

    @PutMapping("/update")
    @ResponseBody
    @SneakyThrows
    @DataQuality(requestType = UPDATE_USER)
    public EventPlannerResponse updateUser(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                           @RequestBody EventPlannerRequest request) {
        return handler.handleRequest(request, UPDATE_USER);
    }

    @DeleteMapping("/delete")
    @ResponseBody
    @SneakyThrows
    public EventPlannerResponse deleteUser(@RequestParam(value = "id", required = false) Long userId,
                                           @RequestHeader(required = false) Map<String, String> requestHeaders) {
        return handler.handleRequest(EventPlannerRequest.builder()
                .userId(userId)
                .build(), DELETE_USER);
    }
}
