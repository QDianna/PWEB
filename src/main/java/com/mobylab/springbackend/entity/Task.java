package com.mobylab.springbackend.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "tasks", schema = "project")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "tasklist_id", referencedColumnName = "id")
    private TaskList taskList;

    public UUID getId() {
        return id;
    }

    public Task setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Task setContent(String content) {
        this.content = content;
        return this;
    }

    public TaskList getTaskList() {
        return taskList;
    }

    public Task setTaskList(TaskList taskList) {
        this.taskList = taskList;
        return this;
    }

}
