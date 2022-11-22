package com.ojih.rex.eventplanner.service;

import com.ojih.rex.eventplanner.exception.UserServiceException;
import com.ojih.rex.eventplanner.model.Event;
import com.ojih.rex.eventplanner.model.Location;
import com.ojih.rex.eventplanner.model.User;
import com.ojih.rex.eventplanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserFromId(Long userId) throws UserServiceException {
        User user = userRepository.findDistinctByUserId(userId);
        if (user == null)
            throw new UserServiceException("Unable to get user. UserId " + userId +" not found");
        return user;
    }

    public List<User> getUserFromNameStartingWith(String name) {
        List<User> usersByUserName = userRepository.findByUserNameStartsWith(name);
        List<User> usersByFistName = userRepository.findByLastNameStartsWith(name);
        List<User> usersByLastName = userRepository.findByFirstNameStartsWith(name);
        return merge(usersByUserName, usersByLastName, usersByFistName);
    }

    @SafeVarargs
    private List<User> merge(List<User>... userLists) {
        List<User> users = new ArrayList<>(userLists[0]);
        for (int i = 1; i < userLists.length; i++) {
            for (User user : userLists[i]) {
                if (!users.contains(user))
                    users.add(user);
            }
        }
        return users;
    }

    public User storeUser(User user) throws UserServiceException {
        if (nonUniqueUserName(user.getUserName()) && nonUniqueEmail(user.getEmail()))
            throw new UserServiceException("Username and email are already being used.");
        else if (nonUniqueUserName(user.getUserName()))
            throw new UserServiceException("Username is already being used");
        else if (nonUniqueEmail(user.getEmail()))
            throw new UserServiceException("Email is already being used");
        return userRepository.save(user);
    }

    public User updateUser(Long userId, User updateUser, String originalPassword) throws UserServiceException {
        User user = userRepository.findDistinctByUserId(userId);
        if (user == null)
            throw new UserServiceException("Unable to update user. UserId " + userId + " not found.");
        mapUpdate(user, updateUser.getUserName(), updateUser.getPassword(), originalPassword, updateUser.getFirstName(), updateUser.getLastName(), updateUser.getLocation());
        return userRepository.save(user);
    }

    private void mapUpdate(User user, String newUserName, String newPassword, String originalPassword, String newFirstName, String newLastName, Location newLocation) throws UserServiceException {
        if (newUserName != null && !newUserName.isBlank())
            user.setUserName(newUserName);
        if (validPasswordChange(user, newPassword, originalPassword))
            user.setPassword(newPassword);
        if (newFirstName != null && !newFirstName.isBlank())
            user.setFirstName(newFirstName);
        if (newLastName != null && !newLastName.isBlank())
            user.setLastName(newLastName);
        if (newLocation != null)
            user.setLocation(newLocation);
    }

    private boolean validPasswordChange(User user, String newPassword, String originalPassword) throws UserServiceException {
        if (newPassword != null && !user.isPassword(originalPassword))
            throw new UserServiceException("Unable to update user password. Incorrect original password");
        return newPassword != null && !newPassword.isBlank() && user.isPassword(originalPassword);
    }

    public boolean userExists(Long userId) {
        return userRepository.existsById(userId);
    }

    private boolean nonUniqueUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    private boolean nonUniqueEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<Long> eventsHosting(Long userId) {
        List<Long> eventIds = null;
        User user = userRepository.findDistinctByUserId(userId);
        if (!(user.getEvents() == null || user.getEvents().isEmpty())) {
            eventIds = new ArrayList<>();
            for (Event event : user.getEvents()) {
                if (event.getHostId().equals(userId))
                    eventIds.add(event.getEventId());
            }
        }
        return eventIds;
    }

    public void removeUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
