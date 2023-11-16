package com0.G22Trello.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.time.LocalDate;

@Entity
public class User {

    private String name;

    @Id
    private String email;

    @NotNull
    private String password;
    private String role;

    private LocalDate creationDate;
    private LocalDate lastLoginDate;
    private LocalDateTime TimeSpentLogin;

    private boolean loggedIn;


    @NotNull
    private String securityAnswer;

    @ManyToMany
    private List<Workspace> workspaces= new ArrayList<>();

    @ManyToMany
    private List<Board> boards= new ArrayList<>();


    @ManyToMany
    private List<Task> tasks= new ArrayList<>();


    public User(String name, String email,String password, String securityAnswer){
        this.name=name;
        this.email=email;
        this.password=password;
        this.securityAnswer = securityAnswer;
    }

    public User(String email, String password){
        this.email=email;
        this.password=password;
    }


    public User() {
    }



    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }
    public List<Workspace> getWorkspaces() {
        return workspaces;
    }

    public void setWorkspaces(List<Workspace> workspaces) {
        this.workspaces = workspaces;
    }


    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Board> getBoards() {
        return boards;
    }

    public void setBoards(List<Board> boards) {
        this.boards = boards;
    }


    public void setLastLoginDate(LocalDate lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

}
