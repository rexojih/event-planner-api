package com.ojih.rex.eventplanner.repository;

import com.ojih.rex.eventplanner.model.CategoryConstants;
import com.ojih.rex.eventplanner.model.Event;
import com.ojih.rex.eventplanner.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    public void saveEvent() {
        User user = new User("rex.ojih", "Rex", "Ojih", "rex.ojih@gmail.com", "fooBar", "Houston");

        Event event = new Event("SSBU Tournament", new Date(), "2511 W BRAKER LN, AUSTIN TX, 78785", CategoryConstants.GAMING, user);

        eventRepository.save(event);
    }
}