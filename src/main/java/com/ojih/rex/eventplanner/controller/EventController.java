package com.ojih.rex.eventplanner.controller;

import com.ojih.rex.eventplanner.exception.EventServiceException;
import com.ojih.rex.eventplanner.model.Event;
import com.ojih.rex.eventplanner.model.EventPlannerResponseBody;
import com.ojih.rex.eventplanner.model.Location;
import com.ojih.rex.eventplanner.model.User;
import com.ojih.rex.eventplanner.model.dto.EventDTO;
import com.ojih.rex.eventplanner.model.dto.UserDTO;
import com.ojih.rex.eventplanner.service.EventService;
import com.ojih.rex.eventplanner.service.UserService;
import com.ojih.rex.eventplanner.utilities.Mapper;
import org.json.JSONArray;
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

import static com.ojih.rex.eventplanner.constants.EventPlannerConstants.*;

@RestController
@RequestMapping("/api/v1/event")
public class EventController {

    private final EventService eventService;
    private final UserService userService;
    private final Mapper<EventDTO, Event> eventMapper;
    private final Mapper<UserDTO, User> userMapper;

    @Autowired
    public EventController(EventService eventService,
                           UserService userService,
                           @Qualifier("eventMapper") Mapper<EventDTO, Event> eventMapper,
                           @Qualifier("userMapper") Mapper<UserDTO, User> userMapper) {
        this.eventService = eventService;
        this.userService = userService;
        this.eventMapper = eventMapper;
        this.userMapper = userMapper;
    }

    @GetMapping("/get")
    public ResponseEntity<EventPlannerResponseBody> getEvent(@RequestParam(value = "id", required = false) Long eventId,
                                                             @RequestHeader(required = false) Map<String, String> requestHeaders,
                                                             @RequestBody(required = false) String requestBody) {
        EventPlannerResponseBody responseBody;
        ResponseEntity<EventPlannerResponseBody> responseEntity;
        try{
            if (eventId == null && requestBody != null && !requestBody.isBlank()) {
                JSONObject requestBodyJson = new JSONObject(requestBody);
                eventId = requestBodyJson.getLong(EVENT_ID);
            }
            Event event = eventService.getEventFromId(eventId);
            if (event != null) {
                responseBody = new EventPlannerResponseBody(SUCCESS, eventMapper.toDto(event));
                responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
            } else {
                responseBody = new EventPlannerResponseBody("Unable to fetch event " + eventId + " from DB");
                responseEntity = new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
            }
        } catch (JSONException e) {
            responseBody = new EventPlannerResponseBody(e.getMessage());
            responseEntity = new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        } catch (EventServiceException e) {
            responseBody = new EventPlannerResponseBody(e.getMessage());
            responseEntity = new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            responseBody = new EventPlannerResponseBody(e.getMessage());
            responseEntity = new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @GetMapping("/getFromIds")
    public ResponseEntity<EventPlannerResponseBody> getEventsFromIds(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                                                     @RequestBody String requestBody) {
        EventPlannerResponseBody responseBody;
        ResponseEntity<EventPlannerResponseBody> responseEntity;
        try{
            JSONObject requestBodyJson = new JSONObject(requestBody);
            String strategy = requestBodyJson.optString(STRATEGY);
            JSONArray eventIdJsonArray = requestBodyJson.getJSONArray(EVENT_IDS);
            List<Object> eventIdObjects = eventIdJsonArray.toList();
            List<Long> eventIds = new ArrayList<>();
            for (Object eventIdObject : eventIdObjects) {
                Integer eventId = (Integer) eventIdObject;
                eventIds.add(Long.valueOf(eventId));
            }
            if (eventIds.isEmpty()) {
                responseBody = new EventPlannerResponseBody("Unable to get events from empty eventId list");
                responseEntity = new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            } else {
                List<Event> events;
                if (UPCOMING.equals(strategy))
                    events = eventService.getUpcomingEventsFromIds(eventIds);
                else if (PAST.equals(strategy))
                    events = eventService.getPastEventsFromIds(eventIds);
                else
                    events = eventService.getEventsFromIds(eventIds);
                EventDTO [] eventDTOs = new EventDTO[events.size()];
                responseBody = new EventPlannerResponseBody(SUCCESS, eventMapper.toDtos(events).toArray(eventDTOs));
                responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
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

    @GetMapping("/getFromHostId")
    public ResponseEntity<EventPlannerResponseBody> getEventsFromHostId(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                                                        @RequestBody(required = false) String requestBody) {
        EventPlannerResponseBody responseBody;
        ResponseEntity<EventPlannerResponseBody> responseEntity;
        try{
            JSONObject requestBodyJson = new JSONObject(requestBody);
            Long hostId = requestBodyJson.getLong(HOST_ID);
            String strategy = requestBodyJson.optString(STRATEGY);
            List<Event> events;
            if (UPCOMING.equals(strategy))
                events = eventService.getUpcomingEventsFromHostId(hostId);
            else if (PAST.equals(strategy))
                events = eventService.getPastEventsFromHostId(hostId);
            else
                events = eventService.getEventsFromHostId(hostId);
            EventDTO [] eventDTOs = new EventDTO[events.size()];
            responseBody = new EventPlannerResponseBody(SUCCESS, eventMapper.toDtos(events).toArray(eventDTOs));
            responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
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


    @GetMapping("/getFromCity")
    public ResponseEntity<EventPlannerResponseBody> getEventsFromCity(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                                                      @RequestBody(required = false) String requestBody) {
        EventPlannerResponseBody responseBody;
        ResponseEntity<EventPlannerResponseBody> responseEntity;
        try{
            JSONObject requestBodyJson = new JSONObject(requestBody);
            String city = requestBodyJson.getString(CITY);
            String strategy = requestBodyJson.optString(STRATEGY);
            List<Event> events;
            if (UPCOMING.equals(strategy))
                events = eventService.getUpcomingEventsFromCity(city);
            else if (PAST.equals(strategy))
                events = eventService.getPastEventsFromCity(city);
            else
                events = eventService.getEventsFromCity(city);
            EventDTO [] eventDTOs = new EventDTO[events.size()];
            responseBody = new EventPlannerResponseBody(SUCCESS, eventMapper.toDtos(events).toArray(eventDTOs));
            responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
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

    @GetMapping("/all")
    public ResponseEntity<EventPlannerResponseBody> getAllEvents() {
        List<Event> events = eventService.getEvents();
        EventDTO [] eventDTOs = new EventDTO[events.size()];
        EventPlannerResponseBody responseBody = new EventPlannerResponseBody(SUCCESS, eventMapper.toDtos(events).toArray(eventDTOs));
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/attendees")
    public ResponseEntity<EventPlannerResponseBody> getEventAttendees(@RequestParam(value = "id", required = false) Long eventId,
                                                                      @RequestHeader(required = false) Map<String, String> requestHeaders,
                                                                      @RequestBody(required = false) String requestBody) {
        EventPlannerResponseBody responseBody;
        ResponseEntity<EventPlannerResponseBody> responseEntity;
        try {
            if (eventId == null && requestBody != null && !requestBody.isBlank()) {
                JSONObject requestBodyJson = new JSONObject(requestBody);
                eventId = requestBodyJson.getLong(EVENT_ID);
            }
            Event event = eventService.getEventFromId(eventId);
            List<User> users = event.getAttendees();
            UserDTO [] userDTOs = new UserDTO[users.size()];
            responseBody = new EventPlannerResponseBody(SUCCESS, userMapper.toDtos(users).toArray(userDTOs));
            responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (JSONException e) {
            responseBody = new EventPlannerResponseBody(e.getMessage());
            responseEntity = new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        } catch (EventServiceException e) {
            responseBody = new EventPlannerResponseBody(e.getMessage());
            responseEntity = new ResponseEntity<>(responseBody, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            responseBody = new EventPlannerResponseBody(e.getMessage());
            responseEntity = new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @GetMapping("/search")
    public ResponseEntity<EventPlannerResponseBody> searchEventByTitle(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                                                       @RequestBody(required = false) String requestBody) {
        EventPlannerResponseBody responseBody;
        ResponseEntity<EventPlannerResponseBody> responseEntity;
        try {
            JSONObject requestBodyJson = new JSONObject(requestBody);
            String title = requestBodyJson.getString(TITLE);
            String strategy = requestBodyJson.optString(STRATEGY);
            String searchType = requestBodyJson.optString(SEARCH_TYPE);
            List<Event> events;
            if (STARTS_WITH.equals(searchType)) {
                if (UPCOMING.equals(strategy))
                    events = eventService.getUpcomingEventsFromTitleStartingWith(title);
                else if (PAST.equals(strategy))
                    events = eventService.getPastEventsFromTitleStartingWith(title);
                else
                    events = eventService.getEventsFromTitleStartingWith(title);
            } else if (CONTAINING.equals(searchType)) {
                if (UPCOMING.equals(strategy))
                    events = eventService.getUpcomingEventsFromTitleContaining(title);
                else if (PAST.equals(strategy))
                    events = eventService.getPastEventsFromTitleContaining(title);
                else
                    events = eventService.getEventsFromTitleContaining(title);
            } else {
                if (UPCOMING.equals(strategy))
                    events = eventService.getUpcomingEventsFromTitle(title);
                else if (PAST.equals(strategy))
                    events = eventService.getPastEventsFromTitle(title);
                else
                    events = eventService.getEventsFromTitle(title);
            }
            EventDTO [] eventDTOs = new EventDTO[events.size()];
            responseBody = new EventPlannerResponseBody(SUCCESS, eventMapper.toDtos(events).toArray(eventDTOs));
            responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
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

    @PostMapping("/add")
    public ResponseEntity<EventPlannerResponseBody> postEvent(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                                              @RequestBody String requestBody) {
        EventPlannerResponseBody responseBody;
        ResponseEntity<EventPlannerResponseBody> responseEntity;
        try {
            if (requestBody == null || requestBody.isBlank()) {
                responseBody = new EventPlannerResponseBody("Cannot store null event");
                responseEntity = new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            } else {
                Event event = new Event();
                JSONObject requestBodyJson = new JSONObject(requestBody);
                if (invalidRequestBodyJson(requestBodyJson, HOST_ID)) {
                    responseBody = new EventPlannerResponseBody("Unable to add event with no host");
                    responseEntity = new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
                } else {
                    Long hostId = requestBodyJson.getLong(HOST_ID);
                    User host = userService.getUserFromId(hostId);
                    Optional.ofNullable(requestBodyJson.optString("title")).ifPresent(event::setTitle);
                    event.setDate(getDateFomDateString(requestBodyJson.optString("date")));
                    Optional.ofNullable(requestBodyJson.optJSONObject("location")).ifPresent(location -> event.setLocation(getLocationFromJson(location)));
                    Optional.ofNullable(requestBodyJson.optString("description")).ifPresent(event::setDescription);
                    Optional.ofNullable(requestBodyJson.optString("category")).ifPresent(event::setCategory);
                    Optional.of(requestBodyJson.optInt("maxAttendees")).ifPresent(event::setMaxAttendees);
                    Event storedEvent = eventService.storeEvent(host, event);
                    responseBody = new EventPlannerResponseBody(SUCCESS, eventMapper.toDto(storedEvent));
                    responseEntity = new ResponseEntity<>(responseBody, HttpStatus.CREATED);
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

    @PutMapping("/update")
    public ResponseEntity<EventPlannerResponseBody> updateEvent(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                                                @RequestBody String requestBody) {
        EventPlannerResponseBody responseBody;
        ResponseEntity<EventPlannerResponseBody> responseEntity;
        try {
            if (requestBody == null || requestBody.isBlank()) {
                responseBody = new EventPlannerResponseBody("Cannot update null event");
                responseEntity = new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            } else {
                Event updateEvent = new Event();
                JSONObject requestBodyJson = new JSONObject(requestBody);
                if (invalidRequestBodyJson(requestBodyJson, EVENT_ID)) {
                    responseBody = new EventPlannerResponseBody("Unable to update event with no eventId");
                    responseEntity = new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
                } else {
                    Long eventId = requestBodyJson.getLong(EVENT_ID);
                    Optional.ofNullable(requestBodyJson.optString("title")).ifPresent(updateEvent::setTitle);
                    updateEvent.setDate(getDateFomDateString(requestBodyJson.optString("date")));
                    Optional.ofNullable(requestBodyJson.optJSONObject("location")).ifPresent(location -> updateEvent.setLocation(getLocationFromJson(location)));
                    Optional.ofNullable(requestBodyJson.optString("description")).ifPresent(updateEvent::setDescription);
                    Optional.ofNullable(requestBodyJson.optString("category")).ifPresent(updateEvent::setCategory);
                    Optional.of(requestBodyJson.optInt("maxAttendees")).ifPresent(updateEvent::setMaxAttendees);
                    Event updatedEvent = eventService.updateEvent(eventId, updateEvent);
                    responseBody = new EventPlannerResponseBody(eventMapper.toDto(updatedEvent));
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

    @PutMapping("/addAttendee")
    public ResponseEntity<EventPlannerResponseBody> addEventAttendee(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                                                     @RequestBody String requestBody) {
        EventPlannerResponseBody responseBody;
        ResponseEntity<EventPlannerResponseBody> responseEntity;
        try {
            if (requestBody == null || requestBody.isBlank()) {
                responseBody = new EventPlannerResponseBody("Cannot add attendee to null event");
                responseEntity = new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            } else {
                JSONObject requestBodyJson = new JSONObject(requestBody);
                if (invalidRequestBodyJson(requestBodyJson, EVENT_ID, USER_ID)) {
                    responseBody = new EventPlannerResponseBody("Unable to add attendee to event with no eventId or userId");
                    responseEntity = new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
                } else {
                    Long eventId = requestBodyJson.getLong(EVENT_ID);
                    Long userId = requestBodyJson.getLong(USER_ID);
                    User attendee = userService.getUserFromId(userId);
                    if (attendee == null) {
                        responseBody = new EventPlannerResponseBody("Unable to fetch attendee from DB");
                        responseEntity = new ResponseEntity<>(responseBody, HttpStatus.NO_CONTENT);
                    } else {
                        Event updatedEvent = eventService.addEventAttendee(eventId, attendee);
                        responseBody = new EventPlannerResponseBody(eventMapper.toDto(updatedEvent));
                        responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
                    }
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
    public ResponseEntity<EventPlannerResponseBody> deleteEvent(@RequestParam(value = "id", required = false) Long eventId,
                                                                @RequestHeader(required = false) Map<String, String> requestHeaders,
                                                                @RequestBody(required = false) String requestBody) {
        EventPlannerResponseBody responseBody;
        ResponseEntity<EventPlannerResponseBody> responseEntity;
        try {
            if (!(requestBody == null || requestBody.isBlank())) {
                JSONObject requestBodyJson = new JSONObject(requestBody);
                eventId = requestBodyJson.getLong(EVENT_ID);
            }
            if (eventService.eventExists(eventId)) {
                eventService.removeEvent(eventId);
                responseBody = new EventPlannerResponseBody("Event " + eventId + " removed.");
                responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
            } else {
                responseBody = new EventPlannerResponseBody("Event " + eventId + " does not exist");
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

    private boolean invalidRequestBodyJson(JSONObject requestBodyJson, String... necessaryKeys) {
        boolean result = false;
        for (String key : necessaryKeys) {
            result = requestBodyJson.isEmpty() || !requestBodyJson.has(key) || requestBodyJson.get(key) == JSONObject.NULL;
        }
        return result;
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
