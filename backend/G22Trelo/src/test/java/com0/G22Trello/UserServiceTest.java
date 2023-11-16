package com0.G22Trello;

import com0.G22Trello.model.Board;
import com0.G22Trello.model.User;
import com0.G22Trello.model.Workspace;
import com0.G22Trello.repository.TaskRepository;
import com0.G22Trello.repository.UserRepository;
import com0.G22Trello.service.TaskService;
import com0.G22Trello.service.UserService;
import com0.G22Trello.tools.ErrorMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;
    String sDate1="21/07/2023";
    Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);

    UserServiceTest() throws ParseException {
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    // Checking signUp test with the correct SignUp information
    @Test
    void signUpTest_valid(){
        User user = new User("Mihir", "harsh.pate@dal.ca","Harsh@2022", "Tommy");
        String result = userService.signUp(user);

        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository).save(user);

        assertEquals("User registered successfully",result,result);
    }


    // Checking signUp test for user with Invalid Password
    @Test
    void signUpTest_InValidPassword(){
        User user = new User("Mihir", "raj.patel@dal.ca","password", "Tommy");
        String result = userService.signUp(user);
        assertEquals(ErrorMessages.INVALID_PASSWORD.getMessage(),result,result);
    }

    // Checking for user with the email that is already registered
    @Test
    void signUpTest_EmailAlreadyRegisterd(){
        User user = new User("Mihir", "mihir.patel@dal.ca","password@2022", "Tommy");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        String result = userService.signUp(user);
        verify(userRepository).findByEmail(user.getEmail());

        assertEquals("Email address already registered. Please log in.",result,result);
    }

    // Checking for user for signIn that is already signed up
    @Test
    void signInTest_Valid(){
        User user = new User("harsh.patel@dal.ca","Harsh@2022");
        User existingUser = new User("harsh.patel@dal.ca", "Harsh@2022");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(existingUser);

        String result = userService.logIn(user);

        verify(userRepository).findByEmail(user.getEmail());
        assertEquals("User logged in successfully",result,result);
    }

    // Checking for user for signIn that is not signed up
    @Test
    void signInTest_InValidUser(){
        User user = new User("kartik.patel@dal.ca","Harsh@2022");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);

        String result = userService.logIn(user);
        verify(userRepository).findByEmail(user.getEmail());

        assertEquals("User not found. Please sign up first.",result,result);
    }

    // Checking for user for signIn that is already signed up but with incorrect password
    @Test
    void signInTest_InValidPassword(){
        User user = new User("harsh.patel@dal.ca","Harsh@2020");
        User existingUser = new User("harsh.patel@dal.ca", "Harsh@0000");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(existingUser);

        String result = userService.logIn(user);
        verify(userRepository).findByEmail(user.getEmail());

        assertEquals("Password didn't match",result,result);
    }

    // Reset Password check for user with valid security answer
    @Test
    void resetPasswordTest_ValidSecUser(){
        User user = new User("Mihir", "mihir.patel@dal.ca","Mihir@2023", "Tommy");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        String result = userService.resetPassword(user);
        verify(userRepository).save(user);

        assertEquals("Password reset done",result,result);
    }


    // Reset Password check for InValid user
    @Test
    void resetPasswordTest_InValidUser(){
        User user = new User("Mihir", "patel.mihirl@dal.ca","Mihir@2023", "Cat");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);

        String result = userService.resetPassword(user);

        assertEquals("User not found. Please try again.",result,result);
    }

    // Test for get user workspaces method, check if all the workspaces are returned for valid user
    @Test
    public void testGetUserWorkspaces_ValidUser() {
        String userEmail = "user@example.com";
        User user = new User();
        user.setEmail(userEmail);

        List<Workspace> expectedWorkspaces = Arrays.asList(
                new Workspace("Workspace 1"),
                new Workspace("Workspace 2")
        );

        user.setWorkspaces(expectedWorkspaces);

        when(userRepository.findByEmail(userEmail)).thenReturn(user);

        List<Workspace> result = userService.getUserWorkspaces(userEmail);

        assertEquals(expectedWorkspaces, result);
        verify(userRepository, times(1)).findByEmail(userEmail);
    }

    // Test for get user workspaces method, check for invalid user that is not in the system
    @Test
    public void testGetUserWorkspaces_UserNotFound() {
        String userEmail = "nonexistent_user@example.com";

        when(userRepository.findByEmail(userEmail)).thenReturn(null);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.getUserWorkspaces(userEmail);
        });

        assertEquals("User not found", exception.getReason());
        verify(userRepository, times(1)).findByEmail(userEmail);
    }

    // Test for get user Boards method, check if all the boards are returned for valid user
    @Test
    public void testGetUserBoards_ValidUser() {
        String userEmail = "user@example.com";
        User user = new User();
        user.setEmail(userEmail);

        Board board1 = new Board("Milestone", "Board 1",date1);
        Board board2 = new Board("Milestone","Board 2", date1);
        Board board3 = new Board("Milestone","Board 3", date1);

        Workspace workspace1 = new Workspace("Workspace 1");
        workspace1.setBoards(Arrays.asList(board1, board2));

        Workspace workspace2 = new Workspace("Workspace 2");
        workspace2.setBoards(Arrays.asList(board3));

        List<Workspace> expectedWorkspaces = Arrays.asList(workspace1, workspace2);
        user.setWorkspaces(expectedWorkspaces);

        when(userRepository.findByEmail(userEmail)).thenReturn(user);

        List<Board> result = userService.getUserBoards(userEmail);

        List<Board> expectedBoards = Arrays.asList(board1, board2, board3);
        assertEquals(expectedBoards, result);
        verify(userRepository, times(1)).findByEmail(userEmail);
    }

    // Test for get user boards method, check for invalid user that is not in the system
    @Test
    public void testGetUserBoards_UserNotFound() {
        String userEmail = "nonexistent_user@example.com";

        when(userRepository.findByEmail(userEmail)).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.getUserBoards(userEmail);
        });

        assertEquals("User not found", exception.getReason());
        verify(userRepository, times(1)).findByEmail(userEmail);
    }


}