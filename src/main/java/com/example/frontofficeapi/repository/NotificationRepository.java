package com.example.frontofficeapi.repository;


import com.example.frontofficeapi.entity.Notification;
import com.example.frontofficeapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUser(User user);

    List<Notification> findByUserOrderByTimestampDesc(User user);

    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId ORDER BY n.timestamp DESC")
    List<Notification> findByUserIdOrderByTimestampDesc(@Param("userId") Long userId);

    List<Notification> findByUserAndSeenOrderByTimestampDesc(User user, boolean seen);

    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId AND n.seen = :seen ORDER BY n.timestamp DESC")
    List<Notification> findByUserIdAndSeenOrderByTimestampDesc(
            @Param("userId") Long userId,
            @Param("seen") boolean seen);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.id = :userId AND n.seen = false")
    Long countUnseenNotifications(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Notification n SET n.seen = true WHERE n.user.id = :userId")
    void markAllAsSeenByUserId(@Param("userId") Long userId);

    List<Notification> findByTimestampAfter(LocalDateTime timestamp);
}