package com.example.frontofficeapi.repository;

import com.example.frontofficeapi.entity.Donation;
import com.example.frontofficeapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

    List<Donation> findByUser(User user);

    List<Donation> findByBloodType(String bloodType);

    List<Donation> findByValidated(boolean validated);

    List<Donation> findByUserId(Long userId);

    // 🔴 Méthodes supprimées car dépendaient de "donation_date"
}
