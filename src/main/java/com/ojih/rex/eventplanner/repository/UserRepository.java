package com.ojih.rex.eventplanner.repository;

import com.ojih.rex.eventplanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public List<User> findByUserName();
    public List<User> findByFirstName();
    public List<User> findByLastName();
    public List<User> findByFirstNameOrLastName();
    public List<User> findByEmail();
    public List<User> findByHomeTown();

}
