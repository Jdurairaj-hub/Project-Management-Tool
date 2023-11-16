package com0.G22Trello.service;


import com0.G22Trello.model.User;
import com0.G22Trello.model.Workspace;
import com0.G22Trello.repository.UserRepository;
import com0.G22Trello.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class WorkspaceService {
    @Autowired
    public WorkspaceService(WorkspaceRepository workspaceRepository, UserRepository userRepository) {
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
    }

    WorkspaceRepository workspaceRepository;
    private UserRepository userRepository;

    public ResponseEntity<String> createWorkspace(String workspaceTitle, String email) {
        Workspace workspace = workspaceRepository.findByWorkspaceTitle(workspaceTitle);
        User user = userRepository.findByEmail(email);
        if (workspace == null){
            LocalDate today = LocalDate.now();
            workspace = new Workspace();
            workspace.setCreationDate(today);
            workspace.setWorkspaceTitle(workspaceTitle);
            Workspace createdWorkspace = workspaceRepository.save(workspace);
            if (user == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
            user.getWorkspaces().add(createdWorkspace);
            userRepository.save(user);

            return new ResponseEntity<>("Workspace created successfully", HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>( "Workspace already exists", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> updateWorkspace(int workspaceId, Workspace updatedWorkspace) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElse(null);
        if (workspace != null) {
            // Update the workspace object with the new values from updatedWorkspace
            workspace.setUsers(updatedWorkspace.getUsers());
            workspace.setBoards(updatedWorkspace.getBoards());
            if (updatedWorkspace.getCreationDate() != null) {
                workspace.setCreationDate(updatedWorkspace.getCreationDate());
            }
            if (updatedWorkspace.getWorkspaceTitle() != null) {
                workspace.setWorkspaceTitle(updatedWorkspace.getWorkspaceTitle());
            }

            // Save the updated workspace object
            workspaceRepository.save(workspace);

            return new ResponseEntity<>("Workspace has been updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>( "Workspace was not found", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<String> addUserToWorkspace(String workspaceTitle, String email) {
        Workspace workspace = workspaceRepository.findByWorkspaceTitle(workspaceTitle);
        User user = userRepository.findByEmail(email);
        if (workspace == null){
            return new ResponseEntity<>( "Workspace not found", HttpStatus.NOT_FOUND);
        } else
        if (user == null){
            return new ResponseEntity<>( "User not found", HttpStatus.NOT_FOUND);
        }
        else {
            List<User> users = workspace.getUsers();
            List<Workspace> workspaces = user.getWorkspaces();
            if (users.contains(user)||workspaces.contains(workspace)){
                return new ResponseEntity<>( "User already added to workspace", HttpStatus.CONFLICT);
            }
            users.add(user);
            workspace.setUsers(users);
            workspaceRepository.save(workspace);

            workspaces.add(workspace);
            user.setWorkspaces(workspaces);
            userRepository.save(user);

            return new ResponseEntity<>("Member added successfully to the workspace", HttpStatus.OK);
        }
    }

    public ResponseEntity<String> deleteUserFromWorkspace(String workspaceTitle, String email) {
        Workspace workspace = workspaceRepository.findByWorkspaceTitle(workspaceTitle);
        User user = userRepository.findByEmail(email);

        if (workspace == null) {
            return new ResponseEntity<>("Workspace not found", HttpStatus.NOT_FOUND);
        } else if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } else {
            List<User> users = workspace.getUsers();
            List<Workspace> workspaces = user.getWorkspaces();

            if (!users.contains(user) && !workspaces.contains(workspace)) {
                return new ResponseEntity<>("User is not a member of the workspace", HttpStatus.CONFLICT);
            }

            users.remove(user);
            workspace.setUsers(users);
            workspaceRepository.save(workspace);

            workspaces.remove(workspace);
            user.setWorkspaces(workspaces);
            userRepository.save(user);

            return new ResponseEntity<>("User removed successfully from the workspace", HttpStatus.OK);
        }
    }


    public List<Workspace> getAllWorkspaces() {
        return workspaceRepository.findAll();
    }


    public WorkspaceService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


}

