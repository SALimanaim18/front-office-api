package com.example.frontofficeapi.mapper;

import com.example.frontofficeapi.dto.DonationDTO;
import com.example.frontofficeapi.entity.*;
import com.example.frontofficeapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DonationMapper {

    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final DonationCenterRepository centerRepository;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public DonationMapper(
            UserRepository userRepository,
            RequestRepository requestRepository,
            DonationCenterRepository centerRepository,
            AppointmentRepository appointmentRepository) {
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.centerRepository = centerRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public DonationDTO toDTO(Donation donation) {
        if (donation == null) return null;

        return DonationDTO.builder()
                .id(donation.getId())
                .userId(donation.getUser() != null ? donation.getUser().getId() : null)
                .requestId(donation.getRequest() != null ? donation.getRequest().getId() : null)
                .centerId(donation.getDonationCenter() != null ? donation.getDonationCenter().getId() : null)
                .bloodType(donation.getBloodType())
                .volumeMl(donation.getVolumeMl())
                .validated(donation.isValidated())
                .appointmentId(donation.getAppointment() != null ? donation.getAppointment().getId() : null)
                .build();
    }

    public List<DonationDTO> toDTOList(List<Donation> donations) {
        return donations.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Donation toEntity(DonationDTO dto) {
        if (dto == null) return null;

        Donation donation = new Donation();
        donation.setId(dto.getId());

        if (dto.getUserId() != null) {
            donation.setUser(userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId())));
        }

        if (dto.getRequestId() != null) {
            donation.setRequest(requestRepository.findById(dto.getRequestId())
                    .orElseThrow(() -> new RuntimeException("Request not found with ID: " + dto.getRequestId())));
        }

        if (dto.getCenterId() != null) {
            donation.setDonationCenter(centerRepository.findById(dto.getCenterId())
                    .orElseThrow(() -> new RuntimeException("Donation Center not found with ID: " + dto.getCenterId())));
        }

        if (dto.getAppointmentId() != null) {
            donation.setAppointment(appointmentRepository.findById(dto.getAppointmentId())
                    .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + dto.getAppointmentId())));
        }

        donation.setBloodType(dto.getBloodType());
        donation.setVolumeMl(dto.getVolumeMl());
        donation.setValidated(dto.isValidated());

        return donation;
    }
}
