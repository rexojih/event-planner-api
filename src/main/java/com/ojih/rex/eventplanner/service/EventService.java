package com.ojih.rex.eventplanner.service;

import com.ojih.rex.eventplanner.exception.EventServiceException;
import com.ojih.rex.eventplanner.model.Location;
import com.ojih.rex.eventplanner.model.Event;
import com.ojih.rex.eventplanner.model.User;
import com.ojih.rex.eventplanner.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private static final String NOT_FOUND = " not found in DB";

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event storeEvent(User host, Event event) {
        event.setHostId(host.getUserId());
        event.addAttendee(host);
        return eventRepository.save(event);
    }

    public Event updateEvent(Long eventId, Event updateEvent) throws EventServiceException {
        Event event = eventRepository.findDistinctByEventId(eventId);
        if (event == null)
            throw new EventServiceException("Unable to update event. EventId " + eventId + NOT_FOUND);
        mapUpdate(event, updateEvent.getTitle(), updateEvent.getDate(), updateEvent.getLocation(), updateEvent.getDescription(), updateEvent.getCategory(), updateEvent.getMaxAttendees());
        return eventRepository.save(event);
    }

    private void mapUpdate(Event event, String newTitle, Date newDate, Location newLocation, String newDescription, String newCategory, Integer newMaxAttendees) {
        if (newTitle != null)
            event.setTitle(newTitle);
        if (newDate != null)
            event.setDate(newDate);
        if (newLocation != null)
            event.setLocation(newLocation);
        if (newDescription != null)
            event.setDescription(newDescription);
        if (newCategory != null)
            event.setCategory(newCategory);
        if (newMaxAttendees != null && validMaxAttendees(newMaxAttendees, event))
            event.setMaxAttendees(newMaxAttendees);
    }

    private boolean validMaxAttendees(Integer newMaxAttendees, Event event) {
        return newMaxAttendees > 0 && newMaxAttendees >= event.getAttendees().size();
    }

    public Event addEventAttendee(Long eventId, User newAttendee) throws EventServiceException {
        Event event = eventRepository.findDistinctByEventId(eventId);
        if (event == null)
            throw new EventServiceException("Unable to add attendee. EventId " + eventId + NOT_FOUND);
        event.addAttendee(newAttendee);
        return eventRepository.save(event);
    }

    public boolean eventExists(Long eventId) {
        return eventRepository.existsById(eventId);
    }

    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    public Event getEventFromId(Long eventId) throws EventServiceException {
        Event event = eventRepository.findDistinctByEventId(eventId);
        if (event == null)
            throw new EventServiceException("Unable to get event. EventId " + eventId + NOT_FOUND);
        return event;
    }

    public List<Event> getEventsFromIds(List<Long> eventIds) throws EventServiceException {
        List<Event> events = eventRepository.findByEventIdIn(eventIds);
        if (events.isEmpty())
            throw new EventServiceException("Unable to get events. No events found with eventIds: " + eventIds);
        return events;
    }

    public List<Event> getUpcomingEventsFromIds(List<Long> eventIds) throws EventServiceException {
        List<Event> events = eventRepository.findByEventIdInAndDateAfter(eventIds, new Date());
        if (events.isEmpty())
            throw new EventServiceException("Unable to get events. No events found with eventIds: " + eventIds);
        return events;
    }

    public List<Event> getPastEventsFromIds(List<Long> eventIds) throws EventServiceException {
        List<Event> events = eventRepository.findByEventIdInAndDateBefore(eventIds, new Date());
        if (events.isEmpty())
            throw new EventServiceException("Unable to get events. No events found with eventIds: " + eventIds);
        return events;
    }

    public List<Event> getEventsFromHostId(Long hostId) {
        return eventRepository.findByHostId(hostId);
    }

    public List<Event> getEventsFromHostByDate(Long hostId) {
        return eventRepository.findByHostIdOrderByDateDesc(hostId);
    }

    public List<Event> getEventsFromTitle(String title) {
        return eventRepository.findByTitle(title);
    }

    public List<Event> getEventsFromTitleByDate(String title) {
        return eventRepository.findByTitleOrderByDateDesc(title);
    }

    public List<Event> getEventsFromTitleContaining(String title) {
        return eventRepository.findByTitleContaining(title);
    }

    public List<Event> getEventsFromTitleContainingByDate(String title) {
        return eventRepository.findByTitleContainingOrderByDateDesc(title);
    }

    public List<Event> getEventsFromDate(Date date) {
        return eventRepository.findByDate(date);
    }

    public List<Event> getEventsFromAfterDate(Date date) {
        return eventRepository.findByDateAfter(date);
    }

    public List<Event> getEventsFromBeforeDate(Date date) {
        return eventRepository.findByDateBefore(date);
    }

    public List<Event> getEventsFromBetweenDates(Date after, Date before) {
        return eventRepository.findByDateBetween(after, before);
    }

    public List<Event> getEventsFromCategory(String category) {
        return eventRepository.findByCategory(category);
    }

    public List<Event> getEventsFromCategoryByDate(String category) {
        return eventRepository.findByCategoryOrderByDateDesc(category);
    }

    public List<Event> getEventsFromCity(String city) {
        return eventRepository.findByLocationCity(city);
    }

    public List<Event> getEventsFromCityByDate(String city) {
        return eventRepository.findByLocationCityOrderByDateDesc(city);
    }

    public List<Event> getEventsFromState(String state) {
        return eventRepository.findByLocationState(state);
    }

    public List<Event> getEventsFromStateByDate(String state) {
        return eventRepository.findByLocationStateOrderByDateDesc(state);
    }

    public List<Event> getEventsFromPostalCode(String postalCode) {
        return eventRepository.findByLocationPostalCode(postalCode);
    }

    public List<Event> getEventsFromPostalCodeByDate(String postalCode) {
        return eventRepository.findByLocationPostalCodeOrderByDateDesc(postalCode);
    }

    public List<Event> getEventsFromCityOrState(String city, String state) {
        return eventRepository.findByLocationCityOrLocationState(city, state);
    }

    public List<Event> getEventsFromCityOrStateByDate(String city, String state) {
        return eventRepository.findByLocationCityOrLocationStateOrderByDateDesc(city, state);
    }

    public List<Event> getEventsFromCityAndState(String city, String state) {
        return eventRepository.findByLocationCityAndLocationState(city, state);
    }

    public List<Event> getEventsFromCityAndStateByDate(String city, String state) {
        return eventRepository.findByLocationCityAndLocationStateOrderByDateDesc(city, state);
    }

    public List<Event> getEventsFromCityOrPostalCode(String city, String postalCode) {
        return eventRepository.findByLocationCityOrLocationPostalCode(city, postalCode);
    }

    public List<Event> getEventsFromCityOrPostalCodeByDate(String city, String postalCode) {
        return eventRepository.findByLocationCityOrLocationPostalCodeOrderByDateDesc(city, postalCode);
    }

    public List<Event> getEventsFromCityAndPostalCode(String city, String postalCode) {
        return eventRepository.findByLocationCityAndLocationPostalCode(city, postalCode);
    }

    public List<Event> getEventsFromCityAndPostalCodeByDate(String city, String postalCode) {
        return eventRepository.findByLocationCityAndLocationPostalCodeOrderByDateDesc(city, postalCode);
    }

    public List<Event> getEventsFromStateOrPostalCode(String state, String postalCode) {
        return eventRepository.findByLocationStateOrLocationPostalCode(state, postalCode);
    }

    public List<Event> getEventsFromStateOrPostalCodeByDate(String state, String postalCode) {
        return eventRepository.findByLocationStateOrLocationPostalCodeOrderByDateDesc(state, postalCode);
    }

    public List<Event> getEventsFromStateAndPostalCode(String state, String postalCode) {
        return eventRepository.findByLocationStateAndLocationPostalCode(state, postalCode);
    }

    public List<Event> getEventsFromStateAndPostalCodeByDate(String state, String postalCode) {
        return eventRepository.findByLocationStateAndLocationPostalCodeOrderByDateDesc(state, postalCode);
    }

    public void removeEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }

    @Transactional
    public void removeEvents(List<Long> eventIds) {
        eventRepository.deleteEventsWithId(eventIds);
    }
}
