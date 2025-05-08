package com.example.frontofficeapi.service;


import com.example.frontofficeapi.dto.NotificationDTO;
import com.example.frontofficeapi.entity.Notification;
import com.example.frontofficeapi.entity.User;
import com.example.frontofficeapi.exception.ResourceNotFoundException;
import com.example.frontofficeapi.mapper.NotificationMapper;
import com.example.frontofficeapi.repository.NotificationRepository;
import com.example.frontofficeapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    @Autowired
    public NotificationService(
            NotificationRepository notificationRepository,
            UserRepository userRepository,
            NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.notificationMapper = notificationMapper;
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO> getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        return notificationMapper.toDTOList(notifications);
    }

    @Transactional(readOnly = true)
    public NotificationDTO getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id " + id));
        return notificationMapper.toDTO(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO> getNotificationsByUserId(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByTimestampDesc(userId);
        return notificationMapper.toDTOList(notifications);
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO> getUnseenNotificationsByUserId(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndSeenOrderByTimestampDesc(userId, false);
        return notificationMapper.toDTOList(notifications);
    }

    @Transactional(readOnly = true)
    public Long countUnseenNotifications(Long userId) {
        return notificationRepository.countUnseenNotifications(userId);
    }

    @Transactional
    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        if (notificationDTO.getTimestamp() == null) {
            notificationDTO.setTimestamp(LocalDateTime.now());
        }

        Notification notification = notificationMapper.toEntity(notificationDTO);
        Notification savedNotification = notificationRepository.save(notification);
        return notificationMapper.toDTO(savedNotification);
    }

    @Transactional
    public NotificationDTO updateNotification(Long id, NotificationDTO notificationDTO) {
        if (!notificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notification not found with id " + id);
        }

        notificationDTO.setId(id);
        Notification notification = notificationMapper.toEntity(notificationDTO);
        Notification updatedNotification = notificationRepository.save(notification);
        return notificationMapper.toDTO(updatedNotification);
    }

    @Transactional
    public void deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notification not found with id " + id);
        }
        notificationRepository.deleteById(id);
    }

    @Transactional
    public NotificationDTO markAsSeen(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id " + id));

        notification.setSeen(true);
        Notification updatedNotification = notificationRepository.save(notification);
        return notificationMapper.toDTO(updatedNotification);
    }

    @Transactional
    public void markAllAsSeenForUser(Long userId) {
        // Check if user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        notificationRepository.markAllAsSeenByUserId(userId);
    }

    @Transactional
    public void sendNotificationToUser(Long userId, String message) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setSeen(false);
        notification.setTimestamp(LocalDateTime.now());

        notificationRepository.save(notification);
    }

    @Transactional
    public void sendNotificationToUsers(List<Long> userIds, String message) {
        for (Long userId : userIds) {
            try {
                sendNotificationToUser(userId, message);
            } catch (ResourceNotFoundException e) {
                // Log the exception but continue processing other users
                System.err.println("Failed to send notification to user with ID " + userId + ": " + e.getMessage());
            }
        }
    }
}