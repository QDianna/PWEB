package com.mobylab.springbackend.service.dto;

import com.mobylab.springbackend.entity.Task;
import java.util.List;
import java.util.UUID;

public class TaskListDto {

    UUID id;
    private String title;

    public UUID getId() {
        return id;
    }

    public TaskListDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public TaskListDto setTitle(String title) {
        this.title = title;
        return this;
    }

}
