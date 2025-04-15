package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    List<Event> findAllByUserId(UUID userId);
}
