package com.example.frontofficeapi.controller;



import com.example.frontofficeapi.dto.NotificationDTO;
import com.example.frontofficeapi.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/SangConnect/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
        List<NotificationDTO> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable Long id) {
        NotificationDTO notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(notification);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByUserId(@PathVariable Long userId) {
        List<NotificationDTO> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/user/{userId}/unseen")
    public ResponseEntity<List<NotificationDTO>> getUnseenNotificationsByUserId(@PathVariable Long userId) {
        List<NotificationDTO> notifications = notificationService.getUnseenNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/user/{userId}/count-unseen")
    public ResponseEntity<Map<String, Long>> countUnseenNotifications(@PathVariable Long userId) {
        Long count = notificationService.countUnseenNotifications(userId);
        return ResponseEntity.ok(Map.of("unseenCount", count));
    }

    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(@RequestBody NotificationDTO notificationDTO) {
        NotificationDTO createdNotification = notificationService.createNotification(notificationDTO);
        return new ResponseEntity<>(createdNotification, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationDTO> updateNotification(
            @PathVariable Long id,
            @RequestBody NotificationDTO notificationDTO) {
        NotificationDTO updatedNotification = notificationService.updateNotification(id, notificationDTO);
        return ResponseEntity.ok(updatedNotification);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/mark-seen")
    public ResponseEntity<NotificationDTO> markAsSeen(@PathVariable Long id) {
        NotificationDTO notification = notificationService.markAsSeen(id);
        return ResponseEntity.ok(notification);
    }

    @PatchMapping("/user/{userId}/mark-all-seen")
    public ResponseEntity<Void> markAllAsSeenForUser(@PathVariable Long userId) {
        notificationService.markAllAsSeenForUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/user/{userId}/send")
    public ResponseEntity<Void> sendNotificationToUser(
            @PathVariable Long userId,
            @RequestBody Map<String, String> payload) {
        String message = payload.get("message");
        if (message == null || message.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        notificationService.sendNotificationToUser(userId, message);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/users/send")
    public ResponseEntity<Void> sendNotificationToUsers(@RequestBody Map<String, Object> payload) {
        @SuppressWarnings("unchecked")
        List<Long> userIds = (List<Long>) payload.get("userIds");
        String message = (String) payload.get("message");

        if (userIds == null || userIds.isEmpty() || message == null || message.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        notificationService.sendNotificationToUsers(userIds, message);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}