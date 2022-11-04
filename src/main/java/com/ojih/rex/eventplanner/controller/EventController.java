package com.ojih.rex.eventplanner.controller;

import com.ojih.rex.eventplanner.model.Location;
import com.ojih.rex.eventplanner.model.User;
import com.ojih.rex.eventplanner.model.event.Event;
import com.ojih.rex.eventplanner.model.event.EventDTO;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/event")
public class EventController {

    private final EventService eventService;
    private final UserService userService;
    private final Mapper<EventDTO, Event> mapper;

    @Autowired
    public EventController(EventService eventService, UserService userService, UserService userService1, @Qualifier("eventMapper") Mapper<EventDTO, Event> mapper) {
        this.eventService = eventService;
        this.userService = userService1;
        this.mapper = mapper;
    }

    @GetMapping("")
    public ResponseEntity<Object> getEvent(@RequestParam(value = "id", required = false) Long eventId,
                             @RequestHeader(required = false) Map<String, String> requestHeaders,
                             @RequestBody(required = false) String requestBody) {
        ResponseEntity<Object> responseEntity;
        try{
            if (requestBody != null) {
                JSONObject requestBodyJson = new JSONObject(requestBody);
                eventId = requestBodyJson.getLong("eventId");
            }
            Event event = eventService.getEventFromId(eventId);

            if (event != null)
                responseEntity = new ResponseEntity<>(mapper.toDto(event), HttpStatus.OK);
            else
                responseEntity = new ResponseEntity<>("Unable to fetch event " + eventId + " from DB", HttpStatus.NO_CONTENT);
        } catch (JSONException e) {
            e.printStackTrace();
            responseEntity = new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            responseEntity = new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @PostMapping("")
    public ResponseEntity<Object> postEvent(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                            @RequestBody String requestBody) {

        ResponseEntity<Object> responseEntity;
        try {
            if (requestBody == null) {
                responseEntity = new ResponseEntity<>("Cannot store null event", HttpStatus.BAD_REQUEST);
            } else {
                Event event = new Event();
                JSONObject requestBodyJson = new JSONObject(requestBody);
                Optional.ofNullable(requestBodyJson.optString("title")).ifPresent(event::setTitle);
                Optional.ofNullable(requestBodyJson.optString("date")).ifPresent(date -> event.setDate(getDateFomDateString(date)));
                Optional.ofNullable(requestBodyJson.optJSONObject("location")).ifPresent(location -> event.setLocation(getLocationFromString(location)));
                Optional.ofNullable(requestBodyJson.optString("description")).ifPresent(event::setDescription);
                Optional.ofNullable(requestBodyJson.optString("category")).ifPresent(event::setCategory);
                Optional.of(requestBodyJson.optLong("hostId")).ifPresent(hostId -> {
                    User host = userService.getUserFromId(hostId);
                    eventService.storeEventWithHost(host, event);
                });

                eventService.storeEvent(event);
                responseEntity = new ResponseEntity<>("Event stored", HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
            responseEntity = new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    public Date getDateFomDateString(String dateString) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-dd-MM'T'HH:mm:ss.SSSX");
        try {
            date = format.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public Location getLocationFromString(JSONObject locationJson) {
        Location location = new Location();
        Optional.ofNullable(locationJson.optString("streetAddress")).ifPresent(location::setStreetAddress);
        Optional.ofNullable(locationJson.optString("city")).ifPresent(location::setCity);
        Optional.ofNullable(locationJson.optString("state")).ifPresent(location::setState);
        Optional.ofNullable(locationJson.optString("postalCode")).ifPresent(location::setPostalCode);
        return location;
    }

    @GetMapping("/events")
    public List<EventDTO> getEvents() {
        return mapper.toDtos(eventService.getEvents());
    }
}
