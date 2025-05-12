package com.example.frontofficeapi.controller;

import com.example.frontofficeapi.dto.AppointmentDTO;
import com.example.frontofficeapi.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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

    // Endpoint pour récupérer les rendez-vous d'un centre à une date donnée
    @GetMapping("/center/{centerId}/date/{date}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsForDate(
            @PathVariable Long centerId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByDateAndCenter(centerId, date);
        return ResponseEntity.ok(appointments);
    }

    // Endpoint pour vérifier la disponibilité d'un créneau
    @GetMapping("/availability")
    public ResponseEntity<Boolean> isSlotAvailable(
            @RequestParam Long centerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
            @RequestParam(defaultValue = "10") int maxPerDay) {

        boolean isAvailable = appointmentService.isSlotAvailable(centerId, date, time, maxPerDay);
        return ResponseEntity.ok(isAvailable);
    }

    // Endpoint pour créer un nouveau rendez-vous
    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        AppointmentDTO createdAppointment = appointmentService.createAppointment(appointmentDTO);
        return ResponseEntity.ok(createdAppointment);
    }

    // Endpoint pour récupérer les rendez-vous sur une plage de dates
    @GetMapping("/center/{centerId}/range")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsInRange(
            @PathVariable Long centerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByDateTimeRange(centerId, start, end);
        return ResponseEntity.ok(appointments);
    }
}