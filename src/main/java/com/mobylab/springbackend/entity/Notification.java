package com.mobylab.springbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications", schema = "project")
public class Notification {

    @Id
    @GeneratedValue(strategy =  GenerationType.UUID)
    private UUID id;

    private String message;

    @Column(name = "scheduled_time")
    private LocalDateTime scheduledTime;

    @Column(name = "is_sent")
    private boolean isSent = false;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "reminder_id", referencedColumnName = "id")
    private Reminder reminder;

    public UUID getId() {
        return id;
    }

    public Notification setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Notification setMessage(String message) {
        this.message = message;
        return this;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public Notification setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
        return this;
    }

    public boolean getIsSent() {
        return isSent;
    }

    public Notification setIsSent(boolean sent) {
        isSent = sent;
        return this;
    }

    public Event getEvent() {
        return event;
    }

    public Notification setEvent(Event event) {
        this.event = event;
        return this;
    }

    public Reminder getReminder() {
        return reminder;
    }

    public Notification setReminder(Reminder reminder) {
        this.reminder = reminder;
        return this;
    }

}
