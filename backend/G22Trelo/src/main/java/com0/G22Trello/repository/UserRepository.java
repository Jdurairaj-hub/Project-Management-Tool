package com0.G22Trello.repository;

import com0.G22Trello.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    //Get the email input from the user, which will be used in the signUpUser method of UserService
        User findByEmail(String email);
}
