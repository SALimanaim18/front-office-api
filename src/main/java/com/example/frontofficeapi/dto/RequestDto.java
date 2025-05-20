package com.example.frontofficeapi.dto;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDto implements Serializable {
    private Long id;
    private String bloodType;
    private Long donationCenter;
    private String donationCenterName;
    private Long cityId;
    private String cityName;
    private String urgencyLevel;
    private String description;
    private String userName;
    private String userPhone;
    private Integer requiredBloodUnits;
    private LocalDateTime createdAt;
    private boolean confirmedByCenterManager;
}