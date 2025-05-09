package com.example.frontofficeapi.service;

import com.example.frontofficeapi.dto.DonationCenterDto;
import com.example.frontofficeapi.entity.City;
import com.example.frontofficeapi.entity.DonationCenter;
import com.example.frontofficeapi.exception.ResourceNotFoundException;
import com.example.frontofficeapi.mapper.DonationCenterMapper;
import com.example.frontofficeapi.repository.DonationCenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class DonationCenterService {

    private final DonationCenterRepository donationCenterRepository;

    public List<DonationCenterDto> getAllCenters(City city, String type) {
        List<DonationCenter> centers;

        if (city != null && type != null) {
            centers = donationCenterRepository.findByCityAndType(city, type);
        } else if (city != null) {
            centers = donationCenterRepository.findByCity(city);
        } else if (type != null) {
            centers = donationCenterRepository.findByType(type);
        } else {
            centers = donationCenterRepository.findAll();
        }

        return centers.stream()
                .map(DonationCenterMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<DonationCenterDto> searchByName(String name) {
        return donationCenterRepository.findByNameContainingIgnoreCase(name).stream()
                .map(DonationCenterMapper::toDto)
                .collect(Collectors.toList());
    }

    public DonationCenterDto createCenter(DonationCenterDto dto) {
        if (dto.getId() == null && donationCenterRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("A center with this name already exists.");
        }

        DonationCenter saved = donationCenterRepository.save(DonationCenterMapper.toEntity(dto));
        return DonationCenterMapper.toDto(saved);
    }

    public DonationCenterDto getById(Long id) {
        DonationCenter center = donationCenterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation center not found with ID: " + id));

        return DonationCenterMapper.toDto(center);
    }

    public DonationCenterDto updateCenter(Long id, DonationCenterDto dto) {
        if (!donationCenterRepository.existsById(id)) {
            throw new ResourceNotFoundException("Donation center not found with ID: " + id);
        }

        dto.setId(id);
        DonationCenter updated = donationCenterRepository.save(DonationCenterMapper.toEntity(dto));
        return DonationCenterMapper.toDto(updated);
    }

    public void deleteCenter(Long id) {
        if (!donationCenterRepository.existsById(id)) {
            throw new ResourceNotFoundException("Donation center not found with ID: " + id);
        }

        donationCenterRepository.deleteById(id);
    }


    public List<DonationCenterDto> getByCity(String name) {
        return donationCenterRepository.findByCity_NameIgnoreCase(name).stream()
                .map(DonationCenterMapper::toDto)
                .collect(Collectors.toList());
    }

}
