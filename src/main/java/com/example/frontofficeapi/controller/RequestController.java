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
        City city = cityRepository.findById(dto.getCityId())
                .orElseThrow(() -> new IllegalArgumentException("Ville non trouvée"));

        DonationCenter center = donationCenterRepository.findById(dto.getDonationCenter())
                .orElseThrow(() -> new IllegalArgumentException("Centre de don non trouvé"));

        User user = getCurrentUser();

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
    public ResponseEntity<List<RequestDto>> getAll() {
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

    @PutMapping("/{id}/confirm")
    public ResponseEntity<?> confirmRequest(@PathVariable Long id) {
        User user = getCurrentUser();

        if (user.getRole() != Role.CENTER_MANAGER) {
            return ResponseEntity.status(403).body("Accès interdit : rôle CENTER_MANAGER requis");
        }

        Request confirmed = requestService.confirmRequestByCenterManager(id);
        return ResponseEntity.ok(confirmed);
    }

    @GetMapping("/blood-types")
    public ResponseEntity<List<String>> getAllBloodTypes() {
        return ResponseEntity.ok(List.of("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"));
    }

    @GetMapping("/center/{centerId}/count")
    public ResponseEntity<Long> countRequestsByCenter(@PathVariable Long centerId) {
        return ResponseEntity.ok(requestService.countRequestsByCenter(centerId));
    }

    @GetMapping("/center/{centerId}/urgent-count")
    public ResponseEntity<Long> countUrgentRequestsByCenter(@PathVariable Long centerId) {
        return ResponseEntity.ok(requestService.countUrgentRequestsByCenter(centerId));
    }

    @GetMapping("/center/{centerId}/latest")
    public ResponseEntity<List<RequestDto>> getLatestRequestsByCenter(@PathVariable Long centerId) {
        List<RequestDto> latestRequests = requestService.getLatestRequestsByCenter(centerId);
        return ResponseEntity.ok(latestRequests);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<RequestDto>> getLatestRequestsByAuthenticatedCenter(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (user.getRole() != Role.CENTER_MANAGER || user.getDonationCenter() == null) {
            return ResponseEntity.status(403).body(null);
        }

        Long centerId = user.getDonationCenter().getId();
        List<RequestDto> latestRequests = requestService.getLatestRequestsByCenter(centerId);
        return ResponseEntity.ok(latestRequests);
    }

    @GetMapping("/user")
    public ResponseEntity<List<RequestDto>> getUserRequests() {
        User user = getCurrentUser();
        List<RequestDto> userRequests = requestService.getRequestsByUser(user.getId());
        return ResponseEntity.ok(userRequests);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            throw new IllegalStateException("Utilisateur non authentifié");
        }

        String email = (authentication.getPrincipal() instanceof UserDetails)
                ? ((UserDetails) authentication.getPrincipal()).getUsername()
                : authentication.getPrincipal().toString();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'email : " + email));
    }
}