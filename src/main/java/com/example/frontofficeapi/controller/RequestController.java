package com.example.frontofficeapi.controller;

import com.example.frontofficeapi.dto.RequestDto;
import com.example.frontofficeapi.entity.*;
import com.example.frontofficeapi.repository.CityRepository;
import com.example.frontofficeapi.repository.DonationCenterRepository;
import com.example.frontofficeapi.repository.UserRepository;
import com.example.frontofficeapi.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/SangConnect/api/demandes")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;
    private final CityRepository cityRepository;
    private final UserRepository userRepository;
    private final DonationCenterRepository donationCenterRepository;

    @PostMapping
    public ResponseEntity<Request> create(@RequestBody RequestDto dto) {
        // 1. Récupérer la ville
        City city = cityRepository.findById(dto.getCityId())
                .orElseThrow(() -> new IllegalArgumentException("Ville non trouvée"));


        DonationCenter center = donationCenterRepository.findById(dto.getDonationCenter())
                .orElseThrow(() -> new IllegalArgumentException("Centre de don non trouvé"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email;

        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(403).build();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'email : " + email));

        Request request = Request.builder()
                .bloodType(dto.getBloodType())
                .urgencyLevel(UrgencyLevel.valueOf(dto.getUrgencyLevel()))
                .description(dto.getDescription())
                .city(city)
                .donationCenter(center)
                .user(user)
                .requiredBloodUnits(dto.getRequiredBloodUnits())
                .build();

        return ResponseEntity.ok(requestService.saveDemande(request));
    }

    @GetMapping
    public ResponseEntity<List<Request>> getAll() {
        return ResponseEntity.ok(requestService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Request> getById(@PathVariable Long id) {
        return requestService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        requestService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/blood-types")
    public ResponseEntity<List<String>> getAllBloodTypes() {
        return ResponseEntity.ok(List.of("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"));
    }
}
