package com.ojih.rex.eventplanner.controller;

import com.ojih.rex.eventplanner.model.Location;
import com.ojih.rex.eventplanner.model.user.User;
import com.ojih.rex.eventplanner.model.user.UserDTO;
import com.ojih.rex.eventplanner.service.UserService;
import com.ojih.rex.eventplanner.utilities.Mapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final Mapper<UserDTO, User> userMapper;

    @Autowired
    public UserController(UserService userService, @Qualifier("userMapper") Mapper<UserDTO, User> userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getUser(@RequestParam(value = "id", required = false) Long userId,
                                           @RequestHeader(required = false) Map<String, String> requestHeaders,
                                           @RequestBody(required = false) String requestBody) {
        ResponseEntity<Object> responseEntity;
        try {
            if (!requestBody.isBlank()) {
                JSONObject requestBodyJson = new JSONObject(requestBody);
                userId = requestBodyJson.getLong("userId");
            }
            User user = userService.getUserFromId(userId);

            if (user != null)
                responseEntity = new ResponseEntity<>(userMapper.toDto(user), HttpStatus.OK);
            else
                responseEntity = new ResponseEntity<>("Unable to fetch user " + userId + " from DB", HttpStatus.NOT_FOUND);
        } catch (JSONException e) {
            e.printStackTrace();
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @PostMapping("/add")
    public ResponseEntity<Object> postUser(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                            @RequestBody String requestBody) {
        ResponseEntity<Object> responseEntity;

        try {
            if (requestBody.isBlank())
                responseEntity = new ResponseEntity<>("Cannot store null user", HttpStatus.BAD_REQUEST);
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
                responseEntity = new ResponseEntity<>(userMapper.toDto(storedUser), HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
}
