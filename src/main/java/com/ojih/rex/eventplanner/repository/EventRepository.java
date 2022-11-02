package com.ojih.rex.eventplanner.repository;

import com.ojih.rex.eventplanner.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
