package com.example.frontofficeapi.service;

import com.example.frontofficeapi.dto.AppointmentDTO;
import com.example.frontofficeapi.entity.*;
import com.example.frontofficeapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DonationRepository donationRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final DonationCenterRepository donationCenterRepository;

    private AppointmentDTO convertToDTO(Appointment appointment) {
        return AppointmentDTO.builder()
                .id(appointment.getId())
                .date(appointment.getAppointmentDateTime().toLocalDate())
                .time(appointment.getAppointmentDateTime().toLocalTime())
                .userId(appointment.getUser().getId())
                .requestId(appointment.getRequest().getId())
                .centerId(appointment.getDonationCenter().getId())
                .userName(appointment.getUser().getFirstName() + " " + appointment.getUser().getLastName())
                .confirmed(appointment.isConfirmed())
                .build();
    }

    private Appointment convertToEntity(AppointmentDTO dto, User user) {
        if (dto.getDate() == null || dto.getTime() == null) {
            throw new IllegalArgumentException("La date et l'heure du rendez-vous sont obligatoires.");
        }

        Appointment appointment = new Appointment();
        appointment.setId(dto.getId());
        appointment.setUser(user);

        Request request = requestRepository.findById(dto.getRequestId())
                .orElseThrow(() -> new RuntimeException("Request not found"));
        appointment.setRequest(request);

        DonationCenter center = donationCenterRepository.findById(dto.getCenterId())
                .orElseThrow(() -> new RuntimeException("Center not found"));
        appointment.setDonationCenter(center);

        LocalDateTime dateTime = LocalDateTime.of(dto.getDate(), dto.getTime());
        appointment.setAppointmentDateTime(dateTime);
        appointment.setConfirmed(dto.isConfirmed());

        return appointment;
    }

    public AppointmentDTO createAppointment(AppointmentDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        LocalDateTime dateTime = LocalDateTime.of(dto.getDate(), dto.getTime());

        // ✅ Vérifier si un rendez-vous existe déjà pour ce créneau
        boolean alreadyTaken = appointmentRepository.existsByDonationCenterIdAndAppointmentDateTime(dto.getCenterId(), dateTime);
        if (alreadyTaken) {
            throw new RuntimeException("Ce créneau est déjà réservé pour ce centre.");
        }

        Appointment appointment = convertToEntity(dto, user);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        Donation donation = Donation.builder()
                .appointment(savedAppointment)
                .user(user)
                .request(savedAppointment.getRequest())
                .donationCenter(savedAppointment.getDonationCenter())
                .bloodType(savedAppointment.getRequest().getBloodType())
                .volumeMl(450)
                .validated(false)
                .build();

        donationRepository.save(donation);

        return convertToDTO(savedAppointment);
    }

    public List<AppointmentDTO> getAppointmentsByDateAndCenter(Long centerId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        return appointmentRepository
                .findByDonationCenterIdAndAppointmentDateTimeBetween(centerId, startOfDay, endOfDay)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public boolean isSlotAvailable(Long centerId, LocalDate date, LocalTime time, int maxPerDay) {
        LocalDateTime slotDateTime = date.atTime(time);

        long count = appointmentRepository
                .countByDonationCenterIdAndAppointmentDateTimeBetween(centerId, date.atStartOfDay(), date.atTime(LocalTime.MAX));

        boolean slotTaken = appointmentRepository
                .existsByDonationCenterIdAndAppointmentDateTime(centerId, slotDateTime);

        return count < maxPerDay && !slotTaken;
    }

    public List<AppointmentDTO> getAppointmentsByDateTimeRange(Long centerId, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository
                .findByDonationCenterIdAndAppointmentDateTimeBetween(centerId, start, end)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
