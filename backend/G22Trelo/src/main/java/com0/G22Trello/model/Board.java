package com0.G22Trello.model;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Board {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int boardID;
    private String milestoneAndIteration;
    private String title;
    private LocalDate dateCreated;
    private Date dateToBeCompleted;




    public Board(int boardID, String milestoneAndIteration, String title, LocalDate dateCreated, Date dateToBeCompleted) {
        this.boardID = boardID;
        this.milestoneAndIteration = milestoneAndIteration;
        this.title = title;
        LocalDate today = LocalDate.now();
        this.dateCreated = today;
        this.dateToBeCompleted = dateToBeCompleted;
    }

    public Board( String milestoneAndIteration, String title,Date dateToBeCompleted) {
        this.milestoneAndIteration = milestoneAndIteration;
        this.title = title;
        this.dateCreated = LocalDate.now();;
        this.dateToBeCompleted = dateToBeCompleted;
    }

    @ManyToOne
    @JoinColumn(name = "fk_workspace_id")
    private Workspace workspace;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Task> tasks;

    @ManyToMany
    private List<User> users = new ArrayList<>();

    public Board() {

    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public int getBoardID() {
        return boardID;
    }

    public void setBoardID(int boardID) {
        this.boardID = boardID;
    }

    public String getMilestoneAndIteration() {
        return milestoneAndIteration;
    }

    public void setMilestoneAndIteration(String milestoneAndIteration) {
        this.milestoneAndIteration = milestoneAndIteration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateToBeCompleted() {
        return dateToBeCompleted;
    }

    public void setDateToBeCompleted(Date dateToBeCompleted) {
        this.dateToBeCompleted = dateToBeCompleted;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}

