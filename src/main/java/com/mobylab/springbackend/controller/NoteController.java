package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.service.dto.NoteDto;
import com.mobylab.springbackend.service.NoteService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notes")
public class NoteController implements SecuredRestController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    // GET all notes for current user
    @GetMapping("/my_notes")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<List<NoteDto>> getNotesForCurrentUser() {
        List<NoteDto> notes = noteService.getNotesCurrentUser();
        return ResponseEntity.ok(notes);
    }

    // GET specific note by id (if owned)
    @GetMapping("/by-id/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<NoteDto> getNotesById(@PathVariable UUID id) {
        NoteDto note = noteService.getNoteById(id);
        return ResponseEntity.ok(note);
    }

    // POST create new note for current user
    @PostMapping("/create_note")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<NoteDto> createNoteForCurrentUser(@RequestBody NoteDto noteDto) {
        NoteDto created = noteService.createNoteCurrentUser(noteDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT update note by id (if owned)
    @PutMapping("/update_note/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<NoteDto> updateNoteForCurrentUser(@PathVariable UUID id, @RequestBody NoteDto noteDto) {
        NoteDto updated = noteService.updateNoteById(id, noteDto);
        return ResponseEntity.ok(updated);
    }

    // DELETE note by id (if owned)
    @DeleteMapping("/delete_note/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteNoteForCurrentUser(@PathVariable UUID id) {
        noteService.deleteNoteById(id);
        return ResponseEntity.noContent().build();
    }

    ////////////////////////////////////// ADMIN ONLY CONTROLLERS //////////////////////////////////////

    // GET all notes of a user
    @GetMapping("/by-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<NoteDto>> getNotesForUserId(@RequestParam("userId") UUID userId) {
        List<NoteDto> notes = noteService.getNotesByUserId(userId);
        return ResponseEntity.ok(notes);
    }

    // POST create new note for a user
    @PostMapping("/by-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<NoteDto> createNoteForUser(@RequestParam("userId") UUID userId, @RequestBody NoteDto noteDto) {
        NoteDto created = noteService.createNoteForUserId(noteDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
