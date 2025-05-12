package com.example.frontofficeapi.repository;

import com.example.frontofficeapi.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByDonationCenterIdAndAppointmentDateTimeBetween(Long centerId, LocalDateTime start, LocalDateTime end);

    long countByDonationCenterIdAndAppointmentDateTimeBetween(Long centerId, LocalDateTime start, LocalDateTime end);

    boolean existsByDonationCenterIdAndAppointmentDateTime(Long centerId, LocalDateTime dateTime);
}