package com.example.frontofficeapi.dto;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDto implements Serializable {
    private Long id;
    private String bloodType;
    private String transfusionCenter;
    private String city;
    private String urgencyLevel;
    private String description;
    private String userName;
}
