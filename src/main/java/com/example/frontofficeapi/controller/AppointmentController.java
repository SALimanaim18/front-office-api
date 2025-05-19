package com.example.frontofficeapi.controller;

import com.example.frontofficeapi.dto.AppointmentDTO;
import com.example.frontofficeapi.entity.Role;
import com.example.frontofficeapi.entity.User;
import com.example.frontofficeapi.repository.UserRepository;
import com.example.frontofficeapi.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/SangConnect/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserRepository userRepository;

    @GetMapping("/today")
    public ResponseEntity<List<AppointmentDTO>> getTodaysAppointments(
            @RequestParam Long centerId,
            Authentication authentication
    ) {
        User user = userRepository.findByEmailWithCenter(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // 🔒 Vérification sécurisée
        if (user.getRole() != Role.CENTER_MANAGER ||
                user.getDonationCenter() == null ||
                !user.getDonationCenter().getId().equals(centerId)) {
            return ResponseEntity.status(403).build();
        }

        LocalDate today = LocalDate.now();
        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByDateAndCenter(centerId, today);

        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/center/{centerId}/date/{date}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsForDate(
            @PathVariable Long centerId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByDateAndCenter(centerId, date);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/availability")
    public ResponseEntity<Boolean> isSlotAvailable(
            @RequestParam Long centerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
            @RequestParam(defaultValue = "10") int maxPerDay) {

        boolean isAvailable = appointmentService.isSlotAvailable(centerId, date, time, maxPerDay);
        return ResponseEntity.ok(isAvailable);
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        AppointmentDTO createdAppointment = appointmentService.createAppointment(appointmentDTO);
        return ResponseEntity.ok(createdAppointment);
    }

    @GetMapping("/center/{centerId}/range")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsInRange(
            @PathVariable Long centerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByDateTimeRange(centerId, start, end);
        return ResponseEntity.ok(appointments);
    }
}
