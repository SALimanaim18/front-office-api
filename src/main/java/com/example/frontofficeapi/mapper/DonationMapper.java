package com.example.frontofficeapi.mapper;

import com.example.frontofficeapi.dto.DonationDTO;
import com.example.frontofficeapi.entity.Donation;
import com.example.frontofficeapi.entity.DonationCenter;
import com.example.frontofficeapi.entity.Request;
import com.example.frontofficeapi.entity.User;
import com.example.frontofficeapi.repository.DonationCenterRepository;
import com.example.frontofficeapi.repository.RequestRepository;
import com.example.frontofficeapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DonationMapper {

    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final DonationCenterRepository centerRepository;

    @Autowired
    public DonationMapper(UserRepository userRepository,
                          RequestRepository requestRepository,
                          DonationCenterRepository centerRepository) {
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.centerRepository = centerRepository;
    }

    public DonationDTO toDTO(Donation donation) {
        if (donation == null) {
            return null;
        }

        DonationDTO dto = new DonationDTO();
        dto.setId(donation.getId());
        dto.setUserId(donation.getUser() != null ? donation.getUser().getId() : null);
        dto.setRequestId(donation.getRequest() != null ? donation.getRequest().getId() : null);
        dto.setCenterId(donation.getDonationCenter() != null ? donation.getDonationCenter().getId() : null);
        dto.setDate(donation.getDate());
        dto.setBloodType(donation.getBloodType());
        dto.setVolumeMl(donation.getVolumeMl());
        dto.setValidated(donation.isValidated());

        return dto;
    }

    public List<DonationDTO> toDTOList(List<Donation> donations) {
        return donations.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Donation toEntity(DonationDTO dto) {
        if (dto == null) {
            return null;
        }

        Donation donation = new Donation();
        donation.setId(dto.getId());

        // Set relationships based on IDs
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));
            donation.setUser(user);
        }

        if (dto.getRequestId() != null) {
            Request request = requestRepository.findById(dto.getRequestId())
                    .orElseThrow(() -> new RuntimeException("Request not found with ID: " + dto.getRequestId()));
            donation.setRequest(request);
        }

        if (dto.getCenterId() != null) {
            DonationCenter center = centerRepository.findById(dto.getCenterId())
                    .orElseThrow(() -> new RuntimeException("Donation Center not found with ID: " + dto.getCenterId()));
            donation.setDonationCenter(center);
        }

        donation.setDate(dto.getDate());
        donation.setBloodType(dto.getBloodType());
        donation.setVolumeMl(dto.getVolumeMl());
        donation.setValidated(dto.isValidated());

        return donation;
    }
}