package com.example.frontofficeapi.controller;

import com.example.frontofficeapi.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/SangConnect/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final RequestRepository requestRepository;

    @GetMapping("/blood-types")
    public Map<String, Long> getBloodTypeDistribution() {
        return requestRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        request -> request.getBloodType(),
                        Collectors.counting()
                ));
    }
}