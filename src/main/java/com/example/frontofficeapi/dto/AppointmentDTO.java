package com.example.frontofficeapi.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDTO implements Serializable {
    private Long id;
    private LocalDate date;
    private LocalTime time;
    private Long userId;
    private Long requestId;
    private Long centerId;
    private String userName;
    private boolean confirmed;
}
