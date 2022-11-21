package com.ojih.rex.eventplanner.repository;

import com.ojih.rex.eventplanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findDistinctByUserId(Long userId);
    List<User> findByUserIdIn(List<Long> userIds);
    List<User> findByUserName(String userName);
    List<User> findByUserNameStartsWith(String userName);
    List<User> findByFirstName(String firstName);
    List<User> findByFirstNameStartsWith(String firstName);
    List<User> findByLastName(String lastName);
    List<User> findByLastNameStartsWith(String lastName);
    List<User> findByFirstNameOrLastName(String first, String last);
    List<User> findByFirstNameOrLastNameContaining(String first, String last);
    List<User> findDistinctByEmail(String email);
    List<User> findByLocationCity(String city);
    List<User> findByLocationState(String state);
    List<User> findByLocationPostalCode(String postalCOde);
    List<User> findByLocationCityOrLocationState(String city, String state);
    List<User> findByLocationCityAndLocationState(String city, String state);
    List<User> findByLocationCityOrLocationPostalCode(String city, String postalCode);
    List<User> findByLocationCityAndLocationPostalCode(String city, String postalCode);
    List<User> findByLocationStateOrLocationPostalCode(String state, String postalCode);
    List<User> findByLocationStateAndLocationPostalCode(String state, String postalCode);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
}
