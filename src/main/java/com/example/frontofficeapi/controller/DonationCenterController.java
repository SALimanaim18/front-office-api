package com.example.frontofficeapi.controller;

import com.example.frontofficeapi.dto.DonationCenterDto;
import com.example.frontofficeapi.entity.City;
import com.example.frontofficeapi.service.DonationCenterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/SangConnect/api/centers")
@RequiredArgsConstructor
public class DonationCenterController {

    private final DonationCenterService donationCenterService;

    @GetMapping
    public ResponseEntity<List<DonationCenterDto>> getAll(
            @RequestParam(required = false) City city,
            @RequestParam(required = false) String type
    ) {
        return ResponseEntity.ok(donationCenterService.getAllCenters(city, type));
    }

    @GetMapping("/search")
    public ResponseEntity<List<DonationCenterDto>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(donationCenterService.searchByName(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonationCenterDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(donationCenterService.getById(id));
    }

    @PostMapping
    public ResponseEntity<DonationCenterDto> create(@Valid @RequestBody DonationCenterDto dto) {
        return ResponseEntity.ok(donationCenterService.createCenter(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DonationCenterDto> update(@PathVariable Long id, @Valid @RequestBody DonationCenterDto dto) {
        return ResponseEntity.ok(donationCenterService.updateCenter(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        donationCenterService.deleteCenter(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-city")
    public ResponseEntity<List<DonationCenterDto>> getByCity(@RequestParam String name) {
        return ResponseEntity.ok(donationCenterService.getByCity(name));
    }




}
