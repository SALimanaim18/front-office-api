package com.example.frontofficeapi.service;

import com.example.frontofficeapi.dto.DonationDTO;
import com.example.frontofficeapi.entity.Donation;
import com.example.frontofficeapi.exception.ResourceNotFoundException;
import com.example.frontofficeapi.mapper.DonationMapper;
import com.example.frontofficeapi.repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DonationService {

    private final DonationRepository donationRepository;
    private final DonationMapper donationMapper;

    @Autowired
    public DonationService(DonationRepository donationRepository, DonationMapper donationMapper) {
        this.donationRepository = donationRepository;
        this.donationMapper = donationMapper;
    }

    @Transactional(readOnly = true)
    public List<DonationDTO> getAllDonations() {
        List<Donation> donations = donationRepository.findAll();
        return donationMapper.toDTOList(donations);
    }

    @Transactional(readOnly = true)
    public DonationDTO getDonationById(Long id) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found with id " + id));
        return donationMapper.toDTO(donation);
    }

    @Transactional(readOnly = true)
    public List<DonationDTO> getDonationsByUserId(Long userId) {
        List<Donation> donations = donationRepository.findByUserId(userId);
        return donationMapper.toDTOList(donations);
    }

    @Transactional(readOnly = true)
    public List<DonationDTO> getDonationsByBloodType(String bloodType) {
        List<Donation> donations = donationRepository.findByBloodType(bloodType);
        return donationMapper.toDTOList(donations);
    }

    @Transactional
    public DonationDTO createDonation(DonationDTO donationDTO) {
        Donation donation = donationMapper.toEntity(donationDTO);
        Donation savedDonation = donationRepository.save(donation);
        return donationMapper.toDTO(savedDonation);
    }

    @Transactional
    public DonationDTO updateDonation(Long id, DonationDTO donationDTO) {
        if (!donationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Donation not found with id " + id);
        }

        donationDTO.setId(id);
        Donation donation = donationMapper.toEntity(donationDTO);
        Donation updatedDonation = donationRepository.save(donation);
        return donationMapper.toDTO(updatedDonation);
    }

    @Transactional
    public void deleteDonation(Long id) {
        if (!donationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Donation not found with id " + id);
        }
        donationRepository.deleteById(id);
    }

    @Transactional
    public DonationDTO validateDonation(Long id) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found with id " + id));

        donation.setValidated(true);
        Donation validatedDonation = donationRepository.save(donation);
        return donationMapper.toDTO(validatedDonation);
    }

    @Transactional(readOnly = true)
    public boolean canUserDonate(Long userId) {
        // Si aucune logique de date n'existe, on retourne true par défaut
        return true;
    }
}
