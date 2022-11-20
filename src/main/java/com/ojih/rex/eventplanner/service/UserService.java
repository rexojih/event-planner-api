package com.ojih.rex.eventplanner.service;

import com.ojih.rex.eventplanner.model.user.User;
import com.ojih.rex.eventplanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserFromId(Long userId) {
        return userRepository.findDistinctByUserId(userId);
    }

    public User storeUser(User user) {
        return userRepository.save(user);
    }

    public boolean userExists(Long userId) {
        return userRepository.existsById(userId);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void removeUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
