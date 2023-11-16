package com0.G22Trello.controller;


import com0.G22Trello.model.Workspace;
import com0.G22Trello.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workspace")
public class WorkspaceController {

    @Autowired
    WorkspaceService workspaceService;
    @PostMapping("/create")
    public ResponseEntity<String> createWorkspace(@RequestParam String workspaceTitle, @RequestParam String EmailID){
        return workspaceService.createWorkspace(workspaceTitle, EmailID);
    }

    @PutMapping("/{workspaceId}")
    public ResponseEntity<String> updateWorkspace(@PathVariable("workspaceId") int workspaceId, @RequestBody Workspace workspace) {
        return workspaceService.updateWorkspace(workspaceId, workspace);
    }

    @PutMapping("/addWorkspace")
    public ResponseEntity<String> addUserToWorkspace(@RequestParam String workspaceTitle, @RequestParam String EmailID) {
        return workspaceService.addUserToWorkspace(workspaceTitle, EmailID);
    }

    @PutMapping("/removeWorkspace")
    public ResponseEntity<String> deleteUserFromWorkspace(@RequestParam String workspaceTitle, @RequestParam String EmailID) {
        return workspaceService.deleteUserFromWorkspace(workspaceTitle, EmailID);
    }

    @GetMapping("/workspaces")
    public List<Workspace> getAllWorkspaces() {
        return workspaceService.getAllWorkspaces();
    }


}