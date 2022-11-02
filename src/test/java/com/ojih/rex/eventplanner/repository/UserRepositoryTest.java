package com.ojih.rex.eventplanner.repository;

import com.ojih.rex.eventplanner.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveUser() {
        User user = new User("rex.ojih", "Rex", "Ojih", "rex.ojih@gmail.com", "fooBar", "Houston");

        userRepository.save(user);
    }
}