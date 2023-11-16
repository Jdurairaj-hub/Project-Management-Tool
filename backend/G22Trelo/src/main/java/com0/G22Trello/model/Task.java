package com0.G22Trello.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;




@Entity
public class Task {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int taskID;
    private String userEmail;
    private int boardID;
    private String name;
    private String status;
    private int weight;

    private LocalDate creationDate;
    private LocalDate dueDate;


    @ManyToMany
    @JoinColumn(referencedColumnName = "id")
    private List<User> users = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "fk_board_id")
    private Board board;



    public Task() {

    }


    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }


    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}



