package com0.G22Trello;

import com0.G22Trello.model.Board;
import  com0.G22Trello.model.Task;
import com0.G22Trello.model.User;
import com0.G22Trello.model.Workspace;
import com0.G22Trello.repository.TaskRepository;
import com0.G22Trello.repository.UserRepository;
import com0.G22Trello.service.BoardService;
import com0.G22Trello.service.TaskService;
import com0.G22Trello.service.UserService;
import com0.G22Trello.tools.ErrorMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Create task test check with all the valid inputs
    @Test
    public void createTaskTest_Valid() {

        Task task = new Task();
        task.setName("TestTask1");
        task.setCreationDate(LocalDate.now());
        task.setDueDate(LocalDate.of(2023, 8, 10));


        when(taskRepository.save(any(Task.class))).thenReturn(task);

        String result = taskService.createTask(task);
        verify(taskRepository).save(task);

        assertEquals("Data successfully saved for Task", result, "Tas cannot be created");
    }


    // Create task test check with null name of the task
    @Test
    public void createTaskTest_NullTaskName() {

        Task task = new Task();
        task.setCreationDate(LocalDate.now());
        task.setDueDate(LocalDate.now().plusDays(7));

        String result = taskService.createTask(task);

        verify(taskRepository, never()).save(any(Task.class));
        assertEquals("Name is null, so task cannot be created",result,"Please add name");
    }


    // Create task test for validating the due date, if it is after the creation date
    @Test
    public void createTaskTest_DueDateValidation() {
        Task task = new Task();
        task.setName("TestTask2");
        task.setCreationDate(LocalDate.now());
        task.setDueDate(LocalDate.of(2021,8,30));

        String result = taskService.createTask(task);

        verify(taskRepository, never()).save(any(Task.class));

        assertEquals(ErrorMessages.INVALID_DATE_ERROR.getMessage(),result, "Please enter proper date, it should be after the task creation date");
    }

    // update task method test for the task that exist
    @Test
    public void updateTaskTest_Valid() {
        int taskId = 5;
        Task createTask = new Task();
        createTask.setTaskID(taskId);
        createTask.setName("TestTask3");
        createTask.setCreationDate(LocalDate.now());
        createTask.setDueDate(LocalDate.of(2023,8,23));

        Task updatedTask = new Task();
        updatedTask.setName("TestTask3Updated");
        updatedTask.setStatus("In Progress");
        updatedTask.setDueDate(LocalDate.of(2023,7,31));

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(createTask));

        String result = taskService.updateTask(taskId, updatedTask);
        assertEquals("Task ID " + taskId + " has been updated successfully.",result, "Task not updated");

    }

    // update task method test for the task that does not exist
    @Test
    public void updateTaskTest_InValidTask() {

        int taskId = 100;
        Task updatedTask = new Task();
        updatedTask.setName("TestTask4");
        updatedTask.setStatus("Doing");
        updatedTask.setDueDate(LocalDate.of(2023,8,30));

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
        String result = taskService.updateTask(taskId, updatedTask);

        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));

        assertEquals("Task ID " + taskId + " does not exist.", result, "Task not found in the system");
    }

    // update task test for validating the due date, if it is after the creation date
    @Test
    public void updateTaskTest_DueDateValidation() {
        int taskId = 1;
        Task existingTask = new Task();
        existingTask.setTaskID(taskId);
        existingTask.setName("TestTask5");
        existingTask.setCreationDate(LocalDate.now());
        existingTask.setDueDate(LocalDate.of(2023,8,30));

        Task updatedTask = new Task();
        updatedTask.setName("TestTask5Updated");
        updatedTask.setStatus("To-Do");
        updatedTask.setDueDate(LocalDate.of(2022,8,30));

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

        String result = taskService.updateTask(taskId, updatedTask);

        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));

        assertEquals(ErrorMessages.INVALID_DATE_ERROR.getMessage(),result, "Please enter proper date, it should be after the task creation date");
    }

    // Change Task Status test for valid task
    @Test
    public void changeTaskStatusTest_Valid() {
        int taskId = 30;
        Task existingTask = new Task();
        existingTask.setTaskID(taskId);
        existingTask.setStatus("To-Do");

        String updatedStatus = "Completed";

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

        String result = taskService.changeTaskStatus(taskId, updatedStatus);

        verify(taskRepository).findById(taskId);

        verify(taskRepository).save(existingTask);

        assertEquals("Task status updated successfully", result, "Task status cannot be updated");
        assertEquals(updatedStatus, existingTask.getStatus());
    }


    // Change Task Status test for InValid task that is not in the system
    @Test
    public void changeTaskStatusTest_InValidTask() {

        int taskId = 1000;
        String updatedStatus = "Completed";

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        String result = taskService.changeTaskStatus(taskId, updatedStatus);

        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));

        assertEquals("Task ID " + taskId + " does not exist.", result, "Task not found in the system");
    }

    // Change Task Status test for InValid status value
    @Test
    public void changeTaskStatusTest_NullStatus() {
        int taskId = 31;
        Task existingTask = new Task();
        existingTask.setTaskID(taskId);
        existingTask.setStatus("Doing");

        String updatedStatus = null;

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

        String result = taskService.changeTaskStatus(taskId, updatedStatus);

        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));

        assertEquals( "Status cannot be null", result, "Please check Status Value");

    }

    // Test case for assign member to task method with valid user and task details
    @Test
    public void testAssignMemberToTask_ValidUserAndTask() {
        int taskId = 1;
        String userEmail = "validUser@dal.ca";

        Task task = new Task();
        task.setTaskID(taskId);
        List<User> users = new ArrayList<>();
        task.setUsers(users);

        User user = new User();
        user.setEmail(userEmail);
        List<Task> tasks = new ArrayList<>();
        user.setTasks(tasks);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail(userEmail)).thenReturn(user);

        ResponseEntity<String> response = taskService.assignMemberToTask(taskId, userEmail);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Member added successfully to the task", response.getBody());

        verify(taskRepository, times(1)).findById(taskId);
        verify(userRepository, times(1)).findByEmail(userEmail);

        assertTrue(task.getUsers().contains(user));
        assertTrue(user.getTasks().contains(task));
    }

    // Test case for assign member to task method with InValid user details
    @Test
    public void testAssignMemberToTask_InvalidUser() {
        int taskId = 1;
        String userEmail = "invalidUser@dal.ca";

        Task task = new Task();
        task.setTaskID(taskId);
        List<User> users = new ArrayList<>();
        task.setUsers(users);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail(userEmail)).thenReturn(null); // Simulate non-existing user

        ResponseEntity<String> response = taskService.assignMemberToTask(taskId, userEmail);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());

        verify(taskRepository, times(1)).findById(taskId);
        verify(userRepository, times(1)).findByEmail(userEmail);

        assertFalse(task.getUsers().contains(null));

    }

}
