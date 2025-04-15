package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.entity.Reminder;
import com.mobylab.springbackend.entity.Notification;
import com.mobylab.springbackend.exception.BadRequestException;
import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.repository.ReminderRepository;
import com.mobylab.springbackend.repository.NotificationRepository;
import com.mobylab.springbackend.service.dto.ReminderDto;
import com.mobylab.springbackend.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReminderService extends BasicsService {

    private final ReminderRepository reminderRepository;
    private final NotificationRepository notificationRepository;
    private final MailService mailService;

    public ReminderService(ReminderRepository reminderRepository,
                           NotificationRepository notificationRepository,
                           UserRepository userRepository,
                           MailService mailService) {
        super(userRepository);
        this.reminderRepository = reminderRepository;
        this.notificationRepository = notificationRepository;
        this.mailService = mailService;
    }

    private ReminderDto mapToReminderDto(Reminder reminder) {
        return new ReminderDto()
                .setId(reminder.getId())
                .setContent(reminder.getContent())
                .setReminderDate(reminder.getReminderDate());
    }

    private Reminder mapToReminder(ReminderDto reminderDto) {
        return new Reminder()
                .setContent(reminderDto.getContent())
                .setReminderDate(reminderDto.getReminderDate());
    }

    private void checkDtoData(ReminderDto dto) {
        if (dto.getContent() == null) {
            throw new BadRequestException("Reminder content cannot be empty");
        }

        if (dto.getReminderDate() == null) {
            throw new BadRequestException("Reminder must have a time");
        }
    }


    // admin only
    public List<ReminderDto> getRemindersByUserId(UUID userId) {
        userRepository.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Reminder> reminders = reminderRepository.findAllByUserId(userId);

        return reminders.stream()
                .map(this::mapToReminderDto)
                .collect(Collectors.toList());
    }

    // user friendly
    public List<ReminderDto> getRemindersCurrentUser() {
        User user = getCurrentUser();
        return getRemindersByUserId(user.getId());
    }

    // user friendly
    public ReminderDto getReminderById(@PathVariable UUID reminderId) {
        User currentUser = getCurrentUser();

        Reminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new NotFoundException("Reminder not found"));

        checkOwnershipOrAdmin(reminder.getUser(), currentUser);

        return mapToReminderDto(reminder);
    }

    // admin only
    public ReminderDto createReminderForUserId(ReminderDto reminderDto, UUID userId) {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return createReminderForUser(reminderDto, user);
    }

    // user friendly
    public ReminderDto createReminderCurrentUser(ReminderDto reminderDto) {
        User currentUser = getCurrentUser();

        return createReminderForUser(reminderDto, currentUser);
    }

    // internal - avoid double DB query
    public ReminderDto createReminderForUser(ReminderDto reminderDto, User user) {
        checkDtoData(reminderDto);

        Reminder reminder = mapToReminder(reminderDto);
        reminder.setUser(user);

        Reminder savedReminder = reminderRepository.save(reminder);

        // create notification for the reminder
        Notification notification = new Notification()
                .setMessage("Reminder: " + savedReminder.getContent())
                .setScheduledTime(savedReminder.getReminderDate())
                .setReminder(savedReminder); // link to the saved reminder

        // send mail for the reminder
        mailService.sendMail(
                user.getEmail(),
                "Reminder creat: " + savedReminder.getContent(),
                "Ai creat un reminder pentru data de: " + savedReminder.getReminderDate()
        );

        notificationRepository.save(notification);

        return mapToReminderDto(savedReminder);
    }

    // user friendly
    public ReminderDto updateReminderById(UUID reminderId, ReminderDto reminderDto) {
        checkDtoData(reminderDto);

        User currentUser = getCurrentUser();

        Reminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new NotFoundException("Reminder not found"));

        checkOwnershipOrAdmin(reminder.getUser(), currentUser);

        reminder.setContent(reminderDto.getContent())
                .setReminderDate(reminderDto.getReminderDate());

        Reminder updatedReminder = reminderRepository.save(reminder);
        return mapToReminderDto(updatedReminder);
    }

    // user friendly
    public void deleteReminderById(UUID reminderId) {
        User currentUser = getCurrentUser();

        Reminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new NotFoundException("Reminder not found"));

        checkOwnershipOrAdmin(reminder.getUser(), currentUser);

        reminderRepository.delete(reminder);
    }


}
