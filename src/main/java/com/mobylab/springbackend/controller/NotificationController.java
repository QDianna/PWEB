package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.service.NotificationService;
import com.mobylab.springbackend.service.dto.NotificationDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController  implements SecuredRestController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/by-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<NotificationDto>> getByUser(@RequestParam UUID userId) {
        List<NotificationDto> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/to_send")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<NotificationDto>> getActiveNotifs() {
        return ResponseEntity.ok(notificationService.getUnsentNotificationsForCurrentUser());
    }

    @GetMapping("/reminder_notifs_by-user")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<NotificationDto>> getReminderNotifsByUser(@RequestParam UUID userId) {
        return ResponseEntity.ok(notificationService.getReminderNotifications(userId));
    }


}
