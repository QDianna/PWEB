package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.entity.Notification;
import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.repository.NotificationRepository;
import com.mobylab.springbackend.service.dto.NotificationDto;
import com.mobylab.springbackend.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService extends BasicsService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        super(userRepository);
        this.notificationRepository = notificationRepository;
    }

    private NotificationDto mapToNotificationDto(Notification notification) {
        return new NotificationDto()
                .setId(notification.getId())
                .setMessage(notification.getMessage())
                .setScheduledTime(notification.getScheduledTime())
                .setIsSent(notification.getIsSent());
    }

    public List<NotificationDto> getNotificationsByUserId(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Notification> notifications = notificationRepository.findAllByUserId(userId);

        return notifications.stream()
                .map(this::mapToNotificationDto)
                .collect(Collectors.toList());
    }

    public List<NotificationDto> getReminderNotifications(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Notification> notifications = notificationRepository.testByReminder(userId);

        return notifications.stream()
                .map(this::mapToNotificationDto)
                .collect(Collectors.toList());
    }


    public List<NotificationDto> getUnsentNotificationsForCurrentUser() {
        User user = getCurrentUser();

        List<Notification> notifications = notificationRepository.findUnsentNotificationsForUser(user.getId());

        notifications.forEach(n -> n.setIsSent(true));
        notificationRepository.saveAll(notifications);

        return notifications.stream()
                .map(this::mapToNotificationDto)
                .collect(Collectors.toList());
    }

}
