package com.example.frontofficeapi.dto;

import com.example.frontofficeapi.entity.City;
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
    private Long cityId;
    private String urgencyLevel;
    private String description;
    private String userName;
    private Integer requiredBloodUnits;
    private LocalDateTime createdAt;
}
