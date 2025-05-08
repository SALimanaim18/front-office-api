package com.example.frontofficeapi.dto;


import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO implements Serializable {
    private Long id;
    private Long userId;
    private String message;
    private boolean seen;
    private LocalDateTime timestamp;
}