package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.service.dto.EventDto;
import com.mobylab.springbackend.service.EventService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController implements SecuredRestController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // GET all events for current user
    @GetMapping("/my_events")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<List<EventDto>> getEventsForCurrentUser() {
        List<EventDto> events = eventService.getEventsCurrentUser();
        return ResponseEntity.ok(events);
    }

    // GET specific event by id (if owned)
    @GetMapping("/by-id/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<EventDto> getEventById(@PathVariable UUID id) {
        EventDto event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    // POST create new event for current user
    @PostMapping("/create_event")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<EventDto> createEventForCurrentUser(@RequestBody EventDto eventDto) {
        EventDto created = eventService.createEventCurrentUser(eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT update event by id (if owned)
    @PutMapping("/modify_event/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<EventDto> updateEventForCurrentUser(@PathVariable UUID id, @RequestBody EventDto eventDto) {
        EventDto updated = eventService.updateEventById(id, eventDto);
        return ResponseEntity.ok(updated);
    }

    // DELETE event by id (if owned)
    @DeleteMapping("/delete_event/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteEventForCurrentUser(@PathVariable UUID id) {
        eventService.deleteEventById(id);
        return ResponseEntity.noContent().build();
    }

    ////////////////////////////////////// ADMIN ONLY CONTROLLERS //////////////////////////////////////

    // GET all events of a user
    @GetMapping("/by-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<EventDto>> getEventsForUserId(@RequestParam("userId") UUID userId) {
        List<EventDto> events = eventService.getEventsByUserId(userId);
        return ResponseEntity.ok(events);
    }

    // POST create new event for a user
    @PostMapping("/by-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EventDto> createEventForUser(@RequestParam("userId") UUID userId, @RequestBody EventDto eventDto) {
        EventDto created = eventService.createEventForUserId(eventDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
