package com.ojih.rex.eventplanner.repository;

import com.ojih.rex.eventplanner.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Event findDistinctByEventId(Long eventId);
    List<Event> findByEventIdIn(List<Long> eventIds);
    List<Event> findByTitle(String title);
    List<Event> findByTitleOrderByDateDesc(String title);
    List<Event> findByTitleContaining(String title);
    List<Event> findByTitleContainingOrderByDateDesc(String title);
    List<Event> findByDate(Date date);
    List<Event> findByDateAfter(Date date);
    List<Event> findByDateBefore(Date date);
    List<Event> findByDateBetween(Date after, Date before);
    List<Event> findByCategory(String category);
    List<Event> findByCategoryOrderByDateDesc(String category);
    List<Event> findByHostId(Long hostId);
    List<Event> findByHostIdOrderByDateDesc(Long hostId);
    List<Event> findByLocationCity(String city);
    List<Event> findByLocationCityOrderByDateDesc(String city);
    List<Event> findByLocationState(String state);
    List<Event> findByLocationStateOrderByDateDesc(String state);
    List<Event> findByLocationPostalCode(String postalCOde);
    List<Event> findByLocationPostalCodeOrderByDateDesc(String postalCode);
    List<Event> findByLocationCityOrLocationState(String city, String state);
    List<Event> findByLocationCityOrLocationStateOrderByDateDesc(String city, String state);
    List<Event> findByLocationCityAndLocationState(String city, String state);
    List<Event> findByLocationCityAndLocationStateOrderByDateDesc(String city, String state);
    List<Event> findByLocationCityOrLocationPostalCode(String city, String postalCode);
    List<Event> findByLocationCityOrLocationPostalCodeOrderByDateDesc(String city, String postalCode);
    List<Event> findByLocationCityAndLocationPostalCode(String city, String postalCode);
    List<Event> findByLocationCityAndLocationPostalCodeOrderByDateDesc(String city, String postalCode);
    List<Event> findByLocationStateOrLocationPostalCode(String state, String postalCode);
    List<Event> findByLocationStateOrLocationPostalCodeOrderByDateDesc(String state, String postalCode);
    List<Event> findByLocationStateAndLocationPostalCode(String state, String postalCode);
    List<Event> findByLocationStateAndLocationPostalCodeOrderByDateDesc(String state, String postalCode);
}
