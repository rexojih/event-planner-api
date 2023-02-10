package com.ojih.rex.eventplanner.controller;

import com.ojih.rex.eventplanner.annotation.DataQuality;
import com.ojih.rex.eventplanner.handler.RequestHandler;
import com.ojih.rex.eventplanner.model.Location;
import com.ojih.rex.eventplanner.model.request.EventPlannerRequest;
import com.ojih.rex.eventplanner.model.response.EventPlannerResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

import static com.ojih.rex.eventplanner.constant.EventPlannerConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event")
public class EventController {

    private final RequestHandler handler;

    @GetMapping("/get")
    @ResponseBody
    @SneakyThrows
    public EventPlannerResponse getEvent(@RequestParam(value = "id") Long eventId,
                                         @RequestHeader(required = false) Map<String, String> requestHeaders) {
        return handler.handleRequest(EventPlannerRequest.builder()
                .eventId(eventId)
                .build(), GET_EVENT);
    }

    @PostMapping("/getFromIds")
    @ResponseBody
    @SneakyThrows
    @DataQuality(requestType = GET_EVENTS_FROM_IDS)
    public EventPlannerResponse getEventsFromIds(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                                 @RequestBody EventPlannerRequest request) {
        return handler.handleRequest(request, GET_EVENTS_FROM_IDS);
    }

    @GetMapping("/getFromHostId")
    @ResponseBody
    @SneakyThrows
    public EventPlannerResponse getEventsFromHostId(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                                    @RequestParam(value = "id") Long hostId,
                                                    @RequestParam(value = "strategy") String strategy) {
        return handler.handleRequest(EventPlannerRequest.builder()
                .hostId(hostId)
                .strategy(strategy)
                .build(), GET_EVENTS_FROM_HOST_ID);
    }


    @GetMapping("/getFromCity")
    @ResponseBody
    public EventPlannerResponse getEventsFromCity(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                                  @RequestParam(value = "city") String city,
                                                  @RequestParam(value = "strategy") String strategy) throws ParseException {
        return handler.handleRequest(EventPlannerRequest.builder()
                .location(Location.builder()
                        .city(city)
                        .build())
                .build(), GET_EVENTS_FROM_CITY);
    }

    @GetMapping("/all")
    @ResponseBody
    public EventPlannerResponse getEvents() throws ParseException {
        return handler.handleRequest(null, GET_EVENTS);
    }

    @GetMapping("/attendees")
    @ResponseBody
    @SneakyThrows
    public EventPlannerResponse getEventAttendees(@RequestParam(value = "id", required = false) Long eventId,
                                                  @RequestHeader(required = false) Map<String, String> requestHeaders) {
        return handler.handleRequest(EventPlannerRequest.builder()
                .eventId(eventId)
                .build(), GET_EVENT_ATTENDEES);
    }

    @PostMapping("/search")
    @ResponseBody
    @SneakyThrows
    @DataQuality(requestType = SEARCH_EVENTS_BY_TITLE)
    public EventPlannerResponse searchEventsByTitle(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                                    @RequestBody(required = false) EventPlannerRequest request) {
        return handler.handleRequest(request, SEARCH_EVENTS_BY_TITLE);
    }

    @PostMapping("/add")
    @ResponseBody
    @SneakyThrows
    @DataQuality(requestType = CREATE_EVENT)
    @ResponseStatus(HttpStatus.CREATED)
    public EventPlannerResponse createEvent(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                            @RequestBody EventPlannerRequest request) {
        return handler.handleRequest(request, CREATE_EVENT);
    }

    @PutMapping("/update")
    @ResponseBody
    @SneakyThrows
    @DataQuality(requestType = UPDATE_EVENT)
    public EventPlannerResponse updateEvent(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                            @RequestBody EventPlannerRequest request) {
        return handler.handleRequest(request, UPDATE_EVENT);
    }

    @PutMapping("/addAttendee")
    @ResponseBody
    @SneakyThrows
    @DataQuality(requestType = ADD_EVENT_ATTENDEE)
    public EventPlannerResponse addEventAttendee(@RequestHeader(required = false) Map<String, String> requestHeaders,
                                                 @RequestBody EventPlannerRequest request) {
        return handler.handleRequest(request, ADD_EVENT_ATTENDEE);
    }

    @DeleteMapping("/delete")
    @ResponseBody
    @SneakyThrows
    public EventPlannerResponse deleteEvent(@RequestParam(value = "id") Long eventId,
                                            @RequestHeader(required = false) Map<String, String> requestHeaders) {
        return handler.handleRequest(EventPlannerRequest.builder()
                .eventId(eventId)
                .build(), DELETE_EVENT);
    }


}
