package com.mobylab.springbackend.service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class NotificationDto {

    UUID id;
    private String message;
    private LocalDateTime scheduledTime;
    private boolean isSent;

    public UUID getId() {
        return id;
    }

    public NotificationDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public NotificationDto setMessage(String message) {
        this.message = message;
        return this;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public NotificationDto setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
        return this;
    }

    public boolean getIsSent() {
        return isSent;
    }

    public NotificationDto setIsSent(boolean sent) {
        isSent = sent;
        return this;
    }

}
