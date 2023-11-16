package com0.G22Trello.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Workspace {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int workspaceID;
    private LocalDate creationDate;
    private String workspaceTitle;

    public Workspace(int workspaceID, String workspaceTitle) {
        this.workspaceID = workspaceID;
        this.creationDate = LocalDate.now();
        this.workspaceTitle = workspaceTitle;
    }

    public Workspace(String workspaceTitle) {
        this.creationDate = LocalDate.now();
        this.workspaceTitle = workspaceTitle;
    }

    @ManyToMany
    private List<User> users = new ArrayList<>();


    @OneToMany(mappedBy = "workspace",cascade = CascadeType.ALL)
    private List<Board> boards;




    public Workspace() {
        this.boards = new ArrayList<>();
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Board> getBoards() {
        return boards;
    }

    public void setBoards(List<Board> boards) {
        this.boards = boards;
    }


    public int getWorkspaceID() {
        return workspaceID;
    }

    public void setWorkspaceID(int workspaceID) {
        this.workspaceID = workspaceID;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getWorkspaceTitle() {
        return workspaceTitle;
    }

    public void setWorkspaceTitle(String workspaceTitle) {
        this.workspaceTitle = workspaceTitle;
    }

}

