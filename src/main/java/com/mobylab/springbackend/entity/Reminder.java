package com.mobylab.springbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "reminders", schema = "project")
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String content;

    @Column(name = "reminder_date")
    private LocalDateTime reminderDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(mappedBy = "reminder", cascade = CascadeType.ALL, orphanRemoval = true)
    private Notification notification;

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public UUID getId() {
        return id;
    }

    public Reminder setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Reminder setContent(String content) {
        this.content = content;
        return this;
    }

    public LocalDateTime getReminderDate() {
        return reminderDate;
    }

    public Reminder setReminderDate(LocalDateTime reminderDate) {
        this.reminderDate = reminderDate;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Reminder setUser(User user) {
        this.user = user;
        return this;
    }

}
