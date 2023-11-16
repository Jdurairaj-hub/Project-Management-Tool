package com0.G22Trello.service;

import com0.G22Trello.model.Task;

import com0.G22Trello.model.User;

import com0.G22Trello.repository.TaskRepository;

import com0.G22Trello.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;

import java.util.List;

import java.util.Optional;



@Service

public class TaskService {


    @Autowired

    TaskRepository taskRepository;


    @Autowired

    public TaskService(UserRepository userRepository) {

        this.userRepository = userRepository;

    }


    @Autowired

    public void setUserRepository(UserRepository userRepository) {

        this.userRepository = userRepository;

    }


    private UserRepository userRepository;





    public String createTask(Task task){

        if(task.getName() == null){

            return "Name is null, so task cannot be created";

        }

        if(!dueDataValidation(task.getCreationDate(),task.getDueDate())){

            return "Date enter is not Valid, due date needs to be after creation date.";

        }


        taskRepository.save(task);

        return "Data successfully saved for Task";

    }


    public String updateTask(int taskId, Task updatedTask) {

        Task existingTask = taskRepository.findById(taskId).orElse(null);


        if (existingTask != null) {


            existingTask.setName(updatedTask.getName());

            existingTask.setStatus(updatedTask.getStatus());

            existingTask.setDueDate(updatedTask.getDueDate());


            if(dueDataValidation(existingTask.getCreationDate(),existingTask.getDueDate())){

                taskRepository.save(existingTask);

                return "Task ID " + taskId + " has been updated successfully.";


            }

            return "Date enter is not Valid, due date needs to be after creation date.";


        }

        return "Task ID " + taskId + " does not exist.";



    }


    public String changeTaskStatus(int taskId, String updatedStatus) {

        Optional<Task> optionalTask = taskRepository.findById(taskId);

        if (optionalTask.isPresent()) {

            Task existingTask = optionalTask.get();


            if(updatedStatus==null){

                return "Status cannot be null";

            }


            existingTask.setStatus(updatedStatus);


            taskRepository.save(existingTask);

            return "Task status updated successfully";



        }

        return "Task ID " + taskId + " does not exist.";


    }


    @Transactional
    public ResponseEntity<String> assignMemberToTask(int taskId, String email) {
        Task task = taskRepository.findById(taskId).orElse(null);
        User user = userRepository.findByEmail(email);

        if (task == null) {
            return new ResponseEntity<>("Task not found", HttpStatus.NOT_FOUND);
        } else if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } else {
            List<User> users = task.getUsers();
            List<Task> tasks = user.getTasks();

            if (users.contains(user) || tasks.contains(task)) {
                return new ResponseEntity<>("User already added to the task", HttpStatus.CONFLICT);
            }

            users.add(user);
            task.setUsers(users);
            taskRepository.save(task);

            tasks.add(task);
            user.setTasks(tasks);
            userRepository.save(user);

            return new ResponseEntity<>("Member added successfully to the task", HttpStatus.OK);
        }
    }

    public List<Task> getTasksByUserEmail(String userEmail) {
        return taskRepository.findByUserEmail(userEmail);
    }


//    public ResponseEntity<String> assignMemberToTask(int taskId, String email) {
//
//        Task task = taskRepository.findById(taskId).orElse(null);
//
//        User user = userRepository.findByEmail(email);
//
//        if (task == null){
//
//            return new ResponseEntity<>( "Task not found", HttpStatus.NOT_FOUND);
//
//        } else if (user == null){
//
//            return new ResponseEntity<>( "User not found", HttpStatus.NOT_FOUND);
//
//        }
//
//        else {
//
//            List<User> users = task.getUsers();
//
//            List<Task> tasks = user.getTasks();
//
//            if (users.contains(user)||tasks.contains(task)){
//
//                return new ResponseEntity<>( "User already added to task", HttpStatus.CONFLICT);
//
//            }
//
//            users.add(user);
//
//            task.setUsers(users);
//
//            taskRepository.save(task);
//
//
//            tasks.add(task);
//
//            user.setTasks(tasks);
//
//            userRepository.save(user);
//
//
//            return new ResponseEntity<>("Member added successfully to the task", HttpStatus.OK);
//
//        }
//
//    }


    public boolean dueDataValidation(LocalDate creationDate, LocalDate dueDate){


        return dueDate.isAfter(creationDate);

    }

}

