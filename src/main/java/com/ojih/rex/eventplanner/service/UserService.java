package com.ojih.rex.eventplanner.service;

import com.ojih.rex.eventplanner.model.User;
import com.ojih.rex.eventplanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserFromId(Long userId) {
        return userRepository.findDistinctByUserId(userId);
    }
}
