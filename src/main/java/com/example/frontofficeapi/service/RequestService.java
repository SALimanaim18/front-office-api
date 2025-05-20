package com.example.frontofficeapi.service;

import com.example.frontofficeapi.dto.RequestDto;
import com.example.frontofficeapi.entity.Request;
import com.example.frontofficeapi.entity.UrgencyLevel;
import com.example.frontofficeapi.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;

    public Request saveDemande(Request request) {
        return requestRepository.save(request);
    }

    public List<RequestDto> getAll() {
        return requestRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<Request> getById(Long id) {
        return requestRepository.findById(id);
    }

    public void deleteById(Long id) {
        requestRepository.deleteById(id);
    }

    public Request confirmRequestByCenterManager(Long id) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Demande non trouvée"));
        request.setConfirmedByCenterManager(true);
        return requestRepository.save(request);
    }

    public Long countRequestsByCenter(Long centerId) {
        return requestRepository.countByDonationCenterId(centerId);
    }

    public Long countUrgentRequestsByCenter(Long centerId) {
        return requestRepository.countByDonationCenterIdAndUrgencyLevelGreaterThanEqual(centerId, UrgencyLevel.CRITIQUE);
    }

    public List<RequestDto> getLatestRequestsByCenter(Long centerId) {
        return requestRepository.findTop4ByDonationCenterIdOrderByCreatedAtDesc(centerId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<RequestDto> getRequestsByUser(Long userId) {
        return requestRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private RequestDto convertToDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .bloodType(request.getBloodType())
                .urgencyLevel(request.getUrgencyLevel().name())
                .description(request.getDescription())
                .cityId(request.getCity() != null ? request.getCity().getId() : null)
                .cityName(request.getCity() != null ? request.getCity().getName() : "Ville inconnue")
                .donationCenter(request.getDonationCenter().getId())
                .donationCenterName(request.getDonationCenter() != null ? request.getDonationCenter().getName() : "Centre inconnu")
                .userName(request.getUser().getFirstName() + " " + request.getUser().getLastName())
                .userPhone(request.getUser().getPhone())
                .requiredBloodUnits(request.getRequiredBloodUnits())
                .confirmedByCenterManager(request.isConfirmedByCenterManager())
                .createdAt(request.getCreatedAt())
                .build();
    }
}