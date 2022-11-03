package com.ojih.rex.eventplanner.repository;

import com.ojih.rex.eventplanner.model.Event;
import com.ojih.rex.eventplanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    public List<Event> findDistinctByEventId(Long eventId);
    public List<Event> findByTitle(String title);
    public List<Event> findByTitleOrderByDateDesc(String title);
    public List<Event> findByTitleContaining(String title);
    public List<Event> findByTitleContainingOrderByDateDesc(String title);
    public List<Event> findByDate(Date date);
    public List<Event> findByDateAfter(Date date);
    public List<Event> findByDateBefore(Date date);
    public List<Event> findByDateBetween(Date after, Date before);
    public List<Event> findByCategory(String category);
    public List<Event> findByCategoryOrderByDateDesc(String category);
    public List<Event> findByHost(User user);
    public List<Event> findByHostOrderByDateDesc(User user);
    public List<Event> findByLocationCity(String city);
    public List<Event> findByLocationCityOrderByDateDesc(String city);
    public List<Event> findByLocationState(String state);
    public List<Event> findByLocationStateOrderByDateDesc(String state);
    public List<Event> findByLocationPostalCode(String postalCOde);
    public List<Event> findByLocationPostalCodeOrderByDateDesc(String postalCode);
    public List<Event> findByLocationCityOrLocationState(String city, String state);
    public List<Event> findByLocationCityOrLocationStateOrderByDateDesc(String city, String state);
    public List<Event> findByLocationCityAndLocationState(String city, String state);
    public List<Event> findByLocationCityAndLocationStateOrderByDateDesc(String city, String state);
    public List<Event> findByLocationCityOrLocationPostalCode(String city, String postalCode);
    public List<Event> findByLocationCityOrLocationPostalCodeOrderByDateDesc(String city, String postalCode);
    public List<Event> findByLocationCityAndLocationPostalCode(String city, String postalCode);
    public List<Event> findByLocationCityAndLocationPostalCodeOrderByDateDesc(String city, String postalCode);
    public List<Event> findByLocationStateOrLocationPostalCode(String state, String postalCode);
    public List<Event> findByLocationStateOrLocationPostalCodeOrderByDateDesc(String state, String postalCode);
    public List<Event> findByLocationStateAndLocationPostalCode(String state, String postalCode);
    public List<Event> findByLocationStateAndLocationPostalCodeOrderByDateDesc(String state, String postalCode);
}
