package com.ojih.rex.eventplanner.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertNotNull;

@SpringBootTest
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    public void eventRepositoryShouldNotBeNull() {
        assertNotNull("EventRepository should not be null", eventRepository);
    }
}