package com.ojih.rex.eventplanner.utilities;

import com.ojih.rex.eventplanner.model.event.Event;
import com.ojih.rex.eventplanner.model.Location;
import com.ojih.rex.eventplanner.model.User;
import com.ojih.rex.eventplanner.model.constants.CategoryConstants;
import com.ojih.rex.eventplanner.repository.EventRepository;
import com.ojih.rex.eventplanner.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class DataBasePopulator {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;

    @Test
    public void populateDatabase() {
        Location homeAddress = new Location("2511 W BRAKER LN", "AUSTIN", "TX", "78758");
        Location eventLocation = new Location("8909 Burnet Rd", "Austin", "TX", "78757");
        User user0 = new User("rex.ojih", "Rex", "Ojih", "rex.ojih@gmail.com", "foo");
        User user1 = new User("darian.gibson", "Darian", "Gibson", "darian.gibson@gmail.com", "bar", homeAddress);
        User user2 = new User("steven.watkins", "Steven", "Watkins", "steven.watkins@gmail.com", "baz", homeAddress);
        User user3 = new User("jasmine.gibson", "Jasmin", "Gibson", "jasmine.gibson@gmail.com", "123", homeAddress);
        User user4 = new User("navaeh.gibson", "Navaeh", "Gibson", "navaeh.gibson@gmail.com", "321", homeAddress);
        List<User> users1 = List.of(user0, user1);
        List<User> users2 = List.of(user2, user3);
        Event event = new Event("Bowling Night", new Date(), eventLocation, "Fun night of bowling! Come Join!", CategoryConstants.SPORTS, 5);
        event.setHost(user0);
        event.setAttendees(users1);
        event.addAttendees(users2);
        event.addAttendee(user4);
        eventRepository.save(event);
    }

    @Test
    public void dropDatabaseTables() {
        eventRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }
}
