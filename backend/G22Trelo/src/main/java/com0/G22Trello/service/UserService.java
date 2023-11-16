package com0.G22Trello.service;

import com0.G22Trello.model.*;
import com0.G22Trello.repository.TaskRepository;
import com0.G22Trello.repository.UserRepository;
import com0.G22Trello.tools.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


@Service
public class UserService {

    private static final int MIN_PASSWORD_LENGTH = 8;

    @Autowired
    UserRepository userRepository;


    public String updateUser(User user) {
        User user1 = userRepository.findByEmail(user.getEmail());
        if (user1 == null) {
            return "No User found with given Email Address";
        }
        user1.setEmail(user.getEmail());
        user1.setPassword(user.getPassword());
        user1.setName(user.getName());
        user1.setSecurityAnswer(user.getSecurityAnswer());
        LocalDate today = LocalDate.now();
        user1.setLastLoginDate(today);
        userRepository.save(user1);
        return "User information updated successfully!";
    }


    public String signUp(User user) {
        if(user.getSecurityAnswer() == null){
            return "Please enter security Answer";
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            //throw new ResponseStatusException(HttpStatus.CONFLICT, "Email address already registered. Please log in.");
            return ErrorMessages.EMAIL_ALREADY_REGISTERED.getMessage();
        }

        String password = user.getPassword();
        if (!validatePassword(password)) {
            return ErrorMessages.INVALID_PASSWORD.getMessage();
        }

        try {
            userRepository.save(user);
            return "User registered successfully";
        } catch (Exception e) {
            //throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while saving user", e);
            return "Error occurred while saving user";
        }
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public String resetPassword(@RequestBody User user2){
        User user = findUserByEmail(user2.getEmail());

        if(user == null)
        {
            return  "User not found. Please try again.";
        }
        if (!user.getSecurityAnswer().equals(user2.getSecurityAnswer()))
        {
            return "Incorrect security answer. Please try again.";
        }
        if(user2.getPassword() == null) {
            return "Please enter new password";
        }

        updateUser(user2);

        return "Password reset done";
    }

    public String logIn(@RequestBody User user2){
        User user = findUserByEmail(user2.getEmail());
        if(user == null)
        {
            return "User not found. Please sign up first.";
        }


        if(user.getPassword().equals(user2.getPassword())){
            return "User logged in successfully";

        }
       return "Password didn't match";
    }


    private boolean validatePassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }

        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        return pattern.matcher(password).matches();
    }


    public List<Workspace> getUserWorkspaces(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        return user.getWorkspaces();
    }

    public List<Board> getUserBoards(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        List<Workspace> workspaces = user.getWorkspaces();
        List<Board> boards = new ArrayList<>();

        for (Workspace workspace : workspaces) {
            boards.addAll(workspace.getBoards());
        }

        return boards;
    }
}
