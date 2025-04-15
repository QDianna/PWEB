package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, UUID> {

    List<Note> findAllByUserId(UUID userId);
}
