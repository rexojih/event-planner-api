package com.ojih.rex.eventplanner.repository;

import com.ojih.rex.eventplanner.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Event findDistinctByEventId(Long eventId);
    List<Event> findByEventIdIn(List<Long> eventIds);
    List<Event> findByEventIdInAndDateAfter(List<Long> eventIds, Date after);
    List<Event> findByEventIdInAndDateBefore(List<Long> eventIds, Date before);
    List<Event> findByTitle(String title);
    List<Event> findByTitleAndDateAfterOrderByDateAsc(String title, Date after);
    List<Event> findByTitleAndDateBeforeOrderByDateDesc(String title, Date after);
    List<Event> findByTitleStartsWith(String title);
    List<Event> findByTitleStartsWithAndDateAfterOrderByDateAsc(String title, Date after);
    List<Event> findByTitleStartsWithAndDateBeforeOrderByDateDesc(String title, Date before);
    List<Event> findByDateAfterOrderByDateAsc(Date date);
    List<Event> findByDateBeforeOrderByDateDesc(Date date);
    List<Event> findByDateBetweenOrderByDateAsc(Date after, Date before);
    List<Event> findByCategory(String category);
    List<Event> findByCategoryAndDateAfterOrderByDateAsc(String category, Date after);
    List<Event> findByCategoryAndDateBeforeOrderByDateDesc(String category, Date before);
    List<Event> findByHostId(Long hostId);
    List<Event> findByHostIdAndDateAfterOrderByDateAsc(Long hostId, Date after);
    List<Event> findByHostIdAndDateBeforeOrderByDateDesc(Long hostId, Date before);
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
    @Modifying
    @Query("delete from Event e where e.id in ?1")
    void deleteEventsWithId(List<Long> eventIds);
}
