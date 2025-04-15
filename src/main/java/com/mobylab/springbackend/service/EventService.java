package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.entity.Event;
import com.mobylab.springbackend.entity.Notification;
import com.mobylab.springbackend.exception.BadRequestException;
import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.repository.EventRepository;
import com.mobylab.springbackend.repository.NotificationRepository;
import com.mobylab.springbackend.service.dto.EventDto;
import com.mobylab.springbackend.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventService extends BasicsService {

    private final EventRepository eventRepository;
    private final NotificationRepository notificationRepository;
    private final MailService mailService;

    public EventService(EventRepository eventRepository,
                        NotificationRepository notificationRepository,
                        MailService mailService,
                        UserRepository userRepository) {
        super(userRepository);
        this.eventRepository = eventRepository;
        this.notificationRepository = notificationRepository;
        this.mailService = mailService;
    }

    private EventDto mapToEventDto(Event event) {
        return new EventDto()
                .setId(event.getId())
                .setTitle(event.getTitle())
                .setStartDate(event.getStartDate())
                .setEndDate(event.getEndDate());
    }

    private Event mapToEvent(EventDto eventDto) {
        return new Event()
                .setTitle(eventDto.getTitle())
                .setStartDate(eventDto.getStartDate())
                .setEndDate(eventDto.getEndDate());
    }

    private void checkDtoData(EventDto dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new BadRequestException("Event title cannot be empty");
        }

        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new BadRequestException("Event must have a start and end date");
        }

        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new BadRequestException("End date cannot be before start date");
        }
    }

    // admin only
    public List<EventDto> getEventsByUserId(UUID userId) {
        userRepository.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Event> events = eventRepository.findAllByUserId(userId);

        return events.stream()
                .map(this::mapToEventDto)
                .collect(Collectors.toList());
    }

    // user friendly
    public List<EventDto> getEventsCurrentUser() {
        User user = getCurrentUser();
        return getEventsByUserId(user.getId());
    }

    // user friendly
    public EventDto getEventById(UUID eventId) {
        User currentUser = getCurrentUser();

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        checkOwnershipOrAdmin(event.getUser(), currentUser);

        return mapToEventDto(event);
    }

    // admin only
    public EventDto createEventForUserId(EventDto eventDto, UUID userId) {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return createEventForUser(eventDto, user);
    }

    // user friendly
    public EventDto createEventCurrentUser(EventDto eventDto) {
        User currentUser = getCurrentUser();

        return createEventForUser(eventDto, currentUser);
    }

    // internal - avoid double DB query
    public EventDto createEventForUser(EventDto eventDto, User user) {
        // check for valid data sent by user/admin
        checkDtoData(eventDto);

        Event event = mapToEvent(eventDto);

        // set user as current user
        event.setUser(user);

        Event savedEvent = eventRepository.save(event);

        // create notification for the event
        Notification notification = new Notification()
                .setMessage("You have this upcoming event: " + savedEvent.getTitle())
                .setScheduledTime(savedEvent.getStartDate())
                .setEvent(savedEvent);

        // send mail for the reminder
        mailService.sendMail(
                user.getEmail(),
                "Reminder creat: " + savedEvent.getTitle(),
                "Ai creat un event pentru: "
                        + savedEvent.getStartDate() + " - "
                        + savedEvent.getEndDate()
        );

        notificationRepository.save(notification);

        return mapToEventDto(savedEvent);
    }

    // user friendly
    public EventDto updateEventById(UUID eventId, EventDto eventDto) {
        // check for valid data sent by user/admin
        checkDtoData(eventDto);

        User currentUser = getCurrentUser();

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        checkOwnershipOrAdmin(event.getUser(), currentUser);

        event.setTitle(eventDto.getTitle())
                .setStartDate(eventDto.getStartDate())
                .setEndDate(eventDto.getEndDate());

        Event updatedEvent = eventRepository.save(event);
        return mapToEventDto(updatedEvent);
    }

    // user friendly
    public void deleteEventById(UUID eventId) {
        User currentUser = getCurrentUser();

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        checkOwnershipOrAdmin(event.getUser(), currentUser);

        eventRepository.delete(event);
    }

}
