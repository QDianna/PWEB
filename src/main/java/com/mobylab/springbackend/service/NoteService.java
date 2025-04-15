package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.entity.Note;
import com.mobylab.springbackend.exception.BadRequestException;
import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.repository.NoteRepository;
import com.mobylab.springbackend.service.dto.NoteDto;
import com.mobylab.springbackend.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class NoteService extends BasicsService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository, UserRepository userRepository) {
        super(userRepository);
        this.noteRepository = noteRepository;
    }

    private NoteDto mapToNoteDto(Note note) {
        return new NoteDto()
                .setId(note.getId())
                .setTitle(note.getTitle())
                .setContent(note.getContent());
    }

    private Note mapToNote(NoteDto noteDto) {
        return new Note()
                .setTitle(noteDto.getTitle())
                .setContent(noteDto.getContent());
    }

    private void checkDtoData(NoteDto dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new BadRequestException("Note title cannot be empty");
        }

        if (dto.getContent() == null) {
            throw new BadRequestException("Note content cannot be empty");
        }
    }


    // admin only
    public List<NoteDto> getNotesByUserId(UUID userId) {
        userRepository.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Note> notes = noteRepository.findAllByUserId(userId);

        return notes.stream()
                .map(this::mapToNoteDto)
                .collect(Collectors.toList());
    }

    // user friendly
    public List<NoteDto> getNotesCurrentUser() {
        User user = getCurrentUser();
        return getNotesByUserId(user.getId());
    }

    // user friendly
    public NoteDto getNoteById(UUID noteId) {
        User currentUser = getCurrentUser();

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new NotFoundException("Note not found"));

        checkOwnershipOrAdmin(note.getUser(), currentUser);

        return mapToNoteDto(note);
    }

    // admin only
    public NoteDto createNoteForUserId(NoteDto noteDto, UUID userId) {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return createNoteForUser(noteDto, user);
    }

    // user friendly
    public NoteDto createNoteCurrentUser(NoteDto noteDto) {
        User currentUser = getCurrentUser();

        return createNoteForUser(noteDto, currentUser);
    }

    // internal - avoid double DB query
    public NoteDto createNoteForUser(NoteDto noteDto, User user) {
        checkDtoData(noteDto);

        Note note = mapToNote(noteDto);
        note.setUser(user);

        Note savedNote = noteRepository.save(note);
        return mapToNoteDto(savedNote);
    }

    // user friendly
    public NoteDto updateNoteById(UUID noteId, NoteDto noteDto) {
        checkDtoData(noteDto);
        
        User currentUser = getCurrentUser();

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new NotFoundException("Note not found"));

        checkOwnershipOrAdmin(note.getUser(), currentUser);

        note.setTitle(noteDto.getTitle())
                .setContent(noteDto.getContent());

        Note updatedNote = noteRepository.save(note);
        return mapToNoteDto(updatedNote);
    }

    // user friendly
    public void deleteNoteById(UUID noteId) {
        User currentUser = getCurrentUser();

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new NotFoundException("Note not found"));

        checkOwnershipOrAdmin(note.getUser(), currentUser);

        noteRepository.delete(note);
    }

}
