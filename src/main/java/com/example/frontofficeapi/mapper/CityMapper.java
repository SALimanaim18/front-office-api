package com.example.frontofficeapi.mapper;

import com.example.frontofficeapi.dto.CityDto;
import com.example.frontofficeapi.entity.City;

public class CityMapper {

    public static CityDto toDto(City city) {
        return new CityDto(city.getId(), city.getName());
    }

    public static City toEntity(CityDto dto) {
        return City.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}
