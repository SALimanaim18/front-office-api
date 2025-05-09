package com.example.frontofficeapi.controller;

import com.example.frontofficeapi.dto.CityDto;
import com.example.frontofficeapi.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/SangConnect/api/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @GetMapping
    public List<CityDto> getAllCities() {
        return cityService.getAllCities();
    }

    @PostMapping
    public CityDto createCity(@RequestBody CityDto cityDto) {
        return cityService.createCity(cityDto);
    }
}
