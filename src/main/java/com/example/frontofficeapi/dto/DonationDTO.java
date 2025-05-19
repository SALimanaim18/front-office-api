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
public class DonationDTO implements Serializable {
    private Long id;
    private Long userId;
    private Long requestId;
    private Long centerId;
    private String bloodType;
    private Long appointmentId;
    private Integer volumeMl;
    private boolean validated;
}