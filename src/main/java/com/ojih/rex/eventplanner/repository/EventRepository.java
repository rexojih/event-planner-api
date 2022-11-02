package com.ojih.rex.eventplanner.repository;

import com.ojih.rex.eventplanner.model.Event;
import com.ojih.rex.eventplanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    public List<Event> findByEventId(Long eventId);
    public List<Event> findByTitle(String title);
    public List<Event> findByDate(Date date);
    public List<Event> findByCategory(String category);
    public List<Event> findByHost(User user);
}
