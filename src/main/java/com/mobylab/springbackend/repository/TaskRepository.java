package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    @Query("""
        SELECT t FROM Task t
        WHERE t.taskList.user.id = :userId
    """)
    List<Task> findAllByUserId(@Param("userId") UUID userId);

}
