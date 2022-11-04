package com.ojih.rex.eventplanner.repository;

import com.ojih.rex.eventplanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public User findDistinctByUserId(Long userId);
    public List<User> findByUserName(String userName);
    public List<User> findByUserNameContaining(String userName);
    public List<User> findByFirstName(String firstName);
    public List<User> findByFirstNameContaining(String firstName);
    public List<User> findByLastName(String lastName);
    public List<User> findByLastNameContaining(String lastName);
    public List<User> findByFirstNameOrLastName(String first, String last);
    public List<User> findByFirstNameOrLastNameContaining(String first, String last);
    public List<User> findDistinctByEmail(String email);
    public List<User> findByLocationCity(String city);
    public List<User> findByLocationState(String state);
    public List<User> findByLocationPostalCode(String postalCOde);
    public List<User> findByLocationCityOrLocationState(String city, String state);
    public List<User> findByLocationCityAndLocationState(String city, String state);
    public List<User> findByLocationCityOrLocationPostalCode(String city, String postalCode);
    public List<User> findByLocationCityAndLocationPostalCode(String city, String postalCode);
    public List<User> findByLocationStateOrLocationPostalCode(String state, String postalCode);
    public List<User> findByLocationStateAndLocationPostalCode(String state, String postalCode);
}
