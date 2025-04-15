package com.mobylab.springbackend.entity;

import jakarta.persistence.*;
import java.util.UUID;
import java.util.List;

@Entity
@Table(name = "tasklists", schema = "project")
public class TaskList {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "taskList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

    public UUID getId() {
        return id;
    }

    public TaskList setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public TaskList setTitle(String title) {
        this.title = title;
        return this;
    }

    public User getUser() {
        return user;
    }

    public TaskList setUser(User user) {
        this.user = user;
        return this;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public TaskList setTasks(List<Task> tasks) {
        this.tasks = tasks;
        return this;
    }

}
