package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.service.dto.ReminderDto;
import com.mobylab.springbackend.service.ReminderService;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reminders")
public class ReminderController implements SecuredRestController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    // GET all reminders for current user
    @GetMapping("/my_reminders")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<List<ReminderDto>> getRemindersForCurrentUser(){
        List<ReminderDto> reminders = reminderService.getRemindersCurrentUser();
        return ResponseEntity.ok(reminders);
    }

    // GET specific reminder by id (if owned)
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<ReminderDto> getReminderById(@PathVariable UUID id) {
        ReminderDto reminder = reminderService.getReminderById(id);
        return ResponseEntity.ok(reminder);
    }

    // POST create new reminder for current user
    @PostMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<ReminderDto> createReminderForCurrentUser(@RequestBody ReminderDto reminderDto) {
        ReminderDto created = reminderService.createReminderCurrentUser(reminderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT update reminder by id (if owned)
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<ReminderDto> updateReminderForCurrentUser(@PathVariable UUID id, @RequestBody ReminderDto reminderDto) {
        ReminderDto updated = reminderService.updateReminderById(id, reminderDto);
        return ResponseEntity.ok(updated);
    }

    // DELETE reminder by id (if owned)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteReminderForCurrentUser(@PathVariable UUID id) {
        reminderService.deleteReminderById(id);
        return ResponseEntity.noContent().build();
    }

    ////////////////////////////////////// ADMIN ONLY CONTROLLERS //////////////////////////////////////

    // GET all reminders of a user
    @GetMapping("/by-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ReminderDto>> getRemindersForUserId(@RequestParam("userId") UUID userId) {
        List<ReminderDto> reminders = reminderService.getRemindersByUserId(userId);
        return ResponseEntity.ok(reminders);
    }

    // POST create new reminder for a user
    @PostMapping("/by-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ReminderDto> createReminderForUser(@RequestParam("userId") UUID userId, @RequestBody ReminderDto reminderDto) {
        ReminderDto created = reminderService.createReminderForUserId(reminderDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

}
