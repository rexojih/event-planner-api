package com.ojih.rex.eventplanner.service;

import com.ojih.rex.eventplanner.exception.EventServiceException;
import com.ojih.rex.eventplanner.model.Event;
import com.ojih.rex.eventplanner.model.Location;
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
        return newMaxAttendees > event.getAttendees().size();
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

    public List<Event> getEventsFromIds(List<Long> eventIds) {
        return eventRepository.findByEventIdIn(eventIds);
    }

    public List<Event> getUpcomingEventsFromIds(List<Long> eventIds) {
        return eventRepository.findByEventIdInAndDateAfter(eventIds, new Date());
    }

    public List<Event> getPastEventsFromIds(List<Long> eventIds) {
        return eventRepository.findByEventIdInAndDateBefore(eventIds, new Date());
    }

    public List<Event> getEventsFromHostId(Long hostId) {
        return eventRepository.findByHostId(hostId);
    }

    public List<Event> getUpcomingEventsFromHostId(Long hostId) {
        return eventRepository.findByHostIdAndDateAfterOrderByDateAsc(hostId, new Date());
    }

    public List<Event> getPastEventsFromHostId(Long hostId) {
        return eventRepository.findByHostIdAndDateBeforeOrderByDateDesc(hostId, new Date());
    }

    public List<Event> getEventsFromTitle(String title) {
        return eventRepository.findByTitle(title);
    }

    public List<Event> getUpcomingEventsFromTitle(String title) {
        return eventRepository.findByTitleAndDateAfterOrderByDateAsc(title, new Date());
    }

    public List<Event> getPastEventsFromTitle(String title) {
        return eventRepository.findByTitleAndDateBeforeOrderByDateDesc(title, new Date());
    }

    public List<Event> getEventsFromTitleStartingWith(String title) {
        return eventRepository.findByTitleStartsWith(title);
    }

    public List<Event> getUpcomingEventsFromTitleStartingWith(String title) {
        return eventRepository.findByTitleStartsWithAndDateAfterOrderByDateAsc(title, new Date());
    }

    public List<Event> getPastEventsFromTitleStartingWith(String title) {
        return eventRepository.findByTitleStartsWithAndDateBeforeOrderByDateDesc(title, new Date());
    }

    public List<Event> getEventsFromAfterDate(Date date) {
        return eventRepository.findByDateAfterOrderByDateAsc(date);
    }

    public List<Event> getEventsFromBeforeDate(Date date) {
        return eventRepository.findByDateBeforeOrderByDateDesc(date);
    }

    public List<Event> getEventsFromBetweenDates(Date after, Date before) {
        return eventRepository.findByDateBetweenOrderByDateAsc(after, before);
    }

    public List<Event> getEventsFromCategory(String category) {
        return eventRepository.findByCategory(category);
    }

    public List<Event> getUpcomingEventsFromCategoryByDate(String category) {
        return eventRepository.findByCategoryAndDateAfterOrderByDateAsc(category, new Date());
    }

    public List<Event> getPastEventsFromCategoryByDate(String category) {
        return eventRepository.findByCategoryAndDateBeforeOrderByDateDesc(category, new Date());
    }

    public List<Event> getEventsFromCity(String city) {
        return eventRepository.findByLocationCity(city);
    }

    public List<Event> getEventsFromCityAfterDate(String city) {
        return eventRepository.findByLocationCityAndDateAfterOrderByDateAsc(city, new Date());
    }

    public List<Event> getEventsFromCityBeforeDate(String city) {
        return eventRepository.findByLocationCityAndDateBeforeOrderByDateDesc(city, new Date());
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
