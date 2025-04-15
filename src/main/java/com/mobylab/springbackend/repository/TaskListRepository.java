package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface TaskListRepository extends JpaRepository<TaskList, UUID> {

    List<TaskList> findAllByUserId(UUID userId);
}
