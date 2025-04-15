package com.mobylab.springbackend.service.dto;

import java.util.UUID;
import java.time.LocalDateTime;

public class ReminderDto {

    UUID id;
    private String content;
    private LocalDateTime reminderDate;

    public UUID getId() {
        return id;
    }

    public ReminderDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ReminderDto setContent(String content) {
        this.content = content;
        return this;
    }

    public LocalDateTime getReminderDate() {
        return reminderDate;
    }

    public ReminderDto setReminderDate(LocalDateTime reminderDate) {
        this.reminderDate = reminderDate;
        return this;
    }

}
