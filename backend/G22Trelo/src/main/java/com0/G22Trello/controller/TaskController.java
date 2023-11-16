package com0.G22Trello.controller;

import com0.G22Trello.model.Task;
import com0.G22Trello.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    TaskService taskService;
    @PostMapping("/create")
    public String createTask(@RequestBody Task task){
        return taskService.createTask(task);
    }


    @PutMapping("/{taskID}")
    public String updateTask(@PathVariable("taskID") int taskID, @RequestBody Task task) {
        return taskService.updateTask(taskID, task);
    }

    @PutMapping("/{taskID}/changeStatus/{updatedStatus}")
    public String changeTaskStatus(@PathVariable("taskID") int taskID, @PathVariable("updatedStatus") String updatedStatus) {
        return taskService.changeTaskStatus(taskID, updatedStatus);
    }

    @PutMapping("/{taskID}/addMember/{userEmail}")
    public ResponseEntity<String> assignMemberToTask(@PathVariable("taskID") int taskID, @PathVariable("userEmail") String email) {
        return taskService.assignMemberToTask(taskID, email);
    }

    @GetMapping
    public List<Task> getTasksByUserEmail(@RequestParam("userEmail") String userEmail) {
        return taskService.getTasksByUserEmail(userEmail);
    }


}

