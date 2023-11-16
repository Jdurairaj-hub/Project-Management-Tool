package com0.G22Trello.controller;

import com0.G22Trello.model.Board;
import com0.G22Trello.model.Workspace;
import com0.G22Trello.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com0.G22Trello.model.User;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PutMapping("/{userId}")
    public String updateUser(@PathVariable("userId") int userId, @RequestBody User user) {
        return userService.updateUser(user);
    }

//    @GetMapping("/{userEmail}/workspaces")
//    public List<Workspace> getUserWorkspaces(@PathVariable("userEmail") String userEmail) {
//        return userService.getUserWorkspaces(userEmail);
//    }

    @GetMapping("/workspaces")
    public List<Workspace> getUserWorkspaces(@RequestParam String userEmail) {
        return userService.getUserWorkspaces(userEmail);
    }

    @GetMapping("/{userEmail}/boards")
    public List<Board> getUserBoards(@PathVariable("userEmail") String userEmail) {
        return userService.getUserBoards(userEmail);
    }


//    @PostMapping("/{userId}/tasks/{taskId}")
//    public String addUserToTask(
//            @PathVariable("userId") int userId,
//            @PathVariable("taskId") int taskId
//    ) {
//        return userService.addUserToTask(userId, taskId);
//    }


    @PostMapping("/passwordReset")
    public String resetPassword(@RequestBody User user2){
        return userService.resetPassword(user2);
    }

    @PostMapping("/logIn")
    public String logIn(@RequestBody User user3) {
        return userService.logIn(user3);
    }

    @PostMapping("/signUp")
    public String signUp(@RequestBody User user3) {
        return userService.signUp(user3);
    }

//    @PostMapping("/logOut")
//    public void logOut(@RequestBody User user) {
//        userService.logOut(user);
//    }

}