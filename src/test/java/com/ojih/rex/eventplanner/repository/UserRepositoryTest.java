package com.ojih.rex.eventplanner.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertNotNull;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void userRepositoryShouldNotBeNull() {
        assertNotNull("UserRepository should not be null", userRepository);
    }
}