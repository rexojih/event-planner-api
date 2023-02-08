package com.ojih.rex.eventplanner.controller;

import com.ojih.rex.eventplanner.annotation.DataQuality;
import com.ojih.rex.eventplanner.model.Event;
import com.ojih.rex.eventplanner.model.User;
import com.ojih.rex.eventplanner.model.dto.EventDTO;
import com.ojih.rex.eventplanner.model.dto.UserDTO;
import com.ojih.rex.eventplanner.model.request.EventPlannerRequest;
import com.ojih.rex.eventplanner.model.response.EventPlannerMessage;
import com.ojih.rex.eventplanner.model.response.EventPlannerResponse;
import com.ojih.rex.eventplanner.service.EventService;
import com.ojih.rex.eventplanner.service.UserService;
import com.ojih.rex.eventplanner.util.Mapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static com.ojih.rex.eventplanner.constant.EventPlannerConstants.*;

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
    @ResponseBody
    @SneakyThrows
    public EventPlannerResponse getEvent(@RequestParam(value = "id") Long eventId,
                                         @RequestHeader(required = false) Map<String, String> requestHeaders) {
        Event event = eventService.getEventFromId(eventId);
        return EventPlannerResponse.builder()
                .eventPlannerMessage(new EventPlannerMessage(SUCCESS, eventMapper.toDTO(event)))
                .build();
    }

    @PostMapping("/getFromIds")
    public EventPlannerResponse getEventsFromIds(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                                 @RequestBody EventPlannerRequest request) {
        List<Event> events;
        if (UPCOMING.equals(request.getStrategy()))
            events = eventService.getUpcomingEventsFromIds(request.getEventIds());
        else if (PAST.equals(request.getStrategy()))
            events = eventService.getPastEventsFromIds(request.getEventIds());
        else
            events = eventService.getEventsFromIds(request.getEventIds());
        EventDTO[] eventDTOs = new EventDTO[events.size()];
        return EventPlannerResponse.builder()
                .eventPlannerMessage(new EventPlannerMessage(SUCCESS, eventMapper.toDTOs(events).toArray(eventDTOs)))
                .build();
    }

    @GetMapping("/getFromHostId")
    @ResponseBody
    public EventPlannerResponse getEventsFromHostId(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                                    @RequestParam(value = "id") Long hostId,
                                                    @RequestParam(value = "strategy") String strategy) {
        List<Event> events;
        if (UPCOMING.equals(strategy))
            events = eventService.getUpcomingEventsFromHostId(hostId);
        else if (PAST.equals(strategy))
            events = eventService.getPastEventsFromHostId(hostId);
        else
            events = eventService.getEventsFromHostId(hostId);
        EventDTO[] eventDTOs = new EventDTO[events.size()];
        return EventPlannerResponse.builder()
                .eventPlannerMessage(new EventPlannerMessage(SUCCESS, eventMapper.toDTOs(events).toArray(eventDTOs)))
                .build();
    }


    @GetMapping("/getFromCity")
    @ResponseBody
    public EventPlannerResponse getEventsFromCity(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                                  @RequestParam(value = "city") String city,
                                                  @RequestParam(value = "strategy") String strategy) {
        List<Event> events;
        if (UPCOMING.equals(strategy))
            events = eventService.getUpcomingEventsFromCity(city);
        else if (PAST.equals(strategy))
            events = eventService.getPastEventsFromCity(city);
        else
            events = eventService.getEventsFromCity(city);
        EventDTO[] eventDTOs = new EventDTO[events.size()];
        return EventPlannerResponse.builder()
                .eventPlannerMessage(new EventPlannerMessage(SUCCESS, eventMapper.toDTOs(events).toArray(eventDTOs)))
                .build();
    }

    @GetMapping("/all")
    public EventPlannerResponse getAllEvents() {
        List<Event> events = eventService.getEvents();
        EventDTO[] eventDTOs = new EventDTO[events.size()];
        return EventPlannerResponse.builder()
                .eventPlannerMessage(new EventPlannerMessage(SUCCESS, eventMapper.toDTOs(events).toArray(eventDTOs)))
                .build();
    }

    @GetMapping("/attendees")
    @ResponseBody
    @SneakyThrows
    public EventPlannerResponse getEventAttendees(@RequestParam(value = "id", required = false) Long eventId,
                                                  @RequestHeader(required = false) Map<String, String> requestHeaders) {
        Event event = eventService.getEventFromId(eventId);
        List<User> users = event.getAttendees();
        UserDTO[] userDTOs = new UserDTO[users.size()];
        return EventPlannerResponse.builder()
                .eventPlannerMessage(new EventPlannerMessage(SUCCESS, userMapper.toDTOs(users).toArray(userDTOs)))
                .build();
    }

    @PostMapping("/search")
    @ResponseBody
    public EventPlannerResponse searchEventByTitle(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                                   @RequestBody(required = false) EventPlannerRequest request) {
        List<Event> events;
        if (STARTS_WITH.equals(request.getSearchType())) {
            if (UPCOMING.equals(request.getStrategy()))
                events = eventService.getUpcomingEventsFromTitleStartingWith(request.getTitle());
            else if (PAST.equals(request.getStrategy()))
                events = eventService.getPastEventsFromTitleStartingWith(request.getTitle());
            else
                events = eventService.getEventsFromTitleStartingWith(request.getTitle());
        } else if (CONTAINING.equals(request.getSearchType())) {
            if (UPCOMING.equals(request.getStrategy()))
                events = eventService.getUpcomingEventsFromTitleContaining(request.getTitle());
            else if (PAST.equals(request.getStrategy()))
                events = eventService.getPastEventsFromTitleContaining(request.getTitle());
            else
                events = eventService.getEventsFromTitleContaining(request.getTitle());
        } else {
            if (UPCOMING.equals(request.getStrategy()))
                events = eventService.getUpcomingEventsFromTitle(request.getTitle());
            else if (PAST.equals(request.getStrategy()))
                events = eventService.getPastEventsFromTitle(request.getTitle());
            else
                events = eventService.getEventsFromTitle(request.getTitle());
        }
        EventDTO[] eventDTOs = new EventDTO[events.size()];
        return EventPlannerResponse.builder()
                .eventPlannerMessage(new EventPlannerMessage(SUCCESS, eventMapper.toDTOs(events).toArray(eventDTOs)))
                .build();
    }

    @PostMapping("/add")
    @ResponseBody
    @SneakyThrows
    @DataQuality(requestType = "createEvent")
    @ResponseStatus(HttpStatus.CREATED)
    public EventPlannerResponse createEvent(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                            @RequestBody EventPlannerRequest request) {
        User host = userService.getUserFromId(request.getHostId());
        Event event = Event.builder()
                .title(request.getTitle())
                .date(getDateFomDateString(request.getDate()))
                .location(request.getLocation())
                .description(request.getDescription())
                .category(request.getCategory())
                .maxAttendees(request.getMaxAttendees())
                .build();
        Event storedEvent = eventService.storeEvent(host, event);
        return EventPlannerResponse.builder()
                .eventPlannerMessage(new EventPlannerMessage(SUCCESS, eventMapper.toDTO(storedEvent)))
                .build();
    }

    @PutMapping("/update")
    @ResponseBody
    @SneakyThrows
    @DataQuality(requestType = "updateEvent")
    public EventPlannerResponse updateEvent(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                            @RequestBody EventPlannerRequest request) {
        Event updateEvent = Event.builder()
                .title(request.getTitle())
                .date(getDateFomDateString(request.getDate()))
                .location(request.getLocation())
                .description(request.getDescription())
                .category(request.getCategory())
                .maxAttendees(request.getMaxAttendees())
                .build();
        Event updatedEvent = eventService.updateEvent(request.getEventId(), updateEvent);
        return EventPlannerResponse.builder()
                .eventPlannerMessage(new EventPlannerMessage(eventMapper.toDTO(updatedEvent)))
                .build();
    }

    @PutMapping("/addAttendee")
    @ResponseBody
    @SneakyThrows
    @DataQuality(requestType = "addEventAttendee")
    public EventPlannerResponse addEventAttendee(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                                 @RequestBody EventPlannerRequest request) {
        User attendee = userService.getUserFromId(request.getUserId());
        Event updatedEvent = eventService.addEventAttendee(request.getEventId(), attendee);
        return EventPlannerResponse.builder()
                .eventPlannerMessage(new EventPlannerMessage(eventMapper.toDTO(updatedEvent)))
                .build();
    }

    @DeleteMapping("/delete")
    @ResponseBody
    @SneakyThrows
    public EventPlannerResponse deleteEvent(@RequestParam(value = "id") Long eventId,
                                            @RequestHeader(required = false) Map<String, String> requestHeaders) {
        eventService.removeEvent(eventId);
        return EventPlannerResponse.builder()
                .eventPlannerMessage(new EventPlannerMessage("Event " + eventId + " removed."))
                .build();
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
}
