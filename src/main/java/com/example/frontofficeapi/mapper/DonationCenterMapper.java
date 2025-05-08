package com.example.frontofficeapi.mapper;

import com.example.frontofficeapi.dto.DonationCenterDto;
import com.example.frontofficeapi.entity.DonationCenter;

public class DonationCenterMapper {

    public static DonationCenterDto toDto(DonationCenter dc) {
        return DonationCenterDto.builder()
                .id(dc.getId())
                .name(dc.getName())
                .city(dc.getCity())
                .address(dc.getAddress())
                .contactPhone(dc.getContactPhone())
                .type(dc.getType())
                .build();
    }

    public static DonationCenter toEntity(DonationCenterDto dto) {
        return DonationCenter.builder()
                .id(dto.getId())
                .name(dto.getName())
                .city(dto.getCity())
                .address(dto.getAddress())
                .contactPhone(dto.getContactPhone())
                .type(dto.getType())
                .build();
    }
}
