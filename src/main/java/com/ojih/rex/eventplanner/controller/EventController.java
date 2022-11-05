package com.ojih.rex.eventplanner.controller;

import com.ojih.rex.eventplanner.model.Location;
import com.ojih.rex.eventplanner.model.user.User;
import com.ojih.rex.eventplanner.model.event.Event;
import com.ojih.rex.eventplanner.model.event.EventDTO;
import com.ojih.rex.eventplanner.model.user.UserDTO;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/v1/event")
public class EventController {

    private final EventService eventService;
    private final UserService userService;
    private final Mapper<EventDTO, Event> eventMapper;
    private final Mapper<UserDTO, User> userMapper;

    private static final String EVENT_ID = "eventId";
    private static final String HOST_ID = "hostId";

    @Autowired
    public EventController(EventService eventService, UserService userService, @Qualifier("eventMapper") Mapper<EventDTO, Event> eventMapper, @Qualifier("userMapper") Mapper<UserDTO, User> userMapper) {
        this.eventService = eventService;
        this.userService = userService;
        this.eventMapper = eventMapper;
        this.userMapper = userMapper;
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getEvent(@RequestParam(value = "id", required = false) Long eventId,
                             @RequestHeader(required = false) Map<String, String> requestHeaders,
                             @RequestBody(required = false) String requestBody) {
        ResponseEntity<Object> responseEntity;
        try{
            if (!requestBody.isEmpty()) {
                JSONObject requestBodyJson = new JSONObject(requestBody);
                eventId = requestBodyJson.getLong(EVENT_ID);
            }
            Event event = eventService.getEventFromId(eventId);

            if (event != null)
                responseEntity = new ResponseEntity<>(eventMapper.toDto(event), HttpStatus.OK);
            else
                responseEntity = new ResponseEntity<>("Unable to fetch event " + eventId + " from DB", HttpStatus.NO_CONTENT);
        } catch (JSONException e) {
            e.printStackTrace();
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @GetMapping("/all")
    public ResponseEntity<List<EventDTO>> getEvents() {
        return new ResponseEntity<>(eventMapper.toDtos(eventService.getEvents()), HttpStatus.OK);
    }

    @GetMapping("/attendees")
    public ResponseEntity<Object> getEventAttendees(@RequestParam(value = "id", required = false) Long eventId,
                                                        @RequestHeader(required = false) Map<String, String> requestHeaders,
                                                        @RequestBody(required = false) String requestBody) {

        ResponseEntity<Object> responseEntity;

        try {
            if (!requestBody.isEmpty()) {
                JSONObject requestBodyJson = new JSONObject(requestBody);
                eventId = requestBodyJson.getLong(EVENT_ID);
            }

            Event event = eventService.getEventFromId(eventId);
            responseEntity = new ResponseEntity<>(userMapper.toDtos(event.getAttendees()), HttpStatus.OK);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        }

        return responseEntity;
    }

    @PostMapping("/add")
    public ResponseEntity<Object> postEvent(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                            @RequestBody String requestBody) {

        ResponseEntity<Object> responseEntity;
        try {
            if (requestBody.isEmpty() || requestBody.isBlank()) {
                responseEntity = new ResponseEntity<>("Cannot store null event", HttpStatus.BAD_REQUEST);
            } else {
                Event event = new Event();
                JSONObject requestBodyJson = new JSONObject(requestBody);

                if (!validRequestBodyJson(requestBodyJson, HOST_ID)) {
                    responseEntity = new ResponseEntity<>("Unable to add event with no host", HttpStatus.BAD_REQUEST);
                } else {
                    Long hostId = requestBodyJson.getLong(HOST_ID);
                    User host = userService.getUserFromId(hostId);
                    Optional.ofNullable(requestBodyJson.optString("title")).ifPresent(event::setTitle);
                    event.setDate(getDateFomDateString(requestBodyJson.optString("date")));
                    Optional.ofNullable(requestBodyJson.optJSONObject("location")).ifPresent(location -> event.setLocation(getLocationFromJson(location)));
                    Optional.ofNullable(requestBodyJson.optString("description")).ifPresent(event::setDescription);
                    Optional.ofNullable(requestBodyJson.optString("category")).ifPresent(event::setCategory);
                    Optional.of(requestBodyJson.optInt("maxAttendees")).ifPresent(event::setMaxAttendees);
                    Event storedEvent = eventService.storeEventWithHost(host, event);
                    responseEntity = new ResponseEntity<>(eventMapper.toDto(storedEvent), HttpStatus.CREATED);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateEvent(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                              @RequestBody String requestBody) {

        ResponseEntity<Object> responseEntity;
        try {
            if (requestBody == null) {
                responseEntity = new ResponseEntity<>("Cannot update null event", HttpStatus.BAD_REQUEST);
            } else {
                Event updateEvent = new Event();
                JSONObject requestBodyJson = new JSONObject(requestBody);

                if (!validRequestBodyJson(requestBodyJson, EVENT_ID)) {
                    responseEntity = new ResponseEntity<>("Unable to update event with no eventId", HttpStatus.BAD_REQUEST);
                } else {
                    Long eventId = requestBodyJson.getLong(EVENT_ID);
                    Optional.ofNullable(requestBodyJson.optString("title")).ifPresent(updateEvent::setTitle);
                    updateEvent.setDate(getDateFomDateString(requestBodyJson.optString("date")));
                    Optional.ofNullable(requestBodyJson.optJSONObject("location")).ifPresent(location -> updateEvent.setLocation(getLocationFromJson(location)));
                    Optional.ofNullable(requestBodyJson.optString("description")).ifPresent(updateEvent::setDescription);
                    Optional.ofNullable(requestBodyJson.optString("category")).ifPresent(updateEvent::setCategory);
                    Optional.of(requestBodyJson.optInt("maxAttendees")).ifPresent(updateEvent::setMaxAttendees);
                    Event updatedEvent = eventService.updateEvent(eventId, updateEvent);
                    responseEntity = new ResponseEntity<>(eventMapper.toDto(updatedEvent), HttpStatus.CREATED);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    private boolean validRequestBodyJson(JSONObject requestBodyJson, String necessaryKey) {
        return !requestBodyJson.isEmpty() && requestBodyJson.has(necessaryKey) && requestBodyJson.get(necessaryKey) != JSONObject.NULL;
    }

    public Date getDateFomDateString(String dateString) throws ParseException {
        Date date = null;
        if (dateString != null && !dateString.isEmpty()) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-dd-MM'T'HH:mm:ss.SSSX");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = format.parse(dateString);
        }
        return date;
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
