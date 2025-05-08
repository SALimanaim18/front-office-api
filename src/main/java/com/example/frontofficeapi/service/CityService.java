package com.example.frontofficeapi.service;

import com.example.frontofficeapi.dto.CityDto;
import com.example.frontofficeapi.entity.City;
import com.example.frontofficeapi.mapper.CityMapper;
import com.example.frontofficeapi.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    public List<CityDto> getAllCities() {
        return cityRepository.findAll()
                .stream()
                .map(CityMapper::toDto)
                .collect(Collectors.toList());
    }

    public CityDto createCity(CityDto cityDto) {
        City city = CityMapper.toEntity(cityDto);
        return CityMapper.toDto(cityRepository.save(city));
    }
}
