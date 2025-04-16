package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    @Query("""
        SELECT n FROM Notification n
        LEFT JOIN n.event e
        LEFT JOIN n.reminder r
        WHERE e.user.id = :userId OR r.user.id = :userId
    """)
    List<Notification> findAllByUserId(@Param("userId") UUID userId);

    @Query("""
        SELECT n FROM Notification n
        LEFT JOIN n.event e
        LEFT JOIN n.reminder r
        WHERE n.scheduledTime <= CURRENT_TIMESTAMP
          AND n.isSent = false
          AND (e.user.id = :userId OR r.user.id = :userId)
    """)
    List<Notification> findUnsentNotificationsForUser(@Param("userId") UUID userId);

}
