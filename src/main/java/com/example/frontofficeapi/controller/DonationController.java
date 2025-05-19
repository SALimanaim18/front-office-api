package com.example.frontofficeapi.controller;

import com.example.frontofficeapi.dto.DonationDTO;
import com.example.frontofficeapi.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/SangConnect/api/donations")
public class DonationController {

    private final DonationService donationService;

    @Autowired
    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @GetMapping
    public ResponseEntity<List<DonationDTO>> getAllDonations() {
        List<DonationDTO> donations = donationService.getAllDonations();
        return ResponseEntity.ok(donations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonationDTO> getDonationById(@PathVariable Long id) {
        DonationDTO donation = donationService.getDonationById(id);
        return ResponseEntity.ok(donation);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DonationDTO>> getDonationsByUserId(@PathVariable Long userId) {
        List<DonationDTO> donations = donationService.getDonationsByUserId(userId);
        return ResponseEntity.ok(donations);
    }

    @GetMapping("/blood-type/{bloodType}")
    public ResponseEntity<List<DonationDTO>> getDonationsByBloodType(@PathVariable String bloodType) {
        List<DonationDTO> donations = donationService.getDonationsByBloodType(bloodType);
        return ResponseEntity.ok(donations);
    }

    @PostMapping
    public ResponseEntity<DonationDTO> createDonation(@RequestBody DonationDTO donationDTO) {
        DonationDTO createdDonation = donationService.createDonation(donationDTO);
        return new ResponseEntity<>(createdDonation, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DonationDTO> updateDonation(
            @PathVariable Long id,
            @RequestBody DonationDTO donationDTO) {
        DonationDTO updatedDonation = donationService.updateDonation(id, donationDTO);
        return ResponseEntity.ok(updatedDonation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonation(@PathVariable Long id) {
        donationService.deleteDonation(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/validate")
    public ResponseEntity<DonationDTO> validateDonation(@PathVariable Long id) {
        DonationDTO validatedDonation = donationService.validateDonation(id);
        return ResponseEntity.ok(validatedDonation);
    }

    @GetMapping("/can-donate/{userId}")
    public ResponseEntity<Boolean> canUserDonate(@PathVariable Long userId) {
        boolean canDonate = donationService.canUserDonate(userId);
        return ResponseEntity.ok(canDonate);
    }
}
