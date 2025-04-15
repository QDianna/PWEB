package com.mobylab.springbackend.service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class EventDto {

    private UUID id;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public UUID getId() {
        return id;
    }

    public EventDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public EventDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public EventDto setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public EventDto setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

}
