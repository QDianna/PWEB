package com.mobylab.springbackend.service.dto;

import java.util.UUID;

public class TaskDto {

    private UUID id;
    private String content;
    private UUID taskListId;

    public UUID getId() {
        return id;
    }

    public TaskDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getContent() {
        return content;
    }

    public TaskDto setContent(String content) {
        this.content = content;
        return this;
    }

    public UUID getTaskListId() {
        return taskListId;
    }

    public TaskDto setTaskListId(UUID taskListId) {
        this.taskListId = taskListId;
        return this;
    }

}
