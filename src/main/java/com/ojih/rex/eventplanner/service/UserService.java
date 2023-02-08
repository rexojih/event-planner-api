package com.ojih.rex.eventplanner.service;

import com.ojih.rex.eventplanner.exception.user.ExistingUserException;
import com.ojih.rex.eventplanner.exception.user.UserNotFoundException;
import com.ojih.rex.eventplanner.exception.user.UserUnauthenticatedException;
import com.ojih.rex.eventplanner.model.Event;
import com.ojih.rex.eventplanner.model.User;
import com.ojih.rex.eventplanner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserFromId(Long userId) throws UserNotFoundException {
        User user = userRepository.findDistinctByUserId(userId);
        if (user == null)
            throw new UserNotFoundException("Unable to get user. UserId " + userId + " not found");
        return user;
    }

    public User authenticateUser(String usernameOrEmail, String password) throws UserUnauthenticatedException {
        User user = userRepository.findDistinctByUserNameOrEmail(usernameOrEmail, usernameOrEmail);
        if (user != null && user.isPassword(password))
            return user;
        else
            throw new UserUnauthenticatedException("Unable to authenticate. Username or password is incorrect");
    }


    public List<User> getUserFromNameStartingWith(String name) {
        List<User> usersByUserName = userRepository.findByUserNameStartsWith(name);
        List<User> usersByFistName = userRepository.findByLastNameStartsWith(name);
        List<User> usersByLastName = userRepository.findByFirstNameStartsWith(name);
        return merge(usersByUserName, usersByLastName, usersByFistName);
    }

    public List<User> getUsersFromNameContaining(String name) {
        List<User> usersByUserName = userRepository.findByUserNameContaining(name);
        List<User> usersByFistName = userRepository.findByLastNameContaining(name);
        List<User> usersByLastName = userRepository.findByFirstNameContaining(name);
        return merge(usersByUserName, usersByLastName, usersByFistName);
    }

    public List<User> getUsersFromName(String name) {
        List<User> usersByUserName = userRepository.findByUserName(name);
        List<User> usersByFistName = userRepository.findByLastName(name);
        List<User> usersByLastName = userRepository.findByFirstName(name);
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

    public User storeUser(User user) throws ExistingUserException {
        if (nonUniqueUserName(user.getUserName()) && nonUniqueEmail(user.getEmail()))
            throw new ExistingUserException("Username and email are already being used.");
        else if (nonUniqueUserName(user.getUserName()))
            throw new ExistingUserException("Username is already being used");
        else if (nonUniqueEmail(user.getEmail()))
            throw new ExistingUserException("Email is already being used");
        return userRepository.save(user);
    }

    public User updateUser(Long userId, User updateUser, String originalPassword) throws UserNotFoundException, UserUnauthenticatedException {
        User user = userRepository.findDistinctByUserId(userId);
        if (user == null)
            throw new UserNotFoundException("Unable to update user. UserId " + userId + " not found.");
        mapUpdate(user, updateUser, originalPassword);
        return userRepository.save(user);
    }

    private void mapUpdate(User user, User updateUser, String originalPassword) throws UserUnauthenticatedException {
        if (updateUser.getUserName() != null && !updateUser.getUserName().isBlank())
            user.setUserName(updateUser.getUserName());
        if (validPasswordChange(user, updateUser.getPassword(), originalPassword))
            user.setPassword(updateUser.getPassword());
        if (updateUser.getEmail() != null && !updateUser.getEmail().isBlank())
            user.setEmail(updateUser.getEmail());
        if (updateUser.getFirstName() != null && !updateUser.getFirstName().isBlank())
            user.setFirstName(updateUser.getFirstName());
        if (updateUser.getLastName() != null && !updateUser.getLastName().isBlank())
            user.setLastName(updateUser.getLastName());
        if (updateUser.getLocation() != null)
            user.setLocation(updateUser.getLocation());
    }

    private boolean validPasswordChange(User user, String newPassword, String originalPassword) throws UserUnauthenticatedException {
        if (newPassword != null && !newPassword.isBlank() && !user.isPassword(originalPassword))
            throw new UserUnauthenticatedException("Unable to update user password. Incorrect original password");
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

    public List<Long> getHostingEventIds(Long userId) {
        List<Long> eventIds = new ArrayList<>();
        User user = userRepository.findDistinctByUserId(userId);
        if (!(user.getEvents() == null || user.getEvents().isEmpty())) {
            for (Event event : user.getEvents()) {
                if (event.getHostId().equals(userId))
                    eventIds.add(event.getEventId());
            }
        }
        return eventIds;
    }

    public List<Long> removeUser(Long userId) throws UserNotFoundException {
        if (!userExists(userId))
            throw new UserNotFoundException("User " + userId + " does not exist");
        List<Long> hostedEventIds = getHostingEventIds(userId);
        userRepository.deleteById(userId);
        return hostedEventIds;
    }
}
