package com.ojih.rex.eventplanner.service;

import com.ojih.rex.eventplanner.model.Event;
import com.ojih.rex.eventplanner.model.User;
import com.ojih.rex.eventplanner.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EventService {

    private EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void createEvent(User host, Event event) {
        event.setHost(host);
        eventRepository.save(event);
    }

    public void createEventWithAttendees(User host, Event event, List<User> attendees) {
        event.setHost(host);
        event.addAttendees(attendees);
        eventRepository.save(event);
    }

    public List<Event> getEventFromId(Long eventId) {
        return eventRepository.findDistinctByEventId(eventId);
    }

    public List<Event> getEventsFromHost(User host) {
        return eventRepository.findByHost(host);
    }

    public List<Event> getEventsFromHostByDate(User host) {
        return eventRepository.findByHostOrderByDateDesc(host);
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
}
