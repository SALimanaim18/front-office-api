package com.example.frontofficeapi.mapper;


import com.example.frontofficeapi.dto.NotificationDTO;
import com.example.frontofficeapi.entity.Notification;
import com.example.frontofficeapi.entity.User;
import com.example.frontofficeapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationMapper {

    private final UserRepository userRepository;

    @Autowired
    public NotificationMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public NotificationDTO toDTO(Notification notification) {
        if (notification == null) {
            return null;
        }

        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setUserId(notification.getUser() != null ? notification.getUser().getId() : null);
        dto.setMessage(notification.getMessage());
        dto.setSeen(notification.isSeen());
        dto.setTimestamp(notification.getTimestamp());

        return dto;
    }

    public List<NotificationDTO> toDTOList(List<Notification> notifications) {
        return notifications.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Notification toEntity(NotificationDTO dto) {
        if (dto == null) {
            return null;
        }

        Notification notification = new Notification();
        notification.setId(dto.getId());

        // Set relationship based on userId
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));
            notification.setUser(user);
        }

        notification.setMessage(dto.getMessage());
        notification.setSeen(dto.isSeen());
        notification.setTimestamp(dto.getTimestamp());

        return notification;
    }
}