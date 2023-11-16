package com0.G22Trello;

import com0.G22Trello.controller.WorkspaceController;
import com0.G22Trello.model.Board;
import com0.G22Trello.model.User;
import com0.G22Trello.model.Workspace;
import com0.G22Trello.repository.UserRepository;
import com0.G22Trello.repository.WorkspaceRepository;
import com0.G22Trello.service.UserService;
import com0.G22Trello.service.WorkspaceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;


import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class WorkspaceServiceTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @InjectMocks
    private WorkspaceService workspaceService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for create workspace method with valid user and workspace details
    @Test
    public void testCreateWorkspace_ValidUser() {
        WorkspaceRepository workspaceRepository = Mockito.mock(WorkspaceRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        String workspaceTitle = "Workspace Title";
        String email = "mihir.patel@dal.ca";

        Mockito.when(workspaceRepository.findByWorkspaceTitle(workspaceTitle)).thenReturn(null);

        User user = new User(); // Assuming a valid user object is returned
        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);

        WorkspaceService workspaceService = new WorkspaceService(workspaceRepository, userRepository);

        ResponseEntity<String> response = workspaceService.createWorkspace(workspaceTitle, email);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Workspace created successfully", response.getBody());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    // Test for create workspace method with duplicate workspace details
    @Test
    public void testCreateWorkspace_WorkspaceExists() {
        WorkspaceRepository workspaceRepository = Mockito.mock(WorkspaceRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        String workspaceTitle = "Workspace Title";
        String email = "mihir.patel@dal.ca";

        Workspace existingWorkspace = new Workspace();
        Mockito.when(workspaceRepository.findByWorkspaceTitle(workspaceTitle)).thenReturn(existingWorkspace);

        WorkspaceService workspaceService = new WorkspaceService(workspaceRepository, userRepository);

        ResponseEntity<String> response = workspaceService.createWorkspace(workspaceTitle, email);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Workspace already exists", response.getBody());
        Mockito.verify(workspaceRepository, Mockito.never()).save(Mockito.any(Workspace.class));
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
    }

    // Update Workspace test for Workspace that is available
    @Test
    public void updateWorkspaceTest_Valid(){
        int workspaceId = 1;
        Workspace existingWorkspace = new Workspace();
        existingWorkspace.setWorkspaceID(workspaceId);
        existingWorkspace.setWorkspaceTitle("TestWorkspace");

        Workspace updatedWorkspace = new Workspace();
        updatedWorkspace.setWorkspaceID(workspaceId);
        updatedWorkspace.setWorkspaceTitle("TestWorkspaceUpdated");

        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(existingWorkspace));
        when(workspaceRepository.save(existingWorkspace)).thenReturn(existingWorkspace);

        ResponseEntity<String> response = workspaceService.updateWorkspace(workspaceId, updatedWorkspace);

        verify(workspaceRepository).findById(workspaceId);
        verify(workspaceRepository).save(existingWorkspace);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Workspace has been updated successfully", response.getBody());
        assertEquals(updatedWorkspace.getWorkspaceTitle(), existingWorkspace.getWorkspaceTitle());

    }



    // Update Workspace test for Workspace that is not created
    @Test
    public void updateWorkspaceTest_InValid() {
        int workspaceId = 500;
        Workspace testWorkspace = new Workspace();
        testWorkspace.setWorkspaceID(workspaceId);

        Workspace updatedWorkspace = new Workspace();
        updatedWorkspace.setWorkspaceTitle("Workspace Updated");

        ResponseEntity<String> response = workspaceService.updateWorkspace(workspaceId, updatedWorkspace);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Workspace was not found", response.getBody());
    }


    // addUserToWorkspaceTest that have valid user and Workspace
    @Test
    public void addUserToWorkspaceTest_Valid() {
        Workspace testWorkspace = new Workspace();
        String workspaceTitle = "Title";
        String userEmail = "mail@dal.ca";
        testWorkspace.setWorkspaceTitle(workspaceTitle);

        Workspace workspace = new Workspace();
        List<User> listWorkspace = new ArrayList<>();
        workspace.setUsers(listWorkspace);

        User user = new User();
        List<Workspace> userListWorkspaces = new ArrayList<>();
        user.setWorkspaces(userListWorkspaces);

        when(workspaceRepository.findByWorkspaceTitle(workspaceTitle)).thenReturn(workspace);
        when(userRepository.findByEmail(userEmail)).thenReturn(user);
        when(workspaceRepository.save(workspace)).thenReturn(workspace);
        when(userRepository.save(user)).thenReturn(user);

        ResponseEntity<String> response = workspaceService.addUserToWorkspace(workspaceTitle, userEmail);

        verify(workspaceRepository).findByWorkspaceTitle(workspaceTitle);
        verify(userRepository).findByEmail(userEmail);
        verify(workspaceRepository).save(workspace);
        verify(userRepository).save(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Member added successfully to the workspace", response.getBody());
    }


    // addUserToWorkspaceTest that have valid user but InValid Workspace
    @Test
    @Transactional
    public void addUserToWorkspaceTest_InValidWorkspace() {
        Workspace testWorkspace = new Workspace();
        String workspaceTitle = "Title2";
        testWorkspace.setWorkspaceTitle(workspaceTitle);

        String email = "mihir.patel@dal.ca";
        User testUser = new User();
        testUser.setEmail(email);
        when(workspaceRepository.findByWorkspaceTitle(workspaceTitle)).thenReturn(null);;

        ResponseEntity<String> response = workspaceService.addUserToWorkspace(workspaceTitle, email);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Workspace not found", response.getBody());
    }

    // addUserToWorkspaceTest that have valid Workspace but InValid User
    @Test
    @Transactional
    public void addUserToWorkspaceTest_InValidUser() {
        Workspace testWorkspace = new Workspace();
        String workspaceTitle = "Title";
        testWorkspace.setWorkspaceTitle(workspaceTitle);

        String email = "test.patel@dal.ca";
        User testUser = new User();
        testUser.setEmail(email);
        when(workspaceRepository.findByWorkspaceTitle(workspaceTitle)).thenReturn(testWorkspace);
        when(userRepository.findByEmail(email)).thenReturn(null);

        ResponseEntity<String> response = workspaceService.addUserToWorkspace(workspaceTitle, email);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("User not found", response.getBody());

    }


    // addUserToWorkspaceTest for user that is already added to the Workspace and trying to add again
    @Test
    @Transactional
    public void addUserToWorkspaceTest_DuplicateAdding() {
        WorkspaceRepository workspaceRepository = Mockito.mock(WorkspaceRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        String workspaceTitle = "Title";
        String email = "mayank.ramchandani@dal.ca";

        Workspace existingWorkspace = new Workspace();
        Mockito.when(workspaceRepository.findByWorkspaceTitle(workspaceTitle)).thenReturn(existingWorkspace);

        User existingUser = new User();
        Mockito.when(userRepository.findByEmail(email)).thenReturn(existingUser);
        WorkspaceService workspaceService = new WorkspaceService(workspaceRepository, userRepository);

        ResponseEntity<String> response1 = workspaceService.addUserToWorkspace(workspaceTitle, email);

        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals("Member added successfully to the workspace", response1.getBody());
        Mockito.verify(workspaceRepository, Mockito.times(1)).save(existingWorkspace);
        Mockito.verify(userRepository, Mockito.times(1)).save(existingUser);

        ResponseEntity<String> response2 = workspaceService.addUserToWorkspace(workspaceTitle, email);

        assertEquals(HttpStatus.CONFLICT, response2.getStatusCode());
        assertEquals("User already added to workspace", response2.getBody());
        Mockito.verify(workspaceRepository, Mockito.times(1)).save(existingWorkspace);
        Mockito.verify(userRepository, Mockito.times(1)).save(existingUser);
    }


    // deleteUserFromWorkspace test for valid User and Workspace
    @Test
    @Transactional
    public void deleteUserTest_Valid(){
        Workspace testWorkspace = new Workspace();
        String workspaceTitle = "Title";
        testWorkspace.setWorkspaceTitle(workspaceTitle);

        String userEmail = "mail@dal.ca";

        User user = new User();
        user.setEmail(userEmail);

        List<Workspace> workspaces = new ArrayList<>();
        workspaces.add(testWorkspace);
        user.setWorkspaces(workspaces);

        when(workspaceRepository.findByWorkspaceTitle(workspaceTitle)).thenReturn(testWorkspace);
        when(userRepository.findByEmail(userEmail)).thenReturn(user);

        ResponseEntity<String> response = workspaceService.deleteUserFromWorkspace(workspaceTitle, userEmail);

        verify(workspaceRepository, times(1)).findByWorkspaceTitle(workspaceTitle);
        verify(userRepository, times(1)).findByEmail(userEmail);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User removed successfully from the workspace", response.getBody());
    }


    // deleteUserFromWorkspace test for valid Workspace but Invalid User
    @Test
    @Transactional
    public void deleteUserTest_InValidUser() {
        Workspace testWorkspace = new Workspace();
        String workspaceTitle = "Title";
        testWorkspace.setWorkspaceTitle(workspaceTitle);

        String email = "test.patel@dal.ca";

        when(workspaceRepository.findByWorkspaceTitle(workspaceTitle)).thenReturn(testWorkspace);
        when(userRepository.findByEmail(email)).thenReturn(null);

        ResponseEntity<String> response = workspaceService.deleteUserFromWorkspace(workspaceTitle, email);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("User not found", response.getBody());

    }

    // deleteUserFromWorkspace test for valid User but Invalid Workspace
    @Test
    @Transactional
    public void deleteUserTest_InValidWorkspace() {
        Workspace testWorkspace = new Workspace();
        String workspaceTitle = "Title";
        testWorkspace.setWorkspaceTitle(workspaceTitle);

        String email = "mihir.patel@dal.ca";
        when(workspaceRepository.findByWorkspaceTitle(workspaceTitle)).thenReturn(null);
        when(userRepository.findByEmail(email)).thenReturn(new User());

        ResponseEntity<String> response = workspaceService.deleteUserFromWorkspace(workspaceTitle, email);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Workspace not found", response.getBody());

    }

    // deleteUserFromWorkspace test for valid Workspace but User Not assigned to workspace
    @Test
    @Transactional
    public void deleteUserTest_UserNotMember() {
        WorkspaceRepository workspaceRepository = Mockito.mock(WorkspaceRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        String workspaceTitle = "Title";
        String email = "mayank.ramchandani@dal.ca";

        Workspace existingWorkspace = new Workspace();
        Mockito.when(workspaceRepository.findByWorkspaceTitle(workspaceTitle)).thenReturn(existingWorkspace);

        User existingUser = new User();
        Mockito.when(userRepository.findByEmail(email)).thenReturn(existingUser);

        WorkspaceService workspaceService = new WorkspaceService(workspaceRepository, userRepository);
        ResponseEntity<String> response = workspaceService.deleteUserFromWorkspace(workspaceTitle, email);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User is not a member of the workspace", response.getBody());
        Mockito.verify(workspaceRepository, Mockito.never()).save(Mockito.any(Workspace.class));
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
    }

}
