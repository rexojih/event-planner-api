package com.ojih.rex.eventplanner.controller;

import com.ojih.rex.eventplanner.constants.EventPlannerConstants;
import com.ojih.rex.eventplanner.exception.UserServiceException;
import com.ojih.rex.eventplanner.model.Event;
import com.ojih.rex.eventplanner.model.EventPlannerResponseBody;
import com.ojih.rex.eventplanner.model.Location;
import com.ojih.rex.eventplanner.model.User;
import com.ojih.rex.eventplanner.model.dto.EventDTO;
import com.ojih.rex.eventplanner.model.dto.UserDTO;
import com.ojih.rex.eventplanner.service.EventService;
import com.ojih.rex.eventplanner.service.UserService;
import com.ojih.rex.eventplanner.utilities.Mapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ojih.rex.eventplanner.constants.EventPlannerConstants.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final EventService eventService;
    private final Mapper<UserDTO, User> userMapper;
    private final Mapper<EventDTO, Event> eventMapper;

    @Autowired
    public UserController(UserService userService, EventService eventService, @Qualifier("userMapper") Mapper<UserDTO, User> userMapper, @Qualifier("eventMapper") Mapper<EventDTO, Event> eventMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.eventMapper = eventMapper;
        this.eventService = eventService;
    }

    @GetMapping("/get")
    public ResponseEntity<EventPlannerResponseBody> getUser(@RequestParam(value = "id", required = false) Long userId,
                                           @RequestHeader(required = false) Map<String, String> requestHeaders,
                                           @RequestBody(required = false) String requestBody) {
        EventPlannerResponseBody responseBody;
        ResponseEntity<EventPlannerResponseBody> responseEntity;
        try {
            if (!(requestBody == null || requestBody.isBlank())) {
                JSONObject requestBodyJson = new JSONObject(requestBody);
                userId = requestBodyJson.getLong(USER_ID);
            }
            User user = userService.getUserFromId(userId);

            if (user != null) {
                responseBody = new EventPlannerResponseBody(SUCCESS, userMapper.toDto(user));
                responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
            } else {
                responseBody = new EventPlannerResponseBody("Unable to fetch user " + userId + " from DB");
                responseEntity = new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            responseBody = new EventPlannerResponseBody(e.getMessage());
            responseEntity = new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            responseBody = new EventPlannerResponseBody(e.getMessage());
            responseEntity = new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @GetMapping("/all")
    public ResponseEntity<EventPlannerResponseBody> getUsers() {
        List<User> users = userService.getUsers();
        UserDTO [] userDTOs = new UserDTO[users.size()];
        EventPlannerResponseBody responseBody = new EventPlannerResponseBody(SUCCESS, userMapper.toDtos(users).toArray(userDTOs));
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/events")
    public ResponseEntity<EventPlannerResponseBody> getUserEvents(@RequestParam(value = "id", required = false) Long userId,
                                                    @RequestHeader(required = false) Map<String, String> requestHeaders,
                                                    @RequestBody(required = false) String requestBody) {
        EventPlannerResponseBody responseBody;
        ResponseEntity<EventPlannerResponseBody> responseEntity;
        try {
            if (!(requestBody == null || requestBody.isBlank())) {
                JSONObject requestBodyJson = new JSONObject(requestBody);
                userId = requestBodyJson.getLong(USER_ID);
            }
            User user = userService.getUserFromId(userId);
            List<Event> events = user.getEvents();
            EventDTO [] eventDTOs = new EventDTO[events.size()];
            responseBody = new EventPlannerResponseBody(SUCCESS, eventMapper.toDtos(events).toArray(eventDTOs));
            responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (JSONException e) {
            responseBody = new EventPlannerResponseBody(e.getMessage());
            responseEntity = new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        } catch (UserServiceException e) {
            responseBody = new EventPlannerResponseBody(e.getMessage());
            responseEntity = new ResponseEntity<>(responseBody, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            responseBody = new EventPlannerResponseBody(e.getMessage());
            responseEntity = new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @PostMapping("/add")
    public ResponseEntity<EventPlannerResponseBody> postUser(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                            @RequestBody String requestBody) {
        EventPlannerResponseBody responseBody;
        ResponseEntity<EventPlannerResponseBody> responseEntity;

        try {
            if (requestBody == null || requestBody.isBlank()) {
                responseBody = new EventPlannerResponseBody("Cannot store null user");
                responseEntity = new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            }
            else {
                User user = new User();
                JSONObject requestBodyJson = new JSONObject(requestBody);

                Optional.ofNullable(requestBodyJson.optString("userName")).ifPresent(user::setUserName);
                Optional.ofNullable(requestBodyJson.optString("firstName")).ifPresent(user::setFirstName);
                Optional.ofNullable(requestBodyJson.optString("lastName")).ifPresent(user::setLastName);
                Optional.ofNullable(requestBodyJson.optString("email")).ifPresent(user::setEmail);
                Optional.ofNullable(requestBodyJson.optString("password")).ifPresent(user::setPassword);
                Optional.ofNullable(requestBodyJson.optJSONObject("location")).ifPresent(location -> user.setLocation(getLocationFromJson(location)));
                User storedUser = userService.storeUser(user);
                responseBody = new EventPlannerResponseBody(SUCCESS, userMapper.toDto(storedUser));
                responseEntity = new ResponseEntity<>(responseBody, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseBody = new EventPlannerResponseBody(e.getMessage());
            responseEntity = new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    @PutMapping("/update")
    public ResponseEntity<EventPlannerResponseBody> updateUser(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                                               @RequestBody String requestBody) {
        EventPlannerResponseBody responseBody;
        ResponseEntity<EventPlannerResponseBody> responseEntity;
        try {
            if (requestBody == null || requestBody.isBlank()) {
                responseBody = new EventPlannerResponseBody("Cannot update null user");
                responseEntity = new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            } else {
                User updateUser = new User();
                JSONObject requestBodyJson = new JSONObject(requestBody);
                if (!validRequestBodyJson(requestBodyJson, USER_ID)) {
                    responseBody = new EventPlannerResponseBody("Unable to update user with no userId");
                    responseEntity = new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
                } else {
                    Long userId = requestBodyJson.getLong(USER_ID);
                    Optional.ofNullable(requestBodyJson.optString("userName")).ifPresent(updateUser::setUserName);
                    Optional.ofNullable(requestBodyJson.optString("firstName")).ifPresent(updateUser::setFirstName);
                    Optional.ofNullable(requestBodyJson.optString("lastName")).ifPresent(updateUser::setLastName);
                    Optional.ofNullable(requestBodyJson.optString("email")).ifPresent(updateUser::setEmail);
                    Optional.ofNullable(requestBodyJson.optString("password")).ifPresent(updateUser::setPassword);
                    Optional.ofNullable(requestBodyJson.optJSONObject("location")).ifPresent(location -> updateUser.setLocation(getLocationFromJson(location)));
                    String originalPassword = requestBodyJson.optString("originalPassword");
                    User updatedUser = userService.updateUser(userId, updateUser, originalPassword);
                    responseBody = new EventPlannerResponseBody(SUCCESS, userMapper.toDto(updatedUser));
                    responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
                }
            }
        } catch (JSONException e) {
            responseBody = new EventPlannerResponseBody(e.getMessage());
            responseEntity = new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            responseBody = new EventPlannerResponseBody(e.getMessage());
            responseEntity = new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<EventPlannerResponseBody> deleteEvent(@RequestParam(value = "id", required = false) Long userId,
                                                                @RequestHeader(required = false) Map<String, String> requestHeaders,
                                                                @RequestBody(required = false) String requestBody) {
        EventPlannerResponseBody responseBody;
        ResponseEntity<EventPlannerResponseBody> responseEntity;
        try {
            if (!(requestBody == null || requestBody.isBlank())) {
                JSONObject requestBodyJson = new JSONObject(requestBody);
                userId = requestBodyJson.getLong(USER_ID);
            }
            if (userService.userExists(userId)) {
                List<Long> eventIds = userService.eventsHosting(userId);
                if (eventIds != null) {
                    eventService.removeEvents(eventIds);
                }
                userService.removeUser(userId);
                responseBody = new EventPlannerResponseBody("User " + userId + " and it's Event(s) " + eventIds + " removed.");
                responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
            } else {
                responseBody = new EventPlannerResponseBody("User " + userId + " does not exist");
                responseEntity = new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
            }
        } catch (JSONException e) {
            responseBody = new EventPlannerResponseBody(e.getMessage());
            responseEntity = new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            e.printStackTrace();
            responseBody = new EventPlannerResponseBody(e.getMessage());
            responseEntity = new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);;
        }
        return responseEntity;
    }

    public Location getLocationFromJson(JSONObject locationJson) {
        Location location = new Location();
        Optional.ofNullable(locationJson.optString("streetAddress")).ifPresent(location::setStreetAddress);
        Optional.ofNullable(locationJson.optString("city")).ifPresent(location::setCity);
        Optional.ofNullable(locationJson.optString("state")).ifPresent(location::setState);
        Optional.ofNullable(locationJson.optString("postalCode")).ifPresent(location::setPostalCode);
        return location;
    }

    private boolean validRequestBodyJson(JSONObject requestBodyJson, String... necessaryKeys) {
        boolean result = false;
        for (String key : necessaryKeys) {
            result = !requestBodyJson.isEmpty() && requestBodyJson.has(key) && requestBodyJson.get(key) != JSONObject.NULL;
        }
        return result;
    }
}
